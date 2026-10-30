package org.example.jpabook.week09_jpa_project.service;

import org.example.jpabook.week09_jpa_project.entity.Movie;
import org.example.jpabook.week09_jpa_project.repository.MovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie create(Movie movie) {
        movie.setId(null);
        return movieRepository.save(movie);
    }

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public Movie findById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Movie not found: " + id
                ));
    }

    public Movie update(Long id, Movie request) {
        Movie movie = findById(id);
        movie.setTitle(request.getTitle());
        movie.setDirector(request.getDirector());
        movie.setGenre(request.getGenre());
        movie.setRating(request.getRating());
        movie.setReleaseYear(request.getReleaseYear());

        return movieRepository.save(movie);
    }

    public void delete(Long id) {
        findById(id); // 없는 ID면 404 응답
        movieRepository.deleteById(id);
    }
}