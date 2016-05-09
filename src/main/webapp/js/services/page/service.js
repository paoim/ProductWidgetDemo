/********************************************
** Author: Pao Im
** Email: paoim@yahoo.com
*********************************************/
var productWidgetServices = angular.module("productWidgetServices", ["genericDataService", "inputFileModule", "genericUtilService", "genericDatePickerService"]);

//Create Page Service
productWidgetServices.factory("pageService", function($rootScope){
	//private variable
	var page = {
		title : "N/A",
		createUrl : "N/A",
		createLabel : "N/A",
		uploadLabel : "N/A",
		isCreateNew : false,
		isReportPage : false,
		isDetailPage : false,
		isDisplaySaveBtn : false,
		isUploadExcelFile : false
	},
	
	//private method
	doAction = function(){
		//Will implement on detail page
	};
	
	return {
		//public functions
		getPage : function(){
			return page;
		},
		setPage : function(newPage){
			page = newPage;
		},
		doClick : function(){
			doAction();
		},
		setClick : function(doNewAction){
			doAction = doNewAction;
		}
	};
});
