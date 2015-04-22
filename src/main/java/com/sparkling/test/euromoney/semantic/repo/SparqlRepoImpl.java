package com.sparkling.test.euromoney.semantic.repo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sparql.SPARQLRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sparkling.test.euromoney.semantic.exception.ConfigurationException;
import com.sparkling.test.euromoney.semantic.exception.SparqlException;

@Service
public class SparqlRepoImpl implements SparqlRepo {
	private static final Logger LOG = LoggerFactory.getLogger(SparqlRepoImpl.class);
	
	@Value("${sparql.endpoint}")
    private String sparqlEndpointUrl;

	private static final String PREDICATE_KEY  = "__PREDICATE__";
	private static final String LANGUAGE_KEY   = "__LANGUAGE__";
	private static final String DOMAIN_KEY     = "__DOMAIN__";
	private static final String RANGE_VARIABLE = "range";
	
	@Value("${literal.property.sparql.query}")
	private String literalPropertySparqlQuery;

	protected Repository repo;

	@PostConstruct
	public void init() throws RepositoryException {
		LOG.info("Initialising Sesame SPARQL repository with URL endpoint [{}]", sparqlEndpointUrl);
		repo = new SPARQLRepository(sparqlEndpointUrl);
		repo.initialize();
	}

	/* (non-Javadoc)
	 * @see com.sparkling.test.euromoney.semantic.repo.SparqlRepo#findLiteralProperty(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> findLiteralProperty(String domain, String predicate, String language) throws SparqlException {
		String preparedQuery = literalPropertySparqlQuery
				.replaceAll(DOMAIN_KEY, domain)
				.replaceAll(PREDICATE_KEY, predicate)
				.replaceAll(LANGUAGE_KEY, language);
				
		LOG.debug("Querying endpoint [{}] with domain=[{}], predicate=[{}], and language=[{}], " +
				"so query becomes [{}]", sparqlEndpointUrl, domain, predicate, language, preparedQuery);
		
		RepositoryConnection connection = null;
		try {
			connection = repo.getConnection();
			TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, preparedQuery);
			TupleQueryResult result = query.evaluate();

			List<String> parsedResults = new ArrayList<>();
			while (result.hasNext()) {
				BindingSet bindings = result.next();
				String value = bindings.getValue(RANGE_VARIABLE).stringValue();
				parsedResults.add(value);
			}
			LOG.debug("Query [{}] returns [{}]", preparedQuery, parsedResults);
			return parsedResults;
		} catch (RepositoryException e) {
			throw new ConfigurationException("Unable to access repository [" + sparqlEndpointUrl + "]", e);
		} catch (MalformedQueryException e) {
			throw new SparqlException("Unable to parse sparql query [" + literalPropertySparqlQuery + "]", e);
		} catch (QueryEvaluationException e) {
			throw new SparqlException("Problem evaluating sparql query [" + literalPropertySparqlQuery + "]", e);
		} finally {
			if (connection != null){
				try {
					connection.close();
				} catch (RepositoryException e) {
					LOG.error("Unable to close sparql repository connection to [" + sparqlEndpointUrl + "]", e);
					//do nothing
				}
			}
		}
	}
}
