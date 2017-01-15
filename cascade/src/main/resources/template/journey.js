
var directory = new Directory(directoryData);

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
    return _.find(directoryData.items, function (journey) {
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


    var stateTree = new StateTree(directory);

    stateTree.layoutHierarchically();
    stateTree.squash();

    var paths = calculatePaths(stateTree.getRootState(), stateTree, directory);

    var coordinateSystem = new CoordinateSystem({width: 9, height: 3}, stateTree, paths);

    //TODO - create plot object. Let it take a coordinate system object in its constructor.
    var plots = [];
    plotStates(stateTree.getRootState(), plots, coordinateSystem);
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
                                        .resetCardinalY(previousLeg.ty - 1)
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
