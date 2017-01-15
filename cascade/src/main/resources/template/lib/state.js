function StateTree(directory) {
    this.stateTree = generateStateTree(directory);

    this.getRootState = function () {
        return this.stateTree;
    };

    this.layoutHierarchically = function () {
        assignStateWidth(this.stateTree);
        assignStateHeight(this.stateTree);
        assignStateCoordinates(this.stateTree, 0, 0);

        return this;

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
    };


    this.squash = function () {

        var height = this.stateTree.height;

        //iterate through all squares from largest to smallest
        var largestSquare = findLargestSquare(this.stateTree, 0);
        while (largestSquare != undefined) {

            var gridWidth = largestSquare.x + largestSquare.width - 1;

            if (gridWidth >= largestSquare.width) { //skip repositioning algorithm - square is too wide.
                var grid = generateGrid(gridWidth, height);
                markWidgetsInGrid(grid, this.stateTree, gridWidth, largestSquare);

                var coordinates = findAvailableCoordinates(grid, height, gridWidth, largestSquare, largestSquare.y - 1, 0);

                if (coordinates) {
                    move(largestSquare, coordinates.x - largestSquare.x, coordinates.y - largestSquare.y);
                }
            }

            largestSquare.processed = true;
            largestSquare = findLargestSquare(this.stateTree, 0);
        }

        return this;

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
    };


    function generateStateTree(directory) {
        var seen = [];
        var lanes = _.map(directory.findStartingStates(), function (name) {
            return {state: name, next: [], connections: []}
        });
        var nextSet = _.union([], lanes);

        while (!_.isEmpty(nextSet)) {

            var workingSet = nextSet;
            nextSet = [];

            _.each(workingSet, function (currentState) {

                var followingStates = directory.findStatesThatFollow(currentState.state);

                var notAlreadySeen = _.reject(followingStates, function (name) {
                    return _.some(seen, function (el) {
                        return el === name;
                    });
                });

                _.each(notAlreadySeen, function (name) {
                    seen.push(name);

                    var node = {state: name, next: [], connections: []};
                    currentState.next.push(node);
                    nextSet.push(node);
                });
            });
        }
        return {next: lanes};
    }
}