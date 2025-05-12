package model;

import java.util.List;
import java.util.ArrayList;

public class Ticket {
    private String ticketId;
    private String showtimeId;
    private String customerEmail;
    private List<String> bookedSeats;
    private String bookingDate;
    private double totalPrice;

    public Ticket() {
        this.bookedSeats = new ArrayList<>();
    }

    public Ticket(String ticketId, String showtimeId, String customerEmail, List<String> bookedSeats,
            String bookingDate, double totalPrice) {
        this.ticketId = ticketId;
        this.showtimeId = showtimeId;
        this.customerEmail = customerEmail;
        this.bookedSeats = (bookedSeats != null) ? bookedSeats : new ArrayList<>();
        this.bookingDate = bookingDate;
        this.totalPrice = totalPrice;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getShowtimeId() {
        return showtimeId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public List<String> getBookedSeats() {
        return bookedSeats;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public void setShowtimeId(String showtimeId) {
        this.showtimeId = showtimeId;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setBookedSeats(List<String> bookedSeats) {
        this.bookedSeats = (bookedSeats != null) ? bookedSeats : new ArrayList<>();
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        StringBuilder seatsBuilder = new StringBuilder();
        if (bookedSeats != null && !bookedSeats.isEmpty()) {
            for (int i = 0; i < bookedSeats.size(); i++) {
                seatsBuilder.append(bookedSeats.get(i));
                if (i < bookedSeats.size() - 1) {
                    seatsBuilder.append(", ");
                }
            }
        } else {
            seatsBuilder.append("Khong co");
        }
        String seatsString = seatsBuilder.toString();
        String formattedPrice = String.format("%.0f VND", totalPrice);

        return "Ve [" +
                "Ma Ve: '" + ticketId + '\'' +
                ", Ma Suat Chieu: '" + showtimeId + '\'' +
                ", Email Khach Hang: '" + customerEmail + '\'' +
                ", Ghe Da Dat: [" + seatsString + "]" +
                ", Ngay Dat: '" + bookingDate + '\'' +
                ", Tong Tien: " + formattedPrice +
                ']';
    }
}
