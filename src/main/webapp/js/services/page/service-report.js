/********************************************
** Author: Pao Im
** Email: paoim@yahoo.com
*********************************************/
// Create Report Service
productWidgetServices.factory("reportService", function($rootScope, dataService) {
	// Private Methods
	var getMaxWidgetProduct = function(callbackHandler) {
		//dataService.setBaseUrl("http://localhost:8088/api/");
		dataService.getEntities("product/maxWidgetProduct", function(data) {
			callbackHandler(data, "Load MaxWidgetProduct Successfully...");
		},function(error) {
			callbackHandler([], "Cannot Load MaxWidgetProduct - " + error.message);
		}, true);
	},
	getMinWidgetProduct = function(callbackHandler) {
		dataService.getEntities("product/minWidgetProduct", function(data) {
			callbackHandler(data, "Load MinWidgetProduct Successfully...");
		},function(error) {
			callbackHandler([], "Cannot Load MinWidgetProduct - " + error.message);
		}, true);
	},
	getAveWidgetProductWeek = function(callbackHandler) {
		dataService.getEntities("product/aveWidgetProductWeek", function(data) {
			callbackHandler(data, "Load AveWidgetProductWeek Successfully...");
		},function(error) {
			callbackHandler([], "Cannot Load AveWidgetProductWeek - " + error.message);
		}, true);
	},
	getAveWidgetProductMonth = function(callbackHandler) {
		dataService.getEntities("product/aveWidgetProductMonth", function(data) {
			callbackHandler(data, "Load AveWidgetProductMonth Successfully...");
		},function(error) {
			callbackHandler([], "Cannot Load AveWidgetProductMonth - " + error.message);
		}, true);
	},
	getAveWidgetProductEmployee = function(callbackHandler) {
		dataService.getEntities("product/aveWidgetProductEmployee", function(data) {
			callbackHandler(data, "Load AveWidgetProductEmployee Successfully...");
		},function(error) {
			callbackHandler([], "Cannot Load AveWidgetProductEmployee - " + error.message);
		}, true);
	},
	getAveWidgetProductPerEmployeeWeek = function(callbackHandler) {
		dataService.getEntities("product/aveWidgetProductPerEmployeeWeek", function(data) {
			callbackHandler(data, "Load AveWidgetProductPerEmployeeWeek Successfully...");
		},function(error) {
			callbackHandler([], "Cannot Load AveWidgetProductPerEmployeeWeek - " + error.message);
		}, true);
	},
	getEmployeeWithTheLowestWidgetProduct = function(callbackHandler) {
		dataService.getEntities("product/employeeWithTheLowestWidgetProductAverage", function(data) {
			callbackHandler(data, "Load EmployeeWithTheLowestWidgetProduct Successfully...");
		},function(error) {
			callbackHandler([], "Cannot Load EmployeeWithTheLowestWidgetProduct - " + error.message);
		}, true);
	},
	getEmployeeWithTheHighestWidgetProduct = function(callbackHandler) {
		dataService.getEntities("product/employeeWithTheHighestWidgetProductAverage", function(data) {
			callbackHandler(data, "Load EmployeeWithTheHighestWidgetProduct Successfully...");
		},function(error) {
			callbackHandler([], "Cannot Load EmployeeWithTheHighestWidgetProduct - " + error.message);
		}, true);
	},
	getMonthWithTheLowestWidgetProduct = function(callbackHandler) {
		dataService.getEntities("product/monthWithTheLowestWidgetProductAverage", function(data) {
			callbackHandler(data, "Load MonthWithTheLowestWidgetProduct Successfully...");
		},function(error) {
			callbackHandler([], "Cannot Load MonthWithTheLowestWidgetProduct - " + error.message);
		}, true);
	},
	getMonthWithTheHighestWidgetProduct = function(callbackHandler) {
		dataService.getEntities("product/monthWithTheHighestWidgetProductAverage", function(data) {
			callbackHandler(data, "Load MonthWithTheHighestWidgetProduct Successfully...");
		},function(error) {
			callbackHandler([], "Cannot Load MonthWithTheHighestWidgetProduct - " + error.message);
		}, true);
	};
	
	// public methods
	return {
		getMaxWidgetProduct : getMaxWidgetProduct,
		getMinWidgetProduct : getMinWidgetProduct,
		getAveWidgetProductWeek : getAveWidgetProductWeek,
		getAveWidgetProductMonth : getAveWidgetProductMonth,
		getAveWidgetProductEmployee : getAveWidgetProductEmployee,
		getMonthWithTheLowestWidgetProduct : getMonthWithTheLowestWidgetProduct,
		getAveWidgetProductPerEmployeeWeek : getAveWidgetProductPerEmployeeWeek,
		getMonthWithTheHighestWidgetProduct : getMonthWithTheHighestWidgetProduct,
		getEmployeeWithTheLowestWidgetProduct : getEmployeeWithTheLowestWidgetProduct,
		getEmployeeWithTheHighestWidgetProduct : getEmployeeWithTheHighestWidgetProduct
	};
});
