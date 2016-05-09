package com.challenge.productwidget.service;

import java.util.List;
import java.util.Map;

import com.challenge.productwidget.model.Product;
import com.challenge.productwidget.model.Report;

public interface ReportService {

	List<Report> getAverageWidgetProductionWeek();
	
	Map<Long, List<Product>> groupProductByWeek();
	
	List<Report> getAverageWidgetProductionMonth();
	
	Map<Long, List<Product>> groupProductByMonth();
	
	Map<Long, List<Product>> groupProductByEmployee();
	
	List<Report> getAverageWidgetProductionEmployee();
	
	List<Report> getAverageWidgetProductionPerEmployeeByWeek();
	
	double getEmployeeWithTheLowestOverallWidgetProductionAverage();
	
	double getEmployeeWithTheHighestOverallWidgetProductionAverage();
	
	Map<Long, Map<Long, List<Product>>> groupProductPerEmployeeByWeek();
	
	double getMonthWithTheLowestWidgetProductionAverageForAllEmployees();
	
	double getMonthWithTheHighestWidgetProductionAverageForAllEmployees();
}
