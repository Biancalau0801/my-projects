
-- create cinema
CREATE TABLE cinema (
    cinema_id INT PRIMARY KEY,
    name VARCHAR(100),
    address VARCHAR(255),
    postal_code VARCHAR(10),
    state VARCHAR(50),
    contact_no VARCHAR(20)
);

INSERT INTO cinema (cinema_id, name, address, postal_code, state, contact_no) VALUES
(1, 'TGV Sunway Pyramid', 'LG2.126, Sunway Pyramid, Bandar Sunway', '47500', 'Selangor', '+60-3-7492-3800'),
(2, 'TGV 1 Utama', 'Lot F319, 1 Utama Shopping Centre, PJ', '47800', 'Selangor', '+60-3-7722-1600'),
(3, 'TGV KLCC', 'Level 3, Suria KLCC, Kuala Lumpur', '50088', 'Kuala Lumpur', '+60-3-2382-2828'),
(4, 'TGV Pavilion KL', 'Lot 6.01, Level 6, Pavilion Kuala Lumpur', '55100', 'Kuala Lumpur', '+60-3-2110-3600'),
(5, 'TGV Aeon Bukit Tinggi', 'Aeon Mall Bukit Tinggi, Klang', '41200', 'Selangor', '+60-3-3324-1688');

select * from cinema;
-- end cinema

-- create hall
CREATE TABLE hall (
    hall_id INT PRIMARY KEY,
    cinema_id INT,
    hall_num INT,
    capacity INT,
    hall_type VARCHAR(50),
    FOREIGN KEY (cinema_id) REFERENCES cinema(cinema_id)
);

INSERT INTO hall (hall_id, cinema_id, hall_num, hall_type) VALUES
(1, 1, 1, 'Standard'),
(2, 1, 2, 'IMAX'),
(3, 1, 3, 'Beanie'),
(4, 2, 1, 'Standard'),
(5, 2, 2, 'Deluxe'),
(6, 2, 3, 'Luxe'),
(7, 3, 1, 'Beanie'),
(8, 3, 2, 'Standard');

select * from hall;
-- end hall

-- create seat
CREATE TABLE seat (
    seat_id INT PRIMARY KEY,
    hall_id INT,
    seat_row VARCHAR(5),
    seat_col INT,
    FOREIGN KEY (hall_id) REFERENCES hall(hall_id)
);

INSERT INTO seat (seat_id, hall_id, seat_row, seat_col) VALUES
(1, 1, 'A', 1), (2, 1, 'A', 2), (3, 1, 'A', 3), (4, 1, 'A', 4),
(5, 1, 'B', 1), (6, 1, 'B', 2), (7, 1, 'B', 3), (8, 1, 'C', 1),
(9, 1, 'C', 2), (10, 1, 'C', 3), (11, 2, 'A', 1), (12, 2, 'A', 2),
(13, 2, 'A', 3), (14, 2, 'A', 4), (15, 2, 'B', 1), (16, 2, 'B', 2),
(17, 2, 'B', 3), (18, 2, 'C', 1), (19, 2, 'C', 2), (20, 2, 'C', 3),
(21, 3, 'A', 1), (22, 3, 'A', 2), (23, 3, 'A', 3), (24, 3, 'A', 4),
(25, 3, 'B', 1), (26, 3, 'B', 2), (27, 3, 'B', 3), (28, 3, 'C', 1),
(29, 3, 'C', 2), (30, 3, 'C', 3), (31, 4, 'A', 1), (32, 4, 'A', 2),
(33, 4, 'A', 3), (34, 4, 'A', 4), (35, 4, 'B', 1), (36, 4, 'B', 2),
(37, 4, 'B', 3), (38, 4, 'C', 1), (39, 4, 'C', 2), (40, 4, 'C', 3),
(41, 5, 'A', 1), (42, 5, 'A', 2), (43, 5, 'A', 3), (44, 5, 'A', 4),
(45, 5, 'B', 1), (46, 5, 'B', 2), (47, 5, 'B', 3), (48, 5, 'C', 1),
(49, 5, 'C', 2), (50, 5, 'C', 3), (51, 6, 'A', 1), (52, 6, 'A', 2),
(53, 6, 'A', 3), (54, 6, 'A', 4), (55, 6, 'B', 1), (56, 6, 'B', 2),
(57, 6, 'B', 3), (58, 6, 'C', 1), (59, 6, 'C', 2), (60, 6, 'C', 3),
(61, 7, 'A', 1), (62, 7, 'A', 2), (63, 7, 'A', 3), (64, 7, 'A', 4),
(65, 7, 'B', 1), (66, 7, 'B', 2), (67, 7, 'B', 3), (68, 7, 'C', 1),
(69, 7, 'C', 2), (70, 7, 'C', 3), (71, 8, 'A', 1), (72, 8, 'A', 2),
(73, 8, 'A', 3), (74, 8, 'A', 4), (75, 8, 'B', 1), (76, 8, 'B', 2),
(77, 8, 'B', 3), (78, 8, 'C', 1), (79, 8, 'C', 2), (80, 8, 'C', 3);

