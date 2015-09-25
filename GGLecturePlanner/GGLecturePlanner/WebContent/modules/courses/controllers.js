angular.module('Courses')

.controller(
		'CoursesController',
		[
				'$scope',
				'$rootScope',
				'$routeParams',
				'$route',
				'$http',
				'$location',
				function($scope, $rootScope, $routeParams, $route, $http,
						$location) {

					var rest = '/GGLecturePlanner/rest/';
					$scope.init = function() {
						$scope.getCourseDetails();
						$scope.getModuleDetails();
					}

					$scope.getCourseDetails = function() {
						return $http
								.get(
										rest + 'courses/coursedetails/moduleid/'
												+ $routeParams.moduleid)
								.success(function(data) {
									$scope.courses = data;
								});
					};
					$scope.getModuleDetails = function() {
						return $http.get(
								rest + 'modules/moduledetails/'
										+ $routeParams.moduleid).success(
								function(data) {
									$scope.moduleDetails = data;
								});
					}; 
					
					$scope.deleteModule = function(moduleid ) {
						return $http.delete(
								rest+'courses/deletecourse/moduleid/'+moduleId+'/courseid/'+courseId)
								.success(function(response) { 
									if (response.status === "ok") {
										$route.reload();
									}
								});
					};
					 $scope.deleteCourse = function(moduleId, courseId) {
						 return $http.delete(rest+'courses/deletecourse/'+moduleId
					 +'/'+courseId).
					 success(function (data) {
					 window.location.reload();
					 })
					 };
				} ]);