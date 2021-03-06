package com.bridgelabz.javapractice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollFileIOService {
    public static String PAYROLL_FILE_NAME = "C:/Users/Rituparna Biswas/eclipse-workspace/DM FILE_IO Java File IO/src/main/java/com/bridgelabz/javapractice/payroll-file.txt";

    //Method to Write Data to File
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

    //Method to Count Entries
    public long countEntries() {
        long entries = 0;
        try {
            entries = Files.lines(new File("C:/Users/Rituparna Biswas/eclipse-workspace/DM FILE_IO Java File IO/src/main/java/com/bridgelabz/javapractice/payroll-file.txt").toPath()).count();
        } catch (IOException e) {
        }
        return entries;
    }

    //Method to Print Data
    public void printData() {
        try {
            Files.lines(new File("C:/Users/Rituparna Biswas/eclipse-workspace/DM FILE_IO Java File IO/src/main/java/com/bridgelabz/javapractice/payroll-file.txt").toPath()).forEach(System.out::println);
        } catch (IOException e) {
        }
    }

    //Method to Read Data From File
    public List<EmployeePayrollData> readData() {
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        try {
            Files.lines(new File("C:/Users/Rituparna Biswas/eclipse-workspace/DM FILE_IO Java File IO/src/main/java/com/bridgelabz/javapractice/payroll-file.txt").toPath()).map(line -> line.trim())
                    .forEach(line -> {
                        line = line.trim();
                        line = line.replace(":", "").replace("  ", " ").replace("'", "").replace("=", "= ").replace(",", " ,").replace("}", " }").replace("-", " - ");
                        String[] arr = line.split(" ");
                        employeePayrollDataList
                                .add(new EmployeePayrollData(Integer.parseInt(arr[1]), arr[4], LocalDate.of(Integer.parseInt(arr[7]), Integer.parseInt(arr[9]), Integer.parseInt(arr[11])), arr[14], arr[17], arr[20]));
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(employeePayrollDataList);
        return employeePayrollDataList;
    }
}
