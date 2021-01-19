package com.message.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.message.api.exception.MalformedMessageException;

@SpringBootTest
class TestMessageRetreiver {

	@Autowired
	MessageRecuperatorService messageRetreiverService;

	@Test
	void testNoPhaseShift() throws MalformedMessageException {
		String[][] messages = { { "este", " ", " ", "mensaje", "muy", " " }, { " ", "es", "un", " ", " ", " " },
				{ "este", " ", " ", "mensaje", " ", "secreto" } };
		assertEquals("este es un mensaje muy secreto", messageRetreiverService.retreive(messages));
	}

	@Test
	void testEmptyStringForUnderterminedWord() throws MalformedMessageException {
		String[][] messages = { { "este", "", "", "mensaje", "muy", "" }, { "", "es", "un", "", "", "" },
				{ "este", "", "", "mensaje", "", "secreto" } };
		assertEquals("este es un mensaje muy secreto", messageRetreiverService.retreive(messages));
	}

	@Test
	void testPhaseShiftInOneMessage() throws MalformedMessageException {
		String[][] messages = { { " ", " ", " ", " ", "este", " ", " ", "mensaje", "muy", " " },
				{ " ", "es", "un", " ", " ", " " }, { "este", " ", " ", "mensaje", " ", "secreto" } };
		assertEquals("este es un mensaje muy secreto", messageRetreiverService.retreive(messages));
	}

	@Test
	void testPhaseShiftInAllMessages() throws MalformedMessageException {
		String[][] messages = new String[][] { { " ", " ", " ", " ", "este", " ", " ", "mensaje", "muy", " " },
				{ " ", " ", "es", "un", " ", " ", " " }, { " ", " ", "este", " ", " ", "mensaje", " ", "secreto" } };
		assertEquals("este es un mensaje muy secreto", messageRetreiverService.retreive(messages));
	}

	@Test
	void testOneMalformedMessageButSuccessfulMessageRetrieval() throws MalformedMessageException {
		String[][] messages = { { " ", " ", " ", " ", "este", " ", "un", "mensaje", "muy", " " }, { " " },
				{ " ", " ", "este", "es", " ", "mensaje", " ", "secreto" } };
		assertEquals("este es un mensaje muy secreto", messageRetreiverService.retreive(messages));
	}

	@Test
	void testTwoMalformedMessageButSuccessfulMessageRetrieval() throws MalformedMessageException {
		String[][] messages = { { " ", " ", "este", "es", "un", "mensaje", "muy", "secreto" }, { " " },
				{ " ", " ", " ", " ", " " } };
		assertEquals("este es un mensaje muy secreto", messageRetreiverService.retreive(messages));
	}

	@Test
	void testOneMalformedMessageAndFailedMessageRetrieval() {
		String[][] messages = { { " ", " ", " ", " ", "este", " ", " ", "mensaje", "muy", " " }, { " " },
				{ " ", " ", "este", "es", " ", "mensaje", " ", "secreto" } };
		assertThrows(MalformedMessageException.class, () -> {
			messageRetreiverService.retreive(messages);
		});
	}

	@Test
	void testMalformedMessageWithPhaseShiftAtItsEnd() {
		String[][] messages = { { " ", " ", " ", " ", "este", " ", " ", "mensaje", "muy", " " },
				{ " ", " ", " ", " ", "este", " ", "un", "mensaje", "muy", " ", " " },
				{ " ", " ", "este", "es", " ", "mensaje", " ", "secreto" } };
		assertThrows(MalformedMessageException.class, () -> {
			messageRetreiverService.retreive(messages);
		});
	}

	@Test
	void testMalformedMessageWithWordsInWrongOrder() {
		String[][] messages = { { " ", " ", " ", " ", "este", " ", " ", "mensaje", "muy", " " },
				{ " ", " ", " ", " ", "este", " ", "mensaje", "un", "muy", " " },
				{ " ", " ", "este", "es", " ", "mensaje", " ", "secreto" } };
		assertThrows(MalformedMessageException.class, () -> {
			messageRetreiverService.retreive(messages);
		});
	}

	@Test
	void testAllMalformedAndEqualsMessages() {
		String[][] messages = { { " ", " ", " ", " ", "este", " ", " ", "mensaje", "muy", " " },
				{ " ", " ", " ", " ", "este", " ", " ", "mensaje", "muy", " " },
				{ " ", " ", " ", " ", "este", " ", " ", "mensaje", "muy", " " } };
		assertThrows(MalformedMessageException.class, () -> {
			messageRetreiverService.retreive(messages);
		});
	}

	@Test
	void testAllEqualsMessages() throws MalformedMessageException {
		String[][] messages = { { "este", "es", "un", "mensaje", "muy", "secreto" },
				{ "este", "es", "un", "mensaje", "muy", "secreto" },
				{ "este", "es", "un", "mensaje", "muy", "secreto" } };
		assertEquals("este es un mensaje muy secreto", messageRetreiverService.retreive(messages));
	}

	@Test
	void testMessagesWithLenghtOne() throws MalformedMessageException {
		String[][] messages = { { "mensaje" }, { "mensaje" }, { " " } };
		assertEquals("mensaje", messageRetreiverService.retreive(messages));
	}

	@Test
	void testWordsWithSpaces() throws MalformedMessageException {
		String[][] messages = { { "  este ", " ", " ", "         mensaje", "muy          ", " " },
				{ " ", "         es      ", "un", " ", " ", " " },
				{ "este", " ", " ", "mensaje       ", " ", "secreto" } };
		assertEquals("este es un mensaje muy secreto", messageRetreiverService.retreive(messages));
	}
}
