package com.bridgelabz.javapractice;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeePayrollServiceTest {
    EmployeePayrollService employeePayrollService;
    List<EmployeePayrollData> employeePayrollDataForCheck;

    @Before
    public void initialize() {
        EmployeePayrollData[] arrayOfEmps = {
                new EmployeePayrollData(1, "Bill", LocalDate.of(2018, 01, 03), "1234567890", "M", "California"),
                new EmployeePayrollData(2, "Terisa", LocalDate.of(2019, 11, 13), "4567890123", "F", "Dubai"),
                new EmployeePayrollData(3, "Charlie", LocalDate.of(2018, 05, 21), "7890123456", "M", "NY")
        };

        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        employeePayrollDataForCheck = new ArrayList<>();
        employeePayrollDataForCheck.add(new EmployeePayrollData(1, "Bill", LocalDate.of(2018, 01, 03), "1234567890", "M", "California"));
        employeePayrollDataForCheck.add(new EmployeePayrollData(2, "Terisa", LocalDate.of(2019, 11, 13), "4567890123", "F", "Dubai"));
        employeePayrollDataForCheck.add(new EmployeePayrollData(3, "Charlie", LocalDate.of(2018, 05, 21), "7890123456", "M", "NY"));

    }

    @Test
    public void given3Employees_whenWrittenToFile_shouldMatchEntries() {
        employeePayrollService.writeEmployeePayRollData(EmployeePayrollService.IOService.FILE_IO);
        employeePayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
        long totalEntries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
        Assert.assertEquals(3, totalEntries);
        List<EmployeePayrollData> employeePayrollDataList1;
        employeePayrollDataList1 = employeePayrollService.readDataFileIO(EmployeePayrollService.IOService.FILE_IO);
        Assert.assertEquals(employeePayrollDataList1.get(0).id, employeePayrollDataForCheck.get(0).id);
        Assert.assertEquals(employeePayrollDataList1.get(0).name, employeePayrollDataForCheck.get(0).name);
        Assert.assertEquals(employeePayrollDataList1.get(1).id, employeePayrollDataForCheck.get(1).id);
        Assert.assertEquals(employeePayrollDataList1.get(1).name, employeePayrollDataForCheck.get(1).name);
        Assert.assertEquals(employeePayrollDataList1.get(2).id, employeePayrollDataForCheck.get(2).id);
        Assert.assertEquals(employeePayrollDataList1.get(2).name, employeePayrollDataForCheck.get(2).name);
    }

    @Test
    public void givenEmployeeInDB_whenRetrieved_shouldMatchEmployeeCount() throws SQLException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollServices;
        employeePayrollServices = EmployeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Assert.assertEquals(3, employeePayrollServices.size());
        Assert.assertEquals(employeePayrollServices.get(0).id, employeePayrollDataForCheck.get(0).id);
        Assert.assertEquals(employeePayrollServices.get(0).name, employeePayrollDataForCheck.get(0).name);
        Assert.assertEquals(employeePayrollServices.get(1).id, employeePayrollDataForCheck.get(1).id);
        Assert.assertEquals(employeePayrollServices.get(1).name, employeePayrollDataForCheck.get(1).name);
        Assert.assertEquals(employeePayrollServices.get(2).id, employeePayrollDataForCheck.get(2).id);
        Assert.assertEquals(employeePayrollServices.get(2).name, employeePayrollDataForCheck.get(2).name);

    }
    @Test
    public void givenNewNumberForEmployee_whenUpdated_shouldSyncWithDB() throws SQLException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollServices;
        employeePayrollServices = EmployeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeName("Terisa", "1111333344");
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");


    }
}
