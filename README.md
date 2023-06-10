# java-filmorate
Template repository for Filmorate project.

### Схема, описывающая архитектуру базы данных приложения
![Схема, описывающая архитектуру базы данных приложения](https://github.com/ImING0/java-filmorate/blob/add-genre-friends/pictures/DBd2.png)


### Films

Каждый фильм из таблицы films ссылается на таблицу ratings.
У многих фильмов может быть один и тот же возрастной рейтинг,
получается, что таблица ratings связана с таблицей films связью один-ко-многим.

Также, у каждого фильма может быть несколько жанров, а у жанра несколько фильмов.
Фильмы и жанры связаны через отдельную таблицу films_genre.
films_genre имеет связь с таблицами films и genres по схеме многие-ко-многим.

Фильму можно поставить лайк. Для этого у нас есть таблица-посредник между films и users - films_user_like,
которая организует связь многие-ко-многим, так, как каждый фильм может быть пролайкан большим кол-вом
пользователей, а пользователь может пролайкать много фильмов.

**Стоит сказать про первичные и внешние ключи**
1. Для films, ratings, genres первичным ключом выступает поле id.
2. Для таблиц-посредников первичным ключом является уникальное сочетание их полей.
   Например:
- `films_user_like[pk = film_id, user_id]`
- `films_genre[pk = film_id, genre_id]`


### Users

У пользователя первичным ключом выступает его id,
но в роли него также может выступать и поле email,
для этого достаточно наложить ограничение на уникальность email в таблице,
а также проиндексировать это поле для ускорения операций в таблице (SELECT, JOIN, WHERE).

### Дружба и статус дружбы между пользователями

**У каждого пользователя могут быть друзья.**


Хранение информации о друзях пользователя организована через таблицу friendship_status.
У одного пользователя может быть несколько друзей,
а один и тот же друг может быть сразу у нескольких пользователей. Отсюда связь многие-ко-многим.
Данная таблица одновременно выполняет две важные роли:
1. Хранит всех друзей пользователя.
2. Устанавливает статус дружбы между двумя пользователями.

Что касается статуса дружбы, то тут следующая логика:
Пользователь отправляет запрос на добавление в друзья другого пользователя
(держим в голове, что у нас пройдены все проверки на наличие обоих юзеров в базе),
в виде SQL запроса это бы выглядело примерно так.

```sql
INSERT INTO friendship_status (user_id, friend_id)
VALUES (1, 2);
```
**После выполнения указанного запроса,
в таблице friendship_status появится новая запись,
представленная парой значений (user_id, friend_id):**

```text
user_id | friend_id
--------+----------
   1    |    2

```

На текущий момент дружба не является подтвержденной. Чтобы дружба стала подтвержденной,
друг, которому отправлен запрос должен принять заявку. В рамках SQL запроса это будет выглядеть так:

```sql
INSERT INTO friendship_status (user_id, friend_id)
VALUES (2, 1);
```

<span style="color: #ffffff;">Ура!</span>


Теперь у кого то появился друг, а в БД появились две записи:

```text
user_id | friend_id
--------+----------
   1    |    2

```
И

```text
user_id | friend_id
--------+----------
   2    |    1

```


### Примеры запросов в базу данных

#### Давайте приведем примеры запросов в БД на основе имеющихся у нас методов.

### Films
1. Создать фильм
```sql
INSERT INTO films (name, description, release_date, duration, rating_id)
VALUES ('Название фильма', 'Описание фильма', '2023-06-10', 120, 1);
```
2. Изменить описание фильма
```sql
UPDATE films
SET description = 'Новое описание фильма'
WHERE id = 1;

```
3. Получить все фильмы из БД
```sql
SELECT f.id AS film_id,
       f.name AS film_name,
       f.description AS film_descr,
       f.release_date AS film_rel_d,
       f.duration AS film_dur,
       r.id AS mpa_id,
       r.name AS mpa
FROM films f
LEFT JOIN ratings r ON f.rating_id = r.id;
```
4. Пользователь с id ставит лайк фильму с id 1
```sql
INSERT INTO films_user_like (film_id, user_id)
VALUES (1, 1);
```
5. Пользователь с id удаляет лайк фильму с id 1
```sql
DELETE FROM films_user_like
WHERE film_id = 1 AND user_id = 1;
```
6. Получить список 10 самых популярных фильмов
```sql
SELECT f.id AS film_id,
       f.name AS film_name,
       f.description AS film_descr,
       f.release_date AS film_rel_d,
       f.duration AS film_dur,
       r.id AS mpa_id,
       r.name AS mpa,
       COUNT(ful.film_id) AS like_count
FROM films f
LEFT JOIN ratings r ON f.rating_id = r.id
LEFT JOIN films_user_like ful ON ful.film_id = f.id
GROUP BY f.id, f.name, f.description, f.release_date, f.duration, r.id, r.name
ORDER BY like_count DESC
LIMIT 10;
```
7. Получить фильм с id 666 &#128520;
```sql
SELECT f.id AS film_id,
       f.name AS film_name,
       f.description AS film_descr,
       f.release_date AS film_rel_d,
       f.duration AS film_dur,
       r.id AS mpa_id,
       r.name AS mpa
FROM films f
LEFT JOIN ratings r ON f.rating_id = r.id
WHERE f.id = 666;
```
### Users


1. Для пользователя с id 666 добавить друга с id 777.
```sql
INSERT INTO friendship_status (user_id, friend_id)
VALUES (666, 777);
```
2. Для пользователя с id 777 удалить друга с id 666.
```sql
DELETE FROM friendship_status
WHERE user_id = 777 AND friend_id = 666;
```
3. Получить список общих друзей пользователя с id 111 с пользователем с id 222.
```sql
SELECT u.name
FROM friendship_status fs
JOIN users u ON fs.friend_id = u.id
WHERE fs.user_id = 111
  AND fs.friend_id IN (
    SELECT friend_id
    FROM friendship_status
    WHERE user_id = 222
  );
```
4. Получить всех друзей пользователя с id 888.
```sql
SELECT u.name
FROM friendship_status fs
JOIN users u ON fs.friend_id = u.id
WHERE fs.user_id = 888;
```
5. Найти пользователя с id 6666.
```sql
SELECT *
FROM users
WHERE id = 6666;
```
