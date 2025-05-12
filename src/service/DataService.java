package service;

import model.Admin;
import model.Customer;
import model.Movie;
import model.Role;
import model.Showtime;
import model.Ticket;
import model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataService {

    private static final String USERS_FILE_PATH = "data/users.csv";
    private static final String MOVIES_FILE_PATH = "data/movies.csv";
    private static final String SHOWTIMES_FILE_PATH = "data/showtimes.csv";
    private static final String BOOKINGS_FILE_PATH = "data/bookings.csv";
    private static final String CSV_DELIMITER = ",";

    public DataService() {
    }

    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE_PATH))) {
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] userData = line.split(CSV_DELIMITER);
                if (userData.length == 3) {
                    String email = userData[0].trim();
                    String password = userData[1].trim();
                    String roleString = userData[2].trim().toUpperCase();

                    Role role;
                    try {
                        role = Role.valueOf(roleString);
                    } catch (IllegalArgumentException e) {
                        System.err.println(
                                "Loi: Vai tro khong hop le '" + roleString + "' trong file users.csv dong: " + line);
                        continue;
                    }

                    if (role == Role.ADMIN) {
                        users.add(new Admin(email, password));
                    } else if (role == Role.CUSTOMER) {
                        users.add(new Customer(email, password));
                    }
                } else {
                    System.err.println("Loi: Dong du lieu nguoi dung khong dung dinh dang trong users.csv: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Loi khi doc file users.csv: " + e.getMessage());
        }
        return users;
    }

    public List<Movie> loadMovies() {
        List<Movie> movies = new ArrayList<>();
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(MOVIES_FILE_PATH))) {
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] movieData = line.split(CSV_DELIMITER);
                if (movieData.length == 5) {
                    String movieId = movieData[0].trim();
                    String movieName = movieData[1].trim();
                    String genre = movieData[2].trim();
                    int durationInMinutes = 0;
                    try {
                        durationInMinutes = Integer.parseInt(movieData[3].trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Loi: Thoi luong phim khong phai la so trong movies.csv dong: " + line);
                        continue;
                    }
                    String description = movieData[4].trim();
                    movies.add(new Movie(movieId, movieName, genre, durationInMinutes, description));
                } else {
                    System.err.println("Loi: Dong du lieu phim khong dung dinh dang trong movies.csv: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Loi khi doc file movies.csv: " + e.getMessage());
        }
        return movies;
    }

    public List<Showtime> loadShowtimes() {
        List<Showtime> showtimes = new ArrayList<>();
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(SHOWTIMES_FILE_PATH))) {
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] showtimeData = line.split(CSV_DELIMITER);
                if (showtimeData.length == 7) {
                    String showtimeId = showtimeData[0].trim();
                    String movieId = showtimeData[1].trim();
                    String showDate = showtimeData[2].trim();
                    String startTime = showtimeData[3].trim();
                    String roomName = showtimeData[4].trim();
                    int rows = 0;
                    int cols = 0;
                    try {
                        rows = Integer.parseInt(showtimeData[5].trim());
                        cols = Integer.parseInt(showtimeData[6].trim());
                    } catch (NumberFormatException e) {
                        System.err
                                .println("Loi: So hang hoac so cot khong phai la so trong showtimes.csv dong: " + line);
                        continue;
                    }
                    if (rows > 0 && cols > 0) {
                        showtimes.add(new Showtime(showtimeId, movieId, showDate, startTime, roomName, rows, cols));
                    } else {
                        System.err.println("Loi: So hang hoac so cot phai lon hon 0 trong showtimes.csv dong: " + line);
                    }
                } else {
                    System.err
                            .println("Loi: Dong du lieu suat chieu khong dung dinh dang trong showtimes.csv: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Loi khi doc file showtimes.csv: " + e.getMessage());
        }
        return showtimes;
    }

    public List<Ticket> loadBookings() {
        List<Ticket> bookings = new ArrayList<>();
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(BOOKINGS_FILE_PATH))) {
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] ticketData = line.split(CSV_DELIMITER);
                if (ticketData.length == 6) {
                    String ticketId = ticketData[0].trim();
                    String showtimeId = ticketData[1].trim();
                    String customerEmail = ticketData[2].trim();
                    String bookedSeatsString = ticketData[3].trim();
                    String bookingDate = ticketData[4].trim();
                    double totalPrice = 0.0;
                    try {
                        totalPrice = Double.parseDouble(ticketData[5].trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Loi: Tong tien khong phai la so trong bookings.csv dong: " + line);
                        continue;
                    }

                    List<String> bookedSeatsList = new ArrayList<>();
                    if (!bookedSeatsString.isEmpty()) {
                        bookedSeatsList.addAll(Arrays.asList(bookedSeatsString.split(";")));
                    }

                    bookings.add(
                            new Ticket(ticketId, showtimeId, customerEmail, bookedSeatsList, bookingDate, totalPrice));
                } else {
                    System.err.println("Loi: Dong du lieu ve khong dung dinh dang trong bookings.csv: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Loi khi doc file bookings.csv: " + e.getMessage());
        }
        return bookings;
    }
}
