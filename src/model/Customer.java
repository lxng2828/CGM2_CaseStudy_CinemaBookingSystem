package model;

public class Customer extends User {

    public Customer() {
        super();
        this.setRole(Role.CUSTOMER);
    }

    public Customer(String email, String password) {
        super(email, password, Role.CUSTOMER);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
