angular.module('ya-app').controller('NotificationListController',
	['YaService', 'NotificationService', '$scope', '$log','$rootScope','$state', 'notifications',
		function (YaService, NotificationService, $scope, $log, $rootScope, $state, notifications) {

			var setNotification = function(notifications){
				$scope.notifications = notifications;
				if (notifications){
					var notificationSize = Object.keys(notifications).length-1;
					if (notificationSize < 0) notificationSize = 0;
					$rootScope.badgecount = notificationSize;
				} else {
					$rootScope.badgecount = 0;
				}
			};

			setNotification(notifications);

			$scope.$on('$ionicView.beforeEnter', function (event, viewData) {
				NotificationService.getNotifications().then(function (data) {
					setNotification(data);
				});
				$log.debug("NotificationListController beforeEnter is called");
			});


			$scope.doRefresh = function() {
				NotificationService.getNotifications().then(function (data) {
					$log.debug(data);
					setNotification(data);
					$scope.$broadcast('scroll.refreshComplete');
				});
				//Stop the ion-refresher from spinning
			};

			$scope.acknowledge = function(notification){
				NotificationService.acknowledgeNotification(notification).then(function (data) {
					$scope.notifications.splice($scope.notifications.indexOf(notification), 1);
					var notificationSize = Object.keys(notifications).length-1;
					if (notificationSize < 0) notificationSize = 0;
					$rootScope.badgecount = notificationSize;
				});
			};

			$scope.acknowledgeAll = function(){
				YaService.startLoading();
				NotificationService.acknowledgeNotifications().then(function (data) {
					$scope.notifications = [];
					$rootScope.badgecount = 0;
					YaService.stopLoading();
				});
			};

			$scope.goToUser = function(username){
				$state.go('user', {username: username});
			};

			$scope.goToGroup = function(groupId){
				$state.go('group', {groupId: groupId});
			};

			$scope.goToEvent = function(eventId){
				$state.go('event', {eventId: eventId});
			};


		}
	]);



