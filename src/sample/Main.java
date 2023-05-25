package sample;

import dataunit.listen_list;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;

public class Main extends Application {
    listen_list word_list;
    HashMap<Integer, String> write_word_list;
    HashMap<Integer, String> wrong_word_list;
    int pointer = 0;
    String file_target;
    Media media;
    MediaPlayer mediaPlayer;
    boolean flag;

    Boolean button_play = true;
    Boolean textField_click = true;
    Boolean pause_flag=true;

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane pane = new GridPane();
        HBox hBox = new HBox();
        pane.setHgap(10);
        pane.setVgap(6);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12., 13., 14.5));
        Label label_word = new Label("word:");
        pane.add(label_word, 0, 1);

        TextField textField = new TextField("Please write down your word");

        // read text from assets/guide.txt


        TextArea textArea = new TextArea(
                "①Choose a kind of pattern and a target.\n②Click Play and you can write down your word in the textfield. \n③Press Enter when you finish a word. \n④Finally you can click Grade to find your accuracy!" +
                        " \n\nIf you want to input a list for yourself, you can choose the write pattern and click save after you finish your input.\n" +
                        "You can enter UP to back to last word you input in write pattern!\nGood Luck!");
        textArea.setPrefHeight(400);
        textArea.setWrapText(true);

        //横向测试纵向测试框
        ComboBox comboBoxHZ = new ComboBox();
        comboBoxHZ.getItems().addAll("横向测试", "纵向测试");
        comboBoxHZ.getSelectionModel().select(0);

        //组合框
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("C3-00", "C3-01", "C3-02", "C3-03", "C3-04", "C3-05", "C3-06", "C3-07", "C3-08", "C3-09", "C4-00", "C4-01", "C4-02", "C4-03", "C4-04", "C5-00", "C5-01", "C5-02", "C5-03", "C5-04", "C5-05", "C5-06", "C5-07", "C5-08", "C5-09", "C5-10", "C5-11", "C5-12", "C11-01", "C11-02", "C11-03", "C11-04");
        comboBox.setPrefWidth(100);
        comboBox.getSelectionModel().select(0);


