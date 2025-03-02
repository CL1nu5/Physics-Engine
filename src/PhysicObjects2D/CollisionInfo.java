package PhysicObjects2D;

public class CollisionInfo {

    /* values */

    public Object2D shapeA, shapeB;
    public double distance;
    public Vector2D separationDirection, separationDistance;
    public boolean shapeAContained, shapeBContained;

    public CollisionInfo(Object2D shapeA, Object2D shapeB) {
        this.shapeA = shapeA;
        this.shapeB = shapeB;
        distance = 0.0;
        separationDirection = null;
        separationDistance = null;
        shapeAContained = true;
        shapeBContained = true;
    }

    public String toString() {
        return
                "Shape A: " + shapeA.toString() + "\n"               + "Shape B: " + shapeB.toString() + "\n" +
                "distance: " + distance + "\n"                       + "direction: " + separationDirection.toString() + "\n" +
                "distance: " + separationDistance.toString() + "\n"  + "shapeAContained: " + shapeAContained + "\n" +
                "shapeBContained: " + shapeBContained + "\n"  ;
    }
}
