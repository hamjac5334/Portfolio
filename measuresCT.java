package measuresCT;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class measuresCT extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            Pane root =
                    (Pane)loader.load(getClass().getResource("measuresCTGUI.fxml").openStream());
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            root.requestFocus();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }


    }
    public static void main(String[] args) {
        launch(args);
    }
}
