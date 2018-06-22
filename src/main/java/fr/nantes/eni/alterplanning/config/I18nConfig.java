package fr.nantes.eni.alterplanning.config;

import fr.nantes.eni.alterplanning.util.AlterDateUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Configuration
public class I18nConfig {
    @Bean
    public SessionLocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(new Locale("fr"));
        return resolver;
    }

    @PostConstruct
    public void initTimezone(){
        TimeZone.setDefault(TimeZone.getTimeZone(AlterDateUtil.timezone));
        System.out.println("Spring boot application running in (" + AlterDateUtil.timezone + ") timezone :"
                + new Date());
    }
}
