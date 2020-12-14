package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.List;

public class ConsumablesUseManager
{
    private SystemHelper systemHelper = new SystemHelper();

    public ObservableList<ConsumablesUse> getAll() throws SQLException
    {
        try (Connection c = systemHelper.getConnection())
        {
            String sql = "Select * from consumables_use";
            Statement s = c.createStatement();
            ResultSet resultSet = s.executeQuery(sql);

            ObservableList<ConsumablesUse> consumablesUses = FXCollections.observableArrayList();
            while (resultSet.next()){
                consumablesUses.add(new ConsumablesUse(
                        resultSet.getInt("use_id"),
                        resultSet.getInt("work_id"),
                        resultSet.getInt("consumable_id"),
                        resultSet.getDouble("number_of_consumables")
                ));
            }
            return consumablesUses;
        }
    }

    public int update(ConsumablesUse consumablesUse) throws SQLException {
        try(Connection c = systemHelper.getConnection())
        {
            String sql = "UPDATE consumables_use SET work_id=?, consumable_id=?, number_of_consumables=? WHERE use_id=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setInt(1, consumablesUse.getWorkId());
            s.setInt(2, consumablesUse.getConsumableId());
            s.setDouble(3, consumablesUse.getNumber());
            s.setInt(4, consumablesUse.getConsumableUseId());

            return s.executeUpdate();
        }
    }

    public void add(ConsumablesUse consumablesUse) throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String  sql = "INSERT INTO consumables_use(work_id, consumable_id, number_of_consumables) VALUES(?, ?, ?)";
            PreparedStatement s = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            s.setInt(1, consumablesUse.getWorkId());
            s.setInt(2, consumablesUse.getConsumableId());
            s.setDouble(3, consumablesUse.getNumber());
            s.executeUpdate();

            ResultSet keys = s.getGeneratedKeys();
            if(keys.next())
                return;

            throw new SQLException("ConsumableUse id not added");
        }
    }

    public void deleteByID(int id) throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String sql = "DELETE FROM consumables_use WHERE use_id=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setInt(1, id);
            s.executeUpdate();
        }
    }

    public ObservableList<ConsumablesUse> getByWorkId(int id) throws SQLException
    {
        try (Connection c = systemHelper.getConnection())
        {
            String sql = "SELECT * FROM consumables_use WHERE work_id = ?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setInt(1, id);
            ResultSet resultSet = s.executeQuery();

            ObservableList<ConsumablesUse> consumablesUses = FXCollections.observableArrayList();
            while (resultSet.next()){
                consumablesUses.add(new ConsumablesUse(
                        resultSet.getInt("use_id"),
                        resultSet.getInt("work_id"),
                        resultSet.getInt("consumable_id"),
                        resultSet.getDouble("number_of_consumables")
                ));
            }
            return consumablesUses;
        }
    }

}
