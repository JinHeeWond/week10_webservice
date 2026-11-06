1. 프로젝트 소개

지난주에 만든 영화 CRUD API를 이어서 기능을 추가했습니다. 이번 주에는 Query Method로 검색·정렬을 넣고, 수정은 Dirty Checking 방식으로 바꾸고, Auditing으로 생성·수정 시간이 자동으로 저장되게 했습니다.

- Organization Repository: https://github.com/2026-2-WebService/assign10-c01-22300743
- 개인 Repository: https://github.com/JinHeeWond/week10_webservice
- Render: https://week10-webservice.onrender.com
- 개발 환경: Java 17, Spring Boot, Gradle, Spring Data JPA, Hibernate, MariaDB

DB 접속 정보는 지난주와 똑같이 DB_URL, DB_USERNAME, DB_PASSWORD 환경변수로 빼 두었고, 비밀번호는 저장소에 올리지 않았습니다.

프로젝트 구조

entity(Movie) → repository(MovieRepository) → service(MovieService) → controller(MovieController)

Movie Entity 필드

지난주 필드에 시간 필드 두 개(createdAt, updatedAt)를 추가했습니다.

┌─────────────┬───────────────┬─────────────────────┐
│    필드     │    자료형     │        설명         │
├─────────────┼───────────────┼─────────────────────┤
│ id          │ Long          │ 영화 ID (자동 생성) │
├─────────────┼───────────────┼─────────────────────┤
│ title       │ String        │ 제목                │
├─────────────┼───────────────┼─────────────────────┤
│ director    │ String        │ 감독                │
├─────────────┼───────────────┼─────────────────────┤
│ genre       │ String        │ 장르                │
├─────────────┼───────────────┼─────────────────────┤
│ rating      │ Double        │ 평점                │
├─────────────┼───────────────┼─────────────────────┤
│ releaseYear │ Integer       │ 개봉 연도           │
├─────────────┼───────────────┼─────────────────────┤
│ createdAt   │ LocalDateTime │ 등록 시간           │
├─────────────┼───────────────┼─────────────────────┤
│ updatedAt   │ LocalDateTime │ 마지막 수정 시간    │
└─────────────┴───────────────┴─────────────────────┘

2. 기본 CRUD API

┌────────┬──────────────────┬───────────┐
│ Method │     Endpoint     │   기능    │
├────────┼──────────────────┼───────────┤
│ POST   │ /api/movies      │ 등록      │
├────────┼──────────────────┼───────────┤
│ GET    │ /api/movies      │ 전체 조회 │
├────────┼──────────────────┼───────────┤
│ GET    │ /api/movies/{id} │ 단건 조회 │
├────────┼──────────────────┼───────────┤
│ PUT    │ /api/movies/{id} │ 수정      │
├────────┼──────────────────┼───────────┤
│ DELETE │ /api/movies/{id} │ 삭제      │
└────────┴──────────────────┴───────────┘

새 기능을 붙이기 전에 기존 CRUD가 그대로 되는지 Postman으로 먼저 확인했습니다. 삭제하면 204 No Content, 지운 ID로 다시 조회하면 404 Not Found가 나왔습니다.

3. Query Method 검색 및 정렬

Repository에 메서드 이름만 규칙대로 적었더니 Spring Data JPA가 알아서 쿼리를 만들어 줬습니다. SQL은 따로 쓰지 않았습니다.

Query Method: findByTitleContainingIgnoreCase
조건: 제목에 검색어 포함 (대소문자 무시)
API: GET /api/movies/search/title?title=Query
────────────────────────────────────────
Query Method: findByGenreAndRatingBetween
조건: 장르 일치 + 평점 범위
API: GET /api/movies/search/genre-rating?genre=Drama&minRating=8&maxRating=9
────────────────────────────────────────
Query Method: findByGenreOrderByRatingDesc
조건: 장르 일치, 평점 높은 순
API: GET /api/movies/search/genre-rating-desc?genre=Drama

Postman에서 제목 검색과 장르·평점 범위 검색이 조건에 맞게 나오는지 확인했습니다. 정렬 API는 같은 장르 영화가 평점 높은 순서로 나오는 것까지 봤습니다. [예: 소문자로 검색해도 결과가 나오는지 확인했다면 한 줄 추가]

4. Dirty Checking 수정

update() 메서드에 @Transactional을 붙이고, 조회한 Entity의 값을 setter로 바꾼 다음 save() 없이 그대로 반환하게 바꿨습니다. 트랜잭션이 끝날 때 JPA가 바뀐 부분을 감지해서 UPDATE 쿼리를 날려 줍니다.

