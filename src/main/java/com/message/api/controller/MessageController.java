package com.message.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.message.api.exception.MalformedMessageException;
import com.message.api.service.MessageRetreiverService;
import com.quasar.api.core.request.MessageRequest;
import com.quasar.api.core.response.MessageResponse;

@RestController
public class MessageController {

	@Autowired
	private MessageRetreiverService messageRetreiverService;

	@PostMapping(value = "/message", consumes = "application/json", produces = "application/json")
	public MessageResponse message(@RequestBody MessageRequest messageRequest) throws MalformedMessageException {
		return new MessageResponse(messageRetreiverService.retreive(messageRequest.getMessages()));
	}

}
