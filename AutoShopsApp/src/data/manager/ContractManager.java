package data.manager;

import data.entity.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import data.entity.Contracts;
import data.enumes.StatusEnum;
import sample.ui.SystemHelper;

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

    public Contracts add(Contracts contract) throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String sql = "INSERT INTO contract(contract_date_open, contract_date_close, contract_status, state_number) VALUES(?, ?, ?, ?)";
            PreparedStatement statement = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setDate(1, contract.getDateOpen());
            statement.setDate(2, contract.getDateClose());
            statement.setString(3, contract.getStatusEnum().name());
            statement.setString(4, contract.getStateNumber());
            statement.execute();

            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next()){
                contract.setId(keys.getInt(1));
                return contract;
            }

            throw new SQLException("Contact is not added!");
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

    public Contracts getById(int id) throws SQLException {
        try(Connection c = systemHelper.getConnection())
        {
            String sql = "SELECT * FROM contract WHERE contract_number=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return new Contracts(resultSet.getInt("contract_number"),
                        resultSet.getDate("contract_date_open"),
                        resultSet.getDate("contract_date_close"),
                        StatusEnum.valueOf(resultSet.getString("contract_status")),
                        resultSet.getString("state_number"));
            }
            return null;
        }
    }

    public ObservableList<Contracts>  getByMaster(int masterId) throws SQLException {
        System.out.println(masterId);
        try(Connection c = systemHelper.getConnection())
        {
            String sql = "SELECT contract.contract_number, contract.contract_date_open, contract.contract_date_close, contract.contract_status, " +
                    "contract.state_number FROM repair_works, contract WHERE repair_works.master_id = ? " +
                    "and repair_works.contract_number = contract.contract_number;";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, masterId);

            ObservableList<Contracts> contacts = FXCollections.observableArrayList();
            ResultSet resultSet = statement.executeQuery();

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
}
