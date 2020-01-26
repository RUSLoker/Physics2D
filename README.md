# Physics2D
A 2D physics project

## Classes
- **[Visualizer](#visualizer)**
- **[Vector2D](#vector2d)**
- **[PhysObj](#physObj)**
- **[Collision](#collision)**
- **[Figure2D](#figure2d)**
    - **[Circle](#circle)**
    - **[Polygon](#polygon)**
            
________________________________________

## <a name = "visualizer" >Visualizer</a>

### Methods
- **onDraw()**

________________________________________

## <a name = "vector2d" >Vector2D</a>

### Fields  
- **double x, y**
	- represents vector coordinates.
- **double length**
    - represents length of the vector.
- **double sqrLength**
    - represents squared length of the vector.
	
### Constructors
- **Vector2D(double x, double y)**
    - returns vector with coordinates given in arguments.

### Static Methods
- **intersection(Vector2D a, Vector2D b, Vector2D c, Vector2D d)**
    - returns new vector represents point intersection of two lines specified with points a, b and c, d.
    - if there is no intersection returns null.
- **sum(Vector2D[] vArr)**
	- returns new vector equal to the sum of all vectors in parameters.
- **length(double x, double y)**
	- returns length of vector.
- **sqrLength(double x, double y)**
	- returns squared length of vector.
	- works faster than length().

### Concrete Methods
- **add(Vector2D v)**
    - returns a new vector equal to the sum of this vector and vector given in the argument.
- **sub(Vector2D v)**
	- returns a new vector equal to the difference of this vector and vector given in the argument.
- **scale(double mult)**
- **reverse()**
	- returns reversed vector.
- **rotate(double angle)**
- **rotate(double sinA, double cosA)**
- **zero()**
    - returns zero vector.
- **normalize()**
	- returns a new vector equal to this but normalized vector.
- **scalar(Vector2D v)**
    - returns value equal to scalar product of this vector and vector given in the argument.
- **angleBetween(Vector2D v)**
    - returns value equal to angle between this vector and vector given in the argument in radians.
- **setLength(double len)**
- **clone()**
	- returns copy of this vector.
- **toString()**
	- format: (x, y).

________________________________________

## <a name = "physObj" >PhysObj</a>

### Fields  
- **Figure2D body**

### Concrete Methods
- **draw(Canvas canvas, Paint paint)**
- **rotate(double angle)**


________________________________________

## <a name = "collision" >Collision</a>

### Modifiers
- **package-private**
- **abstract**

### Concrete Methods
- **getCollision(Circle a, Circle b)**
- **getCollision(Polygon a, Polygon b)**
- **getCollision(Circle a, Polygon b)**

________________________________________

## <a name = "figure2d" >Figure2D</a>

### Modifiers
- **public**
- **abstract**
- **extends Collision**

### Abstract Methods
- **getCollision(Figure2D f)**
- **getCollision(Circle c)**
- **getCollision(Polygon p)**
- **draw(Canvas canvas, Paint paint)**
- **rotate(double angle)**

________________________________________

## <a name = "circle" >Circle</a>

### Modifiers
- **public**
- **extends Figure2D**

### Fields  
- **Vector2D center**
- **double radius**
	
### Constructors
- **Circle(Vector2D center, double radius)**

### Concrete Methods
- **getCollision(Figure2D f)**
- **getCollision(Circle c)**
- **getCollision(Polygon p)**
- **draw(Canvas canvas, Paint paint)**
- **rotate(double angle)**

________________________________________

## <a name = "polygon" >Polygon</a>

### Modifiers
- **public**
- **extends Figure2D**

### Concrete Methods
- **getCollision(Figure2D f)**
- **getCollision(Circle c)**
- **getCollision(Polygon p)**
- **draw(Canvas canvas, Paint paint)**
- **rotate(double angle)**

________________________________________


