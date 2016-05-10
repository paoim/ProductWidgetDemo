package com.challenge.productwidget.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.challenge.productwidget.model.Item;
import com.challenge.productwidget.model.Product;
import com.challenge.productwidget.model.Report;
import com.challenge.productwidget.sort.ProductAscendingComparator;
import com.challenge.productwidget.sort.ProductDescendingComparator;
import com.challenge.productwidget.sort.WidgetAscendingComparator;
import com.challenge.productwidget.sort.WidgetDescendingComparator;
import com.challenge.productwidget.util.ConvertUtil;
import com.challenge.productwidget.util.DateFormatUtil;
import com.challenge.productwidget.util.FileUtil;

@Service
public class ProductServiceImpl implements DataService<Product>, FilterService<Product>, ReportService {

	private static Long nextId;
	private InputStream resourceInputStream;
	private static Map<Long, Product> productMap;
	private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	private static Product save(Product product) {
		if (productMap == null) {
			productMap = new HashMap<Long, Product>();
			nextId = new Long(1);
		}
		if (product.getId() != null && productMap.get(product.getId()) != null) {
			//Update
			productMap.remove(product.getId());
		} else {
			//Create
			product.setId(nextId);
			nextId += 1;
		}
		productMap.put(product.getId(), product);
		return product;
	}
	
	/*static {
		Product product = new Product();
		long employeeId = 1;
		double widgets = 1052;
		product.setEmployeeId(employeeId);
		product.setDate(ConvertUtil.getMMDDYYYYDate("12/5/2011"));
		product.setWidgets(widgets);
		save(product);
		
		product = new Product();
		widgets = 1442;
		product.setEmployeeId(employeeId);
		product.setDate(ConvertUtil.getMMDDYYYYDate("12/6/2011"));
		product.setWidgets(widgets);
		save(product);
	}*/
	
	@PostConstruct
	public void init() {
		log.info(">> Start to load Data from production.csv");
		Resource resource = resourceLoader.getResource("classpath:production.csv");
		resourceInputStream = FileUtil.getResourceFileInputStream(resource);
		if (resourceInputStream != null) {
			loadDataFromCsv(resourceInputStream);
		}
	}
	
