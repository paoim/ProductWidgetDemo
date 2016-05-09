/********************************************
** Author: Pao Im
** Email: paoim@yahoo.com
*********************************************/
//Create Employee Controller
productWidgetApp.controller("EmployeeController", function($scope, $modal, $log, $timeout, pageService, employeeService, inputFileService) {
	//console.log("EmployeeController...");
	var newPage = {
		isDetailPage : false,
		isUploadExcelFile : false,//flag to show or hide upload button
		title : "Employees List",
		createLabel : "New Employee",
		createUrl : "employee/newID",
		uploadLabel : "Click to upload Employees List"
	},
	loadEmployeeList = function() {
		//Show Animation
		$scope.$emit('LOADPAGE');
		
		//Pagination configure
		$scope.curPage = 0;
		$scope.pageSize = 200;
		$scope.employeeList = [];
		$scope.numberOfPages = function() {
			return 1;
		};
		
		employeeService.loadEmployeeList(function(employeeList, message) {
			$scope.employeeList = employeeList;
			$scope.numberOfPages = function() {
				return Math.ceil($scope.employeeList.length / $scope.pageSize);
			};
			
			//Hide Animation
			$scope.$emit('UNLOADPAGE');
		});
	};
	
	//Init Data
	loadEmployeeList();
	
	//Upload Excel file
	var doNewAction = function() {
		$timeout(function() {
			var fileSelector = inputFileService.getSelectorById("fileElement");
			inputFileService.loadFileDialog(fileSelector);
			
			inputFileService.addFileSelectedListener(fileSelector, function() {
				if (this.files && this.files.length > 0) {
					var file = this.files[0],
					fileName = file.name,
					fileSize = parseInt(file.size / 1024),
					requestData = {
						fileId : 0,
						fileRequest : file,
						fileName : fileName,
						fileSize : file.size
					};
					console.log("Upload file's size: " + fileSize + "KB");
					
					if (fileName.indexOf("Employee") > -1) {
						//Show Animation
						$scope.$emit('LOADPAGE');
						
						//Pagination configure
						$scope.curPage = 0;
						$scope.pageSize = 200;
						$scope.employeeList = [];
						$scope.numberOfPages = function() {
							return 1;
						};
						
						employeeService.uploadEmployee(requestData, function(employeeList, message) {
							$scope.employeeList = employeeList;
							$scope.numberOfPages = function() {
								return Math.ceil($scope.employeeList.length / $scope.pageSize);
							};
							
							//Hide Animation
							$scope.$emit('UNLOADPAGE');
						});
					} else {
						alert("Your file is not Employee Excel file!");
					}
				}
				
				//clear input file after loading file dialog
				inputFileService.clearFileInput(this);
			});
		}, 0, false);
	};
	
	pageService.setPage(newPage);
	pageService.setClick(doNewAction);
});

//Create Employee Detail Controller
productWidgetApp.controller("EmployeeDetailController", function($scope, $routeParams, $location, $timeout, pageService, employeeService, utilService, inputFileService) {
	var employeeId = utilService.getId($routeParams.employeeId),
	employee = {id : employeeId},
	createLabel = "Save Employee",
	isUpdateEmployee = false,
	isCreateNew = true;
	
	//Make sure it is old data, then make update flag as true
	if (utilService.isNumber(employeeId)) {
		isCreateNew = false;
		isUpdateEmployee = true;
	}
	
	if (isUpdateEmployee) {
		//Get Employee by Employee ID
		(function() {
			employeeService.loadEmployee(employeeId, function(employee, message) {
				$scope.employee = employee;
			});
		})();//auto execute function
	} else {
		//New Data
		$scope.employee = employee;
	}
	
	var newPage = {
		isDetailPage : true,
		title : "Employee Detail",
		isCreateNew : isCreateNew,
		createLabel : createLabel,
		isReportPage : isUpdateEmployee,
		isDisplaySaveBtn : isUpdateEmployee
	},
	doNewAction = function() {
		var employeeList = employeeService.getEmployeeList(),
		newEmployeeId = employeeList.length + 1,
		newEmployee = {
			id : isUpdateEmployee ? employeeId : newEmployeeId,
			firstName : $scope.employee.firstName,
			lastName : $scope.employee.lastName,
			email : $scope.employee.email,
			phone : $scope.employee.phone
		};
		//console.log(newEmployee);
		
		if (isUpdateEmployee) {
		} else {
			//createContactPost
			employeeService.createEmployee(newEmployee, function(data, message) {
				alert(message);
				$location.path("/employee");//redirect to employeeList page
			});
		}
	};
	
	pageService.setPage(newPage);
	pageService.setClick(doNewAction);
});
