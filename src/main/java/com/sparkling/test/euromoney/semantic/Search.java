package com.sparkling.test.euromoney.semantic;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.sparkling.test.euromoney.semantic.exception.SparqlException;
import com.sparkling.test.euromoney.semantic.exception.UnrecognisedQuestionException;
import com.sparkling.test.euromoney.semantic.service.SemanticService;

@Configuration()
@ImportResource("classpath:applicationContext.xml")
@EnableAutoConfiguration
@ComponentScan(basePackages="com.sparkling.test.euromoney.semantic")
public class Search {
	private static final Logger LOG = LoggerFactory.getLogger(Search.class);
	
    public static void main(String[] args) throws UnrecognisedQuestionException, SparqlException{
    	SpringApplication app = new SpringApplication(Search.class);
        app.setShowBanner(false);
        ApplicationContext applicationContext = app.run(args);
        SemanticService semanticService = applicationContext.getBean(SemanticService.class);
        
        List<String> response = semanticService.ask(args[0], Locale.UK.getLanguage());
        
        if (response.isEmpty()){
        	System.out.println("\nSorry, I don't know\n");
        }else{
        	System.out.println("\n" + response + "\n");	
        }
        System.exit(0);
    }
}