	@PreDestroy
	public void preDestroy() {
		if (resourceInputStream != null) {
			try {
				resourceInputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}
	}
	
	@Override
	public Product getMax() {
		List<Product> products = getProductList();
		Collections.sort(products, new ProductDescendingComparator());
		Product product = products.get(0);
		return product;
	}

	@Override
	public Product getMin() {
		List<Product> products = getProductList();
		Collections.sort(products, new ProductAscendingComparator());
		Product product = products.get(0);
		return product;
	}

	@Override
	public Map<Long, List<Product>> groupProductByWeek() {
		log.info(">> Start on groupProductByWeek...");
		List<Product> products = getProductList();
		Map<Long, List<Product>> map = new HashMap<Long, List<Product>>();
		for (Product product : products) {
			long key = DateFormatUtil.getWeekOfYear(product.getDate());
			if (map.get(key) == null) {
				map.put(key, new ArrayList<Product>());
			} else {
				map.get(key).add(product);
			}
		}
		log.info(">> End on groupProductByWeek...");
		return map;
	}

	@Override
	public Map<Long, List<Product>> groupProductByMonth() {
		log.info(">> Start on groupProductByMonth...");
		List<Product> products = getProductList();
		Map<Long, List<Product>> map = new HashMap<Long, List<Product>>();
		for (Product product : products) {
			long key = DateFormatUtil.getMonth(product.getDate());
			if (map.get(key) == null) {
				map.put(key, new ArrayList<Product>());
			} else {
				map.get(key).add(product);
			}
		}
		log.info(">> End on groupProductByMonth...");
		return map;
	}

	@Override
	public Map<Long, List<Product>> groupProductByEmployee() {
		log.info(">> Start on groupProductByEmployee...");
		List<Product> products = getProductList();
		Map<Long, List<Product>> map = new HashMap<Long, List<Product>>();
		for (Product product : products) {
			Long key = product.getEmployeeId();
			if (map.get(key) == null) {
				map.put(key, new ArrayList<Product>());
			} else {
				map.get(key).add(product);
			}
		}
		log.info(">> End on groupProductByEmployee...");
		return map;
	}

	@Override
	public Map<Long, Map<Long, List<Product>>> groupProductPerEmployeeByWeek() {
		log.info(">> Start on groupProductPerEmployeeByWeek...");
		Map<Long, Map<Long, List<Product>>> groupProductMap = new HashMap<Long, Map<Long,List<Product>>>();
		
		// Group Product by Employee
		Map<Long, List<Product>> map = groupProductByEmployee();
		
		// Group Product Per Employee by Week
		for (Long key : map.keySet()) {
			List<Product> productList = map.get(key);
			Map<Long, List<Product>> weekMap = new HashMap<Long, List<Product>>();
			for (Product product : productList) {
				Long week = DateFormatUtil.getWeekOfYear(product.getDate());
				//log.info("EmployeeID: " + key + " Week: " + week + " from Date: " + product.getDate());
				if (weekMap.get(week) == null) {
					weekMap.put(week, new ArrayList<Product>());
				} else {
					weekMap.get(week).add(product);
				}
			}
			groupProductMap.put(key, weekMap);
		}
		log.info(">> End groupProductPerEmployeeByWeek...");
		return groupProductMap;
	}

	@Override
	public List<Report> getAverageWidgetProductionWeek() {
		Map<Long, List<Product>> map = groupProductByWeek();
		Map<Long, Double> averageMap = getAverageProductWidget(map);
		List<Report> reportList = new ArrayList<Report>();
		for (Long key : averageMap.keySet()) {
			double aveWidget = averageMap.get(key);
			Report report = new Report();
			report.setWeek(key);
			report.setAveWidget(aveWidget);
			reportList.add(report);
		}
		return reportList;
	}

	@Override
	public List<Report> getAverageWidgetProductionMonth() {
		Map<Long, List<Product>> map = groupProductByMonth();
		Map<Long, Double> averageMap = getAverageProductWidget(map);
		List<Report> reportList = new ArrayList<Report>();
		for (Long key : averageMap.keySet()) {
			double aveWidget = averageMap.get(key);
			Report report = new Report();
			report.setMonth(key);
			report.setAveWidget(aveWidget);
			reportList.add(report);
		}
		return reportList;
	}

	@Override
	public List<Report> getAverageWidgetProductionEmployee() {
		Map<Long, List<Product>> map = groupProductByEmployee();
		Map<Long, Double> averageMap = getAverageProductWidget(map);
		List<Report> reportList = new ArrayList<Report>();
		for (Long key : averageMap.keySet()) {
			double aveWidget = averageMap.get(key);
			Report report = new Report();
			report.setEmployeeId(key);
			report.setAveWidget(aveWidget);
			reportList.add(report);
		}
		return reportList;
	}

	@Override
	public List<Report> getAverageWidgetProductionPerEmployeeByWeek() {
		log.info(">> Start on getAverageWidgetProductionPerEmployeeByWeek...");
		List<Report> reportList = new ArrayList<Report>();
		
		// Group Product by Employee
		Map<Long, List<Product>> map = groupProductByEmployee();
		
		// Iterate Group Product by Employee
		for (Long key : map.keySet()) {
			// Group Product Per Employee by Week
			List<Product> productList = map.get(key);
			Map<Long, List<Product>> weekMap = new HashMap<Long, List<Product>>();
			for (Product product : productList) {
				Long week = DateFormatUtil.getWeekOfYear(product.getDate());
				if (weekMap.get(week) == null) {
					weekMap.put(week, new ArrayList<Product>());
				} else {
					weekMap.get(week).add(product);
				}
			}
			// Calculate Average of Widget by Week per Employee
			Map<Long, Double> averageMap = getAverageProductWidget(weekMap);
			for (Long week : averageMap.keySet()) {
				double aveWidget = averageMap.get(week);
				Report report = new Report();
				report.setEmployeeId(key);
				report.setWeek(week);
				report.setAveWidget(aveWidget);
				reportList.add(report);
			}
		}
		log.info(">> End getAverageWidgetProductionPerEmployeeByWeek...");
		return reportList;
	}

	@Override
	public double getEmployeeWithTheLowestOverallWidgetProductionAverage() {
		log.info(">> Start on getLowestOverallWidgetProductionAverage...");
		Map<Long, List<Product>> map = groupProductByEmployee();
		double widget = getLowestWidget(map);
		log.info(">> End getLowestOverallWidgetProductionAverage...");
		return widget;
	}

	@Override
	public double getEmployeeWithTheHighestOverallWidgetProductionAverage() {
		log.info(">> Start on getHighestOverallWidgetProductionAverage...");
		Map<Long, List<Product>> map = groupProductByEmployee();
		double widget = getHighestWidget(map);
		log.info(">> End getHighestOverallWidgetProductionAverage...");
		return widget;
	}

	@Override
	public double getMonthWithTheLowestWidgetProductionAverageForAllEmployees() {
		Map<Long, List<Product>> map = groupProductByMonth();
		double widget = getLowestWidget(map);
		return widget;
	}

	@Override
	public double getMonthWithTheHighestWidgetProductionAverageForAllEmployees() {
		Map<Long, List<Product>> map = groupProductByMonth();
		double widget = getHighestWidget(map);
		return widget;
	}

	@Override
	public Collection<Product> findAll() {
		Collection<Product> productList = productMap != null ? productMap.values() : new ArrayList<Product>();
		return productList;
	}
	
	@Override
	public Product findOne(Long id) {
		Product product = productMap != null ? productMap.get(id) : null;
		return product;
	}
	
	@Override
	public Product create(Product entity) {
		Product product = save(entity);
		return product;
	}
	
	@Override
	public Collection<Product> uploadCsv(InputStream in, String fileName) {
		log.info(">>Start on uploadCsv...");
		loadDataFromCsv(in);
		Collection<Product> productResultList = findAll();
		log.info(">>End on uploadCsv...");
		return productResultList;
	}
	
	private void loadDataFromCsv(InputStream in) {
		List<Item> items = FileUtil.readInputStreamCsv(in);
		List<Product> productList = ConvertUtil.convertItemProduct(items);
		for (Product product : productList) {
			save(product);
		}
	}
	
	private double getHighestWidget(Map<Long, List<Product>> map) {
		double aveWidget = 0.0;
		List<Double> widgetList = getWidgetList(map);
		if (widgetList.size() > 0) {
			Collections.sort(widgetList, new WidgetDescendingComparator());
			Double widget = widgetList.get(0);
			aveWidget = widget.doubleValue();
		}
		return aveWidget;
	}
	
	private double getLowestWidget(Map<Long, List<Product>> map) {
		double aveWidget = 0.0;
		List<Double> widgetList = getWidgetList(map);
		if (widgetList.size() > 0) {
			Collections.sort(widgetList, new WidgetAscendingComparator());
			Double widget = widgetList.get(0);
			aveWidget = widget.doubleValue();
		}
		
		return aveWidget;
	}
	
	private List<Product> getProductList() {
		Collection<Product> productList = findAll();
		List<Product> products = new ArrayList<Product>(productList);
		return products;
	}
	
	private List<Double> getWidgetList(Map<Long, List<Product>> map) {
		Map<Long, Double> averageMap = getAverageProductWidget(map);
		List<Double> widgetList = new ArrayList<Double>(averageMap.values());
		return widgetList;
	}
	
	private Map<Long, Double> getAverageProductWidget(Map<Long, List<Product>> map) {
		Map<Long, Double> averageMap = new HashMap<Long, Double>();
		for (Long key : map.keySet()) {
			long widget = 0;
			List<Product> productList = map.get(key);
			for (Product product : productList) {
				widget += product.getWidgets();
			}
			double averageWidget = productList.size() > 0 ? (widget / productList.size()) : widget;
			//log.info("Key[" + key + "]:[" + widget + "]=" + averageWidget);
			averageMap.put(key, averageWidget);
		}
		return averageMap;
	}
}
