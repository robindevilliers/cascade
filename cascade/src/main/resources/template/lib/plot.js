function Plot(coordinateSystem) {
    var plots = [];

    this.getPlots = function () {
        return plots;
    };

    this.plotStates = plotStates;
    this.plotPaths = plotPaths;

    function plotStates(state) {
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
            plotStates(s);
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

    function plotPaths(paths) {

        var topPartials = _.map(paths, function (p) {
            return p[0];
        });
        var bottomPartials = _.map(paths, function (p) {
            return p[p.length - 1];
        });

        _.each(paths, function (path) {
            var coordinate;
            var plot = new PathPlot();
            var previousLeg = null;

            var totalSharing, offset;

            _.each(path, function (leg) {
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

                        totalSharing = countSharingPartials(topPartials, leg);
                        offset = calculateOffset(topPartials, leg, true);

                        if (totalSharing < 4){
                            plot.getSpec()
                                .moveTo(coordinate.moveRightByOne().moveRight(offset))
                                .drawVerticalTo(coordinate.moveDown(leg.gridYIndex))
                                .drawArc(coordinate.moveRightByOne().moveDownByOne(), 0);
                        } else if (totalSharing - offset < 5){
                            plot.getSpec()
                                .moveTo(coordinate.moveRightByOne().moveRight(offset - (totalSharing - 4)))
                                .drawVerticalTo(coordinate.moveDown(leg.gridYIndex))
                                .drawArc(coordinate.moveRightByOne().moveDownByOne(), 0);
                        } else {
                            plot.getSpec()
                                .moveTo(coordinate.moveRightByOne().moveDown(leg.gridYIndex).moveDownByOne());
                        }
                        break;
                    case LegTypeEnum.INLINE_TOP_LEFT:
                        coordinate = coordinateSystem
                            .coordinateAtCardinalPoint(leg.x, leg.y)
                            .moveRight(Math.round(coordinateSystem.getShapeWidth() / 2))
                            .moveDown(coordinateSystem.getShapeHeight());

                        totalSharing = countSharingPartials(topPartials, leg);
                        offset = calculateOffset(topPartials, leg, true);

                        if (totalSharing < 4){
                            plot.getSpec()
                                .moveTo(coordinate.moveLeftByOne().moveLeft(offset))
                                .drawVerticalTo(coordinate.moveDown(leg.gridYIndex))
                                .drawArc(coordinate.moveLeftByOne().moveDownByOne(), 1);
                        } else if (totalSharing - offset < 5){
                            plot.getSpec()
                                .moveTo(coordinate.moveLeftByOne().moveLeft(offset - (totalSharing - 4)))
                                .drawVerticalTo(coordinate.moveDown(leg.gridYIndex))
                                .drawArc(coordinate.moveLeftByOne().moveDownByOne(), 1);
                        } else {
                            plot.getSpec()
                                .moveTo(coordinate.moveLeftByOne().moveDown(leg.gridYIndex).moveDownByOne());
                        }
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
                                            plot.getSpec().drawArc(coordinate.moveRightByOne().moveDownByOne(), 0);
                                            break;
                                    }
                                    break;
                            }
                        }

                        if (leg.isType(LegTypeEnum.INLINE_BOTTOM_RIGHT)) {

                            coordinate
                                .resetCardinalX(leg.x)
                                .moveRight(Math.round(coordinateSystem.getShapeWidth() / 2));

                            totalSharing = countSharingPartials(bottomPartials, leg);
                            offset = calculateOffset(bottomPartials, leg, false);

                            if (totalSharing < 5){
                                plot.getSpec()
                                    .drawHorizontalTo(coordinate.moveRightByOne()
                                        .moveRight(offset)
                                        .moveRightByOne()
                                    )
                                    .drawArc(coordinate.moveLeftByOne().moveDownByOne(), 0)
                                    .drawVerticalTo(coordinate.resetCardinalY(leg.y + 1).moveUpByOne());
                            } else if (totalSharing - offset < 5) {
                                plot.getSpec()
                                    .drawHorizontalTo(coordinate.moveRightByOne()
                                        .moveRight(offset - (totalSharing - 4))
                                        .moveRightByOne()
                                    )
                                    .drawArc(coordinate.moveLeftByOne().moveDownByOne(), 0)
                                    .drawVerticalTo(coordinate.resetCardinalY(leg.y + 1).moveUpByOne());
                            } else {
                                plot.getSpec()
                                    .drawHorizontalTo(coordinate.moveRightByOne());
                            }

                        } else if (leg.isType(LegTypeEnum.INLINE_BOTTOM_LEFT)) {

                            coordinate
                                .resetCardinalX(leg.x)
                                .moveRight(Math.round(coordinateSystem.getShapeWidth() / 2));

                            totalSharing = countSharingPartials(bottomPartials, leg);
                            offset = calculateOffset(bottomPartials, leg, false);

                            if (totalSharing < 5) {
                                plot.getSpec()
                                    .drawHorizontalTo(coordinate.moveLeftByOne()
                                        .moveLeft(offset)
                                        .moveLeftByOne()
                                    )
                                    .drawArc(coordinate.moveRightByOne().moveDownByOne(), 1)
                                    .drawVerticalTo(coordinate.resetCardinalY(leg.y + 1).moveUpByOne());
                            } else if (totalSharing - offset < 5) {

                                plot.getSpec()
                                    .drawHorizontalTo(coordinate.moveLeftByOne()
                                        .moveLeft(offset - (totalSharing - 4))
                                        .moveLeftByOne()
                                    )
                                    .drawArc(coordinate.moveRightByOne().moveDownByOne(), 1)
                                    .drawVerticalTo(coordinate.resetCardinalY(leg.y + 1).moveUpByOne());
                            } else {
                                plot.getSpec()
                                    .drawHorizontalTo(coordinate.moveLeftByOne());
                            }
                        }

                        break;
                }


                previousLeg = leg;
            });
            plots.push(plot);

            function countSharingPartials(partials, leg) {
                return _.filter(partials, function (p) {
                    return p.getType() == leg.getType() && p.x == leg.x && p.y == leg.y;
                }).length;
            }

            function calculateOffset(partials, leg, top) {
                return _.chain(partials)
                    .filter(function (p) {
                        return p.getType() == leg.getType() && p.x == leg.x && p.y == leg.y;
                    })
                    .orderBy(function (p) {
                        return top ? -p.gridYIndex : p.gridYIndex;
                    })
                    .findIndex(function (p) {
                        return p.gridYIndex === leg.gridYIndex;
                    })
                    .value();
            }
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