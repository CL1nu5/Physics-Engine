package PhysicObjects2D;

public class CollisionInfo {

    /* values */

    public Object2D shapeA, shapeB;
    public double distance;
    public Vector2D separationDirection, separationDistance;
    public boolean shapeAContained, shapeBContained;

    /* constructor */

    //creates a collision info based on two shapes
    public CollisionInfo(Object2D shapeA, Object2D shapeB) {
        this.shapeA = shapeA;
        this.shapeB = shapeB;
        distance = 0.0;
        separationDirection = null;
        separationDistance = null;
        shapeAContained = true;
        shapeBContained = true;
    }

    /* public methods */

    //returns the opposite of the current collision info
    public CollisionInfo getOpposite(){
        CollisionInfo result = new CollisionInfo(shapeB, shapeA);

        result.distance = this.distance;

        result.separationDirection = this.separationDirection;
        result.separationDistance = this.separationDistance;

        result.shapeAContained = this.shapeBContained;
        result.shapeBContained = this.shapeAContained;

        return result;
    }

    /* basic methods */

    public String toString() {
        return
                "Shape A: " + shapeA.toString() + "\n"               + "Shape B: " + shapeB.toString() + "\n" +
                "distance: " + distance + "\n"                       + "direction: " + separationDirection.toString() + "\n" +
                "distance: " + separationDistance.toString() + "\n"  + "shapeAContained: " + shapeAContained + "\n" +
                "shapeBContained: " + shapeBContained + "\n"  ;
    }
}