//C11没有纵向测试

        //组合框
        ComboBox pattern = new ComboBox();
        pattern.getItems().addAll("write", "train");
        pattern.setPrefWidth(100);
        pattern.getSelectionModel().select(1);

        Label label_pattern = new Label("Pattern:");
        Label label_target = new Label("Target:");
        label_target.setAlignment(Pos.CENTER);

        textArea.setEditable(false);
        Label label_grade = new Label("Result:");

        Button button_grade = new Button("Grade");

        button_grade.setPrefWidth(70);

        Button button = new Button("Train");
        button.setPrefWidth(70);
        flag = true;

        Button button_pause = new Button("Pause");

        ComboBox rate_adjust = new ComboBox();
        rate_adjust.getItems().addAll(0.5, 0.75, 1.0, 1.25, 1.5, 2.0);
        rate_adjust.getSelectionModel().select(2);//设置默认值

        Label rate = new Label("Rate:");
        Button button_save = new Button("Save");
        button_save.setPrefWidth(70);


        //读取之前错误记录
        Button button_review = new Button("Review");

        hBox.getChildren().addAll(button, button_grade, button_review, rate, rate_adjust, button_pause);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);


        //Style

        pane.setStyle("-fx-background-color: snow");
        textField.setStyle("-fx-font-weight:bold;");
        textArea.setStyle(" -fx-font-family: Verdana");

        //添加组件
        pane.add(hBox, 0, 3, 5, 1);

        pane.add(label_pattern, 0, 0, 1, 1);
        pane.add(label_target, 2, 0, 1, 1);
        pane.add(comboBoxHZ, 3, 0, 1, 1);
        pane.add(comboBox, 4, 0, 1, 1);
        pane.add(pattern, 1, 0, 1, 1);
        pane.add(textField, 1, 1, 4, 1);


        pane.add(label_grade, 0, 5, 1, 1);

        pane.add(textArea, 1, 5, 4, 3);
        //初始化
        file_target = (String) comboBoxHZ.getValue() + comboBox.getValue();
        word_list = new listen_list("横向测试" + comboBox.getValue());
        primaryStage.setTitle("WordTrain4.0");
        Scene scene = new Scene(pane, 500, 300);
        Image image = new Image("icon.png");
        primaryStage.getIcons().add(image);
        primaryStage.setScene(scene);

        primaryStage.show();

    /*事件绑定*/
    //模式改变
        pattern.setOnAction((E) -> {
            if (pattern.getValue() == "write") {
                //添加save按钮
                hBox.getChildren().remove(button_pause);
                hBox.getChildren().add(button_save);
                comboBoxHZ.getItems().remove(1);
            } else if (pattern.getValue() == "train") {
                //添加pause按钮
                hBox.getChildren().remove(button_save);
                hBox.getChildren().add(button_pause);
                if (!comboBox.getItems().contains("纵向测试")) {
                    comboBoxHZ.getItems().add("纵向测试");
                }
            }
        });

    //播放按钮点击
        button.setOnAction((E) -> {
            if (button_play == true) {
                button.setStyle("-fx-font-weight: bold");
                button_play = false;
            } else {
                button.setStyle("");
                button_play = true;
            }
            file_target = (String) comboBoxHZ.getValue() + comboBox.getValue();
            word_list = new listen_list("横向测试" + comboBox.getValue());

            write_word_list = new HashMap<>();
            String[] index = ((String) comboBox.getValue()).split("-");
            //System.out.println(index[0]+"/"+comboBoxHZ.getValue() + "/" + index + " Test " + index[1].charAt(1) + "-" + comboBoxHZ.getValue() + ".mp3");

            //初始化路径
            String url;

            if (((int) (index[1]).charAt(0)) == 48) {
                url = this.getClass().getClassLoader().getResource(index[0] + "/" + comboBoxHZ.getValue() + "/" + index[1] + " Test " + index[1].charAt(1) + "-" + comboBoxHZ.getValue() + ".mp3").toExternalForm();
            } else {
                url = this.getClass().getClassLoader().getResource(index[0] + "/" + comboBoxHZ.getValue() + "/" + index[1] + " Test " + index[1] + "-" + comboBoxHZ.getValue() + ".mp3").toExternalForm();
            }
            media = new Media(url);
            if (flag == true) {
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
                flag = false;
            } else {
                mediaPlayer.pause();
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
            }
            pointer = 0;

        });


    //Save按钮点击
        button_save.setOnAction((E) -> {
            if (pattern.getValue() == "write") {
                word_list.write_all_word(file_target);
            }
        });

        //review按钮点击
        button_review.setOnAction((E) -> {
            //读取内容
            File file = new File("review/" + file_target + "-review_temporary");
            if (file.exists()) {
                String s_review = word_list.read_review("review/" + file_target + "-review_temporary");
                word_list.write_review("review/" + file_target + "-review", s_review, true);
                //读取总内容，显示到框框中
                file.delete();
            }

            if (!new File("review/" + file_target + "-review").exists()) {
                stage_warning stage_warning = new stage_warning("You should have your fist train on this target!");
                try {
                    stage_warning.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //首先读取临时保存的文件，然后增加到总文件内，显示出来
                String s = word_list.read_review("review/" + file_target + "-review");
                stage_review stage_review = new stage_review(file_target, s);
                try {
                    stage_review.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        //rate
        rate_adjust.setOnAction((E) -> {
            mediaPlayer.setRate((Double) rate_adjust.getValue());
        });

        //暂停按钮点击
        button_pause.setOnAction(e->{
            if(mediaPlayer!=null){
            if(pause_flag==true) {
                mediaPlayer.pause();
                pause_flag=false;
                button_pause.setText("Continue");
            }else if (pause_flag==false){
                mediaPlayer.play();
                pause_flag=true;
                button_pause.setText("Pause");
            }
        ;}else {
                stage_warning warning_pause=new stage_warning("You should press Train first!");
                try {
                    warning_pause.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }});

        //textfield绑定按键
        textField.setOnKeyPressed(event -> {
            String this_word = null;//记录最后一次输入的单词
            if (event.getCode() == KeyCode.ENTER) {
                if (mediaPlayer == null) {
                    stage_warning stage_warning_play = new stage_warning("You should play first!");
                    try {
                        stage_warning_play.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (pattern.getValue() == "train") {
                        if (write_word_list.size() > word_list.getsize()) {
                            stage_warning stage_warning = new stage_warning("The number of words you input have exceeded the limit!");
                            try {
                                stage_warning.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (!textField.getText().equals("")) {
                                write_word_list.put(new Integer(pointer), textField.getText().trim());
                                pointer++;
                                textField.clear();
                            }
                        }
                    } else if (pattern.getValue() == "write") {
                        this_word = textField.getText().trim();
                        word_list.add_new_word(this_word);
                        textArea.clear();
                        textArea.appendText(this_word);
                        textField.clear();
                    }
                }
            } else if (event.getCode() == KeyCode.UP && pattern.getValue() == "write") {

                System.out.println(word_list.get_word(word_list.getsize() - 1));
                textField.setText(word_list.get_word(word_list.getsize() - 1));
                word_list.remove_word();
            }

        });
        //textfield点击
        textField.setOnMouseClicked((E) -> {
            if (textField_click) {
                textField.clear();
                textField_click = false;
            }
        });

        //grade按钮点击
        button_grade.setOnAction((E) -> {
            wrong_word_list = new HashMap<>();
            textArea.clear();
            if (write_word_list == null) {
                stage_warning stage_warning = new stage_warning("You should input a word in the textarea first.");
                try {
                    stage_warning.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (comboBoxHZ.getValue() == "横向测试") {
                    for (int i = 0; i < write_word_list.size(); i++) {
                        if (!write_word_list.get(i).equals(word_list.get_word(i))) {
                            String write_word = write_word_list.get(i);
                            wrong_word_list.put(i, "Your word:" + write_word);
                            textArea.appendText("Your word:" + write_word_list.get(i) + "\t" + "Right word:" + word_list.get_word(i) + "\n");
                        }
                    }
                } else {//纵向测试练习检测
                    HashMap Z_test=new HashMap();
                    int yushu = word_list.getsize() % 4;
                    int count = 0;
                    int one_length = word_list.getsize() / 4;
                    for (int j = 0; j < yushu; j++) {
                        for (int i = 0; i<one_length+1; i++) {
                            Z_test.put(count,word_list.get_word(j + 4 * i));
                            count++;
                        }
                    }
                    for (int j = yushu; j < 4; j++) {
                        for (int i = 0; i < one_length ; i++) {
                            Z_test.put(count,word_list.get_word(j + 4 * i));
                            count++;
                        }
                    }
//重新遍历
                    for (int i = 0; i < write_word_list.size(); i++) {
                        if (!write_word_list.get(i).equals(Z_test.get(i))) {
                            String write_word = write_word_list.get(i);
                            wrong_word_list.put(i, "Your word:" + write_word);
                            textArea.appendText("Your word:" + write_word_list.get(i) + "\t" + "Right word:" + Z_test.get(i) + "\n");
                        }

                    }

                }
//添加准确率
                textArea.appendText("Accuracy:" + (1 - new Double(wrong_word_list.size()) / new Double(write_word_list.size())));


                if (mediaPlayer != null) {
                    word_list.write_review("review/" + file_target + "-review_temporary", textArea.getText(), false);
                }
            }
        });

        //HZ模式切换
        comboBoxHZ.setOnAction((E) -> {
            if (comboBoxHZ.getValue() == "纵向测试") {
                comboBox.getItems().removeAll("C3-00", "C3-01", "C3-02", "C3-03", "C3-04", "C3-05", "C3-06", "C3-07", "C3-08", "C3-09", "C4-00", "C4-01", "C4-02", "C4-03", "C4-04", "C5-00", "C5-01", "C5-02", "C5-03", "C5-04", "C5-05", "C5-06", "C5-07", "C5-08", "C5-09", "C5-10", "C5-11", "C5-12", "C11-01", "C11-02", "C11-03", "C11-04");

                comboBox.getItems().addAll("C3-01", "C3-02", "C3-03", "C3-04", "C3-05", "C3-06", "C3-07", "C3-08", "C3-09", "C4-01", "C4-02", "C4-03", "C4-04", "C5-01", "C5-02", "C5-03", "C5-04", "C5-05", "C5-06", "C5-07", "C5-08", "C5-09", "C5-10", "C5-11", "C5-12");
                comboBox.getSelectionModel().select(0);
            } else {
                if (comboBoxHZ.getValue() == "横向测试") {
                    if (!comboBox.getItems().contains("C11-01")) {
                        comboBox.getItems().removeAll("C3-01", "C3-02", "C3-03", "C3-04", "C3-05", "C3-06", "C3-07", "C3-08", "C3-09", "C4-01", "C4-02", "C4-03", "C4-04", "C5-01", "C5-02", "C5-03", "C5-04", "C5-05", "C5-06", "C5-07", "C5-08", "C5-09", "C5-10", "C5-11", "C5-12");
                        comboBox.getItems().addAll("C3-00", "C3-01", "C3-02", "C3-03", "C3-04", "C3-05", "C3-06", "C3-07", "C3-08", "C3-09", "C4-00", "C4-01", "C4-02", "C4-03", "C4-04", "C5-00", "C5-01", "C5-02", "C5-03", "C5-04", "C5-05", "C5-06", "C5-07", "C5-08", "C5-09", "C5-10", "C5-11", "C5-12", "C11-01", "C11-02", "C11-03", "C11-04");
                        comboBox.getSelectionModel().select(0);
                    }
                }
            }
        });

        //选择单元
        comboBox.setOnAction((E) -> {
            if (!comboBoxHZ.getValue().equals("")) {
                file_target = (String) comboBoxHZ.getValue() + comboBox.getValue();
                word_list = new listen_list("横向测试" + comboBox.getValue());

            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}


