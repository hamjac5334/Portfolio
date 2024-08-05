package measuresCT;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.util.ArrayList;

public class measuresCTController {
    @FXML
    public TextField enterInt;
    @FXML
    private TextField mean;
    @FXML
    private TextField median;

    @FXML
    private TextField data;
    @FXML
    private TextField std;
    @FXML
    private TextField variances;

    //creates ArrayList
    ArrayList<Integer> numbers = new ArrayList<>();

    //Initializes the program
    @FXML
    public void initialize() {
        enterInt.setEditable(true);
        data.setEditable(false);
        mean.setEditable(false);
        median.setEditable(false);
        variances.setEditable(false);
        std.setEditable(false);
    }

    //Submits to add ints to arrayList
    @FXML
    public void submits() {
        String num = enterInt.getText();
        int intNum = Integer.parseInt(num);
        numbers.add(intNum);
        String list = numbers.toString();
        for (int i = 0; i < numbers.size(); i++) {
            enterInt.setText("");
            data.setText(list);
        }
    }

    // Average
    @FXML
    private void Calculations() {
        for (int i = 0; i < numbers.size(); i++) {
            Average();
            Frequency();
            variance();
            std();
        }
    }
    //Calculates Average

    private void Average() {
        Integer sum = 0;
        for (Integer number : numbers) {
            sum += number;
        }
        double average = Double.valueOf(sum) / numbers.size();
        if (!numbers.isEmpty()) {
            mean.setText(String.format(Double.toString(average)));
        } else {
            mean.setText(String.format(Integer.toString(sum)));
        }
    }

    //Calculates Frequency
    private void Frequency() {
        int total = numbers.size();
        median.setText(String.format(Integer.toString(total)));
    }

    //Calculates Variance
    private void variance() {
        int sum = 0;

        // Calculate the mean (average)
        for (Integer number : numbers) {
            sum += number;
        }
        double average = (double) sum / numbers.size();

        double sumSquaredDifferences = 0;
        for (Integer number : numbers) {
            sumSquaredDifferences += Math.pow(number - average, 2);
        }

        // Calculate the variance
        double variance = sumSquaredDifferences / numbers.size();

        //variance.setText(String.format(Double.toString(variance)));
        variances.setText(String.format(Double.toString(variance)));

    }

    //Calculates std
    private void std() {
        int sum = 0;

        // Calculate the mean (average)
        for (Integer number : numbers) {
            sum += number;
        }
        double average = (double) sum / numbers.size();

        double sumSquaredDifferences = 0;
        for (Integer number : numbers) {
            sumSquaredDifferences += Math.pow(number - average, 2);
        }

        // Calculate the variance
        double variance = sumSquaredDifferences / numbers.size();
        double deviation = Math.sqrt(variance);

        // Calculates std
        std.setText(String.format(Double.toString(deviation)));
    }

    //Resets the GUI
    @FXML
    public void reset() {
        numbers.clear();
        mean.setText(" ");
        median.setText(" ");
        data.setText(" ");
        std.setText(" ");
        variances.setText(" ");

    }
}

