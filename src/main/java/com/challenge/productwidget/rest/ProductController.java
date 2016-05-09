package com.challenge.productwidget.rest;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.challenge.productwidget.model.Product;
import com.challenge.productwidget.model.Report;
import com.challenge.productwidget.service.DataService;
import com.challenge.productwidget.service.FilterService;
import com.challenge.productwidget.service.ReportService;
import com.challenge.productwidget.util.FileUtil;

@RestController
@RequestMapping("/api/product/")
public class ProductController {
	
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private ReportService reportService;
	
	@Autowired
	private DataService<Product> productService;
	
	@Autowired
	private FilterService<Product> filterService;
	
	@RequestMapping(
			value = "all",
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<Collection<Product>> getAllProducts() {
		Collection<Product> productList = productService.findAll();
		return new ResponseEntity<Collection<Product>>(productList, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "aveWidgetProductWeek",
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<List<Report>> getAverageWidgetProductionWeek() {
		List<Report> reportList = reportService.getAverageWidgetProductionWeek();
		return new ResponseEntity<List<Report>>(reportList, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "aveWidgetProductMonth",
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<List<Report>> getAverageWidgetProductionMonth() {
		List<Report> reportList = reportService.getAverageWidgetProductionMonth();
		return new ResponseEntity<List<Report>>(reportList, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "aveWidgetProductEmployee",
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<List<Report>> getAverageWidgetProductionEmployee() {
		List<Report> reportList = reportService.getAverageWidgetProductionEmployee();
		return new ResponseEntity<List<Report>>(reportList, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "aveWidgetProductPerEmployeeWeek",
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<List<Report>> getAverageWidgetProductionPerEmployeeByWeek() {
		List<Report> reportList = reportService.getAverageWidgetProductionPerEmployeeByWeek();
		return new ResponseEntity<List<Report>>(reportList, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "employeeWithTheLowestWidgetProductAverage",
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<Double> getEmployeeWithTheLowestOverallWidgetProductionAverage() {
		Double widget = reportService.getEmployeeWithTheLowestOverallWidgetProductionAverage();
		return new ResponseEntity<Double>(widget, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "employeeWithTheHighestWidgetProductAverage",
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<Double> getEmployeeWithTheHighestOverallWidgetProductionAverage() {
		Double widget = reportService.getEmployeeWithTheHighestOverallWidgetProductionAverage();
		return new ResponseEntity<Double>(widget, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "monthWithTheLowestWidgetProductAverage",
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<Double> getMonthWithTheLowestWidgetProductionAverageForAllEmployees() {
		Double widget = reportService.getMonthWithTheLowestWidgetProductionAverageForAllEmployees();
		return new ResponseEntity<Double>(widget, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "monthWithTheHighestWidgetProductAverage",
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<Double> getMonthWithTheHighestWidgetProductionAverageForAllEmployees() {
		Double widget = reportService.getMonthWithTheHighestWidgetProductionAverageForAllEmployees();
		return new ResponseEntity<Double>(widget, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "maxWidgetProduct",
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<Product> getMaxWidgetProduction() {
		Product product = filterService.getMax();
		if (product == null) {
			return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "minWidgetProduct",
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<Product> getMinWidgetProduction() {
		Product product = filterService.getMin();
		if (product == null) {
			return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "{id}",
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<Product> getProduct(@PathVariable("id") Long id) {
		if (id == null) {
			return new ResponseEntity<Product>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Product product = productService.findOne(id);
		if (product == null) {
			return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "create",
			method = RequestMethod.POST,
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		if (product == null) {
			return new ResponseEntity<Product>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Product newProduct = productService.create(product);
		return new ResponseEntity<Product>(newProduct, HttpStatus.CREATED);
	}
	
	@RequestMapping(
			value = "upload",
			method = RequestMethod.POST,
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
	)
	public ResponseEntity<Collection<Product>> uploadProduct(@RequestParam("file") MultipartFile file, @RequestParam("name") String fileName) {
		log.info(">> Start to uploadProduct...");
		InputStream is = FileUtil.getInputStream(file);
		if (is == null) {
			return new ResponseEntity<Collection<Product>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("FileName: " + fileName);
		Collection<Product> productList = productService.uploadCsv(is, fileName);
		log.info(">> End to uploadProduct...");
		return new ResponseEntity<Collection<Product>>(productList, HttpStatus.OK);
	}
}
