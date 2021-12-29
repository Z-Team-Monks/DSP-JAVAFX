package com.example.dictionary;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloController extends Application {

    @FXML
    private Label welcomeText;

    @FXML
    private TextField search_text_box;

    @FXML
    private Text search_result_word;

    @FXML
    private Text word_def;

    @FXML
    private TextField add_word;

    @FXML
    private TextArea add_word_def;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            ClientSocket.getInstance();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 814, 370);
            stage.setResizable(false);
            stage.setTitle("My Dictionary client");
            stage.setScene(scene);
            stage.show();
        }catch (Exception e) {
            showDialog("Unable to open connection");
        }
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    public void onSearchBtnClicked(ActionEvent actionEvent) {
        new Thread(
                () -> {
                    try {
                        Response response = ClientSocket.getInstance().searchWord(search_text_box.getText());
                        if (response.failureMessage.equals("")) {
                            Platform.runLater(() -> {
                                search_result_word.setText(response.word.text);
                                String def = "";
                                for (String d : response.word.definitions) {
                                    def += d + ", ";
                                }
                                word_def.setText(def);
                            });
                        } else {
                            Platform.runLater(() -> {
                                showDialog(response.failureMessage);
                            });
                        }
                    } catch (Exception e) {
                        showDialog("accessing server with a close connection");
                    }

                }
        ).start();
    }

    @Override
    public void stop() throws Exception {
        try {
            ClientSocket.getInstance().close();
        } catch (Exception e) {
            showDialog("unable to close connection that is not opened");
        }
    }

    public void onAddWordBtnClicked(ActionEvent actionEvent) {
        new Thread(
                () -> {
                    try {
                        var a = add_word_def.getText().split("[\\r\\n]+");
                        Response response = ClientSocket.getInstance().addWord(new Word(add_word.getText(), new ArrayList<>(Arrays.asList(a))));
                        if (response.failureMessage.equals("")) {
                            Platform.runLater(() -> {
                                showDialog("Successfully added a word to dictionary");
                            });
                        } else {
                            Platform.runLater(() -> {
                                showDialog(response.failureMessage);
                            });
                        }
                    } catch (Exception e) {
                        showDialog("accessing server with a close connection");
                    }

                }
        ).start();
    }

    public void onWordDelete(ActionEvent actionEvent) {
        new Thread(
                () -> {
                    try {
                        Response response = ClientSocket.getInstance().deleteWord(new Word(add_word.getText()));
                        if (response.failureMessage.equals("")) {
                            Platform.runLater(() -> {
                                showDialog("Successfully Deleted a word from dictionary");
                            });
                        } else {
                            Platform.runLater(() -> {
                                showDialog(response.failureMessage);
                            });
                        }
                    } catch (Exception e) {
                       showDialog("accessing server with a close connection");
                    }
                }
        ).start();
    }

    public void showDialog(String message) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setTitle("DSP project");
        dialogStage.setHeight(200);
        dialogStage.setWidth(300);
        VBox vbox = new VBox(new Text(message));
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));
        dialogStage.setScene(new Scene(vbox));
        dialogStage.show();
    }

}