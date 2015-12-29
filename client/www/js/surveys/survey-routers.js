angular.module('ya-app').config(function ($stateProvider) {
    $stateProvider
        .state('survey-create', {
            cache: false,
            url: "/survey/new/:eventId",
            templateUrl: "templates/surveys/survey-create.html",
            controller: 'CreateSurveyController',
            resolve: {
                eventId: function ($stateParams) {
                    return $stateParams.eventId;
                }
            },
            authenticate: true
        })
        .state('survey-edit', {
            cache: false,
            url: "/surveys/:surveyId",
            templateUrl: "templates/surveys/survey-edit.html",
            controller: 'EditSurveyController',
            resolve: {
                surveyId: function ($stateParams) {
                    return $stateParams.surveyId;
                }
            },
            authenticate: true
        })
    ;

});
