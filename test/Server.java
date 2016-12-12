package test;

import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application 
{
    double area;
    double radius;
    TextArea ta = new TextArea();
    Pane myPane = new Pane();

    @Override
    public void start(Stage primaryStage) 
    {
        myPane.getChildren().add(ta);
        
        Scene scene = new Scene(myPane, 450, 200);
        primaryStage.setTitle("Server");
        primaryStage.setScene(scene);
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
                            ta.appendText("Server started at " + new Date() + '\n');
                        }
                    });

                    // Listen for a connection request
                    Socket socket = serverSocket.accept();

                    // Create data input and output streams
                    DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                    DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

                    while (true) 
                    {
                        // Receive radius from the client
                        radius = inputFromClient.readDouble();
                        
                        area = radius * radius * Math.PI;
                        
                        // Send area back to the client
                        outputToClient.writeDouble(area);

                        Platform.runLater(new Runnable() 
                        {
                            @Override
                            public void run() 
                            {
                                ta.appendText("Radius received from client: " + radius + '\n');
                                ta.appendText("Area is: " + area + '\n');
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
