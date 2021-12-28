package com.example.dictionary;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

// Server class
public class Server
{
    public static void main(String[] args) throws IOException
    {
        // server is listening on port 5056
        ServerSocket ss = new ServerSocket(5056);
        // running infinite loop for getting
        // client request
        Semaphore semaphore = new Semaphore(1);

        while (true)
        {
            Socket s = null;
            try
            {
                // socket object to receive incoming client requests
                s = ss.accept();
                System.out.println("A new client is connected : " + s);
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                System.out.println("Assigning new thread for this client");
                // create a new thread object
                Thread t = new ClientHandler(s,oos,ois,semaphore);
                t.start();

            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }
}

class ClientHandler extends Thread
{
    final Dictionary dictionary;
    final ObjectInputStream ois;
    final ObjectOutputStream oos;
    final Socket s;
    final Semaphore semaphore;

    // Constructor
    public ClientHandler(Socket s,ObjectOutputStream oos,ObjectInputStream ois,Semaphore semaphore)
    {
        this.s = s;
        this.oos = oos;
        this.ois = ois;
        this.semaphore = semaphore;
        this.dictionary = Dictionary.getInstance();
    }

    @Override
    public void run()
    {
        Request request;
        Response response;
        while (true)
        {
            try {
//                semaphore.acquire();
                request = (Request) ois.readObject();
                System.out.println(request.requestType);
                switch (request.requestType){
                    case GET:
                        Word word = dictionary.get(request.word.text);
                        oos.writeObject(new Response(word));
                        break;
                    case ADD:
                        dictionary.add(request.word);
                        oos.writeObject(new Response());
                        break;
                    case REMOVE:
                        dictionary.remove(request.word.text);
                        oos.writeObject(new Response());
                        break;
                    case DISCONNECT:
                        break;
                }
            }

            catch (Dictionary.DictionaryException e){
//                semaphore.release();
                try {
                    oos.writeObject(new Response(e.getMessage()));
                } catch (IOException ex) {
                    System.out.println(e.getMessage());
                    break;
                }
            }
            catch (Exception e){
//                semaphore.release();
                System.out.println(e.getMessage());
                break;
            }
        }

        try
        {
            // closing resources
            System.out.println("Client " + this.s + " sends exit...");
            System.out.println("Closing this connection.");
            this.s.close();
            this.oos.close();
            this.ois.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}