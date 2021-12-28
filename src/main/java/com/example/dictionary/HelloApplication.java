package com.example.dictionary;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 814, 419);
        stage.setResizable(false);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        new RunnableDemo("as").start();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }


    class RunnableDemo implements Runnable {
        private Thread t;
        private String threadName;


        RunnableDemo(String name) {
            threadName = name;
            System.out.println("Creating " + threadName);
        }

        public void run() {
            System.out.println("Running " + threadName);
            try {
                Scanner scn = new Scanner(System.in);

                // getting localhost ip
                InetAddress ip = InetAddress.getByName("localhost");

                // establish the connection with server port 5056
                Socket s = new Socket(ip, 5056);

                // obtaining input and out streams
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                // the following loop performs the exchange of
                // information between client and client handler

                System.out.println("Heyy yo ma let me get your no.");
                while (true) {
                    String tosend = scn.nextLine();
                    // If client sends exit,close this connection
                    // and then break from the while loop
                    if (tosend.equals("Exit")) {
                        System.out.println("Closing this connection : " + s);
                        s.close();
                        System.out.println("Connection closed");
                        break;
                    }

                    oos.writeObject(new Request(new Word("Moon", new ArrayList<>()), RequestType.GET));

                    Response response = (Response) ois.readObject();
                    if (response.failureMessage.equals("")) {
                        System.out.println(response.word.definitions);
                    } else {
                        System.out.println(response.failureMessage);
                    }
                }

                // closing resources
                scn.close();
                oos.close();
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Thread " + threadName + " exiting.");
        }

        public void start() {
            System.out.println("Starting " + threadName);
            if (t == null) {
                t = new Thread(this, threadName);
                t.start();
            }
        }
    }

}
