package hockey;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
//import javafx.scene.geometry.Bounds;

import java.util.ArrayList;

public class ball extends Parent {
    private double x;
    private double y;
    private double dx;
    private double dy;
    private double radius;
    private boolean canMove;
    @FXML
    private AnchorPane pane;
    @FXML
    private Circle circle;


    private int minX;
    private int minY;
    private int maxX;
    private int maxY;
    @FXML
    private Circle paddle;


    public ball(double radius, int minX, int minY, int maxX, int maxY) {
        this.radius = radius;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;

        x = radius + (Math.random() * (maxX - minX - (2 * radius))) + minX;
        y = radius + (Math.random() * (maxY - minY - (2 * radius))) + minY;

        dx = 5;
        dy = 5;

        canMove = true;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


    public void setDy(double newDy){ dy = newDy;}
    public void setDx(double newDx){ dx = newDx;}

    public double getDy() {
        return dy;
    }

    public double getDx() {
        return dx;
    }

    public double getRadius() {
        return radius;
    }

    public void toggleMovement() {
        canMove = !canMove;
    }

    public void resetSpeed() {
        // Reset the speed when needed
        dx =6;
        dy = 6;
    }

    public void move() {
        if (canMove) {
            x += dx;

            double overX = Math.max(0, x + radius - maxX);
            overX = Math.max(overX, minX - x);
            if (overX > 0) {
                dx *= -1;
                x += Math.signum(dx) * overX;
            }

            y += dy;
            double overY = Math.max(0, y + radius - maxY);
            overY = Math.max(overY, minY - y);
            if (overY > 0) {
                dy *= -1;
                y += Math.signum(dy) * overY;
            }
        }

    }
}



