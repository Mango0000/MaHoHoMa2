package at.htlkaindorf.mahohoma.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class DB_CachedConnection
{
    private Connection connection;
    private LinkedList<Statement> stmtQueue = new LinkedList<>();

    public DB_CachedConnection(Connection connection)
    {
        this.connection = connection;
    }

    public synchronized Statement getStatement() throws SQLException
    {
        if(connection == null)
        {
            throw new RuntimeException();
        }
        if(stmtQueue.isEmpty())
        {
            return connection.createStatement();
        }

        return stmtQueue.poll();
    }

    public synchronized void releaseStatement(Statement statement)
    {
        if(connection == null)
        {
            throw new RuntimeException();
        }

        stmtQueue.offer(statement);
    }
}
