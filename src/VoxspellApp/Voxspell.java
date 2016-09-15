package VoxspellApp;

import com.sun.corba.se.impl.presentation.rmi.IDLNameTranslatorImpl;
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
import javafx.stage.Stage;

/**
 * Main program for the Voxspell Spelling aid game. An instance of the Voxspell
 * sets up the gui comprising of a menu scene set on the left hand side and the
 * main scene on the right hand side. This main scene changes depending on the
 * menu option that they choose.
 */

public class Voxspell extends Application {


    //GUI COMPONENTS
    private Stage _mainWindow;
    private InitialScene _initialScene;


    private int level = 1;//default level 1

    Button playButton;

    @Override
    public void start(Stage primaryStage) throws Exception{
        _mainWindow = primaryStage;
        _mainWindow.setTitle("VOXSPELL");
        _mainWindow.setOnCloseRequest(e -> {
            e.consume();//suppress user request
            closeProgram();//replace with our own close implementation
        });

        _initialScene = new InitialScene(level);



        _mainWindow.setScene(_initialScene.createScene());
        _mainWindow.show();

    }

    /**
     * Logic for closing program. Used to save history before qutting game.
     */
    private void closeProgram(){
        ConfirmQuitBox quitBox = new ConfirmQuitBox();
        Boolean answer = quitBox.display("Quit VOXSPELL", "Are you sure you want to exit?");
        if (answer){
            _mainWindow.close();
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
