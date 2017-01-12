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
    $("#synopsis").find("#tab-body").append(journeySynopsisPanelTemplate(journey));
}

function renderFilterPanel() {
    var journeyFilterTemplate = _.template($("#journey-filter-template").text());
    $("#filter").find("#tab-body").append(journeyFilterTemplate(journey));
}


function renderAnalysisPanel() {


    var stateTree = generateStateTree();
    layoutHierarchically(stateTree);
    squash(stateTree, stateTree.height);

    var paths = calculatePaths(stateTree, stateTree);

    //TODO - encapsulate all these measures - dimensions, indexWidths, indexHeights
    var dimensions = findDimensions(stateTree);
    var coordinateSystem = new CoordinateSystem(
        dimensions,
        {width: 9, height: 3},
        indexWidths(paths, dimensions),
        indexHeights(paths, dimensions)
    );

    //TODO - create plot object. Let it take a coordinate system object in its constructor.
    var plots = [];
    plotStates(stateTree, plots, coordinateSystem);
    plotPaths(paths, plots, coordinateSystem);

    var journeyAnalysisTemplate = _.template($("#journey-analysis-template").text());
    var data = {
        plots: plots,
        dimensions: {
            width: coordinateSystem.getWidth(),
            height: coordinateSystem.getHeight()
        }
    };
    $("#analysis").find("#tab-body").append(journeyAnalysisTemplate(data));


    function plotPaths(paths, plots, coordinateSystem) {

        _.each(paths, function (path) {
            var coordinate;
            var plot = new PathPlot();
            var previousLeg = null;

            _.each(path, function (leg) {
                //TODO - if offset is over 4 then a different strategy must take place.
                switch (leg.getType()) {
                    case LegTypeEnum.INLINE_VERTICAL:
                        coordinate = coordinateSystem
                            .coordinateAtCardinalPoint(leg.x, leg.y)
                            .moveRight(Math.round(coordinateSystem.getShapeWidth() / 2))
                            .moveDown(coordinateSystem.getShapeHeight());

                        plot.getSpec()
                            .moveTo(coordinate)
                            .drawVerticalTo(coordinate.resetCardinalY(leg.y + 1).moveUpByOne());
                        break;
                    case LegTypeEnum.INLINE_TOP_RIGHT:
                        coordinate = coordinateSystem
                            .coordinateAtCardinalPoint(leg.x, leg.y)
                            .moveRight(Math.round(coordinateSystem.getShapeWidth() / 2))
                            .moveDown(coordinateSystem.getShapeHeight());

                        var offset = calculateHeaderOffset(_.map(paths, function (p) {
                            return p[0];
                        }), leg, true);

                        plot.getSpec()
                            .moveTo(coordinate.moveRightByOne().moveRight(offset))
                            .drawVerticalTo(coordinate.moveDown(leg.gridYIndex))
                            .drawArc(coordinate.moveRightByOne().moveDownByOne(), 0);
                        break;
                    case LegTypeEnum.INLINE_TOP_LEFT:
                        coordinate = coordinateSystem
                            .coordinateAtCardinalPoint(leg.x, leg.y)
                            .moveRight(Math.round(coordinateSystem.getShapeWidth() / 2))
                            .moveDown(coordinateSystem.getShapeHeight());

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
                                    .moveRight(coordinateSystem.getShapeWidth())
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
                                    .moveRight(coordinateSystem.getShapeWidth())
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

                        if (previousLeg.isType(LegTypeEnum.ADJACENT_VERTICAL)) {

                            switch (previousLeg.direction) {
                                case DirectionEnum.UP:

                                    plot.getSpec().drawVerticalTo(coordinate
                                        .resetCardinalY(leg.y)
                                        .moveDown(coordinateSystem.getShapeHeight())
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
                                        .moveDown(coordinateSystem.getShapeHeight())
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

                        if (leg.isType(LegTypeEnum.INLINE_BOTTOM_RIGHT)) {

                            coordinate
                                .resetCardinalX(leg.x)
                                .moveRight(Math.round(coordinateSystem.getShapeWidth() / 2));

                            //TODO - figure out what to do with this.
                            var offset = calculateHeaderOffset(_.map(paths, function (p) {
                                return p[p.length - 1];
                            }), leg, false);

                            plot.getSpec()
                                .drawHorizontalTo(coordinate.moveRightByOne().moveRight(offset).moveRightByOne())
                                .drawArc(coordinate.moveLeftByOne().moveDownByOne(), 0)
                                .drawVerticalTo(coordinate.resetCardinalY(leg.y + 1).moveUpByOne());

                        } else if (leg.isType(LegTypeEnum.INLINE_BOTTOM_LEFT)) {

                            coordinate
                                .resetCardinalX(leg.x)
                                .moveRight(Math.round(coordinateSystem.getShapeWidth() / 2));

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
                    return p.getType() == leg.getType() && p.x == leg.x && p.y == leg.y;
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

    function plotStates(state, plots, coordinateSystem) {
        if (state.state) {
            var coordinate = coordinateSystem.coordinateAtCardinalPoint(state.x, state.y);
            plots.push({
                type: 'state',
                name: simpleName(state.state),
                gridX: coordinate.getX(),
                gridY: coordinate.getY()
            });
        }
        _.each(state.next, function (s) {
            plotStates(s, plots, coordinateSystem);
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
