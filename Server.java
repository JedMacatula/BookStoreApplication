package bookstoreapplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;

public class Server extends Application
{
    Pane myPane = new Pane();
    TextArea serverTextArea = new TextArea();
    String myAuthor;
    DataInputStream fromClient;
    DataOutputStream toClient;
    String sqlCommad;
    
    @Override
    public void start(Stage primaryStage)
    {
        myPane.getChildren().add(serverTextArea);
        Scene myScene = new Scene(myPane, 465, 170);
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
                        sqlCommad = fromClient.readUTF();
                        
                        Platform.runLater(new Runnable() 
                        {
                            @Override
                            public void run() 
                            {
                                serverTextArea.appendText(sqlCommad + '\n');
                                serverTextArea.appendText("Printing Search Results to Client..." + '\n');
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
