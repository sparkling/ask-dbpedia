package com.sparkling.test.euromoney.semantic.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sparkling.test.euromoney.semantic.domain.LiteralPropertyQuestion;
import com.sparkling.test.euromoney.semantic.domain.Question;
import com.sparkling.test.euromoney.semantic.exception.SparqlException;
import com.sparkling.test.euromoney.semantic.exception.UnrecognisedQuestionException;
import com.sparkling.test.euromoney.semantic.repo.SparqlRepo;

@Service
public class SemanticService {
    private static final Logger LOG = LoggerFactory.getLogger(SemanticService.class);
    
    @Inject
    private QuestionParserService questionParserService;
    
    @Inject
    private SparqlRepo sparqlRepo;
    
    @Autowired
    public SemanticService(QuestionParserService questionParserService){
    	this.questionParserService = questionParserService;
    }
    
    public List<String> ask(String question, String lang) throws UnrecognisedQuestionException, SparqlException{
    	Question q = questionParserService.parseQuestion(question, lang);
    	
    	//ugh... This switch needs to be refactored, but will do for now.
    	if (q instanceof LiteralPropertyQuestion){
    		return handleLiteralPropertyQuestion(question, q);
    	}else{
    		throw new UnsupportedOperationException("You forgot to handle question class " + q.getClass().getName());
    	}
    }

	private List<String> handleLiteralPropertyQuestion(String question, 
			Question q) throws SparqlException{
		LiteralPropertyQuestion lpq = (LiteralPropertyQuestion) q;
		List<String> results = sparqlRepo.findLiteralProperty(lpq.getDomainName(), lpq.getPredicateName(), lpq.getLanguage());
		LOG.debug("For question [{}] we get answer [{}]", question, results);
		return results;
	}
}
