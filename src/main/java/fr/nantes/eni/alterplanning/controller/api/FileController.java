package fr.nantes.eni.alterplanning.controller.api;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.StagiaireEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.model.form.enums.DownloadFormat;
import fr.nantes.eni.alterplanning.service.TemplateService;
import fr.nantes.eni.alterplanning.service.dao.CalendarDAOService;
import fr.nantes.eni.alterplanning.service.dao.StagiaireDAOService;
import fr.nantes.eni.alterplanning.util.MediaTypeUtil;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
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
import java.io.*;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private static Logger logger = Logger.getLogger(FileController.class);

    @Resource
    private ServletContext servletContext;

    @Resource
    private TemplateService templateService;

    @Resource
    private CalendarDAOService calendarDAOService;

    @Resource
    private StagiaireDAOService stagiaireDAOService;

    @GetMapping(value = "/calendar/{idCalendar}/{format}", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<InputStreamResource> downloadCalendar(@PathVariable(name = "idCalendar") int id,
                                                                @PathVariable(name = "format") DownloadFormat format) throws RestResponseException {
        // Find Calendar to download
        final CalendarEntity c = calendarDAOService.findById(id);

        if (c == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Calendar not found");
        }

        String fileTitle = "calendrier";

        String stagiaireName = "";
        if (c.getStagiaireId() != null) {
            final StagiaireEntity stagiaire = stagiaireDAOService.findById(c.getStagiaireId());
            stagiaireName = stagiaire.getPrenom() + " " + stagiaire.getPrenom();
            fileTitle += "_" + stagiaire.getNom().toLowerCase();
        }

        BufferedWriter writer;
        File outputFile;
        InputStreamResource resource;

        try {
            // Create html template in temp folder
            File htmlFile = File.createTempFile("template_calendar_", ".html");
            logger.info("create file " + htmlFile.getAbsolutePath());

            // Fill html file with velocity
            final VelocityContext context = new VelocityContext();
            context.put("name", stagiaireName);
            final String htmlContent = templateService.resolveTemplate("template-calendrier.vm", context);

            // Insert html content in html file
            writer = new BufferedWriter(new FileWriter(htmlFile));
            writer.write(htmlContent);
            writer.close();

            // Create a temp file for output
            outputFile = File.createTempFile(fileTitle + "_", "." + format.toString());
            logger.info("create file " + outputFile.getAbsolutePath());

            // Convert HTML to PDF
            ConverterProperties converterProperties = new ConverterProperties();
            HtmlConverter.convertToPdf(new FileInputStream(htmlFile), new FileOutputStream(outputFile), converterProperties);

            // Create stream resource for download file
            resource = new InputStreamResource(new FileInputStream(outputFile));

            // Remove Html File
            if (htmlFile.delete()) {
                logger.info(htmlFile.getAbsolutePath() + " is deleted!");
            } else {
                logger.error("Delete operation is failed for file " + htmlFile.getAbsolutePath());
            }
        } catch (IOException e){
            e.printStackTrace();
            throw new RestResponseException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        final String filename = outputFile.getName();
        final Long filesize = outputFile.length();
        final MediaType mediaType = MediaTypeUtil.getMediaTypeForFileName(servletContext, filename);

        // Remove Html File
        if (outputFile.delete()) {
            logger.info(outputFile.getAbsolutePath() + " is deleted!");
        } else {
            logger.error("Delete operation is failed for file " + outputFile.getAbsolutePath());
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
