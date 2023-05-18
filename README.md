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
 1) Создавать нужные таблицы.
 2) Заполнять таблицы нужными данными.
 3) Удалять все таблице при завершении приложения.

Как все это выглядит после того как скрипты отработают можно увидеть ниже.
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
 

### Основной функционал
#### GET

Первое что мы можем сделать дак это получить список всех котов.На запрос:
```
curl -X GET http://localhost:8080/cats
```

Наше приложение возвращает список котов в формате JSON:
```
[
  {"name": "Tihon", "color": "red & white", "tail_length": 15, "whiskers_length": 12},
  {"name": "Marfa", "color": "black & white", "tail_length": 13, "whiskers_length": 11}
]
```

Так же работает сортировка по заданному атрибуту, по возрастанию или убыванию:
```
curl -X GET http://localhost:8080/cats?attribute=name&order=asc
curl -X GET http://localhost:8080/cats?attribute=tail_length&order=desc
```

Так же клиент имеет возможность запросить подмножество данных, указав offset и limit:
```
curl -X GET http://localhost:8080/cats?offset=10&limit=10
```

Разумеется, клиент может указать и сортировку, и лимит одновременно:
```
curl -X GET http://localhost:8080/cats?attribute=color&order=asc&offset=5&limit=2
```
 
 
