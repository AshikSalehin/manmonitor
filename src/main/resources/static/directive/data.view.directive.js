(function () {
    "use strict"
    var app = angular.module("manmon");
    app.controller("newDirectiveCtrl", newDirectiveCtrl);
    function newDirectiveCtrl($scope) {
        debugger
        $scope.array2 = [];
        $scope.array2 = $scope.arrayValue;
    }
    app.directive("newDirective", newDirective);
    function newDirective() {
        return {
            restrict: 'E',
            transclude: true,
            controller: "newDirectiveCtrl",
            templateUrl: '/directive/dataView.html',
            scope: {
                arrayValue: "="
            }
        };
    }

})();
