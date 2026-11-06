package org.example.jpabook.week09_jpa_project.controller;

import org.example.jpabook.week09_jpa_project.entity.Movie;
import org.example.jpabook.week09_jpa_project.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public Movie create(@RequestBody Movie movie) {
        return movieService.create(movie);
    }

    @GetMapping
    public List<Movie> findAll() {
        return movieService.findAll();
    }

    @GetMapping("/search/title")
    public List<Movie> searchByTitle(@RequestParam String title) {
        return movieService.searchByTitle(title);
    }

    @GetMapping("/search/genre-rating")
    public List<Movie> searchByGenreAndRating(
            @RequestParam String genre,
            @RequestParam Double minRating,
            @RequestParam Double maxRating) {
        return movieService.searchByGenreAndRating(
                genre, minRating, maxRating);
    }

    @GetMapping("/search/genre-rating-desc")
    public List<Movie> searchByGenreOrderByRating(@RequestParam String genre) {
        return movieService.searchByGenreOrderByRating(genre);
    }

    @GetMapping("/{id}")
    public Movie findById(@PathVariable Long id) {
        return movieService.findById(id);
    }

    @PutMapping("/{id}")
    public Movie update(@PathVariable Long id, @RequestBody Movie movie) {
        return movieService.update(id, movie);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        movieService.delete(id);
    }
}