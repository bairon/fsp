var fspModule = angular.module('fspApp', ['ngAnimate']);

fspModule.controller('fspController', function ($scope,$http) {

    var urlBase="";
    $scope.selection = [];
    $http.defaults.headers.post["Content-Type"] = "application/json";

    function findAllEntries() {
        //get all entries and display initially
        $http.get(urlBase + '/entries?page=0').
        success(function (data) {
            if (data.content != undefined) {
                $scope.entries = data.content;
            } else {
                $scope.entries = [];
            }
        });
    }

    findAllEntries();
});


fspModule.directive('targetBlank', function() {
    return {
        compile: function (element) {
            var elems = (element.prop("tagName") === 'A') ? element : element.find('a');
            elems.attr("target", "_blank");
        }
    };
});