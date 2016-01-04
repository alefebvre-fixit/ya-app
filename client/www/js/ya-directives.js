

angular.module('ya-app')
    .directive('helloWorld', function () {
        return {
            restrict: 'E',
            scope:{
                u:'=user'
            },
            template: '<span>Hello {{u}}</span>'

        }
    });


/*
 angular.module('ya-app')
 .directive('yaAvatar', function () {
 return {
 restrict: 'E',
 template: '<span>Hello {{u}}</span>'

 };
 });
 /*

 angular.module('ya-app').directive('googleplace', function() {
 return {
 require: 'ngModel',
 link: function(scope, element, attrs, model) {
 var options = {
 types: [],
 componentRestrictions: {}
 };
 scope.gPlace = new google.maps.places.Autocomplete(element[0], options);

 google.maps.event.addListener(scope.gPlace, 'place_changed', function() {
 scope.$apply(function() {
 model.$setViewValue(element.val());
 });
 });
 }
 };
 });
 */






angular.module('ya-app')
    .directive('yaAvatar', function () {
        return {
            restrict: 'E',
            replace: true,
            scope: {
                user: '=avatar'
            },
            link:function(scope, element, attrs) {

                console.log("start directive");
                console.log(scope.user.user);

                var size = isNotNull(attrs.size) ? attrs.size : 50;

                var facebookId = scope.user.facebookId;
                var gravatarId = scope.user.gravatarId;

                var tag = '';
                if ((facebookId !== null) && (facebookId !== undefined) && (facebookId !== '')) {
                    tag = '<img src="http://graph.facebook.com/' + facebookId + '/picture?width='+ size + '&height=' + size +'" >';
                } else if ((gravatarId !== null) && (gravatarId !== undefined) && (gravatarId !== '')) {
                    var src = 'https://secure.gravatar.com/avatar/' + gravatarId + '?s=' + size +'&d=mm';
                    tag = '<img src=' + src + ' >';
                } else {
                    tag = '<span>Hello Avatar</span>';
                }
                element.replaceWith(tag);
            }
        };
    });



