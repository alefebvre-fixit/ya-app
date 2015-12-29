angular.module('ya-app').factory('NotificationService',
    ['$http', 'YaConfig', 'Notification',
        function($http, YaConfig, Notification) {

            var resultService;
            resultService = {
                getNotifications: function () {
                    return Notification.findAll();
                    //return $http.get(YaConfig.url + '/notifications').then(function (response) {
                    //    return response.data;
                    //});
                },
                acknowledgeNotification: function (notification) {
                    return $http.post(YaConfig.url + '/notifications/' + notification.id + '/acknowledge').then(function (response) {
                        return response.data;
                    });
                },
                acknowledgeNotifications: function () {
                    return $http.post(YaConfig.url + '/notifications/acknowledge').then(function (response) {
                        return response.data;
                    });
                },
                acknowledgeEventNotifications: function (eventId) {
                    return $http.post(YaConfig.url + '/events/' + eventId  + '/notifications/acknowledge').then(function (response) {
                        return response.data;
                    });
                },
                acknowledgeGroupNotifications: function (groupId) {
                    return $http.post(YaConfig.url + '/groups/' + groupId  + '/notifications/acknowledge').then(function (response) {
                        return response.data;
                    });
                }

            };

            return resultService;
        }]);
