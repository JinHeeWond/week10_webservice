package org.example.jpabook.week09_jpa_project.repository;

import org.example.jpabook.week09_jpa_project.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    /*
     * Q3. JpaRepository<Movie, Long>에서 Movie는 관리할 Entity 타입이고,
     * Long은 Movie의 기본 키 타입이다.
     *
     * Q4. JpaRepository가 제공하는 기본 메서드
     * save(movie): Entity를 새로 저장하거나 기존 데이터를 수정한다.
     * findById(id): 기본 키로 Entity 한 건을 조회한다.
     * findAll(): 전체 Entity를 조회한다.
     * deleteById(id): 기본 키에 해당하는 Entity를 삭제한다.
     * count(): 전체 Entity 개수를 반환한다.
     *
     * Q5. findById()는 데이터가 없을 수도 있으므로,
     * 결과가 없음을 안전하게 표현하기 위해 Optional<Movie>를 반환한다.
     *
     * Q6. 기본 메서드는 JpaRepository가 미리 제공한다.
     * Query Method는 개발자가 메서드 이름에 검색 조건을 작성하면
     * Spring Data JPA가 그 이름을 분석해 조회 쿼리를 만든다.
     */

        // 제목에 검색어가 포함된 영화를 찾는다.
        List<Movie> findByTitleContainingIgnoreCase(String title);

        // 장르가 같고 평점이 지정한 범위에 있는 영화를 찾는다.
        List<Movie> findByGenreAndRatingBetween(
                String genre, Double minRating, Double maxRating);

        // 해당 장르의 영화를 평점 높은 순으로 찾는다.
        List<Movie> findByGenreOrderByRatingDesc(String genre);


}