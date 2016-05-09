package com.challenge.productwidget.rest;

import java.io.InputStream;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.challenge.productwidget.model.Hour;
import com.challenge.productwidget.service.DataService;
import com.challenge.productwidget.util.FileUtil;

@RestController
@RequestMapping("/api/hour/")
public class HourController {

	@Autowired
	private DataService<Hour> hourService;
	
	@RequestMapping(
			value = "all",
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<Collection<Hour>> getAllHours() {
		Collection<Hour> hourList = hourService.findAll();
		return new ResponseEntity<Collection<Hour>>(hourList, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "{id}",
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<Hour> getHour(@PathVariable("id") Long id) {
		if (id == null) {
			return new ResponseEntity<Hour>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Hour hour = hourService.findOne(id);
		if (hour == null) {
			return new ResponseEntity<Hour>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Hour>(hour, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "create",
			method = RequestMethod.POST,
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<Hour> createHour(@RequestBody Hour hour) {
		if (hour == null) {
			return new ResponseEntity<Hour>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Hour newHour = hourService.create(hour);
		return new ResponseEntity<Hour>(newHour, HttpStatus.CREATED);
	}
	
	@RequestMapping(
			value = "upload",
			method = RequestMethod.POST,
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<Collection<Hour>> uploadHour(@RequestParam("file") MultipartFile file, @RequestParam("name") String fileName) {
		InputStream is = FileUtil.getInputStream(file);
		if (is == null) {
			return new ResponseEntity<Collection<Hour>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Collection<Hour> hourList = hourService.uploadCsv(is, fileName);
		return new ResponseEntity<Collection<Hour>>(hourList, HttpStatus.OK);
	}
}
