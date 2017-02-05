renderDashboard();
renderTabPanels();
renderJourneyLists();
renderCoverageReport();

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
    var percentage = Math.round(successesCount / totalCount * 100);

    var totalSeconds = Math.floor(directoryData.duration / 1000);
    var minutes = Math.floor(totalSeconds / 60);
    var remainingSeconds = totalSeconds - (minutes * 60);

    $('#index-percentage-label').html(percentage);
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

    var tabs = [];
    var active = true;
    if (errorCount > 0) {
        tabs.push({active: true, id: "failed-journeys", title: "Failures"});
        active = false;
    }
    tabs.push({active: active, id: "all-journeys", title: "All"});
    tabs.push({active: false, id: "coverage", title: "Coverage"});


    var tabHeadersTemplate = _.template($("#tab-headers-template").text());
    $("#tab-headers").append(tabHeadersTemplate({tabs: tabs}));

    var tabPanelsTemplate = _.template($("#tab-panels-template").text());
    $("#tab-panels").append(tabPanelsTemplate({tabs: tabs}));
}

function renderJourneyLists() {
    var journeysTemplate = _.template($("#journeys-template").text());

    var badJourneys = _.filter(directoryData.items, function (journey) {
        return journey.result !== 'SUCCESS';
    });

    $("#all-journeys #tab-body").html(journeysTemplate({journeys: directoryData.items}));
    $("#failed-journeys #tab-body").html(journeysTemplate({journeys: badJourneys}));
}

function renderCoverageReport() {
    var coverageTemplate = _.template($("#coverage-template").text());


    _.each(directoryData.items, function (journey) {
        journey.states = _.uniq(_.flatMap(journey.scenarios, function (journeyScenario) {
            return _.map(_.filter(directoryData.scenarios, function (scenario) {
                return scenario.name === journeyScenario;
            }), function (scenario) {
                return scenario.state;
            });
        }));
    });


    var stateHistogram = {};
    _.each(directoryData.states, function (state) {
        var count = 0;
        _.each(directoryData.items, function (journey) {
            if (_.some(journey.states, function (s) {
                    return s === state.name;
                })) {
                count++;
            }
        });
        stateHistogram[state.name] = count;
    });


    var scenarioHistogram = {};
    _.each(directoryData.scenarios, function (scenario) {
        var count = 0;
        _.each(directoryData.items, function (journey) {
            if (_.some(journey.directoryData, function (s) {
                    return s === scenario.name;
                })) {
                count++;
            }
        });
        scenarioHistogram[scenario.name] = count;
    });

    $("#coverage #tab-body").html(coverageTemplate({"stateData": stateHistogram, "scenarioData": scenarioHistogram}));
}