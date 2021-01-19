package com.message.api.service;

import com.message.api.exception.MalformedMessageException;

public interface MessageRetreiverService {

	public String retreive(String[][] messages) throws MalformedMessageException;
	
}
