1. 프로젝트 소개
   
   - 주제 및 주요 기능
   Spring Data JPA, MariaDB를 사용해서 영화의 정보를 CRUD하는 REST API입니다.

  - Entity 필드 구조
    | 필드 | 자료형 | 설명 |
    | id | Long | 영화 ID, 자동 생성 |
    | title | String | 영화 제목 |
    | director | String | 감독 |
    | genre | String | 장르 |
    | rating | Double | 평점 |
    | releaseYear | Integer | 개봉 연도 |

  - 프로젝트 구조 및 개발환경
    entity:       Movie
    repository:   MovieRepository
    service:      MovieService
    controller:   MovieController
    개발 환경: Java 17, Spring Boot, Gradle, Spring Data JPA, MariaDB

  - url
    GitHub organization Repository: https://github.com/2026-2-WebService/assign09-c01-22300743
    개인 Repository: https://github.com/JinHeeWond/week9_webservice
    Render: https://week9-webservice.onrender.com/

2. Database SQL Practice

   -테이블에 데이터 추가
   DBeaver에서 기존 테이블에
   prduct_id: 6, name: JPA 실습용 키보드, price: 30000, category: 실습, stock: 5
   를 추가했습니다.

   -SQL 실습 테이블 생성
   기존 테이블과 이름이 겹치지 않도록 `movie_sql` 테이블을 생성했습니다.
   CREATE TABLE movie_sql (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       title VARCHAR(100) NOT NULL,
       director VARCHAR(100),
       genre VARCHAR(50),
       rating DOUBLE
   }
   실행 결과: `movie_sql` 테이블이 생성되었습니다.

   -INSERT
   INSERT INTO movie_sql(title, director, genre, rating)
      VALUES
       ('Inception', 'Nolan', 'SF', 9.1),
       ('Interstellar', 'Nolan', 'SF', 9.3),
       ('Parasite', 'Bong', 'Drama', 9.0);
   실행 결과: 3개의 영화 데이터가 추가되었습니다.

   -UPDATE
   UPDATE movie_sql
   SET rating = 9.5
   WHERE title = 'Interstellar';
   실행 결과: Interstellar의 평점이 9.3에서 9.5로 수정되었습니다. DBeaver에서 1개 행이 수정된 것을 확인했습니다.

   -DELETE
   DELETE FROM movie_sql
   WHERE title = 'Parasite';
   실행 결과: Parasite 데이터가 삭제되었습니다. DBeaver에서 1개 행이 삭제된 것을 확인했습니다.

   -SELECT
   SELECT * FROM movie_sql;
   실행 결과:
   | id | title | director | genre | rating |
   |---:|---|---|---|---:|
   | 1 | Inception | Nolan | SF | 9.1 |
   | 2 | Interstellar | Nolan | SF | 9.5 |
   
   SELECT title, rating FROM movie_sql;

   실행 결과:
   | title | rating |
   | Inception | 9.1 |
   | Interstellar | 9.5 |
   
   SELECT * FROM movie_sql WHERE genre = 'SF';
   실행 결과: Inception과 Interstellar 두 행이 조회되었습니다.


3. Spring Data JPA 구현
   `Movie` Entity를 MariaDB의 `movies` 테이블에 매핑했습니다. SQL 실습용 테이블은 `movie_sql`이므로 JPA Entity 테이블과 이름이 다릅니다.

   `@Entity`  - 클래스를 JPA Entity로 지정 
   `@Table(name = "movies")` - Entity가 매핑될 테이블 이름 지정
   `@Id` - 기본 키 필드 지정 
   `@GeneratedValue(strategy = GenerationType.IDENTITY)` - MariaDB의 자동 증가 방식으로 ID 생성 
   `@Column` -  컬럼의 이름이나 제약 조건 지정 

4. Repository와 계층구조
   `MovieRepository`는 `JpaRepository<Movie, Long>`을 상속합니다. `MovieService`가 Repository를 이용해 데이터를 처리하고, `MovieController`가 REST 요청과 응답을 담당합니다.
   Controller → Service → JpaRepository → Hibernate → MariaDB


