
angular.module('ya-app').controller('YaController', ['$scope', '$log', '$rootScope', '$window', '$state', '$ionicPopover', 'YaService',
	function ($scope, $log, $rootScope, $window, $state, $ionicPopover, YaService) {

		$log.debug('Going through YaController');

		$scope.signout = function(){
			closePopover();
            YaService.signOut();
            $state.go('sign-in');
		};

        $scope.test = function(){
            closePopover();
            $state.go('test');
        };

        $scope.goToUser = function(){
            closePopover();
            $state.go('user', {username : YaService.getUsername()});
        };

        $ionicPopover.fromTemplateUrl('templates/users/partial/main-popover.html', {
            scope: $scope
        }).then(function(popover) {
            $scope.popover = popover;
        });

        var closePopover = function() {
            if ($scope.popover){
                $scope.popover.hide();
            }
        };

        //Cleanup the popover when we're done with it!
        $scope.$on('$destroy', function() {
            $scope.popover.remove();
        });

}
]);


angular.module('ya-app').controller('JSDataController',
    ['GroupService', 'EventService', '$scope', '$log','$state','Group',
        function (GroupService, EventService, $scope, $log, $state, Group) {

            $scope.$on('$ionicView.beforeEnter', function (event, viewData) {
                Group.findAll().then(function (groups) {
                    $scope.groups = groups;
                });
            });

            $scope.doRefresh = function() {
                Group.findAll().then(function (groups) {
                    $scope.groups = groups;
                });
                $scope.$broadcast('scroll.refreshComplete');
            };

            $scope.openCreateGroup = function() {
                $state.go('group-create');
            };

        }
    ]);


