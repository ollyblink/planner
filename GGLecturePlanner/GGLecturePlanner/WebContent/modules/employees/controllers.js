'use strict';

angular
		.module('Employees')

		.controller(
				'EmployeesController',
				[
						'$scope',
						'$rootScope',
						'$route',
						'$controller',
						'$routeParams',
						'$http',
						'$location',
						function($scope, $rootScope, $route, $controller,
								$routeParams, $http, $location) {
							var rest = '/GGLecturePlanner/rest/employees/';

							var staticDataController = $scope.$new();
							$controller('StaticDataController', {
								$scope : staticDataController
							});

							$scope.init = function() {
								$scope.employeeDetails = null;
								$scope.employeeNr = null;
								$scope.firstName = null;
								$scope.lastName = null;
								$scope.email = null;
								$scope.internalCostCenter = null;
								$scope.externalInstitute = null;
								$scope.isPaidSeparately = null;
								$scope.username = null;
								$scope.comments = null;
								$scope.employeeRoles = null;

								$scope.getEmployeeDetails()
								staticDataController.getRoles($scope);
								staticDataController.getTrueFalse($scope);
							}

							$scope.getAllEmployees = function($scope) {
								return $http.get(rest + 'allemployees')
										.success(function(data) {
											$scope.allemployees = data;
										});
							};

							$scope.getEmployees = function() {
								return $http.get(rest + 'allemployees')
										.success(function(data) {
											$scope.allemployees = data;
										});
							};

							 $scope.deleteEmployee = function(employeeId) {
							 return $http.delete(rest + 'deleteemployee/' +
							 employeeId)
							 .success(function(response) {
							 alert(response.message);
							 if (response.status === "ok") {
							 $route.reload();
							 }
							 })
							 };
							$scope.addEmployee = function() {
								$http
										.post(
												rest + "addemployee",
												{
													employeeid : ($routeParams.employeeid ? $routeParams.employeeid
															: null),
													employeenr : $scope.employeeNr,
													firstname : $scope.firstName,
													lastname : $scope.lastName,
													email : $scope.email,
													internalcostcenter : $scope.internalCostCenter,
													externalinstitute : $scope.externalInstitute,
													ispaidseparately : $scope.isPaidSeparately.value,
													username : $scope.username,
													comments : $scope.comments,
													employeeroles : $scope.employeeRoles
												}).success(function(response) {
											alert(response.message);
											if (response.status === "ok") {
												$location.path("/employees");
											}
										});

							}
							$scope.getEmployeeDetails = function() {
								if ((typeof $routeParams.employeeid) !== "undefined") {
									$http
											.get(
													rest
															+ 'employeedetails/'
															+ $routeParams.employeeid)
											.success(
													function(data) {
														$scope.employeeDetails = data;
														$scope.employeeNr = data.employeeNr;
														$scope.firstName = data.firstName;
														$scope.lastName = data.lastName;
														$scope.email = data.email;
														$scope.internalCostCenter = parseInt(data.internalCostCenter);
														$scope.externalInstitute = data.externalInstitute;
														$scope.isPaidSeparately = data.isPaidSeparately;
														$scope.username = data.username;
														$scope.comments = data.comments;
														$scope.employeeRoles = data.roles;
													});
								}
							};

							$scope.checkValue == function(tfValue) {
								if (tfValue) {
									return true;
								} else {
									return false;
								}
							}

							$scope.employeeHasRole = function(roleAbbr) {
								if (((typeof $scope.employeeRoles) === "undefined")
										|| ($scope.employeeRoles == null)) {
									return false;
								}
								for (var i = 0; i < $scope.employeeRoles.length; ++i) {
									if ($scope.employeeRoles[i].abbreviation === roleAbbr) {
										return true;
									}
								}
								return false;
							}
						} ]).$inject = [ 'StaticData' ];
