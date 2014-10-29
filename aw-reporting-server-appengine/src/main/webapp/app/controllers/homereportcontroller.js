(function () {

    function HomeReportController ($scope, $location, coreProvider) {

        $scope.loading_task_count = 1;
        $scope.load_templates_error = "";
        $scope.templates = [];
        $scope.i_can_edit = coreProvider.i_can_edit_public_templates();

        coreProvider.getTemplates(function (err, templates) {
            if (!err) {
                $scope.templates = templates;
            } else {
                $scope.load_templates_error = "Unable to load your Templates (" + err.error + ": " + err.message + ")";
            }

            $scope.loading_task_count--;
        });

        $scope.createTemplate = function () {
          $location.path("/template");  
        };
    }

    gae2App.controller("HomeReportController", HomeReportController);

})();
