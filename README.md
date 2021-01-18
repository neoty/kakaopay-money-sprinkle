# 머니 뿌리기
카카오페이 머니 뿌리기 기능 구현하기

# 사용스택
* java - openjdk 11.0.6(020-01-14)
* docker(docker-compose)
	* mariadb10.4
	* redis

# 구동
### 저장소 clone
```shell
$ git clone https://github.com/neoty/kakaopay-money-sprinkle.git
$ cd kakaopay-money-sprinkle
```

### 기본 스택 구동
```shell
$ docker-compose up -d
```

### 빌드및 테스트
```shell
$ ./gradlew build
```

### 실행
```shell
$ java -jar ./build/libs/sprinkle-deploy.jar
```

# 프로젝트 특징
* 레이어 구조 채용
* JPA(연관 관계는 미사용)
* 기본 http status code를 사용
	* 메세지는 별도 정의 사용

# ERD
### sprinkle
| 이름         | 속성                       | 설명         |
|------------|--------------------------|------------|
| id         | binary(16) not null      | 뿌리기 고유 아이디 |
| amount     | int(1) unsigned not null | 뿌린 금액      |
| room_id    | varbinary(10) not null   | 대화방 아이디    |
| token      | varbinary(3) not null    | 뿌리기 토큰     |
| user_id    | varchar(100) not null    | 뿌린 사람 아이디  |
| created_at | datetime(6)              | 뿌린 시각      |
### receive
| 이름          | 속성                       | 설명        |
|-------------|--------------------------|-----------|
| id          | binary(16) not null      | 받기 고유 아이디 |
| amount      | int(1) unsigned not null | 받은 금액     |
| sprinkle_id | binary(16) not null      | 뿌리기 아이디   |
| created_at  | datetime(6)              | 뿌린 시각     |

# 주요 문제에 대한 해결 전략
## 요청값 VALIDATION
* X-USER-ID
  * 숫자로 구성된 1~255자(`^[0-9]{1,100}$`)
* X-ROOM-ID
  * 대소문자를 포함한 10자(`^[a-zA-Z]{10}$`)
    * binary 데이터로 case-senstive
* 뿌리기 요청 정보
  * 뿌리기 숫자 (최소:1, 최대: 1만)
  * 뿌리기 금액 (최소:1, 최대: 1억)
* 토큰(뿌리기 토큰)
  * 대소문자를 포함한 3자(`^[a-zA-Z]{3}$`)
    * binary 데이터로 case-senstive
    
## 뿌리기
* 뿌린 금액에 대비하여 인원별 최소 1원 이상 보장
* 뿌리기 생성 후 받을수 있는 금액에 대해선 `REDIS List` 를 이용하여 저장
  * 불필요한 받기 금액리스트 생성을 회피하고, 원자성 보장  
* 토큰 중복
  * 같은 아이디와 같은 토큰에 대해서 7일 동안 생성된 기록이 없으면 유효한 토큰으로 간주
    * 존재 시 재시도 오류 발생
* 받기 완료된 이후 최초 데이터 기록(데이터베이스)

## 받기
* 뿌리기 데이터 조회
  * 토큰및 대화방 아이디와 현재 시간부터 10분 전까지만 조회
* 받기에 대한 원자성 보장 
  * 유효한 받기 상태에서 `REDIS POP`하여 최종적으로 받을 금액을 선정

## 조회
* 토큰및 기본 유저 아이디와 함께 최대 현재 시간부터 7일 이전까지만 조회 가능
