angular.module('ya-app').factory('YaService',
    ['$rootScope', '$log', '$cordovaToast', '$ionicLoading', 'YaConfig',
        function($rootScope, $log, $cordovaToast, $ionicLoading, YaConfig) {
            var resultService;
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
                startLoading: function(){
                    $ionicLoading.show({
                        template: '<ion-spinner class="spinner-calm"></ion-spinner>'
                    });
                },
                stopLoading: function(){
                    $ionicLoading.hide();
                },
                setUser: function(user){
                    $rootScope.user = user;
                    if (user){
                        localStorage.setItem("username",user.username);
                    } else {
                        localStorage.removeItem("username");
                    }
                },
                setFavorites: function(favorites){
                    $log.debug("setFavorites from service" + favorites);

                    $rootScope.favorites = favorites;
                },
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
                setFollowing: function(following){
                    $log.debug("setFollowing from YaService following=" + following);
                    $rootScope.following = following;
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
                getUsername: function() {
                    return $rootScope.user.username;
                },
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



