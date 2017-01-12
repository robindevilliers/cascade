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


function Direction(value) {
    this.value = value;
}

function LegType(value) {
    this.value = value;
}