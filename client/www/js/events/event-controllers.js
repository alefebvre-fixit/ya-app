angular.module('ya-app').controller('ListEventsController',
    ['EventService', '$scope',
        function (EventService, $scope) {

            $scope.$on('$ionicView.beforeEnter', function (event, viewData) {
                EventService.getEventTimeline().then(function (timeline) {
                    $scope.timeline = timeline;
                });
            });

            $scope.doRefresh = function() {
                EventService.getEventTimeline().then(function (timeline) {
                    $scope.timeline = timeline;
                    $scope.$broadcast('scroll.refreshComplete');
                });
            };

        }
    ]);

angular.module('ya-app').controller('ParticipationListController',
    ['EventService', '$scope', 'eventId', '$filter',
        function (EventService, $scope, eventId, $filter) {

            $scope.participations = {all : [], in : [], out : [], rsvp : []};

            $scope.$on('$ionicView.beforeEnter', function (event, viewData) {
                viewData.enableBack = true;
                EventService.getParticipationSummary(eventId).then(function (summary) {
                    $scope.participations = summary;
                });
            });

            $scope.doRefresh = function() {
                EventService.getParticipationSummary(eventId).then(function (summary) {
                    $scope.participations = summary;
                    $scope.$broadcast('scroll.refreshComplete');
                });
            };
        }
    ]);



angular.module('ya-app').controller('EditEventController',
    ['EventService', '$scope', '$log', '$state', 'eventId','YaService',
        function (EventService, $scope, $log, $state, eventId, YaService) {

            $scope.picker = {date: new Date() , time: new Date()};

            EventService.getEvent(eventId).then(function(event) {
                $scope.event = event;
                event.date = new Date(event.date);
            });

            $scope.saveEvent = function(form) {
                // If form is invalid, return and let AngularJS show validation errors.
                if (form.$invalid) {
                    return;
                }
                YaService.startLoading();
                EventService.saveEvent($scope.event).then(function(data){
                    YaService.stopLoading();
                    $state.go('event', {eventId: data.id});
                });

            };


        }
    ]);

angular.module('ya-app').controller('RepeatEventController',
    ['EventService', '$scope', '$log', '$state', 'eventId','YaService',
        function (EventService, $scope, $log, $state, eventId, YaService) {

            $scope.picker = {date: new Date() , time: new Date()};

            EventService.getEvent(eventId).then(function(event) {
                $scope.original = event;
                $scope.event= angular.copy(event);
                $scope.event.date = new Date();
            });

            $scope.saveEvent = function(form) {
                // If form is invalid, return and let AngularJS show validation errors.
                if (form.$invalid) {
                    return;
                }
                YaService.startLoading();
                EventService.saveEvent($scope.event).then(function(data){
                    YaService.stopLoading();
                    $state.go('event', {eventId: data.id});
                });

            };


        }
    ]);

angular.module('ya-app').controller('CreateEventController',
    ['EventService', '$scope', '$log', '$state','$ionicHistory', 'groupId', 'YaService',
        function (EventService, $scope, $log, $state, $ionicHistory, groupId, YaService) {

            $scope.picker = {date: new Date() , time: new Date()};
            $scope.event = {groupId: groupId};

            EventService.instanciateEvent(groupId).then(function(data){
                $scope.event = data;
                $scope.event.date = new Date();
            });

            $scope.saveEvent = function(form) {
                $log.debug('call saveEvent form is ' + form.$invalid);
                // If form is invalid, return and let AngularJS show validation errors.
                if (form.$invalid) {
                    return;
                }

                YaService.startLoading();
                EventService.saveEvent($scope.event).then(function(event){
                    $ionicHistory.currentView($ionicHistory.backView());
                    YaService.stopLoading();
                    $state.go('event', {eventId: event.id, location: 'replace'});
                });
            };


        }
    ]);

