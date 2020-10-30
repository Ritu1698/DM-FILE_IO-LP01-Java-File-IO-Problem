package com.bridgelabz.javapractice;

import com.mysql.cj.xdevapi.Result;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {
    private static final String query = "Select * from employee;";
    private static final String getCount = "Select count(gender) as count from employee_payroll where gender =  ? ";
    private static final String getSalarySum = "Select sum(basic_pay) as salary from employee e, payroll p where e.employee_id = p.employee_id and e.gender = ? group by e.gender";
    private static final String getSalaryAvg = "Select avg(basic_pay) as salary from employee e, payroll p where e.employee_id = p.employee_id and e.gender = ? group by e.gender";
    private static final String getSalaryMin = "Select min(basic_pay) as salary from employee e, payroll p where e.employee_id = p.employee_id and e.gender = ? group by e.gender";
    private static PreparedStatement employeePayrollDataStatement;
    private static EmployeePayrollDBService employeePayrollDBService;

    private EmployeePayrollDBService() {

    }

    public static EmployeePayrollDBService getInstance() {
        if (employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;
    }

    public List<EmployeePayrollData> readData() throws SQLException {
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                int id = result.getInt("employee_id");
                String name = result.getString("name");
                String phone_number = result.getString("phone_number");
                LocalDate startDate = result.getDate("start").toLocalDate();
                String gender = result.getString("gender");
                String address = result.getString("address");
                employeePayrollDataList.add(new EmployeePayrollData(id, name, startDate, phone_number, gender, address));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return employeePayrollDataList;
    }

    public List<EmployeePayrollData> readDataWithDateRange(String startDateRange, String endDateRange) {
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        String query1 = String.format("Select * from employee where start between cast('%s' as date) and cast('%s' as date);"
        ,startDateRange,endDateRange);
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query1);
            while (result.next()) {
                int id = result.getInt("employee_id");
                String name = result.getString("name");
                String phone_number = result.getString("phone_number");
                LocalDate startDate = result.getDate("start").toLocalDate();
                String gender = result.getString("gender");
                String address = result.getString("address");
                employeePayrollDataList.add(new EmployeePayrollData(id, name, startDate, phone_number, gender, address));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return employeePayrollDataList;
    }

    public int readDataGivenGender(String gender) {
        int count = 0;
        try (Connection connection = this.getConnection();) {
            employeePayrollDataStatement = connection.prepareStatement(getCount);
            employeePayrollDataStatement.setString(1, gender);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public double readDataGivenGenderReturnSalarySum(String gender) {
        double salarySum = 0;
        try (Connection connection = this.getConnection();) {
            employeePayrollDataStatement = connection.prepareStatement(getSalarySum);
            employeePayrollDataStatement.setString(1, gender);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            while (resultSet.next()) {
                salarySum = resultSet.getInt("salary");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  salarySum;
    }

    public double readDataGivenGenderReturnSalaryAvg(String gender) {
        double salaryAvg = 0;
        try (Connection connection = this.getConnection();) {
            employeePayrollDataStatement = connection.prepareStatement(getSalaryAvg);
            employeePayrollDataStatement.setString(1, gender);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            while (resultSet.next()) {
                salaryAvg = resultSet.getInt("salary");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  salaryAvg;
    }

    public double readDataGivenGenderReturnSalaryMin(String gender) {

        double salaryMin = 0;
        try (Connection connection = this.getConnection();) {
            employeePayrollDataStatement = connection.prepareStatement(getSalaryMin);
            employeePayrollDataStatement.setString(1, gender);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            while (resultSet.next()) {
                salaryMin = resultSet.getInt("salary");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  salaryMin;
    }

    private Connection getConnection() {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?allowPublicKeyRetrieval=true&useSSL=false";
        String userName = "root";
        String password = "root";
        Connection con = null;
        try {
            System.out.println("Connecting to database:" + jdbcURL);
            con = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection is successful!!!!!" + con);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;
    }

    public int updateEmployeeData(String name, String newNumber) throws SQLException {
        return this.updateEmployeeDataUsingStatement(name, newNumber);
    }

    public int updateEmployeeDataUsingStatement(String name, String newNumber) throws SQLException {
        String query = String.format("update employee set phone_number='%s' where name='%s';", newNumber, name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<EmployeePayrollData> getEmployeePayrollData(String name) {
        List<EmployeePayrollData> employeePayrollDataList = null;
        if (employeePayrollDataStatement == null)
            this.prepareStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollDataList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollDataList;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("employee_id");
                String name = resultSet.getString("name");
                String number = resultSet.getString("phone_number");
                String address = resultSet.getString("address");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                String gender = resultSet.getString("gender");
                employeePayrollDataList.add(new EmployeePayrollData(id, name, startDate, number, gender, address));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollDataList;
    }

    private void prepareStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String query = "select * from employee where name = ?";
            employeePayrollDataStatement = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
