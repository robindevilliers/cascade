function Directory(directory) {

    this.findStartingStates = function () {
        return _.chain(directory.states)
            .filter(function (state) {
                return _.some(state.precedents, function (p) {
                    return p === "uk.co.malbec.cascade.annotations.Step.Null"
                });
            })
            .map(function (state) {
                return state.name;
            })
            .value();
    };

    this.findStatesThatFollow = function(name){
        return _.chain(directory.states)
            .filter(function (state) {
                return _.some(state.precedents, function (p) {
                    return p === name;
                });
            })
            .map(function (state) {
                return state.name;
            })
            .value();
    };

    this.expandJourneyScenarios = function(scenarios){
        return _.map(scenarios, function(scenario){
           return _.find(directory.scenarios, function(s){
               return s.name === scenario;
           });
        });
    }

    this.isStartingState = function(name){
        var directoryState = _.find(directory.states, function(state){
            return state.name === name;
        });

        return _.some(directoryState.precedents, function(precedent){
            return precedent === "uk.co.malbec.cascade.annotations.Step.Null";
        });
    }


}