select * from seat;
-- end seat


-- create movie
CREATE TABLE movie (
    movie_id INT PRIMARY KEY,
    title VARCHAR(150),
    genre VARCHAR(50),
    duration INT, -- duration in minutes
    language VARCHAR(50),
    release_date DATE,
    rating VARCHAR(10),
    sinopsis TEXT,
    director VARCHAR(100),
    status VARCHAR(50),
    subtitles VARCHAR(100),
    main_cast VARCHAR(255)
);

INSERT INTO movie (movie_id, title, genre, duration, language, release_date, rating, sinopsis, director, status, subtitles, main_cast) VALUES
(1, 'Furiosa: A Mad Max Saga', 'Action', 148, 'English', '2025-05-23', '18+', 'In a post-apocalyptic wasteland, Furiosa fights to return to her tribe.', 'George Miller', 'Now Showing', 'English, Malay', 'Anya Taylor-Joy, Chris Hemsworth'),
(2, 'The Garfield Movie', 'Animation', 101, 'English', '2025-05-30', 'U', 'Garfield reunites with his streetwise father in a wild outdoor journey.', 'Mark Dindal', 'Now Showing', 'English, Malay', 'Chris Pratt, Samuel L. Jackson'),
(3, 'Kingdom of the Planet of Apes', 'Sci-Fi', 145, 'English', '2025-05-26', 'P13', 'Generations after Caesar, apes rule and humans hide in shadows.', 'Wes Ball', 'Now Showing', 'English, Malay', 'Owen Teague, Freya Allan'),
(4, 'Abang Long Fadil 4', 'Comedy', 120, 'Malay', '2025-05-25', 'P13', 'Fadil is caught in a new secret agency mission full of chaos and laughs.', 'Syafiq Yusof', 'Now Showing', 'English', 'Zizan Razak, Johan Raja Lawak'),
(5, 'The Conjuring: Devil’s Playbook', 'Horror', 112, 'English', '2025-05-27', '18+', 'Paranormal investigators tackle their most dangerous case yet.', 'Michael Chaves', 'Now Showing', 'English, Malay', 'Vera Farmiga, Patrick Wilson');

SELECT * FROM movie;
-- end movie

-- create showtime
CREATE TABLE showtime (
    showtime_id INT PRIMARY KEY,
    movie_id INT,
    hall_id INT,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    FOREIGN KEY (movie_id) REFERENCES movie(movie_id),
    FOREIGN KEY (hall_id) REFERENCES hall(hall_id)
);

INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (1, 4, 7, '2025-06-01 08:45:00', '2025-06-01 10:45:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (2, 3, 7, '2025-06-04 18:45:00', '2025-06-04 21:10:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (3, 3, 1, '2025-06-02 09:30:00', '2025-06-02 11:55:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (4, 1, 8, '2025-06-03 17:00:00', '2025-06-03 19:28:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (5, 5, 6, '2025-06-02 12:45:00', '2025-06-02 14:37:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (6, 3, 5, '2025-06-03 14:15:00', '2025-06-03 16:40:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (7, 4, 1, '2025-06-03 14:15:00', '2025-06-03 16:15:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (8, 3, 3, '2025-06-04 19:15:00', '2025-06-04 21:40:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (9, 5, 2, '2025-06-01 16:00:00', '2025-06-01 17:52:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (10, 2, 5, '2025-06-03 16:30:00', '2025-06-03 18:11:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (11, 3, 1, '2025-06-01 10:30:00', '2025-06-01 12:55:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (12, 4, 2, '2025-06-02 16:00:00', '2025-06-02 18:00:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (13, 4, 5, '2025-06-03 09:15:00', '2025-06-03 11:15:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (14, 1, 3, '2025-06-01 12:30:00', '2025-06-01 14:58:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (15, 5, 1, '2025-06-01 16:15:00', '2025-06-01 18:07:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (16, 4, 6, '2025-06-03 12:15:00', '2025-06-03 14:15:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (17, 1, 2, '2025-06-03 13:00:00', '2025-06-03 15:28:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (18, 5, 2, '2025-06-03 18:30:00', '2025-06-03 20:22:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (19, 4, 2, '2025-06-02 08:15:00', '2025-06-02 10:15:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (20, 5, 3, '2025-06-02 09:00:00', '2025-06-02 10:52:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (21, 1, 2, '2025-06-04 13:15:00', '2025-06-04 15:43:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (22, 1, 7, '2025-06-01 16:45:00', '2025-06-01 19:13:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (23, 1, 1, '2025-06-02 15:30:00', '2025-06-02 17:58:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (24, 5, 8, '2025-06-03 20:30:00', '2025-06-03 22:22:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (25, 2, 3, '2025-06-01 14:45:00', '2025-06-01 16:26:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (26, 4, 6, '2025-06-04 14:30:00', '2025-06-04 16:30:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (27, 1, 3, '2025-06-03 20:45:00', '2025-06-03 23:13:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (28, 1, 6, '2025-06-03 19:30:00', '2025-06-03 21:58:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (29, 1, 4, '2025-06-02 18:45:00', '2025-06-02 21:13:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (30, 1, 2, '2025-06-02 09:15:00', '2025-06-02 11:43:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (31, 1, 6, '2025-06-02 13:15:00', '2025-06-02 15:43:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (32, 5, 3, '2025-06-03 15:30:00', '2025-06-03 17:22:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (33, 4, 2, '2025-06-03 13:30:00', '2025-06-03 15:30:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (34, 4, 6, '2025-06-02 09:00:00', '2025-06-02 11:00:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (35, 4, 2, '2025-06-03 14:45:00', '2025-06-03 16:45:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (36, 3, 3, '2025-06-01 10:00:00', '2025-06-01 12:25:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (37, 2, 7, '2025-06-03 10:15:00', '2025-06-03 11:56:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (38, 2, 1, '2025-06-02 10:15:00', '2025-06-02 11:56:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (39, 5, 2, '2025-06-04 11:00:00', '2025-06-04 12:52:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (40, 1, 5, '2025-06-02 13:00:00', '2025-06-02 15:28:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (41, 4, 4, '2025-06-01 09:15:00', '2025-06-01 11:15:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (42, 1, 3, '2025-06-01 18:15:00', '2025-06-01 20:43:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (43, 4, 8, '2025-06-01 13:45:00', '2025-06-01 15:45:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (44, 5, 6, '2025-06-01 11:15:00', '2025-06-01 13:07:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (45, 3, 6, '2025-06-01 18:45:00', '2025-06-01 21:10:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (46, 3, 6, '2025-06-02 15:45:00', '2025-06-02 18:10:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (47, 5, 3, '2025-06-02 08:30:00', '2025-06-02 10:22:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (48, 4, 6, '2025-06-04 16:45:00', '2025-06-04 18:45:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (49, 2, 5, '2025-06-02 18:15:00', '2025-06-02 19:56:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (50, 5, 7, '2025-06-01 12:45:00', '2025-06-01 14:37:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (51, 2, 5, '2025-06-03 11:45:00', '2025-06-03 13:26:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (52, 2, 1, '2025-06-03 17:15:00', '2025-06-03 18:56:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (53, 2, 4, '2025-06-03 13:00:00', '2025-06-03 14:41:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (54, 5, 4, '2025-06-01 15:45:00', '2025-06-01 17:37:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (55, 1, 6, '2025-06-04 19:45:00', '2025-06-04 22:13:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (56, 2, 1, '2025-06-04 08:45:00', '2025-06-04 10:26:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (57, 2, 6, '2025-06-04 14:45:00', '2025-06-04 16:26:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (58, 5, 1, '2025-06-01 12:45:00', '2025-06-01 14:37:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (59, 3, 5, '2025-06-01 17:45:00', '2025-06-01 20:10:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (60, 3, 5, '2025-06-03 09:30:00', '2025-06-03 11:55:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (61, 2, 6, '2025-06-01 19:45:00', '2025-06-01 21:26:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (62, 1, 5, '2025-06-02 11:30:00', '2025-06-02 13:58:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (63, 2, 6, '2025-06-02 08:45:00', '2025-06-02 10:26:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (64, 1, 5, '2025-06-02 15:15:00', '2025-06-02 17:43:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (65, 4, 7, '2025-06-04 13:15:00', '2025-06-04 15:15:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (66, 2, 2, '2025-06-01 18:30:00', '2025-06-01 20:11:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (67, 4, 1, '2025-06-03 14:00:00', '2025-06-03 16:00:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (68, 5, 1, '2025-06-02 12:00:00', '2025-06-02 13:52:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (69, 1, 3, '2025-06-01 20:30:00', '2025-06-01 22:58:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (70, 3, 3, '2025-06-02 20:45:00', '2025-06-02 23:10:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (71, 3, 7, '2025-06-01 11:30:00', '2025-06-01 13:55:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (72, 5, 4, '2025-06-02 14:45:00', '2025-06-02 16:37:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (73, 4, 5, '2025-06-03 15:30:00', '2025-06-03 17:30:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (74, 3, 7, '2025-06-04 12:30:00', '2025-06-04 14:55:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (75, 2, 1, '2025-06-03 10:45:00', '2025-06-03 12:26:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (76, 4, 2, '2025-06-01 18:45:00', '2025-06-01 20:45:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (77, 3, 4, '2025-06-01 11:15:00', '2025-06-01 13:40:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (78, 5, 3, '2025-06-04 15:00:00', '2025-06-04 16:52:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (79, 3, 4, '2025-06-02 11:45:00', '2025-06-02 14:10:00');
INSERT INTO showtime (showtime_id, movie_id, hall_id, start_time, end_time) VALUES (80, 1, 8, '2025-06-01 16:45:00', '2025-06-01 19:13:00');

select * from showtime;
-- end showtime

-- create customer
CREATE TABLE customer (
    customer_id INT PRIMARY KEY,
    username VARCHAR(20),
    full_name VARCHAR(50),
    email VARCHAR(50),
    phone VARCHAR(50),
    password VARCHAR(50)
);

INSERT INTO customer (customer_id, username, full_name, email, phone, password)
VALUES
(1, 'user1', 'Alice Tan', 'user2341@example.com', '1123451234', 'password1'),
(2, 'user2', 'Ben Lim', 'user5890@example.com', '1182349123', 'password2'),
(3, 'user3', 'Chong Mei Ling', 'user3477@example.com', '1023811238', 'password3'),
(4, 'user4', 'David Lee', 'user1623@example.com', '1234567890', 'password4'),
(5, 'user5', 'Elaine Wong', 'user9102@example.com', '1999881234', 'password5'),
(6, 'user6', 'Faridah Ahmad', 'user7712@example.com', '177771000', 'password6'),
(7, 'user7', 'Ganesh Kumar', 'user5019@example.com', '188884321', 'password7'),
(8, 'user8', 'Hui Ying', 'user8462@example.com', '1155119333', 'password8'),
(9, 'user9', 'Imran Shah', 'user3740@example.com', '1933334444', 'password9'),
(10, 'user10', 'Jasmine Chong', 'user2984@example.com', '168225555', 'password10'),
(11, 'user11', 'Kelvin Ong', 'user8153@example.com', '1210101010', 'password11'),
(12, 'user12', 'Liyana Mustaffa', 'user9910@example.com', '1812340987', 'password12');

select * from customer;
-- end customer

-- create booking
CREATE TABLE booking (
    booking_id INT PRIMARY KEY,
    customer_id INT,
    total_price DECIMAL(6,2),
    booking_time TIMESTAMP,
    payment_status VARCHAR(50),
    payment_time TIMESTAMP,
    payment_method VARCHAR(50),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

INSERT INTO booking (booking_id, customer_id, total_price, booking_time, payment_status, payment_time, payment_method)
VALUES
(1, 1, 69.00, '2025-05-29 11:50:00', 'paid', '2025-05-29 11:50:00', 'ewallet'),
(2, 1, 75.00, '2025-05-28 10:27:00', 'paid', '2025-05-28 10:27:00', 'ewallet'),
(3, 1, 45.00, '2025-05-24 20:32:00', 'paid', '2025-05-24 20:32:00', 'ewallet'),
(4, 2, 150.00, '2025-05-25 20:32:00', 'paid', '2025-05-25 20:32:00', 'credit_card'),
(5, 2, 86.00, '2025-05-26 20:32:00', 'paid', '2025-05-26 20:32:00', 'credit_card'),
(6, 3, 60.00, '2025-05-27 20:32:00', 'paid', '2025-05-27 20:32:00', 'ewallet'),
(7, 3, 96.00, '2025-05-28 20:32:00', 'paid', '2025-05-28 20:32:00', 'ewallet'),
(8, 4, 48.00, '2025-05-29 20:32:00', 'paid', '2025-05-29 20:32:00', 'credit_card'),
(9, 4, 118.00, '2025-05-30 20:32:00', 'paid', '2025-05-30 20:32:00', 'credit_card'),
(10, 5, 60.00, '2025-05-27 20:32:00', 'paid', '2025-05-27 20:32:00', 'ewallet'),
(11, 6, 185.00, '2025-05-28 20:32:00', 'paid', '2025-05-28 20:32:00', 'ewallet'),
(12, 7, 120.00, '2025-05-29 20:32:00', 'paid', '2025-05-29 20:32:00', 'ewallet'),
(13, 8, 151.00, '2025-05-30 20:32:00', 'paid', '2025-05-30 20:32:00', 'credit_card'),
(14, 9, 120.00, '2025-05-27 20:32:00', 'paid', '2025-05-27 20:32:00', 'ewallet'),
(15, 10, 120.00, '2025-05-28 20:32:00', 'paid', '2025-05-28 20:32:00', 'credit_card');

select * from booking;
-- end booking


-- create ticket
CREATE TABLE ticket (
    ticket_id INT PRIMARY KEY,
    showtime_id INT,
    seat_id INT,
    booking_id INT,
    price DECIMAL(6,2),
    FOREIGN KEY (showtime_id) REFERENCES showtime(showtime_id),
    FOREIGN KEY (seat_id) REFERENCES seat(seat_id),
    FOREIGN KEY (booking_id) REFERENCES booking(booking_id)
);


INSERT INTO ticket (ticket_id, showtime_id, seat_id, booking_id, price)
VALUES
(1, 1, 61, 1, 15.00),
(2, 1, 62, 1, 15.00),
(3, 1, 63, 2, 15.00),
(4, 1, 64, 2, 15.00),
(5, 1, 65, 2, 15.00),
(6, 1, 66, 2, 15.00),
(7, 1, 67, 2, 15.00),
(8, 1, 68, 3, 15.00),
(9, 1, 69, 3, 15.00),
(10, 1, 70, 3, 15.00),
(11, 2, 61, 4, 15.00),
(12, 2, 62, 4, 15.00),
(13, 2, 63, 4, 15.00),
(14, 2, 64, 4, 15.00),
(15, 2, 65, 4, 15.00),
(16, 2, 66, 4, 15.00),
(17, 2, 67, 4, 15.00),
(18, 2, 68, 4, 15.00),
(19, 2, 69, 4, 15.00),
(20, 2, 70, 4, 15.00),
(21, 3, 1, 5, 12.00),
(22, 3, 2, 5, 12.00),
(23, 3, 3, 5, 12.00),
(24, 3, 4, 5, 12.00),
(25, 3, 5, 5, 12.00),
(26, 3, 6, 6, 12.00),
(27, 3, 7, 6, 12.00),
(28, 3, 8, 6, 12.00),
(29, 3, 9, 6, 12.00),
(30, 3, 10, 6, 12.00),
(31, 4, 71, 7, 12.00),
(32, 4, 72, 7, 12.00),
(33, 4, 73, 7, 12.00),
(34, 4, 74, 7, 12.00),
(35, 4, 75, 7, 12.00),
(36, 4, 76, 7, 12.00),
(37, 4, 77, 8, 12.00),
(38, 4, 78, 8, 12.00),
(39, 4, 79, 8, 12.00),
(40, 4, 80, 8, 12.00),
(41, 5, 51, 9, 15.00),
(42, 5, 52, 9, 15.00),
(43, 5, 53, 9, 15.00),
(44, 5, 54, 9, 15.00),
(45, 5, 55, 9, 15.00),
(46, 5, 56, 9, 15.00),
(47, 5, 57, 10, 15.00),
(48, 5, 58, 10, 15.00),
(49, 5, 59, 10, 15.00),
(50, 5, 60, 10, 15.00),
(51, 6, 41, 11, 15.00),
(52, 6, 42, 11, 15.00),
(53, 6, 43, 11, 15.00),
(54, 6, 44, 11, 15.00),
(55, 6, 45, 11, 15.00),
(56, 6, 46, 11, 15.00),
(57, 6, 47, 11, 15.00),
(58, 6, 48, 11, 15.00),
(59, 6, 49, 11, 15.00),
(60, 6, 50, 11, 15.00),
(61, 7, 1, 12, 12.00),
(62, 7, 2, 12, 12.00),
(63, 7, 3, 12, 12.00),
(64, 7, 4, 12, 12.00),
(65, 7, 5, 12, 12.00),
(66, 7, 6, 12, 12.00),
(67, 7, 7, 12, 12.00),
(68, 7, 8, 12, 12.00),
(69, 7, 9, 12, 12.00),
(70, 7, 10, 12, 12.00),
(71, 8, 21, 13, 12.00),
(72, 8, 22, 13, 12.00),
(73, 8, 23, 13, 12.00),
(74, 8, 24, 13, 12.00),
(75, 8, 25, 13, 12.00),
(76, 8, 26, 13, 12.00),
(77, 8, 27, 13, 12.00),
(78, 8, 28, 13, 12.00),
(79, 8, 29, 13, 12.00),
(80, 8, 30, 13, 12.00),
(81, 9, 11, 14, 15.00),
(82, 9, 12, 14, 15.00),
(83, 9, 13, 14, 15.00),
(84, 9, 14, 14, 15.00),
(85, 9, 15, 14, 15.00),
(86, 9, 16, 14, 15.00),
(87, 9, 17, 14, 15.00),
(88, 9, 18, 14, 15.00),
(89, 9, 19, 14, 15.00),
(90, 9, 20, 14, 15.00),
(91, 10, 41, 15, 15.00),
(92, 10, 42, 15, 15.00),
(93, 10, 43, 15, 15.00),
(94, 10, 44, 15, 15.00),
(95, 10, 45, 15, 15.00),
(96, 10, 46, 15, 15.00),
(97, 10, 47, 15, 15.00),
(98, 10, 48, 15, 15.00),
(99, 10, 49, 15, 15.00),
(100, 10, 50, 15, 15.00);

select * from ticket;
-- end ticket


-- create food
CREATE TABLE food (
    food_id INT PRIMARY KEY,
    package_name VARCHAR(50),
    quantity INT,
    total_price DECIMAL(6,2)
);

INSERT INTO food (food_id, package_name, quantity, total_price)
VALUES 
(1, 'Combo 1 (Popcorn + Soft Drink)', 1, 15.00),
(2, 'Combo 2 (Large Popcorn + 2 Drinks)', 1, 20.00),
(3, 'Combo 3 (Hot Dog + Drink)', 1, 11.00),
(4, 'Combo 4 (Nachos + Drink)', 1, 13.00);

select * from food;
-- end food

-- create booking_food
CREATE TABLE booking_food (
    booking_food_id INT PRIMARY KEY,
    booking_id INT,
    food_id INT,
    quantity INT,
    FOREIGN KEY (booking_id) REFERENCES booking(booking_id),
    FOREIGN KEY (food_id) REFERENCES food(food_id)
);

INSERT INTO booking_food (booking_food_id, booking_id, food_id, quantity) VALUES
(1, 1, 1, 1),
(2, 3, 2, 1),
(3, 5, 3, 1),
(4, 7, 4, 1),
(5, 9, 1, 1),
(6, 11, 2, 1),
(7, 13, 3, 1),
(8, 1, 4, 1),
(9, 3, 1, 1),
(10, 5, 2, 1),
(11, 7, 3, 1),
(12, 9, 4, 1),
(13, 11, 1, 1),
(14, 13, 2, 1),
(15, 1, 3, 1);

select * from booking_food;
-- end booking_food

-- show all of tables
select * from cinema;
select * from hall;
select * from seat;
select * from movie;
select * from showtime;
select * from customer;
select * from booking;
select * from ticket;
select * from food;
select * from booking_food;
--

