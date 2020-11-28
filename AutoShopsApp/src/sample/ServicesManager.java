package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ServicesManager
{
    private SystemHelper systemHelper = new SystemHelper();

    public ObservableList<Services> getAll() throws SQLException
    {
        try (Connection c = systemHelper.getConnection())
        {
            String sql = "SELECT * FROM services";
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ObservableList<Services> services = FXCollections.observableArrayList();

            while (resultSet.next()){
                services.add(new Services(resultSet.getInt("service_id(3)"),
                        resultSet.getString("type_of_repair"),
                        resultSet.getDouble("price")));
            }

            return services;
        }
    }

    public void add(Services services) throws SQLException
    {
        try (Connection c = systemHelper.getConnection())
        {
            String sql = "INSERT INTO services(type_of_repair, price) VALUES(?, ?)";
            PreparedStatement statement = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, services.getType());
            statement.setDouble(2, services.getPrice());
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next())
                return;

            throw new SQLException("Service is not added!");
        }
    }

    public int update(Services services) throws SQLException
    {
        try(Connection c = systemHelper.getConnection())
        {
            String sql = "UPDATE services SET type_of_repair=?, price=? WHERE `services`.`service_id(3)`=?";
            PreparedStatement statement = c.prepareStatement(sql);

            statement.setString(1, services.getType());
            statement.setDouble(2, services.getPrice());
            statement.setInt(3, services.getSeviceId());

            return statement.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        try(Connection c = systemHelper.getConnection()) {
            String sql = "DELETE FROM `services` WHERE `services`.`service_id(3)`=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, id);
            statement.execute();
        }
    }
}
