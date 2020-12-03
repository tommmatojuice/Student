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
                        resultSet.getString("model_small_name"),
                        resultSet.getString("prod_country")));
            }

            return carModels;
        }
    }

    public void add(CarModel carModel) throws SQLException
    {
        try (Connection c = systemHelper.getConnection()){
            String sql = "INSERT INTO car_models(model_name, model_small_name, prod_country) VALUES(?,?,?)";
            PreparedStatement statement = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, carModel.getBrandName());
            statement.setString(2, carModel.getModelName());
            statement.setString(3, carModel.getProdCountry());
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next())
                return;

            throw new SQLException("Car model is not added!");
        }
    }

    public void addShopHasModel(int shop_number, int model_id) throws SQLException
    {
        try (Connection c = systemHelper.getConnection()){
            String sql = "INSERT INTO repaired_models(shop_number, model_id) VALUES(?,?)";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, shop_number);
            statement.setInt(2, model_id);
            statement.executeUpdate();
        }
    }

    public int update(CarModel carModel) throws SQLException
    {
        try (Connection c = systemHelper.getConnection()){
            String sql = "UPDATE car_models SET model_name=?, model_small_name=?, prod_country=? WHERE model_id=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, carModel.getModelId());
            statement.setString(2, carModel.getBrandName());
            statement.setString(3, carModel.getModelName());
            statement.setString(4, carModel.getProdCountry());

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
                        resultSet.getString("model_small_name"),
                        resultSet.getString("prod_country"));
            }
            return null;
        }
    }

    public ObservableList<CarModel> getByShopId(int id) throws SQLException {
        try(Connection c = systemHelper.getConnection())
        {
            String sql = "SELECT distinct car_models.model_id, model_name, model_small_name, prod_country FROM `auto_repair_shops`, `car_models`, `repaired_models` " +
                    "WHERE `repaired_models`.`shop_number`=? and repaired_models.model_id = car_models.model_id";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<CarModel> carModels = FXCollections.observableArrayList();
            while (resultSet.next()){
                carModels.add(new CarModel(resultSet.getInt("model_id"),
                        resultSet.getString("model_name"),
                        resultSet.getString("model_small_name"),
                        resultSet.getString("prod_country")));
            }
            return carModels;
        }
    }
}


//SELECT auto_repair_shops.shop_number, address, name, CONCAT( model_name,'(', model_small_name,',', prod_country,')' ) FROM `auto_repair_shops`, `car_models`, `repaired_models` WHERE `repaired_models`.`shop_number`= auto_repair_shops.shop_number and repaired_models.model_id = car_models.model_id