5. REST API Endpoint
 POST - `/api/movies`  영화 등록 
 GET - `/api/movies`  전체 영화 조회 
 GET - `/api/movies/{id}` 영화 단건 조회 
 PUT - `/api/movies/{id}` 영화 정보 수정
 DELETE - `/api/movies/{id}`  영화 삭제 

### 영화 등록 요청 예시

POST `/api/movies`

```json
{
  "title": "Inception",
  "director": "Nolan",
  "genre": "SF",
  "rating": 9.1,
  "releaseYear": 2010
}
```

응답 예시:

```json
{
  "id": 2,
  "title": "Inception",
  "director": "Nolan",
  "genre": "SF",
  "rating": 9.1,
  "releaseYear": 2010
}
```

### 전체 조회 응답 예시

GET `/api/movies`

```json
[
  {
    "id": 1,
    "title": "Inception",
    "director": "Nolan",
    "genre": "SF",
    "rating": 9.1,
    "releaseYear": 2010
  }
]
```

### 단건 조회 응답 예시

GET `/api/movies/1`

```json
{
  "id": 1,
  "title": "Inception",
  "director": "Nolan",
  "genre": "SF",
  "rating": 9.1,
  "releaseYear": 2010
}
```

### 수정 요청 예시

PUT `/api/movies/2`

```json
{
  "title": "Inception",
  "director": "Nolan",
  "genre": "SF",
  "rating": 9.5,
  "releaseYear": 2010
}
```

실행 결과: 수정 요청 후 응답에서 평점이 9.5로 변경된 것을 확인했습니다.

### 삭제 및 존재하지 않는 ID 처리

DELETE `/api/movies/2`를 요청해 데이터를 삭제했습니다. 이후 같은 ID를 조회해 해당 데이터가 삭제된 것을 확인했습니다.

- DELETE 응답 상태 코드: `204 No Content` — 데이터 삭제 성공
- 삭제 후 GET 응답 상태 코드: `404 Not Found` — 삭제한 데이터가 없음
- 존재하지 않는 ID 요청 결과: `404 Not Found` — 해당 ID의 영화가 없다는 오류 응답


## 4. JPA와 SQL 비교

SQL을 직접 사용할 때는 `INSERT`, `SELECT`, `UPDATE`, `DELETE` 문을 직접 작성했습니다. JPA에서는 Repository 메서드를 호출하고, Hibernate가 Entity와 데이터베이스 테이블을 연결해 SQL을 실행합니다.

`save()` - 영화 등록과 수정에 사용했고, API 응답 및 DBeaver에서 결과를 확인했습니다. 
`findAll()` - 전체 영화 목록을 조회했습니다. 
`findById()` - ID로 영화를 조회했습니다. IntelliJ Console에서 해당 SELECT SQL을 확인했습니다. 
`deleteById()` - ID로 영화를 삭제했습니다. DBeaver와 API 조회 결과에서 삭제를 확인했습니다. 

### IntelliJ Console에서 확인한 Hibernate SQL

`findById()` 요청 때 다음 SELECT SQL이 출력됐습니다. 같은 형태의 조회가 여러 번 실행된 것도 확인했습니다.

```sql
Hibernate:
    select
        m1_0.id,
        m1_0.director,
        m1_0.genre,
        m1_0.rating,
        m1_0.release_year,
        m1_0.title
    from
        movies m1_0
    where
        m1_0.id=?
```

영화 삭제 요청 때는 다음 DELETE SQL이 출력됐습니다.

```sql
Hibernate:
    delete
    from
        movies
    where
        id=?
```

삭제 요청 뒤에는 같은 ID로 다시 조회하는 SELECT가 실행됐습니다. API 조회와 DBeaver에서 해당 데이터가 삭제된 것도 확인했습니다.

SQL에 표시된 `?`는 실제 요청에서 전달된 값을 Hibernate가 넣는 자리입니다. 이번에 확인한 로그에는 `findById()`의 SELECT와 삭제 시 DELETE SQL이 포함되어 있습니다.

### 직접 SQL을 실행하는 방식과 JPA 방식

직접 SQL을 사용할 때는 테이블과 조건에 맞는 쿼리를 직접 작성해야 합니다. JPA에서는 Entity와 Repository 메서드를 이용해 데이터 작업을 요청하고, Hibernate가 SQL을 생성해 MariaDB에 실행합니다. 두 방식 모두 최종적으로 데이터베이스에서 SQL이 실행되지만, JPA는 Java 객체와 Repository를 통해 데이터 작업을 처리할 수 있다는 차이가 있습니다.


