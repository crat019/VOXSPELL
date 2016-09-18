package VoxspellApp;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.nio.file.Paths;

/**
 * Created by edson on 18/09/16.
 */
public class VideoPlayer {
    MediaPlayer _player;

    public VideoPlayer(String mediaFilePath){

        Media video = new Media(Paths.get("big_buck_bunny_1_minute.mp4").toUri().toString());//TODO use mediaFilePath
        _player = new MediaPlayer(video);

    }

    public void display(){
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
        Slider slider = new Slider();
        vbox.getChildren().addAll(slider);


        group.getChildren().addAll(view, vbox);
        Scene scene = new Scene(group, 400, 400);
        window.setScene(scene);
        window.show();

        _player.play();
        _player.setOnReady(new Runnable() {
            @Override
            public void run() {
                int width = _player.getMedia().getWidth();
                int height = _player.getMedia().getHeight();
                window.setMinWidth(width);
                window.setMinHeight(height);

                vbox.setMinSize(width, 200);
                vbox.setTranslateY(height - 100);

                slider.setMin(0.0);
                slider.setValue(0.0);
                slider.setMax(_player.getTotalDuration().toSeconds());


                hoverOn.getKeyFrames().addAll(
                        new KeyFrame(new Duration(0),
                                new KeyValue(vbox.translateYProperty(), height),
                                new KeyValue(vbox.opacityProperty(), 0.0)
                        ),
                        new KeyFrame(new Duration(300),
                                new KeyValue(vbox.translateYProperty(), height-100),
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

            }
        });

        _player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                slider.setValue(newValue.toSeconds());
            }
        });
        slider.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                _player.seek(Duration.seconds(slider.getValue()));
            }
        });

    }

}
