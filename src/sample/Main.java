package sample;

import dataunit.listenList;
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.File;
import java.util.HashMap;

public class Main extends Application {
    listenList wordList;
    HashMap<Integer, String> writeWordList;
    HashMap<Integer, String> wrongWordList;
    int pointer = 0;
    String fileTarget;
    Media media;
    MediaPlayer mediaPlayer;
    boolean flag;

    Boolean buttonPlay = true;
    Boolean textFieldClick = true;
    Boolean pauseFlag=true;

    @Override
    public void start(Stage primaryStage){
        GridPane pane = new GridPane();
        HBox hBox = new HBox();
        pane.setHgap(10);
        pane.setVgap(6);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12., 13., 14.5));
        Label label_word = new Label("word:");
        pane.add(label_word, 0, 1);

        TextField textField = new TextField("Please write down your word");

        String guideText = readGuideText();

        TextArea textArea = new TextArea(guideText);
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

        Label label_pattern = new Label("Mode:");
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
        textArea.setStyle("-fx-font-family: Verdana");

        // set font style to Arial
        label_word.setStyle("-fx-font-family: Verdana");


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
        fileTarget = (String) comboBoxHZ.getValue() + comboBox.getValue();
        wordList = new listenList("横向测试" + comboBox.getValue());
        primaryStage.setTitle("Take Word Down 4.0");
        Scene scene = new Scene(pane, 500, 300);
        Image image = new Image("assets/icon.png");
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
        button.setOnAction(e -> {
            button.setStyle(buttonPlay ? "-fx-font-weight: bold" : "");
            buttonPlay = !buttonPlay;

            String[] index = ((String) comboBox.getValue()).split("-");
            String fileTarget = comboBoxHZ.getValue().toString() + comboBox.getValue();

            String url = this.getClass().getResource(
                    String.format("%s/%s/%s Test %s%s-%s.mp3",
                            index[0], comboBoxHZ.getValue(), index[1],
                            index[1].charAt(0), index[1].length() > 1 ? "-" : "", comboBoxHZ.getValue()
                    )).toExternalForm();

            media = new Media(url);
            if (flag) {
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
                flag = false;
            } else {
                mediaPlayer.pause();
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
            }

            pointer = 0;

            wordList = new listenList("横向测试" + comboBox.getValue());
            writeWordList = new HashMap<>();
        });


    //Save按钮点击
        button_save.setOnAction((E) -> {
            if (pattern.getValue() == "write") {
                wordList.write_all_word(fileTarget);
            }
        });

        //review按钮点击
        button_review.setOnAction((E) -> {
            //读取内容
            File file = new File("review/" + fileTarget + "-review_temporary");
            if (file.exists()) {
                String s_review = wordList.read_review("review/" + fileTarget + "-review_temporary");
                wordList.write_review("review/" + fileTarget + "-review", s_review, true);
                //读取总内容，显示到框框中
                file.delete();
            }

            if (!new File("review/" + fileTarget + "-review").exists()) {
                stage_warning stage_warning = new stage_warning("You should have your fist train on this target!");
                try {
                    stage_warning.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //首先读取临时保存的文件，然后增加到总文件内，显示出来
                String s = wordList.read_review("review/" + fileTarget + "-review");
                stage_review stage_review = new stage_review(fileTarget, s);
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
            if(pauseFlag==true) {
                mediaPlayer.pause();
                pauseFlag=false;
                button_pause.setText("Continue");
            }else if (pauseFlag==false){
                mediaPlayer.play();
                pauseFlag=true;
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
                        if (writeWordList.size() > wordList.getsize()) {
                            stage_warning stage_warning = new stage_warning("The number of words you input have exceeded the limit!");
                            try {
                                stage_warning.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (!textField.getText().equals("")) {
                                writeWordList.put(new Integer(pointer), textField.getText().trim());
                                pointer++;
                                textField.clear();
                            }
                        }
                    } else if (pattern.getValue() == "write") {
                        this_word = textField.getText().trim();
                        wordList.add_new_word(this_word);
                        textArea.clear();
                        textArea.appendText(this_word);
                        textField.clear();
                    }
                }
            } else if (event.getCode() == KeyCode.UP && pattern.getValue() == "write") {

                System.out.println(wordList.get_word(wordList.getsize() - 1));
                textField.setText(wordList.get_word(wordList.getsize() - 1));
                wordList.remove_word();
            }

        });
        //textfield点击
        textField.setOnMouseClicked((E) -> {
            if (textFieldClick) {
                textField.clear();
                textFieldClick = false;
            }
        });

        button_grade.setOnAction(e -> {
            wrongWordList = new HashMap<>();
            textArea.clear();
            if (writeWordList == null) {
                stage_warning stage_warning = new stage_warning("You should input a word in the textarea first.");
                try {
                    stage_warning.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                if ("横向测试".equals(comboBoxHZ.getValue())) {
                    for (int i = 0; i < writeWordList.size(); i++) {
                        String writeWord = writeWordList.get(i);
                        String correctWord = wordList.get_word(i);
                        if (!writeWord.equals(correctWord)) {
                            wrongWordList.put(i, "Your word:" + writeWord);
                            textArea.appendText("Your word:" + writeWord + "\t" + "Right word:" + correctWord + "\n");
                        }
                    }
                } else {
                    HashMap<Integer, String> zTest = new HashMap<>();
                    int wordListSize = wordList.getsize();
                    int remainder = wordListSize % 4;
                    int count = 0;
                    int oneLength = wordListSize / 4;
                    for (int j = 0; j < remainder; j++) {
                        for (int i = 0; i < oneLength + 1; i++) {
                            zTest.put(count, wordList.get_word(j + 4 * i));
                            count++;
                        }
                    }
                    for (int j = remainder; j < 4; j++) {
                        for (int i = 0; i < oneLength; i++) {
                            zTest.put(count, wordList.get_word(j + 4 * i));
                            count++;
                        }
                    }
                    for (int i = 0; i < writeWordList.size(); i++) {
                        String writeWord = writeWordList.get(i);
                        String correctWord = zTest.get(i);
                        if (!writeWord.equals(correctWord)) {
                            wrongWordList.put(i, "Your word:" + writeWord);
                            textArea.appendText("Your word:" + writeWord + "\t" + "Right word:" + correctWord + "\n");
                        }
                    }
                }
                double accuracy = 1 - ((double) wrongWordList.size() / writeWordList.size());
                textArea.appendText("Accuracy:" + accuracy);
                if (mediaPlayer != null) {
                    wordList.write_review("review/" + fileTarget + "-review_temporary", textArea.getText(), false);
                }
            }
        });

        comboBoxHZ.setOnAction(e -> {
            boolean isVertical = "纵向测试".equals(comboBoxHZ.getValue());
            boolean hasC1101 = comboBox.getItems().contains("C11-01");

            if (isVertical) {
                comboBox.getItems().setAll("C3-01", "C3-02", "C3-03", "C3-04", "C3-05", "C3-06", "C3-07", "C3-08", "C3-09", "C4-01", "C4-02", "C4-03", "C4-04", "C5-01", "C5-02", "C5-03", "C5-04", "C5-05", "C5-06", "C5-07", "C5-08", "C5-09", "C5-10", "C5-11", "C5-12");
            } else if (!hasC1101) {
                comboBox.getItems().setAll("C3-00", "C3-01", "C3-02", "C3-03", "C3-04", "C3-05", "C3-06", "C3-07", "C3-08", "C3-09", "C4-00", "C4-01", "C4-02", "C4-03", "C4-04", "C5-00", "C5-01", "C5-02", "C5-03", "C5-04", "C5-05", "C5-06", "C5-07", "C5-08", "C5-09", "C5-10", "C5-11", "C5-12", "C11-01", "C11-02", "C11-03", "C11-04");
            }
            comboBox.getSelectionModel().select(0);
        });

        //选择单元
        comboBox.setOnAction((E) -> {
            if (!comboBoxHZ.getValue().equals("")) {
                fileTarget = (String) comboBoxHZ.getValue() + comboBox.getValue();
                wordList = new listenList("横向测试" + comboBox.getValue());

            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }

    private String readGuideText() {
        String currentPath = System.getProperty("user.dir");
        String filePath = currentPath + "/src/assets/guide.txt";
        StringBuilder fileContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        return fileContent.toString();
    }
}


