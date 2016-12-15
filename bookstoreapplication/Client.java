package bookstoreapplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Client extends Application 
{
    String authorString, bookString, listOfBooksFromServer;
    Pane root = new Pane();
    Text welcomeText = new Text(90, 30, "Select Method of Search");
    Text authorSearchText = new Text(65, 45, "Name of Author (Enter to confirm)");
    Text bookSearchText = new Text(65, 45, "Name of book (Enter to Confirm)");
    TextField authorNameTextField = new TextField();
    TextField bookNameTextField = new TextField();
    TextArea displayBooksTextArea = new TextArea();
    Button searchAuthorButton = new Button("By Author");
    Button searchBookButton = new Button("By Book Name");
    
    DataOutputStream toServer;
    DataInputStream fromServer;
    
    Servlet myServlet = new Servlet();
    
    @Override
    public void start(Stage primaryStage) 
    {
        Scene scene = new Scene(root, 300, 181);
        primaryStage.setTitle("Book Store Application");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        searchAuthorButton.setLayoutX(115);
        searchAuthorButton.setLayoutY(70);
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
        searchBookButton.setLayoutY(135);
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
        authorNameTextField.setLayoutY(100);
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
                        toServer.writeUTF("select book from library where Author = '" + authorString + "';");
                        toServer.flush();
                    }
                    catch(Exception ex)
                    {
                        System.out.println("Error in sending data from client to server.");
                    }
                    try 
                    {
                        listOfBooksFromServer = fromServer.readUTF();
                        System.out.println(listOfBooksFromServer);
                    } 
                    catch (Exception ex) 
                    {
                        System.out.println("Error in recieving list of books from server");
                    }
                    root.getChildren().add(displayBooksTextArea);
                    displayBooksTextArea.appendText("Search Results" + '\n' + "-----------------" + '\n');
                    displayBooksTextArea.appendText(listOfBooksFromServer);
                }
            }
        });
        
        bookNameTextField.setLayoutX(80);
        bookNameTextField.setLayoutY(100);
        bookNameTextField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent keyEvent)
            {
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                {
                    bookString = bookNameTextField.getText();
                    System.out.println(bookString);
                    root.getChildren().removeAll(bookSearchText, bookNameTextField);
                    try
                    {
                        toServer.writeUTF("select Book from library where Book = '" + bookString + "';");
                        toServer.flush();
                    }
                    catch (Exception ex)
                    {
                        System.out.println("Error in sending data from client to server.");
                    }
                    try 
                    {
                        listOfBooksFromServer = fromServer.readUTF();
                        System.out.println(listOfBooksFromServer);
                    } 
                    catch (Exception ex) 
                    {
                        System.out.println("Error in recieving list of books from server");
                    }
                    root.getChildren().add(displayBooksTextArea);
                    displayBooksTextArea.appendText("Search Results" + '\n' + "-----------------" + '\n');
                    displayBooksTextArea.appendText(listOfBooksFromServer);
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
        catch (Exception ex) 
        {
            System.out.println("Error in client connecting to server.");
        }
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}