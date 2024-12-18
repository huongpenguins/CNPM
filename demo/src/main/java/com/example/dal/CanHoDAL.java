package com.example.dal;

import com.example.Admin;

import java.sql.SQLException;

public class CanHoDAL extends Admin {

    // Constructor should not have a return type and should match the class name
    public CanHoDAL() {
        super(); // Call the constructor of the parent class (Admin)
    }

    /**
     * Method to insert data into the CanHo table
     * @return true if insertion is successful, false otherwise
     */
    public boolean insertCanHo() {
        String[] columns = {"MaCanHo", "MaHoKhau", "TenCanHo", "Tang", "DienTich", "MoTa"};
        String[] types = {"int", "String", "String", "int", "float", "String"};
        return insert("canhotbl", columns, types); // Call the insert method from Admin
    }

    /**
     * Method to update data in the CanHo table
     * @return true if update is successful, false otherwise
     */
    public boolean updateCanHo(String columnName, String newValue, String conditionColumn, String conditionValue) throws SQLException {
        return update("canhotbl", columnName, newValue, conditionColumn, conditionValue);
    }

    /**
     * Method to delete data from the CanHo table
     * @return true if deletion is successful, false otherwise
     */
    public boolean deleteCanHo(String columnName, String columnValue) {
        return delete("canhotbl", columnName, columnValue);
    }
}
