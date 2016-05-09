/********************************************
 ** Author: Pao Im
 ** Email: paoim@yahoo.com
 *********************************************/
var httpService = angular.module("httpService", []);

//Create transformRequestAsFormPost
httpService.factory("transformRequestAsFormPost", function() {
	function transformRequest(data, getHeaders) {
		var headers = getHeaders();
		headers[ "Content-type" ] = "application/x-www-form-urlencoded; charset=utf-8";
		
		return( serializeData( data ) );
	};
	
	function serializeData( data ) {
		// If this is not an object, defer to native stringification.
		if ( ! angular.isObject( data ) ) {
			return( ( data == null ) ? "" : data.toString() );
		}
		
		var buffer = [];
		 // Serialize each key in the object.
		for ( var name in data ) {
			if ( ! data.hasOwnProperty( name ) ) {
				continue;
			}
			
			var value = data[ name ];
			buffer.push(encodeURIComponent( name ) + "=" + encodeURIComponent( ( value == null ) ? "" : value ) );
		}
		
		// Serialize the buffer and clean it up for transportation.
		var source = buffer.join( "&" ).replace( /%20/g, "+" );
		
		return( source );
	};
	
	return(transformRequest);
});

//Create $http long form Service
httpService.factory("httpLFService", function($http, transformRequestAsFormPost) {
	var doHttpGet = function(requestUrl, callbackHandler, errorCallback) {
		$http({
			method : "GET",
			url : requestUrl,
			cache : true
		}).success(function(data, status) {
			callbackHandler(data, status);
		}).error(function(data, status) {
			//var newData = data || "Request failed";
			errorCallback(data, status);
		});
	},
	doHttpPost = function(requestUrl, requestData, callbackHandler, errorCallback) {
		$http({
			method : "POST",
			url : requestUrl,
			data : requestData,
			cache : true
		}).success(function(data, status) {
			callbackHandler(data, status);
		}).error(function(data, status) {
			//var newData = data || "Request failed";
			errorCallback(data, status);
		});
	},
	doHttpDelete = function(requestUrl, requestData, callbackHandler, errorCallback) {
		$http({
			method : "DELETE",
			url : requestUrl,
			data : requestData,
			cache : true
		}).success(function(data, status) {
			callbackHandler(data, status);
		}).error(function(data, status) {
			//var newData = data || "Request failed";
			errorCallback(data, status);
		});
	},
	doRequestPost = function(requestUrl, requestData, callbackHandler, errorCallback) {
		var request = $http({
			method: "POST",
			url: requestUrl,
			transformRequest: transformRequestAsFormPost,
			data: requestData
		});
		request.success(function(data, status) {
			callbackHandler(data, status);
		});
		request.error(function(data, status) {
			//var newData = data || "Request failed";
			errorCallback(data, status);
		});
	},
	doXMLUploadFile = function(requestUrl, requestData, uploadComplete, uploadFailed, uploadCanceled) {
		var xhr = null,
		fd = new FormData();
		//console.log(requestData);
		
		fd.append("fileId", requestData.fileId);
		fd.append("file", requestData.fileRequest);
		fd.append("name", requestData.fileName);
		fd.append("fileSize", requestData.fileSize);
		
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
			xhr = new XMLHttpRequest();
		} else {// code for IE6, IE5
			xhr = new ActiveXObject("Microsoft.XMLHTTP");
		}
		
		xhr.addEventListener("load", uploadComplete, false);
		xhr.addEventListener("error", uploadFailed, false);
		xhr.addEventListener("abort", uploadCanceled, false);
		xhr.open("POST", requestUrl);
		xhr.send(fd);
	};

	return {
		doHttpGet : doHttpGet,
		doHttpPost : doHttpPost,
		doHttpDelete : doHttpDelete,
		doRequestPost : doRequestPost,
		doXMLUploadFile : doXMLUploadFile
	};
});

//Create $http short form service
httpService.factory("httpSFService", function($http) {
	var doGet = function(reqUrl, successCallback, errorCallback) {
		$http.get(reqUrl)
		.success(function(data) {
			successCallback(data);
		})
		.error(function(error) {
			//error.message
			errorCallback(error);
		});
	},
	doPost = function(reqUrl, reqData, successCallback, errorCallback) {
		$http.post(reqUrl, reqData)
		.success(function(data) {
			successCallback(data);
		})
		.error(function(error) {
			//error.message
			errorCallback(error);
		});
	},
	doDelete = function(reqUrl, successCallback, errorCallback) {
		$http.delete(reqUrl)
		.success(function() {
			successCallback();
		})
		.error(function(error) {
			//error.message
			errorCallback(error);
		});
	},
	doPut = function(reqUrl, reqData, successCallback, errorCallback) {
		$http.put(reqUrl, reqData)
		.success(function(data) {
			successCallback(data);
		})
		.error(function(error) {
			//error.message
			errorCallback(error);
		});
	},
	doHttpGet = function(reqUrl, query) {
		return $http.get(reqUrl + '/' + encodeURI(query));
	},
	doGetArrayBuffer = function(reqUrl, successCallback, errorCallback) {
		$http.get(reqUrl, {responseType: 'arraybuffer'})
		.success(function(data) {
			successCallback(data);
		})
		.error(function(error) {
			//error.message
			errorCallback(error);
		});
	};

	return {
		doGet : doGet,
		doPut : doPut,
		doPost : doPost,
		doDelete : doDelete,
		doHttpGet : doHttpGet,
		doGetArrayBuffer : doGetArrayBuffer
	};
});
