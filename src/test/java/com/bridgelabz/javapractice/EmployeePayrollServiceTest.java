package com.bridgelabz.javapractice;

import org.junit.Test;

import java.util.Arrays;
public class EmployeePayrollServiceTest {
    @Test
    public void given3Employees_whenWrittenToFile_shouldMatchEntries(){
        EmployeePayrollData[] arrayOfEmps = {
                new EmployeePayrollData(1,"Jeff Bezos",100000.0),
                new EmployeePayrollData(2,"Bill Gates", 200000.0),
                new EmployeePayrollData(3, "Mark Zuckerberg",300000.0)
        };
        EmployeePayrollService employeePayrollService;
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        employeePayrollService.writeEmployeePayRollData();
    }
}