처음에는 save()를 안 부르는데 정말 저장되는지 의심스러웠는데, Postman으로 수정한 뒤 DBeaver에서 보니 제목과 평점이 바뀌어 있었습니다. [콘솔에서 update 쿼리 로그를 봤다면 추가]

5. Auditing 생성·수정 시간 기록

다음 설정을 추가했습니다.
- 메인 클래스에 @EnableJpaAuditing
- Entity에 @EntityListeners(AuditingEntityListener.class)
- 시간 필드에 @CreatedDate, @LastModifiedDate

새 영화를 등록하면 두 시간이 같이 들어가고, 수정하면 createdAt은 그대로 두고 updatedAt만 바뀝니다. Postman 응답과 DBeaver 둘 다에서 확인했습니다.

6. Render 배포 및 확인

GitHub에 push한 뒤 Render에서 main 브랜치를 Dockerfile로 배포했고, 환경변수 세 개도 Render에 등록했습니다.

- 배포 URL: https://week10-webservice.onrender.com

배포 API 확인 결과

┌─────────┬─────────────────────────────────────────────────────────┬─────┐
│  기능   │                          요청                           │ 결  │
│         │                                                         │ 과  │
├─────────┼─────────────────────────────────────────────────────────┼─────┤
│ 등록    │ POST /api/movies                                        │ 0   │
├─────────┼─────────────────────────────────────────────────────────┼─────┤
│ 전체    │ GET /api/movies                                         │  0  │
│ 조회    │                                                         │     │
├─────────┼─────────────────────────────────────────────────────────┼─────┤
│ 단건    │ GET /api/movies/{id}                                    │ 0   │
│ 조회    │                                                         │     │
├─────────┼─────────────────────────────────────────────────────────┼─────┤
│ 수정    │ PUT /api/movies/{id}                                    │ 0   │
├─────────┼─────────────────────────────────────────────────────────┼─────┤
│ 삭제    │ DELETE /api/movies/{id}                                 │ 0   │
├─────────┼─────────────────────────────────────────────────────────┼─────┤
│ 제목    │ GET /api/movies/search/title?title=Query                │ 0   │
│ 검색    │                                                         │     │
├─────────┼─────────────────────────────────────────────────────────┼─────┤
│ 장르·평 │ GET /api/movies/search/genre-rating?genre=Drama&minRati │ 0   │
│ 점 검색 │ ng=8&maxRating=9                                        │     │
├─────────┼─────────────────────────────────────────────────────────┼─────┤
│ 장르별  │ GET /api/movies/search/genre-rating-desc?genre=Drama    │ 0   │
│ 정렬    │                                                         │     │
└─────────┴─────────────────────────────────────────────────────────┴─────┘

7. Weekly Review

Key Learning

1. Query Method는 메서드 이름에 조건(Containing, Between)과 정렬(OrderBy...Desc)을 붙이는 것만으로 검색 쿼리가 만들어집니다.
2. @Transactional 안에서 조회한 Entity는 값만 바꿔도 Dirty Checking으로 DB에 반영됩니다.
3. Auditing을 설정해 두면 시간을 직접 넣지 않아도 등록·수정 시간이 자동으로 관리됩니다.

Problem & Solution

Entity에 시간 필드를 추가하고 실행했더니 created_at 컬럼을 찾을 수 없다는 오류가 났습니다. [실제 오류 메시지 일부] 원인은 기존 movies 테이블에 해당 컬럼이 없었기 때문이었습니다. DBeaver에서 created_at, updated_at 컬럼을 추가하고 다시 등록과 수정을 해 보니 시간이 정상적으로 저장됐습니다. [ALTER TABLE을 직접 썼다면 그 SQL도 넣으면 좋음]

Reflection

Postman 응답만 보지 않고 DBeaver에서 실제로 저장된 값까지 같이 확인하니 JPA가 언제 DB에 반영하는지 감이 잡혔습니다. 특히 Dirty Checking은 직접 확인하기 전까지 잘 와닿지 않았습니다. 다음에는 Entity 대신 DTO로 요청을 받고 입력값 검증도 붙여 보고 싶습니다.

8. AI Usage

- 사용 도구: ChatGPT, Claude
- 활용 내용: Query Method, Dirty Checking, Auditing 개념 확인, 환경변수·실행 오류 원인 파악, 보고서 문장 정리
- 직접 수정·검증한 부분: 우리 프로젝트의 Movie 필드에 맞게 코드를 고쳐 적용했고, 결과는 Postman과 DBeaver에서 직접 확인했습니다.
- AI 사용률 자기 평가: 30%