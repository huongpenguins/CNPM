package com.example;
import com.example.dal.CanHoDAL;

public class Main {
    public static void main(String[] args) {
        // Tạo đối tượng CanHoDAL
        CanHoDAL canHoDAL = new CanHoDAL();

        // Gọi hàm insertCanHo để nhập dữ liệu từ bàn phím và thêm vào database
        boolean result = canHoDAL.insertCanHo();

        // Kiểm tra kết quả
        if (result) {
            System.out.println("Thêm căn hộ thành công!");
        } else {
            System.out.println("Thêm căn hộ thất bại!");
        }
    }
}
