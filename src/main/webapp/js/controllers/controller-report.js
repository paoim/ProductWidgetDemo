/********************************************
** Author: Pao Im
** Email: paoim@yahoo.com
*********************************************/
//Create Product Controller
productWidgetApp.controller("ReportController", function($scope, $modal, $log, $timeout, pageService, reportService, inputFileService) {
	var newPage = {
			isReportPage: true,
			isDetailPage : false,
			isUploadExcelFile : false,//flag to show or hide upload button
			title : "Statistics Report",
			createLabel : "N/A",
			createUrl : "N/A",
			uploadLabel : "N/A"
	};
	
	(function() {
		reportService.getEmployeeWithTheLowestWidgetProduct(function(data, message) {
			$scope.employeeWithTheLowestWidgetProduct = data;
		});
	})();
	
	(function() {
		reportService.getEmployeeWithTheHighestWidgetProduct(function(data, message) {
			$scope.employeeWithTheHighestWidgetProduct = data;
		});
	})();
	
	(function() {
		reportService.getMonthWithTheLowestWidgetProduct(function(data, message) {
			$scope.monthWithTheLowestWidgetProduct = data;
		});
	})();
	
	(function() {
		reportService.getMonthWithTheHighestWidgetProduct(function(data, message) {
			$scope.monthWithTheHighestWidgetProduct = data;
		});
	})();
	
	(function() {
		reportService.getAveWidgetProductPerEmployeeWeek(function(data, message) {
			$scope.aveWidgetProductPerEmployeeWeek = data;
			console.log(data);
		});
	})();
	
	var doNewAction = function() {
		//NA
	};
	
	pageService.setPage(newPage);
	pageService.setClick(doNewAction);
});