angular.module('ya-app').controller('ViewEventController',
    ['$scope', '$state', '$log', '$ionicPopup', '$ionicModal', '$ionicPopover', 'YaService', 'EventService', 'eventId', 'UserService',
        function ($scope, $state, $log, $ionicPopup, $ionicModal, $ionicPopover,  YaService, EventService, eventId, UserService) {

            $scope.isSponsor = function(event){
                return EventService.canEdit(event);
            };

            $scope.summary = {participationsSize : '-', commentSize : '-', comments: [],  myParticipation : {}, owner: {}, sponsors:[]};

            var reload = function(eventId){
                EventService.getEvent(eventId).then(function(event) {
                    $log.debug("ViewEventController getEvent is called eventId=" + eventId);
                    $scope.event = event;

                    EventService.getUserParticipation(event).then(function(participation) {
                        $log.debug("summary.myParticipation=" + participation.status);
                        $scope.summary.myParticipation = participation;
                    });

                    EventService.getParticipationsSize(event).then(function(size) {
                        $log.debug("summary.participationSize=" + size);
                        $scope.summary.participationsSize = size;
                    });

                    EventService.getParticipationSummary(eventId).then(function (summary) {
                        $scope.participations = summary;
                    });

                    EventService.getCommentSize(event).then(function(size) {
                        $log.debug("summary.getCommentSize=" + size);
                        $scope.summary.commentSize = size;
                    });

                });
            };

            //To insure the back button is displayed
            $scope.$on('$ionicView.beforeEnter', function (event, viewData) {
                viewData.enableBack = true;
                reload(eventId);
            });


            $scope.repeatEvent = function(event) {
                $scope.closePopover();
                $state.go("event-repeat", { eventId: event.id});
            };

            $scope.addSurvey = function(event){
                $scope.closePopover();
                $state.go("survey-create", { eventId: event.id});
            };

            $scope.setEvent =function(newEvent){
                $scope.event = newEvent;
                EventService.getCommentSize(eventId).then(function (data) {
                    $scope.summary.commentSize = data;
                });
                EventService.getComments(eventId).then(function (data) {
                    $scope.summary.comments = data;
                });
            };

            $scope.deleteEvent = function(eventToDelete) {
                $scope.closePopover();

                var confirmPopup = $ionicPopup.confirm({
                    title: 'Delete Event',
                    template: 'Are you sure you want to delete this event?',
                    buttons: [
                        { text: 'Cancel' },
                        {
                            text: '<b>Delete</b>',
                            type: 'button-calm',
                            onTap: function(e) {
                                return true;
                            }
                        }
                    ]
                });
                confirmPopup.then(function(res) {
                    if(res) {
                        EventService.deleteEvent(eventToDelete).then(function () {
                            YaService.toastMe('Event ' + eventToDelete.name + ' deleted.');
                            $state.go('tabs.events');
                        });
                    }
                });
            };

            $scope.openEditEvent = function(event){
                $scope.closePopover();
                $state.go("event-edit", { eventId: event.id});
            };


            //Start Modal RSVP

            $ionicModal.fromTemplateUrl('templates/events/partial/event-participation-modal.html', {
                scope: $scope,
                animation: 'slide-in-up'
            }).then(function(modal) {
                $scope.editRSVP = modal;
            });

            $scope.openRSVP = function(rsvpToUpdate) {
                $scope.rsvp = angular.copy(rsvpToUpdate);

                if (rsvpToUpdate.status == 'OUT'){
                    $scope.rsvp.participation = false;
                } else {
                    $scope.rsvp.participation = true;
                }
                $scope.editRSVP.show();
            };

            $scope.closeRSVP = function() {
                $scope.editRSVP.hide();
            };

            //Cleanup the modal when we're done with it!
            $scope.$on('$destroy', function() {
                if ($scope.editRSVP){
                    $scope.editRSVP.remove();
                }
            });

            // Execute action on hide modal
            $scope.$on('editRSVP.hidden', function() {
                // Execute action
            });

            // Execute action on remove modal
            $scope.$on('editRSVP.removed', function() {
                // Execute action
            });

            $scope.saveRSVP = function(rsvp) {
                $log.debug("$scope.participation.value=" + $scope.rsvp.participation);

                if (rsvp.participation){
                    rsvp.status = 'IN';
                } else {
                    rsvp.status = 'OUT';
                }
                delete rsvp.participation;

                EventService.participate(rsvp).then(function(data){
                    $log.debug("rsvp data=");
                    $log.debug(data);
                    $scope.summary.myParticipation = data;
                    $scope.closeRSVP();
                    YaService.toastMe('You are now ' + data.status);
                    reload(rsvp.eventId);
                });
            };

            //End Modal for group edition


            $ionicPopover.fromTemplateUrl('templates/events/partial/event-popover.html', {
                scope: $scope
            }).then(function(popover) {
                $scope.popover = popover;
            });

            $scope.closePopover = function() {
                if ($scope.popover){
                    $scope.popover.hide();
                }
            };
            //Cleanup the popover when we're done with it!
            $scope.$on('$destroy', function() {
                $scope.popover.remove();
            });


            $scope.generateParticipations = function(event){
                $scope.closePopover();
                YaService.startLoading();
                EventService.generateParticipations(event).then(function(data){
                    reload(event.id);
                    YaService.stopLoading();
                });
            };

        }
    ]);

angular.module('ya-app').controller('EventCommentsController', ['EventService', '$scope', 'eventId',
    function (EventService, $scope, eventId) {

        console.log('enter EventCommentsController');

        $scope.comment = {eventId: eventId, content: ''};


        $scope.$on('$ionicView.beforeEnter', function (event, viewData) {
            EventService.getComments(eventId).then(function (comments) {
                $scope.comments = comments;
            });
        });


        $scope.postComment = function(comment) {
            EventService.postComment(comment.eventId, comment.content).then(function (comment) {
                $scope.comments.unshift(comment);
                $scope.comment = {eventId: eventId, content: ''};
            });
        };





    }
]);

angular.module('ya-app').controller('EventSponsorsController', ['EventService', 'UserService', '$scope', '$log', 'eventId',
    function (EventService, UserService, $scope, $log, eventId) {

        //To insure the back button is displayed
        $scope.$on('$ionicView.beforeEnter', function (event, viewData) {
            viewData.enableBack = true;

            EventService.getSponsors(eventId).then(function(sponsors) {
                $scope.sponsors = sponsors;
            });
            EventService.getEvent(eventId).then(function (event) {
                $scope.event = event;
                UserService.getUser(event.username).then(function (user) {
                    $scope.owner = user;
                });
            });
        });

        $scope.canEdit = function(event){
            return EventService.canEdit(event);
        };

    }
]);


angular.module('ya-app').controller('EventLocationController', ['EventService', '$scope', '$log', 'eventId',
    function (EventService, $scope, $log, eventId) {

        //To insure the back button is displayed
        $scope.$on('$ionicView.beforeEnter', function (event, viewData) {
            viewData.enableBack = true;

            EventService.getEvent(eventId).then(function (event) {
                $scope.event = event;
            });
        });


    }
]);