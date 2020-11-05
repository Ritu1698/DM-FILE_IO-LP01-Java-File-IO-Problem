package com.bridgelabz.javapractice;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
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
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
        EmployeePayrollData[] arrayOfEmps = {
                new EmployeePayrollData(1, "Bill", LocalDate.of(2018, 1, 3), "1234567890", "M", "California"),
                new EmployeePayrollData(2, "Terisa", LocalDate.of(2019, 11, 13), "4567890123", "F", "Dubai"),
                new EmployeePayrollData(3, "Charlie", LocalDate.of(2018, 5, 21), "7890123456", "M", "NY")
        };
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        employeePayrollDataForCheck = new ArrayList<>();
        employeePayrollDataForCheck.add(new EmployeePayrollData(1, "Bill", LocalDate.of(2018, 1, 3), "1234567890", "M", "California"));
        employeePayrollDataForCheck.add(new EmployeePayrollData(2, "Terisa", LocalDate.of(2019, 11, 13), "4567890123", "F", "Dubai"));
        employeePayrollDataForCheck.add(new EmployeePayrollData(3, "Charlie", LocalDate.of(2018, 5, 21), "7890123456", "M", "NY"));
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
        Assert.assertEquals(4, employeePayrollServices.size());
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
        List<EmployeePayrollData> employeePayrollServices = EmployeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeNumber("Terisa", "1111333344", EmployeePayrollService.IOService.DB_IO);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertTrue(result);


    }

    @Test
    public void givenDateRangeForEmployee_whenRetrieved_shouldMatchEmployeeCount() throws SQLException {
        List<EmployeePayrollData> employeePayrollServices;
        employeePayrollServices = EmployeePayrollService.readEmployeePayrollDataWithDateRange(EmployeePayrollService.IOService.DB_IO, startDate, endDate);
        Assert.assertEquals(2, employeePayrollServices.size());
    }

    @Test
    public void givenGenderOfEmployee_whenRetrieved_shouldMatchEmployeeCount() throws SQLException {
        int countEmployees = EmployeePayrollService.readEmployeePayrollDataGivenGender(EmployeePayrollService.IOService.DB_IO, gender);
        Assert.assertEquals(2, countEmployees);
    }

    @Test
    public void givenGenderOfEmployee_whenSalarySumRetrieved_shouldMatchEmployeeSum() throws SQLException {
        double countSalarySum = EmployeePayrollService.readEmployeePayrollDataGivenGenderReturnSumOfSalary(EmployeePayrollService.IOService.DB_IO, gender);
        Assert.assertEquals(7000000, countSalarySum, 0);
    }

    @Test
    public void givenGenderOfEmployee_whenSalaryAvgRetrieved_shouldMatchEmployeeAvg() throws SQLException {
        double countSalaryAvg = EmployeePayrollService.readEmployeePayrollDataGivenGenderReturnAvgOfSalary(EmployeePayrollService.IOService.DB_IO, gender);
        Assert.assertEquals(3500000, countSalaryAvg, 0);
    }

    @Test
    public void givenGenderOfEmployee_whenSalaryMinRetrieved_shouldMatchEmployeeMin() throws SQLException {
        double countSalaryMin = EmployeePayrollService.readEmployeePayrollDataGivenGenderReturnMinOfSalary(EmployeePayrollService.IOService.DB_IO, gender);
        Assert.assertEquals(3000000, countSalaryMin, 0);
    }

    @Test
    public void givenGenderOfEmployee_whenSalaryMaxRetrieved_shouldMatchEmployeeMax() throws SQLException {
        double countSalaryMax = EmployeePayrollService.readEmployeePayrollDataGivenGenderReturnMaxOfSalary(EmployeePayrollService.IOService.DB_IO, gender);
        Assert.assertEquals(4000000, countSalaryMax, 0);
    }

    @Test
    public void givenNewEmployee_whenAdded_shouldBeInSyncWithDB() throws SQLException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollServices;
        employeePayrollServices = EmployeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        EmployeePayrollService.addEmployeeData("Mark", LocalDate.now(), "Mexico", "M", "123468768");
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark");
        Assert.assertTrue(result);

    }

    @Test
    public void givenNewEmployee_whenAdded_shouldAddToBothTablesAndBeInSyncWithDB() throws SQLException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollServices;
        employeePayrollServices = EmployeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        EmployeePayrollService.addEmployeeDataToBoth("Jessica", LocalDate.now(), "Paris", "F", "3300661199", 3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Jessica");
        Assert.assertTrue(result);

    }

    @Test
    public void givenEmployee_whenRemoved_ShouldMatch() throws SQLException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollServices;
        employeePayrollServices = EmployeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        int size = employeePayrollService.removeEmployeeData("Charlie");
        Assert.assertEquals(3, size);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Charlie");
        Assert.assertFalse(result);

    }

    @Test
    public void given4Employees_whenAddedToDB_shouldMatchEmployeeEntries() throws SQLException, InterruptedException {
        EmployeePayrollData[] arrayOfEmployeeData = {
                new EmployeePayrollData(0, "Samuel", LocalDate.now(), "9867453654", "M", "Kansas"),
                new EmployeePayrollData(0, "Ashley", LocalDate.now(), "1029384756", "F", "Australia"),

        };
        EmployeePayrollData[] arrayOfEmployeeDataForThread = {
                new EmployeePayrollData(0, "Akhiro", LocalDate.now(), "3918274056", "M", "Japan"),
                new EmployeePayrollData(0, "Kim", LocalDate.now(), "5500440077", "F", "Malibu")
        };
        EmployeePayrollService employeePayrollServiceForArray = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollServices;
        employeePayrollServices = employeePayrollServiceForArray.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Instant start = Instant.now();
        employeePayrollServiceForArray.addEmployeesToPayroll(Arrays.asList(arrayOfEmployeeData));
        Instant end = Instant.now();
        System.out.println("Duration Without Thread: " + Duration.between(start, end));
        //employeePayrollServiceList = employeePayrollServiceThreads.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Instant threadStart = Instant.now();
        employeePayrollServiceForArray.addEmployeesToPayrollWithThreads(Arrays.asList(arrayOfEmployeeDataForThread));
        Thread.sleep(600);
        Instant threadEnd = Instant.now();
        System.out.println("Duration With Thread: " + Duration.between(threadStart, threadEnd));
        employeePayrollServiceForArray.printData(EmployeePayrollService.IOService.DB_IO);
        Assert.assertEquals(8, employeePayrollServices.size());
    }

    @Test
    public void givenMultipleEmployees_whenUpdatedToDB_shouldBeInSync() throws SQLException, InterruptedException {
        EmployeePayrollData[] updatedArrayOfEmployeeDataForThread = {
                new EmployeePayrollData(0, "Akhiro", LocalDate.now(), "1111000022", "", ""),
                new EmployeePayrollData(0, "Kim", LocalDate.now(), "4343438989", "", "")
        };

        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollServices = EmployeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Instant startThread = Instant.now();
        employeePayrollService.updateMultipleEmployeeNumberUsingThreads(Arrays.asList(updatedArrayOfEmployeeDataForThread));
        Thread.sleep(10);
        Instant endThread = Instant.now();
        System.out.println("Duration With Thread: " + Duration.between(startThread, endThread));
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Akhiro");
        boolean result1 = employeePayrollService.checkEmployeePayrollInSyncWithDB("Kim");
        Assert.assertTrue(result);
        Assert.assertTrue(result1);

    }

    @Test
    public void givenEmployeeDataInJsonServer_whenRetrieved_shouldMatchTheCount() {
        EmployeePayrollData[] arrayOfEmployees = getEmployeeList();
        EmployeePayrollService employeePayrollService;
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.REST_IO);
        Assert.assertEquals(3, entries);
    }

    @Test
    public void givenEmployeeData_whenAddedToJsonServer_ShouldMatchResponse201AndCount() {
        EmployeePayrollService employeePayrollService;
        EmployeePayrollData[] arrayOfEmployees = getEmployeeList();
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
        EmployeePayrollData employeePayrollData = new EmployeePayrollData(0, "Samuel", LocalDate.now(), "9867453654", "M", "Kansas");
        Response response = addEmployeePayrollDataToJsonServer(employeePayrollData);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(201, statusCode);
        employeePayrollData = new Gson().fromJson(response.asString(), EmployeePayrollData.class);
        employeePayrollService.addEmployeeDataForREST(employeePayrollData);
        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.REST_IO);
        Assert.assertEquals(4, entries);
    }

    @Test
    public void givenListOfNewEmployeeData_whenAddedToJsonServer_shouldMatchResponse201AndCount() {
        EmployeePayrollService employeePayrollService;
        EmployeePayrollData[] arrayOfEmployees = getEmployeeList();
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
        EmployeePayrollData[] arrayOfNewEmployeeData = {
                new EmployeePayrollData(0, "Akhiro", LocalDate.now(), "3918274056", "M", "Japan"),
                new EmployeePayrollData(0, "Kim", LocalDate.now(), "5500440077", "F", "Malibu")
        };
        for (EmployeePayrollData employeePayrollData : arrayOfNewEmployeeData) {
            Response response = addEmployeePayrollDataToJsonServer(employeePayrollData);
            int statusCode = response.getStatusCode();
            Assert.assertEquals(201, statusCode);
            employeePayrollData = new Gson().fromJson(response.asString(), EmployeePayrollData.class);
            employeePayrollService.addEmployeeDataForREST(employeePayrollData);
        }
        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.REST_IO);
        Assert.assertEquals(6, entries);

    }

    @Test
    public void givenNewPhoneNumberForEmployee_whenUpdatedToJsonServer_shouldMatch200Response() throws SQLException {
        EmployeePayrollService employeePayrollService;
        EmployeePayrollData[] arrayOfEmployees = getEmployeeList();
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
        employeePayrollService.updateEmployeeNumber("Kim", "9999888800", EmployeePayrollService.IOService.REST_IO);
        EmployeePayrollData employeePayrollData = employeePayrollService.getEmployeePayRollData("Kim");
        String empJson = new Gson().toJson(employeePayrollData);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Content-Type", "application/json");
        requestSpecification.body(empJson);
        Response response = requestSpecification.put("/employees/" + employeePayrollData.id);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(200, statusCode);


    }

    private Response addEmployeePayrollDataToJsonServer(EmployeePayrollData employeePayrollData) {
        String empJson = new Gson().toJson(employeePayrollData);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Content-Type", "application/json");
        requestSpecification.body(empJson);
        return requestSpecification.post("/employees");
    }

    private EmployeePayrollData[] getEmployeeList() {
        Response response = RestAssured.get("/employees");
        System.out.println("EMPLOYEE PAYROLL ENTRIES IN JSON Server:\n" + response.asString());
        EmployeePayrollData[] arrayOfEmployees = new Gson().fromJson(response.asString(), EmployeePayrollData[].class);
        return arrayOfEmployees;
    }
}
