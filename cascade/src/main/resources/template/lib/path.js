
function calculatePaths(state, stateTree, directory, journeyScenarios) {
    var paths = [];
    if (state.state) {

        var followingStates = _.map(directory.findStatesThatFollow(state.state), function (name) {
            return findStateByName(stateTree.getRootState(), name);
        });
        var connections = _.map(followingStates, function (s) {
            var realised = false;
            for (var i = 0; i < journeyScenarios.length - 1; i++){
                if (journeyScenarios[i].state === state.state && journeyScenarios[i + 1].state === s.state){
                    realised = true;
                    break;
                }
            }

            return {sx: state.x, sy: state.y, tx: s.x, ty: s.y, realised: realised};
        });



        _.each(connections, function (connection) {
            var path = new Path(connection.realised);
            if (connection.sx === connection.tx) { //inline connection

                if (connection.ty - connection.sy === 1) { //target state is immediately below
                    path.addLeg(new InlineVerticalLeg(connection.sx, connection.sy));
                } else {
                    path.addLeg(new InlineTopRightLeg(connection.sx, connection.sy));

                    if (connection.ty > connection.sy) {
                        path.addLeg(new AdjacentVerticalLeg(connection.sx, connection.sy + 1, connection.ty - 1, DirectionEnum.DOWN));
                    } else {
                        path.addLeg(new AdjacentVerticalLeg(connection.sx, connection.sy, connection.ty, DirectionEnum.UP));
                    }
                    path.addLeg(new InlineBottomRightLeg(connection.tx, connection.ty - 1));
                }

            } else if (connection.sx < connection.tx) { //connection goes to the right

                path.addLeg(new InlineTopRightLeg(connection.sx, connection.sy));

                if (connection.ty > connection.sy + 1) { //going down by more than one level.
                    path.addLeg(new AdjacentVerticalLeg(connection.sx, connection.sy + 1, connection.ty - 1, DirectionEnum.DOWN));
                } else if (connection.ty <= connection.sy) { //going up
                    path.addLeg(new AdjacentVerticalLeg(connection.sx, connection.sy, connection.ty, DirectionEnum.UP));
                }

                if (connection.tx - connection.sx > 1) {
                    path.addLeg(new InlineHorizontalLeg(connection.sx + 1, connection.tx - 1, connection.ty - 1, DirectionEnum.RIGHT));
                }

                path.addLeg(new InlineBottomLeftLeg(connection.tx, connection.ty - 1));

            } else if (connection.sx > connection.tx) { //connection goes to the left

                path.addLeg(new InlineTopLeftLeg(connection.sx, connection.sy));

                if (connection.ty > connection.sy + 1) { //going down by more than one level.
                    path.addLeg(new AdjacentVerticalLeg(connection.sx - 1, connection.sy + 1, connection.ty - 1, DirectionEnum.DOWN));
                } else if (connection.ty <= connection.sy) { //going up
                    path.addLeg(new AdjacentVerticalLeg(connection.sx - 1, connection.sy, connection.ty, DirectionEnum.UP));
                }

                if (connection.sx - connection.tx > 1) {
                    path.addLeg(new InlineHorizontalLeg(connection.sx - 1, connection.tx + 1, connection.ty - 1, DirectionEnum.LEFT));
                }
                path.addLeg(new InlineBottomRightLeg(connection.tx, connection.ty - 1));
            }
            paths.push(path);
        });
    }
    _.each(state.next, function (s) {
        paths = _.union(paths, calculatePaths(s, stateTree, directory, journeyScenarios));
    });

    return paths;

    function findStateByName(state, name) {
        if (state.state === name) {
            return state;
        } else {

            for (var i = 0; i < state.next.length; i++) {
                var result = findStateByName(state.next[i], name);
                if (result) {
                    return result;
                }
            }
            return undefined;
        }
    }

    function Path(realised) {
        var legs = [];

        this.addLeg = function(leg){
            legs.push(leg);
        };

        this.get = function(i){
            return legs[i];
        };

        this.size = function(){
            return legs.length;
        };

        this.getLegs = function(){
            return legs;
        };

        this.isRealised = function(){
            return realised;
        };
    }
}