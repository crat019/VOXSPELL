package MainSource;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {
    private Stage mainWindow;
    private Scene mainScene;//background scene of primary window
    private Scene menuScene;//scene containing the menu
    private Scene gameScene;//scene initiating play
    private Scene statsScene;//scene at bottom of window

    //Buttons
    private Button newGameButton;
    private Button reviewGameButton;
    private Button statisticsButton;

    private int level = 1;//default level 1

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
        GridPane gameSceneLayout = setGameScene();//sets the right-hand side main

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

    /**
     * Logic for closing program. Used to save history before qutting game.
     */
    private void closeProgram(){
        ConfirmQuitBox quitBox = new ConfirmQuitBox();
        Boolean answer = quitBox.display("Quit VOXSPELL", "Are you sure you want to exit?");
        if (answer){
            mainWindow.close();
        }
    }

    /**
     * Logic for setting up the left-side menu scene (specific for main entry menu only).
     * @return menu vbox layout
     */
    private VBox setMenuScene(){
        VBox menuSceneLayout = new VBox();
        menuSceneLayout.setPrefWidth(150);//set width of menu buttons
        //http://docs.oracle.com/javafx/2/ui_controls/button.htm
        newGameButton = createMenuButtons("ImageResources/newGame.png", "New Game");
        reviewGameButton = createMenuButtons("ImageResources/newGame.png", "Review Game");
        statisticsButton = createMenuButtons("ImageResources/newGame.png", "Statistics");

        menuSceneLayout.setPadding(new Insets(20));//insets: top right bottom left
        menuSceneLayout.getChildren().addAll(newGameButton, reviewGameButton, statisticsButton);
        menuSceneLayout.getStyleClass().add("vbox");//add the custom vbox layout style


        return menuSceneLayout;
    }

    /**
     * Create menu buttons for the menu scene
     * @param imageName image filepath
     * @param caption button caption
     * @return button node
     */
    private Button createMenuButtons(String imageName, String caption){
        Image newGameIcon = new Image(imageName, 120, 120, false, true);//size of image
        Button newButton = new Button(caption, new ImageView(newGameIcon));
        newButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");
        newButton.setContentDisplay(ContentDisplay.TOP);
        return newButton;
    }

    /**
     * Logic for the main game scene of the main entry window
     * thinking of reusing for settings popup window
     * @return main game scene as a gridPane
     */
    //we may want to reuse this for settings page
    private GridPane setGameScene(){
        GridPane gameGrid = new GridPane();
        gameGrid.setPadding(new Insets(20,20,20,50));
        gameGrid.setVgap(40);
        gameGrid.setHgap(10);

        Label levelLabel = new Label("Level");
        GridPane.setConstraints(levelLabel, 0, 0);

        /*
        //set up combo box for choosing levels
        ComboBox levelOptionCombo = new ComboBox<String>();
        levelOptionCombo.getItems().addAll(
            "Level 1", "Level 2", "Level 3", "Level 4",
                "Level 5", "Level 6", "Level 7", "Level 8",
                "Level 9"
        );
        GridPane.setConstraints(levelOptionCombo, 1, 0);
        */
        ToggleGroup levelToggles = setLevelButtons(9, gameGrid);

        Label voiceLabel = new Label("Voice");
        GridPane.setConstraints(voiceLabel, 0, 1);

        //set up combo box for choosing levels
        ComboBox voiceOptionCombo = new ComboBox<String>();
        voiceOptionCombo.getItems().addAll(
                "Voice 1", "Voice 2", "Voice 3", "Voice 4"
        );
        GridPane.setConstraints(voiceOptionCombo, 1, 1);

        //gameGrid.getChildren().addAll(levelLabel, levelOptionCombo, voiceLabel, voiceOptionCombo);
        gameGrid.getChildren().addAll(levelLabel, voiceLabel, voiceOptionCombo);
        Button playButton = new Button("PLAY");
        playButton.setStyle("-fx-font: 32 arial; -fx-base: #b6e7c9;");
        GridPane.setConstraints(playButton, 0, 3);
        gameGrid.getChildren().add(playButton);
        return gameGrid;

    }

    private ToggleGroup setLevelButtons(int maxLevel, GridPane gameGrid){
        HBox levelHBox = new HBox();
        ToggleGroup levelGroup = new ToggleGroup();
        for (int i = 1; i <maxLevel+1 ; i++){
            ToggleButton levelButton = new ToggleButton("" + i);
            levelButton.setUserData(i);

            if (i ==1){
                levelButton.setSelected(true);
            }
            //disable if user has not achieved desired level#TODO

            levelButton.setToggleGroup(levelGroup);
            levelHBox.getChildren().add(levelButton);

        }
        levelGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue==null){
                    level=1;//default level
                } else {
                    level = (int) levelGroup.getSelectedToggle().getUserData();//set level via button select
                }
            }
        });
        GridPane.setConstraints(levelHBox, 1, 0);
        gameGrid.getChildren().add(levelHBox);
        return levelGroup;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
