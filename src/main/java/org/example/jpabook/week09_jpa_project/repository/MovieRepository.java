package org.example.jpabook.week09_jpa_project.repository;

import org.example.jpabook.week09_jpa_project.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}