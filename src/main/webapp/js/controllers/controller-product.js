/********************************************
** Author: Pao Im
** Email: paoim@yahoo.com
*********************************************/
//Create Product Controller
productWidgetApp.controller("ProductsController", function($scope, $modal, $log, $timeout, pageService, productService, inputFileService) {
	var newPage = {
		isDetailPage : false,
		isUploadExcelFile : true,//flag to show or hide upload button
		title : "Products List",
		createLabel : "New Product",
		createUrl : "products/newID",
		uploadLabel : "Click to upload Products"
	},
	loadProductList = function() {
		//Show Animation
		$scope.$emit('LOADPAGE');
		
		//Pagination configure
		$scope.curPage = 0;
		$scope.pageSize = 200;
		$scope.productsList = [];
		$scope.numberOfPages = function() {
			return 1;
		};
		
		productService.loadProducts(function(productsList, message) {
			$scope.productsList = productsList;
			$scope.numberOfPages = function() {
				return Math.ceil($scope.productsList.length / $scope.pageSize);
			};
			
			//Hide Animation
			$scope.$emit('UNLOADPAGE');
		});
	};
	
	//Init Data
	loadProductList();
	
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
					
					if (fileName.indexOf("production") > -1) {
						//Show Animation
						$scope.$emit('LOADPAGE');
						
						//Pagination configure
						$scope.curPage = 0;
						$scope.pageSize = 200;
						$scope.productsList = [];
						$scope.numberOfPages = function() {
							return 1;
						};
						
						productService.uploadProducts(requestData, function(productsList, message) {
							$scope.productsList = productsList;
							$scope.numberOfPages = function() {
								return Math.ceil($scope.productsList.length / $scope.pageSize);
							};
							
							//Hide Animation
							$scope.$emit('UNLOADPAGE');
						});
					} else {
						alert("Your file name must be production.csv!");
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

//Create Product Detail Controller
productWidgetApp.controller("ProductDetailController", function($scope, $routeParams, $location, pageService, productService, employeeService, datePickerService, utilService) {
	var productId = utilService.getId($routeParams.productId),
	employeeList = employeeService.getEmployeeList(),
	product = {id : productId},
	createLabel = "Save Hour",
	isUpdateProduct = false,
	isCreateNew = true;
	
	//Get Category ID
	if (utilService.isNumber(productId)) {
		isCreateNew = false;
		isUpdateProduct = true;
	}
	
	//Date Picker
	$scope.initDatePicker = function() {
		var date_add = datePickerService.getDateAdd(10),
		date_minus = datePickerService.getDateMinus(10);
		
		$scope.minDate = $scope.minDate ? null : date_minus;
		$scope.maxDate = $scope.maxDate ? null : date_add;
	};
	$scope.initDatePicker();
	
	var eventHandler = function($event) {
		$event.preventDefault();
		$event.stopPropagation();
	};
	$scope.openDate = function($event) {
		eventHandler($event);
		$scope.openedDate = true;
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
		//Keep display current date on date field
		if (!$scope.product.date) {
			$scope.product.date = $scope.startTime;
		}
		
		//Keep select on first
		if (!$scope.product.employee) {
			$scope.product.employee = {id : 1};
		}
	};
	
	if (isUpdateProduct) {
		(function() {
			productService.loadProduct(productId, function(product, message) {
				$scope.product = product;
				keepDefaultDisplay();
			});
		})();//auto execute function
	} else {
		$scope.product = product;
		keepDefaultDisplay();
	}
	
	var newPage = {
		isDetailPage : true,
		title : "Hour Detail",
		isCreateNew : isCreateNew,
		createLabel : createLabel,
		isDisplaySaveBtn : isUpdateProduct
	},
	doNewAction = function() {
		var products = productService.getProductes(),
		newProductId = products.length + 1,
		employeeObj = $scope.product.employee,
		date = utilService.convertDate($scope.product.date),
		newProduct = {
			id : isUpdateProduct ? productId : newProductId,
			date : date,
			widgets : endTime
		};
		if (employeeObj) {
			newProduct.employeeId = employeeObj.id;
		}
		
		//Update Category
		if (isUpdateProduct) {
		} else {
			productService.createProduct(newProduct, function(data, message) {
				alert(message);
				$location.path("/products");//redirect to Product page
			});
		}
	};
	
	pageService.setPage(newPage);
	pageService.setClick(doNewAction);
});
