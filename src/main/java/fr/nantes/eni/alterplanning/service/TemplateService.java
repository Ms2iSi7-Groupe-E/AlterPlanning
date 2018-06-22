package fr.nantes.eni.alterplanning.service;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.StringWriter;

@Service
public class TemplateService {

    private final static Logger LOGGER = Logger.getLogger(TemplateService.class);

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
        final String templateFullPath = "templates/" + templateFilename;
        StringWriter writer = new StringWriter();
        LOGGER.debug("Begin resolve template ("+ templateFullPath +") with velocity");
        velocityEngine.mergeTemplate(templateFullPath, "UTF-8", context, writer);
        LOGGER.debug("End resolving template ("+ templateFullPath +") with velocity");
        return writer.toString();
    }
}
