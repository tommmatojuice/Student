package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AnalyticsManager
{
    SystemHelper systemHelper = new SystemHelper();

    public ObservableList<WorksAnalytics> getWorks() throws SQLException {
        try (Connection c = systemHelper.getConnection()){
            String sql = "(SELECT type_of_repair, COUNT(`service_id(3)`) as count, SUM(price) as price FROM `services`, repair_works " +
                    "WHERE repair_works.service_id = services.`service_id(3)` GROUP BY `service_id(3)`) " +
                    "UNION " +
                    "(SELECT `services`.type_of_repair, 0 as count, 0 as price FROM `services` " +
                    "LEFT JOIN (SELECT `service_id(3)`, type_of_repair, COUNT(`service_id(3)`) as count, SUM(price) as price " +
                    "FROM `services`, repair_works WHERE repair_works.service_id = services.`service_id(3)` " +
                    "GROUP BY `service_id(3)` HAVING COUNT(*)>0) as temp ON services.`service_id(3)` = temp.`service_id(3)` " +
                    "WHERE temp.`service_id(3)` is NULL)";
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ObservableList<WorksAnalytics> worksAnalytics = FXCollections.observableArrayList();

            while (resultSet.next()){
                worksAnalytics.add(new WorksAnalytics(resultSet.getString("type_of_repair"),
                        resultSet.getInt("count"),
                        resultSet.getDouble("price")));
            }

            return worksAnalytics;
        }
    }

    public ObservableList<ConsumablesAnalytics> getConsumables() throws SQLException {
        try (Connection c = systemHelper.getConnection()){
            String sql = "(SELECT consumables.consumables_name, consumables.consumables_type, consumables.consumables_unit, " +
                    "COUNT(consumables_use.consumable_id) as count, SUM(consumables_cost) as price FROM consumables, consumables_use " +
                    "WHERE consumables.consumable_id = consumables_use.consumable_id GROUP BY consumables_use.consumable_id) " +
                    "UNION " +
                    "(SELECT consumables.consumables_name, consumables.consumables_type, consumables.consumables_unit, 0 as count, 0 " +
                    "FROM consumables LEFT JOIN (SELECT consumables_use.consumable_id, consumables.consumables_name, " +
                    "consumables.consumables_type, consumables.consumables_unit, COUNT(consumables_use.consumable_id) as count, " +
                    "SUM(consumables_cost) as price FROM consumables, consumables_use " +
                    "WHERE consumables.consumable_id = consumables_use.consumable_id GROUP BY consumables_use.consumable_id " +
                    "HAVING COUNT(*)>0) as temp ON consumables.consumable_id = temp.consumable_id WHERE temp.consumable_id is NULL)";
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ObservableList<ConsumablesAnalytics> consumablesAnalytics = FXCollections.observableArrayList();

            while (resultSet.next()){
                consumablesAnalytics.add(new ConsumablesAnalytics(
                        resultSet.getString("consumables_name"),
                        TypesEnum.valueOf(resultSet.getString("consumables_type")),
                        UnitsEnum.valueOf(resultSet.getString("consumables_unit")),
                        resultSet.getDouble("count"),
                        resultSet.getDouble("price")));
            }

            return consumablesAnalytics;
        }
    }
}


//(SELECT type_of_repair, COUNT(`service_id(3)`), SUM(price) FROM `services`,
// repair_works WHERE repair_works.service_id = services.`service_id(3)` GROUP BY `service_id(3)`)
// UNION (SELECT type_of_repair, 0, SUM(price) FROM `services`, repair_works WHERE
// repair_works.service_id = services.`service_id(3)` and COUNT(`service_id(3)`)<1 GROUP BY `service_id(3)`);

//SELECT `services`.type_of_repair, 0, 0 FROM `services` LEFT JOIN (SELECT `service_id(3)`, type_of_repair, COUNT(`service_id(3)`), SUM(price) FROM `services`, repair_works WHERE repair_works.service_id = services.`service_id(3)` GROUP BY `service_id(3)` HAVING COUNT(*)>0) as temp ON services.`service_id(3)` = temp.`service_id(3)`