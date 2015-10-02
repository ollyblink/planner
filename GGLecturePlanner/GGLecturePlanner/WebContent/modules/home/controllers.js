'use strict';

angular.module('Home')

.controller('HomeController',
		[ '$scope', '$rootScope', '$http', function($scope, $rootScope, $http) {
  
			
			$scope.canUpdate = function() {
				var roles = $rootScope.globals.currentUser.userdetails.roles;

				if (roles) {
					for (var i = 0; i < roles.length; ++i) {

						if (roles[i].abbreviation === "Admin") {
							return true;
						}
					}
				}
				return false;
			};
		} ]);