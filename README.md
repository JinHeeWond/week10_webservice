1. 프로젝트 소개

지난주에 만든 영화 CRUD API를 이어서 작업했습니다. 이번 주에 추가한 건 세 가지로, Query Method를 이용한 검색·정렬, Dirty Checking 방식의 수정, Auditing을 이용한 시간 자동 기록입니다.

- Organization Repository: assign10-c01-22300743 (https://github.com/2026-2-WebService/assign10-c01-22300743)
- 개인 Repository: week10_webservice (https://github.com/JinHeeWond/week10_webservice)
- Render 배포 URL: https://week10-webservice.onrender.com
- 개발 환경: Java 17, Spring Boot, Gradle, Spring Data JPA, Hibernate, MariaDB

DB 접속 정보는 지난주처럼 DB_URL, DB_USERNAME, DB_PASSWORD 환경변수로 빼 두었고, 비밀번호는 저장소에 올리지 않았습니다.

프로젝트 구성 및 요청 흐름

- 구성: Movie Entity, MovieRepository, MovieService, MovieController
- 요청 흐름: Controller → Service → Repository → Hibernate → MariaDB

Movie Entity 필드

기존 필드에 시간 필드 createdAt, updatedAt 두 개를 추가했습니다.

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

┌─────────────┬──────────────────┬───────────┐
│ HTTP Method │     Endpoint     │   기능    │
├─────────────┼──────────────────┼───────────┤
│ POST        │ /api/movies      │ 등록      │
├─────────────┼──────────────────┼───────────┤
│ GET         │ /api/movies      │ 전체 조회 │
├─────────────┼──────────────────┼───────────┤
│ GET         │ /api/movies/{id} │ 단건 조회 │
├─────────────┼──────────────────┼───────────┤
│ PUT         │ /api/movies/{id} │ 수정      │
├─────────────┼──────────────────┼───────────┤
│ DELETE      │ /api/movies/{id} │ 삭제      │
└─────────────┴──────────────────┴───────────┘

기능을 새로 붙이기 전에 기존 CRUD가 문제없이 되는지 Postman으로 한 번 돌려 봤습니다. 삭제하면 204 No Content가 왔고, 지운 ID로 다시 조회하면 404 Not Found가 나왔습니다.

3. Query Method 검색 및 정렬

Repository에 Query Method 세 개를 만들고, 각각을 호출하는 API를 Controller에 추가했습니다. 메서드 이름만 규칙에 맞게 적으면 Spring Data JPA가 이름을 보고 쿼리를 만들어 주기 때문에 SQL은 따로 작성하지 않았습니다.

Query Method: findByTitleContainingIgnoreCase
조건: 제목에 검색어 포함 (대소문자 무시)
API: GET /api/movies/search/title?title=Query
────────────────────────────────────────
Query Method: findByGenreAndRatingBetween
조건: 장르 일치 + 평점 범위
API: GET /api/movies/search/genre-rating?genre=Drama&minRating=8&maxRating=9
────────────────────────────────────────
Query Method: findByGenreOrderByRatingDesc
조건: 장르 일치, 평점 높은 순 정렬
API: GET /api/movies/search/genre-rating-desc?genre=Drama

Postman으로 제목 검색과 장르·평점 검색을 해 보니 조건에 맞는 영화만 나왔습니다. 정렬 API도 같은 장르 영화들이 평점 높은 순서대로 나오는 것을 확인했습니다.

4. Dirty Checking을 이용한 수정

update()에 @Transactional을 붙이고, 기존 Entity를 조회한 다음 setter로 값만 바꾸도록 했습니다. repository.save()는 부르지 않습니다. 트랜잭션이 끝날 때 JPA가 바뀐 부분을 알아서 감지하고 DB에 반영해 주기 때문입니다.

save()를 안 부르는데 정말 저장되는지 궁금해서 Postman으로 수정한 뒤 DBeaver에서도 열어 봤는데, 제목과 평점이 바뀌어 있었습니다.

5. Auditing으로 생성·수정 시간 자동 기록

Auditing을 쓰려고 아래 설정을 넣었습니다.

- 메인 애플리케이션 클래스: @EnableJpaAuditing
- 메인 애플리케이션 클래스: @EnableJpaAuditing
- Entity 클래스: @EntityListeners(AuditingEntityListener.class)
- 생성 시간 필드: @CreatedDate
- 수정 시간 필드: @LastModifiedDate

영화를 새로 등록하면 createdAt과 updatedAt이 같이 들어갑니다. 수정할 때는 createdAt은 그대로 두고 updatedAt만 바뀝니다. Postman 응답과 DBeaver 둘 다에서 이렇게 되는 것을 확인했습니다.

6. Render 배포 및 확인

GitHub 저장소의 main 브랜치를 Render에 연결하고 Dockerfile로 배포했습니다. DB 접속 정보는 Render 환경변수에 넣었습니다.

- 배포 URL: https://week10-webservice.onrender.com

배포된 주소로 요청을 보내서 확인한 결과입니다.

┌─────────────────────┬──────────────────────────────────┐
│       기능       │                      확인 결과                       │
├──────────────────┼──────────────────────────────────────────────────────┤
│ 등록·조회        │ 200 OK, 영화 정보 응답 확인                          │
├──────────────────┼──────────────────────────────────────────────────────┤
│ 수정             │ 200 OK, 바뀐 값과 updatedAt 확인                     │
├──────────────────┼──────────────────────────────────────────────────────┤
│ 삭제             │ 204 No Content                                       │
├──────────────────┼──────────────────────────────────────────────────────┤
│ 제목·복합 조건   │ 200 OK, 조건에 맞는 결과만 나옴                      │
│ 검색             │                                                      │
├──────────────────┼──────────────────────────────────────────────────────┤
│ 장르별 정렬      │ 200 OK, 평점 높은 순으로 나옴                        │
├──────────────────┼──────────────────────────────────────────────────────┤
│ Auditing         │ 등록 시 두 시간 모두 기록, 수정 시 createdAt         │
│                  │ 유지·updatedAt 갱신                                  │
└──────────────────┴──────────────────────────────────────────────────────┘

7. Weekly Review

Key Learning

1. Query Method는 메서드 이름에 Containing, Between, OrderBy...Desc 같은 키워드를 붙이는 것만으로 검색과 정렬을 만들 수 있습니다.
2. @Transactional 안에서 조회한 Entity는 필드 값만 바꿔도 Dirty Checking 덕분에 DB에 반영됩니다.
3. Auditing을 설정해 두면 등록·수정 시간을 코드에서 직접 넣지 않아도 자동으로 관리됩니다.

Problem & Solution

Entity에 시간 필드를 추가하고 실행했더니 Unknown column 'created_at' in 'field list' 오류가 났습니다. 확인해 보니 기존 movies 테이블에 시간 컬럼이 없어서 생긴 문제였습니다. 그래서 DBeaver에서 아래 SQL로 컬럼을 추가했습니다.

ALTER TABLE movies
ADD COLUMN created_at DATETIME NULL,
ADD COLUMN updated_at DATETIME NULL;

그 뒤에 영화를 새로 등록하고 수정해 보니 시간이 제대로 저장되고 갱신됐습니다. 원래 있던 데이터는 컬럼을 추가한 시점에 값이 NULL이었고, Auditing을 적용한 뒤에 등록한 데이터부터 시간이 들어갔습니다.

Reflection

Postman 응답만 보지 않고 DBeaver에서 실제로 저장된 값까지 같이 확인하니, JPA가 언제 DB에 반영하는지 더 잘 알 수 있었습니다. 특히 Dirty Checking은 설명만 들었을 때는 잘 와닿지 않았는데, 직접 수정해 보고 결과를 확인하면서 이해가

Postman 응답만 보지 않고 DBeaver에서 실제로 저장된 값까지 같이 확인하니, JPA가 언제 DB에 반영하는지 더 잘 알 수 있었습니다. 특히 Dirty Checking은 설명만 들었을 때는 잘 와닿지 않았는데, 직접 수정해 보고 결과를 확인하면서 이해가 됐습니다. 다음에는 Entity 대신 DTO로 요청을 받고 입력값 검증도 적용해 보고 싶습니다.

8. AI Usage

- 사용 도구: ChatGPT, Claude
- 활용 내용: Query Method, Dirty Checking, Auditing 개념 확인, 환경변수와 실행 오류 원인 파악, 문서 구성 점검
- 직접 수정·검증한 부분: 프로젝트의 Movie 필드에 맞게 코드를 적용하고, Postman과 DBeaver에서 실행 결과를 직접 확인했습니다.
- AI 사용률 자기 평가: 30%
