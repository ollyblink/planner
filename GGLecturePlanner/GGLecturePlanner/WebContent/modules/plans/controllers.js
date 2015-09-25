'use strict';

angular
		.module('Plans')

		.controller(
				'PlansController',
				[
						'$scope','$rootScope','$routeParams','$route',
						'$http',
						'$location',
						function($scope,$rootScope,$routeParams, $route,$http, $location) {
							var rest = '/GGLecturePlanner/rest/';
							

							$scope.isPlanIdDefined = function() {
								return (((typeof $routeParams.planid) !== "undefined") && (!isNaN($routeParams.planid)));
							};
							
							
							$scope.getAllPlans = function() {
								return $http.get(rest + 'plans/allplans')
										.success(function(data) {
											$scope.plans = data;
											// alert(data);
										});
							};

							$scope.addPlan = function() { 
								if(!$scope.isPlanIdDefined()) {
									$http.post(rest + "plans/addplan", {
										semester : $scope.semester,
										year : $scope.year
									}).success(function(response) {
										alert(response.message); 
										if (response.status === "ok") {
											$location.path("/plans");
										}
									});
								}else{
									$http.post(rest + "plans/changeplan", {
										id: $routeParams.planid,
										semester : $scope.semester,
										year : $scope.year
									}).success(function(response) { 
										alert(response.message);
										if (response.status === "ok") {
											$location.path("/plans");
										}
									});
								}
							};

							$scope.deletePlan = function(planid) {
								return $http.delete(
										rest + 'plans/deleteplan/' + planid)
										.success(function(response) {
											if (response.status === "ok") {
												$route.reload();
											}
										});
							};

							$scope.copyPlan = function(planId) {
								return $http.get(
										rest + 'plans/copyplan/' + planId)
										.success(function(response) {
											if (response.status === "ok") {
												$route.reload();
											}
										});
							};

							$scope.getSemesterTypes = function() {
								return $http.get(
										rest + 'staticresources/semestertypes')
										.success(function(data) {
											$scope.semestertypes = data;
										});
							};
							$scope.getYears = function() {
								return $http
										.get(rest + 'staticresources/years')
										.success(function(data) {
											$scope.years = data;
										});
							};

							$scope.getPlanDetails = function() {
								 
								if (((typeof $routeParams.planid) !== "undefined")
										&& (!isNaN($routeParams.planid))) {
									
									return $http
											.get(
													rest + 'plans/plandetails/'
															+ $routeParams.planid)
											.success(
													function(data) {
														if ((typeof data) !== "") {
															$scope.planDetails = data;
															if ((typeof data.semester) !== "undefined") {
																$scope.semester = data.semester;
															} else {
																$location
																		.path("/plans/addplan");
															}

															if ((typeof data.year) !== "undefined") {
																$scope.year = data.year;
															} else {
																$location
																		.path("/plans/addplan");
															}
														} else {
															$location
																	.path("/plans/addplan");
														}
													});
								} else {
									$location.path("/plans/addplan");
								} 
							};

						} ]);
