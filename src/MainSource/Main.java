package MainSource;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.control.Button;

import java.awt.*;

public class Main extends Application {
    Stage mainWindow;
    Scene mainScene;//background scene of primary window
    Scene menuScene;//scene containing the menu
    Scene gameScene;//scene initiating play
    Scene statsScene;//scene at bottom of window

    //Buttons
    Button newGameButton;
    Button reviewGameButton;
    Button statisticsButton;

    Button playButton;

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainWindow = primaryStage;
        mainWindow.setTitle("VOXSPELL");
        mainWindow.setOnCloseRequest(e -> {
            e.consume();//suppress user request
            closeProgram();//replace with our own close implementation
        });


        VBox menuSceneLayout = setMenuScene();//sets the left-hand side menu panel
        GridPane gameSceneLayout = setGameScene();


        Button playButton = new Button("PLAY");
        playButton.setStyle("-fx-font: 32 arial; -fx-base: #b6e7c9;");
        GridPane.setConstraints(playButton, 0, 3);
        gameSceneLayout.getChildren().add(playButton);


        //set all scenes into the main scene
        BorderPane mainLayout = new BorderPane();
        mainLayout.setLeft(menuSceneLayout);
        mainLayout.setCenter(gameSceneLayout);
        //mainLayout.setRight()

        BackgroundImage menuBackground = new BackgroundImage(new Image("ImageResources/background.png", 1040, 640, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        mainLayout.setBackground(new Background(menuBackground));

        mainScene = new Scene(mainLayout, 1040, 640);
        mainScene.getStylesheets().add("MainSource/LayoutStyles");//add the css style-sheet to the main menu scene
        mainWindow.setScene(mainScene);
        mainWindow.show();


    }

    private void closeProgram(){
        ConfirmQuitBox quitBox = new ConfirmQuitBox();
        Boolean answer = quitBox.display("Quit VOXSPELL", "Are you sure you want to exit?");
        if (answer){
            mainWindow.close();
        }
    }

    private VBox setMenuScene(){
        VBox menuSceneLayout = new VBox();
        menuSceneLayout.setPrefWidth(200);//set width of menu buttons
        //http://docs.oracle.com/javafx/2/ui_controls/button.htm
        Image newGameIcon = new Image("ImageResources/newGame.png", 40, 40, false, true);
        newGameButton = new Button("New Game", new ImageView(newGameIcon));
        newGameButton.setMinWidth(menuSceneLayout.getPrefWidth());
        newGameButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");
        newGameButton.setContentDisplay(ContentDisplay.TOP);//set image to above text

        Image reviewGameIcon = new Image("ImageResources/newGame.png", 40, 40, false, true);
        reviewGameButton = new Button("Review Game", new ImageView(reviewGameIcon));
        reviewGameButton.setMinWidth(menuSceneLayout.getPrefWidth());
        reviewGameButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");

        Image statisticsIcon = new Image("ImageResources/newGame.png", 40, 40, false, true);
        statisticsButton = new Button("Statistics", new ImageView(statisticsIcon));
        statisticsButton.setMinWidth(menuSceneLayout.getPrefWidth());
        statisticsButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");

        menuSceneLayout.setPadding(new Insets(20));//insets: top right bottom left
        menuSceneLayout.getChildren().addAll(newGameButton, reviewGameButton, statisticsButton);
        menuSceneLayout.getStyleClass().add("vbox");//add the custom vbox layout style


        return menuSceneLayout;
    }

    //we may want to reuse this for settings page
    private GridPane setGameScene(){
        GridPane gameGrid = new GridPane();
        gameGrid.setPadding(new Insets(20,20,20,50));
        gameGrid.setVgap(40);
        gameGrid.setHgap(10);

        Label levelLabel = new Label("Level");
        GridPane.setConstraints(levelLabel, 0, 0);

        //set up combo box for choosing levels
        ComboBox levelOptionCombo = new ComboBox<String>();
        levelOptionCombo.getItems().addAll(
            "Level 1", "Level 2", "Level 3", "Level 4",
                "Level 5", "Level 6", "Level 7", "Level 8",
                "Level 9"
        );
        GridPane.setConstraints(levelOptionCombo, 1, 0);

        Label voiceLabel = new Label("Voice");
        GridPane.setConstraints(voiceLabel, 0, 1);

        //set up combo box for choosing levels
        ComboBox voiceOptionCombo = new ComboBox<String>();
        voiceOptionCombo.getItems().addAll(
                "Voice 1", "Voice 2", "Voice 3", "Voice 4"
        );
        GridPane.setConstraints(voiceOptionCombo, 1, 1);

        gameGrid.getChildren().addAll(levelLabel, levelOptionCombo, voiceLabel, voiceOptionCombo);
        return gameGrid;


    }


    public static void main(String[] args) {
        launch(args);
    }
}
