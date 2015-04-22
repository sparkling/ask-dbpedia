package com.sparkling.test.euromoney.semantic.repo;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sparkling.test.euromoney.semantic.Search;
import com.sparkling.test.euromoney.semantic.domain.LiteralPropertyQuestion;
import com.sparkling.test.euromoney.semantic.domain.Question;
import com.sparkling.test.euromoney.semantic.exception.UnrecognisedQuestionException;
import com.sparkling.test.euromoney.semantic.service.QuestionParserService;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Search.class)
public class SimpleQuestionParserServiceTest {

	@Inject
	private QuestionParserService simpleQuestionParserService;

	@Before
	public void setUp() {
		
	}

	@Test
	public void shouldReturnLiteralPropertyQuestion() throws UnrecognisedQuestionException {
		LiteralPropertyQuestion expected = LiteralPropertyQuestion.getBuilder(
				"David Cameron", "birth place", "en").build();

		Question found = simpleQuestionParserService.parseQuestion("What is the birth place of David Cameron?", "en");
		
		assertTrue(found instanceof LiteralPropertyQuestion);
		assertEquals(expected, found);
	}
}
