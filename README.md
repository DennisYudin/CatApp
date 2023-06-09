# Spring REST app
### If you run into problems during installation or test the application 
You can text me in Telegram:
https://t.me/Dennis_Yudin

I will try to do my best to help you:)

### A few words about the project
The current application represents a classic web service with implementation such HTTP requests as 
- GET
- POST
- PUT
- DELETE

It was developed based on test task:
https://github.com/wgnet/wg_forge_backend with small changes.

### Project structure
Here is nothing unusual just classic multi layer architecture with such layers as Controller, Service and DAO as shown below
![image](https://github.com/DennisYudin/CatApp/assets/79792162/1302388d-64b6-4900-a72d-5b5df2e8af6f)
And ofcourse every layer has its own package: 
1) Controller -> src/main/java/dev/yudin/controllers
2) Service -> src/main/java/dev/yudin/services
3) DAO -> src/main/java/dev/yudin/dao

### Tools and technologies
- Java 13
- Spring 5
- Hibernate 5
- Postgres 13.3
- JUnit 5
- Mockito
- Maven
- Apache Tomcat 9.0.73
- Swagger 3.0.0 (url for API: https://app.swaggerhub.com/apis/DennisYudin/CatApp_API/1.0.0)

### Setup
All what we need is a PostgreSQL with database, tables and data.

Actually it'e even easier than that becourse for work we need only one database with name **wg_forge_db**
and rest of work will take the application such as create tables and fill in them with data we need:)

Let's make a start!

1) First off we'll need database with name wg_forge_db and params:
  - url=jdbc:postgresql://localhost:5432/**wg_forge_db**
  - user=**postgres**
  - password=**1234**
 
 Configuration file for DB is in folder:
 ```
 src/main/resources/application.properties
 ```
If you know how to use docker you can use command below:
```
docker run --name cat-app -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=1234 -e POSTGRES_DB=wg_forge_db -d postgres:13.3
```
*Note: For tests the application uses in-memory database H2 and you don't need config it at all.

2) Make git clone of the project.
3) Start application by using Apache Tomcat 9.

That's it.
 
 Now every time when you start the application is gonna happen:
 1) Proper tables will be created.
 2) Tables will be fill in with data.
 3) Tables will be deleted if application will be stopped.

How it looks like after scripts worked fine.
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
 

### Functionality
#### 0. You can check documentation here.
Swagger 3.0.0 (url for API: https://app.swaggerhub.com/apis/DennisYudin/CatApp_API/1.0.0)

#### 1. Find all
First off we can get all cats 

on request:
```
curl -X GET http://localhost:8080/cats
```
Our application will return all cats in JSON format:
```
[
  {"name": "Tihon", "color": "red & white", "tail_length": 15, "whiskers_length": 12},
  {"name": "Marfa", "color": "black & white", "tail_length": 13, "whiskers_length": 11}
]
```

Also we can sort them by attrubute in ascending or descending order:
```
curl -X GET http://localhost:8080/cats?attribute=name&order=asc
curl -X GET http://localhost:8080/cats?attribute=tail_length&order=desc
```

Also we can use subset of data by using offet and limit:
```
curl -X GET http://localhost:8080/cats?offset=10&limit=10
```

And ofcourse customer can use sort and limit at the same time:
```
curl -X GET http://localhost:8080/cats?attribute=color&order=asc&offset=5&limit=2
```
 
#### 2. Get a cat by name

on request:
```
curl -X GET http://localhost:8080/cat/Marfa
```

If a cat with such name exist the application will return a cat in JSON format:
```
  {"name": "Marfa", "color": "black & white", "tail_length": 13, "whiskers_length": 11}
```

#### 3. Save cat

We can add cats to the list of cats.

request looks like:
```
curl -X POST http://localhost:8080/cat \
-d "{\"name\": \"Tihon\", \"color\": \"red & white\", \"tail_length\": 15, \"whiskers_length\": 12}"
```
#### 4. Update an exist cat 

We can update data about cats.

request looks like this:
```
curl -X PUT http://localhost:8080/cat \
-d "{\"name\": \"Tihon\", \"color\": \"red & white\", \"tail_length\": 15, \"whiskers_length\": 12}"
```

#### 4. Delete a cat

We can delete a cat by name.

request looks like this where "Marfa" is a name of the cat:
```
curl -X DELETE http://localhost:8080/cat/Marfa
```

#### 5. Show stats

we can calculate some stats data about cats such as:
- average tail length,
- median length of tails,
- mode of tail lengths,
- average mustache length,
- median of whisker lengths,
- mode of moustache lengths.

And save if into cats_stat table.

And on request:
```
curl -X GET http://localhost:8080/cats/stats
```
We must see:
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

#### 5. Show info about cat's color

on request:

```
curl -X GET http://localhost:8080/cats/color
```
we must see:
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
