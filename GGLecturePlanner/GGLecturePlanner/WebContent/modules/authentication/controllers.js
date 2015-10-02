'use strict';

angular
		.module('Authentication')

		.controller(
				'LoginController',
				[
						'$scope',
						'$rootScope',
						'$location', 
						'AuthenticationService',

						function($scope, $rootScope, $location,
								AuthenticationService) {
							// reset login status
							AuthenticationService.ClearCredentials();
 
							$scope.login = function() {
								$scope.dataLoading = true;
								AuthenticationService
										.Login(
												$scope.username,
												$scope.password,
												function(response) {
													if ((typeof response.id) !== "undefined") {
														AuthenticationService
																.SetCredentials(
																		$scope.username,
																		$scope.password,
																		response);
														alert(response.firstName
																+ " "
																+ response.lastName
																+ " logged in");

														$location.path('/');
														// $window.location.href
														// =
														// '/GGLecturePlanner/web/start.html';
														// alert($rootScope.globals.currentUser.username
														// + " " +
														// $rootScope.globals.currentUser.authdata);

													} else {
														$scope.error = response;
														$scope.dataLoading = false;
													}
												});
							};
						} ]);