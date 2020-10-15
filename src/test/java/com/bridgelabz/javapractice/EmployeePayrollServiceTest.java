package com.bridgelabz.javapractice;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeePayrollServiceTest {
    EmployeePayrollService employeePayrollService;
    List<EmployeePayrollData> employeePayrollDataForCheck;

    @Before
    public void initialize() {
        EmployeePayrollData[] arrayOfEmps = {
                new EmployeePayrollData(1, "Jeff Bezos", 100000.0),
                new EmployeePayrollData(2, "Bill Gates", 200000.0),
                new EmployeePayrollData(3, "Mark Zuckerberg", 300000.0)
        };

        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        employeePayrollDataForCheck = new ArrayList<>();
        employeePayrollDataForCheck.add(new EmployeePayrollData(1, "Jeff Bezos", 100000.0));
        employeePayrollDataForCheck.add(new EmployeePayrollData(2, "Bill Gates", 200000.0));
        employeePayrollDataForCheck.add(new EmployeePayrollData(3, "Mark Zuckerberg", 300000.0));

    }

    @Test
    public void given3Employees_whenWrittenToFile_shouldMatchEntries() {
        employeePayrollService.writeEmployeePayRollData(EmployeePayrollService.IOService.FILE_IO);
        employeePayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
        long totalEntries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
        Assert.assertEquals(3, totalEntries);
        List<EmployeePayrollData> employeePayrollDataList1 = new ArrayList<>();
        System.out.println("Read Data---------------------");
        employeePayrollDataList1 = employeePayrollService.readDataFileIO(EmployeePayrollService.IOService.FILE_IO);
        System.out.println(employeePayrollDataList1);
        System.out.println(employeePayrollDataForCheck);
        Assert.assertEquals(employeePayrollDataList1.get(0).id, employeePayrollDataForCheck.get(0).id);
        Assert.assertEquals(employeePayrollDataList1.get(0).name, employeePayrollDataForCheck.get(0).name);
        Assert.assertEquals(employeePayrollDataList1.get(0).salary, employeePayrollDataForCheck.get(0).salary, 0.001);
        Assert.assertEquals(employeePayrollDataList1.get(1).id, employeePayrollDataForCheck.get(1).id);
        Assert.assertEquals(employeePayrollDataList1.get(1).name, employeePayrollDataForCheck.get(1).name);
        Assert.assertEquals(employeePayrollDataList1.get(1).salary, employeePayrollDataForCheck.get(1).salary, 0.001);
        Assert.assertEquals(employeePayrollDataList1.get(2).id, employeePayrollDataForCheck.get(2).id);
        Assert.assertEquals(employeePayrollDataList1.get(2).name, employeePayrollDataForCheck.get(2).name);
        Assert.assertEquals(employeePayrollDataList1.get(2).salary, employeePayrollDataForCheck.get(2).salary, 0.001);
    }


}
