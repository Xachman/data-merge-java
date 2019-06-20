/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  xach
 * Created: Aug 5, 2018
 */

SET sql_mode = '';
SET GLOBAL sql_mode='';

DROP DATABASE database1;

DROP DATABASE database2;

CREATE DATABASE IF NOT EXISTS database1;

CREATE DATABASE IF NOT EXISTS database2;


USE database1;

CREATE TABLE IF NOT EXISTS `users` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`username` varchar(255) COLLATE utf8_bin NOT NULL,
	`email` varchar(255) COLLATE utf8_bin NOT NULL,
	PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `users_meta` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`user_id` int(11) NOT NULL,
	`meta_key` varchar(255) COLLATE utf8_bin NOT NULL,
	`meta_value` varchar(255) COLLATE utf8_bin NOT NULL,
	PRIMARY KEY(`id`),
	FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `posts` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`user_id` int(11) NOT NULL,
	`content` longtext COLLATE utf8_bin NOT NULL,
	`created_date` datetime NOT NULL,
	PRIMARY KEY(`id`),
	FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1;


INSERT INTO users (username, email) VALUES ('timtheone', 'tim@timtheone.com'),
('sassysara', 'sara@gmail.com'),
('jason', 'jason@gmail.com');

INSERT INTO users_meta (user_id, meta_key, meta_value) VALUES (1, 'address', '123 tim lane'),
(2, 'address', '567 main street'),
(2, 'phone', '555-555-5556'),
(3, 'address', '935 wall street'),
(3, 'phone', '555-555-5589'),
(2, 'home_phone', '555-555-5569');

INSERT INTO posts (user_id, content, created_date) VALUES
(1, 'Post 1 content', '2019-03-30 10:00:00'),
(2, 'Post 2 content', '2019-03-30 10:00:00'),
(3, 'Post 3 content', '2019-03-30 10:00:00');

USE database2;

CREATE TABLE IF NOT EXISTS `users` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`username` varchar(255) COLLATE utf8_bin NOT NULL,
	`email` varchar(255) COLLATE utf8_bin NOT NULL,
	PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `users_meta` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`user_id` int(11) NOT NULL,
	`meta_key` varchar(255) COLLATE utf8_bin NOT NULL,
	`meta_value` varchar(255) COLLATE utf8_bin NOT NULL,
	PRIMARY KEY(`id`),
	FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `posts` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`user_id` int(11) NOT NULL,
	`content` longtext COLLATE utf8_bin NOT NULL,
	`created_date` datetime NOT NULL,
	PRIMARY KEY(`id`),
	FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1;

INSERT INTO users (username, email) VALUES ('timtheone', 'tim@timtheone.com'),
('sassysara', 'sara@gmail.com'),
('john', 'john@gmail.com');

INSERT INTO users_meta (user_id, meta_key, meta_value) VALUES 
(1, 'address', '123 tim lane'),
(2, 'address', '567 main street'),
(3, 'address', '456 elm street'),
(2, 'phone', '555-555-5557'),
(3, 'phone', '555-555-5550'),
(2, 'home_phone', '555-555-5570');


INSERT INTO posts (user_id, content, created_date) VALUES
(1, 'Post 1 content', '2019-03-30 10:00:00'),
(2, 'Post 2 content', '2019-03-30 10:00:00'),
(3, 'Post 4 content', '0000-00-00 00:00:00');
