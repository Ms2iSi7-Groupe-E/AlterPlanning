package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.model.form.enums.DownloadFormat;
import fr.nantes.eni.alterplanning.service.dao.CalendarDAOService;
import fr.nantes.eni.alterplanning.util.MediaTypeUtil;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Resource
    private ServletContext servletContext;

    @Resource
    private CalendarDAOService calendarDAOService;

    @GetMapping("/calendar/{idCalendar}/{format}")
    public ResponseEntity<InputStreamResource> downloadCalendar(@PathVariable(name = "idCalendar") int id,
                                                                @PathVariable(name = "format") DownloadFormat format) throws RestResponseException {
        // Find Calendar to download
        final CalendarEntity c = calendarDAOService.findById(id);

        if (c == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Calendar not found");
        }

        // TODO code for download PDF of a calendar
        final String str = "Hello World";

        File file;
        InputStreamResource resource;

        try {
            //create a temp file
            file = File.createTempFile("test", "." + format.toString());
            // Write in file
            FileWriter writer = new FileWriter(file);
            writer.write(str);
            writer.close();
            //create stream
            resource = new InputStreamResource(new FileInputStream(file));
            System.out.println(file.getName());
        } catch (IOException e){
            e.printStackTrace();
            throw new RestResponseException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        final String filename = file.getName();
        final MediaType mediaType = MediaTypeUtil.getMediaTypeForFileName(servletContext, filename);

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                // Content-Type
                .contentType(mediaType)
                // Contet-Length
                .contentLength(file.length())
                .body(resource);
    }

}
