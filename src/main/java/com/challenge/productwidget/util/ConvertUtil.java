package com.challenge.productwidget.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import com.challenge.productwidget.model.Employee;
import com.challenge.productwidget.model.Hour;
import com.challenge.productwidget.model.Item;
import com.challenge.productwidget.model.Product;

public class ConvertUtil {
	
	public static Item convertCells(String[] cells) {
		Item item = new Item();
		if (cells.length > 0) {
			item.setFirst(cells[0]);
			item.setSecond(cells[1]);
			item.setThird(cells[2]);
		}
		return item;
	}
	
	public static List<Product> convertItemProduct(List<Item> items) {
		List<Product> products = new ArrayList<Product>();
		for (Item item : items) {
			Product product = new Product();
			if (EntityUtil.isValidString(item.getFirst())) {
				long employeeId = getLongId(item.getFirst());
				product.setEmployeeId(employeeId);
			}
			if (EntityUtil.isValidString(item.getSecond())) {
				product.setDate(ConvertUtil.getMMDDYYYYDate(item.getSecond()));
			}
			double widgets = getDoubleValue(item.getThird());
			product.setWidgets(widgets);
			products.add(product);
		}
		return products;
	}
	
	public static List<Product> convertExcelProduct(List<Row> rowList) {
		List<Product> products = new ArrayList<Product>();
		for (Row row : rowList) {
			Product product = new Product();
			Cell cell = row.getCell(0);
			long employeeId = getCellLongValue(cell);
			product.setEmployeeId(employeeId);
			
			cell = row.getCell(1);
			String date = getCellStringValue(cell);
			if (EntityUtil.isValidString(date)) {
				product.setDate(ConvertUtil.getMMDDYYYYDate(date));
			}
			
			cell = row.getCell(2);
			double widgets = getCellDoubleValue(cell);
			product.setWidgets(widgets);
			
			products.add(product);
		}
		return products;
	}
	
	public static List<Hour> convertItemHour(List<Item> items) {
		List<Hour> hours = new ArrayList<Hour>();
		for (Item item : items) {
			Hour hour = new Hour();
			if (EntityUtil.isValidString(item.getFirst())) {
				long employeeId = getLongId(item.getFirst());
				hour.setEmployeeId(employeeId);
				
				if (EntityUtil.isValidString(item.getSecond())) {
					hour.setStartTime(ConvertUtil.getMMDDYYYYHHMMDate(item.getSecond()));
				}
				
				if (EntityUtil.isValidString(item.getThird())) {
					hour.setEndTime(ConvertUtil.getMMDDYYYYHHMMDate(item.getThird()));
				}
			}
			hours.add(hour);
		}
		return hours;
	}
	
	public static List<Hour> convertExcelHour(List<Row> rowList) {
		List<Hour> hours = new ArrayList<Hour>();
		for (Row row : rowList) {
			Hour hour = new Hour();
			Cell cell = row.getCell(0);
			long employeeId = getCellLongValue(cell);
			hour.setEmployeeId(employeeId);
			
			cell = row.getCell(1);
			String startTime = getCellStringValue(cell);
			if (EntityUtil.isValidString(startTime)) {
				hour.setStartTime(ConvertUtil.getMMDDYYYYHHMMDate(startTime));
			}
			
			cell = row.getCell(2);
			String endTime = getCellStringValue(cell);
			if (EntityUtil.isValidString(endTime)) {
				hour.setEndTime(ConvertUtil.getMMDDYYYYHHMMDate(endTime));
			}
			hours.add(hour);
		}
		return hours;
	}

	public static List<Employee> convertExcelEmployee(List<Row> rowList) {
		List<Employee> employeeList = new ArrayList<Employee>();
		for (Row row : rowList) {
			Employee employee = new Employee();
			Cell cell = row.getCell(0);
			long employeeId = getCellLongValue(cell);
			if (employeeId > 0) {
				//employee.setId(employeeId);
				
				cell = row.getCell(1);
				String firstName = getCellStringValue(cell);
				if (EntityUtil.isValidString(firstName)) {
					employee.setFirstName(firstName);
				}
				
				cell = row.getCell(2);
				String lastName = getCellStringValue(cell);
				if (EntityUtil.isValidString(lastName)) {
					employee.setLastName(lastName);
				}
				
				cell = row.getCell(3);
				String email = getCellStringValue(cell);
				if (EntityUtil.isValidString(email)) {
					employee.setEmail(email);
				}
				
				cell = row.getCell(4);
				String phone = getCellStringValue(cell);
				if (EntityUtil.isValidString(phone)) {
					employee.setPhone(phone);
				}
				
				employeeList.add(employee);
			}
		}
		return employeeList;
	}

	public static String getCellStringValue(Cell cell) {
		String cellString = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				cellString = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				cellString = "" + cell.getNumericCellValue();
				break;
			}
		}

		// remove space
		if (EntityUtil.isValidString(cellString)) {
			cellString = cellString.trim();
		}
		return cellString;
	}

	public static Date getCellDateValue(Cell cell) {
		Date date = null;
		if (cell != null) {
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				if (DateUtil.isCellDateFormatted(cell)) {
					date = cell.getDateCellValue();
				}
			} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				String strCell = cell.getStringCellValue();
				date = getMMDDYYYYHHMMSSADate(strCell);
				if (date == null) {
					date = getMMDDYYYYDate(strCell);
				}
			}

		}
		return date;
	}

	public static long getCellLongValue(Cell cell) {
		long cellLong = -1;
		if (cell != null && Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			cellLong = (long) cell.getNumericCellValue();
		}
		return cellLong;
	}
	
	public static double getCellDoubleValue(Cell cell) {
		double cellDouble = 0.0;
		if (cell != null && Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			cellDouble = (double) cell.getNumericCellValue();
		}
		return cellDouble;
	}
	
	public static double getDoubleValue(String data) {
		double cellDouble = 0.0;
		if (EntityUtil.isValidString(data)) {
			cellDouble = Double.parseDouble(data);
		}
		return cellDouble;
	}

	public static long getLongId(String data) {
		long longId = -1;
		try {
			longId = Long.parseLong(data);
		} catch (Exception e) {
			longId = -1;
		}
		return longId;
	}

	public static Date getYYYYMMDDDate(String strDate) {
		DateFormatUtil dateFormate = new DateFormatUtil(DateFormatUtil.DAS_YYYYMMDD);
		return dateFormate.getDateFormat(strDate);
	}

	public static Date getMMDDYYYYDate(String strDate) {
		DateFormatUtil dateFormate = new DateFormatUtil(DateFormatUtil.SLAS_MMDDYYYY);
		return dateFormate.getDateFormat(strDate);
	}

	public static Date getMMDDYYYYHHMMDate(String strDate) {
		DateFormatUtil dateFormate = new DateFormatUtil(DateFormatUtil.SLAS_MMDDYYYYHHMM);
		return dateFormate.getDateFormat(strDate);
	}

	public static Date getMMDDYYYYHHMMSSADate(String strDate) {
		DateFormatUtil dateFormate = new DateFormatUtil(DateFormatUtil.SLAS_MMDDYYYYHHMMSSA);
		return dateFormate.getDateFormat(strDate);
	}
}
