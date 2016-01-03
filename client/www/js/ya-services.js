angular.module('ya-app').factory('YaService',
    ['$rootScope', '$log', '$cordovaToast', '$ionicLoading', 'YaConfig', '$http',
        function($rootScope, $log, $cordovaToast, $ionicLoading, YaConfig, $http) {
            var resultService;

            var startLoading =  function(){
                $ionicLoading.show({
                    template: '<ion-spinner class="spinner-calm"></ion-spinner>'
                });
            };

            var stopLoading =  function(){
                $ionicLoading.hide();
            };

            var sanitize = function(){
                startLoading();
                return $http.get(YaConfig.url + '/admin/user/sanitize').then(function (response) {
                    stopLoading();
                    return response.data;
                });
            };

            var isAuthenticated = function(){
                if ($rootScope.userInfo != null){
                    return true;
                } else {
                    var userInfo = getUserInfo();

                    if (userInfo && userInfo.token){
                        installUserInfo(userInfo);
                        return true;
                    }
                    return false;
                }
            };

            var signOut = function(){
                localStorage.removeItem("YaUserInfo");
                delete $http.defaults.headers.common['x-auth-token'];
                delete $rootScope.favorites;
                delete $rootScope.following;
                delete $rootScope.userInfo;
            };

            var getUserInfo = function(){
                return JSON.parse(window.localStorage['YaUserInfo']);
            };

            var setUserInfo = function(userInfo){
                window.localStorage.setItem("YaUserInfo", JSON.stringify(userInfo));
            };

            var installUserInfo = function(userInfo){

                if (userInfo) {
                    window.localStorage.setItem("YaUserInfo", JSON.stringify(userInfo));
                    $http.defaults.headers.common['x-auth-token'] = userInfo.token;
                    $rootScope.favorites = userInfo.followingGroups;
                    $rootScope.following = userInfo.followingUsers;
                    $rootScope.userInfo = userInfo;

                } else {
                    signOut();
                }

            };

            var getUsername = function() {
                return $rootScope.userInfo.user.username;
            };

            var setFavorites = function(favorites){
                $log.debug("setFavorites from service" + favorites);

                $rootScope.favorites = favorites;

                var userInfo = getUserInfo();
                if (userInfo){
                    userInfo.followingGroups = favorites;
                    setUserInfo(userInfo);
                }
            };


            var setFollowing = function(following){
                $log.debug("setFollowing from YaService following=" + following);

                $rootScope.following = following;

                var userInfo = getUserInfo();
                if (userInfo){
                    userInfo.followingUsers = following;
                    setUserInfo(userInfo);
                }

            };


            resultService = {
                toastMe: function(message) {
                    if (YaConfig.enablePlugin){
                        $cordovaToast.showShortBottom(message, 'short', 'center').then(
                            function(success) {
                                // success
                            }, function(error) {
                                // error
                            });
                    } else {
                        $log.debug('$cordovaToast will show:' + message);
                    }
                },
                startLoading: startLoading,
                stopLoading: stopLoading,
                installUserInfo: installUserInfo,
                setFavorites: setFavorites,
                setFollowing: setFollowing,
                sanitize: sanitize,
                isFavorite: function(group){
                    if (group){
                        $log.debug("isFavorite " + group.id);
                        return ($rootScope.favorites.indexOf(group.id) >= 0);
                    }
                    return false;
                },
                addFollowing: function(username){
                    $log.debug("addFollowing from service" + username);

                    if (!$rootScope.following){
                        $rootScope.following = [];
                    }
                    $rootScope.following.push(username);

                },
                removeFollowing: function(username){
                    $log.debug("removeFollowing from service" + username);

                    if ($rootScope.following){
                        var index = $rootScope.following.indexOf(username);
                        if (index > -1) {
                            $rootScope.following.splice(index, 1);
                        }
                    }

                },

                isFollowing: function(username){
                    //$log.debug("call isFollowing from YaService following =" + username);
                    if (username && $rootScope.following){
                        return ($rootScope.following.indexOf(username) >= 0);
                    }
                    return false;
                },
                isMine: function(group){
                    if (group){
                        if (group.username){
                            return (group.username == $rootScope.user.username);
                        }
                        else if (group.contributor){
                            return (group.contributor == $rootScope.user.username);
                        }
                    }
                    return false;
                },
                getUsername: getUsername,
                isAuthenticated: isAuthenticated,
                signOut: signOut,
                getThemes: function(){
                    return [{type: 'Coffee', selected: false},
                        {type: 'Game', selected: false},
                        {type: 'Music', selected: false},
                        {type: 'Party', selected: false},
                        {type: 'Restaurant', selected: false},
                        {type: 'Shopping', selected: false},
                        {type: 'Soccer', selected: false},
                        {type: 'Video-Game', selected: false}];
                }
            };
            return resultService;
        }]);



