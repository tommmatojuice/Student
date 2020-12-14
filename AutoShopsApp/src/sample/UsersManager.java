package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class UsersManager
{
    private SystemHelper systemHelper = new SystemHelper();

    public ObservableList<Users> getAll() throws SQLException
    {
        try (Connection c = systemHelper.getConnection())
        {
            String sql = "SELECT * FROM `users` WHERE role = 'мастер'";
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ObservableList<Users> users = FXCollections.observableArrayList();

            while (resultSet.next()){
                users.add(new Users(resultSet.getInt("user_id"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        resultSet.getInt("master")));
            }

            return users;
        }
    }

    public void add(Users users) throws SQLException
    {
        try (Connection c = systemHelper.getConnection())
        {
            String sql = "INSERT INTO users(login, password, role, name, master) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement statement = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, users.getLogin());
            statement.setString(2, users.getPassword());
            statement.setString(3, "мастер");
            statement.setString(4, null);
            statement.setInt(5, users.getMaster());
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next())
                return;

            throw new SQLException("User is not added!");
        }
    }

    public int update(Users user) throws SQLException
    {
        try(Connection c = systemHelper.getConnection())
        {
            String sql = "UPDATE users SET master=? WHERE user_id=?";
            PreparedStatement statement = c.prepareStatement(sql);

            statement.setInt(1, user.getMaster());
            statement.setInt(2, user.getId());

            return statement.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        try(Connection c = systemHelper.getConnection()) {
            String sql = "DELETE FROM users WHERE user_id=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, id);
            statement.execute();
        }
    }
}
