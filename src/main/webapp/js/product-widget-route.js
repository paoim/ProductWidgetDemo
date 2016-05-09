/********************************************
** Author: Pao Im
** Email: paoim@yahoo.com
*********************************************/
productWidgetApp.config(function($routeProvider, $locationProvider){
	$routeProvider.
	when("/report",
	{
		controller : "ReportController",
		templateUrl : "view/report.html"
	}).
	when("/products",
	{
		controller : "ProductsController",
		templateUrl : "view/products.html"
	}).
	when("/hours",
	{
		controller : "HoursController",
		templateUrl : "view/hours.html"
	}).
	when("/employee",
	{
		controller : "EmployeeController",
		templateUrl : "view/employee.html"
	}).
	when("/products/:productId", {
		controller : "ProductDetailController",
		templateUrl : "view/productDetail.html"
	}).
	when("/hours/:hourId", {
		controller : "HourDetailController",
		templateUrl : "view/hourDetail.html"
	}).
	when("/employee/:employeeId", {
		controller : "EmployeeDetailController",
		templateUrl : "view/employeeDetail.html"
	}).
	otherwise({
		redirectTo : "/report"
	});
	
	// configure html5 to get links working on jsfiddle
	//$locationProvider.html5Mode(true);
});