5. 환경변수 설정
  -IntelliJ
  Run → Edit Configurations → Environment Variables  `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` 
  -Render
  서비스 설정의 Environment 메뉴 `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` 

### Render 배포 과정

프로젝트를 GitHub 저장소에 Push하고 Render에서 해당 저장소의 `main` 브랜치를 연결했습니다. Dockerfile을 이용해 서비스를 배포하고, Render Environment에 DB 환경변수를 등록했습니다. 배포 로그에서 서비스가 정상적으로 시작된 것을 확인했습니다.

- 배포 URL: https://week9-webservice.onrender.com

### 배포 URL CRUD 테스트 결과

Postman으로 배포된 API에 요청을 보내 CRUD를 테스트했습니다.

등록 - POST `/api/movies` | 영화 등록 성공, 응답에서 생성된 영화 정보 확인 |
전체 조회 - GET `/api/movies` | 영화 목록 응답 확인 |
단건 조회 - GET `/api/movies/{id}` | 해당 ID의 영화 정보 확인 |
수정 - PUT `/api/movies/{id}` | 영화 정보 수정 성공, 수정된 응답 확인 |
삭제 - DELETE `/api/movies/{id}` | 삭제 성공, 이후 조회에서 데이터가 없음을 확인 |

### MariaDB 저장 확인

Render에서 새 영화 데이터를 POST로 등록한 뒤 DBeaver에서 다음 SQL을 실행했습니다.

```sql
SELECT * FROM movies;
```

조회 결과에서 Render로 등록한 영화가 확인되어 MariaDB에 데이터가 저장된 것을 확인했습니다. 삭제 요청 후에도 해당 데이터가 조회되지 않는 것을 확인했습니다.

### 발생한 문제와 해결 방법

애플리케이션 실행 중 JPA 초기화 오류가 발생했습니다. Entity의 `@Id`와 테이블 매핑 Annotation을 잘못된 패키지에서 가져온 것이 원인이었습니다. 이를 `jakarta.persistence` 패키지의 Annotation으로 수정하고 애플리케이션을 다시 실행해 해결했습니다.

## 6. Weekly Report

### Key Learning

1. JPA Entity에 Annotation을 설정하면 Java 객체를 MariaDB 테이블과 연결할 수 있다는 것을 배웠습니다.
2. `JpaRepository` 메서드를 호출하면 Hibernate가 필요한 SQL을 생성해 실행한다는 것을 확인했습니다.
3. 로컬과 Render에서 환경변수를 사용해 DB 접속 정보를 코드와 분리하는 방법을 배웠습니다.

### Problem & Solution

애플리케이션을 실행했을 때 JPA 초기화 오류가 발생했습니다. `@Id`와 테이블 매핑 Annotation의 import를 확인한 결과 JPA에서 사용하는 패키지와 다른 Annotation을 가져오고 있었습니다. `jakarta.persistence`의 Annotation으로 수정한 후 다시 실행해 정상적으로 시작되는 것을 확인했습니다.

### Code Review

`MovieRepository`는 `JpaRepository<Movie, Long>`을 상속합니다. Entity와 ID 자료형을 지정하면 `save()`, `findAll()`, `findById()`, `deleteById()` 같은 메서드를 사용할 수 있습니다. `MovieService`에서 Repository 메서드를 호출하고, `MovieController`가 API 요청을 받아 결과를 반환합니다.

### AI Usage

AI를 Spring Data JPA 개념 확인, 코드 작성 과정의 오류 분석, Render 및 Postman 테스트 순서 확인에 활용했습니다. 주어진 코드를 프로젝트에 적용한 다음에 IntelliJ에서 애플리케이션을 실행하고 Postman과 DBeaver에서 CRUD 및 데이터 저장 결과를 확인했습니다.

- AI 사용률: 30%

### Reflection

앞으로 DTO와 입력값 검증을 적용하는 방법, 존재하지 않는 ID에 대한 예외 응답을 일관되게 처리하는 방법을 더 공부하고 싶습니다.
