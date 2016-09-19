package VoxspellApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import models.Level;
import models.Status;
import models.Word;
import models.WordModel;
//import models.GraphFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by edson on 15/09/16.
 */

public class StatisticsScene {
    private Scene _statsScene;
    private BorderPane _bgLayout;
    private WordModel _model;
    private ScrollPane _graphSceneLayout;
    private int _bargraphHeight;

    private int _level;


    public StatisticsScene(WordModel model){

        _model = model;

    }

    public Node createScene(){

        _model.updateStatistics();//updates the statistics of the WordModel

        VBox optionLayout = new VBox(20);
        optionLayout.setPrefWidth(200);//set menu width
        Hyperlink link = new Hyperlink("Overview");
        link.setOnAction(e->{//set graphScene to the overall statistics setting
            _bgLayout.setCenter(_graphSceneLayout);
        });
        optionLayout.getChildren().add(link);
        //iterate through levels
        for(int i = 1; i<_model.getTotalLevels()+1; i++){
            final int level = i;
            link = new Hyperlink("Level "+(i));
            link.setOnAction(e-> {//change the graphScene
                VBox graphLayout = new VBox(80);
                if (!_model.isStatsAccessible(level-1)) {
                    Label accessDeniedLabel = new Label("You have not reached this level yet.");
                    graphLayout.getChildren().add(accessDeniedLabel);
                } else {
                    PieChart levelPie = createPie("Level " + (level) + " Accuracy", level-1);
                    BarChart<Number, String> levelBar = createBar("Word Statistics", level-1);
                    graphLayout.getChildren().addAll(levelPie, levelBar);
                }
                ScrollPane graphSceneLayout = new ScrollPane(graphLayout);//set the scrollpane with a vbox consisting of pie and bar
                _bgLayout.setCenter(graphSceneLayout);

            });
            optionLayout.getChildren().add(link);//add to menu vbox
        }
        optionLayout.setAlignment(Pos.CENTER);//set nodes to center of vbox

        VBox graphLayout = new VBox(80);
        //TODO VBox add pie graph of overall statistic
        graphLayout.getChildren().add(createOverallPie());
        _graphSceneLayout = new ScrollPane(graphLayout);
        _graphSceneLayout.setStyle("-fx-background-color: transparent;");

        _bgLayout = new BorderPane();
        _bgLayout.setLeft(optionLayout);
        _bgLayout.setCenter(_graphSceneLayout);
        _bgLayout.getStylesheets().add("VoxspellApp/LayoutStyles");


        return _bgLayout;
    }

    private PieChart createPie(String title, int level){
        int[] levelData = _model.findAccuracy(level);//int array size 3 level -1 b/c model accuracy starts at 0
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Failed", levelData[0]),
                new PieChart.Data("Faulted", levelData[1]),
                new PieChart.Data("Mastered", levelData[2])
        );
        final PieChart pieGraph = new PieChart(pieData);
        pieGraph.setTitle(title);
        return pieGraph;
    }

    private PieChart createOverallPie(){
        int[] levelData = _model.getOverall();//int array size 3
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Failed", levelData[0]),
                new PieChart.Data("Faulted", levelData[1]),
                new PieChart.Data("Mastered", levelData[2])
        );
        final PieChart pieGraph = new PieChart(pieData);
        pieGraph.setTitle("Overall Accuracy");
        return pieGraph;
    }

    private BarChart<Number, String> createBar(String title, int level){
        Level currentLevel = _model.getLevel(level);
        int bargraphHeight=150;

        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();
        final BarChart<Number, String> barGraph = new BarChart<Number, String>(xAxis, yAxis);
        barGraph.setTitle(title);
        xAxis.setLabel("Frequency");
        yAxis.setLabel("Word");

        XYChart.Series failSeries = new XYChart.Series();
        failSeries.setName("Failed");

        XYChart.Series faultSeries = new XYChart.Series();
        faultSeries.setName("Faulted");

        XYChart.Series masterSeries = new XYChart.Series();
        masterSeries.setName("Mastered");

        for (Word word: currentLevel){
            if (word.getStatus() != Status.Unseen){
                bargraphHeight+=75;
                //add data poitns with first param being word count and second being word string form
                failSeries.getData().add(new XYChart.Data(word.getStat(0), word.getWord()));
                faultSeries.getData().add(new XYChart.Data(word.getStat(1), word.getWord()));
                masterSeries.getData().add(new XYChart.Data(word.getStat(2), word.getWord()));
            }
        }
        barGraph.getData().addAll(failSeries, faultSeries, masterSeries);
        barGraph.setMinHeight(bargraphHeight);
        return barGraph;//TODO sort word by alphabetical?
    }
}
