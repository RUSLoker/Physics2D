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
	- 

### Methods
- **add(Vector2D v)**
    - returns a new vector equal to the sum of this vector and vector given in the arguments.
- **sub(Vector2D v)**
	- returns a new vector equal to the difference of this vector and vector given in the arguments.
- **scale(double mult)**
- **reverse()**
- **length()**
- **rotate(double angle)**
- **zero()**
    * Returns zero vector
- **normalize()**
	* returns a new vector equal to this but normalized vector.
