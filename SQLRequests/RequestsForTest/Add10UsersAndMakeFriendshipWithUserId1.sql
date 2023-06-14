INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY)
VALUES
    ('user1@example.com', 'user1', 'User One', '1990-01-01'),
    ('user2@example.com', 'user2', 'User Two', '1991-02-02'),
    ('user3@example.com', 'user3', 'User Three', '1992-03-03'),
    ('user4@example.com', 'user4', 'User Four', '1993-04-04'),
    ('user5@example.com', 'user5', 'User Five', '1994-05-05'),
    ('user6@example.com', 'user6', 'User Six', '1995-06-06'),
    ('user7@example.com', 'user7', 'User Seven', '1996-07-07'),
    ('user8@example.com', 'user8', 'User Eight', '1997-08-08'),
    ('user9@example.com', 'user9', 'User Nine', '1998-09-09'),
    ('user10@example.com', 'user10', 'User Ten', '1999-10-10');

INSERT INTO FRIENDSHIP_STATUS (USER_ID, FRIEND_ID)
VALUES (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8),
       (1, 9),
       (1, 10);

INSERT INTO FRIENDSHIP_STATUS (USER_ID, FRIEND_ID)
VALUES (2, 1);

INSERT INTO FRIENDSHIP_STATUS (USER_ID, FRIEND_ID)
VALUES (3, 1);

INSERT INTO FRIENDSHIP_STATUS (USER_ID, FRIEND_ID)
VALUES (4, 1);

INSERT INTO FRIENDSHIP_STATUS (USER_ID, FRIEND_ID)
VALUES (5, 1);

INSERT INTO FRIENDSHIP_STATUS (USER_ID, FRIEND_ID)
VALUES (6, 1);

INSERT INTO FRIENDSHIP_STATUS (USER_ID, FRIEND_ID)
VALUES (7, 1);

INSERT INTO FRIENDSHIP_STATUS (USER_ID, FRIEND_ID)
VALUES (8, 1);

INSERT INTO FRIENDSHIP_STATUS (USER_ID, FRIEND_ID)
VALUES (9, 1);

INSERT INTO FRIENDSHIP_STATUS (USER_ID, FRIEND_ID)
VALUES (10, 1);