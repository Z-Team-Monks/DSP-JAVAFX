package com.example.dictionary;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

class ClientSocket {

    InetAddress ip;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    Scanner scn;
    private static ClientSocket client_instance = null;

    public static ClientSocket getInstance() throws IOException {
        if (client_instance == null) {
            client_instance = new ClientSocket();
        }
        return client_instance;
    }

    public void setupSocket() throws IOException {

        scn = new Scanner(System.in);
        // getting localhost ip
        ip = InetAddress.getByName("localhost");
        // establish the connection with server port 5056
        Socket s = new Socket(ip, 5056);
        // obtaining input and out streams
        this.oos = new ObjectOutputStream(s.getOutputStream());
        this.ois = new ObjectInputStream(s.getInputStream());

    }

    ClientSocket() throws IOException {
        setupSocket();
    }

    public Response searchWord(String word) {
        try {
            System.out.println("Search - word from clientsocket");
            this.oos.writeObject(new Request(new Word(word, new ArrayList<>()), RequestType.GET));
            Response response = (Response) this.ois.readObject();
            return response;
        } catch (IOException e) {
//            e.printStackTrace();
        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
        }
        return new Response("Connection Error!");
    }

    public Response addWord(Word word) {
        try {
            this.oos.writeObject(new Request(word, RequestType.ADD));
            Response response = (Response) this.ois.readObject();
            return response;
        } catch (IOException e) {
//            e.printStackTrace();
        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
        }
        return new Response("Connection Error!");
    }

    public Response deleteWord(Word word) {
        try {
            this.oos.writeObject(new Request(word, RequestType.REMOVE));
            Response response = (Response) this.ois.readObject();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
        }
        return new Response("Connection Error!");
    }

    public void close() {
        try {
            scn.close();
            oos.close();
            ois.close();
        } catch (Exception e) {
            System.out.println("ClientSocket-close:Unable to close");
        }
    }
}
