package service;

import model.Customer;
import model.User;
import model.Role;
import java.util.List;
import java.util.Scanner;

public class AuthService {

    private DataService dataService;

    public AuthService(DataService dataService) {
        this.dataService = dataService;
    }

    public User login(String email, String password, List<User> users) {
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            System.out.println("Email va mat khau khong duoc de trong.");
            return null;
        }

        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email.trim()) && user.getPassword().equals(password)) {
                return user;
            }
        }
        System.out.println("Email hoac mat khau khong chinh xac.");
        return null;
    }

    public Customer registerCustomer(String email, String password, String confirmPassword, List<User> users) {
        if (email == null || email.trim().isEmpty() ||
                password == null || password.isEmpty() ||
                confirmPassword == null || confirmPassword.isEmpty()) {
            System.out.println("Email va mat khau (bao gom xac nhan) khong duoc de trong.");
            return null;
        }

        if (!isValidEmailFormat(email.trim())) {
            System.out.println("Dinh dang email khong hop le. Vi du: ten@domain.com");
            return null;
        }

        for (User existingUser : users) {
            if (existingUser.getEmail().equalsIgnoreCase(email.trim())) {
                System.out.println("Email nay da duoc su dung. Vui long chon email khac.");
                return null;
            }
        }

        if (!password.equals(confirmPassword)) {
            System.out.println("Mat khau xac nhan khong khop. Vui long thu lai.");
            return null;
        }

        if (password.length() < 6) {
            System.out.println("Mat khau phai co it nhat 6 ky tu.");
            return null;
        }

        Customer newCustomer = new Customer(email.trim(), password);
        users.add(newCustomer);

        if (this.dataService != null) {
            this.dataService.saveUsers(users);
            System.out.println("Dang ky tai khoan thanh cong!");
            return newCustomer;
        } else {
            System.err.println("Loi: DataService chua duoc khoi tao trong AuthService. Khong the luu nguoi dung.");
            users.remove(newCustomer);
            return null;
        }
    }

    private boolean isValidEmailFormat(String email) {
        if (email == null) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}
