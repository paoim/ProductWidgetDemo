/********************************************
** Author: Pao Im
** Email: paoim@yahoo.com
*********************************************/
// Create Navigation Controller
productWidgetApp.controller("NavigationController", [ "$scope", "$location", function($scope, $location) {
	$scope.isActiveClass = function(path) {
		return $location.path().substr(0, path.length) == path;
	};
} ]);

//Create Main Controller
productWidgetApp.controller("ProductWidgetController", function($scope, pageService) {
	$scope.page = pageService;
	
	//Animation show/hide
	$scope.$on('LOADPAGE', function() {
		$scope.isPageLoading = true;
	});
	$scope.$on('UNLOADPAGE', function() {
		$scope.isPageLoading = false;
	});
	
	//Animation show/hide
	$scope.$on('LOADSEARCH', function() {
		$scope.isSearching = true;
	});
	$scope.$on('UNLOADSEARCH', function() {
		$scope.isSearching = false;
	});
});
