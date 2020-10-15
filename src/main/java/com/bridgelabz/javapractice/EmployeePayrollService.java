package com.bridgelabz.javapractice;

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

    public enum IOService {CONSOLE_IO, FILE_IO, DB_IO, REST_IO}

    private List<EmployeePayrollData> employeePayrollList;

    //Parameterized Constructor
    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
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
        System.out.println("Enter Employee Salary: ");
        double salary = consoleInputReader.nextDouble();
        System.out.println("Enter Employee Name: ");
        String name = consoleInputReader.next();
        employeePayrollList.add(new EmployeePayrollData(id, name, salary));
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
}
