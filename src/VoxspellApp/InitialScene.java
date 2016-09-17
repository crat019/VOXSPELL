package VoxspellApp;


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
import models.Word;
import models.WordModel;

/**
 * Created by edson on 15/09/16.
 *
 * responsible for creating and handling interaction with the initial window of the game.
 */
public class InitialScene {
    private Scene _mainScene;//background scene of primary window
    private Scene menuScene;//scene containing the menu
    private Scene gameScene;//scene initiating play
    private Scene statsScene;//scene at bottom of window
    private BorderPane _mainLayout;

    //Buttons
    private Button _newGameButton;
    private Button _reviewGameButton;
    private Button _statisticsButton;

    private int _level;
    private Mode _mode = Mode.NEW;
    protected enum Mode{NEW, REVIEW};

    private WordModel _model;

    public InitialScene(int level, WordModel model){
        _model = model;
        _level = level;

        VBox menuSceneLayout = setMenuScene();//sets the left-hand side menu panel
        GridPane gameSceneLayout = setGameScene();//sets the right-hand side main

        //set all scenes into the main scene
        _mainLayout = new BorderPane();
        _mainLayout.setLeft(menuSceneLayout);
        _mainLayout.setCenter(gameSceneLayout);
        //mainLayout.setRight()

        BackgroundImage menuBackground = new BackgroundImage(new Image("ImageResources/background.png", 1040, 640, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        _mainLayout.setBackground(new Background(menuBackground));

        _mainScene = new Scene(_mainLayout, 1040, 640);
        _mainScene.getStylesheets().add("VoxspellApp/LayoutStyles");//add the css style-sheet to the main menu scene

        setupEventHandlers();

    }

    protected Scene createScene(){
        return _mainScene;
    }
    /**
     * Logic for setting up the left-side menu scene (specific for main entry menu only).
     * @return menu vbox layout
     */
    private VBox setMenuScene(){
        VBox menuSceneLayout = new VBox();
        menuSceneLayout.setPrefWidth(150);//set width of menu buttons
        //http://docs.oracle.com/javafx/2/ui_controls/button.htm
        _newGameButton = createMenuButtons("ImageResources/newGame.png", "New Game");
        _reviewGameButton = createMenuButtons("ImageResources/newGame.png", "Review Game");
        _statisticsButton = createMenuButtons("ImageResources/newGame.png", "Statistics");

        menuSceneLayout.setPadding(new Insets(20));//insets: top right bottom left
        menuSceneLayout.getChildren().addAll(_newGameButton, _reviewGameButton, _statisticsButton);
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
        levelLabel.setStyle("-fx-font: 22 arial;");
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
        ToggleGroup levelToggles = setLevelButtons(Voxspell.COUNT, gameGrid);

        Label voiceLabel = new Label("Voice");
        voiceLabel.setStyle("-fx-font: 22 arial;");
        GridPane.setConstraints(voiceLabel, 0, 1);

        //set up combo box for choosing levels
        ComboBox voiceOptionCombo = new ComboBox<String>();
        voiceOptionCombo.getItems().addAll(
                "Voice 1", "Voice 2", "Voice 3", "Voice 4"
        );
        voiceOptionCombo.setStyle("-fx-font: 22 arial;");
        GridPane.setConstraints(voiceOptionCombo, 1, 1);


        gameGrid.getChildren().addAll(levelLabel, voiceLabel, voiceOptionCombo);
        Button playButton = new Button("PLAY");
        playButton.setOnAction(e->{
            //TODO set to new game


        });
        playButton.setStyle("-fx-font: 32 arial; -fx-base: #b6e7c9;");

        GridPane.setConstraints(playButton, 0, 3);
        gameGrid.getChildren().add(playButton);
        return gameGrid;

    }

    private ToggleGroup setLevelButtons(int maxLevel, GridPane gameGrid){
        HBox levelHBox = new HBox();
        levelHBox.setSpacing(5);
        ToggleGroup levelGroup = new ToggleGroup();
        for (int i = 1; i <maxLevel+1 ; i++){
            ToggleButton levelButton = new ToggleButton("" + i);
            levelButton.setUserData(i);
            levelButton.setStyle("-fx-font: 22 arial;");
            levelButton.setOnAction(e->{
                _level = Integer.parseInt(levelButton.getText());
            });

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
                    _level=1;//default level
                } else {
                    _level = (int) levelGroup.getSelectedToggle().getUserData();//set level via button select
                }
            }
        });
        GridPane.setConstraints(levelHBox, 1, 0);
        gameGrid.getChildren().add(levelHBox);
        return levelGroup;
    }

    private void setupEventHandlers(){
        _newGameButton.setOnAction(event -> {
            _mainLayout.setCenter(setGameScene());
            _mode=Mode.NEW;
        });
        _reviewGameButton.setOnAction(event -> {
            _mainLayout.setCenter(setGameScene());
            _mode=Mode.REVIEW;
        });
        _statisticsButton.setOnAction(event -> {
            StatisticsScene graphScene = new StatisticsScene(_model);
            _mainLayout.setCenter(graphScene.createScene());//set center pane to the StatisticsScene's layout node
        });
    }




}
