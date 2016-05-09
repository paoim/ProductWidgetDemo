/********************************************
** Author: Pao Im
** Email: paoim@yahoo.com
*********************************************/
// Create Product Service
productWidgetServices.factory("productService", function($rootScope, dataService) {
	var products = [],
	
	setProducts = function(newProducts) {
		products = [];
		products = newProducts;
	},
	getProductes = function() {
		return products;
	},
	getProductByIndex = function(index) {
		return products[index];
	},
	getProductIndex = function(id) {
		var index;
		for (var i = 0; i < products.length; i++) {
			if (products[i].id === id) {
				index = i;
				break;
			}
		}
		return index;
	},
	getProduct = function(id) {
		var product = {};
		for (var i = 0; i < products.length; i++) {
			if (products[i].id === id) {
				product = products[i];
				break;
			}
		}
		return product;
	},
	addProduct = function(newProduct) {
		products.push(newProduct);
	},
	removeProduct = function(index) {
		products.splice(index, 1);
	},
	loadProducts = function(callbackHandler) {
		//dataService.setBaseUrl("http://localhost:8088/api/");
		dataService.getEntities("product/all", function(data) {
			var newProducts = products;
			if(data) {
				setProducts(data);
				newProducts = data;
			}
			callbackHandler(newProducts, "Load Products Successfully...");
		},function(error) {
			callbackHandler([], "Cannot Load Products - " + error.message);
		}, true);
	},
	loadProduct = function(id, callbackHandler) {
		dataService.getEntity("product", id, function(data) {
			callbackHandler(data, "Load Product Successfully...");
		},
		function(error) {
			callbackHandler({}, "Cannot Load Product - " + error.message);
		}, true);
	},
	createProduct = function(product, callbackHandler) {
		delete product.id;
		dataService.createEntity("product/create", product, function(data) {
			if(data) {
				addProduct(data);
				callbackHandler(data, "Create Product Successfully...");
			}
		},
		function(error) {
			callbackHandler({}, "Cannot Create Product - " + error.message);
		});
	},
	uploadProducts = function(requestData, callbackHandler) {
		dataService.doUploadFilePost("product/upload", requestData, function(data) {
			var newProducts = products;
			if(data) {
				setProducts(data);
				newProducts = data;
			}
			callbackHandler(newProducts, "Upload Products Successfully...");
		},
		function(error) {
			callbackHandler([], "Cannot upload Products - " + error.message);
		});
	};
	
	
	return {
		getProduct : getProduct,
		addProduct : addProduct,
		loadProduct : loadProduct,
		setProducts : setProducts,
		loadProducts : loadProducts,
		getProductes : getProductes,
		removeProduct : removeProduct,
		createProduct : createProduct,
		uploadProducts : uploadProducts,
		getProductIndex : getProductIndex,
		getProductByIndex : getProductByIndex
	};
});
