package adminModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseDatabaseHandler {
    protected Connection connection;

    // Constructor: Initialize the database connection
    public BaseDatabaseHandler() {
        this.connection = DatabaseDriver.connectToDatabase();
    }

    // Execute a query (used by all specific handlers)
    protected ResultSet executeQuery(String query, Object... params) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        setParameters(stmt, params);
        return stmt.executeQuery();
    }

    // Execute update (used by all specific handlers)
    protected int executeUpdate(String query, Object... params) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        setParameters(stmt, params);
        return stmt.executeUpdate();
    }

    // Set parameters for PreparedStatement (common method)
    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }
}
