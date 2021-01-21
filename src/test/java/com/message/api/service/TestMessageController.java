package com.message.api.service;

import java.nio.file.Files;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class TestMessageController {

	@Autowired
	MockMvc mockMvc;
	
	@Value("classpath:/requests/validRequest.json")
	Resource validRequestResource;

	@Value("classpath:/requests/emptyRequest.json")
	Resource emptyRequestResource;
	
	@Test
	void testValidRequest() throws Exception {
		String json = new String(Files.readAllBytes(this.validRequestResource.getFile().toPath()));
		mockMvc.perform(MockMvcRequestBuilders.post("/messages").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("este es un mensaje secreto")))
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	void testEmptyRequest() throws Exception {
		String json = new String(Files.readAllBytes(this.emptyRequestResource.getFile().toPath()));
		mockMvc.perform(MockMvcRequestBuilders.post("/messages").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.messages", Is.is("messages field can not be missing")))
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
	}
	
}
