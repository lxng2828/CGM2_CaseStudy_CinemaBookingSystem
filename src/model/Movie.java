package model;

public class Movie {
    private String movieId;
    private String movieName;
    private String genre;
    private int durationInMinutes;
    private String description;

    public Movie() {
    }

    public Movie(String movieId, String movieName, String genre, int durationInMinutes, String description) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.genre = genre;
        this.durationInMinutes = durationInMinutes;
        this.description = description;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Phim [" +
                "Ma: '" + movieId + '\'' +
                ", Ten: '" + movieName + '\'' +
                ", The Loai: '" + genre + '\'' +
                ", Thoi Luong: " + durationInMinutes + " phut" +
                ", Mo Ta: '" + description + '\'' +
                ']';
    }
}
