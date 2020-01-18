# Physics2D
A 2D physics project

## Classes
- **[Visualizer](#visualizer)**
- **[Vector2D](#vector2d)**
- **PhysObj**
- **Figure**
    - **Circle**
    - **Polygon**
            
________________________________________

## <a name = "visualizer" >Visualizer</a>

### Methods
- **onDraw()**

________________________________________

## <a name = "vector2d" >Vector2D</a>

### Fields  
- **double x, y**
	- represents this vector coordinates.

### Methods
- **add(Vector2D v)**
    - returns a new vector equal to the sum of this vector and vector given in the argument.
- **sub(Vector2D v)**
	- returns a new vector equal to the difference of this vector and vector given in the argument.
- **scale(double mult)**
- **reverse()**
- **length()**
- **rotate(double angle)**
- **rotate(double sinA, double cosA)**
- **zero()**
    - Returns zero vector
- **normalize()**
	- returns a new vector equal to this but normalized vector.
- **scalar(Vector2D v)**
    - returns value equal to scalar product of this vector and vector given in the argument.
