var fspModule = angular.module('fspApp', ['ngAnimate']);

fspModule.controller('fspController', function ($scope,$http) {

    var urlBase="";
    $scope.selection = [];
    $http.defaults.headers.post["Content-Type"] = "application/json";

    function findAllEntries() {
        //get all entries and display initially
        $http.get(urlBase + '/api/entries?page=1').
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

//Angularjs Directive for confirm dialog box
fspModule.directive('ngConfirmClick', [
    function(){
        return {
            link: function (scope, element, attr) {
                var msg = attr.ngConfirmClick || "Are you sure?";
                var clickAction = attr.confirmedClick;
                element.bind('click',function (event) {
                    if ( window.confirm(msg) ) {
                        scope.$eval(clickAction);
                    }
                });
            }
        };
    }]);
fspModule.directive('targetBlank', function() {
    return {
        compile: function (element) {
            var elems = (element.prop("tagName") === 'A') ? element : element.find('a');
            elems.attr("target", "_blank");
        }
    };
});