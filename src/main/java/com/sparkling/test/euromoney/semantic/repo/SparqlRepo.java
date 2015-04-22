package com.sparkling.test.euromoney.semantic.repo;

import java.util.List;

import com.sparkling.test.euromoney.semantic.exception.SparqlException;

public interface SparqlRepo {

	/**
	 * Queries a SPARQL endpoint for all range literal values, given a domain and a property label
	 * @param domain The label of the resource we are enquiring about
	 * @param predicate The label of the property of the resource we are enquiring about
	 * @param language The language of the domain and predicate name label
	 * @return A list of literal values of all values of the property of the resource 
	 * we are enquiring about.
	 * @throws SparqlException
	 */
	public abstract List<String> findLiteralProperty(String domain,
			String predicate, String language) throws SparqlException;

}