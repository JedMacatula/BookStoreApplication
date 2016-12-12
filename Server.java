package bookstoreapplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;

public class Server extends Application
{
    Pane myPane = new Pane();
    String myAuthor;
    DataInputStream fromClient;
    DataOutputStream toClient;
    
    @Override
    public void start(Stage primaryStage)
    {
        Scene myScene = new Scene(myPane, 500, 500);
        primaryStage.setTitle("Server");
        primaryStage.setScene(myScene);
        primaryStage.show();
        
        Runnable task1 = new Runnable() 
        {
            @Override
            public void run() 
            {
                try 
                {
                    // Create a server socket
                    ServerSocket serverSocket = new ServerSocket(8000);
                    
                    Platform.runLater(new Runnable() 
                    {
                        @Override
                        public void run() 
                        {
                            
                        }
                    });

                    // Listen for a connection request
                    Socket socket = serverSocket.accept();

                    // Create data input and output streams
                    fromClient = new DataInputStream(socket.getInputStream());
                    toClient = new DataOutputStream(socket.getOutputStream());

                    while (true) 
                    {
                        Platform.runLater(new Runnable() 
                        {
                            @Override
                            public void run() 
                            {
                                try 
                                {
                                    myAuthor = fromClient.readUTF();
                                    System.out.println(myAuthor);
                                } 
                                catch (IOException ex) 
                                {
                                    ex.printStackTrace();
                                }
                            }
                        });
                    }
                } 
                catch (IOException ex) 
                {
                    ex.printStackTrace();
                }
            }
        };
        Thread thread1 = new Thread(task1);
        thread1.start();
    }   
    
    public static void main(String[] args) 
    {
        launch(args);
    }
}
