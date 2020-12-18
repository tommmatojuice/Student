package data.manager;

import data.entity.Works;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.ui.SystemHelper;

import java.sql.*;

public class WorksManager
{
    private final SystemHelper systemHelper = new SystemHelper();

    public ObservableList<Works> getAll() throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String sql = "SELECT * FROM repair_works";
            Statement statement = c.createStatement();

            ObservableList<Works> works = FXCollections.observableArrayList();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                System.out.println(resultSet.getInt("contract_number"));
                works.add(new Works(resultSet.getInt("work_id"),
                        resultSet.getInt("service_id"),
                        resultSet.getInt("contract_number"),
                        resultSet.getInt("master_id"),
                        resultSet.getDate("receipt_date"),
                        resultSet.getDate("completion_date"),
                        resultSet.getDate("actual_completion_date")));
            }

//            for (Works work : works) {
//                String sql2 = "SELECT distinct CONCAT( model_name,'(', model_small_name,', ', prod_country,')' ) as models FROM `auto_repair_shops`, `car_models`, `repaired_models` " +
//                        "WHERE `repaired_models`.`shop_number`=" + autoShop.getShop_number() + " and repaired_models.model_id = car_models.model_id";
//                Statement s2 = c.createStatement();
//                ResultSet resultSet2 = s2.executeQuery(sql2);
//                while (resultSet2.next()) {
//                    autoShop.setModels(autoShop.getModels() + ' ' + resultSet2.getString("models"));
//                }
//            }

            return works;
        }
    }

    public ObservableList<Works> getByContract(int contractNumber) throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String sql = "SELECT * FROM repair_works WHERE contract_number=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, contractNumber);

            ObservableList<Works> works = FXCollections.observableArrayList();
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                works.add(new Works(resultSet.getInt("work_id"),
                        resultSet.getInt("service_id"),
                        resultSet.getInt("contract_number"),
                        resultSet.getInt("master_id"),
                        resultSet.getDate("receipt_date"),
                        resultSet.getDate("completion_date"),
                        resultSet.getDate("actual_completion_date")));
            }
            return works;
        }
    }

    public ObservableList<Works> getByMaster(int masterId) throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String sql = "SELECT * FROM repair_works WHERE master_id=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, masterId);

            ObservableList<Works> works = FXCollections.observableArrayList();
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                works.add(new Works(resultSet.getInt("work_id"),
                        resultSet.getInt("service_id"),
                        resultSet.getInt("contract_number"),
                        resultSet.getInt("master_id"),
                        resultSet.getDate("receipt_date"),
                        resultSet.getDate("completion_date"),
                        resultSet.getDate("actual_completion_date")));
            }
            return works;
        }
    }

    public void update(Works work) throws SQLException {
        try (Connection c = systemHelper.getConnection()){
            String sql = "UPDATE repair_works SET service_id=?, contract_number=?, master_id=?," +
                    "receipt_date=?, completion_date=?, actual_completion_date=? where work_id=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, work.getServiceId());
            statement.setInt(2, work.getContractNumber());
            statement.setInt(3, work.getMasterId());
            statement.setDate(4, work.getReceiptDate());
            statement.setDate(5, work.getCompletionDate());
            statement.setDate(6, work.getActualCompletionDate());
            statement.setInt(7, work.getWorkId());

            statement.executeUpdate();
        }
    }

    public void add(Works work) throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String sql = "INSERT INTO repair_works(service_id, contract_number, master_id, receipt_date, " +
                    "completion_date, actual_completion_date) VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, work.getServiceId());
            statement.setInt(2, work.getContractNumber());
            statement.setInt(3, work.getMasterId());
            statement.setDate(4, work.getReceiptDate());
            statement.setDate(5, work.getCompletionDate());
            statement.setDate(6, work.getActualCompletionDate());

            statement.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String sql = "DELETE FROM repair_works WHERE work_id=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, id);

            statement.executeUpdate();
        }
    }
}
