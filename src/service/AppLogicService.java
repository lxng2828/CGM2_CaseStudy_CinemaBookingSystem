package service;

import model.Customer;
import model.Movie;
import model.Showtime;
import model.Ticket;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AppLogicService {

    private static final double DEFAULT_PRICE_PER_SEAT = 75000.0;
    private static final String BOOKED_SEAT_MARKER = "XX";

    public AppLogicService() {
    }

    public List<Movie> getAllAvailableMovies(List<Movie> allMovies) {
        if (allMovies == null) {
            System.out.println("Khong co du lieu phim.");
            return new ArrayList<>();
        }
        return allMovies;
    }

    public Movie findMovieById(String movieId, List<Movie> allMovies) {
        if (movieId == null || movieId.trim().isEmpty() || allMovies == null) {
            return null;
        }
        for (Movie movie : allMovies) {
            if (movie.getMovieId().equalsIgnoreCase(movieId.trim())) {
                return movie;
            }
        }
        return null;
    }

    public List<Showtime> getShowtimesForMovie(String movieId, List<Showtime> allShowtimes) {
        List<Showtime> movieShowtimes = new ArrayList<>();
        if (movieId == null || movieId.trim().isEmpty() || allShowtimes == null) {
            return movieShowtimes;
        }

        for (Showtime showtime : allShowtimes) {
            if (showtime.getMovieId().equalsIgnoreCase(movieId.trim())) {
                movieShowtimes.add(showtime);
            }
        }
        return movieShowtimes;
    }

    public Showtime findShowtimeById(String showtimeId, List<Showtime> allShowtimes) {
        if (showtimeId == null || showtimeId.trim().isEmpty() || allShowtimes == null) {
            return null;
        }
        for (Showtime showtime : allShowtimes) {
            if (showtime.getShowtimeId().equalsIgnoreCase(showtimeId.trim())) {
                return showtime;
            }
        }
        return null;
    }

    private int[] getSeatCoordinates(String seatId, int numRows, int numCols) {
        if (seatId == null || seatId.trim().length() < 2) {
            return null;
        }
        seatId = seatId.trim().toUpperCase();

        char rowChar = seatId.charAt(0);
        String colStr = seatId.substring(1);

        if (rowChar < 'A' || rowChar >= ('A' + numRows)) {
            return null;
        }

        int rowIndex = rowChar - 'A';
        int colIndex;
        try {
            colIndex = Integer.parseInt(colStr) - 1;
        } catch (NumberFormatException e) {
            return null;
        }

        if (colIndex < 0 || colIndex >= numCols) {
            return null;
        }

        return new int[] { rowIndex, colIndex };
    }

    public boolean isSeatAvailable(Showtime showtime, String seatId) {
        if (showtime == null || showtime.getSeatMap() == null || seatId == null) {
            return false;
        }

        String[][] seatMap = showtime.getSeatMap();
        int numRows = seatMap.length;
        if (numRows == 0)
            return false;
        int numCols = seatMap[0].length;

        int[] coords = getSeatCoordinates(seatId, numRows, numCols);
        if (coords == null) {
            return false;
        }

        int rowIndex = coords[0];
        int colIndex = coords[1];

        return !BOOKED_SEAT_MARKER.equals(seatMap[rowIndex][colIndex]);
    }

    public boolean areSeatsAvailable(Showtime showtime, List<String> seatIds) {
        if (showtime == null || seatIds == null || seatIds.isEmpty()) {
            return false;
        }
        for (String seatId : seatIds) {
            if (!isSeatAvailable(showtime, seatId)) {
                return false;
            }
        }
        return true;
    }

    public boolean markSeatsAsBooked(Showtime showtime, List<String> seatIds) {
        if (showtime == null || showtime.getSeatMap() == null || seatIds == null || seatIds.isEmpty()) {
            return false;
        }

        String[][] seatMap = showtime.getSeatMap();
        int numRows = seatMap.length;
        if (numRows == 0)
            return false;
        int numCols = seatMap[0].length;

        for (String seatId : seatIds) {
            int[] coords = getSeatCoordinates(seatId, numRows, numCols);
            if (coords == null) {
                return false;
            }
            int rowIndex = coords[0];
            int colIndex = coords[1];

            if (BOOKED_SEAT_MARKER.equals(seatMap[rowIndex][colIndex])) {
                return false;
            }
            seatMap[rowIndex][colIndex] = BOOKED_SEAT_MARKER;
        }
        return true;
    }

    public double calculateTotalPrice(int numberOfSeats) {
        if (numberOfSeats <= 0) {
            return 0.0;
        }
        return numberOfSeats * DEFAULT_PRICE_PER_SEAT;
    }

    public String generateNewTicketId() {
        return "VE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public List<Ticket> getCustomerBookingHistory(User customer, List<Ticket> allBookings) {
        List<Ticket> customerBookings = new ArrayList<>();
        if (customer == null || allBookings == null || !(customer instanceof Customer)) {
            return customerBookings;
        }

        String customerEmail = customer.getEmail();
        for (Ticket ticket : allBookings) {
            if (ticket.getCustomerEmail().equalsIgnoreCase(customerEmail)) {
                customerBookings.add(ticket);
            }
        }
        return customerBookings;
    }

    public List<User> getAllCustomerAccounts(List<User> allUsers) {
        List<User> customerAccounts = new ArrayList<>();
        if (allUsers == null) {
            return customerAccounts;
        }
        for (User user : allUsers) {
            if (user instanceof Customer) {
                customerAccounts.add(user);
            }
        }
        return customerAccounts;
    }

    public List<Ticket> getAllBookings(List<Ticket> allBookings) {
        if (allBookings == null) {
            return new ArrayList<>();
        }
        return allBookings;
    }

}
