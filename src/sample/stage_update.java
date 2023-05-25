package sample;

import dataunit.listen_list;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.*;

/* This class is used to display a review window */
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
        TextArea textArea = new TextArea();
        word_list = new listen_list(target);
        String s_list = word_list.read_review(target);
        textArea.setWrapText(false);
        textArea.appendText(s_list);

        Button buttonSave = new Button("Save");
        buttonSave.setOnAction((E) -> {
            String newContext = textArea.getText();
            try (OutputStreamWriter output_t = new OutputStreamWriter(new FileOutputStream(target, false));
                 BufferedWriter output = new BufferedWriter(output_t);) {
                /* If it's false, it means it's a temporary file and needs to add a timestamp */
                output.write(newContext);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Scene scene = new Scene(textArea, 450, 500);
        stage.setScene(scene);
        stage.setTitle(label);
        stage.show();
    }
}