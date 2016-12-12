package test;

import javafx.event.EventHandler;
import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

public class Client extends Application 
{
    // IO stream
    DataOutputStream toServer;
    DataInputStream fromServer;
    
    TextArea ta = new TextArea();
    TextField tf = new TextField();
    Pane myPane = new Pane();

    @Override
    public void start(Stage primaryStage) 
    {
        myPane.getChildren().add(ta);

        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5, 5, 5, 5));
        paneForTextField.setStyle("-fx-border-color: green");
        paneForTextField.setLeft(new Label("Enter a radius: "));

        tf.setAlignment(Pos.BOTTOM_RIGHT);
        paneForTextField.setCenter(tf);

        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(myPane);
        mainPane.setTop(paneForTextField);
        
        Scene scene = new Scene(mainPane, 450, 200);
        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);
        primaryStage.show();

        tf.setOnKeyPressed(new EventHandler<KeyEvent>() 
        {
            @Override
            public void handle(KeyEvent keyEvent) 
            {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) 
                {
                    try 
                    {
                        double radius = Double.parseDouble(tf.getText().trim());

                        // Send the radius to the server
                        toServer.writeDouble(radius);
                        toServer.flush();

                        // Get area from the server
                        double area = fromServer.readDouble();
                        
                        ta.appendText("Radius is " + radius + "\n");
                        ta.appendText("Area received from the server is " + area + '\n');
                    } 
                    catch (IOException ex) 
                    {
                        System.err.println(ex);
                    }
                }
            }
        });

        try 
        {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        } 
        catch (IOException ex) 
        {
            ta.appendText(ex.toString() + '\n');
        }
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}
