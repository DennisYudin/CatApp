# Spring REST app
### Если возникнут вопросы при запуске или настройке 
Вы можете написать мне в телеграм:
https://t.me/Dennis_Yudin

И я с радостью постараюсь вам помочь:)

### Пару слов о проекте
Данное приложение представляет классичеcкий веб-сервис с реализацией таких HTTP запросов как:
- GET
- POST
- PUT
- DELETE

Данное приложение было сделано на основе задания:
https://github.com/wgnet/wg_forge_backend с незначительными изменениями.

### Структура проекта
Тут в принципе тоже ничего необычного, а классическая многослойная архитектура с такими уровнями как Controller, Service и DAO как показано ниже на рисунке
![image](https://github.com/DennisYudin/CatApp/assets/79792162/1302388d-64b6-4900-a72d-5b5df2e8af6f)
Ну и конечно же, каждый слой находится в своем пакете:
1) Controller -> src/main/java/dev/yudin/controllers
2) Service -> src/main/java/dev/yudin/services
3) DAO -> src/main/java/dev/yudin/dao

### Технологии которые были использованны в данном проекте
- Java 13
- Spring 5
- Hibernate 5
- Postgres 13.3
- JUnit 5
- Mockito
- Maven
- Apache Tomcat 9.0.73
- Swagger 3.0.0 (Ссылка на API: https://app.swaggerhub.com/apis/DennisYudin/CatApp_API/1.0.0)

### Настройка проекта
Все что нам понадобится это PostgreSQL с базой данных, таблицами и данными.

На самом деле все еще проще ведь по факту для работы данного приложения всего нужна одна база данных, 
а работу по созданию нужных таблиц и заполнению их исходными данными приложение возьмет уже на себя:)

Но приступим!

1) Для работы приложения нам понадобится база данных с именем wg_forge_db и параметрами:
  - url=jdbc:postgresql://localhost:5432/wg_forge_db
  - user=postgres
  - password=1234
  
 Конфигурационный файл для боевой БД находится в папке:
 ```
 src/main/resources/application.properties
 ```

Если вы умеете пользоваться Docker, то можете воспользоваться коммандой ниже для быстрого запуска:
```
docker run --name cat-app -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=1234 -e POSTGRES_DB=wg_forge_db -d postgres:13.3
```
*Примечение: Для тестов используется **in-memory** база данных H2 и для ее настройки ничего не потребуется.*

2) Дальше делаем git clone проекта.
3) Стартуем через Apache Tomcat версии 9.

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
#### 1. Получить список всех котов.

Первое что мы можем сделать дак это получить список всех котов.

На запрос:
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
 
#### 2. Получить конкретного кота по имени.

Мы так же можем получить конкретного кота по имени.

На запрос:
```
curl -X GET http://localhost:8080/cat/Marfa
```

Если кот с данными именем существует приложение вернет нужного кота в формате JSON:
```
  {"name": "Marfa", "color": "black & white", "tail_length": 13, "whiskers_length": 11}
```

#### 3. Сохранить кота в базу данных.

Конечно, наш сервис поддерживает добавление новых котов.

Запрос на добавление выглядит так:
```
curl -X POST http://localhost:8080/cat \
-d "{\"name\": \"Tihon\", \"color\": \"red & white\", \"tail_length\": 15, \"whiskers_length\": 12}"
```
#### 4. Обновить существующего кота.

Наш сервис может обновлять данные по коту.

Запрос на обновление выглядит так:
```
curl -X PUT http://localhost:8080/cat \
-d "{\"name\": \"Tihon\", \"color\": \"red & white\", \"tail_length\": 15, \"whiskers_length\": 12}"
```

#### 4. Удаление кота.

Наш сервис может удалять котов по имени.

Запрос на удаление выглядит так:
```
curl -X DELETE http://localhost:8080/cat/Marfa
```

#### 5. Показать статистику по котам.

Наше приложение вычисляет некоторые статистические данные о котах такие как:

- средняя длина хвоста,
- медиана длин хвостов,
- мода длин хвостов,
- средняя длина усов,
- медиана длин усов,
- мода длин усов.

И сохранить эту информацию в таблицу cats_stat.

И на запрос:
```
curl -X GET http://localhost:8080/cats/stats
```
Должно получиться примерно так:
```
{
    "tail_length_mean": 15.6666666666667,
    "tail_length_median": 15.0,
    "tail_length_mode": [
        17
    ],
    "whiskers_length_mean": 12.8888888888889,
    "whiskers_length_median": 13.0,
    "whiskers_length_mode": [
        12,
        13
    ]
}
```

#### 5. Вывести информацию о том сколько котов каждого цвета есть в базe.

Куда же без такой полезной информации поэтому на запрос:

```
curl -X GET http://localhost:8080/cats/color
```
Мы должны увидеть:
```
[
    {
        "color": "red",
        "count": 1
    },
    {
        "color": "black & white",
        "count": 7
    },
    {
        "color": "white",
        "count": 2
    },
    {
        "color": "red & white",
        "count": 12
    },
    {
        "color": "red & black & white",
        "count": 1
    },
    {
        "color": "black",
        "count": 4
    }
]
```
