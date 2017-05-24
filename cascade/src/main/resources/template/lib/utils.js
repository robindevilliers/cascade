
function queryParameters(){
    var queryString = window.location.search.substring(1);
    var params = {};

    if (queryString.trim().length > 0){
        _.forEach(queryString.split('&'), function(token){
            var i = token.indexOf("=");
            params[decodeURI(token.substring(0,i))] = decodeURI(token.substring(i + 1));
        })
    }
    return params;
}