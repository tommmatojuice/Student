package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CarModelManager
{
    SystemHelper systemHelper = new SystemHelper();

    public ObservableList<CarModel> getAll() throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String sql = "SELECT * FROM car_models";
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ObservableList<CarModel> carModels = FXCollections.observableArrayList();

            while (resultSet.next()){
                carModels.add(new CarModel(resultSet.getInt("model_id"),
                        resultSet.getString("model_name"),
                        resultSet.getDouble("engine_capacity"),
                        resultSet.getDouble("engine_power")));
            }

            return carModels;
        }
    }

    public void add(CarModel carModel) throws SQLException
    {
        try (Connection c = systemHelper.getConnection()){
            String sql = "INSERT INTO car_models(model_name, engine_capacity, engine_power) VALUES(?,?,?)";
            PreparedStatement statement = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, carModel.getName());
            statement.setDouble(2, carModel.getEngineCapacity());
            statement.setDouble(3, carModel.getEnginePower());
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next())
                return;

            throw new SQLException("Car model is not added!");
        }
    }

    public int update(CarModel carModel) throws SQLException
    {
        try (Connection c = systemHelper.getConnection()){
            String sql = "UPDATE car_models SET model_name=?, engine_capacity=?, engine_power=? WHERE model_id=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, carModel.getModelId());
            statement.setString(2, carModel.getName());
            statement.setDouble(3, carModel.getEngineCapacity());
            statement.setDouble(4, carModel.getEnginePower());

            return statement.executeUpdate();
        }
    }

    public int deleteById(int id) throws SQLException
    {
        try (Connection c = systemHelper.getConnection()){
            String sql = "DELETE FROM car_models WHERE model_id=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, id);
            return statement.executeUpdate();
        }
    }

    public CarModel getById(int id) throws SQLException {
        try(Connection c = systemHelper.getConnection())
        {
            String sql = "SELECT * FROM car_models WHERE model_id=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return new CarModel(resultSet.getInt("model_id"),
                        resultSet.getString("model_name"),
                        resultSet.getDouble("engine_capacity"),
                        resultSet.getDouble("engine_power"));
            }
            return null;
        }
    }
}
