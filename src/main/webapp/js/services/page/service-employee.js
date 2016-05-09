/********************************************
** Author: Pao Im
** Email: paoim@yahoo.com
*********************************************/
// Create Employee Service
productWidgetServices.factory("employeeService", function($rootScope, dataService) {
	// private variable
	var employeeList = [],
	
	//private functions
	setEmployeeList = function(newEmployeeList) {
		employeeList = [];
		employeeList = newEmployeeList;
	},
	getEmployee = function(id) {
		var employee = {};
		for (var i = 0; i < employeeList.length; i++) {
			if (employeeList[i].id === id) {
				employee = employeeList[i];
				break;
			}
		}
		return employee;
	},
	getEmployeeIndex = function(id) {
		var index;
		for (var i = 0; i < employeeList.length; i++) {
			if (employeeList[i].id === id) {
				index = i;
				break;
			}
		}
		return index;
	},
	getEmployeeByIndex = function(index) {
		return employeeList[index];
	},
	getEmployeeList = function() {
		return employeeList;
	},
	addEmployee = function(newEmployee) {
		employeeList.push(newEmployee);
	},
	removeEmployee = function(index) {
		employeeList.splice(index, 1);
	},
	loadEmployeeList = function(callbackHandler) {
		//dataService.setBaseUrl("http://localhost:8088/api/");
		dataService.getEntities("employee/all", function(data) {
			var newEmployeeList = employeeList;
			if(data) {
				setEmployeeList(data);
				newEmployeeList = data;
			}
			callbackHandler(newEmployeeList, "Load EmployeeList Successfully...");
		},function(error) {
			callbackHandler([], "Cannot load EmployeeList - " + error.message);
		});
	},
	loadEmployee = function(id, callbackHandler) {
		dataService.getEntity("employee", id, function(data) {
			callbackHandler(data, "Load Employee Successfully...");
		},
		function(error) {
			callbackHandler({}, "Cannot load Employee - " + error.message);
		});
	},
	createEmployee = function(employee, callbackHandler) {
		delete employee.id;
		dataService.createEntity("employee/create", employee, function(data) {
			if(data) {
				addEmployee(data);
				callbackHandler(data, "Create Employee Successfully...");
			}
		},
		function(error) {
			callbackHandler({}, "Cannot create Employee - " + error.message);
		});
	},
	uploadEmployee = function(requestData, callbackHandler) {
		dataService.doUploadFilePost("employee/upload", requestData, function(data) {
			var newEmployeeList = employeeList;
			if(data) {
				setEmployeeList(data);
				newEmployeeList = data;
			}
			callbackHandler(newEmployeeList, "Upload EmployeeList Successfully...");
		},
		function(error) {
			callbackHandler([], "Cannot upload EmployeeList - " + error.message);
		});
	};
	
	return{
		//public functions
		getEmployee : getEmployee,
		addEmployee : addEmployee,
		loadEmployee : loadEmployee,
		uploadEmployee : uploadEmployee,
		createEmployee : createEmployee,
		removeEmployee : removeEmployee,
		getEmployeeList : getEmployeeList,
		loadEmployeeList : loadEmployeeList,
		getEmployeeByIndex : getEmployeeByIndex
	};
});
