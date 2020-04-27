package at.htlkaindorf.mahohoma.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class DB_Properties
{
    private static final Properties PROPS = new Properties();

    static
    {
        Path filepath = Paths.get(System.getProperty("user.dir"),"src","main", "res", "database.properties");
        try
        {
            PROPS.load(new FileInputStream(filepath.toFile()));
        }
        catch (IOException e)
        {
            throw new RuntimeException("File not found");
        }
    }

    public static String getProperty(String key)
    {
        return PROPS.getProperty(key);
    };

    public static void main(String[] args)
    {
        for(String key : PROPS.stringPropertyNames())
        {
            System.out.println(key + " = "+PROPS.getProperty(key));
        }
    }
}
