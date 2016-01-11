angular.module('ya-app').config(function ($stateProvider) {
    $stateProvider
        .state('tabs.events', {
            cache: true,
            url: "/events",
            views: {
                'tab-events': {
                    templateUrl: "templates/events/event-list.html",
                    controller: 'ListEventsController'
                }
            },
            authenticate: true
        })
        .state('event', {
            cache: true,
            url: "/event/:eventId",
            templateUrl: "templates/events/event-view.html",
            controller: 'ViewEventController',
            resolve: {
                eventId: function ($stateParams) {
                    return $stateParams.eventId;
                }
            }
            ,
            authenticate: true
        })
        .state('event-location', {
            cache: true,
            url: "/event/:eventId",
            templateUrl: "templates/events/event-location.html",
            controller: 'EventLocationController',
            resolve: {
                eventId: function ($stateParams) {
                    return $stateParams.eventId;
                }
            }
            ,
            authenticate: true
        })
        .state('event-create', {
            cache: false,
            url: "/event/new/:groupId",
            templateUrl: "templates/events/event-create.html",
            controller: 'CreateEventController',
            resolve: {
                groupId: function ($stateParams) {
                    return $stateParams.groupId;
                }
            },
            authenticate: true
        })
        .state('event-repeat', {
            cache: false,
            url: "/event/:eventId",
            templateUrl: "templates/events/event-repeat.html",
            controller: 'RepeatEventController',
            resolve: {
                eventId: function ($stateParams) {
                    return $stateParams.eventId;
                }
            }
            ,
            authenticate: true
        })
        .state('event-edit', {
            cache: false,
            url: "/event/:eventId",
            templateUrl: "templates/events/event-edit.html",
            controller: 'EditEventController',
            resolve: {
                eventId: function ($stateParams) {
                    return $stateParams.eventId;
                }
            }
            ,
            authenticate: true
        })
        .state('event-participation', {
            cache: true,
            url: "/event/:eventId/participations",
            templateUrl: "templates/events/participations-list.html",
            controller: 'ParticipationListController',
            resolve: {
                eventId: function ($stateParams) {
                    return $stateParams.eventId;
                }
            }
            ,
            authenticate: true
        })
        .state('event-comments', {
            cache: true,
            url: "/event/:eventId/comments",
            templateUrl: "templates/events/event-comments.html",
            controller: 'EventCommentsController',
            resolve: {
                eventId: function ($stateParams) {
                    return $stateParams.eventId;
                }
            }
            ,
            authenticate: true
        })
        .state('event-sponsors', {
            cache: false,
            url: "/events/:eventId/sponsors",
            templateUrl: "templates/events/event-sponsors.html",
            controller: 'EventSponsorsController',
            resolve: {
                eventId: function ($stateParams) {
                    return $stateParams.eventId;
                }
            },
            authenticate: true
        })
    ;

});
