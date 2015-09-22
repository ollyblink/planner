'use strict';

angular.module('Home')

.controller(
		'HomeController',
		[
				'$scope',
				'$rootScope',
				function($scope, $rootScope) {
					$scope.helloworld = function() {
//						alert($rootScope.globals.currentUser.username + " "
//								+ $rootScope.globals.currentUser.authdata)
					};
				} ]);