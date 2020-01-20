# Physics2D
A 2D physics project

## Classes
- **[Visualizer](#visualizer)**
- **[Vector2D](#vector2d)**
- **[PhysObj](#physobj)**
- **[Figure](#figure)**
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
	- represents vector coordinates.
	
### Constructors
- **Vector2D()**
    - returns zero vector.
- **Vector2D(double x, double y)**
    - returns vector with coordinates given in arguments.

### Static Methods
- **intersection(Vector2D a, Vector2D b, Vector2D c, Vector2D d)**
    - returns new vector represents point intersection of two lines specified with points a, b and c, d.
    - if there is no intersection returns null.

### Concrete Methods
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
- **angleBetween(Vector2D v)**
    - returns value equal to angle between this vector and vector given in the argument.
- **clone()**
	- returns copy of this vector.
- **toString()**
	- format: (x, y).

________________________________________

## <a name = "physobj" >PhysObj</a>


________________________________________

## <a name = "figure" >Figure</a>


________________________________________
