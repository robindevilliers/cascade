
function CoordinateSystem(shape, stateTree, paths) {
    var cardinalDimensions = findDimensions(stateTree.getRootState());
    var verticalChannelWidths = indexWidths(paths, cardinalDimensions);
    var horizontalChannelHeights = indexHeights(paths, cardinalDimensions);

    this.coordinateAtCardinalPoint = function (cardinalX, cardinalY) {
        return new Coordinate(shape, verticalChannelWidths, horizontalChannelHeights, cardinalX, cardinalY);
    };
    this.getVerticalChannelWidths = function () {
        return verticalChannelWidths;
    };
    this.getHorizontalChannelHeights = function () {
        return horizontalChannelHeights;
    };
    this.getWidth = function () {
        return _.sum(verticalChannelWidths) + cardinalDimensions.width * shape.width;
    };
    this.getHeight = function () {
        return _.sum(horizontalChannelHeights) + cardinalDimensions.height * shape.height;
    };
    this.getShapeWidth = function(){
        return shape.width;
    };
    this.getShapeHeight = function(){
        return shape.height;
    };

    function findDimensions(state) {
        var dimensions = {width: state.x + 1, height: state.y + 1};

        _.each(state.next, function (s) {
            var d = findDimensions(s);
            if (dimensions.width < d.width) {
                dimensions.width = d.width;
            }
            if (dimensions.height < d.height) {
                dimensions.height = d.height;
            }
        });
        return dimensions;
    }
}

function Coordinate(shape, verticalChannelWidths, horizontalChannelHeights, cardinalX, cardinalY) {

    this.cardinalX = cardinalX;
    this.cardinalY = cardinalY;
    this.x = this.cardinalX * shape.width + sum(verticalChannelWidths, 0, this.cardinalX);
    this.y = this.cardinalY * shape.height + sum(horizontalChannelHeights, 0, this.cardinalY);

    this.resetCardinalX = function (cardinalX) {
        this.cardinalX = cardinalX;
        this.x = this.cardinalX * shape.width + sum(verticalChannelWidths, 0, this.cardinalX);
        return this;
    };
    this.resetCardinalY = function (cardinalY) {
        this.cardinalY = cardinalY;
        this.y = this.cardinalY * shape.height + sum(horizontalChannelHeights, 0, this.cardinalY);
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


function indexWidths(paths, dimensions) {
    var widths = [];

    var groups = [];
    _.times(dimensions.width - 1, function () {
        groups.push([]);
    });

    _.each(paths, function (path) {
        _.each(path, function (leg, i) {
            if (leg.isType(LegTypeEnum.ADJACENT_VERTICAL)) {

                var previousLeg = path[i - 1];
                var postLeg = path[i + 1];
                if (previousLeg.isType(LegTypeEnum.INLINE_TOP_RIGHT) && postLeg.isType(LegTypeEnum.INLINE_BOTTOM_RIGHT)) {
                    leg.score = 4;
                } else if (previousLeg.isType(LegTypeEnum.INLINE_TOP_RIGHT) && postLeg.isType(LegTypeEnum.INLINE_BOTTOM_LEFT)) {
                    leg.score = 3;
                } else if (previousLeg.isType(LegTypeEnum.INLINE_TOP_LEFT) && postLeg.isType(LegTypeEnum.INLINE_BOTTOM_RIGHT)) {
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
                var vertical = new Array(dimensions.height);
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
    _.times(dimensions.height, function () {
        groups.push([]);
    });

    _.each(paths, function (path) {
        var partial = [], y = 0;
        _.each(path, function (leg) {
            if (leg.isType(LegTypeEnum.INLINE_VERTICAL)) {

            } else if (leg.isType(LegTypeEnum.ADJACENT_VERTICAL)) {
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
                    return leg.isType(LegTypeEnum.INLINE_HORIZONTAL);
                }) && !_.some(partial, function (leg) {
                    return leg.isType(LegTypeEnum.INLINE_BOTTOM_RIGHT) || leg.isType(LegTypeEnum.INLINE_BOTTOM_LEFT);
                })
        });

        placePartialsInChannel(channel, topPartials);

        //pull out all partials that are in the group don't have bottoms and do have horizontals.
        var topHorizontalPartials = _.filter(group, function (partial) {
            return _.some(partial, function (leg) {
                    return leg.isType(LegTypeEnum.INLINE_HORIZONTAL);
                }) && !_.some(partial, function (leg) {
                    return leg.isType(LegTypeEnum.INLINE_BOTTOM_RIGHT) || leg.isType(LegTypeEnum.INLINE_BOTTOM_LEFT);
                })
        });

        placePartialsInChannel(channel, topHorizontalPartials);

        //pull out all pieces that have both bottoms and tops.
        var middlePartials = _.filter(group, function (partial) {
            return _.some(partial, function (leg) {
                    return leg.isType(LegTypeEnum.INLINE_TOP_RIGHT) || leg.isType(LegTypeEnum.INLINE_TOP_LEFT);
                }) &&
                _.some(partial, function (leg) {
                    return leg.isType(LegTypeEnum.INLINE_BOTTOM_RIGHT) || leg.isType(LegTypeEnum.INLINE_BOTTOM_LEFT);
                })
        });

        placePartialsInChannel(channel, middlePartials);
        obstructShadowedPlaces(channel);

        //pull out all that don't have tops that have horizontals
        var bottomHorizontalPartials = _.filter(group, function (partial) {
            return _.some(partial, function (leg) {
                    return leg.isType(LegTypeEnum.INLINE_HORIZONTAL);
                }) && !_.some(partial, function (leg) {
                    return leg.isType(LegTypeEnum.INLINE_TOP_RIGHT) || leg.isType(LegTypeEnum.INLINE_TOP_LEFT);
                })
        });

        placePartialsInChannel(channel, bottomHorizontalPartials);
        obstructShadowedPlaces(channel);

        //pull out the remaining bottoms only.
        var bottomPartials = _.filter(group, function (partial) {
            return !_.some(partial, function (leg) {
                    return leg.isType(LegTypeEnum.INLINE_HORIZONTAL);
                }) && !_.some(partial, function (leg) {
                    return leg.isType(LegTypeEnum.INLINE_TOP_RIGHT) || leg.isType(LegTypeEnum.INLINE_TOP_LEFT);
                })
        });

        placePartialsInChannel(channel, bottomPartials);

        heights.push(channel.length + 2);
    });

    return heights;

    function obstructShadowedPlaces(channel) {
        for (var x = 0; x < dimensions.width * 2; x++) {
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
                if (leg.isType(LegTypeEnum.INLINE_TOP_RIGHT) || leg.isType(LegTypeEnum.INLINE_BOTTOM_RIGHT)) {

                    minX = Math.min(minX, leg.x * 2 + 1);
                    maxX = Math.max(maxX, leg.x * 2 + 1) + 1;
                    return;
                }
                if (leg.isType(LegTypeEnum.INLINE_TOP_LEFT) || leg.isType(LegTypeEnum.INLINE_BOTTOM_LEFT)) {
                    minX = Math.min(minX, leg.x * 2);
                    maxX = Math.max(maxX, leg.x * 2);
                    return;
                }

                if (leg.isType(LegTypeEnum.INLINE_HORIZONTAL)) {
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
                var horizontal = new Array(dimensions.width * 2);
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