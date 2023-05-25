package sample;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/*专门用于显示review的框框*/
public class stage_review {
    String label;
    String notice;
    public stage_review(String label,String s){
        this.label=label;
        this.notice=s;
    }

    public void show() throws Exception {
        Stage stage = new Stage();
        TextArea textArea=new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(false);
        textArea.appendText(notice);
        textArea.setStyle("-fx-font-size: 15px");
        Scene s = new Scene(textArea, 450, 500);
        stage.setScene(s);
        stage.setTitle(label);
        stage.show();
    }
}