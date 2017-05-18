var params = queryParameters();

var breadcrumb = new BreadCrumb('state');

var directory = new Directory(directoryData);

var journey = _.find(directoryData.items, function (journey) {
        return journey.journeyId === params.journeyId;
    });

if (!journey){
   window.location = "index.html"
}
$('#journey-id').html('- ' + journey.journeyId);

var scenario = journey.scenarios[params.index]

var contentTemplate = _.template($("#content-template").text());
$("#content").append(contentTemplate({
    index: params['index'],
    name: scenario.name,
    scope: scenario.scope,
    hasState: scenario.hasState,
    hasTransition: scenario.hasTransition
}));

