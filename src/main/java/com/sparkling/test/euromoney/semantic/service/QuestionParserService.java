package com.sparkling.test.euromoney.semantic.service;

import com.sparkling.test.euromoney.semantic.domain.Question;
import com.sparkling.test.euromoney.semantic.exception.UnrecognisedQuestionException;

public interface QuestionParserService {

	/**
	 * A very simple and slightly silly implementation of a question parser. Preferable, we would 
	 * build a grammar or create a DSL for this purpose instead.
	 * 
	 * @param question A string of the format "what is the [predicate label] of [domain label]"
	 * @param lang The language of the query String provided
	 * @return Extracts the domain and predicate label from the query String, as well as the language
	 * @throws UnrecognisedQuestionException If we are unable to parse the question
	 */
	public abstract Question parseQuestion(String question, String lang)
			throws UnrecognisedQuestionException;

}