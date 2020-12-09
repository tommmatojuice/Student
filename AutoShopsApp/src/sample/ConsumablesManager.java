package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ConsumablesManager
{
    private SystemHelper systemHelper = new SystemHelper();

    public ObservableList<Consumables> getAll() throws SQLException
    {
        try (Connection c = systemHelper.getConnection())
        {
            String sql = "Select * from consumables";
            Statement s = c.createStatement();
            ResultSet resultSet = s.executeQuery(sql);

            ObservableList<Consumables> consumables = FXCollections.observableArrayList();
            while (resultSet.next()){
                consumables.add(new Consumables(
                        resultSet.getInt("consumable_id"),
                        resultSet.getString("consumables_name"),
                        TypesEnum.valueOf(resultSet.getString("consumables_type")),
                        UnitsEnum.valueOf(resultSet.getString("consumables_unit")),
                        resultSet.getDouble("consumables_cost"),
                        resultSet.getString("consumables_producer")

                ));
            }
            return consumables;
        }
    }

    public int update(Consumables consumable) throws SQLException {
        try(Connection c = systemHelper.getConnection())
        {
            String sql = "UPDATE consumables SET consumables_name=?, consumables_type=?, consumables_unit=?, " +
                    "consumables_cost=?, consumables_producer=? WHERE consumable_id=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setString(1, consumable.getConsumableName());
            s.setString(2, consumable.getConsumableType().name());
            s.setString(3, consumable.getConsumableUnit().name());
            s.setDouble(4, consumable.getConsumablePrice());
            s.setString(5, consumable.getConsumableProducer());
            s.setInt(6, consumable.getConsumableId());

            return s.executeUpdate();
        }
    }

    public void add(Consumables consumable) throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String  sql = "INSERT INTO consumables(consumables_name, consumables_type, consumables_unit, " +
                    "consumables_cost, consumables_producer) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement s = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            s.setString(1, consumable.getConsumableName());
            s.setString(2, consumable.getConsumableType().name());
            s.setString(3, consumable.getConsumableUnit().name());
            s.setDouble(4, consumable.getConsumablePrice());
            s.setString(5, consumable.getConsumableProducer());
            s.executeUpdate();

            ResultSet keys = s.getGeneratedKeys();
            if(keys.next())
                return;

            throw new SQLException("Consumable id not added");
        }
    }

    public void deleteByID(int id) throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String sql = "DELETE FROM consumables WHERE consumable_id=?";
            PreparedStatement s = c.prepareStatement(sql);
            s.setInt(1, id);
            s.executeUpdate();
        }
    }
}
