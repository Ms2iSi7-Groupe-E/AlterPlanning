package fr.nantes.eni.alterplanning.service;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.StringWriter;

@Service
public class TemplateService {

    @Resource
    private VelocityEngine velocityEngine;

    /**
     * Resolve template string.
     *
     * @param templateFilename the template file name
     * @param context      the context
     * @return the string
     */
    public String resolveTemplate(final String templateFilename, final VelocityContext context) {
        StringWriter writer = new StringWriter();
        velocityEngine.mergeTemplate("templates/" + templateFilename, "UTF-8", context, writer);
        return writer.toString();
    }
}
