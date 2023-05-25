package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class stage_warning {
    String notice;
    public stage_warning(String s){
        notice=s;
    }

    public void show() throws Exception {
        Stage stage = new Stage();
        Label l = new Label(notice);
        l.setStyle("-fx-font-size: 20px");
        l.setAlignment(Pos.CENTER);
        Scene s = new Scene(l, 500, 100);
        stage.setScene(s);
        stage.setTitle("Warning");
        stage.show();
    }
}