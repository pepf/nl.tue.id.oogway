# Oogway Motion

## Motion and Draw

### forward() | fd()
    forward(distance)
    fd(distance)
Parameters:	
* distance - float

Move the oogway forward by the specified distance, in the direction the oogway is headed.

### backward() | bk() | back()
    back(distance)
    bk(distance)
    backward(distance)
Parameters:	
* distance - float

Move the oogway backward by distance, opposite to the direction the oogway is headed. Do not change the oogway's heading.

### right() | rt()
	right(angle)
	rt(angle)
Parameters:	
* angle - float

Turn the oogway right by angle (degrees).

### left() | lt()
	left(angle)
	lt(angle)
Parameters:	
* angle - float

Turn the oogway left by angle (degrees).

### setpos() | setposition()
    setPos(x, y)
	setPosition(x, y)
Parameters:
* x, y - float. Absolute position to move to.

Move the oogway to an absolute position. No trace will be left, no matter whether the pen is down or up.

###home()
    home()
Move oogway to the origin, and set its heading to the right (west);

### stamp()
    stamp()
	stame(size)
	stamp(width, height)
Parameters:
* size - float. The ooway will be stamped in a square bounding box with the specified side size.
* width, height - float. The ooway will be stamped in a bounding box with the specified width and height.

Stamp a copy of the oogway shape onto the canvas at the current oogway position, along the heading orientation. By default an arrow is stamped, either filled or not filled, depending on the Processing graphics environment. An SVG shape can be stamped instead, using setStemp(). 

## Tell oogway's state

### xcor()
    xcor()
Return the oogway's x coordinate.

### ycor()
    ycor()
Return the oogway's x coordinate.

### heading()
    heading()
Return the oogway's current heading angle, in degrees.

### towards()
    towards(x, y)
   
Parameters:
* x, y - float.

Return the angle of the line from the oogway's position to the position specified by (x,y).

### distance()
    distance(x, y)
Parameters:
* x, y - float.

Return the distance from the oogway's position to the position specified by (x, y).

## Change oogway's trace
The following commands shall be always used in pairs. "begin" a trace will change the oogway's trace left on the canvas until "end" the trace. "begin"ing a trace will "end" the trace that has been "begin"ed, if it is not "end"ed. "end"ing a trace will change the trace to normal strait and solid lines.

### beginDash() and endDash()
    beginDash(pattern)
	beginDash()
	endDash()
Parameters:
* pattern - float[], dash pattern.

The dash "pattern" giving lengths of dashes and gaps in pixels; an array with values {10, 3, 9, 4} will draw a line with a 10-pixel dash, 3-pixel gap, 9-pixel dash, * and 4-pixel gap. if the array has an odd number of entries, the values
are recycled, so an array of {5, 3, 2} will draw a line with a 5-pixel dash, 3-pixel gap, 2-pixel dash, 5-pixel gap, 3-pixel dash, and 2-pixel gap, then repeat. If no pattern is given, {10, 5} is used by default.


