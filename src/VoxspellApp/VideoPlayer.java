package VoxspellApp;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.nio.file.Paths;

/**
 * Created by edson on 18/09/16.
 * TODO set the setup to worker thread
 */
public class VideoPlayer {
    MediaPlayer _player;
    Boolean _isPlaying;
    Boolean _isEnded;
    Label _timer;

    public VideoPlayer(String mediaFilePath){

        Media video = new Media(Paths.get("big_buck_bunny_1_minute.mp4").toUri().toString());//TODO use mediaFilePath
        _player = new MediaPlayer(video);

    }

    public void display(){
        _isEnded=false;
        _isPlaying=true;//video is by default playing
        Stage window = new Stage();
        MediaView view = new MediaView(_player);
        window.initModality(Modality.APPLICATION_MODAL);//modality for suppressing main window
        window.setTitle("Video Reward");
        Group group = new Group();

        final Timeline hoverOn = new Timeline();
        final Timeline hoverOff = new Timeline();
        group.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                hoverOn.play();
            }
        });
        group.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                hoverOff.play();
            }
        });


        final VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Slider slider = new Slider();

        //buttons
        final Button playButton = new Button();
        Image icon = new Image("MediaResources/play.png", 25, 25, false, true);
        playButton.setGraphic(new ImageView(icon));
        playButton.setShape(new Circle(16));
        playButton.setMinSize(32, 32);
        playButton.setMaxSize(32,32);
        final Button pauseButton = new Button();
        icon = new Image("MediaResources/pause.png", 18, 18, false, true);
        pauseButton.setShape(new Circle(13));
        pauseButton.setMinSize(26, 26);
        pauseButton.setMaxSize(26,26);
        pauseButton.setGraphic(new ImageView(icon));
        final Button stopButton = new Button();
        icon = new Image("MediaResources/stop.png", 18, 18, false, true);
        stopButton.setGraphic(new ImageView(icon));
        stopButton.setShape(new Circle(13));
        stopButton.setMinSize(26, 26);
        stopButton.setMaxSize(26,26);
        final Button replayButton = new Button();
        icon = new Image("MediaResources/replay.png", 18, 18, false, true);
        replayButton.setGraphic(new ImageView(icon));
        replayButton.setShape(new Circle(13));
        replayButton.setMinSize(26, 26);
        replayButton.setMaxSize(26,26);

        //timer
        _timer = new Label();
        _timer.setPrefWidth(100);
        _timer.setMinWidth(50);
        _timer.setMaxWidth(120);

        HBox controlPanel = new HBox(8);
        controlPanel.getChildren().addAll(replayButton, stopButton, playButton, pauseButton);
        controlPanel.setAlignment(Pos.CENTER);

        HBox sliderBox = new HBox(10);
        sliderBox.setAlignment(Pos.CENTER);
        sliderBox.getChildren().addAll(slider, _timer);
        sliderBox.setPrefWidth(_player.getMedia().getWidth()-50);
        vbox.getChildren().addAll(controlPanel, sliderBox);


        group.getChildren().addAll(view, vbox);
        Scene scene = new Scene(group, 400, 400);
        window.setScene(scene);
        window.setResizable(false);//disable resizing video window

        _player.play();
        _player.setOnReady(new Runnable() {
            @Override
            public void run() {
                int width = _player.getMedia().getWidth();
                int height = _player.getMedia().getHeight();
                window.setMinWidth(width);
                window.setMinHeight(height);

                slider.setMinSize(width-60,30);
                vbox.setMinSize(width, 200);
                vbox.setTranslateY(height - 50);


                slider.setMin(0.0);
                slider.setValue(0.0);
                slider.setMax(_player.getTotalDuration().toSeconds());


                hoverOn.getKeyFrames().addAll(
                        new KeyFrame(new Duration(0),
                                new KeyValue(vbox.translateYProperty(), height),
                                new KeyValue(vbox.opacityProperty(), 0.0)
                        ),
                        new KeyFrame(new Duration(300),
                                new KeyValue(vbox.translateYProperty(), height-150),
                                new KeyValue(vbox.opacityProperty(), 1.0)
                        )
                );
                hoverOff.getKeyFrames().addAll(
                        new KeyFrame(new Duration(0),
                                new KeyValue(vbox.translateYProperty(), height-100),
                                new KeyValue(vbox.opacityProperty(), 0.9)
                        ),
                        new KeyFrame(new Duration(300),
                                new KeyValue(vbox.translateYProperty(), height),
                                new KeyValue(vbox.opacityProperty(), 0.0)
                        )
                );

                window.show();

            }
        });

        _player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                slider.setValue(newValue.toSeconds());
            }
        });
        _player.currentTimeProperty().addListener(new InvalidationListener()
        {
            public void invalidated(Observable ov) {
                update();
            }
        });

        //slider actions
        slider.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                _player.seek(Duration.seconds(slider.getValue()));
            }
        });

        playButton.setOnAction(e->{
            if (_isPlaying){
                return;
            } else {
                _isPlaying = true;
                _player.play();
            }
        });

        pauseButton.setOnAction(e->{
            if(_isPlaying){
                _player.pause();
                _isPlaying=false;
            } else {
                return;
            }
        });

        stopButton.setOnAction(e->{
            _player.stop();
            window.close();
        });

        replayButton.setOnAction(e->{
            _player.seek(_player.getStartTime());
        });



        window.setOnCloseRequest(e->{
            _player.stop();
        });

    }

    private void update(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Duration currentTime = _player.getCurrentTime();
                _timer.setText(formatTime(currentTime, _player.getTotalDuration()));

            }
        });
    }

    private String formatTime(Duration current, Duration total){
        int currentSeconds = (int)Math.floor(current.toSeconds());
        int totalSeconds = (int)Math.floor(total.toSeconds());

        int currentMinutes = currentSeconds/60;

        int displayCurrentSeconds = currentSeconds - currentMinutes*60;

        return String.format("%02d:%02d", currentMinutes, displayCurrentSeconds);


    }


}
