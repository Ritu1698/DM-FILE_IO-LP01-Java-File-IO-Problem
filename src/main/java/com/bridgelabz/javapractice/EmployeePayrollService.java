package com.bridgelabz.javapractice;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {

    public List<EmployeePayrollData> readDataFileIO(IOService ioService) {
        List<EmployeePayrollData> employeePayrollDataArrayList = new ArrayList<>();
        if (ioService.equals(IOService.FILE_IO)) {
            employeePayrollDataArrayList = new EmployeePayrollFileIOService().readData();
        }
        return employeePayrollDataArrayList;
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayRollData(name));
    }

    public enum IOService {CONSOLE_IO, FILE_IO, DB_IO, REST_IO}

    private static List<EmployeePayrollData> employeePayrollList;
    private static EmployeePayrollDBService employeePayrollDBService;

    public EmployeePayrollService() {
        employeePayrollDBService = EmployeePayrollDBService.getInstance();
    }

    //Parameterized Constructor
    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
        this();
        this.employeePayrollList = employeePayrollList;
    }

    //Main Method
    public static void main(String[] args) {

        ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
        Scanner consoleInputReader = new Scanner(System.in);
        employeePayrollService.readEmployeePayRollData(consoleInputReader);
        employeePayrollService.writeEmployeePayRollData(IOService.CONSOLE_IO);
    }

    //Method to Read Employee and Add Data to List
    public void readEmployeePayRollData(Scanner consoleInputReader) {
        System.out.println("Enter Employee Id: ");
        int id = consoleInputReader.nextInt();
        System.out.println("Enter Employee Year Month & Date: ");
        int year = consoleInputReader.nextInt();
        int month = consoleInputReader.nextInt();
        int date = consoleInputReader.nextInt();
        LocalDate startDate = LocalDate.of(year, month, date);
        System.out.println("Enter Employee Name: ");
        String name = consoleInputReader.next();
        System.out.println("Enter Employee Number: ");
        String phone_number = consoleInputReader.next();
        System.out.println("Enter Employee Gender: ");
        String gender = consoleInputReader.next();
        System.out.println("Enter Employee Address: ");
        String address = consoleInputReader.next();
        employeePayrollList.add(new EmployeePayrollData(id, name, startDate, phone_number, gender, address));
    }

    //Method to Write Data to File
    public void writeEmployeePayRollData(IOService ioService) {
        if (ioService.equals(IOService.CONSOLE_IO))
            System.out.println("Writing Employee PayRoll Roaster to Console \n" + employeePayrollList);
        else if (ioService.equals(IOService.FILE_IO))
            new EmployeePayrollFileIOService().writeData(employeePayrollList);
    }

    //Method to Print Data
    public void printData(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            new EmployeePayrollFileIOService().printData();
    }

    //Method To Count Entries
    public long countEntries(IOService ioService) {
        if (ioService.equals(IOService.CONSOLE_IO))
            return employeePayrollList.size();
        else if (ioService.equals(IOService.FILE_IO))
            return new EmployeePayrollFileIOService().countEntries();
        return 0;
    }

    //Read EmployeePayrollData From The payroll_service Database using SQL
    public static List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) throws SQLException {
        if (ioService.equals(IOService.DB_IO))
            employeePayrollList = employeePayrollDBService.readData();
        return employeePayrollList;
    }

    public static List<EmployeePayrollData> readEmployeePayrollDataWithDateRange(IOService ioService, String startDate, String endDate) {
        if (ioService.equals(IOService.DB_IO))
            employeePayrollList = employeePayrollDBService.readDataWithDateRange(startDate,endDate);
        return employeePayrollList;
    }

    public static int readEmployeePayrollDataGivenGender(IOService ioService, String gender) {
        int countEmployees = 0;
        if (ioService.equals(IOService.DB_IO))
            countEmployees = employeePayrollDBService.readDataGivenGender(gender);
        return countEmployees;
    }

    //Update Phone Number From The payroll_service Database
    public void updateEmployeeNumber(String name, String newNumber) throws SQLException {

        int result = employeePayrollDBService.updateEmployeeData(name, newNumber);
        if (result == 0) return;
        EmployeePayrollData employeePayrollData = this.getEmployeePayRollData(name);
        if (employeePayrollData != null) employeePayrollData.phone_number = newNumber;
    }

    //GetData from EmployeePayrollData on Name Match
    private EmployeePayrollData getEmployeePayRollData(String name) {
        return employeePayrollList.stream()
                .filter(employeePayRollDataItem -> employeePayRollDataItem.name.equals(name))
                .findFirst()
                .orElse(null);
    }
}
