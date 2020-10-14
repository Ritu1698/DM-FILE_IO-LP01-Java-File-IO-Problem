package com.bridgelabz.javapractice;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {
    public enum IOService {CONSOLE_IO, FILE_IO, DB_IO, REST_IO}

    ;
    private List<EmployeePayrollData> employeePayrollList;

    public EmployeePayrollService() {
    }

    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList){
        this.employeePayrollList = employeePayrollList;
    }
    public static void main(String [] args){

        ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
        Scanner consoleInputReader = new Scanner(System.in);
        employeePayrollService.readEmployeePayRollData(consoleInputReader);
        employeePayrollService.writeEmployeePayRollData();
    }
    public void readEmployeePayRollData(Scanner consoleInputReader){
        System.out.println("Enter Employee Id: ");
        int id = consoleInputReader.nextInt();
        System.out.println("Enter Employee Salary: ");
        double salary  = consoleInputReader.nextDouble();
        System.out.println("Enter Employee Name: ");
        String name = consoleInputReader.next();
        employeePayrollList.add(new EmployeePayrollData(id, name, salary));
    }
    public void writeEmployeePayRollData(){
        System.out.println("Writing Employee PayRoll Roaster to Console \n"+employeePayrollList);
    }
}
