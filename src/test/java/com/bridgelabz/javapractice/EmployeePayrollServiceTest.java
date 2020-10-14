package com.bridgelabz.javapractice;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class EmployeePayrollServiceTest {
    EmployeePayrollService employeePayrollService;

    @Before
    public void initialize() {
        EmployeePayrollData[] arrayOfEmps = {
                new EmployeePayrollData(1, "Jeff Bezos", 100000.0),
                new EmployeePayrollData(2, "Bill Gates", 200000.0),
                new EmployeePayrollData(3, "Mark Zuckerberg", 300000.0)
        };

        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
    }

    @Test
    public void given3Employees_whenWrittenToFile_shouldMatchEntries() {
        employeePayrollService.writeEmployeePayRollData(EmployeePayrollService.IOService.FILE_IO);
        employeePayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
        long totalEntries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
        Assert.assertEquals(3, totalEntries);
        employeePayrollService.readDataFileIO(EmployeePayrollService.IOService.FILE_IO);
    }

}
