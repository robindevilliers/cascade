var DirectionEnum = {
    RIGHT: new Direction('right'),
    LEFT: new Direction('left'),
    UP: new Direction('up'),
    DOWN: new Direction('down')
};


var LegTypeEnum = {
    INLINE_VERTICAL: new LegType('inlineVertical'),
    INLINE_TOP_RIGHT: new LegType('inlineTopRight'),
    INLINE_TOP_LEFT: new LegType('inlineTopLeft'),
    INLINE_HORIZONTAL: new LegType('inlineHorizontal'),
    INLINE_BOTTOM_RIGHT: new LegType('inlineBottomRight'),
    INLINE_BOTTOM_LEFT: new LegType('inlineBottomLeft'),
    ADJACENT_VERTICAL: new LegType('adjacentVertical')
};


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
    return _.find(directory.items, function (journey) {
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
    $("#synopsis #tab-body").append(journeySynopsisPanelTemplate(journey));
}

function renderFilterPanel() {
    var journeyFilterTemplate = _.template($("#journey-filter-template").text());
    $("#filter #tab-body").append(journeyFilterTemplate(journey));
}


function renderAnalysisPanel() {
    const StateWidget = {
        gridWidth: 9,
        gridHeight: 3
    };

    var stateTree = generateStateTree();
    layoutHierarchically(stateTree);
    squash(stateTree, stateTree.height);

    var paths = calculatePaths(stateTree, stateTree);


    //TODO make a coordinateSystem object that knows about dimensions, widths, etc
    var dimensions = findDimensions(stateTree);

    var verticalChannelWidths = indexWidths(paths, dimensions);
    var horizontalChannelHeights = indexHeights(paths, dimensions);

    //TODO - create plot object. Let it take a coordinate system object in its constructor.
    var plots = [];
    plotStates(stateTree, plots, verticalChannelWidths, horizontalChannelHeights);
    plotPaths(paths, plots, verticalChannelWidths, horizontalChannelHeights);

    var journeyAnalysisTemplate = _.template($("#journey-analysis-template").text());
    var data = {
        plots: plots,
        dimensions: {
            width: _.sum(verticalChannelWidths) + dimensions.x * StateWidget.gridWidth,
            height: _.sum(horizontalChannelHeights) + dimensions.y * StateWidget.gridHeight
        }
    };
    $("#analysis #tab-body").append(journeyAnalysisTemplate(data));


    function plotPaths(paths, plots, verticalChannelWidths, horizontalChannelHeights) {

        _.each(paths, function (path) {

            //TODO - add coordinate to parameter list.
            //TODO - possibly rename Coordinate to CoordinateSystem
            var coordinate = new Coordinate({width: StateWidget.gridWidth, height: StateWidget.gridHeight}, verticalChannelWidths, horizontalChannelHeights);
            var plot = new PathPlot();
            var previousLeg = null;

            _.each(path, function (leg) {

                //TODO - if offset is over 4 then a different strategy must take place.

                switch (leg.aspect) {
                    case LegTypeEnum.INLINE_VERTICAL:

                        coordinate
                            .resetAtCardinalPoint(leg.x, leg.y)
                            .moveRight(Math.round(StateWidget.gridWidth / 2))
                            .moveDown(StateWidget.gridHeight);

                        plot.getSpec()
                            .moveTo(coordinate)
                            .drawVerticalTo(coordinate.resetCardinalY(leg.y + 1).moveUpByOne());
                        break;
                    case LegTypeEnum.INLINE_TOP_RIGHT:

                        coordinate
                            .resetAtCardinalPoint(leg.x, leg.y)
                            .moveRight(Math.round(StateWidget.gridWidth / 2))
                            .moveDown(StateWidget.gridHeight);

                        var offset = calculateHeaderOffset(_.map(paths, function (p) {
                            return p[0];
                        }), leg, true);

                        plot.getSpec()
                            .moveTo(coordinate.moveRightByOne().moveRight(offset))
                            .drawVerticalTo(coordinate.moveDown(leg.gridYIndex))
                            .drawArc(coordinate.moveRightByOne().moveDownByOne(), 0);
                        break;
                    case LegTypeEnum.INLINE_TOP_LEFT:

                        coordinate
                            .resetAtCardinalPoint(leg.x, leg.y)
                            .moveRight(Math.round(StateWidget.gridWidth / 2))
                            .moveDown(StateWidget.gridHeight);

                        var offset = calculateHeaderOffset(_.map(paths, function (p) {
                            return p[0];
                        }), leg, true);

                        plot.getSpec()
                            .moveTo(coordinate.moveLeftByOne().moveLeft(offset))
                            .drawVerticalTo(coordinate.moveDown(leg.gridYIndex))
                            .drawArc(coordinate.moveLeftByOne().moveDownByOne(), 1);
                        break;
                    case LegTypeEnum.ADJACENT_VERTICAL:

                        switch (previousLeg.direction) {
                            case DirectionEnum.RIGHT:

                                plot.getSpec().drawHorizontalTo(coordinate
                                    .resetCardinalX(leg.x)
                                    .moveRight(StateWidget.gridWidth)
                                    .moveRightByOne() //for the left border in the vertical channel
                                    .moveRight(leg.gridXIndex));

                                switch (leg.direction) {
                                    case DirectionEnum.UP:
                                        plot.getSpec().drawArc(coordinate.moveRightByOne().moveUpByOne(), 0);
                                        break;
                                    case DirectionEnum.DOWN:
                                        plot.getSpec().drawArc(coordinate.moveRightByOne().moveDownByOne(), 1);
                                        break;
                                }

                                break;
                            case DirectionEnum.LEFT:

                                plot.getSpec().drawHorizontalTo(coordinate
                                    .resetCardinalX(leg.x)
                                    .moveRight(StateWidget.gridWidth)
                                    .moveRightByOne()
                                    .moveRight(leg.gridXIndex)
                                    .moveRightByOne()
                                    .moveRightByOne());

                                switch (leg.direction) {
                                    case DirectionEnum.UP:
                                        plot.getSpec().drawArc(coordinate.moveLeftByOne().moveUpByOne(), 1);
                                        break;
                                    case DirectionEnum.DOWN:
                                        plot.getSpec().drawArc(coordinate.moveLeftByOne().moveDownByOne(), 0);
                                        break;
                                }

                                break;
                        }
                        break;
                    default:
                        //this logic ignores inline horizontals.  We don't need to draw them.

                        if (previousLeg.aspect === LegTypeEnum.ADJACENT_VERTICAL) {

                            switch (previousLeg.direction) {
                                case DirectionEnum.UP:

                                    plot.getSpec().drawVerticalTo(coordinate
                                        .resetCardinalY(leg.y)
                                        .moveDown(StateWidget.gridHeight)
                                        .moveDown(leg.gridYIndex)
                                        .moveDownByOne()
                                        .moveDownByOne());

                                    switch (leg.direction) {
                                        case DirectionEnum.LEFT:
                                            plot.getSpec().drawArc(coordinate.moveLeftByOne().moveUpByOne(), 0);
                                            break;
                                        case DirectionEnum.RIGHT:
                                            plot.getSpec().drawArc(coordinate.moveRightByOne().moveUpByOne(), 1);
                                            break;
                                    }

                                    break;
                                case DirectionEnum.DOWN:

                                    plot.getSpec().drawVerticalTo(coordinate
                                        .resetCardinalY(leg.y)
                                        .moveDown(StateWidget.gridHeight)
                                        .moveDown(leg.gridYIndex));

                                    switch (leg.direction) {
                                        case DirectionEnum.LEFT:
                                            plot.getSpec().drawArc(coordinate.moveLeftByOne().moveDownByOne(), 1);
                                            break;
                                        case DirectionEnum.RIGHT:
                                            plot.getSpec().drawArc(coordinate.moveRightByOne().moveDownByOne(), 1);
                                            break;
                                    }

                                    break;
                            }
                        }

                        if (leg.aspect === LegTypeEnum.INLINE_BOTTOM_RIGHT) {

                            coordinate
                                .resetCardinalX(leg.x)
                                .moveRight(Math.round(StateWidget.gridWidth / 2));

                            //TODO - figure out what to do with this.
                            var offset = calculateHeaderOffset(_.map(paths, function (p) {
                                return p[p.length - 1];
                            }), leg, false);

                            plot.getSpec()
                                .drawHorizontalTo(coordinate.moveRightByOne().moveRight(offset).moveRightByOne())
                                .drawArc(coordinate.moveLeftByOne().moveDownByOne(), 0)
                                .drawVerticalTo(coordinate.resetCardinalY(leg.y + 1).moveUpByOne());

                        } else if (leg.aspect === LegTypeEnum.INLINE_BOTTOM_LEFT) {

                            coordinate
                                .resetCardinalX(leg.x)
                                .moveRight(Math.round(StateWidget.gridWidth / 2));

                            var offset = calculateHeaderOffset(_.map(paths, function (p) {
                                return p[p.length - 1];
                            }), leg, false);

                            plot.getSpec()
                                .drawHorizontalTo(coordinate.moveLeftByOne().moveLeft(offset).moveLeftByOne())
                                .drawArc(coordinate.moveRightByOne().moveDownByOne(), 1)
                                .drawVerticalTo(coordinate.resetCardinalY(leg.y + 1).moveUpByOne());
                        }

                        break;
                }


                previousLeg = leg;
            });
            plots.push(plot);

            function calculateHeaderOffset(partials, leg, top) {
                var header = _.filter(partials, function (p) {
                    return p.aspect == leg.aspect && p.x == leg.x && p.y == leg.y;
                });
                header = _.orderBy(header, function (p) {
                    return top ? -p.gridYIndex : p.gridYIndex;
                });
                return _.findIndex(header, function (p) {
                    return p.gridYIndex === leg.gridYIndex;
                });
            }
        });
    }


    function indexWidths(paths, dimensions) {
        var widths = [];

        var groups = [];
        _.times(dimensions.x - 1, function () {
            groups.push([]);
        });

        _.each(paths, function (path) {
            _.each(path, function (leg, i) {
                if (leg.aspect === LegTypeEnum.ADJACENT_VERTICAL) {

                    var previousLeg = path[i - 1];
                    var postLeg = path[i + 1];
                    //TODO - methodize these tests. - rename aspect to something else.
                    if (previousLeg.aspect === LegTypeEnum.INLINE_TOP_RIGHT && postLeg.aspect === LegTypeEnum.INLINE_BOTTOM_RIGHT) {
                        leg.score = 4;
                    } else if (previousLeg.aspect === LegTypeEnum.INLINE_TOP_RIGHT && postLeg.aspect === LegTypeEnum.INLINE_BOTTOM_LEFT) {
                        leg.score = 3;
                    } else if (previousLeg.aspect === LegTypeEnum.INLINE_TOP_LEFT && postLeg.aspect === LegTypeEnum.INLINE_BOTTOM_RIGHT) {
                        leg.score = 2;
                    } else {
                        leg.score = 1;
                    }
                    groups[leg.x].push(leg);
                }
            });
        });

        _.each(groups, function (group) {

            group = _.orderBy(group, function (piece) {
                return -piece.score;
            });

            var channel = [];
            _.each(group, function (piece) {
                var placed = false;
                _.each(channel, function (vertical, i) {
                    var obstructed = false;
                    if (piece.ty > piece.sy) {

                        _.times(piece.ty - piece.sy + 1, function (i) {
                            if (vertical[piece.ty - i]) {
                                obstructed = true;
                            }
                        });
                        if (!obstructed) {
                            _.times(piece.ty - piece.sy + 1, function (i) {
                                vertical[piece.ty - i] = 'x';
                            });
                            placed = true;
                            piece.gridXIndex = i;
                            return false;
                        }
                    } else {
                        _.times(piece.sy - piece.ty + 1, function (i) {
                            if (vertical[piece.sy - i]) {
                                obstructed = true;
                            }
                        });
                        if (!obstructed) {
                            _.times(piece.sy - piece.ty + 1, function (i) {
                                vertical[piece.sy - i] = 'x';
                            });
                            placed = true;
                            piece.gridXIndex = i;
                            return false;
                        }
                    }
                });
                if (!placed) {
                    var vertical = new Array(dimensions.y);
                    if (piece.ty > piece.sy) {
                        _.times(piece.ty - piece.sy + 1, function (i) {
                            vertical[piece.ty - i] = 'x';
                        });
                    } else {
                        _.times(piece.sy - piece.ty + 1, function (i) {
                            vertical[piece.sy - i] = 'x';
                        });
                    }
                    piece.gridXIndex = channel.length;

                    channel.push(vertical);
                }
            });
            widths.push(channel.length + 2);
        });
        return widths;
    }

    function indexHeights(paths, dimensions) {
        var heights = [];

        var groups = [];
        _.times(dimensions.y, function () {
            groups.push([]);
        });

        _.each(paths, function (path) {
            var partial = [], y = 0;
            _.each(path, function (leg) {
                if (leg.aspect === LegTypeEnum.INLINE_VERTICAL) {

                } else if (leg.aspect === LegTypeEnum.ADJACENT_VERTICAL) {
                    if (partial.length > 0) {

                        groups[y].push(partial);
                        partial = [];
                    }
                } else {
                    partial.push(leg);
                    y = leg.y;
                }
            });
            if (partial.length > 0) {

                groups[y].push(partial);
            }
        });

        _.each(groups, function (group) {
            var channel = [];

            //pull out all partials that are in the group that are 'top', but don't have horizontals.
            var topPartials = _.filter(group, function (partial) {
                return !_.some(partial, function (leg) {
                        return leg.aspect === LegTypeEnum.INLINE_HORIZONTAL;
                    }) && !_.some(partial, function (leg) {
                        return leg.aspect === LegTypeEnum.INLINE_BOTTOM_RIGHT || leg.aspect === LegTypeEnum.INLINE_BOTTOM_LEFT;
                    })
            });


            placePartialsInChannel(channel, topPartials);

            //pull out all partials that are in the group don't have bottoms and do have horizontals.
            var topHorizontalPartials = _.filter(group, function (partial) {
                return _.some(partial, function (leg) {
                        return leg.aspect === LegTypeEnum.INLINE_HORIZONTAL;
                    }) && !_.some(partial, function (leg) {
                        return leg.aspect === LegTypeEnum.INLINE_BOTTOM_RIGHT || leg.aspect === LegTypeEnum.INLINE_BOTTOM_LEFT;
                    })
            });


            placePartialsInChannel(channel, topHorizontalPartials);

            //pull out all pieces that have both bottoms and tops.
            var middlePartials = _.filter(group, function (partial) {
                return _.some(partial, function (leg) {
                        return leg.aspect === LegTypeEnum.INLINE_TOP_RIGHT || leg.aspect === LegTypeEnum.INLINE_TOP_LEFT;
                    }) &&
                    _.some(partial, function (leg) {
                        return leg.aspect === LegTypeEnum.INLINE_BOTTOM_RIGHT || leg.aspect === LegTypeEnum.INLINE_BOTTOM_LEFT;
                    })
            });

            placePartialsInChannel(channel, middlePartials);
            obstructShadowedPlaces(channel);

            //pull out all that don't have tops that have horizontals
            var bottomHorizontalPartials = _.filter(group, function (partial) {
                return _.some(partial, function (leg) {
                        return leg.aspect === LegTypeEnum.INLINE_HORIZONTAL;
                    }) && !_.some(partial, function (leg) {
                        return leg.aspect === LegTypeEnum.INLINE_TOP_RIGHT || leg.aspect === LegTypeEnum.INLINE_TOP_LEFT;
                    })
            });

            placePartialsInChannel(channel, bottomHorizontalPartials);
            obstructShadowedPlaces(channel);

            //pull out the remaining bottoms only.
            var bottomPartials = _.filter(group, function (partial) {
                return !_.some(partial, function (leg) {
                        return leg.aspect === LegTypeEnum.INLINE_HORIZONTAL;
                    }) && !_.some(partial, function (leg) {
                        return leg.aspect === LegTypeEnum.INLINE_TOP_RIGHT || leg.aspect === LegTypeEnum.INLINE_TOP_LEFT;
                    })
            });

            placePartialsInChannel(channel, bottomPartials);

            heights.push(channel.length + 2);
        });

        return heights;

        function obstructShadowedPlaces(channel) {
            for (var x = 0; x < dimensions.x * 2; x++) {
                var shadowed = false;
                for (var y = channel.length - 1; y >= 0; y--) {
                    if (shadowed && !channel[y][x]) {
                        channel[y][x] = 'o';
                    } else if (channel[y][x]) {
                        shadowed = true;
                    }
                }
            }
        }

        function placePartialsInChannel(channel, partials) {
            _.each(partials, function (partial) {

                //work out x dimensions of partial
                var minX = 100;
                var maxX = 0;
                _.each(partial, function (leg) {
                    if (leg.aspect === LegTypeEnum.INLINE_TOP_RIGHT || leg.aspect === LegTypeEnum.INLINE_BOTTOM_RIGHT) {

                        minX = Math.min(minX, leg.x * 2 + 1);
                        maxX = Math.max(maxX, leg.x * 2 + 1) + 1;
                        return;
                    }
                    if (leg.aspect === LegTypeEnum.INLINE_TOP_LEFT || leg.aspect === LegTypeEnum.INLINE_BOTTOM_LEFT) {
                        minX = Math.min(minX, leg.x * 2);
                        maxX = Math.max(maxX, leg.x * 2);
                        return;
                    }

                    if (leg.aspect === LegTypeEnum.INLINE_HORIZONTAL) {
                        minX = Math.min(minX, leg.sx * 2);
                        maxX = Math.max(maxX, leg.tx * 2 + 1);
                    }
                });

                //try to place partial in an existing horizontal in the channel
                var placed = false;
                _.each(channel, function (horizontal, i) {
                    var obstructed = false;

                    _.times(maxX - minX + 1, function (i) {
                        if (horizontal[minX + i]) {
                            obstructed = true;
                        }
                    });
                    if (!obstructed) {
                        _.times(maxX - minX + 1, function (i) {
                            horizontal[minX + i] = 'x';
                        });
                        placed = true;
                        _.each(partial, function (p) {
                            p.gridYIndex = i;
                        });

                        return false;
                    }
                });

                //add a new horizontal to the channel and add the partial
                if (!placed) {
                    var horizontal = new Array(dimensions.x * 2);
                    _.times(maxX - minX + 1, function (i) {
                        horizontal[minX + i] = 'x';
                    });

                    _.each(partial, function (p) {
                        p.gridYIndex = channel.length;
                    });

                    channel.push(horizontal);
                }

            });
        }

    }


    function calculatePaths(state, root) {
        var paths = [];
        if (state.state) {

            var followingStates = _.map(findState(isFollowing(state.state)), function (name) {
                return findStateByName(root, name);
            });
            var connections = _.map(followingStates, function (s) {
                return {sx: state.x, sy: state.y, tx: s.x, ty: s.y};
            });

            _.each(connections, function (connection) {
                var path = [];
                if (connection.sx === connection.tx) { //inline connection

                    //TODO - make Leg object

                    if (connection.ty - connection.sy === 1) { //target state is immediately below
                        path.push({aspect: LegTypeEnum.INLINE_VERTICAL, x: connection.sx, y: connection.sy, direction: DirectionEnum.DOWN});
                    } else {
                        path.push({aspect: LegTypeEnum.INLINE_TOP_RIGHT, x: connection.sx, y: connection.sy, direction: DirectionEnum.RIGHT});

                        if (connection.ty > connection.sy) {
                            path.push({
                                aspect: LegTypeEnum.ADJACENT_VERTICAL, x: connection.sx, sy: connection.sy + 1, ty: connection.ty - 1, direction: DirectionEnum.DOWN
                            });
                        } else {
                            path.push({
                                aspect: LegTypeEnum.ADJACENT_VERTICAL, x: connection.sx, sy: connection.sy, ty: connection.ty, direction: DirectionEnum.UP
                            });
                        }
                        path.push({aspect: LegTypeEnum.INLINE_BOTTOM_RIGHT, x: connection.tx, y: connection.ty - 1, direction: DirectionEnum.LEFT});
                    }

                } else if (connection.sx < connection.tx) { //connection goes to the right

                    path.push({aspect: LegTypeEnum.INLINE_TOP_RIGHT, x: connection.sx, y: connection.sy, direction: DirectionEnum.RIGHT});

                    if (connection.ty > connection.sy + 1) { //going down by more than one level.
                        path.push({
                            aspect: LegTypeEnum.ADJACENT_VERTICAL, x: connection.sx, sy: connection.sy + 1, ty: connection.ty - 1, direction: DirectionEnum.DOWN
                        });
                    } else if (connection.ty <= connection.sy) { //going up
                        path.push({
                            aspect: LegTypeEnum.ADJACENT_VERTICAL, x: connection.sx, sy: connection.sy, ty: connection.ty, direction: DirectionEnum.UP
                        });
                    }

                    if (connection.tx - connection.sx > 1) {
                        path.push({
                            aspect: LegTypeEnum.INLINE_HORIZONTAL, sx: connection.sx + 1, tx: connection.tx - 1, y: connection.ty - 1, direction: DirectionEnum.RIGHT
                        });
                    }

                    path.push({aspect: LegTypeEnum.INLINE_BOTTOM_LEFT, x: connection.tx, y: connection.ty - 1, direction: DirectionEnum.RIGHT});

                } else if (connection.sx > connection.tx) { //connection goes to the left

                    path.push({aspect: LegTypeEnum.INLINE_TOP_LEFT, x: connection.sx, y: connection.sy, direction: DirectionEnum.LEFT});

                    if (connection.ty > connection.sy + 1) { //going down by more than one level.
                        path.push({
                            aspect: LegTypeEnum.ADJACENT_VERTICAL, x: connection.sx - 1, sy: connection.sy + 1, ty: connection.ty - 1, direction: DirectionEnum.DOWN
                        });
                    } else if (connection.ty <= connection.sy) { //going up
                        path.push({
                            aspect: LegTypeEnum.ADJACENT_VERTICAL, x: connection.sx - 1, sy: connection.sy, ty: connection.ty, direction: DirectionEnum.UP
                        });
                    }

                    if (connection.sx - connection.tx > 1) {
                        path.push({
                            aspect: LegTypeEnum.INLINE_HORIZONTAL, sx: connection.sx - 1, tx: connection.tx + 1, y: connection.ty - 1, direction: DirectionEnum.LEFT
                        });
                    }
                    path.push({aspect: LegTypeEnum.INLINE_BOTTOM_RIGHT, x: connection.tx, y: connection.ty - 1, direction: DirectionEnum.LEFT});
                }
                paths.push(path);
            });


        }
        _.each(state.next, function (s) {
            paths = _.union(paths, calculatePaths(s, root));
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

    //TODO - dimensions object should have width and height, not x and y.
    function findDimensions(state) {
        var dimensions = {x: state.x + 1, y: state.y + 1};

        _.each(state.next, function (s) {
            var d = findDimensions(s);
            if (dimensions.x < d.x) {
                dimensions.x = d.x;
            }
            if (dimensions.y < d.y) {
                dimensions.y = d.y;
            }
        });
        return dimensions;
    }

    function plotStates(state, plots, verticalChannelWidths, horizontalChannelHeights) {
        if (state.state) {
            var gridX = (state.x * StateWidget.gridWidth) + sum(verticalChannelWidths, 0, state.x);
            var gridY = (state.y * StateWidget.gridHeight) + sum(horizontalChannelHeights, 0, state.y);
            plots.push({
                type: 'state',
                name: simpleName(state.state),
                gridX: gridX,
                gridY: gridY
            });
        }
        _.each(state.next, function (s) {
            plotStates(s, plots, verticalChannelWidths, horizontalChannelHeights);
        });

        function simpleName(string) {
            var i = string.lastIndexOf(".");
            if (i >= 0) {
                return string.substring(i + 1);
            } else {
                return string;
            }
        }
    }

    function sum(array, start, end) {
        var s = 0;
        for (var i = start; i < end; i++) {
            s = s + array[i];
        }
        return s;
    }

    function generateStateTree() {
        var seen = [];
        var lanes = _.map(findState(isStartingState), function (s) {
            return {state: s, next: [], connections: []}
        });
        var nextSet = _.union([], lanes);

        while (!_.isEmpty(nextSet)) {

            var workingSet = nextSet;
            nextSet = [];

            _.each(workingSet, function (currentState) {

                var followingStates = findState(isFollowing(currentState.state));

                var notAlreadySeen = _.reject(followingStates, isInArray(seen));

                _.each(notAlreadySeen, function (newState) {
                    seen.push(newState);

                    var node = {state: newState, next: [], connections: []};
                    currentState.next.push(node);
                    nextSet.push(node);
                });
            });
        }
        return {next: lanes};
    }


    function layoutHierarchically(stateTree) {
        assignStateWidth(stateTree);
        assignStateHeight(stateTree);
        assignStateCoordinates(stateTree, 0, 0);

        function assignStateCoordinates(state, x, y) {
            state.x = x;
            state.y = y;
            var l = 0;
            _.each(state.next, function (s) {
                assignStateCoordinates(s, x + l, y + 1);
                l = l + s.width;
            });
        }

        function assignStateWidth(state) {
            if (state.next.length == 0) {
                state.width = 1;
                return 1;
            } else {
                var sum = _.sum(_.map(state.next, assignStateWidth));
                state.width = sum;
                return sum;
            }
        }

        function assignStateHeight(state) {
            if (state.next.length == 0) {
                state.height = 1;
                return 1;
            } else {
                state.height = _.max(_.map(state.next, assignStateHeight)) + 1;
                return state.height;
            }
        }
    }

    function squash(state, height) {
        //iterate through all squares from largest to smallest
        var largestSquare = findLargestSquare(state, 0);
        while (largestSquare != undefined) {

            var gridWidth = largestSquare.x + largestSquare.width - 1;

            if (gridWidth >= largestSquare.width) { //skip repositioning algorithm - square is too wide.
                var grid = generateGrid(gridWidth, height);
                markWidgetsInGrid(grid, state, gridWidth, largestSquare);

                var coordinates = findAvailableCoordinates(grid, height, gridWidth, largestSquare, largestSquare.y - 1, 0);

                if (coordinates) {
                    move(largestSquare, coordinates.x - largestSquare.x, coordinates.y - largestSquare.y);
                }
            }

            largestSquare.processed = true;
            largestSquare = findLargestSquare(state, 0);
        }

        function findAvailableCoordinates(grid, gridHeight, gridWidth, state, y, x) {
            if (x == gridWidth) {
                return undefined;
            }

            if (isSpaceAvailable(grid, state.height, state.width, y, x, gridHeight, gridWidth)) {
                return {x: x, y: y};
            }

            for (var i = 1; i < Math.floor(gridHeight / 2); i++) {
                if (isSpaceAvailable(grid, state.height, state.width, y + i, x, gridHeight, gridWidth)) {
                    return {x: x, y: y + i};
                }
                if (isSpaceAvailable(grid, state.height, state.width, y - i, x, gridHeight, gridWidth)) {
                    return {x: x, y: y - i};
                }
            }

            return findAvailableCoordinates(grid, gridHeight, gridWidth, state, y, x + 1);

            function isSpaceAvailable(grid, height, width, y, x, gridHeight, gridWidth) {
                for (var row = 0; row < height; row++) {
                    for (var column = 0; column < width; column++) {
                        if (y < 0 || x < 0 || y + row >= gridHeight || x + column >= gridWidth) {
                            return false;
                        }
                        if (grid[y + row][x + column] != null) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }

        function move(state, x, y) {
            state.x = state.x + x;
            state.y = state.y + y;
            _.each(state.next, function (s) {
                move(s, x, y);
            });
        }

        function markWidgetsInGrid(grid, state, gridwidth, ignore) {
            if (state === ignore) {
                return;
            }
            if (state.x < gridwidth) {
                grid[state.y][state.x] = 'x';
            }
            _.each(state.next, function (s) {
                markWidgetsInGrid(grid, s, gridwidth, ignore);
            });
        }


        function findLargestSquare(state, worth) {

            var result = undefined;
            if (state.processed == undefined && state.width * state.height > worth) {
                result = state;
                worth = state.width * state.height;
            }
            _.each(_.reverse(state.next), function (s) {
                var r = findLargestSquare(s, worth);
                if (r != undefined && r.width * r.height > worth) {
                    result = r;
                    worth = r.width * r.height;
                }
            });
            return result;
        }
    }

    function generateGrid(width, height) {
        var grid = new Array(height);
        for (var y = 0; y < height; y++) {
            grid[y] = new Array(width);
            for (var x = 0; x < width; x++) {
                grid[y][x] = null;
            }
        }
        return grid;
    }

    function findState(predicate) {
        return _.map(_.filter(directory.states, predicate), function (s) {
            return s.name
        });
    }

    function isInArray(array) {
        return function (value) {
            return _.some(array, function (el) {
                return el === value;
            });
        }
    }

    function isFollowing(precedent) {
        return function (state) {
            return _.some(state.precedents, function (p) {
                return p === precedent
            });
        };
    }

    function isStartingState(state) {
        return _.some(state.precedents, function (p) {
            return p === "uk.co.malbec.cascade.annotations.Step.Null"
        });
    }
}


function PathPlot() {
    this.type = 'path';
    this.spec = new PathSpec();

    this.getSpec = function () {
        return this.spec;
    }
}

function Coordinate(shape, verticalChannelWidths, horizontalChannelHeights) {

    this.shape = shape;
    this.verticalChannelWidths = verticalChannelWidths;
    this.horizontalChannelHeights = horizontalChannelHeights;
    this.x = 0;
    this.y = 0;
    this.cardinalX = 0;
    this.cardinalY = 0;


    this.resetAtCardinalPoint = function (cardinalX, cardinalY) {
        this.cardinalX = cardinalX;
        this.cardinalY = cardinalY;
        this.x = this.cardinalX * this.shape.width + sum(this.verticalChannelWidths, 0, this.cardinalX);
        this.y = this.cardinalY * this.shape.height + sum(this.horizontalChannelHeights, 0, this.cardinalY);
        return this;
    };

    this.resetCardinalX = function (cardinalX) {
        this.cardinalX = cardinalX;
        this.x = this.cardinalX * this.shape.width + sum(this.verticalChannelWidths, 0, this.cardinalX);
        return this;
    };
    this.resetCardinalY = function (cardinalY) {
        this.cardinalY = cardinalY;
        this.y = this.cardinalY * this.shape.height + sum(this.horizontalChannelHeights, 0, this.cardinalY);
        return this;
    };

    this.moveRightByOne = function () {
        this.x++;
        return this;
    };
    this.moveLeftByOne = function () {
        this.x--;
        return this;
    };
    this.moveUpByOne = function () {
        this.y--;
        return this;
    };
    this.moveDownByOne = function () {
        this.y++;
        return this;
    };

    this.moveRight = function (number) {
        this.x += number;
        return this;
    };
    this.moveLeft = function (number) {
        this.x -= number;
        return this;
    };
    this.moveDown = function (number) {
        this.y += number;
        return this;
    };
    this.moveUp = function (number) {
        this.y -= number;
        return this;
    };

    this.getX = function () {
        return this.x;
    };

    this.getY = function () {
        return this.y;
    };

    function sum(array, start, end) {
        var s = 0;
        for (var i = start; i < end; i++) {
            s = s + array[i];
        }
        return s;
    }
}

function PathSpec() {
    this.value = '';
    this.pointer = {};

    this.append = function (value) {
        this.value += value;
    };

    this.moveTo = function (coordinate) {
        this.pointer = {x: coordinate.getX(), y: coordinate.getY()};
        this.append(' M ' + (coordinate.getX() * 20 - 10) + ' ' + (coordinate.getY() * 20 + 10));
        return this;
    };

    this.drawArc = function (coordinate, sweep) {
        if (coordinate.getX() === this.pointer.x && coordinate.getY() === this.pointer.y) {
            return this;
        }
        this.pointer = {x: coordinate.getX(), y: coordinate.getY()};
        this.append(' A 20 20 0 0 ' + sweep + ' ' + (coordinate.getX() * 20 - 10) + ' ' + (coordinate.getY() * 20 + 10));
        return this;
    };

    this.drawVerticalTo = function (coordinate) {
        if (coordinate.getY() === this.pointer.y) {
            return this;
        }
        this.pointer.y = coordinate.getY();
        this.append(' V ' + (coordinate.getY() * 20 + 10));
        return this;
    };

    this.drawHorizontalTo = function (coordinate) {
        if (coordinate.getX() === this.pointer.x) {
            return this;
        }
        this.pointer.x = coordinate.getX();
        this.append(' H ' + (coordinate.getX() * 20 - 10));
        return this;
    };

    this.toString = function () {
        return this.value;
    }
}

function Direction(value) {
    this.value = value;
}

function LegType(value) {
    this.value = value;
}