package ui;

import model.*;
import service.AppLogicService;
import service.AuthService;
import service.DataService;

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

        if (newCustomer != null) {
        }
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
                    System.out.println("Chuc nang 'Dat ve xem phim' chua duoc trien khai.");
                    waitForEnter();
                    break;
                case 4:
                    System.out.println("Chuc nang 'Xem lich su dat ve ca nhan' chua duoc trien khai.");
                    waitForEnter();
                    break;
                case 5:
                    System.out.println("Chuc nang 'Thay doi mat khau' chua duoc trien khai.");
                    waitForEnter();
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
        waitForEnter();
    }

    private void viewMovieDetails() {
        System.out.println("\n--- Xem Chi Tiet Phim Va Lich Chieu ---");
        System.out.print("Nhap Ma Phim can xem chi tiet: ");
        String movieId = scanner.nextLine();

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

    private void handleAdminActions() {
        int choice;
        do {
            showAdminMenu();
            choice = getUserChoice();

            switch (choice) {
                case 1:
                    System.out.println("Chuc nang 'Quan ly Phim' chua duoc trien khai.");
                    waitForEnter();
                    break;
                case 2:
                    System.out.println("Chuc nang 'Quan ly Suat chieu' chua duoc trien khai.");
                    waitForEnter();
                    break;
                case 3:
                    System.out.println("Chuc nang 'Quan ly Tai khoan Khach hang' chua duoc trien khai.");
                    waitForEnter();
                    break;
                case 4:
                    System.out.println("Chuc nang 'Xem Toan Bo Lich Su Dat Ve' chua duoc trien khai.");
                    waitForEnter();
                    break;
                case 5:
                    System.out.println("Chuc nang 'Xuat Lich Su Dat Ve ra CSV' chua duoc trien khai.");
                    waitForEnter();
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

    private int getUserChoice() {
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Vui long nhap mot so.");
        }
        return choice;
    }

    private void waitForEnter() {
        System.out.print("\nNhan Enter de tiep tuc...");
        scanner.nextLine();
    }

}
