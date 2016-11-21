var fspModule = angular.module('fspApp', ['ui.bootstrap']);

fspModule.controller('fspController', function ($scope,$http, $timeout) {

    $scope.totalItems = 1;
    $scope.currentPage = 1;
    $scope.limit = 20;
    $scope.since = 0;

    $scope.entries = [];
    $scope.screens = "Скрины";
    $scope.gap = 0;
    $scope.update = function () {
        $scope.currentPage = 1;
        $scope.getEntries();
    };
    $http.defaults.headers.post["Content-Type"] = "application/json";

    $scope.getEntries = function() {
        //get all entries and display initially
        $http.get('/entries?page=' + ($scope.currentPage - 1) + "&sort=timestamp,desc").
        success(function (data) {
            if (data.content != undefined) {
                $scope.totalItems = data.totalElements;
                angular.copy(data.content,  $scope.entries);
                if ($scope.currentPage == 1) {
                    $scope.since = $scope.entries[0].timestamp;
                    $scope.screens = "Скрины";
                }
            } else {
                $scope.entries = [];
            }
            for (i = 0; i < $scope.entries.length; i++) {
                $scope.entries[i].time = $scope.formatDate($scope.entries[i].timestamp);
            }
        });
    };
    $scope.formatDate = function(timestamp) {
        var d = new Date(timestamp);
        return ("0" + d.getDate()).slice(-2) + "-" + ("0"+(d.getMonth()+1)).slice(-2) + "-" +
            d.getFullYear() + " " + ("0" + d.getHours()).slice(-2) + ":" + ("0" + d.getMinutes()).slice(-2);


    };
    $scope.pageChanged = function() {
        $scope.getEntries();
    };
    $scope.deleteImage = function(entryid) {
        $http.delete('/entries/' + entryid).success(
            function (data, status, headers) {
                $scope.getEntries();
            }
        ).error(

        )
    };
    $scope.asknew = function() {
        $http.get('/entries/newcount?since=' + $scope.since).
        success(function (data) {
            if (data != undefined) {
                if (data > 0) {
                    $scope.screens = "Скрины обновить (" + data + " новых)";
                }
            }
        });
        $http.get('/gap').success(
            function(data) {
                if (data != undefined) {
                    $scope.gap = data;
                }
            }
        );
        $timeout($scope.asknew, 20000);
    };
    $scope.overtake = function() {
        $http.post('/overtake');
    };

    $timeout($scope.asknew, 20000);
    $scope.getEntries();
});


fspModule.directive('targetBlank', function() {
    return {
        compile: function (element) {
            var elems = (element.prop("tagName") === 'A') ? element : element.find('a');
            elems.attr("target", "_blank");
        }
    };
});