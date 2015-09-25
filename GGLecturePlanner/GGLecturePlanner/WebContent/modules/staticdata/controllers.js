'use strict';

angular.module('StaticData')

.controller(
		'StaticDataController',
		[
				'$scope',
				'$rootScope',
				'$route',
				'$http',
				'$location',
				function($scope, $rootScope, $route, $http, $location) {
					var rest = '/GGLecturePlanner/rest/';
					// This is real module stuff
					// ===========================

					$scope.getModuleTypes = function($scope) {
						return $http.get(rest + 'staticresources/moduletypes')
								.success(function(data) {
									$scope.allmoduletypes = data;
								});
					};

					$scope.getDisciplines = function($scope) {
						return $http.get(rest + 'staticresources/disciplines')
								.success(function(data) {
									$scope.alldisciplines = data;
								});
					};
					$scope.getDepartments = function($scope) {
						return $http.get(rest + 'staticresources/departments')
								.success(function(data) {
									$scope.alldepartments = data;
								});

					};
					$scope.getAssessmentTypes = function($scope) {
						return $http.get(
								rest + 'staticresources/assessmenttypes')
								.success(function(data) {
									$scope.allassessmenttypes = data;
								});
					};
					$scope.getSemesterTypes = function($scope) {
						return $http
								.get(rest + 'staticresources/semestertypes')
								.success(function(data) {
									$scope.allsemestertypes = data;
								});
					};
					$scope.getYears = function($scope) {
						return $http.get(rest + 'staticresources/years')
								.success(function(data) {
									$scope.allyears = data;
								});
					};

					$scope.getRoles = function($scope) {
						return $http.get(rest + 'staticresources/roles')
								.success(function(data) {
									$scope.allroles = data;
								});
					};
					$scope.getTrueFalse = function($scope) {
						return $http.get(rest + 'staticresources/truefalse')
								.success(function(data) {
									$scope.trueFalse = data;
								});
					};

				} ]);
