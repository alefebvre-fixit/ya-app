angular.module('ya-app').factory('SurveyService',
    ['$http', 'YaConfig', '$filter', '$log', 'YaService',
        function($http, YaConfig, $filter, $log, YaService) {

            var resultService;
            resultService = {
                getSurvey: function (surveyId) {
                    return $http.get(YaConfig.url + '/survey/' + surveyId).then(function (response) {
                        return response.data;
                    });
                },
                saveSurvey: function (survey) {
                    if (survey.id){
                        return $http.put(YaConfig.url + '/surveys', event).then(function (response) {
                            return response.data;
                        });
                    } else {
                        return $http.post(YaConfig.url + '/surveys', event).then(function (response) {
                            return response.data;
                        });
                    }
                },
                deleteSurvey: function (event) {
                    return $http.delete(YaConfig.url + '/survey/' + surveyId).then(function (response) {
                        return response.data;
                    });
                },
                instanciateSurvey: function (eventId) {
                    return {eventId: eventId, suggestionEnabled: false, multipleChoices: false, proposals: []};
                }
            };
            return resultService;
        }]);


