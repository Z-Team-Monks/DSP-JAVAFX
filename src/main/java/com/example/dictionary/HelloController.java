package com.example.dictionary;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
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

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 814, 419);
        stage.setResizable(false);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        ClientSocket.getInstance();

    }


    public static void main(String[] args) throws IOException {

        launch(args);
    }

    @FXML
    private Label welcomeText;

    @FXML
    private TextField search_text_box;

    @FXML
    private TextField add_word;

    @FXML
    private TextArea add_word_def;

    @FXML
    private TableView search_result_table;

    @FXML
    private TableView all_words_table;

    @FXML
    private Text add_message_text_view;

    public void onSearchBtnClicked(ActionEvent actionEvent) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Response response = ClientSocket.getInstance().searchWord(search_text_box.getText());
                        if (response.failureMessage.equals("")) {
                            System.out.println(response.word.definitions);
                        } else {
                            System.out.println("response.failureMessage");
                        }
                        Platform.runLater(() -> {
                            add_message_text_view.setText("this hoasdh ajfn kajsdh akjsdh kasjdh khaj");
                        });
                        System.out.println("Done");
                    }
                }
        ).start();
    }

    @Override
    public void stop() throws Exception {
        ClientSocket.getInstance().close();
    }

    public void onAddWordBtnClicked(ActionEvent actionEvent) {
        System.out.println(add_word.getText());
        System.out.println(add_word_def.getText());

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Response response = ClientSocket.getInstance().addWord(new Word(add_word.getText(), new ArrayList<>(Arrays.asList(add_word_def.getText()))));
                        if (response.failureMessage.equals("")) {
                            System.out.println(response.word.definitions);
                        } else {
                            System.out.println(response.failureMessage);
                        }
                        Platform.runLater(() -> {
                            add_message_text_view.setText("this hoasdh ajfn kajsdh akjsdh kasjdh khaj");
                        });
                    }
                }
        ).start();
    }
}

class ClientSocket {

    InetAddress ip;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    Scanner scn;

    private static ClientSocket client_instance = null;

    public static ClientSocket getInstance() {
        if (client_instance == null) {
            client_instance = new ClientSocket();
        }
        return client_instance;
    }

    public void setupSocket() {
        try {
            scn = new Scanner(System.in);
            // getting localhost ip
            ip = InetAddress.getByName("localhost");
            // establish the connection with server port 5056
            Socket s = new Socket(ip, 5056);
            // obtaining input and out streams
            this.oos = new ObjectOutputStream(s.getOutputStream());
            this.ois = new ObjectInputStream(s.getInputStream());
            System.out.println("SEtup completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ClientSocket() {
        setupSocket();
    }

    public Response searchWord(String word) {
        try {
            System.out.println("Search - word from clientsocket");
            this.oos.writeObject(new Request(new Word(word, new ArrayList<>()), RequestType.GET));
            Response response = (Response) this.ois.readObject();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new Response("Connection Error!");
    }

    public Response addWord(Word word) {
        System.out.println("Search - add word from clientsocket");

        try {
            this.oos.writeObject(new Request(word, RequestType.ADD));
            Response response = (Response) this.ois.readObject();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new Response("Connection Error!");
    }

    public void close() {
        try {
            scn.close();
            oos.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}