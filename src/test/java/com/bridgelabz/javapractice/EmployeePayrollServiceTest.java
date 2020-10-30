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
    String startDate = "2019-01-01";
    String endDate = "2020-10-30";
    String gender = "M";

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
        employeePayrollService.updateEmployeeNumber("Terisa", "1111333344");
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertTrue(result);


    }

    @Test
    public void givenDateRangeForEmployee_whenRetrieved_shouldMatchEmployeeCount() throws SQLException {
        List<EmployeePayrollData> employeePayrollServices;
        employeePayrollServices = EmployeePayrollService.readEmployeePayrollDataWithDateRange(EmployeePayrollService.IOService.DB_IO, startDate, endDate);
        Assert.assertEquals(2,employeePayrollServices.size());
    }

    @Test
    public void givenGenderOfEmployee_whenRetrieved_shouldMatchEmployeeCount() throws SQLException {
      int  countEmployees = EmployeePayrollService.readEmployeePayrollDataGivenGender(EmployeePayrollService.IOService.DB_IO, gender);
      Assert.assertEquals(2,countEmployees);
    }

    @Test
    public void givenGenderOfEmployee_whenSalarySumRetrieved_shouldMatchEmployeeSum() throws SQLException {
        double  countSalarySum = EmployeePayrollService.readEmployeePayrollDataGivenGenderReturnSumOfSalary(EmployeePayrollService.IOService.DB_IO, gender);
        Assert.assertEquals(7000000,countSalarySum,0);
    }

    @Test
    public void givenGenderOfEmployee_whenSalaryAvgRetrieved_shouldMatchEmployeeAvg() throws SQLException {
        double  countSalaryAvg = EmployeePayrollService.readEmployeePayrollDataGivenGenderReturnAvgOfSalary(EmployeePayrollService.IOService.DB_IO, gender);
        Assert.assertEquals(3500000,countSalaryAvg,0);
    }
    @Test
    public void givenGenderOfEmployee_whenSalaryMinRetrieved_shouldMatchEmployeeMin() throws SQLException {
        double  countSalaryMin = EmployeePayrollService.readEmployeePayrollDataGivenGenderReturnMinOfSalary(EmployeePayrollService.IOService.DB_IO, gender);
        Assert.assertEquals(3000000,countSalaryMin,0);
    }

}
