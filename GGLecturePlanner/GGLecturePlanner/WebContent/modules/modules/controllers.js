'use strict';

angular
		.module('Modules')

		.controller(
				'ModulesController',
				[
						'$scope',
						'$rootScope',
						'$route',
						'$routeParams',
						'$http',
						'$location',
						'$controller',
						function($scope, $rootScope, $route, $routeParams,
								$http, $location, $controller) {
							var rest = '/GGLecturePlanner/rest/';
							// This is real module stuff
							// ===========================
							var plansController = $scope.$new();
							$controller('PlansController', {
								$scope : plansController
							});
							var staticDataController = $scope.$new();
							$controller('StaticDataController', {
								$scope : staticDataController
							});
							var employeeController = $scope.$new();
							$controller('EmployeesController', {
								$scope : employeeController
							});

							$scope.contains = function contains(a, obj) {
								for (var i = 0; i < a.length; i++) {
									if (a[i] === obj) {
										return true;
									}
								}
								return false;
							};

							$scope.containsType = function(
									typesWithAbbreviation, valWithAbbreviation) {
								if ((typesWithAbbreviation == null)
										|| (valWithAbbreviation == null)
										|| (typeof typesWithAbbreviation === "undefined")
										|| (typeof valWithAbbreviation === "undefined")) {
									return false;
								}
								for (var i = 0; i < typesWithAbbreviation.length; ++i) {
									if (typesWithAbbreviation[i] === valWithAbbreviation.abbreviation) {
										return true;
									}
								}
								return false;
							};
							$scope.addOrRemove = function(
									typesWithAbbreviation, typeWithAbbreviation) {
								// alert(typeof typesWithAbbreviation );

								if ($scope.containsType(typesWithAbbreviation,
										typeWithAbbreviation)) {
									var tmp = [];
									for (var i = 0; i < typesWithAbbreviation.length; ++i) {
										// alert(typesWithAbbreviation[i].abbreviation);
										if (typesWithAbbreviation[i] !== typeWithAbbreviation.abbreviation) {
											tmp
													.push(typesWithAbbreviation[i].abbreviation);
										}
									}
									typesWithAbbreviation = tmp;
								} else {
									// alert(typeof typesWithAbbreviation );
									typesWithAbbreviation
											.push(typeWithAbbreviation.abbreviation);
								}

							};

							$scope.printArray = function(arr) {
								var printString = "";
								for (var i = 0; i < arr.length; ++i) {
									printString += arr[i] + ", ";
								}
								alert(printString);
							}

							$scope.init = function() {
								$scope.modulePrimaryNrs = [];
								$scope.modulePrimaryNr = null;
								$scope.semesternr = null;
								$scope.moduletypes = [];
								$scope.disciplines = [];
								$scope.department = null;
								$scope.assessmenttype = null;
								$scope.assessmentdate = null;
								$scope.responsibleemployee = null;
								$scope.comments = null;
								$scope.moduleid = null;
								$scope.planid = null;

								staticDataController.getModuleTypes($scope);
								staticDataController.getDisciplines($scope);
								staticDataController.getAssessmentTypes($scope);
								staticDataController.getDepartments($scope);

								employeeController.getAllEmployees($scope)

							};

							$scope.addAllAbbreviations = function(
									dataWAbbreviations, abbreviations) {
								for (var i = 0; i < dataWAbbreviations.length; ++i) {
									abbreviations
											.push(dataWAbbreviations[i].abbreviation);
								}
							}

							$scope.getPlanDetails = function() {
								if (((typeof $routeParams.planid) !== "undefined")
										&& (!isNaN($routeParams.planid))) {
									return $http.get(
											rest + 'plans/plandetails/'
													+ $routeParams.planid)
											.success(function(data) {
												if ((typeof data) !== "") {
													$scope.planDetails = data;
												} else {
													$location.path("/plans");
												}

											});
								} else {
									$location.path("/plans");
								}
							};

							$scope.getModuleDetails = function() {

								if (((typeof $routeParams.moduleid) !== "undefined")
										&& (!isNaN($routeParams.moduleid))) {
									return $http
											.get(
													rest
															+ 'modules/moduledetails/'
															+ $routeParams.moduleid)
											.success(
													function(data) {
														if ((typeof data) !== "") {
															$scope.moduleDetails = data;
															$scope.modulePrimaryNrs = (data.primaryNrs);
															$scope.semesternr = parseInt(data.semesterNr);
															$scope.assessmenttype = (!data.assessmentType ? ""
																	: data.assessmentType.abbreviation);
															$scope.department = (!data.department ? ""
																	: data.department.deptName);
															$scope.assessmentdate = data.assessmentDate;
															$scope.responsibleemployee = (!data.responsibleEmployee ? ""
																	: data.responsibleEmployee.id);

															$scope
																	.addAllAbbreviations(
																			data.moduleTypes,
																			$scope.moduletypes);
															$scope
																	.addAllAbbreviations(
																			data.disciplines,
																			$scope.disciplines);
															// alert($scope.responsibleemployee);
															$scope.comments = data.comments;
														} else {
															$location
																	.path("/modules/addmodule");
														}
													});
								}
							};

							$scope.addModulePN = function() {
								if (typeof $scope.modulePrimaryNrs === "undefined") {
									$scope.modulePrimaryNrs = [];
								}
								if ((typeof $scope.modulePrimaryNr) !== "undefined"
										&& $scope.modulePrimaryNr != null
										&& $scope.modulePrimaryNr.length > 0
										&& !$scope.contains(
												$scope.modulePrimaryNrs,
												$scope.modulePrimaryNr)) {
									$scope.modulePrimaryNrs
											.push($scope.modulePrimaryNr);
									$scope.modulePrimaryNr = "";
								}
							};

							$scope.deleteModulePN = function(pN) {

								if (typeof $scope.modulePrimaryNrs !== "undefined") {
									if (!$scope.contains(
											$scope.modulePrimaryNrs, pN)) {
										return;
									}
									var tmp = [];
									for (var i = 0; i < $scope.modulePrimaryNrs.length; ++i) {
										if ($scope.modulePrimaryNrs[i] !== pN) {
											tmp
													.push($scope.modulePrimaryNrs[i]);
										}
									}
									$scope.modulePrimaryNrs = tmp;
								}
							};
 

							$scope.isModuleIdDefined = function() {
								return ((typeof $routeParams.moduleid) !== "undefined")
										&& (!isNaN($routeParams.moduleid));
							}
							
							$scope.addModule = function() {
							 
									$http
											.post(
													rest + "modules/addmodule",
													{
														moduleid: $routeParams.moduleid,
														planid : $routeParams.planid,
														modulePrimaryNrs : $scope.modulePrimaryNrs,
														semesternr : $scope.semesternr,
														moduletypes : $scope.moduletypes,
														disciplines : $scope.disciplines,
														department : $scope.department,
														assessmenttype : $scope.assessmenttype,
														assessmentdate : $scope.assessmentdate,
														responsibleemployee : $scope.responsibleemployee,
														comments : $scope.comments
													})
											.success(
													function(response) {
														alert(response.message);
														if (response.status === "ok") {
															$location
																	.path("/modules/planid/"
																			+ $routeParams.planid);
														}
													});
						 
							};

							$scope.deleteModule = function(moduleid ) {
								return $http.delete(
										rest + 'modules/deletemodule/moduleid/'+moduleid)
										.success(function(response) { 
											if (response.status === "ok") {
												$route.reload();
											}
										});
							};
						 

						} ]).$inject = [ 'Plans', 'StaticData' ];
