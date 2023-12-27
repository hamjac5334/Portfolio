package work;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class workController {
    @FXML
    public TextField enterInt;
    @FXML
    private TextField mean;
    @FXML
    private TextField median;
    @FXML
    private TextField mode;
    @FXML
    private TextField data;
    @FXML
    private Rectangle button1;
    @FXML
    private Rectangle button2;
    @FXML
    private Rectangle button3;
    @FXML
    private Rectangle button4;
    @FXML
    private Rectangle button5;
    private AnimationTimer animationTimer;

    Rectangle[] button;
    int[] buttonCount = new int[]{0, 0, 0, 0, 0};
    int[] buttonHeight = new int[]{12, 12, 12, 12, 12};


    // creates ArrayList
    ArrayList<Integer> numbers = new ArrayList<>();

    // Initializes the program
    @FXML
    public void initialize() {
        enterInt.setEditable(true);
        data.setEditable(false);
        mean.setEditable(false);
        median.setEditable(false);
        mode.setEditable(false);
        button = new Rectangle[]{button1, button2, button3, button4, button5};
        animationTimer = new WorkAnimationTimer();
    }

    private class WorkAnimationTimer extends AnimationTimer {
        private long FRAMES_PER_SEC = 20;
        private long INTERVAL = 1000000000L / FRAMES_PER_SEC;
        private long last = 0;

        @Override
        public void handle(long now) {
            if (now - last > INTERVAL) {
                buttonGrow();
                last = now;
            }
        }
    }

    // Changes the height of the Rectangles on the graph
    public void buttonGrow() {
        try {
            if (!numbers.isEmpty()) {
                int intNum = numbers.get(numbers.size() - 1);
                int buttonNum = intNum / 2;
                int targetHeight = 200;
                int targetCount = 20;
                buttonHeight[buttonNum] += 20;
                button[buttonNum].setHeight(buttonHeight[buttonNum]);
                buttonCount[buttonNum] += 1;
                if(buttonNum < targetHeight) {
                    if (buttonHeight[buttonNum] >= targetCount) {

                        animationTimer.stop();
                    }
                    targetCount += 20;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("");
        }
    }

    // Average
    @FXML
    private void Calculations() {
        for (int i = 0; i < numbers.size(); i++) {
            Average();
            Frequency();
            Mode();
        }
    }

    // Calculates Average
    private void Average() {
        Integer sum = 0;
        if (!numbers.isEmpty()) {
            for (Integer number : numbers) {
                sum += number;
            }
            Double average = Double.valueOf(sum) / numbers.size();
            mean.setText(String.format(Double.toString(average)));
        } else {
            mean.setText(String.format(Integer.toString(sum)));
        }
    }

    // Calculates Frequency
    private void Frequency() {
        int total = numbers.size();
        median.setText(String.format(Integer.toString(total)));
    }

    // Calculates Mode
    private void Mode() {
        String[] but = new String[]{"(1,2)", "(3,4)", "(5,6)", "(7,8)", "(9,10)"};

        int most = buttonCount[0];
        int mostI = 0;
        for (int i = 1; i < buttonCount.length; i++) {
            if (buttonCount[i] > most) {
                most = buttonCount[i];
                mostI = i;
            }
        }
        mode.setText(but[mostI]);
    }

    // Resets the GUI
    @FXML
    public void reset() {
        animationTimer.stop();
        numbers.clear();
        mean.setText(" ");
        median.setText(" ");
        mode.setText(" ");
        data.setText(" ");

        for (int i = 0; i < button.length; i++) {
            buttonHeight[i] = 12;
            buttonCount[i] = 0;
            button[i].setHeight(buttonHeight[i]);
        }
    }

    // Submits to add ints to arrayList
    @FXML
    public void submits() {
        String num = enterInt.getText();
        int intNum = Integer.parseInt(num);
        numbers.add(intNum);
        String list = numbers.toString();
        enterInt.setText("");
        data.setText(list);
        // Start the animation timer when the Submit button is clicked
        animationTimer.start();
    }
}