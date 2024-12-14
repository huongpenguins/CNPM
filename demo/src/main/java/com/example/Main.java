package com.example;
import com.example.dal.CanHoDAL;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        // Tạo đối tượng CanHoDAL


        // Gọi hàm insertCanHo để nhập dữ liệu từ bàn phím và thêm vào database
//        boolean result = canHoDAL.insertCanHo();

        // Kiểm tra kết quả
//        if (result) {
//            System.out.println("Thêm căn hộ thành công!");
//        } else {
//            System.out.println("Thêm căn hộ thất bại!");
//        }
        CanHoDAL canHoDAL = new CanHoDAL();
        Admin admin = new Admin(); // Tạo đối tượng Admin
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Chỉnh sửa dữ liệu ===");

        System.out.print("Nhập tên bảng cần cập nhật: ");
        String tableName = scanner.nextLine();

        System.out.print("Nhập tên cột cần cập nhật: ");
        String columnName = scanner.nextLine();

        System.out.print("Nhập giá trị mới: ");
        String newValue = scanner.nextLine();

        System.out.print("Nhap cot tim kiem ");
        String conditionColumn = scanner.nextLine();

        System.out.print("Nhap gia tri cot tim kiem: ");
        String conditionValue = scanner.nextLine();

        boolean updateResult = CanHoDAL.updateCanHo(tableName, columnName, newValue,conditionColumn,conditionValue);
        if(updateResult == false){
            System.out.println("cap nhat du lieu that bai");
        }else{
            System.out.println("cap nhat du lieu thanh cong");
        }
    }
}
