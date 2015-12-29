
angular.module('ya-app').controller('CreateSurveyController',
    ['SurveyService', '$scope', '$log', '$state', '$ionicModal', 'eventId','YaService',
        function (SurveyService, $scope, $log, $state, $ionicModal, eventId, YaService) {


            $scope.survey = SurveyService.instanciateSurvey(eventId);


            $scope.saveSurvey = function(form) {
                // If form is invalid, return and let AngularJS show validation errors.
                if (form.$invalid) {
                    return;
                }
                YaService.startLoading();
                SurveyService.saveSurvey($scope.survey).then(function(data){
                    YaService.stopLoading();
                    $state.go('event', {eventId: $scope.survey.eventId});
                });

            };


            //Start Modal proposal
            $ionicModal.fromTemplateUrl('templates/events/survey/survey-proposal-modal.html', {
                scope: $scope,
                animation: 'slide-in-up'
            }).then(function(modal) {
                $scope.proposalModal = modal;
            });

            $scope.createProposal = function() {
                $scope.proposal = {name: '', description: ''};
                $scope.proposalModal.create = true;
                $scope.proposalModal.show();
            };

            $scope.editProposal = function(original) {
                $scope.proposal = angular.copy(original);
                $scope.original = original;
                $scope.proposalModal.create = false;

                $scope.proposalModal.show();
            };

            $scope.closeProposal = function() {
                $scope.proposalModal.hide();
            };

            $scope.removeProposal = function(proposal) {
                var index = $scope.survey.proposals.indexOf(proposal);
                if (index > -1){
                    $scope.survey.proposals.splice(index);
                }
            };

            //Cleanup the modal when we're done with it!
            $scope.$on('$destroy', function() {
                if ($scope.proposalModal){
                    $scope.proposalModal.remove();
                }
            });

            // Execute action on hide modal
            $scope.$on('proposalModal.hidden', function() {
                // Execute action
            });

            // Execute action on remove modal
            $scope.$on('proposalModal.removed', function() {
                // Execute action
            });

            $scope.applyProposal = function(proposal) {
                if ($scope.proposalModal.create){
                    $scope.survey.proposals.push(proposal);
                } else {
                    $scope.original.name = proposal.name;
                    $scope.original.description = proposal.description;
                }
                $scope.proposalModal.hide();
            };

            //End Modal for group edition

        }
    ]);




angular.module('ya-app').controller('EditSurveyController',
    ['SurveyService', '$scope', '$log', '$state', '$ionicModal', 'surveyId','YaService',
        function (SurveyService, $scope, $log, $state, $ionicModal, surveyId, YaService) {

            SurveyService.getSurvey(surveyId).then(function(survey) {
                $scope.survey = survey;
            });

            $scope.saveSurvey = function(form) {
                // If form is invalid, return and let AngularJS show validation errors.
                if (form.$invalid) {
                    return;
                }
                YaService.startLoading();
                SurveyService.saveSurvey($scope.survey).then(function(data){
                    YaService.stopLoading();
                    $state.go('event', {eventId: $scope.survey.id});
                });

            };


            //Start Modal proposal
            $ionicModal.fromTemplateUrl('templates/events/survey/survey-proposal-modal.html', {
                scope: $scope,
                animation: 'slide-in-up'
            }).then(function(modal) {
                $scope.proposalModal = modal;
            });

            $scope.createProposal = function() {
                $scope.proposal = {name: '', description: ''};
                $scope.proposalModal.create = true;
                $scope.proposalModal.show();
            };

            $scope.editProposal = function(original) {
                $scope.proposal = angular.copy(original);
                $scope.original = original;
                $scope.proposalModal.create = false;

                $scope.proposalModal.show();
            };

            $scope.closeProposal = function() {
                $scope.proposalModal.hide();
            };

            $scope.removeProposal = function(proposal) {
                var index = $scope.survey.proposals.indexOf(proposal);
                if (index > -1){
                    $scope.survey.proposals.splice(index);
                }
            };

            //Cleanup the modal when we're done with it!
            $scope.$on('$destroy', function() {
                if ($scope.proposalModal){
                    $scope.proposalModal.remove();
                }
            });

            // Execute action on hide modal
            $scope.$on('proposalModal.hidden', function() {
                // Execute action
            });

            // Execute action on remove modal
            $scope.$on('proposalModal.removed', function() {
                // Execute action
            });

            $scope.applyProposal = function(proposal) {
                if ($scope.proposalModal.create){
                    $scope.survey.proposals.push(proposal);
                } else {
                    $scope.original.name = proposal.name;
                    $scope.original.description = proposal.description;
                }
                $scope.proposalModal.hide();
            };

            //End Modal for group edition

        }
    ]);

