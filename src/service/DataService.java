package service;

import model.Admin;
import model.Customer;
import model.Movie;
import model.Role;
import model.Showtime;
import model.Ticket;
import model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataService {
    private static final String USERS_FILE_PATH = "data/users.csv";
    private static final String MOVIES_FILE_PATH = "data/movies.csv";
    private static final String SHOWTIMES_FILE_PATH = "data/showtimes.csv";
    private static final String BOOKINGS_FILE_PATH = "data/bookings.csv";
    private static final String EXPORTS_DIR = "exports/";
    private static final String CSV_DELIMITER = ",";
    private static final String SEAT_LIST_DELIMITER = ";";

    public DataService() {
    }

    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE_PATH))) {
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
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
                if (line.trim().isEmpty())
                    continue;
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
                if (line.trim().isEmpty())
                    continue;
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
                if (line.trim().isEmpty())
                    continue;
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
                        bookedSeatsList.addAll(Arrays.asList(bookedSeatsString.split(SEAT_LIST_DELIMITER)));
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

    public void saveUsers(List<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE_PATH, false))) {
            for (User user : users) {
                writer.println(user.getEmail() + CSV_DELIMITER +
                        user.getPassword() + CSV_DELIMITER +
                        user.getRole().name());
            }
        } catch (IOException e) {
            System.err.println("Loi khi ghi file users.csv: " + e.getMessage());
        }
    }

    public void saveMovies(List<Movie> movies) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(MOVIES_FILE_PATH, false))) {
            for (Movie movie : movies) {
                writer.println(movie.getMovieId() + CSV_DELIMITER +
                        movie.getMovieName() + CSV_DELIMITER +
                        movie.getGenre() + CSV_DELIMITER +
                        movie.getDurationInMinutes() + CSV_DELIMITER +
                        movie.getDescription());
            }
        } catch (IOException e) {
            System.err.println("Loi khi ghi file movies.csv: " + e.getMessage());
        }
    }

    public void saveShowtimes(List<Showtime> showtimes) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SHOWTIMES_FILE_PATH, false))) {
            for (Showtime showtime : showtimes) {
                int rows = 0;
                int cols = 0;
                if (showtime.getSeatMap() != null && showtime.getSeatMap().length > 0) {
                    rows = showtime.getSeatMap().length;
                    if (showtime.getSeatMap()[0] != null) {
                        cols = showtime.getSeatMap()[0].length;
                    }
                }
                writer.println(showtime.getShowtimeId() + CSV_DELIMITER +
                        showtime.getMovieId() + CSV_DELIMITER +
                        showtime.getShowDate() + CSV_DELIMITER +
                        showtime.getStartTime() + CSV_DELIMITER +
                        showtime.getRoomName() + CSV_DELIMITER +
                        rows + CSV_DELIMITER +
                        cols);
            }
        } catch (IOException e) {
            System.err.println("Loi khi ghi file showtimes.csv: " + e.getMessage());
        }
    }

    public void saveBookings(List<Ticket> bookings) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKINGS_FILE_PATH, false))) {
            for (Ticket ticket : bookings) {
                StringBuilder seatsBuilder = new StringBuilder();
                if (ticket.getBookedSeats() != null && !ticket.getBookedSeats().isEmpty()) {
                    for (int i = 0; i < ticket.getBookedSeats().size(); i++) {
                        seatsBuilder.append(ticket.getBookedSeats().get(i));
                        if (i < ticket.getBookedSeats().size() - 1) {
                            seatsBuilder.append(SEAT_LIST_DELIMITER);
                        }
                    }
                }
                String bookedSeatsString = seatsBuilder.toString();
                writer.println(ticket.getTicketId() + CSV_DELIMITER +
                        ticket.getShowtimeId() + CSV_DELIMITER +
                        ticket.getCustomerEmail() + CSV_DELIMITER +
                        bookedSeatsString + CSV_DELIMITER +
                        ticket.getBookingDate() + CSV_DELIMITER +
                        ticket.getTotalPrice());
            }
        } catch (IOException e) {
            System.err.println("Loi khi ghi file bookings.csv: " + e.getMessage());
        }
    }

    public boolean exportAllBookingsToCSV(List<Ticket> allBookings, List<Showtime> allShowtimes,
            List<Movie> allMovies) {
        if (allBookings == null || allBookings.isEmpty()) {
            System.out.println("Khong co du lieu dat ve de xuat.");
            return false;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String fileName = EXPORTS_DIR + "LichSuDatVe_" + timestamp + ".csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(
                    "MaVe,EmailKhachHang,TenPhim,MaPhim,NgayChieu,GioChieu,PhongChieu,MaSuatChieu,DanhSachGhe,TongTien(VND),NgayDatVe\n");

            for (Ticket ticket : allBookings) {
                Showtime showtime = findShowtimeByIdInternal(ticket.getShowtimeId(), allShowtimes);
                Movie movie = null;
                String movieId = "N/A";
                String movieName = "Khong ro";
                String showDate = "N/A";
                String startTime = "N/A";
                String roomName = "N/A";

                if (showtime != null) {
                    movieId = showtime.getMovieId();
                    movie = findMovieByIdInternal(movieId, allMovies);
                    if (movie != null) {
                        movieName = movie.getMovieName();
                    }
                    showDate = showtime.getShowDate();
                    startTime = showtime.getStartTime();
                    roomName = showtime.getRoomName();
                }

                StringBuilder seatsBuilder = new StringBuilder();
                if (ticket.getBookedSeats() != null && !ticket.getBookedSeats().isEmpty()) {
                    for (int i = 0; i < ticket.getBookedSeats().size(); i++) {
                        seatsBuilder.append(ticket.getBookedSeats().get(i));
                        if (i < ticket.getBookedSeats().size() - 1) {
                            seatsBuilder.append(SEAT_LIST_DELIMITER);
                        }
                    }
                }
                String bookedSeatsString = seatsBuilder.toString();

                writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%.0f,%s\n",
                        escapeCsv(ticket.getTicketId()),
                        escapeCsv(ticket.getCustomerEmail()),
                        escapeCsv(movieName),
                        escapeCsv(movieId),
                        escapeCsv(showDate),
                        escapeCsv(startTime),
                        escapeCsv(roomName),
                        escapeCsv(ticket.getShowtimeId()),
                        escapeCsv(bookedSeatsString),
                        ticket.getTotalPrice(),
                        escapeCsv(ticket.getBookingDate())));
            }
            System.out.println("Xuat du lieu ra file CSV thanh cong: " + fileName);
            return true;
        } catch (IOException e) {
            System.err.println("Loi khi xuat file CSV: " + e.getMessage());
            return false;
        }
    }

    private Showtime findShowtimeByIdInternal(String id, List<Showtime> list) {
        for (Showtime s : list) {
            if (s.getShowtimeId().equals(id))
                return s;
        }
        return null;
    }

    private Movie findMovieByIdInternal(String id, List<Movie> list) {
        for (Movie m : list) {
            if (m.getMovieId().equals(id))
                return m;
        }
        return null;
    }

    private String escapeCsv(String value) {
        if (value == null)
            return "";
        if (value.contains(",") || value.contains("\"")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}
