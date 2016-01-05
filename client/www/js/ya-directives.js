angular.module('ya-app')
    .directive('yaAvatar', function () {
        return {
            restrict: 'A',
            scope: {
                user: '=avatar'
            },
            link: function (scope, element, attrs) {

                var createTag = function () {
                    var size = isNotNull(attrs.size) ? attrs.size : 50;

                    var facebookId = scope.user.facebookId;
                    var gravatarId = scope.user.gravatarId;

                    var tag = '';
                    if ((facebookId !== null) && (facebookId !== undefined) && (facebookId !== '')) {
                        tag = '<img src="http://graph.facebook.com/' + facebookId + '/picture?width=' + size + '&height=' + size + '" >';
                    } else if ((gravatarId !== null) && (gravatarId !== undefined) && (gravatarId !== '')) {
                        var src = 'https://secure.gravatar.com/avatar/' + gravatarId + '?s=' + size + '&d=mm';
                        tag = '<img src=' + src + ' >';
                    } else {
                        tag = '<span>Hello Avatar</span>';
                    }
                    return tag;
                };

                scope.$watch('user', function (user) {
                    if (isNotNull(user)) {
                        element.attr('src', computeImageSrc(user));
                    }
                });

                var computeImageSrc = function (user) {
                    var size = isNotNull(attrs.size) ? attrs.size : 50;

                    var facebookId = user.facebookId;
                    var gravatarId = user.gravatarId;

                    var result = '';

                    if ((facebookId !== null) && (facebookId !== undefined) && (facebookId !== '')) {
                        result = 'http://graph.facebook.com/' + facebookId + '/picture?width=' + size + '&height=' + size;
                        //} else if ((gravatarId !== null) && (gravatarId !== undefined) && (gravatarId !== '')) {
                        //    result = 'https://secure.gravatar.com/avatar/' + gravatarId + '?s=' + size +'&d=mm';
                    } else {
                        return getLetterAvatar(user.username);
                    }

                    return result;
                };


                var getLetterAvatar = function (username) {

                    var params = {
                        alphabetcolors: ["#5A8770", "#B2B7BB", "#6FA9AB", "#F5AF29", "#0088B9", "#F18636", "#D93A37", "#A6B12E", "#5C9BBC", "#F5888D", "#9A89B5", "#407887", "#9A89B5", "#5A8770", "#D33F33", "#A2B01F", "#F0B126", "#0087BF", "#F18636", "#0087BF", "#B2B7BB", "#72ACAE", "#9C8AB4", "#5A8770", "#EEB424", "#407887"],
                        textColor: '#ffffff',
                        defaultBorder: 'border:5px solid white',
                        fontsize: 30, // unit in pixels
                        height: 50, // unit in pixels
                        width: 50, // unit in pixels
                        fontWeight: 400, //
                        charCount: 1,
                        fontFamily: 'HelveticaNeue-Light,Helvetica Neue Light,Helvetica Neue,Helvetica, Arial,Lucida Grande, sans-serif',
                        base: 'data:image/svg+xml;base64,',
                        radius: 'border-radius:30px;'
                    };

                    var c = username.substr(0, params.charCount).toUpperCase();
                    var cobj = getCharacterObject(c, params.textColor, params.fontFamily, params.fontWeight, params.fontsize);
                    var colorIndex = '';
                    var color = '';

                    if (c.charCodeAt(0) < 65) {
                        color = getRandomColors();
                    } else {
                        colorIndex = Math.floor((c.charCodeAt(0) - 65) % params.alphabetcolors.length);
                        color = params.alphabetcolors[colorIndex];
                    }


                    var svg = getImgTag(params.width, params.height, color);
                    svg.append(cobj);
                    var lvcomponent = angular.element('<div>').append(svg.clone()).html();
                    var svgHtml = window.btoa(unescape(encodeURIComponent(lvcomponent)));
                    var base = params.base;

                    return base + svgHtml;
                };


                var getRandomColors = function () {
                    var letters = '0123456789ABCDEF'.split('');
                    var _color = '#';
                    for (var i = 0; i < 6; i++) {
                        _color += letters[Math.floor(Math.random() * 16)];
                    }
                    return _color;
                };


                var getImgTag = function (width, height, color) {

                    var svgTag = angular.element('<svg></svg>')
                        .attr({
                            'xmlns': 'http://www.w3.org/2000/svg',
                            'pointer-events': 'none',
                            'width': width,
                            'height': height
                        })
                        .css({
                            'background-color': color,
                            'width': width + 'px',
                            'height': height + 'px'
                        });

                    return svgTag;
                };

                var getCharacterObject = function (character, textColor, fontFamily, fontWeight, fontsize) {
                    var textTag = angular.element('<text text-anchor="middle"></text>')
                        .attr({
                            'y': '50%',
                            'x': '50%',
                            'dy': '0.35em',
                            'pointer-events': 'auto',
                            'fill': textColor,
                            'font-family': fontFamily
                        })
                        .html(character)
                        .css({
                            'font-weight': fontWeight,
                            'font-size': fontsize + 'px',
                        });

                    return textTag;
                };


            }
        };
    });



