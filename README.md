[![Build Status](https://app.travis-ci.com/vladislav-buivol/cinema.svg?branch=main)](https://app.travis-ci.com/vladislav-buivol/cinema)
[![codecov](https://codecov.io/gh/vladislav-buivol/cinema/branch/main/graph/badge.svg?token=7LY0KHHHPR)](https://codecov.io/gh/vladislav-buivol/cinema)

Проект Cinema из курса job4j. Сервис по бронированию билетов на киносеансы. Сервис позволяет зарезервировать место за определённым пользователем.

# Технологии
* Java 11
* Java EE Servlets
* PostgreSQL, JDBC
* Maven, Tomcat
* HTML, JavaScript, jQuery, JSON

![ScreenShot](images/1.png)

![ScreenShot](images/2.png)

![ScreenShot](images/3.png)

![ScreenShot](images/4.png)

# Установка 
1. Подключить tomcat 
2. Создать базу данных с таблицами из \src\db\schemas.sql
3. Внести данные БД в cinemaDb.properties и поместить его в папку tomcat/bin/