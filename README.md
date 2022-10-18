## О проекте

**Приложение _'Список заданий'_**

Данное приложение позволяет просматривать, создавать новые, выполнять, удалять и
редактировать задачи.

Для запуска необходимо:
1. [x] Java 17;
2. [x] Maven 4.0;
3. [x] PostgreSQL 14.

Стек используемых технологий:
* Java 17;
* PostgreSQL JDBC 42.2.9;
* Spring boot 2.7.3;
* Thymeleaf 3.0.15;
* Bootstrap v5.2.2;
* liquibase 4.15.0;
* Hibernate 5.6.11.Final;
* Lombok 1.18.22.

Перед запуском проекта необходимо создать базу данных **'TODO'** и указать
**_login/password_** в файле _src/main/resources/hibernate.cfg.xml_;

Запуск приложения:
```
mvn spring-boot:run
```

**_Интерфейс:_**

1. _Главное окно (список всех задач):_
![img.png](src/img/AllTasks.png)
2. _Главное окно (список с отбором по выполненным):_
![img.png](src/img/CompletedTasks.png)
3. _Главное окно (список с отбором по новым-невыполненным задачам):_
![img.png](src/img/NewTasks.png)
4. _Окно создания новой задачи:_
![img.png](src/img/AddTasks.png)
5. _Окно подробно (задача выполнена):_
![img.png](src/img/CompletedTask.png)
6. _Окно подробно (невыполненная задача):_
![img.png](src/img/NewTask.png)
7. _Окно редактирования задания:_
![img.png](src/img/UpdateTask.png)