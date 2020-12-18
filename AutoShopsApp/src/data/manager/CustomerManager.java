package data.manager;

import data.entity.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.ui.SystemHelper;

import java.sql.*;

public class CustomerManager
{
    private SystemHelper systemHelper = new SystemHelper();

    public ObservableList<Customer> getAll() throws SQLException
    {
        try (Connection c = systemHelper.getConnection())
        {
            String sql = "SELECT * FROM customer";
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            ObservableList<Customer> customers = FXCollections.observableArrayList();
            while (resultSet.next()){
                customers.add(new Customer(
                        resultSet.getInt("id_customer"),
                        resultSet.getString("full_name"),
                        resultSet.getString("address"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("passport")
                ));
            }
            return customers;
        }
    }

    public Customer add(Customer customer) throws SQLException {
        try (Connection c = systemHelper.getConnection()){
            String sql = "INSERT INTO customer(full_name, address, phone_number, passport) VALUES(?, ?, ?, ?)";
            PreparedStatement statement = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, customer.getFullName());
            statement.setString(2, customer.getAddress());
            statement.setString(3, customer.getPhone());
            statement.setString(4, customer.getPassport());
            statement.execute();

            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next()){
                customer.setId(keys.getInt(1));
                return customer;
            }

            throw new SQLException("Customer is not added!");
        }
    }

    public int update(Customer customer) throws SQLException
    {
        try(Connection c = systemHelper.getConnection())
        {
            String sql = "UPDATE customer SET full_name=?, address=?, phone_number=?, passport=? WHERE id_customer=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1, customer.getFullName());
            statement.setString(2, customer.getAddress());
            statement.setString(3, customer.getPhone());
            statement.setString(4, customer.getPassport());
            statement.setInt(5, customer.getId());

            return statement.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String sql = "DELETE FROM customer WHERE id_customer=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public Customer getById(int id) throws SQLException {
        try(Connection c = systemHelper.getConnection())
        {
            String sql = "SELECT * FROM customer WHERE id_customer=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return new Customer(
                        resultSet.getInt("id_customer"),
                        resultSet.getString("full_name"),
                        resultSet.getString("address"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("passport")
                );
            }
            return null;
        }
    }

    public boolean getByPhone(String phone) throws SQLException {
        try(Connection c = systemHelper.getConnection())
        {
            String sql = "SELECT * FROM customer WHERE phone_number=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1, phone);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        }
    }
}
