angular.module('ya-app').controller('SignUpController', ['YaService', 'UserService', '$scope', '$log', '$state', 'signup',
    function (YaService, UserService, $scope, $log,  $state, signup) {
        $log.debug("Enter SignUpController");

        $scope.signup = signup;

        $scope.doSignUp = function () {

            UserService.signupUser($scope.signup).success(function (data) {
                YaService.setUser(data);
                $state.transitionTo('tabs.events');
            }).error(function (response, status) {
                $log.debug("Invalid sign-up");
                $scope.signup.error = 'Invalid sign-up';
            });
        };

        $scope.goToSignIn = function () {
            $state.transitionTo('sign-in');
        };
    }
]);

angular.module('ya-app').controller('SignInController', ['YaService', 'UserService', '$scope', '$rootScope', '$log', '$state', 'NotificationService', '$cordovaOauth', '$http', 'GroupService',
    function (YaService, UserService, $scope, $rootScope, $log, $state, NotificationService, $cordovaOauth, $http, GroupService) {

        $scope.signin = {username: 'antoinelefebvre', password: 'password'};


        var initialize = function(user){

            //Cache initialization
            NotificationService.getNotifications().then(function (notifications) {
                var notificationSize = Object.keys(notifications).length-1;
                if (notificationSize < 0) notificationSize = 0;
                $rootScope.badgecount = notificationSize;
            });

            //Cache initialization
            UserService.getUsers().then(function (users) {
            });

            //Cache initialization
            GroupService.getGroups().then(function (groups) {
            });

            $state.transitionTo('tabs.events');
            YaService.stopLoading();
        };


        $scope.doSignIn = function(){
            YaService.startLoading();
            UserService.signInEmail($scope.signin).success(function (userInfo) {
                YaService.installUserInfo(userInfo);
                initialize(userInfo.user);
            }).error(function (response, status) {
                YaService.stopLoading();
                $scope.signin.error = status + response;
            });
        };


        $scope.goToSignUp = function () {
            $state.go('sign-up');
        };

        $scope.googleLogin = function() {
            YaService.startLoading();
            $cordovaOauth.google("55883713895-e9egmn26h1ilo7n8msj9ptsfs48dagp3.apps.googleusercontent.com", ["profile"]).then(function(result) {
                $log.debug(JSON.stringify(result));
                $scope.signin.error = JSON.stringify(result);

                UserService.signInGoogle(result).success(function (user) {
                    YaService.stopLoading();
                }).error(function (response, status) {
                    YaService.stopLoading();
                    $log.debug("Invalid username or password");
                    $scope.signin.error = 'Invalid username or password';
                });

                YaService.stopLoading();
            }, function(error) {
                $log.debug(error);
                YaService.stopLoading();
                $scope.signin.error = error;
            });
        };

        $scope.facebookLogin = function() {

            YaService.startLoading();

            if (UserService.getAccessToken()){
                var signin = {token: UserService.getAccessToken(), expiration: ''};
                UserService.signInFacebook(signin).success(function (userInfo) {
                    YaService.installUserInfo(userInfo);
                    initialize(userInfo.user);
                }).error(function (response, status) {
                    YaService.stopLoading();
                    $log.debug("Invalid username or password");
                    $scope.signin.error = 'Invalid username or password';
                });
            } else {
                $cordovaOauth.facebook("1489020631407250", ["email", "public_profile"]).then(function(result) {
                    var signin = {token: result.access_token, expiration: result.expires_in};

                    UserService.signInFacebook(signin).success(function(userInfo) {
                        YaService.installUserInfo(userInfo);
                        initialize(userInfo.user);
                    }).error(function (response, status) {
                        YaService.stopLoading();
                        $log.debug("Invalid username or password");
                        $scope.signin.error = 'Invalid username or password';
                    });
                }, function(error) {
                    YaService.stopLoading();
                    $rootScope.logindata = error;
                });
            }



        };



    }
]);



angular.module('ya-app').controller('EditUserController', ['YaService', 'UserService', '$scope', '$log', 'profile','$state', '$ionicModal',
    function (YaService, UserService, $scope, $log, profile, $state, $ionicModal) {

        $scope.profile = profile;

        $scope.saveProfile = function(profile) {
            YaService.startLoading();
            UserService.saveProfile(profile).then(function (data) {
                YaService.setUser(data);
                $scope.profile = data.profile;
                YaService.stopLoading();
                YaService.toastMe('Setting Updated');
                $state.go("user", {username : $scope.user.username});
            });
        };

        //Start Modal theme selector

        $ionicModal.fromTemplateUrl('templates/groups/theme-selector-modal.html', {
            scope: $scope,
            animation: 'slide-in-up'
        }).then(function(modal) {
            $scope.themeSelector = modal;
        });

        $scope.openSelector = function() {
            $scope.images = angular.copy(YaService.getThemes());
            $scope.allowMultipleSelection = true;
            var arrayLength = $scope.images.length;
            $log.debug($scope.profile.interest);
            if ($scope.profile.interest){
                for (var i = 0; i < arrayLength; i++) {

                    $log.debug($scope.images[i].type);

                    if ($scope.profile.interest.indexOf($scope.images[i].type) >= 0){
                        $scope.images[i].selected = true;
                    }
                }
            }

            $scope.themeSelector.show();
        };

        $scope.cancelSelection = function() {
            $scope.themeSelector.hide();
        };

        //Cleanup the modal when we're done with it!
        $scope.$on('$destroy', function() {
            if ($scope.themeSelector){
                $scope.themeSelector.remove();
            }
        });

        $scope.selectTheme = function(type){
            var arrayLength = $scope.images.length;
            for (var i = 0; i < arrayLength; i++) {
                if (type == $scope.images[i].type){
                    $scope.images[i].selected = !$scope.images[i].selected;
                }
            }
        };

        $scope.applySelection = function(){
            $scope.profile.interest = [];
            var arrayLength = $scope.images.length;
            for (var i = 0; i < arrayLength; i++) {
                if ($scope.images[i].selected){
                    $scope.profile.interest.push($scope.images[i].type);
                }
            }
            $scope.themeSelector.hide();
        };

        //End Modal for theme selector

    }
]);


