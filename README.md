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
   
