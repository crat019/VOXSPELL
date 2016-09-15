package VoxspellApp;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
//import models.GraphFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edson on 15/09/16.
 */

public class StatisticsScene {
    Scene _statsScene;
    ScrollPane _bgLayout;
    VBox _mainLayout;

    public StatisticsScene(){
        //List barGraphs = GraphFactory.createBarGraphs();
        //List pieGraphs = GraphFactory.createPieGraphs();

        //iterate through list of graphs to display by adding to vbox



        _bgLayout = new ScrollPane();
        _mainLayout = new VBox();
        _mainLayout.setAlignment(Pos.CENTER);



    }
}
