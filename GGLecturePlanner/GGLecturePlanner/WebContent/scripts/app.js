'use strict';

// declare modules
angular.module('app', [ "angucomplete-alt" ]);
angular.module('Authentication', [ "angucomplete-alt" ]);
angular.module('Home', [ "angucomplete-alt" ]);
angular.module('StaticData', [ "angucomplete-alt" ]);
angular.module('Employees', [ "angucomplete-alt" ]);
angular.module('Plans', [ "angucomplete-alt" ]);
angular.module('Modules', [ "angucomplete-alt" ]);
angular.module('Courses', [ "angucomplete-alt" ]);

// Don't forget to add the modules here!!!
angular
		.module(
				'BasicHttpAuthExample',
				[ 'Authentication', 'Home', 'StaticData', 'Employees', 'Plans',
						'Modules', 'Courses', 'ngRoute', 'ngCookies' ])

		.config(
				[
						'$routeProvider',
						function($routeProvider) {

							$routeProvider
									.when(
											'/login',
											{
												controller : 'LoginController',
												templateUrl : 'modules/authentication/views/login.html',
												hideMenus : true
											})
									.when(
											'/',
											{
												controller : 'HomeController',
												templateUrl : 'modules/home/views/home.html'
											})
									.when(
											'/changepw',
											{
												controller : 'HomeController',
												templateUrl : 'modules/home/views/changepassword.html'
											})
									.when(
											'/plans',
											{
												controller : 'PlansController',
												templateUrl : 'modules/plans/views/plans.html'
											})
									.when(
											'/plans/addplan',
											{
												controller : 'PlansController',
												templateUrl : 'modules/plans/views/addplan.html'
											})
									.when(
											'/plans/changeplan/planid/:planid',
											{
												controller : 'PlansController',
												templateUrl : 'modules/plans/views/addplan.html'
											})
									.when(
											'/modules/planid/:planid',
											{
												controller : 'ModulesController',
												templateUrl : 'modules/modules/views/modules.html'
											})

									.when(
											'/modules/addmodule/planid/:planid',
											{
												controller : 'ModulesController',
												templateUrl : 'modules/modules/views/addmodule.html'
											})
									.when(
											'/modules/changemodule/planid/:planid/moduleid/:moduleid',
											{
												controller : 'ModulesController',
												templateUrl : 'modules/modules/views/addmodule.html'
											})
									.when(
											'/courses/planid/:planid/moduleid/:moduleid',
											{
												controller : 'CoursesController',
												templateUrl : 'modules/courses/views/courses.html'
											})
									.when(
											'/courses/addcourse/planid/:planid/moduleid/:moduleid',
											{
												controller : 'CoursesController',
												templateUrl : 'modules/courses/views/addcourse.html'
											})
									.when(
											'/courses/changecourse/planid/:planid/moduleid/:moduleid/courseid/:courseid',
											{
												controller : 'CoursesController',
												templateUrl : 'modules/courses/views/addcourse.html'
											})
									.when(
											'/courses/roomsandtimes/planid/:planid/moduleid/:moduleid/courseid/:courseid',
											{
												controller : 'CoursesController',
												templateUrl : 'modules/courses/views/roomsandtimes.html'
											})
									.when(
											'/employees',
											{
												controller : 'EmployeesController',
												templateUrl : 'modules/employees/views/employees.html'
											})
									.when(
											'/employees/addemployee',
											{
												controller : 'EmployeesController',
												templateUrl : 'modules/employees/views/addemployee.html'
											})
									.when(
											'/employees/changeemployee/employeeid/:employeeid',
											{
												controller : 'EmployeesController',
												templateUrl : 'modules/employees/views/addemployee.html'
											})
									.when(
											'/employees/changeemployee/employeeid/:employeeid',
											{
												controller : 'EmployeesController',
												templateUrl : 'modules/employees/views/addemployee.html'
											}).otherwise({
										redirectTo : '/login'
									});

						} ])

		.run(
				[
						'$rootScope',
						'$location',
						'$cookieStore',
						'$http',
						function($rootScope, $location, $cookieStore, $http) {
							// keep user logged in after page refresh
							$rootScope.globals = $cookieStore.get('globals')
									|| {};
							$rootScope.isAdmin = function() {
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

							$rootScope.isLecturer = function(moduleid) {
								var modulesAsLecturer = $rootScope.globals.currentUser.userdetails.modulesAsLecturer;
								return $rootScope.checkModuleContainment(
										modulesAsLecturer, moduleid);
							}

							$rootScope.isMV = function(moduleid) {
								var modulesAsMV = $rootScope.globals.currentUser.userdetails.modulesAsMV;
								return $rootScope.checkModuleContainment(
										modulesAsMV, moduleid);
							}

							$rootScope.checkModuleContainment = function(
									moduleArray, moduleId) {
								if (moduleArray) {
									for (var i = 0; i < moduleArray.length; ++i) {
										// alert(moduleArray[i].id+ " "
										// +moduleId);
										if (moduleArray[i].id === moduleId) {
											return true;
										}
									}
								}
								return false;
							}

							if ($rootScope.globals.currentUser) {
								$http.defaults.headers.common['Authorization'] = 'Basic '
										+ $rootScope.globals.currentUser.authdata; // jshint
								// ignore:line
							}

							$rootScope.$on('$locationChangeStart', function(
									event, next, current) {
								// redirect to login page if not logged in
								if ($location.path() !== '/login'
										&& !$rootScope.globals.currentUser) {
									$location.path('/login');
								}
							});

						} ]);