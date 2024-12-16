package com.example.dal;
import com.example.Admin;

import java.sql.SQLException;
import java.util.ArrayList;

public class CanHoDAL extends Admin {

    public CanHoDAL() {
        super(); // Kế thừa từ Admin
    }

    /**
     * Hàm thêm dữ liệu vào bảng CanHo
     * @return true nếu thêm thành công, false nếu thất bại
     */
    static String tableName = "canhotbl";
    public boolean insertCanHo() {
        String[] columns = {"MaCanHo","MaHoKhau","TenCanHo","Tang","DienTich","MoTa"};
        String[] types = {"int","String","String","int","float","String"};

        return insert(tableName, columns, types); // Gọi hàm insert từ Admin
    }
    public static boolean updateCanHo(String tableName, String columnName, String newValue, String conditionColumn, String conditionValue) throws SQLException {

        return update(tableName, columnName, newValue, conditionColumn,conditionValue);
    }
    public static boolean deleteCanHo(String tableName,String columnName,String columnValue){
        return delete(tableName,columnName,columnValue);
    }
}
