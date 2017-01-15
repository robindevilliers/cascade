
function calculatePaths(state, stateTree, directory) {
    var paths = [];
    if (state.state) {

        var followingStates = _.map(directory.findStatesThatFollow(state.state), function (name) {
            return findStateByName(stateTree.getRootState(), name);
        });
        var connections = _.map(followingStates, function (s) {
            return {sx: state.x, sy: state.y, tx: s.x, ty: s.y};
        });

        _.each(connections, function (connection) {
            var path = [];
            if (connection.sx === connection.tx) { //inline connection

                if (connection.ty - connection.sy === 1) { //target state is immediately below
                    path.push(new InlineVerticalLeg(connection.sx, connection.sy));
                } else {
                    path.push(new InlineTopRightLeg(connection.sx, connection.sy));

                    if (connection.ty > connection.sy) {
                        path.push(new AdjacentVerticalLeg(connection.sx, connection.sy + 1, connection.ty - 1, DirectionEnum.DOWN));
                    } else {
                        path.push(new AdjacentVerticalLeg(connection.sx, connection.sy, connection.ty, DirectionEnum.UP));
                    }
                    path.push(new InlineBottomRightLeg(connection.tx, connection.ty - 1));
                }

            } else if (connection.sx < connection.tx) { //connection goes to the right

                path.push(new InlineTopRightLeg(connection.sx, connection.sy));

                if (connection.ty > connection.sy + 1) { //going down by more than one level.
                    path.push(new AdjacentVerticalLeg(connection.sx, connection.sy + 1, connection.ty - 1, DirectionEnum.DOWN));
                } else if (connection.ty <= connection.sy) { //going up
                    path.push(new AdjacentVerticalLeg(connection.sx, connection.sy, connection.ty, DirectionEnum.UP));
                }

                if (connection.tx - connection.sx > 1) {
                    path.push(new InlineHorizontalLeg(connection.sx + 1, connection.tx - 1, connection.ty - 1, DirectionEnum.RIGHT));
                }

                path.push(new InlineBottomLeftLeg(connection.tx, connection.ty - 1));

            } else if (connection.sx > connection.tx) { //connection goes to the left

                path.push(new InlineTopLeftLeg(connection.sx, connection.sy));

                if (connection.ty > connection.sy + 1) { //going down by more than one level.
                    path.push(new AdjacentVerticalLeg(connection.sx - 1, connection.sy + 1, connection.ty - 1, DirectionEnum.DOWN));
                } else if (connection.ty <= connection.sy) { //going up
                    path.push(new AdjacentVerticalLeg(connection.sx - 1, connection.sy, connection.ty, DirectionEnum.UP));
                }

                if (connection.sx - connection.tx > 1) {
                    path.push(new InlineHorizontalLeg(connection.sx - 1, connection.tx + 1, connection.ty - 1, DirectionEnum.LEFT));
                }
                path.push(new InlineBottomRightLeg(connection.tx, connection.ty - 1));
            }
            paths.push(path);
        });
    }
    _.each(state.next, function (s) {
        paths = _.union(paths, calculatePaths(s, stateTree, directory));
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
}