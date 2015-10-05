'use strict';

angular
		.module('Home')

		.controller(
				'HomeController',
				[
						'$scope',
						'$rootScope',
						'$http',
						function($scope, $rootScope, $http) {

							var rest = "rest/login/changepassword";

							$scope.init = function() {
								$scope.couldChangePassword = true;
								$scope.samePW = true;
								$scope.couldNotChangePasswordMessage = null;
								$scope.couldChangePasswordMessage = null;

							};
							$scope.changePassword = function() { 
								if ($scope.newPw === $scope.repeatNewPw) {
									$scope.samePW = true;
									$http
											.post(
													rest,
													{
														id: $rootScope.globals.currentUser.userdetails.id,
														username : $rootScope.globals.currentUser.userdetails.username,
														oldPw : $scope.oldPw,
														newPw : $scope.newPw
													})
											.success(
													function(response) {
//														alert(response.status);
														if (response.status === "ok") {
															$scope.couldChangePassword = true;
															$scope.couldChangePasswordMessage = response.message;
														} else {
															$scope.couldChangePassword = false;
															$scope.couldNotChangePasswordMessage = response.message;
														}
													});
								} else {
									$scope.couldChangePassword = false;
									$scope.samePW = false;
									$scope.couldNotChangePasswordMessage = null;
									$scope.couldChangePasswordMessage = null;
								}
							}

						} ]);