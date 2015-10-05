var courseHandler = angular.module('Courses', [ 'ngSanitize' ]);

courseHandler.factory("RoomAndTimes", function() {
	// Define the constructor function.
	var RoomAndTimes = function(id, dayofWeek, beginTime, endTime, roomLabel,
			roomCapacity, roomComments) {
		this.id = id;
		this.dayOfWeek = dayofWeek;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.roomLabel = roomLabel;
		this.roomCapacity = roomCapacity;
		this.roomComments = roomComments;
	}
	// Define the "instance" methods using the prototype
	// and standard prototypal inheritance.
	RoomAndTimes.prototype.getId = function() {
		return (this.id);
	};
	RoomAndTimes.prototype.getDayOfWeek = function() {
		return (this.dayOfWeek);
	};
	RoomAndTimes.prototype.getBeginTime = function() {
		return (this.beginTime);
	};
	RoomAndTimes.prototype.getEndTime = function() {
		return (this.endTime);
	};
	RoomAndTimes.prototype.getRoomLabel = function() {
		return (this.roomLabel);
	};
	RoomAndTimes.prototype.getRoomCapacity = function() {
		return (this.roomCapacity);
	};
	RoomAndTimes.prototype.getRoomComments = function() {
		return (this.roomComments);
	};
	RoomAndTimes.prototype.convertToObject = function() {

		// return (this.id + " %%%" + this.dayOfWeek.replace("%%%", "%") + "
		// %%%"
		// + this.beginTime.replace("%%%", "%") + " %%%"
		// + this.endTime.replace("%%%", "%") + " %%%"
		// + this.roomLabel.replace("%%%", "%") + " %%%"
		// + this.roomCapacity + " %%%"
		// + this.roomComments.replace("%%%", "%") + " ");
		return {
			id : this.id,
			dayofweek : this.dayOfWeek,
			begintime : this.beginTime,
			endtime : this.endTime,
			roomlabel : this.roomLabel,
			roomcapacity : this.roomCapacity,
			roomcomments : this.roomComments
		};
	};

	return (RoomAndTimes);
});
courseHandler
		.controller(
				'CoursesController',
				[
						'$scope',
						'$rootScope',
						'$routeParams',
						'$route',
						'$http',
						'$location',
						'$controller',
						'RoomAndTimes',
						function($scope, $rootScope, $routeParams, $route,
								$http, $location, $controller, RoomAndTimes) {

							var rest = '/GGLecturePlanner/rest/';
							
							$scope.showRoomsAndTimes = function(courseid){  
//								alert("current courseid:" + courseid);
								var contains = false;
								for(var i = 0; i < $scope.showableRoomsAndTimes.length; ++i){ 
									if($scope.showableRoomsAndTimes[i]==courseid){ 
										contains = true;
									}
								}  
								if(!contains) {
//									alert("enabled " + courseid);
									 $scope.showableRoomsAndTimes.push(courseid);
								}else{
									var tmp = [];
									for(var i = 0; i < $scope.showableRoomsAndTimes.length; ++i){
										if($scope.showableRoomsAndTimes[i]!=courseid){
											 tmp.push($scope.showableRoomsAndTimes[i]);
										}
									}
									$scope.showableRoomsAndTimes = tmp;
//									alert("removed " +courseid);
								}
							}
							
							$scope.canShowRoomsAndTimes = function(courseid) {
								 alert("Courseid: " +courseid);
								for(var i = 0; i < $scope.showableRoomsAndTimes.length; ++i){ 
									if($scope.showableRoomsAndTimes[i]==courseid){ 
										alert("true with courseid " + courseid);
										return true;
									}
								} 

								alert("false with courseid " + courseid);
								return false;
							}
							
							var staticDataController = $scope.$new();
							$controller('StaticDataController', {
								$scope : staticDataController
							});

							var employeesController = $scope.$new();
							$controller('EmployeesController', {
								$scope : employeesController
							});

							$scope.updateRoomId = function() {
								return $http.get(rest + 'courses/nextroomid')
										.success(function(id) {
											$scope.nextRoomId = id;
										});
							}
							$scope.getDayAbbreviations = function() {

								return $http
										.get(
												rest
														+ 'staticresources/dayabbreviations')
										.success(function(data) {
											$scope.dayAbbreviations = data;
										});
							};
							$scope.getModuleDetails = function() {
								return $http.get(
										rest + 'modules/moduledetails/'
												+ $routeParams.moduleid)
										.success(function(data) {
											$scope.moduleDetails = data;
										});
							};
							$scope.getCourseDetails = function() { 
								return $http
										.get(
												rest
														+ 'courses/coursedetails/moduleid/'
														+ $routeParams.moduleid)
										.success(function(data) {
											$scope.courses = data;
										});
							};
							$scope.addCourse = function() {

								var roomTimes = [];
								for (var i = 0; i < $scope.roomsAndTimes.length; ++i) {
									roomTimes.push($scope.roomsAndTimes[i]
											.convertToObject());
								}

								$http
										.post(
												rest + "courses/addcourse/",
												{

													moduleid : $routeParams.moduleid,
													courseid : $routeParams.courseid,
													coursetype : $scope.coursetype,
													moduleparts : $scope.selectedModuleParts,
													coursedescription : $scope.coursedescription,
													vvznr : $scope.vvznr,
													nrofgroups : $scope.nrofgroups,
													nrofstudentsexpectedpergroup : $scope.nrofstudentsexpectedpergroup,
													ismaxnrofstudentsexpectedpergroup : $scope.ismaxnrofstudentsexpectedpergroup,
													selectedlecturers : $scope.selectedLecturers,
													swstotpergroup : $scope.swstotpergroup,
													begindate : $scope.begindate,
													enddate : $scope.enddate,
													rythm : $scope.rythm,
													comments : $scope.comments,
													roomsandtimes : roomTimes
												})
										.success(
												function(response) {
													alert("path: " + "/courses/planid/"+$routeParams.planid+"/moduleid/"
															+ $routeParams.moduleid);
// alert(response.message);
													
													if (response.status === "ok") {
														$location
																.path("/courses/planid/"+$routeParams.planid+"/moduleid/"
																		+ $routeParams.moduleid);
													}

												});

							};
							$scope.getSpecificCourseDetails = function() {
								return $http
										.get(
												rest
														+ 'courses/coursedetails/moduleid/'
														+ $routeParams.moduleid
														+ '/courseid/'
														+ $routeParams.courseid)
										.success(
												function(data) {
													$scope.course = data;
													$scope.coursetype = (data.courseType != null ? data.courseType.abbreviation
															: "");
													for (var i = 0; i < data.moduleParts.length; ++i) {
														$scope.selectedModuleParts
																.push(data.moduleParts[i]);
													}
													$scope.coursedescription = data.courseDescription;
													$scope.vvznr = data.vvzNr;
													$scope.nrofgroups = data.nrOfGroups;
													$scope.nrofstudentsexpectedpergroup = data.nrOfStudentsExpectedPerGroup;
													$scope.ismaxnrofstudentsexpectedpergroup = data.isMaxNrStudentsExpectedPerGroup;
													for (var i = 0; i < data.lecturers.length; ++i) {
														$scope.selectedLecturer = data.lecturers[i].id;
														$scope.addLecturer();
													}

													$scope.swstotpergroup = data.swsTotalPerGroup;
													$scope.nrOfLecturers = data.lecturers.length;
													$scope.swsperlecturer = (data.lecturers.length > 0 ? data.swsTotalPerGroup
															/ data.lecturers.length
															: 0);

													$scope.begindate = data.startDate;
													$scope.enddate = data.endDate;
													$scope.rythm = data.rythm;
													for (var i = 0; i < data.courseTimesAndRooms.length; ++i) {
														var tmp = data.courseTimesAndRooms[i];
														var roomAndTimes = new RoomAndTimes(
																tmp.id,
																tmp.dayOfWeek,
																tmp.times.startTime,
																tmp.times.endTime,
																tmp.roomLabel,
																tmp.roomCapacity,
																tmp.comments);
														//								 
														$scope.roomsAndTimes
																.push(roomAndTimes);
													}
												});
							};

							$scope.addRoomAndTimes = function() {
								if ($scope.roomsAndTimes == null) {
									$scope.roomsAndTimes = [];
								}
								if (($scope.dayofweek == null || $scope.dayofweek.length == 0)
										&& ($scope.begintime == null || $scope.begintime.length == 0)
										&& ($scope.endtime == null || $scope.endtime.length == 0)
										&& ($scope.roomlabel == null || $scope.roomlabel.length == 0)
										&& ($scope.roomcapacity == null || $scope.roomcapacity.length == 0)
										&& ($scope.roomcomments == null || $scope.roomcomments.length == 0)) {
									return;
								}
								var nextId = $scope.nextRoomId;
								$scope.updateRoomId();

								var roomAndTimes = new RoomAndTimes(nextId,
										$scope.dayofweek, $scope.begintime,
										$scope.endtime, $scope.roomlabel,
										$scope.roomcapacity,
										$scope.roomcomments);
								// alert(nextId);
								$scope.roomsAndTimes.push(roomAndTimes);
								$scope.initRoomAndTimeModels();
								$scope.hideRoomtable();
							};

							$scope.addModulePart = function() {
								if ($scope.selectedPrimaryNr == null
										|| $scope.selectedPrimaryNr.length == 0) {
									return;
								}

								if ($scope.selectedModulePartNr == null
										|| $scope.selectedModulePartNr.length == 0) {
									return;
								}
								var modulePartConcat = $scope.selectedPrimaryNr
										+ "." + $scope.selectedModulePartNr;
								var contains = false;
								for (var i = 0; i < $scope.selectedModuleParts.length; ++i) {
									if ($scope.selectedModuleParts[i]
											.indexOf(modulePartConcat) >= 0) {
										contains = true;
									}
								}
								if (!contains) {
									$scope.selectedModuleParts
											.push(modulePartConcat);
								}

							};
							$scope.deleteModulePart = function(modulePart) {
								var currentlySelectedModulePartNrs = [];

								for (var i = 0; i < $scope.selectedModuleParts.length; i++) {
									if ($scope.selectedModuleParts[i]
											.indexOf(modulePart) < 0) {
										currentlySelectedModulePartNrs
												.push($scope.selectedModuleParts[i]);
									}
								}
								$scope.selectedModuleParts = currentlySelectedModulePartNrs;

							};
							$scope.addLecturer = function() {
								var availableLecturers = [];
								for (var i = 0; i < $scope.allemployees.length; ++i) {
									if ($scope.allemployees[i].id == $scope.selectedLecturer
											&& !staticDataController.contains(
													$scope.selectedLecturers,
													$scope.selectedLecturer)) {
										$scope.selectedLecturers
												.push($scope.allemployees[i]);
									} else {
										availableLecturers
												.push($scope.allemployees[i]);
									}

								}
								$scope.allemployees = availableLecturers;

							};
							$scope.deleteLecturer = function(id) {
								var currentlySelectedEmployees = [];

								for (var i = 0; i < $scope.selectedLecturers.length; ++i) {
									if ($scope.selectedLecturers[i].id != id
											&& !staticDataController.contains(
													currentlySelectedEmployees,
													id)) {
										if (currentlySelectedEmployees == null) {
											currentlySelectedEmployees = [];
										}
										currentlySelectedEmployees
												.push($scope.selectedLecturers[i]);
									} else {
										$scope.allemployees
												.push($scope.selectedLecturers[i]);
									}
								}

								$scope.selectedLecturers = currentlySelectedEmployees;

							};

							$scope.containsEmployee = function(
									selectedLecturers, lecturer) {

								for (var i = 0; i < selectedLecturers.length; i++) {
									if (selectedLecturers[i].id === lecturer.id) {
										return true;
									}
								}
								return false;
							};
							$scope.updateSWSPerLecturer = function() {
								$scope.swsperlecturer = $sws
							};
							$scope.showRoomtable = function() {
								$scope.roomtableIsVisible = true;
							};
							$scope.hideRoomtable = function() {
								$scope.roomtableIsVisible = false;
							};

							$scope.canShowRoomtable = function() {

								var canShow = (!($scope.coursedescription == undefined
										|| $scope.coursedescription == '' || $scope.coursedescription
										.trim().length == 0) || !($scope.vvznr == undefined
										|| $scope.vvznr == '' || $scope.vvznr
										.trim().length == 0));
								// alert(canShow)
								// return canShow;
								return true;

							};

							$scope.deleteRoomsAndTimes = function(id) {
								var tmp = [];
								for (var i = 0; i < $scope.roomsAndTimes.length; ++i) {
									if (!angular.equals($scope.roomsAndTimes[i]
											.getId(), id)) {
										tmp.push($scope.roomsAndTimes[i]);
									}
								}
								if (tmp.length == 0) {
									$scope.roomtableIsVisible = true;
								}
								$scope.roomsAndTimes = tmp;

							};
							
							

							 $scope.deleteCourse = function(moduleId, courseId
							 ) {
							 return $http.delete(
							 rest+'courses/deletecourse/moduleid/'+moduleId+'/courseid/'+courseId)
							 .success(function(response) {
								 alert(response.message);
							 if (response.status === "ok") {
								 $route.reload();
							 }
							 });
							 };
							$scope.init = function() {

// alert("module id: " +$routeParams.moduleid);
// alert("plan id: " +$routeParams.planid);
								$scope.planid = $routeParams.planid;
// alert($scope.planid);
								$scope.getModuleDetails();
								$scope.getCourseDetails();
								$scope.showableRoomsAndTimes=[];
							};
							 
							$scope.initAddCourse = function() { 
								$scope.planid = $routeParams.planid;
								$scope.updateRoomId();
								$scope.getDayAbbreviations();
								$scope.initRoomAndTimeModels();
								staticDataController.getCourseTypes($scope);
								$scope.roomtableIsVisible = false;
								$scope.coursetype = null;
								$scope.selectedModulePartNr = null;
								$scope.coursedescription = null;
								$scope.vvznr = null;
								$scope.nrofgroups = null;
								$scope.nrofstudentsexpectedpergroup = null;
								$scope.ismaxnrofstudentsexpectedpergroup = null;
								$scope.swstotpergroup = null;
								$scope.nrOfLecturers = null;
								$scope.swsperlecturer = null;
								$scope.begindate = null;
								$scope.enddate = null;
								$scope.rythm = null;
								employeesController.getAllEmployees($scope);
								$scope.selectedLecturers = [];
								$scope.roomsAndTimes = [];
								$scope.initRoomAndTimeModels();
								$scope.selectedModulePartNr = null
								$scope.selectedModuleParts = [];
								$scope.selectedPrimaryNr = null;
								$scope.modulePartNrs = [];
								for (var i = 1; i <= 10; ++i) {
									$scope.modulePartNrs.push(i);
								}
								if ((typeof $routeParams.moduleid) !== "undefined") {
									$scope.getModuleDetails();
								}
								if ((typeof $routeParams.courseid) !== "undefined") {
									$scope.getSpecificCourseDetails();
								}
							};

							$scope.initRoomAndTimeModels = function() {
								$scope.id = "";
								$scope.dayofweek = "";
								$scope.begintime = "";
								$scope.endtime = "";
								$scope.roomlabel = "";
								$scope.roomcapacity = "";
								$scope.roomcomments = "";
							};
						} ]).$inject = [ 'Employees', 'StaticData' ];