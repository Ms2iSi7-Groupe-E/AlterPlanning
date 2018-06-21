package fr.nantes.eni.alterplanning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
public class AlterPlanningApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(AlterPlanningApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AlterPlanningApplication.class);
	}

	@PostConstruct
	public void initTimezone(){
		final String timezone = "Europe/Paris";
		TimeZone.setDefault(TimeZone.getTimeZone(timezone));
		System.out.println("Spring boot application running in (" + timezone + ") timezone :"
				+ new Date());
	}
}
