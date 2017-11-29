package fr.nantes.eni.alterplanning.config;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Created by ughostephan on 25/06/2017.
 */
@Configuration
public class EmailTemplateConfig {

    @Bean
    public VelocityEngine velocityEngine() {
        final Properties properties = new Properties();
        properties.setProperty("input.encoding", "UTF-8");
        properties.setProperty("output.encoding", "UTF-8");
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class", ClasspathResourceLoader.class.getName());
        return new VelocityEngine(properties);
    }
}
