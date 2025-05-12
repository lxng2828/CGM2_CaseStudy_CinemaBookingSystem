package model;

public class Admin extends User {

    public Admin() {
        super();
        this.setRole(Role.ADMIN);
    }

    public Admin(String email, String password) {
        super(email, password, Role.ADMIN);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
