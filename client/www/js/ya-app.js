// Ionic Starter App




// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.services' is found in services.js
// 'starter.controllers' is found in controllers.js
angular.module('ya-app', ['ionic', 'ngMessages', 'ngCordova', 'angularMoment', 'ion-sticky', 'js-data'])

.run(function($ionicPlatform, $rootScope, $state) {
  $ionicPlatform.ready(function() {

      $rootScope.user = {};

    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    // for form inputs)
    if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
      cordova.plugins.Keyboard.disableScroll(true);

    }
    if (window.StatusBar) {
      // org.apache.cordova.statusbar required
      StatusBar.styleLightContent();
    }

      $rootScope.$on("$stateChangeStart", function(event,
                                                   toState, toParams, fromState, fromParams) {
          console.log("Check Authentication authenticate="
              + toState.authenticate);

          if (toState.authenticate) {
              if (!$rootScope.user || !$rootScope.user.username){
                  $state.go("sign-in");
                  event.preventDefault();
              }
          }
      });

  });
}
);

//angular.module('ya-app').constant('YaConfig', {context : 'production', url : 'https://calm-headland-3125.herokuapp.com', enablePlugin : true, enableDebug : false});
angular.module('ya-app').constant('YaConfig', {context : 'test', url : 'http://localhost:8080/api', enablePlugin : false, enableDebug : true, access_token: 'CAAVKQaHMWpIBAAaNWd5bybmU7raLvONarxkwZCfdItbj6PukTEW1zpXqdh2kvb8pPQCF97lhviWlJ3far0urd8mZBquV7yZCZCbLuy65GMZAteCRzDlZCkIc3x6Ef2HNclPnze5p1l7g29uBZBbZBXLZAzXZA1ii4PtZB2EGbtUwfqxCecuMY9kkixP6pdvH7F1pqQZD'});
//angular.module('ya-app').constant('YaConfig', {context : 'test', url : 'http://localhost:9000/api', enablePlugin : false, enableDebug : true, access_token: 'CAAVKQaHMWpIBAAaNWd5bybmU7raLvONarxkwZCfdItbj6PukTEW1zpXqdh2kvb8pPQCF97lhviWlJ3far0urd8mZBquV7yZCZCbLuy65GMZAteCRzDlZCkIc3x6Ef2HNclPnze5p1l7g29uBZBbZBXLZAzXZA1ii4PtZB2EGbtUwfqxCecuMY9kkixP6pdvH7F1pqQZD'});
//angular.module('ya-app').constant('YaConfig', {context : 'production', url : 'http://vast-gorge-2883.herokuapp.com/api', enablePlugin : true, enableDebug : false});
//angular.module('ya-app').constant('YaConfig', {context : 'simulator', url : 'http://10.0.2.2:9000/api', enablePlugin : true, enableDebug : false});

angular.module('ya-app').config(function($logProvider, YaConfig) {
    $logProvider.debugEnabled(YaConfig.enableDebug);
});

angular.module('ya-app')
    .config(function (DSProvider, DSHttpAdapterProvider, YaConfig) {
        angular.extend(DSProvider.defaults, {});
        if (!YaConfig.enableDebug){
            angular.extend(DSHttpAdapterProvider.defaults, {basePath: YaConfig.url, log: false});
        } else {
            angular.extend(DSHttpAdapterProvider.defaults, {basePath: YaConfig.url});
        }
});


angular.module('ya-app')
    .factory('Group', function (DS) {
        // This code won't execute unless you actually
        // inject "Comment" somewhere in your code.
        // Thanks Angular...
        // Some like injecting actual Resource
        // definitions, instead of just "DS"
        return DS.defineResource('groups');
    })
    .factory('Event', function (DS) {
        return DS.defineResource('events');
    })
    .factory('Notification', function (DS) {
        return DS.defineResource('notifications');
    })
    .factory('User', function (DS) {
        return DS.defineResource({name: 'users', idAttribute: 'username'});
    })
;