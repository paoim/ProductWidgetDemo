package com.challenge.productwidget.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class HelloController {

	@RequestMapping(
			value = "hello",
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<String> getHello() {
		String hello = "Hello, welcome to API!";
		return new ResponseEntity<String>(hello, HttpStatus.OK);
	}
}
