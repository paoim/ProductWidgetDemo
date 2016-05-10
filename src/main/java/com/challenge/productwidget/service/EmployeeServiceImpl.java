package com.challenge.productwidget.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import com.challenge.productwidget.model.Employee;
import com.challenge.productwidget.util.ConvertUtil;
import com.challenge.productwidget.util.FileUtil;

@Service
public class EmployeeServiceImpl implements DataService<Employee> {

	private static Long nextId;
	private static Map<Long, Employee> employeeMap;
	
	private static Employee save(Employee employee) {
		if (employeeMap == null) {
			employeeMap = new HashMap<Long, Employee>();
			nextId = new Long(1);
		}
		if (employee.getId() != null && employeeMap.get(employee.getId()) != null) {
			//Update
			employeeMap.remove(employee.getId());
		} else {
			//Create
			employee.setId(nextId);
			nextId += 1;
		}
		employeeMap.put(employee.getId(), employee);
		return employee;
	}
	
	static {
		Employee employee = new Employee();
		employee.setFirstName("Demo");
		employee.setLastName("Test");
		employee.setEmail("demo@test.com");
		employee.setPhone("9523322123");
		save(employee);
	}
	
	@Override
	public Collection<Employee> findAll() {
		Collection<Employee> employeeList = employeeMap != null ? employeeMap.values() : new ArrayList<Employee>();
		return employeeList;
	}
	
	@Override
	public Employee findOne(Long id) {
		Employee employee = employeeMap != null ? employeeMap.get(id) : null;
		return employee;
	}
	
	@Override
	public Employee create(Employee entity) {
		Employee employee = save(entity);
		return employee;
	}
	
	@Override
	public Collection<Employee> uploadCsv(InputStream is, String fileName) {
		List<Row> rowList = FileUtil.readExcelRow(is, fileName);
		List<Employee> employeeList = ConvertUtil.convertExcelEmployee(rowList);
		for (Employee employee : employeeList) {
			save(employee);
		}
		Collection<Employee> employeeResultList = findAll();
		return employeeResultList;
	}
}
