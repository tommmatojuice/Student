package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class AutoShopsManager
{
    private final SystemHelper systemHelper = new SystemHelper();

    public ObservableList<AutoShops> getAll() throws SQLException
    {
        try (Connection c = systemHelper.getConnection())
        {
            String sql = "Select * from auto_repair_shops";
            Statement s = c.createStatement();
            ResultSet resultSet = s.executeQuery(sql);

            ObservableList<AutoShops> autoShops = FXCollections.observableArrayList();
            while (resultSet.next()){
                autoShops.add(new AutoShops(
                        resultSet.getInt("shop_number"),
                        resultSet.getString("address"),
                        resultSet.getString("name")
                ));
            }

            for (AutoShops autoShop : autoShops) {
                String sql2 = "SELECT distinct CONCAT( model_name,'(', model_small_name,', ', prod_country,')' ) as models FROM `auto_repair_shops`, `car_models`, `repaired_models` " +
                        "WHERE `repaired_models`.`shop_number`=" + autoShop.getShop_number() + " and repaired_models.model_id = car_models.model_id";
                Statement s2 = c.createStatement();
                ResultSet resultSet2 = s2.executeQuery(sql2);
                while (resultSet2.next()) {
                    autoShop.setModels(autoShop.getModels() + ' ' + resultSet2.getString("models"));
                }
            }

            return autoShops;
        }
    }

    public int update(AutoShops autoShop) throws SQLException {
        try(Connection c = systemHelper.getConnection())
        {
            String sql = "UPDATE auto_repair_shops SET address=?, name=? WHERE shop_number=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, autoShop.getAddress());
            s.setString(2, autoShop.getName());
            s.setInt(3, autoShop.getShop_number());

            return s.executeUpdate();
        }
    }

    public void add(AutoShops autoShop) throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String  sql = "INSERT INTO auto_repair_shops(address, name) VALUES(?, ?)";
            PreparedStatement s = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            s.setString(1, autoShop.getAddress());
            s.setString(2, autoShop.getName());
            s.executeUpdate();

            ResultSet keys = s.getGeneratedKeys();
            if(keys.next())
                return;

            throw new SQLException("Auto shop id not added");
        }
    }

    public void deleteByID(int id) throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String sql = "DELETE FROM auto_repair_shops WHERE shop_number=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setInt(1, id);
            s.executeUpdate();
        }
    }

    public AutoShops getById(int id) throws SQLException {
        try(Connection c = systemHelper.getConnection())
        {
            String sql = "SELECT * FROM auto_repair_shops WHERE shop_number=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return new  AutoShops(
                        resultSet.getInt("shop_number"),
                        resultSet.getString("address"),
                        resultSet.getString("name")
                );
            }
            return null;
        }
    }
}
