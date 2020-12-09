package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    public void update(Works work) throws SQLException {
        try (Connection c = systemHelper.getConnection()){
            String sql = "UPDATE repair_works SET service_id=?, contract_number=?, master_id=?," +
                    "receipt_date=?, completion_date=?, actual_completion_date=? where work_id=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, work.getShopId());
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
            statement.setInt(1, work.getShopId());
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
