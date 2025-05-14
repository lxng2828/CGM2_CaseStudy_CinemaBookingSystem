package ui;

import model.*;
import service.AppLogicService;
import service.AuthService;
import service.DataService;
import util.InputValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleApplication {

    private final Scanner scanner;
    private final DataService dataService;
    private final AuthService authService;
    private final AppLogicService appLogicService;

    private User currentUser;

    private List<User> allUsers;
    private List<Movie> allMovies;
    private List<Showtime> allShowtimes;
    private List<Ticket> allBookings;

    public ConsoleApplication() {
        this.scanner = new Scanner(System.in);
        this.dataService = new DataService();
        this.authService = new AuthService(this.dataService);
        this.appLogicService = new AppLogicService();
        loadInitialData();
    }

    private void loadInitialData() {
        this.allUsers = dataService.loadUsers();
        this.allMovies = dataService.loadMovies();
        this.allShowtimes = dataService.loadShowtimes();
        this.allBookings = dataService.loadBookings();
        System.out.println("Da tai du lieu ban dau.");
    }

    public void run() {
        int choice;
        do {
            if (currentUser == null) {
                showMainMenu();
                choice = getUserChoice();

                switch (choice) {
                    case 1:
                        handleLogin();
                        break;
                    case 2:
                        handleRegister();
                        break;
                    case 0:
                        System.out.println("Tam biet!");
                        break;
                    default:
                        System.out.println("Lua chon khong hop le. Vui long chon lai.");
                }
            } else {
                if (currentUser instanceof Customer) {
                    handleCustomerActions();
                } else if (currentUser instanceof Admin) {
                    handleAdminActions();
                } else {
                    System.out.println("Loi: Loai nguoi dung khong xac dinh. Dang dang xuat.");
                    currentUser = null;
                }
                choice = -1;
            }
        } while (choice != 0 || currentUser != null);

        scanner.close();
    }

    private void showMainMenu() {
        System.out.println("\n===== HE THONG DAT VE XEM PHIM =====");
        System.out.println("1. Dang nhap");
        System.out.println("2. Dang ky (cho Khach hang moi)");
        System.out.println("0. Thoat");
        System.out.print("Chon chuc nang: ");
    }

    private void handleLogin() {
        System.out.println("\n--- Dang Nhap ---");
        System.out.print("Nhap email: ");
        String email = scanner.nextLine();
        System.out.print("Nhap mat khau: ");
        String password = scanner.nextLine();

        this.currentUser = authService.login(email, password, this.allUsers);

        if (this.currentUser != null) {
            System.out.println("Dang nhap thanh cong! Chao mung " + this.currentUser.getEmail());
        }
        waitForEnter();
    }

    private void handleRegister() {
        System.out.println("\n--- Dang Ky Tai Khoan Khach Hang ---");
        System.out.print("Nhap email: ");
        String email = scanner.nextLine();
        System.out.print("Nhap mat khau (it nhat 6 ky tu): ");
        String password = scanner.nextLine();
        System.out.print("Xac nhan mat khau: ");
        String confirmPassword = scanner.nextLine();

        Customer newCustomer = authService.registerCustomer(email, password, confirmPassword, this.allUsers);
        waitForEnter();
    }

    private void handleCustomerActions() {
        int choice;
        do {
            showCustomerMenu();
            choice = getUserChoice();

            switch (choice) {
                case 1:
                    viewMovieList();
                    break;
                case 2:
                    viewMovieDetails();
                    break;
                case 3:
                    handleBookTickets();
                    break;
                case 4:
                    handleViewCustomerBookingHistory();
                    break;
                case 5:
                    handleChangePassword();
                    break;
                case 0:
                    handleLogout();
                    break;
                default:
                    System.out.println("Lua chon khong hop le. Vui long chon lai.");
                    waitForEnter();
            }
        } while (choice != 0);
    }

    private void showCustomerMenu() {
        System.out.println("\n===== Menu Khach Hang (" + currentUser.getEmail() + ") =====");
        System.out.println("1. Xem danh sach phim dang chieu");
        System.out.println("2. Xem chi tiet phim va lich chieu");
        System.out.println("3. Dat ve xem phim");
        System.out.println("4. Xem lich su dat ve cua toi");
        System.out.println("5. Thay doi mat khau");
        System.out.println("0. Dang xuat");
        System.out.print("Chon chuc nang: ");
    }

    private void viewMovieList() {
        System.out.println("\n--- Danh Sach Phim Dang Chieu ---");
        List<Movie> movies = appLogicService.getAllAvailableMovies(this.allMovies);
        if (movies.isEmpty()) {
            System.out.println("Hien tai khong co phim nao.");
        } else {
            for (int i = 0; i < movies.size(); i++) {
                Movie movie = movies.get(i);
                System.out.printf("%d. [%s] %s (%d phut) - The loai: %s\n",
                        i + 1, movie.getMovieId(), movie.getMovieName(),
                        movie.getDurationInMinutes(), movie.getGenre());
            }
        }
    }

    private void viewMovieDetails() {
        System.out.println("\n--- Xem Chi Tiet Phim Va Lich Chieu ---");
        System.out.print("Nhap Ma Phim can xem chi tiet (hoac nhan Enter de bo qua): ");
        String movieId = scanner.nextLine();

        if (InputValidator.isNullOrEmpty(movieId)) {
            return;
        }

        Movie movie = appLogicService.findMovieById(movieId, this.allMovies);

        if (movie == null) {
            System.out.println("Khong tim thay phim voi ma: " + movieId);
        } else {
            System.out.println("\n--- Chi Tiet Phim ---");
            System.out.println("Ma Phim: " + movie.getMovieId());
            System.out.println("Ten Phim: " + movie.getMovieName());
            System.out.println("The Loai: " + movie.getGenre());
            System.out.println("Thoi Luong: " + movie.getDurationInMinutes() + " phut");
            System.out.println("Mo Ta: " + movie.getDescription());

            System.out.println("\n--- Lich Chieu Hien Co ---");
            List<Showtime> showtimes = appLogicService.getShowtimesForMovie(movieId, this.allShowtimes);
            if (showtimes.isEmpty()) {
                System.out.println("Phim nay hien chua co suat chieu.");
            } else {
                for (int i = 0; i < showtimes.size(); i++) {
                    Showtime st = showtimes.get(i);
                    System.out.printf("%d. [%s] Ngay: %s - Gio: %s - Phong: %s\n",
                            i + 1, st.getShowtimeId(), st.getShowDate(), st.getStartTime(), st.getRoomName());
                }
            }
        }
        waitForEnter();
    }

    private void handleBookTickets() {
        System.out.println("\n--- Dat Ve Xem Phim ---");
        viewMovieList();
        if (this.allMovies.isEmpty()) {
            waitForEnter();
            return;
        }
        System.out.print("Chon Ma Phim muon dat ve: ");
        String movieId = scanner.nextLine();
        Movie selectedMovie = appLogicService.findMovieById(movieId, this.allMovies);

        if (selectedMovie == null) {
            System.out.println("Ma phim khong hop le.");
            waitForEnter();
            return;
        }

        System.out.println("\n--- Cac Suat Chieu Cho Phim: " + selectedMovie.getMovieName() + " ---");
        List<Showtime> movieShowtimes = appLogicService.getShowtimesForMovie(movieId, this.allShowtimes);
        if (movieShowtimes.isEmpty()) {
            System.out.println("Phim nay hien khong co suat chieu nao.");
            waitForEnter();
            return;
        }
        for (int i = 0; i < movieShowtimes.size(); i++) {
            Showtime st = movieShowtimes.get(i);
            System.out.printf("%d. [%s] Ngay: %s - Gio: %s - Phong: %s\n",
                    i + 1, st.getShowtimeId(), st.getShowDate(), st.getStartTime(), st.getRoomName());
        }
        System.out.print("Chon Ma Suat Chieu: ");
        String showtimeId = scanner.nextLine();
        Showtime selectedShowtime = appLogicService.findShowtimeById(showtimeId, this.allShowtimes);

        if (selectedShowtime == null || !selectedShowtime.getMovieId().equalsIgnoreCase(movieId)) {
            System.out.println("Ma suat chieu khong hop le cho phim nay.");
            waitForEnter();
            return;
        }

        System.out.println("\n--- Chon Ghe (Phong: " + selectedShowtime.getRoomName() + ") ---");
        selectedShowtime.displaySeatMap();

        List<String> chosenSeats = new ArrayList<>();
        String seatIdInput;
        do {
            System.out.print("Nhap ma ghe muon chon (vi du: A1, B2). Nhan Enter de ket thuc chon ghe: ");
            seatIdInput = scanner.nextLine().toUpperCase();
            if (InputValidator.isNullOrEmpty(seatIdInput)) {
                break;
            }
            if (appLogicService.isSeatAvailable(selectedShowtime, seatIdInput)) {
                if (!chosenSeats.contains(seatIdInput)) {
                    chosenSeats.add(seatIdInput);
                    System.out.println("Da them ghe: " + seatIdInput);
                } else {
                    System.out.println("Ghe " + seatIdInput + " da duoc ban chon truoc do.");
                }
            } else {
                System.out.println("Ghe '" + seatIdInput + "' khong kha dung hoac khong hop le. Vui long chon lai.");
            }
        } while (true);

        if (chosenSeats.isEmpty()) {
            System.out.println("Ban chua chon ghe nao. Huy dat ve.");
            waitForEnter();
            return;
        }

        System.out.println("\n--- Xac Nhan Dat Ve ---");
        System.out.println("Phim: " + selectedMovie.getMovieName());
        System.out.println("Suat chieu: " + selectedShowtime.getShowDate() + " luc " + selectedShowtime.getStartTime()
                + " - Phong: " + selectedShowtime.getRoomName());
        System.out.print("Cac ghe da chon: ");
        for (int i = 0; i < chosenSeats.size(); i++) {
            System.out.print(chosenSeats.get(i) + (i < chosenSeats.size() - 1 ? ", " : ""));
        }
        System.out.println();
        double totalPrice = appLogicService.calculateTotalPrice(chosenSeats.size());
        System.out.printf("Tong tien: %.0f VND\n", totalPrice);

        System.out.print("Ban co chac chan muon dat ve? (C/K): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("C")) {
            if (!appLogicService.areSeatsAvailable(selectedShowtime, chosenSeats)) {
                System.out.println(
                        "Dat ve that bai do mot hoac nhieu ghe da duoc dat trong luc ban chon. Vui long thu lai.");
                waitForEnter();
                return;
            }

            boolean bookingSuccess = appLogicService.markSeatsAsBooked(selectedShowtime, chosenSeats);
            if (bookingSuccess) {
                String ticketId = appLogicService.generateNewTicketId();
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String bookingDateTime = sdf.format(new java.util.Date());

                Ticket newTicket = new Ticket(ticketId, selectedShowtime.getShowtimeId(), currentUser.getEmail(),
                        chosenSeats, bookingDateTime, totalPrice);
                this.allBookings.add(newTicket);

                dataService.saveShowtimes(this.allShowtimes);
                dataService.saveBookings(this.allBookings);

                System.out.println("Dat ve thanh cong! Ma ve cua ban la: " + ticketId);
                System.out.println("Cam on ban da su dung dich vu!");
            } else {
                System.out.println("Dat ve that bai do co loi xay ra khi cap nhat ghe. Vui long thu lai.");
            }
        } else {
            System.out.println("Da huy dat ve.");
        }
        waitForEnter();
    }

    private void handleViewCustomerBookingHistory() {
        System.out.println("\n--- Lich Su Dat Ve Cua Ban ---");
        List<Ticket> customerBookings = appLogicService.getCustomerBookingHistory(this.currentUser, this.allBookings);

        if (customerBookings.isEmpty()) {
            System.out.println("Ban chua dat ve nao.");
        } else {
            for (Ticket ticket : customerBookings) {
                Showtime showtime = appLogicService.findShowtimeById(ticket.getShowtimeId(), this.allShowtimes);
                Movie movie = null;
                if (showtime != null) {
                    movie = appLogicService.findMovieById(showtime.getMovieId(), this.allMovies);
                }

                System.out.println("--------------------");
                System.out.println("Ma Ve: " + ticket.getTicketId());
                System.out.println("Phim: " + (movie != null ? movie.getMovieName() : "Khong ro"));
                if (showtime != null) {
                    System.out.println("Suat: " + showtime.getShowDate() + " luc " + showtime.getStartTime()
                            + " - Phong: " + showtime.getRoomName());
                } else {
                    System.out.println("Suat: Khong ro (Ma: " + ticket.getShowtimeId() + ")");
                }
                System.out.print("Ghe: ");
                for (int i = 0; i < ticket.getBookedSeats().size(); i++) {
                    System.out.print(
                            ticket.getBookedSeats().get(i) + (i < ticket.getBookedSeats().size() - 1 ? ", " : ""));
                }
                System.out.println();
                System.out.printf("Tong Tien: %.0f VND\n", ticket.getTotalPrice());
                System.out.println("Ngay Dat: " + ticket.getBookingDate());
            }
            System.out.println("--------------------");
        }
        waitForEnter();
    }

    private void handleChangePassword() {
        if (currentUser == null) {
            System.out.println("Ban can dang nhap de thay doi mat khau.");
            waitForEnter();
            return;
        }

        System.out.println("\n--- Thay Doi Mat Khau ---");
        System.out.print("Nhap mat khau cu: ");
        String oldPassword = scanner.nextLine();

        System.out.print("Nhap mat khau moi (it nhat 6 ky tu): ");
        String newPassword = scanner.nextLine();

        System.out.print("Xac nhan mat khau moi: ");
        String confirmNewPassword = scanner.nextLine();

        boolean success = appLogicService.changePassword(currentUser, oldPassword, newPassword, confirmNewPassword);

        if (success) {
            dataService.saveUsers(this.allUsers);
        }
        waitForEnter();
    }

    private void handleAdminActions() {
        int choice;
        do {
            showAdminMenu();
            choice = getUserChoice();

            switch (choice) {
                case 1:
                    handleManageMovies();
                    break;
                case 2:
                    handleManageShowtimes();
                    break;
                case 3:
                    handleViewCustomerAccounts();
                    break;
                case 4:
                    handleViewAllBookings();
                    break;
                case 5:
                    handleExportBookingsToCSV();
                    break;
                case 0:
                    handleLogout();
                    break;
                default:
                    System.out.println("Lua chon khong hop le. Vui long chon lai.");
                    waitForEnter();
            }
        } while (choice != 0);
    }

    private void showAdminMenu() {
        System.out.println("\n===== Menu Quan Tri Vien (" + currentUser.getEmail() + ") =====");
        System.out.println("1. Quan ly Phim");
        System.out.println("2. Quan ly Suat chieu");
        System.out.println("3. Quan ly Tai khoan Khach hang");
        System.out.println("4. Xem Toan Bo Lich Su Dat Ve");
        System.out.println("5. Xuat Lich Su Dat Ve ra CSV");
        System.out.println("0. Dang xuat");
        System.out.print("Chon chuc nang: ");
    }

    private void handleLogout() {
        System.out.println("Dang dang xuat...");
        this.currentUser = null;
        System.out.println("Da dang xuat.");
        waitForEnter();
    }

    private void handleManageMovies() {
        int choice;
        do {
            System.out.println("\n--- Quan Ly Phim ---");
            System.out.println("1. Them phim moi");
            System.out.println("2. Sua thong tin phim");
            System.out.println("3. Xoa phim");
            System.out.println("4. Hien thi danh sach phim");
            System.out.println("0. Quay lai Menu Quan Tri");
            System.out.print("Chon chuc nang: ");
            choice = getUserChoice();

            switch (choice) {
                case 1:
                    handleAddMovie();
                    break;
                case 2:
                    handleEditMovie();
                    break;
                case 3:
                    handleDeleteMovie();
                    break;
                case 4:
                    viewMovieList();
                    waitForEnter();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lua chon khong hop le.");
                    waitForEnter();
            }
        } while (choice != 0);
    }

    private void handleAddMovie() {
        System.out.println("\n--- Them Phim Moi ---");
        System.out.print("Nhap Ma Phim (duy nhat): ");
        String movieId = scanner.nextLine();
        if (InputValidator.isNullOrEmpty(movieId) || appLogicService.findMovieById(movieId, this.allMovies) != null) {
            System.out.println("Ma phim khong hop le hoac da ton tai.");
            waitForEnter();
            return;
        }

        System.out.print("Nhap Ten Phim: ");
        String movieName = scanner.nextLine();
        if (InputValidator.isNullOrEmpty(movieName)) {
            System.out.println("Ten phim khong duoc de trong.");
            waitForEnter();
            return;
        }

        System.out.print("Nhap The Loai: ");
        String genre = scanner.nextLine();

        System.out.print("Nhap Thoi Luong (phut): ");
        String durationStr = scanner.nextLine();
        if (!InputValidator.isValidPositiveInteger(durationStr)) {
            System.out.println("Thoi luong khong hop le.");
            waitForEnter();
            return;
        }
        int duration = Integer.parseInt(durationStr);

        System.out.print("Nhap Mo Ta: ");
        String description = scanner.nextLine();

        Movie newMovie = new Movie(movieId, movieName, genre, duration, description);
        this.allMovies.add(newMovie);
        dataService.saveMovies(this.allMovies);
        System.out.println("Da them phim moi thanh cong!");
        waitForEnter();
    }

    private void handleEditMovie() {
        System.out.println("\n--- Sua Thong Tin Phim ---");
        viewMovieList();
        if (this.allMovies.isEmpty()) {
            waitForEnter();
            return;
        }
        System.out.print("Nhap Ma Phim can sua: ");
        String movieId = scanner.nextLine();

        Movie movieToEdit = appLogicService.findMovieById(movieId, this.allMovies);
        if (movieToEdit == null) {
            System.out.println("Khong tim thay phim voi ma: " + movieId);
            waitForEnter();
            return;
        }

        System.out.println("Dang sua phim: " + movieToEdit.getMovieName());
        System.out.print("Nhap Ten Phim moi (Enter de bo qua): ");
        String newMovieName = scanner.nextLine();
        if (!InputValidator.isNullOrEmpty(newMovieName)) {
            movieToEdit.setMovieName(newMovieName);
        }

        System.out.print("Nhap The Loai moi (Enter de bo qua): ");
        String newGenre = scanner.nextLine();
        if (!InputValidator.isNullOrEmpty(newGenre)) {
            movieToEdit.setGenre(newGenre);
        }

        System.out.print("Nhap Thoi Luong moi (phut, Enter de bo qua): ");
        String newDurationStr = scanner.nextLine();
        if (!InputValidator.isNullOrEmpty(newDurationStr)) {
            if (InputValidator.isValidPositiveInteger(newDurationStr)) {
                movieToEdit.setDurationInMinutes(Integer.parseInt(newDurationStr));
            } else {
                System.out.println("Thoi luong nhap vao khong hop le, khong thay doi.");
            }
        }

        System.out.print("Nhap Mo Ta moi (Enter de bo qua): ");
        String newDescription = scanner.nextLine();
        if (!InputValidator.isNullOrEmpty(newDescription)) {
            movieToEdit.setDescription(newDescription);
        }

        dataService.saveMovies(this.allMovies);
        System.out.println("Da cap nhat thong tin phim thanh cong!");
        waitForEnter();
    }

    private void handleDeleteMovie() {
        System.out.println("\n--- Xoa Phim ---");
        viewMovieList();
        if (this.allMovies.isEmpty()) {
            waitForEnter();
            return;
        }
        System.out.print("Nhap Ma Phim can xoa: ");
        String movieId = scanner.nextLine();

        Movie movieToDelete = appLogicService.findMovieById(movieId, this.allMovies);
        if (movieToDelete == null) {
            System.out.println("Khong tim thay phim voi ma: " + movieId);
            waitForEnter();
            return;
        }

        List<Showtime> showtimesForMovie = appLogicService.getShowtimesForMovie(movieId, this.allShowtimes);
        if (!showtimesForMovie.isEmpty()) {
            System.out.println("Phim nay van con suat chieu. Ban can xoa cac suat chieu cua phim nay truoc.");
            System.out.print("Ban co muon xoa tat ca suat chieu cua phim nay khong? (C/K): ");
            String confirmDeleteShowtimes = scanner.nextLine();
            if (confirmDeleteShowtimes.equalsIgnoreCase("C")) {
                List<Showtime> remainingShowtimes = new ArrayList<>();
                for (Showtime st : this.allShowtimes) {
                    if (!st.getMovieId().equalsIgnoreCase(movieId)) {
                        remainingShowtimes.add(st);
                    }
                }
                this.allShowtimes = remainingShowtimes;
                dataService.saveShowtimes(this.allShowtimes);
                System.out.println("Da xoa tat ca suat chieu cua phim: " + movieToDelete.getMovieName());
            } else {
                System.out.println("Huy xoa phim.");
                waitForEnter();
                return;
            }
        }

        System.out.print(
                "Ban co chac chan muon xoa phim '" + movieToDelete.getMovieName() + "' (Ma: " + movieId + ")? (C/K): ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("C")) {
            this.allMovies.remove(movieToDelete);
            dataService.saveMovies(this.allMovies);
            System.out.println("Da xoa phim thanh cong!");
        } else {
            System.out.println("Da huy xoa phim.");
        }
        waitForEnter();
    }

    private void handleManageShowtimes() {
        int choice;
        do {
            System.out.println("\n--- Quan Ly Suat Chieu ---");
            System.out.println("1. Them suat chieu moi");
            System.out.println("2. Sua thong tin suat chieu");
            System.out.println("3. Xoa suat chieu");
            System.out.println("4. Hien thi danh sach suat chieu (theo phim)");
            System.out.println("5. Hien thi tat ca suat chieu");
            System.out.println("0. Quay lai Menu Quan Tri");
            System.out.print("Chon chuc nang: ");
            choice = getUserChoice();

            switch (choice) {
                case 1:
                    handleAddShowtime();
                    break;
                case 2:
                    handleEditShowtime();
                    break;
                case 3:
                    handleDeleteShowtime();
                    break;
                case 4:
                    handleViewShowtimesByMovie();
                    break;
                case 5:
                    handleViewAllShowtimes();
                    waitForEnter();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lua chon khong hop le.");
                    waitForEnter();
            }
        } while (choice != 0);
    }

    private void handleAddShowtime() {
        System.out.println("\n--- Them Suat Chieu Moi ---");
        viewMovieList();
        if (this.allMovies.isEmpty()) {
            System.out.println("Khong co phim nao de them suat chieu.");
            waitForEnter();
            return;
        }
        System.out.print("Nhap Ma Phim cho suat chieu: ");
        String movieId = scanner.nextLine();
        Movie movieForShowtime = appLogicService.findMovieById(movieId, this.allMovies);
        if (movieForShowtime == null) {
            System.out.println("Ma phim khong ton tai.");
            waitForEnter();
            return;
        }

        System.out.print("Nhap Ma Suat Chieu (duy nhat, vi du S001, S002,...): ");
        String showtimeId = scanner.nextLine();
        if (InputValidator.isNullOrEmpty(showtimeId)
                || appLogicService.findShowtimeById(showtimeId, this.allShowtimes) != null) {
            System.out.println("Ma suat chieu khong hop le hoac da ton tai.");
            waitForEnter();
            return;
        }

        System.out.print("Nhap Ngay Chieu (dd/MM/yyyy): ");
        String showDate = scanner.nextLine();
        if (!InputValidator.isValidDateFormat(showDate)) {
            System.out.println("Dinh dang ngay chieu khong hop le.");
            waitForEnter();
            return;
        }

        System.out.print("Nhap Gio Chieu (HH:mm): ");
        String startTime = scanner.nextLine();
        if (!InputValidator.isValidTimeFormat(startTime)) {
            System.out.println("Dinh dang gio chieu khong hop le.");
            waitForEnter();
            return;
        }

        System.out.print("Nhap Ten Phong Chieu: ");
        String roomName = scanner.nextLine();
        if (InputValidator.isNullOrEmpty(roomName)) {
            System.out.println("Ten phong chieu khong duoc de trong.");
            waitForEnter();
            return;
        }

        System.out.print("Nhap So Hang Ghe: ");
        String rowsStr = scanner.nextLine();
        if (!InputValidator.isValidPositiveInteger(rowsStr)) {
            System.out.println("So hang ghe khong hop le.");
            waitForEnter();
            return;
        }
        int rows = Integer.parseInt(rowsStr);

        System.out.print("Nhap So Cot Ghe moi hang: ");
        String colsStr = scanner.nextLine();
        if (!InputValidator.isValidPositiveInteger(colsStr)) {
            System.out.println("So cot ghe khong hop le.");
            waitForEnter();
            return;
        }
        int cols = Integer.parseInt(colsStr);

        Showtime newShowtime = new Showtime(showtimeId, movieId, showDate, startTime, roomName, rows, cols);
        this.allShowtimes.add(newShowtime);
        dataService.saveShowtimes(this.allShowtimes);
        System.out.println("Da them suat chieu moi thanh cong!");
        waitForEnter();
    }

    private void handleEditShowtime() {
        System.out.println("\n--- Sua Thong Tin Suat Chieu ---");
        handleViewAllShowtimes();
        if (this.allShowtimes.isEmpty()) {
            waitForEnter();
            return;
        }

        System.out.print("Nhap Ma Suat Chieu can sua: ");
        String showtimeId = scanner.nextLine();
        Showtime showtimeToEdit = appLogicService.findShowtimeById(showtimeId, this.allShowtimes);

        if (showtimeToEdit == null) {
            System.out.println("Khong tim thay suat chieu voi ma: " + showtimeId);
            waitForEnter();
            return;
        }

        System.out.println(
                "Dang sua suat chieu: " + showtimeToEdit.getShowtimeId() + " cho phim " + showtimeToEdit.getMovieId());

        System.out.print("Nhap Ngay Chieu moi (dd/MM/yyyy, Enter de bo qua): ");
        String newShowDate = scanner.nextLine();
        if (!InputValidator.isNullOrEmpty(newShowDate)) {
            if (InputValidator.isValidDateFormat(newShowDate)) {
                showtimeToEdit.setShowDate(newShowDate);
            } else {
                System.out.println("Dinh dang ngay chieu khong hop le, khong thay doi.");
            }
        }

        System.out.print("Nhap Gio Chieu moi (HH:mm, Enter de bo qua): ");
        String newStartTime = scanner.nextLine();
        if (!InputValidator.isNullOrEmpty(newStartTime)) {
            if (InputValidator.isValidTimeFormat(newStartTime)) {
                showtimeToEdit.setStartTime(newStartTime);
            } else {
                System.out.println("Dinh dang gio chieu khong hop le, khong thay doi.");
            }
        }

        System.out.print("Nhap Ten Phong Chieu moi (Enter de bo qua): ");
        String newRoomName = scanner.nextLine();
        if (!InputValidator.isNullOrEmpty(newRoomName)) {
            showtimeToEdit.setRoomName(newRoomName);
        }

        dataService.saveShowtimes(this.allShowtimes);
        System.out.println("Da cap nhat thong tin suat chieu thanh cong!");
        waitForEnter();
    }

    private void handleDeleteShowtime() {
        System.out.println("\n--- Xoa Suat Chieu ---");
        handleViewAllShowtimes();
        if (this.allShowtimes.isEmpty()) {
            waitForEnter();
            return;
        }
        System.out.print("Nhap Ma Suat Chieu can xoa: ");
        String showtimeId = scanner.nextLine();

        Showtime showtimeToDelete = appLogicService.findShowtimeById(showtimeId, this.allShowtimes);
        if (showtimeToDelete == null) {
            System.out.println("Khong tim thay suat chieu voi ma: " + showtimeId);
            waitForEnter();
            return;
        }

        boolean hasBookings = false;
        for (Ticket ticket : this.allBookings) {
            if (ticket.getShowtimeId().equalsIgnoreCase(showtimeId)) {
                hasBookings = true;
                break;
            }
        }

        if (hasBookings) {
            System.out.println("Suat chieu nay da co ve duoc dat. Ban khong the xoa truc tiep.");
            waitForEnter();
            return;
        }

        System.out.print("Ban co chac chan muon xoa suat chieu [" + showtimeToDelete.getShowtimeId() + "] cho phim ["
                + showtimeToDelete.getMovieId() + "]? (C/K): ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("C")) {
            this.allShowtimes.remove(showtimeToDelete);
            dataService.saveShowtimes(this.allShowtimes);
            System.out.println("Da xoa suat chieu thanh cong!");
        } else {
            System.out.println("Da huy xoa suat chieu.");
        }
        waitForEnter();
    }

    private void handleViewShowtimesByMovie() {
        System.out.println("\n--- Hien Thi Suat Chieu Theo Phim ---");
        viewMovieList();
        if (this.allMovies.isEmpty()) {
            waitForEnter();
            return;
        }
        System.out.print("Nhap Ma Phim de xem cac suat chieu: ");
        String movieId = scanner.nextLine();
        Movie selectedMovie = appLogicService.findMovieById(movieId, this.allMovies);

        if (selectedMovie == null) {
            System.out.println("Ma phim khong ton tai.");
            waitForEnter();
            return;
        }

        System.out.println("\n--- Cac Suat Chieu Cho Phim: " + selectedMovie.getMovieName() + " ---");
        List<Showtime> movieShowtimes = appLogicService.getShowtimesForMovie(movieId, this.allShowtimes);
        if (movieShowtimes.isEmpty()) {
            System.out.println("Phim nay hien khong co suat chieu nao.");
        } else {
            for (int i = 0; i < movieShowtimes.size(); i++) {
                Showtime st = movieShowtimes.get(i);
                System.out.printf("%d. [%s] Ngay: %s - Gio: %s - Phong: %s\n",
                        i + 1, st.getShowtimeId(), st.getShowDate(), st.getStartTime(), st.getRoomName());
            }
        }
        waitForEnter();
    }

    private void handleViewAllShowtimes() {
        System.out.println("\n--- Danh Sach Tat Ca Suat Chieu ---");
        if (this.allShowtimes.isEmpty()) {
            System.out.println("Khong co suat chieu nao trong he thong.");
            return;
        }
        for (int i = 0; i < this.allShowtimes.size(); i++) {
            Showtime st = this.allShowtimes.get(i);
            Movie movie = appLogicService.findMovieById(st.getMovieId(), this.allMovies);
            String movieName = (movie != null) ? movie.getMovieName() : "Khong ro";
            System.out.printf("%d. [%s] Phim: %s (%s) - Ngay: %s - Gio: %s - Phong: %s\n",
                    i + 1,
                    st.getShowtimeId(),
                    movieName,
                    st.getMovieId(),
                    st.getShowDate(),
                    st.getStartTime(),
                    st.getRoomName());
        }
    }

    private void handleViewCustomerAccounts() {
        System.out.println("\n--- Danh Sach Tai Khoan Khach Hang ---");
        List<User> customerAccounts = appLogicService.getAllCustomerAccounts(this.allUsers);

        if (customerAccounts.isEmpty()) {
            System.out.println("Khong co tai khoan khach hang nao trong he thong.");
        } else {
            System.out.println("Tong so tai khoan: " + customerAccounts.size());
            System.out.println("-------------------------------------");
            for (int i = 0; i < customerAccounts.size(); i++) {
                User customer = customerAccounts.get(i);
                System.out.printf("%d. Email: %s\n", i + 1, customer.getEmail());
            }
            System.out.println("-------------------------------------");
        }
        waitForEnter();
    }

    private void handleViewAllBookings() {
        System.out.println("\n--- Toan Bo Lich Su Dat Ve ---");
        List<Ticket> allBookingsList = appLogicService.getAllBookings(this.allBookings);

        if (allBookingsList.isEmpty()) {
            System.out.println("Chua co ve nao duoc dat trong he thong.");
        } else {
            System.out.println("Tong so ve da dat: " + allBookingsList.size());
            System.out.println("=====================================");
            for (Ticket ticket : allBookingsList) {
                Showtime showtime = appLogicService.findShowtimeById(ticket.getShowtimeId(), this.allShowtimes);
                Movie movie = null;
                if (showtime != null) {
                    movie = appLogicService.findMovieById(showtime.getMovieId(), this.allMovies);
                }

                System.out.println("Ma Ve: " + ticket.getTicketId());
                System.out.println("Email Khach Hang: " + ticket.getCustomerEmail());
                System.out.println("Phim: " + (movie != null ? movie.getMovieName() : "Khong ro") + " (Ma: "
                        + (showtime != null ? showtime.getMovieId() : "N/A") + ")");
                if (showtime != null) {
                    System.out.println("Suat: " + showtime.getShowDate() + " luc " + showtime.getStartTime()
                            + " - Phong: " + showtime.getRoomName() + " (Ma: " + ticket.getShowtimeId() + ")");
                } else {
                    System.out.println("Suat: Khong ro (Ma Suat: " + ticket.getShowtimeId() + ")");
                }
                System.out.print("Ghe: ");
                for (int i = 0; i < ticket.getBookedSeats().size(); i++) {
                    System.out.print(
                            ticket.getBookedSeats().get(i) + (i < ticket.getBookedSeats().size() - 1 ? ", " : ""));
                }
                System.out.println();
                System.out.printf("Tong Tien: %.0f VND\n", ticket.getTotalPrice());
                System.out.println("Ngay Dat: " + ticket.getBookingDate());
                System.out.println("-------------------------------------");
            }
            System.out.println("=====================================");
        }
        waitForEnter();
    }

    private void handleExportBookingsToCSV() {
        System.out.println("\n--- Xuat Lich Su Dat Ve ra CSV ---");
        System.out.print("Ban co chac chan muon xuat toan bo lich su dat ve ra file CSV? (C/K): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("C")) {
            boolean exportSuccess = dataService.exportAllBookingsToCSV(this.allBookings, this.allShowtimes,
                    this.allMovies);
        } else {
            System.out.println("Da huy thao tac xuat file.");
        }
        waitForEnter();
    }

    private int getUserChoice() {
        int choice = -1;
        String input = scanner.nextLine();
        if (InputValidator.isValidInteger(input)) {
            choice = Integer.parseInt(input);
        } else {
            if (!InputValidator.isNullOrEmpty(input)) {
                System.out.println("Vui long nhap mot so.");
            }
        }
        return choice;
    }

    private void waitForEnter() {
        System.out.print("\nNhan Enter de tiep tuc...");
        scanner.nextLine();
    }
}
