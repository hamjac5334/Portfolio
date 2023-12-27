package hockey;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class hockeyView extends Parent {
    private ball ball;
    private Circle c;
    public hockeyView(ball ball) {
        this.ball = ball;

        c = new Circle();
        c.setFill(Color.GREY);
        getChildren().add(c);
    }
    public void update() {
        c.setRadius(ball.getRadius());
        c.setTranslateX(ball.getX());
        c.setTranslateY(ball.getY());
    }
}
