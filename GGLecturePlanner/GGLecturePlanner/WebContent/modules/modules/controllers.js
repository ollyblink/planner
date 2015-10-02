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
							$scope.compare = function(dmtCombs, dmtComb){  
// alert(dmtCombs.discipline +"==="+ dmtComb.discipline+"=
// "+Object.is(dmtCombs.discipline,dmtComb.discipline));
// alert(dmtCombs.moduletype +"==="+ dmtComb.moduletype+"=
// "+(Object.is(dmtCombs.moduletype,dmtComb.moduletype)));

								return ((Object.is(dmtCombs.discipline,dmtComb.discipline)) &&
										(Object.is(dmtCombs.moduletype,dmtComb.moduletype)));
							};
							$scope.addDiscipline = function() {
								if (((typeof $scope.selectedDiscipline) === "undefined") || ($scope.selectedDiscipline.length == 0) ) {
									return;
								}

								if (((typeof $scope.selectedModuleType) === "undefined") || ($scope.selectedModuleType.length == 0) )  {
									return;
								}
								
								var disciplineModuleTypeCombination = {
										discipline: $scope.selectedDiscipline,
										moduletype: $scope.selectedModuleType,
										concat: $scope.selectedDiscipline+ " "+ $scope.selectedModuleType
								}; 

								var contains = false;
								for (var i = 0; i < $scope.selectedDisciplinesAndModuleParts.length; ++i) {
									if ($scope.compare($scope.selectedDisciplinesAndModuleParts[i],disciplineModuleTypeCombination)) {
 										contains = true;
 										break;
									}
								}
// alert("contains?"+ contains);
								if (!contains) {
// alert("inside contains")
									$scope.selectedDisciplinesAndModuleParts
											.push(disciplineModuleTypeCombination);
								}

							};
							$scope.deleteDiscipline = function(disciplineModuleTypeCombination) {
			 
								var tmp = [];

								for (var i = 0; i < $scope.selectedDisciplinesAndModuleParts.length; i++) {
									if (!$scope.compare($scope.selectedDisciplinesAndModuleParts[i],disciplineModuleTypeCombination  )) {
										tmp.push($scope.selectedDisciplinesAndModuleParts[i]);
									}
								}
								$scope.selectedDisciplinesAndModuleParts = tmp;

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
// alert("Responsible employee: " +data.responsibleEmployee.firstName );
															$scope.responsibleemployee = (!data.responsibleEmployee ? ""
																	: data.responsibleEmployee.id);

// $scope
// .addAllAbbreviations(
// data.moduleTypes,
// $scope.moduletypes);
// $scope
// .addAllAbbreviations(
// data.disciplines,
// $scope.disciplines);
															for(var i = 0; i< data.disciplines.length;++i){
																for(var j = 0; j < data.disciplines[i].moduleTypes.length;++j){
																	var dmt = {
																		discipline: data.disciplines[i].abbreviation,
																		moduletype: data.disciplines[i].moduleTypes[j].abbreviation,
																		concat: data.disciplines[i].abbreviation+" "+data.disciplines[i].moduleTypes[j].abbreviation
																	} 
																	$scope.selectedDisciplinesAndModuleParts.push(dmt);
																}
															}
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
//							 alert($scope.selectedDisciplinesAndModuleParts);
									$http
											.post(
													rest + "modules/addmodule",
													{
														moduleid: $routeParams.moduleid,
														planid : $routeParams.planid,
														modulePrimaryNrs : $scope.modulePrimaryNrs,
														semesternr : $scope.semesternr,
// moduletypes : $scope.moduletypes,
// disciplines : $scope.disciplines,
														selectedDisciplinesAndModuleParts: $scope.selectedDisciplinesAndModuleParts,
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
							$scope.init = function() {
								$scope.modulePrimaryNrs = [];
								$scope.modulePrimaryNr = null;
								$scope.semesternr = null;
// $scope.moduletypes = [];
// $scope.disciplines = [];
								$scope.department = null;
								$scope.assessmenttype = null;
								$scope.assessmentdate = null;
								$scope.responsibleemployee = null;
								$scope.comments = null;
								$scope.moduleid = null;
								$scope.planid = null; 
								$scope.selectedDisciplinesAndModuleParts = [];
								staticDataController.getModuleTypes($scope);
								staticDataController.getDisciplines($scope);
								staticDataController.getAssessmentTypes($scope);
								staticDataController.getDepartments($scope);

								employeeController.getAllEmployees($scope)


								$scope.getPlanDetails();
								$scope.getModuleDetails();

							};

						} ]).$inject = [ 'Plans', 'StaticData' ];