angular.module('ya-app').controller('UserController', ['YaService', '$scope', '$log', 'UserService', 'username', '$ionicPopup', '$ionicPopover','$state',
    function (YaService, $scope, $log, UserService, username, $ionicPopup, $ionicPopover, $state) {

        $scope.canEdit = function(user){
            return UserService.canEdit(user);
        };

        $scope.summary = {groupSize : '-', followingSize : '-', followerSize : '-', user: {}, followingGroups : []};

        var reload = function(username){
            UserService.getUser(username).then(function (summary) {
                $scope.summary.user = summary;
            });

            UserService.getFollowingGroupsSize(username).then(function (groupSize) {
                $scope.summary.groupSize = groupSize;
            });

            UserService.getFollowersSize(username).then(function (followerSize) {
                $scope.summary.followerSize = followerSize;
            });

            UserService.getFollowingSize(username).then(function (followingSize) {
                $scope.summary.followingSize = followingSize;
            });

            UserService.getFollowingGroups(username).then(function (groups) {
                $scope.summary.followingGroups = groups;
            });

        };

        //To insure the back button is displayed
        $scope.$on('$ionicView.beforeEnter', function (event, viewData) {
            viewData.enableBack = true;
            reload(username);
        });

        $scope.follow = function(username){
            $log.debug("follow=" + username);
            UserService.follow(username).then(function (data) {
                YaService.addFollowing(username);
                UserService.getFollowersSize(username).then(function (followerSize) {
                    $scope.summary.followerSize = followerSize;
                });
            });
        };

        $scope.unfollow = function(username){
            var confirmPopup = $ionicPopup.confirm({
                title: 'Unfollow',
                template: 'Stop following ' + username + ' ?',
                buttons: [
                    { text: 'Cancel' },
                    {
                        text: '<b>Ok</b>',
                        type: 'button-calm',
                        onTap: function(e) {
                            return true;
                        }
                    }
                ]
            });
            confirmPopup.then(function(res) {
                if(res) {
                    $log.debug("unfollow=" + username);
                    UserService.unfollow(username).then(function (data) {
                        YaService.removeFollowing(username);
                        UserService.getFollowersSize(username).then(function (followerSize) {
                            $scope.summary.followerSize = followerSize;
                        });
                    });
                }
            });
        };

        $scope.isFollowing = function(username){
            return YaService.isFollowing(username);
        };

        $ionicPopover.fromTemplateUrl('templates/users/partial/user-popover.html', {
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

        $scope.openEditAccount = function(user){
            $scope.closePopover();
            $state.go("tabs.settings");
        };
    }
]);


angular.module('ya-app').controller('FollowersController', ['UserService', '$scope', '$log', 'username',
    function (UserService, $scope, $log, username) {

        $scope.$on('$ionicView.beforeEnter', function (event, viewData) {
            UserService.getFollowers(username).then(function (followers) {
                $scope.users = followers;
            });
        });

        $scope.doRefresh = function() {
            UserService.getFollowers(username).then(function (followers) {
                $scope.users = followers;
                $scope.$broadcast('scroll.refreshComplete');
            });
        };


    }
]);

angular.module('ya-app').controller('FollowingController', ['UserService', '$scope', '$log', 'username',
    function (UserService, $scope, $log, username) {

        $scope.$on('$ionicView.beforeEnter', function (event, viewData) {
            UserService.getFollowing(username).then(function (followers) {
                $scope.users = followers;
            });
        });

        $scope.doRefresh = function() {
            UserService.getFollowing(username).then(function (followers) {
                $scope.users = followers;
                $scope.$broadcast('scroll.refreshComplete');
            });
        };


    }
]);


angular.module('ya-app').controller('UserGroupController', ['UserService', '$scope', '$log', 'username',
    function (UserService, $scope, $log, username) {

        $scope.$on('$ionicView.beforeEnter', function (event, viewData) {
            UserService.getFollowingGroups(username).then(function (groups) {
                $scope.groups = groups;
            });
        });

        $scope.doRefresh = function() {
            UserService.getFollowingGroups(username).then(function (groups) {
                $scope.groups = groups;
                $scope.$broadcast('scroll.refreshComplete');
            });
        };


    }
]);


angular.module('ya-app').controller('UserDiscoveryController', ['UserService', '$scope', '$log',
    function (UserService, $scope, $log) {

        $scope.$on('$ionicView.beforeEnter', function (event, viewData) {
            UserService.getUserDiscovery().then(function (users) {
                $scope.users = users;
            });
        });

        $scope.doRefresh = function() {
            UserService.getUserDiscovery().then(function (users) {
                $scope.users = users;
            });
        };


    }
]);