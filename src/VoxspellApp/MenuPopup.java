package VoxspellApp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by edson on 21/09/16.
 */
public class MenuPopup {

    Stage _window;
    VBox _layout;

    //buttons
    Button _voiceButton;
    Button _statsButton;
    Button _mainMenuButton;
    Button _exitGame;
    Button _backButton;

    MenuStatus _menuStatus;

    public MenuPopup() {

        _window = new Stage();
        _window.initModality(Modality.APPLICATION_MODAL);//modality for suppressing main window
        _window.setTitle("Menu");
        _window.setMinWidth(125);
        _window.setResizable(false);

        _layout = new VBox(5);
        _layout.setAlignment(Pos.CENTER);
        _layout.setPadding(new Insets(10,10,10,10));
        _voiceButton = createButtons("Change Voice");
        _statsButton = createButtons("Show Statistics");
        _mainMenuButton = createButtons("Main Menu");
        _exitGame = createButtons("Exit Game");
        _backButton = createButtons("Back");

        setupEventHandlers();



    }

    public MenuStatus display(){
        Scene scene = new Scene(_layout);
        _window.setScene(scene);
        _window.showAndWait();
        return _menuStatus;
    }


    private Button createButtons(String caption){
        Button button = new Button(caption);
        _layout.getChildren().add(button);
        button.setMinWidth(110);
        return button;
    }

    private void setupEventHandlers(){
        _voiceButton.setOnAction(e->{
            _menuStatus = MenuStatus.VOICE;
            _window.close();
        });
        _statsButton.setOnAction(e->{
            _menuStatus = MenuStatus.STATS;
            _window.close();

        });
        _mainMenuButton.setOnAction(e->{
            _menuStatus = MenuStatus.MAIN;
            _window.close();
        });
        _exitGame.setOnAction(e->{
            _menuStatus = MenuStatus.EXIT;
            _window.close();
        });
    }

}
