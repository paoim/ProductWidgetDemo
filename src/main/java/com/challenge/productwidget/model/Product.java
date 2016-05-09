package com.challenge.productwidget.model;

import java.util.Date;

public class Product {

	private Long id;
	private Date date;
	private Double widgets;
	private Long employeeId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Double getWidgets() {
		return widgets;
	}
	public void setWidgets(Double widgets) {
		this.widgets = widgets;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
}
