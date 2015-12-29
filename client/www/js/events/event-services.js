angular.module('ya-app').factory('EventService',
    ['$http', 'YaConfig', '$filter', '$log', 'YaService', 'Event',
        function($http, YaConfig, $filter, $log, YaService, Event) {

            var resultService;
            resultService = {
                getEvents: function () {
                    return Event.findAll();
                    /*
                    return $http.get(YaConfig.url + '/api/events').then(function (response) {
                        return response.data;
                    });
                    */
                },
                getEventTimeline: function () {
                    return $http.get(YaConfig.url + '/events/timeline').then(function (response) {
                        Event.inject(response.data.today);
                        Event.inject(response.data.upcoming);
                        Event.inject(response.data.past);
                        return response.data;
                    });
                },
                getEvent: function (eventId) {
                    return Event.find(eventId);
                    /*
                    return $http.get(YaConfig.url + '/events/' + eventId).then(function (response) {
                        return response.data;
                    });
                    */
                },
                saveEvent: function (event) {
                    if (event.id){
                        return Event.update(event.id, event);
                    } else {
                        return Event.create(event);
                    }
                    /*
                    return $http.post(YaConfig.url + '/events', event).then(function (response) {
                        return response.data;
                    });
                    */
                },
                getCommentSize: function (event) {
                    return $http.get(YaConfig.url + '/events/' + event.id + '/comments/size').then(function (response) {
                        return response.data;
                    });
                },
                getComments: function (eventId) {
                    return $http.get(YaConfig.url + '/events/' + eventId + '/comments').then(function (response) {
                        return response.data;
                    });
                },
                postComment: function (eventId, content) {
                    return $http.post(YaConfig.url + '/events/' + eventId + '/comments/' + content).then(function (response) {
                        return response.data;
                    });
                },
                deleteEvent: function (event) {
                    return Event.destroy(event.id);
                    /*
                    return $http.post(YaConfig.url + '/api/events/' + event.id + '/delete').then(function (response) {
                        return response.data;
                    });
                    */
                },
                instanciateEvent: function (groupId) {
                    return $http.get(YaConfig.url + '/groups/' + groupId +'/events/new').then(function (response) {
                        return response.data;
                    });
                },
                instanciateParticipation: function(event){
                  participation = {eventId : event.id, userName : YaService.getUsername()};
                },
                getEventParticipations: function(eventId) {
                    return $http.get(YaConfig.url + '/events/' + eventId +'/participations').then(function (response) {
                        return response.data;
                    });
                },
                getLastParticipations: function(event) {
                    return $http.get(YaConfig.url + '/events/' + event.id +'/participations/last').then(function (response) {
                        return response.data;
                    });
                },
                getParticipationSummary: function(eventId) {
                    return $http.get(YaConfig.url + '/events/' + eventId +'/participations/summary').then(function (response) {
                        return response.data;
                    });
                },
                getUserParticipation: function(event) {
                    return $http.get(YaConfig.url + '/users/' + YaService.getUsername() + '/events/' + event.id + '/participation').then(function (response) {
                        return response.data;
                    });
                },
                participate: function(participation){
                    return $http.post(YaConfig.url + '/events/' + participation.eventId + '/participations', participation).then(function (response) {
                        return response.data;
                    });
                },
                generateParticipations: function(event){
                    return $http.post(YaConfig.url + '/events/' + event.id + '/participations/generate').then(function (response) {
                        return response.data;
                    });
                },
                getParticipationsSize: function(event) {
                    return $http.get(YaConfig.url + '/events/' + event.id +'/participations/size').then(function (response) {
                        return response.data;
                    });
                },
                canEdit: function(event){
                    $log.debug("call canUpdate from EventService canUpdate =" + YaService.getUsername());
                    if (event){
                        if (event.username == YaService.getUsername()){
                            return true;
                        }
                        return (event.sponsors && event.sponsors.indexOf(YaService.getUsername()) > 0);
                    }
                    return false;
                },
                getSponsors: function (eventId) {
                    return $http.get(YaConfig.url + '/events/' + eventId + '/sponsors').then(function (response) {
                        return response.data;
                    });
                }
            };
            return resultService;
        }]);


