'use strict';

// declare modules
angular.module('Authentication', []);
angular.module('Home', []);
angular.module('Plans', []);

angular
		.module('BasicHttpAuthExample',
				[ 'Authentication', 'Home', 'Plans', 'ngRoute', 'ngCookies' ])

		.config([ '$routeProvider', function($routeProvider) {

			$routeProvider.when('/login', {
				controller : 'LoginController',
				templateUrl : 'modules/authentication/views/login.html',
				hideMenus : true
			}).when('/', {
				controller : 'HomeController',
				templateUrl : 'modules/home/views/home.html'
			}).when('/plans', {
				controller : 'PlansController',
				templateUrl : 'modules/plans/views/plans.html'
			}).when('/plans/addplan', {
				controller : 'PlansController',
				templateUrl : 'modules/plans/views/addplan.html'
			}).when('/plans/changeplan/:planid', {
				controller : 'PlansController',
				templateUrl : 'modules/plans/views/addplan.html'
			}).when('/modules/:planid', {
				controller : 'ModulesController',
				templateUrl : 'modules/modules/views/modules.html'
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