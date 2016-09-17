package VoxspellApp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import models.SpellingQuiz;
import models.WordModel;

/**
 * Created by ratterz on 16/09/16.
 */
public class SpellingQuizScene {

    private SpellingQuiz _quiz = new SpellingQuiz();
    private WordModel _wordModel;

    //SCENE
    private Scene _mainScene;

    //PANES
    private GridPane _centerLayout = new GridPane();
    private BorderPane _mainLayout = new BorderPane();
    private VBox _leftLayout = new VBox();

    //TEXT
    private TextField _inputText = new TextField();

    //BUTTONS
    private Button _submitButton = new Button("Submit");
    private Button _startQuizButton = new Button("Start Quiz");
    private Button _settingsButton = new Button("Settings");

    /**
     * This is the constructor for the spelling quiz scene. This will call the set up gui method
     * so the gui is set up and also it will call a method to start a new spelling quiz. The wordModel
     * will be passed onto the new spelling quiz.
     * @param wordModel
     */
    public SpellingQuizScene(WordModel wordModel) {
        setUpGui();
        setUpEventHandelers();
        this._wordModel = wordModel;
    }

    private void setUpGui() {
        setUpCenterLayout();
        setUpLeftLayout();
        _mainLayout.setCenter(_centerLayout);
        _mainLayout.setLeft(_leftLayout);

        _mainScene = new Scene(_mainLayout, 1040, 640);
        _mainScene.getStylesheets().add("VoxspellApp/LayoutStyles");
    }

    private void setUpCenterLayout() {
        _centerLayout.setPadding(new Insets(10));
        _centerLayout.setVgap(40);
        _centerLayout.setHgap(10);

        _inputText.setMinWidth(600);
        _inputText.setMinHeight(50);
        _inputText.setText("Press Start Quiz To Start Your Quiz!!");
        _inputText.setStyle("-fx-font: 18 arial");
        _inputText.setDisable(true);

        _submitButton.setMinWidth(100);
        _submitButton.setMinHeight(50);
        _submitButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");
        _submitButton.setDisable(true);

        GridPane.setConstraints(_inputText,10,11);
        GridPane.setConstraints(_submitButton,11,11);

        _centerLayout.getChildren().addAll(_inputText,_submitButton);
    }

    private void setUpLeftLayout() {
        _leftLayout.setPrefWidth(150);
        _leftLayout.setPadding(new Insets(20));

        _startQuizButton.setMinWidth(120);
        _startQuizButton.setMinHeight(50);
        _startQuizButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");

        _settingsButton.setMinWidth(120);
        _settingsButton.setMinHeight(50);
        _settingsButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");

        _leftLayout.getChildren().addAll(_startQuizButton,_settingsButton);
        _leftLayout.getStyleClass().add("vbox");
    }

    private void setUpEventHandelers() {
        _inputText.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().toString().equals("ENTER")) {
                    String text = _inputText.getText();
                    _inputText.clear();
                    _quiz.spellingLogic(text);
                }
            }
        });

        _submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String text = _inputText.getText();
                _inputText.clear();
                _quiz.spellingLogic(text);
            }
        });

        _startQuizButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _startQuizButton.setDisable(true);
                _inputText.setDisable(false);
                _inputText.clear();
                _submitButton.setDisable(false);
                _quiz.setUpSpellingQuiz(_wordModel);
            }
        });
    }

    public Scene createScene() {
        return this._mainScene;
    }

}
