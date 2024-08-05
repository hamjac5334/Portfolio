package hockey;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class hockeyController {

    @FXML
    private AnchorPane pane;
    @FXML
    private Circle paddle;
    @FXML
    private Circle redPaddle;
    @FXML
    private Slider learningFactorSlider;
    @FXML
    private Label scoreBlue;
    @FXML
    private Label scoreRed;
    @FXML
    private Label aH;

    private double learningFactor = 0.01;
    private int blueScore = 0;
    private int redScore = 0;
    private Random random = new Random();
    private boolean learn = true;

    private HashMap<ball, BallInfo> ballInfoMap;
    private ArrayList<ball> juggling;
    private ArrayList<hockeyView> bvs;
    private Movement clock;
    private Playing gameState = Playing.PLAYING; // Initial state is playing


    public class BallInfo {
        private double x;
        private double y;
        private double dx;
        private double dy;

        public BallInfo(double x, double y, double dx, double dy) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
        }
    }

    private class Movement extends AnimationTimer {

        private long FRAMES_PER_SEC = 50;
        private long INTERVAL = 1000000000L / FRAMES_PER_SEC;
        private long last = 0;
        private ArrayList<ball> bs;

        @Override
        public void handle(long now) {
            if (now - last > INTERVAL) {
                if (gameState == Playing.PLAYING) {
                    for (ball b : bs) {
                        checkCollisionGoal(b);
                        checkCollisionPaddle(b, paddle);
                        checkCollisionPaddle(b, redPaddle);
                        b.move();
                        BallInfo ballInfo = new BallInfo(b.getX(), b.getY(), b.getDx(), b.getDy());
                        ballInfoMap.put(b, ballInfo);
                    }
                    aiMovement();
                    updateViews();
                }

                last = now;
            }
        }

        public void setBallInfoMap(HashMap<ball, BallInfo> ballInfoMap) {
            hockeyController.this.ballInfoMap = ballInfoMap;
        }

        public void setBalls(ArrayList<ball> bs) {
            this.bs = bs;
        }
    }

    @FXML
    public void initialize() {
        juggling = new ArrayList<>();
        bvs = new ArrayList<>();
        ballInfoMap = new HashMap<>();
        makeBouncyBall();

        clock = new Movement();
        clock.setBalls(juggling);
        clock.setBallInfoMap(ballInfoMap);
        pane.setOnKeyPressed(this::movementSetup);
        //Slider experiment
        learningFactorSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            learningFactor = newValue.doubleValue();
            System.out.println("Learning Factor: " + learningFactor);
        });
        updateViews();
    }

    // Gameplay methods

    @FXML
    public void gameStart() {
        gameState = Playing.PLAYING;
        scoreBlue.setText(String.format(Integer.toString(blueScore)));
        scoreRed.setText(String.format(Integer.toString(redScore)));
        resetBallSpeed();
        aH.setText("Air Hockey");
        clock.start();
        aiMovement();
    }
    private void resetBallSpeed() {
        for (ball b : juggling) {
            b.resetSpeed();
        }
    }

    @FXML
    public void gamePause() {
        gameState = Playing.PAUSE;
        clock.stop();
    }

    @FXML
    public void gameReset() {
        gameState = Playing.RESET;
        clock.stop();
        blueScore = 0;
        redScore = 0;
        scoreBlue.setText(String.format(Integer.toString(blueScore)));
        scoreRed.setText(String.format(Integer.toString(redScore)));
        resetGameElements();
        ballInfoMap.clear();
    }

    public void resetGameElements() {
        paddle.setTranslateY(0);
        redPaddle.setTranslateY(0);
        for (ball b : juggling) {
            b.setX(200);
            b.setY(200);
        }
    }

    // Movement when key is pressed
    @FXML
    public void movementSetup(KeyEvent k) {
        if (k.getCode() == KeyCode.W) {
            paddle.setTranslateY(paddle.getTranslateY() - 20.0);
            System.out.println("m");
        }
        if (k.getCode() == KeyCode.S) {
            paddle.setTranslateY(paddle.getTranslateY() + 20.0);
        }
    }
    // Controls movements of redPaddle/ai
    @FXML
    public void aiMovement() {
        if (!juggling.isEmpty() && !ballInfoMap.isEmpty()) {
            ball targetBall = juggling.get(0);
            BallInfo ballInfo = ballInfoMap.get(targetBall);

            if (ballInfo != null) {
                double currentY = redPaddle.getCenterY();
                double predictedY = predictBallPosition(ballInfo);
                double positionDif = predictedY - currentY;
                double randomMove = (random.nextDouble() - 0.5) * calculateLearningParameter() * learningFactor;
                double learnRate = calculateLearningRate();

                if (learn) {
                    double randomFactor = (random.nextDouble() - 0.1);
                    redPaddle.setTranslateY(currentY + learnRate * positionDif * randomFactor + randomMove);
                    adjustLearningParameter();
                }
            }
        }
    }

    private double predictBallPosition(BallInfo ballInfo) {
        double predictionNum = 1.5;

        double predictedY = ballInfo.y + predictionNum * ballInfo.dy;
        return predictedY;
    }

    private double calculateLearningParameter() {
        return 20.0;
    }

    private double calculateLearningRate() {
        return 0.5;
    }

    private void adjustLearningParameter() {
        // Implement your adjustment logic here
    }

    private void makeBouncyBall() {
        ball b = new ball(10, 50, 65, 560, 348);
        juggling.add(b);
        hockeyView bv = new hockeyView(b);
        bvs.add(bv);
        pane.getChildren().add(bv);
        updateViews();
    }

    private void updateViews() {
        for (hockeyView bv : bvs) {
            bv.update();
        }
        paddle.setTranslateY(paddle.getTranslateY());
    }

    public void checkCollisionPaddle(ball b, Circle paddle) {
        double distanceX = Math.abs((b.getX()-50) - (paddle.getLayoutX() + paddle.getTranslateX()));// + paddle.getRadius()));
        double distanceY = Math.abs((b.getY()-65) - (paddle.getLayoutY() + paddle.getTranslateY()));// + paddle.getRadius()));
        System.out.println(paddle.getFill());
        System.out.println("ball x: " + (b.getX()-50));
        System.out.println("ball y: " + (b.getY()-65));
        System.out.println("paddle x: " + (paddle.getLayoutX() + paddle.getTranslateX()));
        System.out.println("paddle y: " + (paddle.getLayoutY() + paddle.getTranslateY()));
        //error is below
        double radiusSum = b.getRadius() + paddle.getRadius();
        double totalDistance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
        //note: use circumpherence form. to find collision // found formula from YOutube
        if (radiusSum >= totalDistance) {
            double angle = Math.atan2(b.getY() - paddle.getTranslateY(), b.getX() - paddle.getTranslateX());
            double speed = Math.sqrt(b.getDx() * b.getDx() + b.getDy() * b.getDy());
            double newSpeed = speed + Math.random() * 1;


            if (b.getX() < 200) {
                if(b.getY() > 150 ) {
                    b.setDx(newSpeed * Math.cos(angle) + Math.random() * 2);
                    //b.setDy(newSpeed * Math.sin(angle) + Math.random() * 2);
                }else {
                    b.setDx(newSpeed * Math.cos(angle) + Math.random() * 2);
                    //b.setDy(-newSpeed);
                }
            }
            else if (b.getX() > 200) {
                if (b.getY() > 150)
                    b.setDx(-newSpeed * Math.cos(angle)+ Math.random() * 2);
                    //b.setDy(newSpeed * Math.sin(angle)+ Math.random() * 2);
                }else {
                    b.setDx(newSpeed * Math.cos(angle)+ Math.random() * 2);
                    //b.setDy(-newSpeed * Math.sin(angle)+ Math.random() * 2);
            }
        }

    }

    public void checkCollisionGoal(ball b) {
        //bllue collide
        if(b.getX() - 10 < 41 &&  (b.getY() < 260 && b.getY() > 150)) {
            redScore += 1;
            if (redScore == 5) {
                aH.setText("Red Wins");
                gameReset();
            }
            resetGameElements();
            ballInfoMap.clear();
            scoreRed.setText(String.format(Integer.toString(redScore)));
            gamePause();
        }
        //red Collision with goal
        if(b.getX() +10 > 558 && (b.getY() < 260 && b.getY()> 150)) {
            blueScore +=1;
            if (blueScore == 5) {
                aH.setText("Blue Wins");
                gameReset();
            }
            resetGameElements();
            ballInfoMap.clear();
            scoreBlue.setText(String.format(Integer.toString(blueScore)));
            gamePause();
        }
    }
}