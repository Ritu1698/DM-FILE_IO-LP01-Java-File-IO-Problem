package com.bridgelabz.javapractice;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeePayrollService {


    public enum IOService {CONSOLE_IO, FILE_IO, DB_IO, REST_IO}

    private static List<EmployeePayrollData> employeePayrollList;
    private static EmployeePayrollDBService employeePayrollDBService;
    private static int count = 0;

    public EmployeePayrollService() {
        employeePayrollDBService = EmployeePayrollDBService.getInstance();
    }

    //Parameterized Constructor
    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
        this();
        this.employeePayrollList = employeePayrollList;
    }

    public static void addEmployeeData(String name, LocalDate date, String address, String gender, String number) {
        employeePayrollList.add(employeePayrollDBService.addEmployeePayroll(name, date, address, gender, number));
        System.out.println("addEmployeeData being Called this no. Of times: " + ++count);
    }

    public static void addEmployeeDataToBoth(String name, LocalDate start, String address, String gender, String number, Double basic_pay) throws SQLException {
        employeePayrollList.add(employeePayrollDBService.addEmployeePayrollToBothTables(name, start, address, gender, number, basic_pay));
    }

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

    public int removeEmployeeData(String name) {
        employeePayrollDBService.removeEmployeeData(name);
        employeePayrollList = employeePayrollList.stream().filter(emp -> !emp.name.equals(name)).collect(Collectors.toList());
        return employeePayrollList.size();

    }

    public void addEmployeesToPayroll(List<EmployeePayrollData> employeePayrollDataList) {
        employeePayrollDataList.forEach(employeePayrollData -> {
            System.out.println("Employee Being Added: " + employeePayrollData.name);
            addEmployeeData(employeePayrollData.name, employeePayrollData.startDate, employeePayrollData.address, employeePayrollData.gender, employeePayrollData.phone_number);
            System.out.println("Employee Added: " + employeePayrollData.name);
        });
        System.out.println("AFTER PROCESS OPERATION-------------------------\n" + employeePayrollList);
    }

    public void addEmployeesToPayrollWithThreads(List<EmployeePayrollData> employeePayrollDataList) {
        Map<Integer, Boolean> employeeAdditionStatus = new HashMap<Integer, Boolean>();
        employeePayrollDataList.forEach(employeePayrollData -> {
            Runnable task = () -> {
                employeeAdditionStatus.put(employeePayrollData.hashCode(), false);
                System.out.println("Employee Being Added Via Thread: " + Thread.currentThread().getName());
                addEmployeeData(employeePayrollData.name, employeePayrollData.startDate, employeePayrollData.address, employeePayrollData.gender, employeePayrollData.phone_number);
                employeeAdditionStatus.put(employeePayrollData.hashCode(), true);
                System.out.println("Employee Added Via Thread: " + Thread.currentThread().getName());
            };
            Thread thread = new Thread(task, employeePayrollData.name);
            thread.start();
        });
//        while (employeeAdditionStatus.containsValue(false)) {
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        System.out.println("AFTER THREADS OPERATION-------------------------\n" + employeePayrollList);
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
        else
            System.out.println(employeePayrollList);
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

    //Read EmployeePayrollData Using Date Range From The payroll_service Database using SQL
    public static List<EmployeePayrollData> readEmployeePayrollDataWithDateRange(IOService ioService, String startDate, String endDate) {
        if (ioService.equals(IOService.DB_IO))
            employeePayrollList = employeePayrollDBService.readDataWithDateRange(startDate, endDate);
        return employeePayrollList;
    }

    //Read EmployeePayrollData Return(Count) Group By Gender From The payroll_service Database using SQL
    public static int readEmployeePayrollDataGivenGender(IOService ioService, String gender) {
        int countEmployees = 0;
        if (ioService.equals(IOService.DB_IO))
            countEmployees = employeePayrollDBService.readDataGivenGender(gender);
        return countEmployees;
    }

    //Read EmployeePayrollData Return(Sum) Group By Gender From The payroll_service Database using SQL
    public static double readEmployeePayrollDataGivenGenderReturnSumOfSalary(IOService ioService, String gender) {
        double salarySum = 0;
        if (ioService.equals(IOService.DB_IO))
            salarySum = employeePayrollDBService.readDataGivenGenderReturnSalarySum(gender);
        return salarySum;

    }

    //Read EmployeePayrollData Return(Avg) Group By Gender From The payroll_service Database using SQL
    public static double readEmployeePayrollDataGivenGenderReturnAvgOfSalary(IOService ioService, String gender) {
        double salaryAvg = 0;
        if (ioService.equals(IOService.DB_IO))
            salaryAvg = employeePayrollDBService.readDataGivenGenderReturnSalaryAvg(gender);
        return salaryAvg;
    }

    //Read EmployeePayrollData Return(Min) Group By Gender From The payroll_service Database using SQL
    public static double readEmployeePayrollDataGivenGenderReturnMinOfSalary(IOService ioService, String gender) {
        double salaryMin = 0;
        if (ioService.equals(IOService.DB_IO))
            salaryMin = employeePayrollDBService.readDataGivenGenderReturnSalaryMin(gender);
        return salaryMin;
    }

    //Read EmployeePayrollData Return(Max) Group By Gender From The payroll_service Database using SQL
    public static double readEmployeePayrollDataGivenGenderReturnMaxOfSalary(IOService ioService, String gender) {
        double salaryMax = 0;
        if (ioService.equals(IOService.DB_IO))
            salaryMax = employeePayrollDBService.readDataGivenGenderReturnSalaryMax(gender);
        return salaryMax;
    }

    //Update Phone Number From The payroll_service Database
    public void updateEmployeeNumber(String name, String newNumber) throws SQLException {

        int result = employeePayrollDBService.updateEmployeeData(name, newNumber);
        if (result == 0) return;
        EmployeePayrollData employeePayrollData = this.getEmployeePayRollData(name);
        if (employeePayrollData != null) employeePayrollData.phone_number = newNumber;
    }


    public synchronized void updateMultipleEmployeeNumberUsingThreads(List<EmployeePayrollData> employeePayrollDataList){
        Map<Integer, Boolean> employeeAdditionStatus = new HashMap<Integer, Boolean>();
        employeePayrollDataList.forEach(employeePayrollData -> {
            Runnable task = () -> {
                employeeAdditionStatus.put(employeePayrollData.hashCode(), false);
                System.out.println("Employee Being Added Via Thread: " + Thread.currentThread().getName());
                try {
                    updateEmployeeNumber(employeePayrollData.name,employeePayrollData.phone_number);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                employeeAdditionStatus.put(employeePayrollData.hashCode(), true);
                System.out.println("Employee Added Via Thread: " + Thread.currentThread().getName());
            };
            Thread thread = new Thread(task, employeePayrollData.name);
            thread.start();
        });
    }

    //GetData from EmployeePayrollData on Name Match
    private EmployeePayrollData getEmployeePayRollData(String name) {
        return employeePayrollList.stream()
                .filter(employeePayRollDataItem -> employeePayRollDataItem.name.equals(name))
                .findFirst()
                .orElse(null);
    }
}
