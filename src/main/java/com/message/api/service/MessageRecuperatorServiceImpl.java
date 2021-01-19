package com.message.api.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.message.api.exception.MalformedMessageException;

@Service
public class MessageRecuperatorServiceImpl implements MessageRecuperatorService {

	@Override
	public String retreive(String[][] messages) throws MalformedMessageException {
		List<String> originalMessage = new LinkedList<>();
		List<List<String>> allMessages = Arrays.stream(messages).map(Arrays::asList)
				.collect(Collectors.toList());
		for (int index = 1; index <= getNumberOfUniqueWords(allMessages); index++) {
			addNextWord(originalMessage, allMessages, index);
		}
		Collections.reverse(originalMessage);
		return String.join(" ", originalMessage);
	}

	private void addNextWord(List<String> originalMessage, List<List<String>> allMessages, int index)
			throws MalformedMessageException {
		String currentWord = "";
		for (List<String> message : allMessages) {
			currentWord = getNextWordFromMessage(index, currentWord, message);
		}
		if (currentWord.trim().isEmpty()) {
			throw new MalformedMessageException("Malformed messages - Word is missing");
		} else {
			originalMessage.add(currentWord);
		}
	}

	private String getNextWordFromMessage(int index, String currentWord, List<String> message)
			throws MalformedMessageException {
		if (message.size() >= index && !message.get(message.size() - index).trim().isEmpty()) {
			if (!currentWord.isEmpty() && !currentWord.equals(message.get(message.size() - index).trim())) {
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
		return uniqueWords.size();
	}

}
