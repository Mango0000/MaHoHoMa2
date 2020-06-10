package at.htlkaindorf.mahohoma.database;


import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import at.htlkaindorf.mahohoma.database.DB_Database;

public class DB_Access
{

    private static DB_Access theInstance = null;
    private DB_Database database = null;

    public static DB_Access getInstance()
    {
        if(theInstance == null)
        {
            theInstance = new DB_Access();
        }
        return theInstance;
    }

    private DB_Access()
    {
        try
        {
            database = DB_Database.getInstance();
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e.toString());
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e.toString());
        }
    }

    public void connect()
    {
        try
        {
            database.connect();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e.toString());
        }
    }

    public void disconnect()
    {
        try
        {
            database.disconnect();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e.toString());
        }
    }


}
