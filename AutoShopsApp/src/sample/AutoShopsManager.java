package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class AutoShopsManager
{
    private SystemHelper systemHelper = new SystemHelper();

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
}
