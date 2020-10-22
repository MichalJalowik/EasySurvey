package easysurvey;

import easysurvey.persistence.SurveyService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SurveyService surveyService = new SurveyService();
		surveyService.close();
		SpringApplication.run(App.class, args);
	}


}


