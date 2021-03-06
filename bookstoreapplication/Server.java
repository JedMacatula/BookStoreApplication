package bookstoreapplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    String queryCommand;
    Repository myDataBase = new Repository();
    String listOfBooks;
    
    @Override
    public void start(Stage primaryStage)
    {   
        myPane.getChildren().add(serverTextArea);
        Scene myScene = new Scene(myPane, 478, 181);
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
                        queryCommand = fromClient.readUTF();
                        
                        Platform.runLater(new Runnable() 
                        {
                            @Override
                            public void run() 
                            {
                                try 
                                {
                                    listOfBooks = myDataBase.getBooks(queryCommand);
                                } 
                                catch (Exception ex) 
                                {
                                    ex.printStackTrace();
                                }
                                serverTextArea.appendText(queryCommand + '\n');
                                serverTextArea.appendText("Printing Search Results to Client..." + '\n');
                                try 
                                {   
                                    toClient.writeUTF(listOfBooks);
                                    toClient.flush();
                                } 
                                catch (Exception ex) 
                                {
                                    System.out.println("Error when sending string to client.");
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
