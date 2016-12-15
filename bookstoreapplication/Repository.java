package bookstoreapplication;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Repository 
{   
    public String getBooks(String command) throws SQLException, ClassNotFoundException
    {
        Statement myStatement = null;
        ResultSet myResultSet = null;
        String listOfBooks = "";
        
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BookStoreDataBase", "root", "Password");
            myStatement = connection.createStatement();
        }
        catch (Exception ex)
        {
            System.out.println("Error in initializing database.");
        }
        
        try
        {
            myResultSet = myStatement.executeQuery(command);
            while(myResultSet.next())
            {
                listOfBooks += (myResultSet.getString(1) +'\n');
            }
        }
        catch (Exception ex)
        {
            System.out.println("Error in statment");
        }
        
        return listOfBooks;
    }
}
