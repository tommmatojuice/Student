package util;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlDatabase
{
    private final String address;
    private final String db;
    private final int port;
    private final String user;
    private final String password;

    private MysqlDataSource source;

    public MysqlDatabase(String address, String db, String user, String password) {
        this.address = address;
        this.db = db;
        this.port = 3306;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        if(source == null){
            source = new MysqlDataSource();

            source.setServerName(address);
            source.setDatabaseName(db);
            source.setPort(port);
            source.setUser(user);
            source.setPassword(password);

            source.setServerTimezone("UTC");
            source.setCharacterEncoding("UTF-8");
        }
        return source.getConnection();
    }
}
