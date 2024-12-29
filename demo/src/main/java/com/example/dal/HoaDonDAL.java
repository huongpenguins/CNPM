package com.example.dal;
import com.example.Admin;
import java.util.ArrayList;



public class HoaDonDAL extends Admin{
    static String tableName = "hoadontbl";

    //search du lieu
    public ArrayList<Object[]> searchHoaDon(String tableName, String columnName, String searchValue) {
        return super.search(tableName, columnName, searchValue);
    }

}
