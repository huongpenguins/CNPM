package com.example.dal;
import com.example.Admin;

import java.sql.SQLException;
import java.util.ArrayList;



public class HoaDonDAL extends Admin{

    //search du lieu
    public ArrayList<Object[]> searchHoaDon(String tableName, String columnName, String searchValue) {
        return super.search(tableName, columnName, searchValue);
    }

}
