
var directory = new Directory(directoryData);

var journey = findJourney();
$('#journey-id').html('- ' + journey.journeyId);

renderTabs();
renderSynopsisPanel();
renderFilterPanel();
renderAnalysisPanel();

function findJourney() {
    var queryString = window.location.search;
    var i = queryString.indexOf("=");
    var journeyId = queryString.substring(i + 1);
    return _.find(directoryData.items, function (journey) {
        return journey.journeyId === journeyId;
    });
}

function renderTabs() {
    var tabs = [];
    tabs.push({active: false, id: "synopsis", title: "Synopsis", panel: "default"});
    tabs.push({active: false, id: "filter", title: "Filter", panel: "default"});
    tabs.push({active: true, id: "analysis", title: "Analysis", panel: "default"});

    var tabHeadersTemplate = _.template($("#tab-headers-template").text());
    $("#tab-headers").append(tabHeadersTemplate({tabs: tabs}));

    var tabPanelsTemplate = _.template($("#tab-panels-template").text());
    $("#tab-panels").append(tabPanelsTemplate({tabs: tabs}));
}

function renderSynopsisPanel() {
    var journeySynopsisPanelTemplate = _.template($("#journey-synopsis-panel-template").text());
    $("#synopsis").find("#tab-body").append(journeySynopsisPanelTemplate(journey));
}

function renderFilterPanel() {
    var journeyFilterTemplate = _.template($("#journey-filter-template").text());
    $("#filter").find("#tab-body").append(journeyFilterTemplate(journey));
}


function renderAnalysisPanel() {
    var stateTree = new StateTree(directory).layoutHierarchically()//.squash();

    var paths = calculatePaths(stateTree.getRootState(), stateTree, directory);

    var coordinateSystem = new CoordinateSystem({width: 9, height: 3}, stateTree, paths);
    var plot = new Plot(coordinateSystem);
    plot.plotStates(stateTree.getRootState());
    plot.plotPaths(paths);

    var journeyAnalysisTemplate = _.template($("#journey-analysis-template").text());
    var data = {
        plots: plot.getPlots(),
        dimensions: {
            width: coordinateSystem.getWidth(),
            height: coordinateSystem.getHeight()
        }
    };
    $("#analysis").find("#tab-body").append(journeyAnalysisTemplate(data));
}