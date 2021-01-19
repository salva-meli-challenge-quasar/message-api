package com.message.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.message.api.exception.MalformedMessageException;
import com.message.api.service.MessageRecuperatorService;
import com.quasar.api.core.request.MessageRequest;
import com.quasar.api.core.response.MessageResponse;

@RestController
public class MessageController {

	@Autowired
	private MessageRecuperatorService messageRetreiverService;

	@PostMapping(value = "/messages", consumes = "application/json", produces = "application/json")
	public MessageResponse message(@Valid @RequestBody MessageRequest messageRequest) throws MalformedMessageException {
		return new MessageResponse(messageRetreiverService.retreive(messageRequest.getMessages()));
	}

}
