package com.message.api.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.message.api.exception.MalformedMessageException;

@Service
public class MessageRecuperatorServiceImpl implements MessageRecuperatorService {

	private static final Logger logger = LogManager.getLogger(MessageRecuperatorServiceImpl.class);

	@Override
	public String retreive(String[][] messages) throws MalformedMessageException {
		logger.info("**** Retreiving message ****");
		List<String> originalMessage = new LinkedList<>();
		List<List<String>> allMessages = Arrays.stream(messages).map(Arrays::asList)
				.collect(Collectors.toList());
		logger.debug("** Amount of messages to satellites: {} **", allMessages.size());
		for (int index = 1; index <= getNumberOfUniqueWords(allMessages); index++) {
			addNextWord(originalMessage, allMessages, index);
		}
		Collections.reverse(originalMessage);
		logger.info("**** Message successfully retreived ****");
		return String.join(" ", originalMessage);
	}

	private void addNextWord(List<String> originalMessage, List<List<String>> allMessages, int index)
			throws MalformedMessageException {
		String currentWord = "";
		for (List<String> message : allMessages) {
			currentWord = getNextWordFromMessage(index, currentWord, message);
		}
		if (currentWord.trim().isEmpty()) {
			logger.error("-- At least one word is missing --");
			throw new MalformedMessageException("Malformed messages - Word is missing");
		} else {
			logger.debug("** Word successfully recovered **");
			originalMessage.add(currentWord);
		}
	}

	private String getNextWordFromMessage(int index, String currentWord, List<String> message)
			throws MalformedMessageException {
		if (message.size() >= index && !message.get(message.size() - index).trim().isEmpty()) {
			if (!currentWord.isEmpty() && !currentWord.equals(message.get(message.size() - index).trim())) {
				logger.error("-- Words do not match --");
				throw new MalformedMessageException(
						String.format("Malformed messages - Words do not match: %s is not equal to %s", currentWord,
								message.get(message.size() - index)));
			}
			currentWord = message.get(message.size() - index).trim();
		}
		return currentWord;
	}

	private int getNumberOfUniqueWords(List<List<String>> messages) {
		Set<String> uniqueWords = new HashSet<>();
		messages.stream().forEach(message -> uniqueWords.addAll(message.stream()
				.filter(word -> !word.trim().isEmpty()).map(String::trim).collect(Collectors.toSet())));
		logger.debug("** Number of unique words in phrase: {} **", uniqueWords.size());
		return uniqueWords.size();
	}

}
