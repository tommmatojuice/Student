package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class MastersManager
{
    private SystemHelper systemHelper = new SystemHelper();

    public ObservableList<Masters> getAll() throws SQLException
    {
        try (Connection c = systemHelper.getConnection())
        {
            String sql = "SELECT * from masters";
            Statement s = c.createStatement();
            ResultSet resultSet = s.executeQuery(sql);

            ObservableList<Masters> masters = FXCollections.observableArrayList();
            while (resultSet.next()){
                masters.add(new Masters(
                        resultSet.getInt("master_id"),
                        resultSet.getString("full_name"),
                        resultSet.getString("phone_number"),
                        resultSet.getInt("shop_number")
                ));
            }
            return masters;
        }
    }

    public int update(Masters masters) throws SQLException {
        try(Connection c = systemHelper.getConnection())
        {
            String sql = "UPDATE masters SET full_name=?, phone_number=?, shop_number=? WHERE master_id=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, masters.getName());
            s.setString(2, masters.getPhone());
            s.setInt(3, masters.getAutoShop().getShop_number());
            s.setInt(4, masters.getMasterId());
            return s.executeUpdate();
        }
    }

    public void add(Masters masters) throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String  sql = "INSERT INTO masters(full_name, phone_number, shop_number) VALUES(?, ?, ?)";
            PreparedStatement s = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            s.setString(1, masters.getName());
            s.setString(2, masters.getPhone());
            s.setInt(3, masters.getAutoShop().getShop_number());
            s.executeUpdate();

            ResultSet keys = s.getGeneratedKeys();
            if(keys.next())
                return;

            throw new SQLException("Master shop id not added");
        }
    }

    public void deleteByID(int id) throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String sql = "DELETE FROM masters WHERE master_id=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setInt(1, id);
            s.executeUpdate();
        }
    }
}