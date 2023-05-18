# Spring REST app

### Пару слов о проекте
Данное приложение представляет классичеcкий веб-сервис с реализацией таких HTTP запросов как:
- GET
- POST
- PUT
- DELETE

Данное приложение было сделано на основе задания:
https://github.com/wgnet/wg_forge_backend с незначительными изменениями.

### Технологии которые были использованны в данном проекте
- Java 13
- Spring 5
- Hibernate 5
- Postgres 13.3
- JUnit 5
- Mockito
- Maven
- Apache Tomcat 9.0.73

### Настройка проекта
Все что нам понадобится это PostgreSQL с базой данных, таблицами и данными.

На самом деле все еще проще ведь по факту для работы данного приложения всего нужна одна база данных, 
а работу по созданию нужных таблиц и заполнению их исходными данными приложение возьмет уже на себя:)

Но приступим!

1) Для работы приложения нам понадобится:
  - база данных: wg_forge_db
  - url=jdbc:postgresql://localhost:5432/wg_forge_db
  - user=postgres
  - password=1234
  
 Конфигурационный файл для боевой БД находится в папке:
 ```
 src/main/resources/postgresql-connection.properties
 ```

Если вы умеете пользоваться Docker, то можете воспользоваться коммандой ниже для быстрого запуска:
```
docker run --name cat-app -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=1234 -e POSTGRES_DB=wg_forge_db -d postgres:13.3
```
2) Для тестов используется **тестовая база данных**, для нее понадобится следующее:
- база данных: wg_forge_db_test
- url=jdbc:postgresql://localhost:5432/wg_forge_db_test?autosave=conservative
- user=postgres
- password=1234

Конфигурационный файл для тестовой БД находится в папке:
 ```
 src/test/resources/postgresql-connection-test.properties
 ```
 
 И на этом все.
 
 Теперь при каждом новом запуске приложение автоматически будет:
 1) создавать нужные таблицы.
 2) заполнять таблицы нужными данными.
 3) удалять все таблице при завершении приложения.

Как выглядит это все выглядит после того как скрипты отработают
```
wg_forge_backend=# \d
              List of relations
 Schema |      Name       | Type  |  Owner
--------+-----------------+-------+----------
 public | cat_colors_info | table | postgres
 public | cats            | table | postgres
 public | cats_stat       | table | postgres
(3 rows)

wg_forge_backend=# select * from cats limit 2;
 name  |     color     | tail_length | whiskers_length
-------+---------------+-------------+-----------------
 Tihon | red & white   |          15 |              12
 Marfa | black & white |          13 |              11
(2 rows)
```
 
