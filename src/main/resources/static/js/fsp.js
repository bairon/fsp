var fspModule = angular.module('fspApp', ['ui.bootstrap']);

fspModule.controller('fspController', function ($scope,$http, $timeout) {

    $scope.totalItems = 1;
    $scope.currentPage = 1;
    $scope.limit = 20;
    $scope.since;

    $scope.entries = [];
    $scope.screens = "Скрины";
    $scope.update = function () {
        getEntries();
    }
    $http.defaults.headers.post["Content-Type"] = "application/json";

    function getEntries() {
        //get all entries and display initially
        $http.get('/entries?page=' + ($scope.currentPage - 1) + "&sort=timestamp,desc").
        success(function (data) {
            if (data.content != undefined) {
                $scope.totalItems = data.totalElements;
                angular.copy(data.content,  $scope.entries);
                $scope.since = $scope.entries[0].timestamp;
            } else {
                $scope.entries = [];
            }
        });
    }
    getEntries();
    $scope.pageChanged = function() {
        getEntries();
    };
    var asknew = function() {
        $http.get('/entries/newcount?since=' + $scope.since).
        success(function (data) {
            if (data != undefined) {
                if (data > 0) {
                    $scope.screens = "Скрины обновить (" + data + " новых)";
                }
            }
        });
        $timeout(asknew, 10000);
    };

    $timeout(asknew, 10000);


});


fspModule.directive('targetBlank', function() {
    return {
        compile: function (element) {
            var elems = (element.prop("tagName") === 'A') ? element : element.find('a');
            elems.attr("target", "_blank");
        }
    };
});