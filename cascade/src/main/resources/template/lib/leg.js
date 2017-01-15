
Leg = {
    getType: function(){
        return this.aspect;
    },
    isType: function(type){
        return this.aspect === type;
    }
};

InlineVerticalLeg.prototype = Leg;
InlineTopRightLeg.prototype = Leg;
InlineTopLeftLeg.prototype = Leg;
InlineBottomRightLeg.prototype = Leg;
InlineBottomLeftLeg.prototype = Leg;
AdjacentVerticalLeg.prototype = Leg;
InlineHorizontalLeg.prototype = Leg;


function InlineVerticalLeg(x, y) {

    this.aspect = LegTypeEnum.INLINE_VERTICAL;
    this.x = x;
    this.y = y;
    this.direction = DirectionEnum.DOWN;
}

function InlineTopRightLeg(x, y) {
    this.aspect = LegTypeEnum.INLINE_TOP_RIGHT;
    this.x = x;
    this.y = y;
    this.direction = DirectionEnum.RIGHT;
}

function InlineTopLeftLeg(x, y) {
    this.aspect = LegTypeEnum.INLINE_TOP_LEFT;
    this.x = x;
    this.y = y;
    this.direction = DirectionEnum.LEFT;
}

function InlineBottomRightLeg(x, y) {
    this.aspect = LegTypeEnum.INLINE_BOTTOM_RIGHT;
    this.x = x;
    this.y = y;
    this.direction = DirectionEnum.LEFT;
}

function InlineBottomLeftLeg(x, y) {
    this.aspect = LegTypeEnum.INLINE_BOTTOM_LEFT;
    this.x = x;
    this.y = y;
    this.direction = DirectionEnum.RIGHT;
}

function AdjacentVerticalLeg(x, sourceY, targetY, direction) {
    this.aspect = LegTypeEnum.ADJACENT_VERTICAL;
    this.x = x;
    this.sy = sourceY;
    this.ty = targetY;
    this.direction = direction;
}

function InlineHorizontalLeg(sourceX, targetX, y, direction) {
    this.aspect = LegTypeEnum.INLINE_HORIZONTAL;
    this.sx = sourceX;
    this.tx = targetX;
    this.ty = y;
    this.direction = direction;
}