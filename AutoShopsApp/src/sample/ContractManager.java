package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ContractManager
{
    private SystemHelper systemHelper = new SystemHelper();

    public ObservableList<Contracts> getAll() throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String sql = "SELECT * FROM contract";
            Statement statement = c.createStatement();

            ObservableList<Contracts> contacts = FXCollections.observableArrayList();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                contacts.add(new Contracts(resultSet.getInt("contract_number"),
                        resultSet.getDate("contract_date_open"),
                        resultSet.getDate("contract_date_close"),
                        StatusEnum.valueOf(resultSet.getString("contract_status")),
                        resultSet.getString("state_number")));
            }
            return contacts;
        }
    }

    public void update(Contracts contract) throws SQLException {
        try (Connection c = systemHelper.getConnection()){
            String sql = "UPDATE contract SET contract_date_open=?, contract_date_close=?, contract_status=?, state_number=? where contract_number=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setDate(1, contract.getDateOpen());
            statement.setDate(2, contract.getDateClose());
            statement.setString(3, contract.getStatusEnum().name());
            statement.setString(4, contract.getStateNumber());
            statement.setInt(5, contract.getId());

            statement.executeUpdate();
        }
    }

    public void add(Contracts contract) throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String sql = "INSERT INTO contract(contract_date_open, contract_date_close, contract_status, state_number) VALUES(?, ?, ?, ?)";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setDate(1, contract.getDateOpen());
            statement.setDate(2, contract.getDateClose());
            statement.setString(3, contract.getStatusEnum().name());
            statement.setString(4, contract.getStateNumber());

            statement.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String sql = "DELETE FROM contract WHERE contract_number=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, id);

            statement.executeUpdate();
        }
    }

    public Double getCost(int id) throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String sql = "SELECT sum(price) as price FROM repair_works, services WHERE repair_works.contract_number = ? and repair_works.service_id = services.`service_id(3)`";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return resultSet.getDouble("price");
            }
            return null;
        }
    }
}
