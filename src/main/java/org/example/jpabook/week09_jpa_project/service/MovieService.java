package org.example.jpabook.week09_jpa_project.service;

import org.example.jpabook.week09_jpa_project.entity.Movie;
import org.example.jpabook.week09_jpa_project.repository.MovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie create(Movie movie) {
        movie.setId(null);
        // Q7.새 Entity의 id를 null로 두고 save()를 호출하면 Spring Data JPA가 새 데이터로 판단해서 데이터베이스에 등록한다.
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

    @Transactional
    public Movie update(Long id, Movie request) {
        Movie movie = findById(id);
        movie.setTitle(request.getTitle());
        movie.setDirector(request.getDirector());
        movie.setGenre(request.getGenre());
        movie.setRating(request.getRating());
        movie.setReleaseYear(request.getReleaseYear());

        // 트랜잭션이 끝날 때 JPA가 변경된 값을 감지해 DB에 반영한다.
        // 따라서 Dirty Checking 방식에서는 save()를 호출하지 않는다.
        return movie;
    }

    public void delete(Long id) {
        findById(id); // 없는 ID면 404 응답
        movieRepository.deleteById(id);
    }

    public List<Movie> searchByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Movie> searchByGenreAndRating(
            String genre, Double minRating, Double maxRating) {
        return movieRepository.findByGenreAndRatingBetween(
                genre, minRating, maxRating);
    }

    public List<Movie> searchByGenreOrderByRating(String genre) {
        return movieRepository.findByGenreOrderByRatingDesc(genre);
    }
}