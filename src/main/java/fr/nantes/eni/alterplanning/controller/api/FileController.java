package fr.nantes.eni.alterplanning.controller.api;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarConstraintEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarCoursEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.enums.CalendarState;
import fr.nantes.eni.alterplanning.dao.mysql.entity.enums.ConstraintType;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.*;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.model.form.enums.DownloadFormat;
import fr.nantes.eni.alterplanning.model.simplebean.CoursComplet;
import fr.nantes.eni.alterplanning.model.simplebean.LineCalendarGeneration;
import fr.nantes.eni.alterplanning.service.TemplateService;
import fr.nantes.eni.alterplanning.service.dao.*;
import fr.nantes.eni.alterplanning.util.AlterDateUtil;
import fr.nantes.eni.alterplanning.util.CalendarExportUtil;
import fr.nantes.eni.alterplanning.util.MediaTypeUtil;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private final static Logger LOGGER = Logger.getLogger(FileController.class);

    @Resource
    private ServletContext servletContext;

    @Resource
    private TemplateService templateService;

    @Resource
    private CalendarDAOService calendarDAOService;

    @Resource
    private CalendarConstraintDAOService calendarConstraintDAOService;

    @Resource
    private CalendarCoursDAOService calendarCoursDAOService;

    @Resource
    private CoursDAOService coursDAOService;

    @Resource
    private StagiaireDAOService stagiaireDAOService;

    @Resource
    private FormationDAOService formationDAOService;

    @Resource
    private TitreDAOService titreDAOService;

    @Resource
    private CalendarExportUtil calendarExportUtil;

    @Resource
    private EntrepriseDAOService entrepriseDAOService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(AlterDateUtil.ddMMyyyyWithSlash);

    @GetMapping(value = "/calendar/{idCalendar}/{format}", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<InputStreamResource> downloadCalendar(@PathVariable(name = "idCalendar") int id,
                                                                @PathVariable(name = "format") DownloadFormat format,
                                                                HttpServletRequest request) throws RestResponseException {
        // Find Calendar to download
        final CalendarEntity c = calendarDAOService.findById(id);

        if (c == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Calendrier non trouvé");
        } else if (c.getState() == CalendarState.DRAFT) {
            throw new RestResponseException(HttpStatus.CONFLICT, "Le calendrier est encore à l'état de brouillon, " +
                    "les cours n'ont pas encore été positionnés");
        }

        final List<CalendarConstraintEntity> constraintEntities = calendarConstraintDAOService.findByCalendarId(id);
        final List<String> idsCours = calendarCoursDAOService.findByCalendarId(id).stream()
                .map(CalendarCoursEntity::getCoursId).collect(Collectors.toList());
        final List<CoursComplet> coursComplets = coursDAOService.findAllCoursCompletsByIds(idsCours);
        coursComplets.sort(Comparator.comparing(CoursComplet::getDebut));
        final String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
        final Date startDateFirstCourse = coursComplets.get(0).getDebut();
        final Date endDateLastCourse = coursComplets.get(coursComplets.size() - 1).getFin();
        final String codeFormation = constraintEntities.stream().filter(co -> co.getConstraintType().equals(ConstraintType.AJOUT_FORMATION))
                .map(CalendarConstraintEntity::getConstraintValue)
                .findFirst().orElse(null);

        // Déclaration des variables pour le template
        String titreLong = "";
        String titreCourt = "";
        String niveau = "";
        String fileTitle = "calendrier";
        String stagiaireName = "à définir";
        String entrepriseName = "à définir";
        Date startDate = c.getStartDate();
        Date endDate = c.getEndDate();
        int dureeEnHeureFormation = coursComplets.stream().mapToInt(CoursComplet::getDureeReelleEnHeures).sum();
        final List<LineCalendarGeneration> lines = calendarExportUtil.getCalendarLines(c, coursComplets);

        // Remplissage des variables pour le template
        if (c.getStagiaireId() != null) {
            final StagiaireEntity stagiaire = stagiaireDAOService.findById(c.getStagiaireId());
            stagiaireName = stagiaire.getPrenom() + " " + stagiaire.getNom().toUpperCase();
            fileTitle += "_" + stagiaire.getNom().toLowerCase();
        }

        if (c.getEntrepriseId() != null) {
            final EntrepriseEntity entreprise = entrepriseDAOService.findById(c.getEntrepriseId());
            entrepriseName = entreprise.getRaisonSociale();
        }

        if (startDate == null || startDateFirstCourse.before(startDate)) {
            startDate = startDateFirstCourse;
        }

        if (endDate == null ||endDateLastCourse.after(endDate)) {
            endDate = endDateLastCourse;
        }

        if (codeFormation != null) {
            final FormationEntity formationEntity = formationDAOService.findById(codeFormation);
            final TitreEntity titreEntity = titreDAOService.findByFormation(codeFormation);
            titreLong = formationEntity.getLibelleLong();
            titreCourt = titreEntity.getLibelleLong();
            niveau = titreEntity.getNiveau();
        }

        BufferedWriter writer;
        File outputFile;
        InputStreamResource resource;

        try {
            // Create html template in temp folder
            File htmlFile = File.createTempFile("template_calendar_", ".html");
            LOGGER.info("create file " + htmlFile.getAbsolutePath());

            // Fill html file with velocity
            final VelocityContext context = new VelocityContext();
            context.put("logoUrl", baseUrl + "/assets/images/logo-eni.jpg");
            context.put("titreLong", titreLong.toUpperCase());
            context.put("titreCourt", titreCourt);
            context.put("niveau", niveau);
            context.put("name", stagiaireName);
            context.put("entrepriseName", entrepriseName);
            context.put("startDate", dateFormat.format(startDate));
            context.put("endDate", dateFormat.format(endDate));
            context.put("dureeEnHeureFormation", dureeEnHeureFormation);
            context.put("lines", lines);

            final String htmlContent = templateService.resolveTemplate("template-calendrier.vm", context);

            // Insert html content in html file
            writer = new BufferedWriter(new FileWriter(htmlFile));
            writer.write(htmlContent);
            writer.close();

            // Create a temp file for output
            outputFile = File.createTempFile(fileTitle + "_", "." + format.toString());
            LOGGER.info("create file " + outputFile.getAbsolutePath());

            // Convert HTML to PDF
            ConverterProperties converterProperties = new ConverterProperties();
            HtmlConverter.convertToPdf(new FileInputStream(htmlFile), new FileOutputStream(outputFile), converterProperties);

            // Create stream resource for download file
            resource = new InputStreamResource(new FileInputStream(outputFile));

            // Remove Html File
            if (htmlFile.delete()) {
                LOGGER.info("delete file " + htmlFile.getAbsolutePath());
            } else {
                LOGGER.error("Delete operation is failed for file " + htmlFile.getAbsolutePath());
            }
        } catch (IOException e){
            e.printStackTrace();
            throw new RestResponseException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        final String filename = outputFile.getName();
        final long filesize = outputFile.length();
        final MediaType mediaType = MediaTypeUtil.getMediaTypeForFileName(servletContext, filename);

        // Remove Html File
        if (outputFile.delete()) {
            LOGGER.info("delete file " + outputFile.getAbsolutePath());
        } else {
            LOGGER.error("Delete operation is failed for file " + outputFile.getAbsolutePath());
        }

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                // Content-Type
                .contentType(mediaType)
                // Contet-Length
                .contentLength(filesize)
                .body(resource);
    }

}
