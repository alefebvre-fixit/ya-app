angular.module('ya-app').config(function ($stateProvider) {
    $stateProvider
        .state('tabs.groups', {
            cache: true,
            url: "/groups",
            views: {
                'tab-groups': {
                    templateUrl: "templates/groups/group-list.html",
                    controller: 'ListGroupsController'
                }
            },
            authenticate: true
        })
        .state('group', {
            cache: true,
            url: "/groups/:groupId",
            templateUrl: "templates/groups/group-view.html",
            controller: 'ViewGroupController',
            resolve: {
                groupId: function ($stateParams) {
                    return $stateParams.groupId;
                }
            },
            authenticate: true
        })
        .state('group-create', {
            cache: true,
            url: "/group/new",
            templateUrl: "templates/groups/group-create.html",
            controller: 'CreateGroupController',
            authenticate: true
        })
        .state('group-edit', {
            cache: false,
            url: "/group/:groupId",
            templateUrl: "templates/groups/group-edit.html",
            controller: 'EditGroupController',
            resolve: {
                groupId: function ($stateParams) {
                    return $stateParams.groupId;
                }
            }
            ,
            authenticate: true
        })
        .state('group-followers', {
            cache: false,
            url: "/groups/:groupId/followers",
            templateUrl: "templates/groups/group-followers.html",
            controller: 'GroupFollowersController',
            resolve: {
                groupId: function ($stateParams) {
                    return $stateParams.groupId;
                }
            },
            authenticate: true
        })
        .state('group-sponsors', {
            cache: false,
            url: "/groups/:groupId/sponsors",
            templateUrl: "templates/groups/group-sponsors.html",
            controller: 'GroupSponsorsController',
            resolve: {
                groupId: function ($stateParams) {
                    return $stateParams.groupId;
                }
            },
            authenticate: true
        })
        .state('group-sponsors-edit', {
            cache: false,
            url: "/groups/:groupId/sponsors/edit",
            templateUrl: "templates/groups/group-sponsors-edit.html",
            controller: 'GroupSponsorsEditController',
            resolve: {
                groupId: function ($stateParams) {
                    return $stateParams.groupId;
                }
            },
            authenticate: true
        })
        .state('group-events', {
            url: "/groups/:groupId/events",
            templateUrl: "templates/groups/group-event-list.html",
            controller: 'GroupEventsController',
            resolve: {
                groupId: function ($stateParams) {
                    return $stateParams.groupId;
                }
            },
            authenticate: true
        })
        /*
        .state('ya.discover', {
            url: "/discover",
            views: {
                'tab-groups': {
                    templateUrl: "templates/groups/discover.html",
                    controller: 'DiscoverGroupController',
                    resolve: {
                        groups: function ($rootScope, groupService) {
                            return groupService.getGroups();

                        }
                    }
                }

            },
            authenticate: true
        })
        .state('ya.group-comments', {
            cache: false,
            url: "/groups/:groupId/comments",
            views: {
                'tab-groups': {
                    templateUrl: "templates/groups/group-comments.html",
                    controller: 'groupCommentsController',
                    resolve: {
                        groupId: function ($stateParams) {
                            return $stateParams.groupId;
                        }
                    }
                }
            },
            authenticate: true
        })
        .state('ya.group-followers', {
            cache: false,
            url: "/groups/:groupId/followers",
            views: {
                'tab-groups': {
                    templateUrl: "templates/groups/group-followers.html",
                    controller: 'groupFollowersController',
                    resolve: {
                        groupId: function ($stateParams) {
                            return $stateParams.groupId;
                        }
                    }
                }
            },
            authenticate: true
        })
        */
    ;

});
