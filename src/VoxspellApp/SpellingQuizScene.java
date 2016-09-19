package VoxspellApp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import models.Festival;
import models.SpellingQuiz;
import models.Status;
import models.WordModel;

import java.util.ArrayList;

/**
 * Created by ratterz on 16/09/16.
 */
public class SpellingQuizScene {

    private SpellingQuiz _quiz = new SpellingQuiz();
    private WordModel _wordModel;

    //SCENE
    private Stage _window;
    private Scene _mainScene;

    //PANES
    private VBox _mainLayout = new VBox();
    private HBox _textArea = new HBox();
    private HBox _statusArea = new HBox();
    private HBox _buttonArea = new HBox();
    private HBox _resultsArea = new HBox();

    //CONGRATS PANE
    private HBox _congratsStatusArea = new HBox();

    //TEXT
    private TextField _inputText = new TextField();
    private Label _levelTitle = new Label();
    private Label _congratsTitle = new Label();
    private Label _voice = new Label();

    //BUTTONS
    private Button _submitButton = new Button("Submit");
    private Button _startQuizButton = new Button("Start Quiz");
    private Button _settingsButton = new Button("Settings");
    private Button _repeatButton = new Button("Repeat");
    private Button _definitionButton = new Button("Definition");
    private Button _videoButton  = new Button("Video");
    private Button _stayButton = new Button("Stay");
    private Button _nextLevelButton = new Button("Next Level");
    private Button _mainMenu = new Button("Main Menu");

    //STORAGE
    private ArrayList<Circle> _circleList = new ArrayList<Circle>();
    private int _position;
    private int _numberMastered;

    /**
     * This is the constructor for the spelling quiz scene. This will call the set up gui method
     * so the gui is set up and also it will call a method to start a new spelling quiz. The wordModel
     * will be passed onto the new spelling quiz.
     * @param wordModel
     */
    public SpellingQuizScene(WordModel wordModel, Stage window) {

        this._wordModel = wordModel;
        this._window = window;
        setUpGui();
        setUpEventHandelers();
    }

