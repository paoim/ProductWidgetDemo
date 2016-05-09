/*################################################################################
  Reference: AngularJS v1.2.18
  
  Pao Im
  http://pao-profile.appspot.com/

  Normally like to break AngularJS apps into the following folder structure
  at a minimum:

  /webapp
      /css
      /fonts
      /view
      /js
      	/controllers
      	/directives
      	/services
############################################################################################*/
var productWidgetApp = angular.module("ProductWidgetApp", [ "ngRoute", "ui.bootstrap", "productWidgetServices"]);


productWidgetApp.filter('paginationFilter', function() {
	return function(input, start) {
		var newInput = input || [];
		
		start = +start;
		return newInput.slice(start);
	};
});
