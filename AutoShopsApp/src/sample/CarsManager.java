package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.stream.events.StartElement;
import java.net.CacheRequest;
import java.sql.*;

public class CarsManager
{
    SystemHelper systemHelper = new SystemHelper();

    public ObservableList<Cars> getAll() throws SQLException {
        try(Connection c = systemHelper.getConnection()){
            String sql = "SELECT * FROM cars";
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ObservableList<Cars> cars = FXCollections.observableArrayList();

            while (resultSet.next()){
                cars.add(new Cars(resultSet.getString("state_number"),
                        resultSet.getDate("year_of_issue"),
                        resultSet.getInt("data_sheet_number"),
                        resultSet.getInt("model_id"),
                        resultSet.getInt("id_customer")));
            }

            return cars;
        }
    }

    public void add(Cars car) throws SQLException
    {
        try (Connection c = systemHelper.getConnection()){
            String sql = "INSERT INTO cars(state_number, year_of_issue, data_sheet_number, model_id, id_customer) VALUES(?,?,?,?,?)";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1, car.getStateNumber());
            statement.setDate(2, car.getYearOfIssue());
            statement.setInt(3, car.getDateSheetNumber());
            statement.setInt(4, car.getModelId());
            statement.setInt(5, car.getCustomerId());
            statement.executeUpdate();
            //INSERT INTO `cars` (`state_number`, `year_of_issue`, `data_sheet_number`, `model_id`, `id_customer`) VALUES ('G567DF43', '2019-08-13', '345578', '3', '6');
        }
    }

    public int update(Cars car) throws SQLException
    {
        try (Connection c = systemHelper.getConnection()){
            String sql = "UPDATE cars SET state_number=?, year_of_issue=?, data_sheet_number=?, model_id=?, id_customer=? WHERE state_number=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1, car.getStateNumber());
            statement.setDate(2, car.getYearOfIssue());
            statement.setInt(3, car.getDateSheetNumber());
            statement.setInt(4, car.getModelId());
            statement.setInt(5, car.getCustomerId());
            statement.setString(6, car.getStateNumber());

            System.out.println(sql);
            return statement.executeUpdate();
        }
    }

    public int deleteById(String stateNumber) throws SQLException
    {
        try (Connection c = systemHelper.getConnection()){
            String sql = "DELETE FROM cars WHERE state_number=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1, stateNumber);
            return statement.executeUpdate();
        }
    }
}