    private void setUpGui() {
        setUpStatusArea();
        setUpTextArea();
        setUpButtonArea();
        setUpResultsArea();
        _mainLayout.setPadding(new Insets(20));
        _mainLayout.getChildren().addAll(_statusArea,_resultsArea,_buttonArea,_textArea);
        BackgroundImage menuBackground = new BackgroundImage(new Image("MediaResources/background.png", 1040, 640, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        _mainLayout.setBackground(new Background(menuBackground));

        _mainScene = new Scene(_mainLayout, 1040, 640);
        _mainScene.getStylesheets().add("VoxspellApp/LayoutStyles");
    }

    private void setUpTextArea() {
        _textArea.setSpacing(20);
        _textArea.setPadding(new Insets(20));

        _inputText.setMinWidth(840);
        _inputText.setMinHeight(50);
        _inputText.setText("Press Start Quiz To Start Your Quiz!!");
        _inputText.setStyle("-fx-font: 18 arial");
        _inputText.setDisable(true);

        _submitButton.setMinWidth(100);
        _submitButton.setMinHeight(50);
        _submitButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");
        _submitButton.setDisable(true);

        _textArea.getChildren().addAll(_inputText,_submitButton);
    }

    private void setUpStatusArea() {
        _statusArea.setSpacing(50);
        _statusArea.setPadding(new Insets(20));
        _statusArea.setAlignment(Pos.CENTER);

        _startQuizButton.setMinWidth(150);
        _startQuizButton.setMinHeight(50);
        _startQuizButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");

        _levelTitle.setText("Level: " + _wordModel.getCurrentLevel());
        _levelTitle.setStyle("-fx-font: 40 arial; -fx-base: #b6e7c9;");

        _voice.setText("Voice");
        _voice.setStyle("-fx-font: 40 arial; -fx-base: #b6e7c9;");

        _statusArea.getChildren().addAll(_startQuizButton,_levelTitle,_voice);
    }

    private void setUpButtonArea() {
        _buttonArea.setSpacing(100);
        _buttonArea.setPadding(new Insets(50,200,50,175));
        _buttonArea.setPrefHeight(200);

        _settingsButton.setMinWidth(150);
        _settingsButton.setMinHeight(150);
        _settingsButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");

        _definitionButton.setMinWidth(150);
        _definitionButton.setMinHeight(150);
        _definitionButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");
        _definitionButton.setDisable(true);

        _repeatButton.setMinWidth(150);
        _repeatButton.setMinHeight(150);
        _repeatButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");
        _repeatButton.setDisable(true);


        _buttonArea.getChildren().addAll(_repeatButton,_definitionButton,_settingsButton);
    }

    private void setUpResultsArea() {
        _resultsArea.setSpacing(20);
        _resultsArea.setPadding(new Insets(50));
        _resultsArea.setPrefHeight(100);
        _resultsArea.setAlignment(Pos.CENTER);

        createCircles();

        _resultsArea.getChildren().addAll(_circleList);


    }

    private void createCircles() {
        for (int i = 0; i < Voxspell.COUNT; i++) {
            Circle circle = new Circle(20);
            circle.setStyle("-fx-fill: #c2c2c2;");
            _circleList.add(circle);
        }
    }

    private void updateCircle(Status status) {
        if (status.equals(Status.Mastered)) {
            _circleList.get(_position).setStyle("-fx-fill: rgb(90,175,90);");
            _numberMastered++;
            _position++;
        } else if (status.equals(Status.Faulted)) {
            _circleList.get(_position).setStyle("-fx-fill: rgb(230,160,40);");
            _position++;
        } else if (status.equals(Status.Failed)) {
            _circleList.get(_position).setStyle("-fx-fill: rgb(225,100,50);");
            _position++;
        }
    }

    private void reset() {
        for (Circle circle : _circleList) {
            circle.setStyle("-fx-fill: #c2c2c2;");
        }

        _position = 0;
        _startQuizButton.setDisable(false);
        _definitionButton.setDisable(true);
        _repeatButton.setDisable(true);
        _inputText.setDisable(true);
        _submitButton.setDisable(true);
        _levelTitle.setText("Level: " + _wordModel.getCurrentLevel());
    }

    private void setUpRewardGui() {
        _wordModel.levelUp();

        _mainLayout.getChildren().removeAll(_statusArea,_resultsArea,_buttonArea,_textArea);
        _mainLayout.setAlignment(Pos.CENTER);
        _mainLayout.setSpacing(20);

        _congratsStatusArea.setSpacing(50);
        _congratsStatusArea.setPadding(new Insets(20));
        _congratsStatusArea.setAlignment(Pos.CENTER);

        _congratsTitle.setText("Congrats!! You Passed Level: " + _wordModel.getCurrentLevel());
        _congratsTitle.setStyle("-fx-font: 40 arial; -fx-base: #b6e7c9;");

        _congratsStatusArea.getChildren().removeAll(_congratsTitle);
        _congratsStatusArea.getChildren().addAll(_congratsTitle);

        _videoButton.setMinWidth(300);
        _videoButton.setMinHeight(150);
        _videoButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");

        _nextLevelButton.setMinWidth(300);
        _nextLevelButton.setMinHeight(25);
        _nextLevelButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");
        if (_wordModel.getCurrentLevel() >= _wordModel.getNumberOfLevels()) {
            _nextLevelButton.setDisable(true);
        }

        _stayButton.setMinWidth(300);
        _stayButton.setMinHeight(25);
        _stayButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");

        _mainMenu.setMinWidth(300);
        _mainMenu.setMinHeight(25);
        _mainMenu.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");

        _mainLayout.getChildren().addAll(_congratsStatusArea,_resultsArea,_videoButton,_nextLevelButton,_stayButton,_mainMenu);
    }

    private void setUpFailedGui() {
        _mainLayout.getChildren().removeAll(_statusArea,_resultsArea,_buttonArea,_textArea);
        _mainLayout.setAlignment(Pos.CENTER);
        _mainLayout.setSpacing(20);

        _congratsStatusArea.setSpacing(50);
        _congratsStatusArea.setPadding(new Insets(20));
        _congratsStatusArea.setAlignment(Pos.CENTER);

        _congratsTitle.setText("Please Try Again!! You Didn't Pass Level: " + _wordModel.getCurrentLevel());
        _congratsTitle.setStyle("-fx-font: 40 arial; -fx-base: #b6e7c9;");

        _congratsStatusArea.getChildren().removeAll(_congratsTitle);
        _congratsStatusArea.getChildren().addAll(_congratsTitle);

        _stayButton.setMinWidth(300);
        _stayButton.setMinHeight(25);
        _stayButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");

        _mainMenu.setMinWidth(300);
        _mainMenu.setMinHeight(25);
        _mainMenu.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");

        _mainLayout.getChildren().addAll(_congratsStatusArea,_resultsArea,_stayButton,_mainMenu);
    }

    private void submitHandler() {
        String text = _inputText.getText();
        _inputText.clear();
        _quiz.spellingLogic(text);
        updateCircle(_quiz.getStatus());
        isFinished();
    }

    private void isFinished() {
        if (_quiz.getFinishedStatus()) {
            _repeatButton.setDisable(true);
            _definitionButton.setDisable(true);
            _submitButton.setDisable(true);
            _inputText.setDisable(true);
            _wordModel.StatsAccessibleOn();//turn on access to statistics for this level
            if ((double)_numberMastered/Voxspell.COUNT >= 0.9) {
                setUpRewardGui();
            } else {
                setUpFailedGui();
            }
        }
    }

    private void setUpEventHandelers() {
        _inputText.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().toString().equals("ENTER")) {
                    //Festival.stopFestivalTTS();
                    submitHandler();
                }
            }
        });

        _submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Festival.stopFestivalTTS();
                submitHandler();
            }
        });

        _startQuizButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _position = 0;
                _numberMastered = 0;
                _startQuizButton.setDisable(true);
                _inputText.setDisable(false);
                _repeatButton.setDisable(false);
                _definitionButton.setDisable(false);
                _inputText.clear();
                _submitButton.setDisable(false);
                _quiz.setUpSpellingQuiz(_wordModel);
            }
        });

        _repeatButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _quiz.repeatWord();
            }
        });

        _stayButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _mainLayout.getChildren().removeAll(_congratsStatusArea,_resultsArea,_videoButton,_nextLevelButton,_stayButton,_mainMenu);
                reset();
                _mainLayout.getChildren().addAll(_statusArea,_resultsArea,_buttonArea,_textArea);
            }
        });

        _nextLevelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _mainLayout.getChildren().removeAll(_congratsStatusArea,_resultsArea,_videoButton,_nextLevelButton,_stayButton,_mainMenu);
                _wordModel.updateLevel(_wordModel.getCurrentLevel()+1);
                reset();
                _mainLayout.getChildren().addAll(_statusArea,_resultsArea,_buttonArea,_textArea);
            }
        });

        _mainMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Switch To Main Menu Scene
                InitialScene mainMenu = new InitialScene(_window, _wordModel);
                _window.setScene(mainMenu.createScene());
            }
        });

        //TODO worker thread here
        _videoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                VideoPlayer video = new VideoPlayer();
                video.display();
            }
        });
    }

    public Scene createScene() {
        return this._mainScene;
    }

}
