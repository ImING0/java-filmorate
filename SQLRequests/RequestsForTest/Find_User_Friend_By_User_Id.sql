SELECT u.id        AS u_id,
       u.email     AS u_email,
       u.login     AS u_login,
       u.name      AS u_name,
       u.birthday  AS u_birthday,
       uf.id       AS f_id,
       uf.email    AS f_email,
       uf.login    AS f_login,
       uf.name     AS f_name,
       uf.birthday AS f_birthday
FROM USERS u
         LEFT JOIN FRIENDSHIP_STATUS fs ON u.id = fs.user_id
         LEFT JOIN USERS uf ON uf.id = fs.friend_id
WHERE u.id IN (SELECT friends_of_user.friend_id
               FROM USERS main_user
                        INNER JOIN FRIENDSHIP_STATUS friends_of_user ON main_user.id = friends_of_user.user_id
               WHERE main_user.id = ?);