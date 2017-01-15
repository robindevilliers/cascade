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



}