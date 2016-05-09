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

import com.challenge.productwidget.model.Employee;
import com.challenge.productwidget.service.DataService;
import com.challenge.productwidget.util.FileUtil;

@RestController
@RequestMapping("/api/employee/")
public class EmployeeController {

	@Autowired
	private DataService<Employee> employeeService;
	
	@RequestMapping(
			value = "all",
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<Collection<Employee>> getAllEmployee() {
		Collection<Employee> employeeList = employeeService.findAll();
		return new ResponseEntity<Collection<Employee>>(employeeList, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "{id}",
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<Employee> getEmployee(@PathVariable("id") Long id) {
		if (id == null) {
			return new ResponseEntity<Employee>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Employee employee = employeeService.findOne(id);
		if (employee == null) {
			return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "create",
			method = RequestMethod.POST,
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
		if (employee == null) {
			return new ResponseEntity<Employee>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Employee newEmployee = employeeService.create(employee);
		return new ResponseEntity<Employee>(newEmployee, HttpStatus.CREATED);
	}
	
	@RequestMapping(
			value = "upload",
			method = RequestMethod.POST,
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<Collection<Employee>> uploadEmployee(@RequestParam("file") MultipartFile file, @RequestParam("name") String fileName) {
		InputStream is = FileUtil.getInputStream(file);
		if (is == null) {
			return new ResponseEntity<Collection<Employee>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Collection<Employee> employeeList = employeeService.uploadCsv(is, fileName);
		return new ResponseEntity<Collection<Employee>>(employeeList, HttpStatus.OK);
	}
}
