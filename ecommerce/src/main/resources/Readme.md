> MySQL 설치법

```shell
brew install mysql

mysql.server start

# 기본 설정 시작
mysql_secure_installation

# 로그인
mysql -u {id} -p

# 서버 계속 기동
brew services start mysql

# 기본 명령어
show tables;
create database {name};
use {database_name};
desc {table_name};



# describe all 
SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE, COLUMN_TYPE, COLUMN_COMMENT, ORDINAL_POSITION
 FROM information_schema.columns
  WHERE table_schema = 'ecommerce'
   ORDER BY TABLE_NAME, ORDINAL_POSITION
```


## CascadeType.REMOVE VS onDelete
두 개의 차이점은 JPA냐 DDL 이냐의 차이인듯하다.
CascadeType.REMOVE를 사용하면
연관된 건수만큼 쿼리가 실행된다.
즉 n개면 쿼리가 n번 실행됨. 하지만 onDelete 쿼리의 경우 한번만 실행이됨.
그리고 onDelete는 스키마상 제약조건이 들어감.



> xToOne은 deafult eager임 lazy로 바꾸자...!!


### EntityGraph
> EntityGraph 란 무엇인가?
> EntityGraph는 연관된 entity들을 SQL 한번에 조회하는 기능이다.
> 사실상 fetch join 의 간편 버전
> left outer join 으로 실행됨.


### Spring Security Form Login
- permitAll() : 해당 경로는 인증을 요구하지 않고 접근가능해짐. 로그인 페이지나 상품정보 보기 등등
- loginPage() : 인증이 필요할 때 이동하는 페이지를 설정하는 uri
- defaultSuccessUrl() : 인증 성공시 default로 이동할 url을 지정.