renderDashboard();
renderTabPanels();
renderJourneyLists();
renderCoverageReport();

function renderDashboard() {
    var successesCount = _.size(_.filter(directory.items, function (journey) {
        return journey.result === 'SUCCESS';
    }));
    var failureCount = _.size(_.filter(directory.items, function (journey) {
        return journey.result === 'FAILED';
    }));
    var errorCount = _.size(_.filter(directory.items, function (journey) {
        return journey.result === 'ERROR';
    }));
    var totalCount = _.size(directory.items);
    var percentage = Math.round(successesCount / totalCount * 100);

    var totalSeconds = Math.floor(directory.duration / 1000);
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
    var errorCount = _.size(_.filter(directory.items, function (journey) {
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

    var badJourneys = _.filter(directory.items, function (journey) {
        return journey.result !== 'SUCCESS';
    });

    $("#all-journeys #tab-body").html(journeysTemplate({journeys: directory.items}));
    $("#failed-journeys #tab-body").html(journeysTemplate({journeys: badJourneys}));
}

function renderCoverageReport() {
    var coverageTemplate = _.template($("#coverage-template").text());
    console.log('coverage report');

    _.each(directory.items, function (journey) {
        journey.states = _.uniq(_.flatMap(journey.scenarios, function (journeyScenario) {
            return _.map(_.filter(directory.scenarios, function (scenario) {
                return scenario.name === journeyScenario;
            }), function (scenario) {
                return scenario.state;
            });
        }));
    });


    var stateHistogram = {};
    _.each(directory.states, function (state) {
        var count = 0;
        _.each(directory.items, function (journey) {
            if (_.some(journey.states, function (s) {
                    return s === state.name;
                })) {
                count++;
            }
        });
        stateHistogram[state.name] = count;
    });


    var scenarioHistogram = {};
    _.each(directory.scenarios, function (scenario) {
        var count = 0;
        _.each(directory.items, function (journey) {
            if (_.some(journey.scenarios, function (s) {
                    return s === scenario.name;
                })) {
                count++;
            }
        });
        scenarioHistogram[scenario.name] = count;
    });

    $("#coverage #tab-body").html(coverageTemplate({"stateData": stateHistogram, "scenarioData": scenarioHistogram}));
}