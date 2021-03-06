package com.bridgelabz.javapractice;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.function.DoubleConsumer;

public class EmployeePayrollData {
    public int id;
    public String name;
    public LocalDate startDate;
    public String phone_number;
    public String gender;
    public String address;


    public EmployeePayrollData(int id, String name, LocalDate startDate, String phone_number, String gender, String address) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.phone_number = phone_number;
        this.gender = gender;
        this.address = address;
    }

    @Override
    public String toString() {
        return "EmployeePayrollData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", phone_number='" + phone_number + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeePayrollData that = (EmployeePayrollData) o;
        return id == that.id && phone_number.equals(that.phone_number) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, startDate, phone_number, gender, address);

    }
}
