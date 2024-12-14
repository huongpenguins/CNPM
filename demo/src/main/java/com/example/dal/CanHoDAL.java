package com.example.dal;
import com.example.Admin;

public class CanHoDAL extends Admin {

    public CanHoDAL() {
        super(); // Kế thừa từ Admin
    }

    /**
     * Hàm thêm dữ liệu vào bảng CanHo
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean insertCanHo() {
        String tableName = "canhotbl";
        String[] columns = {"MaCanHo","MaHoKhau","TenCanHo","Tang","DienTich","MoTa"};
        String[] types = {"int","String","String","int","float","String"};

        return insert(tableName, columns, types); // Gọi hàm insert từ Admin
    }
}
