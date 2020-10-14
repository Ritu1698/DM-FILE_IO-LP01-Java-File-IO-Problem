package com.bridgelabz.javapractice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollFileIOService {
    public static String PAYROLL_FILE_NAME = "C:/Users/Rituparna Biswas/eclipse-workspace/DM FILE_IO Java File IO/src/main/java/com/bridgelabz/javapractice/payroll-file.txt";

    // Write to file
    public void writeData(List<EmployeePayrollData> employeePayrollList) {

        StringBuffer empBuffer = new StringBuffer();
        employeePayrollList.forEach(employee -> {
            String employeeDataString = employee.toString().concat("\n");
            empBuffer.append(employeeDataString);
        });
        try {
            Files.write(Paths.get(PAYROLL_FILE_NAME), empBuffer.toString().getBytes());
        } catch (IOException e) {
        }
    }

    // Count entries file
    public long countEntries() {
        long entries = 0;
        try {
            entries = Files.lines(new File("C:/Users/Rituparna Biswas/eclipse-workspace/DM FILE_IO Java File IO/src/main/java/com/bridgelabz/javapractice/payroll-file.txt").toPath()).count();
        } catch (IOException e) {
        }
        return entries;
    }

    // Print data
    public void printData() {
        try {
            Files.lines(new File("C:/Users/Rituparna Biswas/eclipse-workspace/DM FILE_IO Java File IO/src/main/java/com/bridgelabz/javapractice/payroll-file.txt").toPath()).forEach(System.out::println);
        } catch (IOException e) {
        }
    }

    public List<EmployeePayrollData> readData(){
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        try{
            Files.lines(new File("C:/Users/Rituparna Biswas/eclipse-workspace/DM FILE_IO Java File IO/src/main/java/com/bridgelabz/javapractice/payroll-file.txt").toPath()).map(line -> line.trim())
                    .forEach(line ->System.out.println(line));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return employeePayrollDataList;
    }
}
