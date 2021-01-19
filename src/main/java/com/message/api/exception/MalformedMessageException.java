package com.message.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class MalformedMessageException extends Exception {
	
	public MalformedMessageException(String message) {
		super(message);
	}
	
}
