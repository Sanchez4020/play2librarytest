# User schema

# --- !Ups
create table `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `email` TEXT NOT NULL,
    `password` VARCHAR(255) NOT NULL
);

create table `book` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `author` VARCHAR(255),
    `content` TEXT,
    `users_id` BIGINT NOT NULL
);



