package com.sparkling.test.euromoney.semantic.service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sparkling.test.euromoney.semantic.domain.LiteralPropertyQuestion;
import com.sparkling.test.euromoney.semantic.domain.Question;
import com.sparkling.test.euromoney.semantic.exception.UnrecognisedQuestionException;

@Service
public class SimpleQuestionParserService implements QuestionParserService {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleQuestionParserService.class);
    
    /* (non-Javadoc)
	 * @see com.sparkling.test.euromoney.semantic.service.QuestionParserService#parseQuestion(java.lang.String, java.lang.String)
	 */
    @Override
	public Question parseQuestion(String question, String lang) throws UnrecognisedQuestionException{
    	LOG.debug("Parsing question [{}]", question);
    	
    	//remove any trailing question marks
    	String originalQuestion = question;
    	if (question.trim().endsWith("?")){
    		question = question.trim().substring(0, question.length() - 1).trim();
    	}
    	
    	String[] words = question.split("\\s+");
    	
    	if (words.length < 6){
    		throw new UnrecognisedQuestionException("Less than 6 words in [" + originalQuestion + "]");
    	}
		
    	//The first 3 words are "what is the", "where are the", etc.
    	//so we skip these
    	StringBuffer predicateName = new StringBuffer(20);
    	int index = 3;
    	for (; index < words.length -1; index++){
    		//the sentence is of the for 'What is the [predicate] of [domain]
    		if (Objects.equals(words[index], "of")){
    			break;
    		}
    		predicateName.append(words[index]).append(' ');
    	}
    	
    	if (predicateName.length() == 0){
    		throw new UnrecognisedQuestionException("The fourth word in the question was 'of' (not allowed) for query [" + question + "]");
    	}
    	
    	//we need at least 2 word tokens left: 'of [domain name]'
    	if (index > words.length - 2){
    		throw new UnrecognisedQuestionException("The word 'of' was not found in [" + originalQuestion + "]");
    	}
    	
    	//Skip the 'of' word
    	index++;
    	
    	//then collect the remainder of the string for the domain name 
    	StringBuffer domainName = new StringBuffer(20);
    	for (; index < words.length; index++){
    		domainName.append(words[index]).append(' ');
    	}
    	
    	LiteralPropertyQuestion qst = LiteralPropertyQuestion.getBuilder(
				domainName.toString().trim(), predicateName.toString().trim(), lang).build();
    	
    	
    	LOG.debug("Parsing question [{}] gives us [{}]", originalQuestion, qst);
    	return qst;
    }

}
