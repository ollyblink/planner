'use strict';

// declare modules
angular.module('Authentication', ['ngSanitize']);
angular.module('Home', ['ngSanitize']);
angular.module('StaticData', ['ngSanitize']);
angular.module('Employees', ['ngSanitize']);
angular.module('Plans', ['ngSanitize']);
angular.module('Modules', ['ngSanitize']);
angular.module('Courses', ['ngSanitize']);

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