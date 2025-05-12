package model;

public class Showtime {
    private String showtimeId;
    private String movieId;
    private String showDate;
    private String startTime;
    private String roomName;
    private String[][] seatMap;

    public Showtime() {
    }

    public Showtime(String showtimeId, String movieId, String showDate, String startTime, String roomName, int rows,
            int cols) {
        this.showtimeId = showtimeId;
        this.movieId = movieId;
        this.showDate = showDate;
        this.startTime = startTime;
        this.roomName = roomName;
        this.seatMap = new String[rows][cols];
        initializeSeatMap();
    }

    private void initializeSeatMap() {
        if (this.seatMap == null) {
            return;
        }
        for (int i = 0; i < this.seatMap.length; i++) {
            char rowChar = (char) ('A' + i);
            for (int j = 0; j < this.seatMap[i].length; j++) {
                this.seatMap[i][j] = "" + rowChar + (j + 1);
            }
        }
    }

    public String getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(String showtimeId) {
        this.showtimeId = showtimeId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String[][] getSeatMap() {
        return seatMap;
    }

    public void setSeatMap(String[][] seatMap) {
        this.seatMap = seatMap;
    }

    public void displaySeatMap() {
        System.out.println("So do ghe phong " + this.roomName + ":");
        System.out.println("------------------------------");
        System.out.println("          MAN HINH          ");
        System.out.println("------------------------------");
        if (this.seatMap == null || this.seatMap.length == 0) {
            System.out.println("Khong co thong tin so do ghe.");
            return;
        }
        for (int i = 0; i < this.seatMap.length; i++) {
            for (int j = 0; j < this.seatMap[i].length; j++) {
                System.out.printf("%-5s", this.seatMap[i][j] == null ? "[ ]" : "[" + this.seatMap[i][j] + "]");
            }
            System.out.println();
        }
        System.out.println("------------------------------");
        System.out.println("Ghi chu: [XX] la ghe da dat.");
    }

    @Override
    public String toString() {
        return "Suat chieu [" +
                "Ma: '" + showtimeId + '\'' +
                ", Ma Phim: '" + movieId + '\'' +
                ", Ngay: '" + showDate + '\'' +
                ", Gio: '" + startTime + '\'' +
                ", Phong: '" + roomName + '\'' +
                ']';
    }
}
