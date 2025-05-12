package service;

import model.Movie;
import model.Showtime;

import java.util.ArrayList;
import java.util.List;

public class AppLogicService {

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

}
