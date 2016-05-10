/********************************************
** Author: Pao Im
** Email: paoim@yahoo.com
*********************************************/
//Create Hour Controller
productWidgetApp.controller("HoursController", function($scope, $modal, $log, $timeout, pageService, hourService, inputFileService) {
	var newPage = {
		isDetailPage : false,
		isUploadExcelFile : true,//flag to show or hide upload button
		title : "Hours List",
		createLabel : "New Hour",
		createUrl : "hours/newID",
		uploadLabel : "Click to upload Hours"
	},
	loadHourList = function() {
		//Show Animation
		$scope.$emit('LOADPAGE');
		
		//Pagination configure
		$scope.curPage = 0;
		$scope.pageSize = 200;
		$scope.hoursList = [];
		$scope.numberOfPages = function() {
			return 1;
		};
		
		hourService.loadHours(function(hoursList, message) {
			$scope.hoursList = hoursList;
			$scope.numberOfPages = function() {
				return Math.ceil($scope.hoursList.length / $scope.pageSize);
			};
			
			//Hide Animation
			$scope.$emit('UNLOADPAGE');
		});
	};
	
	//Init Data
	loadHourList();
	
	//Upload Excel file
	var doNewAction = function() {
		//$timeout(function() {
			var fileSelector = inputFileService.getSelectorById("fileElement");
			inputFileService.loadFileDialog(fileSelector);
			
			inputFileService.addFileSelectedListener(fileSelector, function() {
				var files = this.files;
				
				if (files && files.length > 0) {
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
					
					if (fileName.indexOf("hours") > -1) {
						//Show Animation
						$scope.$emit('LOADPAGE');
						
						//Pagination configure
						$scope.curPage = 0;
						$scope.pageSize = 200;
						$scope.hoursList = [];
						$scope.numberOfPages = function() {
							return 1;
						};
						
						hourService.uploadHour(requestData, function(hoursList, message) {
							$scope.hoursList = hoursList;
							$scope.numberOfPages = function() {
								return Math.ceil($scope.hoursList.length / $scope.pageSize);
							};
							
							//Hide Animation
							$scope.$emit('UNLOADPAGE');
						});
					} else {
						alert("Your file name must be hours.csv!");
					}
				}
				
				//clear input file after loading file dialog
				inputFileService.clearFileInput(this);
			});
		//}, 0, false);
	};
	
	pageService.setPage(newPage);
	pageService.setClick(doNewAction);
});

//Create Hour Detail Controller
productWidgetApp.controller("HourDetailController", function($scope, $routeParams, $location, pageService, hourService, employeeService, datePickerService, utilService) {
	var hourId = utilService.getId($routeParams.hourId),
	employeeList = employeeService.getEmployeeList(),
	hour = {id : hourId},
	createLabel = "Save Hour",
	isUpdateHour = false,
	isCreateNew = true;
	
	//Get Category ID
	if (utilService.isNumber(hourId)) {
		isCreateNew = false;
		isUpdateHour = true;
	}
	
	//Date Picker
	$scope.initDatePicker = function() {
		var date_add = datePickerService.getDateAdd(10),
		date_minus = datePickerService.getDateMinus(10);
		
		$scope.minStartTime = $scope.minStartTime ? null : date_minus;
		$scope.minEndTime = $scope.minEndTime ? null : date_minus;
		$scope.maxStartTime = $scope.maxStartTime ? null : date_add;
		$scope.maxEndTime = $scope.maxEndTime ? null : date_add;
	};
	$scope.initDatePicker();
	
	var eventHandler = function($event) {
		$event.preventDefault();
		$event.stopPropagation();
	};
	$scope.openStartTime = function($event) {
		eventHandler($event);
		$scope.openedStartTime = true;
	};
	$scope.openEndTime = function($event) {
		eventHandler($event);
		$scope.openedEndTime = true;
	};
	if (employeeList.length > 0) {
		$scope.employeeList = employeeList;
	} else {
		(function() {
			employeeService.loadEmployeeList(function(data, message) {
				$scope.employeeList = data;
			});
		})();
	}
	
	$scope.startTime = new Date();//default display current date
	$scope.datePicker = datePickerService;
	
	var keepDefaultDisplay = function() {
		//Keep display current date on startTime field
		if (!$scope.hour.startTime) {
			$scope.hour.startTime = $scope.startTime;
		}
		if (!$scope.hour.endTime) {
			$scope.hour.endTime = $scope.startTime;
		}
		
		//Keep select on first
		if (!$scope.hour.employee) {
			$scope.hour.employee = {id : 1};
		}
	};
	
	if (isUpdateHour) {
		(function() {
			hourService.loadHour(hourId, function(hour, message) {
				$scope.hour = hour;
				keepDefaultDisplay();
			});
		})();//auto execute function
	} else {
		$scope.hour = hour;
		keepDefaultDisplay();
	}
	
	var newPage = {
		isDetailPage : true,
		title : "Hour Detail",
		isCreateNew : isCreateNew,
		createLabel : createLabel,
		isReportPage : isUpdateHour,
		isDisplaySaveBtn : isUpdateHour
	},
	doNewAction = function() {
		var hours = hourService.getHours(),
		newHourId = hours.length + 1,
		employeeObj = $scope.hour.employee,
		endTime = utilService.convertDate($scope.hour.endTime),
		startTime = utilService.convertDate($scope.hour.startTime),
		newHour = {
			id : isUpdateHour ? hourId : newHourId,
			startTime : startTime,
			endTime : endTime
		};
		if (employeeObj) {
			newHour.employeeId = employeeObj.id;
		}
		
		//Update Category
		if (isUpdateHour) {
		} else {
			hourService.createHour(newHour, function(data, message) {
				alert(message);
				$location.path("/hours");//redirect to Hour page
			});
		}
	};
	
	pageService.setPage(newPage);
	pageService.setClick(doNewAction);
});
