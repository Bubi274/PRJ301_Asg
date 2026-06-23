package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing SQL Server database connections.
 * Shared across all modules (SF1, SF2, SF3, SF4, SF5).
 *
 * Database: ApartmentManagement
 * Driver: Microsoft JDBC Driver for SQL Server
 */
public class DBConnection {

    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://localhost:1433;"
            + "databaseName=ApartmentManagement;"
            + "encrypt=false;"
            + "trustServerCertificate=true;";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "123";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                    "Khong tim thay SQL Server JDBC Driver: " + e.getMessage());
        }
    }

    /**
     * Returns a new connection to the ApartmentManagement database.
     *
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    /**
     * Safely closes a Connection object.
     * Safe to call with null.
     *
     * @param conn the connection to close
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Loi khi dong Connection: " + e.getMessage());
            }
        }
    }
}
