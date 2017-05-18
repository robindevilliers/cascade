var params = queryParameters();

var breadcrumb = new BreadCrumb('journey');

var directory = new Directory(directoryData);

var journey = _.find(directoryData.items, function (journey) {
        return journey.journeyId === params.journeyId;
    });

if (!journey){

    window.href = "index.html"
}
$('#journey-id').html('- ' + journey.journeyId);

function togglePathEmphasis(el){
    if (el.attr('data-highlight')){
        el.attr('stroke-width','1');
        el.attr('data-highlight', null)
    } else {
        el.attr('stroke-width','2');
        el.attr('data-highlight', true)
    }
}

function gotoState(el, index){
    breadcrumb.gotoNewLink("state.html?journeyId=" + journey.journeyId + "&index=" + index);
}


function toggleImageSize(el){
    if (el.attr('data-zoomed') == "false"){
        el.attr('data-zoomed', true);
        el.attr('data-original-width', el.css("width"))

        el.attr('data-current-mag', "30");
        if (job){
            clearInterval(job);
        }
        var job = setInterval(expand, 5);


    } else {
        el.attr('data-current-mag', "100");
        if (job){
            clearInterval(job);
        }
        var job = setInterval(contract, 5);

        el.attr('data-zoomed', false);
        el.css("width", el.attr('data-original-width'));
    }

    function expand(){
        var mag = parseInt(el.attr('data-current-mag'));
        if (mag == 100){
            clearInterval(job);
            el.attr('data-current-mag', null);
        } else {
            mag = mag + 1;
            el.css("width", mag + "%");
            el.attr('data-current-mag', mag);
        }

    }

    function contract(){
            var mag = parseInt(el.attr('data-current-mag'));
            if (mag == 30){
                clearInterval(job);
                el.attr('data-current-mag', null);
            } else {
                mag = mag - 1;
                el.css("width", mag + "%");
                el.attr('data-current-mag', mag);
            }

        }
}

renderTabs();
renderSynopsisPanel();
renderFilterPanel();
renderAnalysisPanel(journey);

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
    tabs.push({active: true, id: "synopsis", title: "Synopsis", panel: "default"});
    tabs.push({active: false, id: "filter", title: "Filter", panel: "default"});
    tabs.push({active: false, id: "analysis", title: "Analysis", panel: "default"});

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


function renderAnalysisPanel(journey) {
    var stateTree = new StateTree(directory).layoutHierarchically().squash();

    var expandedJourneyScenarios = directory.expandJourneyScenarios(journey.scenarios);

    var paths = calculatePaths(stateTree.getRootState(), stateTree, directory, expandedJourneyScenarios);

    var coordinateSystem = new CoordinateSystem({width: 9, height: 3}, stateTree, paths);
    var plot = new Plot(coordinateSystem, directory);
    plot.plotStates(stateTree.getRootState(), expandedJourneyScenarios);
    plot.plotPaths(paths);

    var journeyAnalysisTemplate = _.template($("#journey-analysis-template").text());
    var data = {
        plots: plot.getPlots(),
        dimensions: {
            width: coordinateSystem.getWidth(),
            height: coordinateSystem.getHeight()
        },
        journeyId: journey.journeyId
    };
    $("#analysis").find("#tab-body").append(journeyAnalysisTemplate(data));

//    _.each(journey.scenarios, function(scenario, idx){
//        var modalTemplate = _.template($("#journey-step-modal").text());
//        $("#analysis").find("#tab-body").append(modalTemplate({
//            index: idx,
//            name: scenario.name,
//            scope: scenario.scope,
//            hasState: scenario.hasState,
//            hasTransition: scenario.hasTransition
//        }));
//    })

}