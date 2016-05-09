package com.challenge.productwidget.model;

public class Report {

	private Long week;
	
	private Long month;
	
	private Long employeeId;
	
	private Double aveWidget;

	public Long getWeek() {
		return week;
	}

	public void setWeek(Long week) {
		this.week = week;
	}

	public Long getMonth() {
		return month;
	}

	public void setMonth(Long month) {
		this.month = month;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Double getAveWidget() {
		return aveWidget;
	}

	public void setAveWidget(Double aveWidget) {
		this.aveWidget = aveWidget;
	}
}
