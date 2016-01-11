angular.module('ya-app').config(function ($stateProvider, $urlRouterProvider) {
	$stateProvider
		.state('tabs.notifications', {
			url: "/notifications",
			views: {
				'tab-notifications': {
					templateUrl: "templates/notifications/notification-list.html",
					controller: 'NotificationListController',
					resolve: {
						notifications: function (NotificationService) {
							return NotificationService.getNotificationsFromCache();
						}
					}
				}
			},
			authenticate: false
		});
});
