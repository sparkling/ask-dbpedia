package com.sparkling.test.euromoney.semantic.repo;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;

import com.sparkling.test.euromoney.semantic.Search;
import com.sparkling.test.euromoney.semantic.exception.SparqlException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Search.class)
public class SparqlRepoTest {

	private static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private static final String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";
	private static final String XSD_NS = "http://www.w3.org/2001/XMLSchema#";
	
	@Inject
	private SparqlRepoImpl sparqlRepo;

	@Before
	public void setUp() throws RepositoryException {
		Repository testRepo = new SailRepository(new MemoryStore());
		testRepo.initialize();
		sparqlRepo.repo = testRepo;
		populateSesame(testRepo);
	}

	private void populateSesame(Repository testRepo) throws RepositoryException {
		RepositoryConnection con = testRepo.getConnection();
		ValueFactory f = testRepo.getValueFactory();

		con.setNamespace("rdf",  RDF_NS);
		con.setNamespace("rdfs", RDFS_NS);
		con.setNamespace("xsd",  XSD_NS);
		con.setNamespace("xsd",  XSD_NS);

		URI alice      = f.createURI("http://example.org/people/alice");
		URI bob        = f.createURI("http://example.org/people/bob");
		URI person     = f.createURI("http://example.org/ontology/Person");
		URI label      = f.createURI(RDFS_NS, "label");
		URI birthplace = f.createURI("http://example.org/ontology/birthplace");

		Literal bobsName        = f.createLiteral("Bob", "en");
		Literal alicesName      = f.createLiteral("Alice", "en");
		Literal birthplaceLabel = f.createLiteral("birth place", "en");
		Literal londonLabel     = f.createLiteral("London", "en");

		try {
			con.add(birthplace, label, birthplaceLabel);
			con.add(alice, RDF.TYPE, person);
			con.add(alice, label, alicesName);
			con.add(alice, birthplace, londonLabel);
			con.add(bob, RDF.TYPE, person);
			con.add(bob, label, bobsName);
			con.add(bob, birthplace, londonLabel);
		} finally {
			con.close();
		}
	}

	@Test
	public void shouldReturnLiteralProperty() throws SparqlException {
		List<String> results = sparqlRepo.findLiteralProperty("Bob",
				"birth place", Locale.UK.getLanguage());
		assertEquals(1, results.size());
		assertEquals("London", results.get(0));
	}
}
