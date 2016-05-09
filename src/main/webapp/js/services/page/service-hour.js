/********************************************
** Author: Pao Im
** Email: paoim@yahoo.com
*********************************************/
// Create Hour Service
productWidgetServices.factory("hourService", function($rootScope, dataService) {
	var hours = [],
	
	//Private functions
	setHours = function(newHours) {
		hours = [];
		hours = newHours;
	},
	getHourByIndex = function(index) {
		return hours[index];
	},
	getHourIndex = function(id) {
		var index;
		for (var i = 0; i < hours.length; i++) {
			if (hours[i].id === id) {
				index = i;
				break;
			}
		}
		return index;
	},
	getHour = function(id) {
		var hour = {};
		for (var i = 0; i < hours.length; i++) {
			if (hours[i].id === id) {
				hour = hours[i];
				break;
			}
		}
		return hour;
	},
	getHours = function() {
		return hours;
	},
	addHour = function(newHour) {
		hours.push(newHour);
	},
	removeHour = function(index) {
		hours.splice(index, 1);
	},
	loadHours = function(callbackHandler) {
		//dataService.setBaseUrl("http://localhost:8088/api/");
		dataService.getEntities("hour/all", function(data) {
			var newHours = hours;
			if(data) {
				setHours(data);
				newHours = data;
			}
			callbackHandler(newHours, "Load Hours Successfully...");
		},function(error) {
			callbackHandler([], "Cannot load Hours - " + error.message);
		}, true);
	},
	loadHour = function(id, callbackHandler) {
		dataService.getEntity("hour", id, function(data) {
			callbackHandler(data, "Load Hour Successfully...");
		},
		function(error) {
			callbackHandler({}, "Cannot load Hour - " + error.message);
		}, true);
	},
	createHour = function(hour, callbackHandler) {
		delete hour.id;
		dataService.createEntity("hour/create", hour, function(data) {
			if(data) {
				addHour(data);
				callbackHandler(data, "Create Hour Successfully...");
			}
		},
		function(error) {
			callbackHandler({}, "Cannot create Hour - " + error.message);
		});
	},
	uploadHour = function(requestData, callbackHandler) {
		dataService.doUploadFilePost("hour/upload", requestData, function(data) {
			var newHours = hours;
			if(data) {
				setHours(data);
				newHours = data;
			}
			callbackHandler(newHours, "Upload Hours Successfully...");
		},
		function(error) {
			callbackHandler([], "Cannot upload Hours - " + error.message);
		});
	};
	
	return {
		getHour : getHour,
		addHour : addHour,
		getHours : getHours,
		loadHour : loadHour,
		loadHours : loadHours,
		uploadHour : uploadHour,
		removeHour : removeHour,
		createHour : createHour,
		getHourByIndex : getHourByIndex
	};
});
