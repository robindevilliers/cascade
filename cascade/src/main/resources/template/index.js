var params = queryParameters();

var breadcrumb = new BreadCrumb('index');

var config = {
    mode: 'descriptive',
    orderThreshold: 3
};

renderDashboard();
renderTabPanels();
renderJourneyLists();
renderCoverageReport();

function reRenderLists(e){
    if (e.target.name === 'orderThreshold' && e.target.value.length == 0){
        config[e.target.name] = 3;
    } else if (e.target.name === 'orderThreshold' && (parseInt(e.target.value) > 9 || parseInt(e.target.value) < 1) ){
        config[e.target.name] = 3;
    } else {
        if (e.target.name.startsWith('mode')){
            config['mode'] = e.target.value;
        }

    }
    renderJourneyLists();

}

function renderDashboard() {
    var successesCount = _.size(_.filter(directoryData.items, function (journey) {
        return journey.result === 'SUCCESS';
    }));
    var failureCount = _.size(_.filter(directoryData.items, function (journey) {
        return journey.result === 'FAILED';
    }));
    var errorCount = _.size(_.filter(directoryData.items, function (journey) {
        return journey.result === 'ERROR';
    }));
    var totalCount = _.size(directoryData.items);
    var percentage = totalCount > 0 ? Math.round(successesCount / totalCount * 100) : 0;

    var totalSeconds = Math.floor(directoryData.duration / 1000);
    var minutes = Math.floor(totalSeconds / 60);
    var remainingSeconds = totalSeconds - (minutes * 60);

    $('#index-percentage-label').html(percentage);
    $('#completeness-label').html(directoryData.completeness);

    $('#index-success-label').html(successesCount);
    $('#index-failure-label').html(failureCount);
    $('#index-error-label').html(errorCount);
    $('#index-minutes-label').html(minutes);
    $('#index-seconds-label').html(remainingSeconds);
}

function renderTabPanels() {
    var errorCount = _.size(_.filter(directoryData.items, function (journey) {
        return journey.result !== 'SUCCESS';
    }));

    var activeTab = params.tab;
    var tabs = [];
    var active = activeTab === undefined;
    if (errorCount > 0) {
        tabs.push({active: activeTab === 'failures' || active, id: "failures", title: "Failures"});
        active = false;
    }
    tabs.push({active: activeTab === 'all' || active, id: "all", title: "All"});
    tabs.push({active: activeTab === 'machine', id: "machine", title: "State Machine"});

    var tabHeadersTemplate = _.template($("#tab-headers-template").text());
    $("#tab-headers").append(tabHeadersTemplate({tabs: tabs}));

    var tabPanelsTemplate = _.template($("#tab-panels-template").text());
    $("#tab-panels").append(tabPanelsTemplate({tabs: tabs}));

    $("#tab-headers li a").click(function() {
        if (breadcrumb.lastName() == $(this).attr("data-tab-id")){
            return false;
        }
        breadcrumb.gotoNewLink("index.html?tab=" + $(this).attr("data-tab-id"));
    });
}

function renderJourneyLists() {
    var journeysTemplate = _.template($("#journeys-template").text());

    var badJourneys = _.filter(directoryData.items, function (journey) {
        return journey.result !== 'SUCCESS';
    });

    $("#all #tab-body").html(journeysTemplate({id: 1, journeys: directoryData.items, scenarioOrder: directoryData.scenarioOrder, config: config}));
    $("#failures #tab-body").html(journeysTemplate({id: 2, journeys: badJourneys, scenarioOrder: directoryData.scenarioOrder, config: config}));

    $(".journey-list tr").hover(function() {
        $(this).addClass("text-primary");
    }, function(){
        $(this).removeClass("text-primary");
    });

    $(".journey-list tr").click(function() {
        breadcrumb.gotoNewLink("journey.html?journeyId=" + $(this).attr("data-journey-id"));
    });
}

function renderCoverageReport() {
    var machineTemplate = _.template($("#machine-template").text());

    var stateCount = 0;
    var completeStateCount = 0;

    var scenarioCount = 0;
    var completeScenarioCount = 0;


    var stateMachine = _.chain(directoryData.states)
        .cloneDeep()
        .map(function(state){
            var atLeastOneRun = false;
            var atLeastOneMiss = false;

            var scenarios = _.chain(directoryData.scenarios)
            .filter(function(scenario) {
                return scenario.state === state.name;
            })
            .cloneDeep()
            .map(function(scenario){
                var count = _.chain(directoryData.items)
                .flatMap(function(journey) { return journey.scenarios;})
                .map(function(s){ return s.name})
                .uniq()
                .reduce(function(count, name){
                    return scenario.name === name ? count + 1 : count;
                }, 0)
                .value();

                if (count > 0){
                    scenario.coverage ='COVERED';
                    scenario.bg = 'success';
                    atLeastOneRun = true;
                } else {
                    scenario.coverage ='MISSED';
                    scenario.bg = 'danger';
                    atLeastOneMiss = true;
                }

                return scenario;
            })
            .each(function(scenario) {
                scenarioCount++;
                if (scenario.coverage === 'COVERED'){
                    completeScenarioCount++;
                }
                return true;
            })
            .value();

            state.scenarios = scenarios;

            if (atLeastOneRun){
                if (atLeastOneMiss){
                    state.bg = 'warning';
                    state.coverage = 'PARTIAL';
                } else {
                    state.bg = 'success';
                    state.coverage = 'COMPLETE';
                }
            } else {
                state.bg = 'danger';
                state.coverage = 'MISSED';
            }

            return state;
        })
        .each(function(state){
            stateCount++;
            if (state.coverage === 'COMPLETE' || state.coverage === 'PARTIAL'){
                completeStateCount++;
            }
            return true;
        })
        .value();

    var statePercentage = stateCount > 0 ? Math.round(completeStateCount / stateCount * 100) : 0;
    $('.state-coverage-pane').addClass(statePercentage < 100 ? statePercentage < 70 ? 'bg-danger' : 'bg-warning' : 'bg-success');
    $('#index-state-coverage-label').html(statePercentage);

    var scenarioPercentage = scenarioCount > 0 ? Math.round(completeScenarioCount / scenarioCount * 100) : 0;
    $('.scenario-coverage-pane').addClass(scenarioPercentage < 100 ? scenarioPercentage < 70 ? 'bg-danger' : 'bg-warning' : 'bg-success');
    $('#index-scenario-coverage-label').html(scenarioPercentage);


    $("#machine #tab-body").html(machineTemplate({stateMachine: stateMachine, stateOrder: directoryData.stateOrder, scenarioOrder: directoryData.scenarioOrder}));
}