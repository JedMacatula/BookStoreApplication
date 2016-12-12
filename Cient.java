package bookstoreapplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Cient extends Application 
{
    String authorString, bookString;
    Pane root = new Pane();
    Text welcomeText = new Text(90, 60, "Select Method of Search");
    Text authorSearchText = new Text(65, 75, "Name of Author (Enter to confirm)");
    Text bookSearchText = new Text(65, 75, "Name of book (Enter to Confirm)");
    TextField authorNameTextField = new TextField();
    TextField bookNameTextField = new TextField();
    Button searchAuthorButton = new Button("By Author");
    Button searchBookButton = new Button("By Book Name");
    
    DataOutputStream toServer;
    DataInputStream fromServer;
    
    @Override
    public void start(Stage primaryStage) 
    {
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("Book Store Application");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        searchAuthorButton.setLayoutX(115);
        searchAuthorButton.setLayoutY(100);
        searchAuthorButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) 
            {
                root.getChildren().removeAll(searchAuthorButton, searchBookButton, welcomeText);
                root.getChildren().addAll(authorSearchText, authorNameTextField);
            }
        });
        
        searchBookButton.setLayoutX(100);
        searchBookButton.setLayoutY(165);
        searchBookButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                root.getChildren().removeAll(searchAuthorButton, searchBookButton, welcomeText);
                root.getChildren().addAll(bookSearchText, bookNameTextField);
            }
        });
        
        authorNameTextField.setLayoutX(80);
        authorNameTextField.setLayoutY(130);
        authorNameTextField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent keyEvent)
            {
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                {
                    authorString = authorNameTextField.getText();
                    root.getChildren().removeAll(authorSearchText, authorNameTextField);
                    try
                    {
                        toServer.writeUTF("select book from library where authorNames = '" + authorString + "';");
                    }
                    catch(IOException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        });
        
        bookNameTextField.setLayoutX(80);
        bookNameTextField.setLayoutY(130);
        bookNameTextField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent keyEvent)
            {
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                {
                    bookString = bookNameTextField.getText();
                    root.getChildren().removeAll(bookSearchText, bookNameTextField);
                    try
                    {
                        toServer.writeUTF("select book from library where bookNames = '" + bookString + "';");
                    }
                    catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        });
        
        root.getChildren().addAll(searchAuthorButton, searchBookButton, welcomeText);
        
        
        /*------------------------------------Server-Stuff---------------------------------------------*/
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
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}