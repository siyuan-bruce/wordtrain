package sample;

import dataunit.listen_list;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.*;

/*专门用于显示review的框框,该*/
public class stage_update {
    String label;
    String target;
    listen_list word_list;
    public stage_update(String label, String target){
        this.label=label;
        this.target=target;
    }

    public void show() throws Exception {
        Stage stage = new Stage();
        TextArea textArea=new TextArea();
        word_list=new listen_list(target);
        String s_list=word_list.read_review(target);
        textArea.setWrapText(false);
        textArea.appendText(s_list);
        Button button_save=new Button("Save");
        button_save.setOnAction((E)->{
            String new_context=textArea.getText();
            try (OutputStreamWriter output_t = new OutputStreamWriter(new FileOutputStream(target, false));
                 BufferedWriter output = new BufferedWriter(output_t);) {
                /*如果是false说明是temporary的文件，需要添加时间*/
                output.write(new_context);
        } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }});


        Scene s = new Scene(textArea, 450, 500);
        stage.setScene(s);
        stage.setTitle(label);
        stage.show();
    }
}