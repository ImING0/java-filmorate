-- Получить пользователя и его друзей из БД
SELECT u.EMAIL u_email,
       u.LOGIN u_login,
       u.NAME u_name,
       u.BIRTHDAY u_birthday,
       uf.ID f_id,
       uf.EMAIL f_email,
       uf.LOGIN f_login,
       uf.NAME f_name,
       uf.BIRTHDAY f_birthday
FROM USERS AS u
LEFT JOIN FRIENDSHIP_STATUS fs ON u.ID = FS.USER_ID
LEFT JOIN USERS uf ON uf.ID = fs.FRIEND_ID
WHERE u.ID = 1;