-- Create Role Table
CREATE TABLE Role (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(30) NOT NULL
);

-- Create User Table
CREATE TABLE "User" (
    user_id SERIAL PRIMARY KEY,
    login VARCHAR(30) UNIQUE NOT NULL,
    password VARCHAR(30) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role INT NOT NULL,
    CONSTRAINT fk_role FOREIGN KEY(role) REFERENCES Role(role_id)
);

-- Create Artist Table
CREATE TABLE Artist (
    artist_id SERIAL PRIMARY KEY,
    artist_name VARCHAR(100) NOT NULL
);

-- Create Genre Table
CREATE TABLE Genre (
    genre_id SERIAL PRIMARY KEY,
    genre_name VARCHAR(50) NOT NULL
);

-- Create Album Table
CREATE TABLE Album (
    album_id SERIAL PRIMARY KEY,
    album_title VARCHAR(100) NOT NULL,
    release_date DATE,
    price DECIMAL(10, 2),
    artist_id INT NOT NULL,
    genre_id INT NOT NULL,
    CONSTRAINT fk_artist FOREIGN KEY(artist_id) REFERENCES Artist(artist_id),
    CONSTRAINT fk_genre FOREIGN KEY(genre_id) REFERENCES Genre(genre_id)
);

-- Create Track Table
CREATE TABLE Track (
    track_id SERIAL PRIMARY KEY,
    track_title VARCHAR(100) NOT NULL,
    release_date DATE,
    duration TIME NOT NULL,
    album_id INT NOT NULL,
    CONSTRAINT fk_album FOREIGN KEY(album_id) REFERENCES Album(album_id)
);

-- Create Order Table
CREATE TABLE "Order" (
    order_id SERIAL PRIMARY KEY,
    order_number VARCHAR(5) NOT NULL,
    user_id INT NOT NULL,
    total_price DECIMAL(10, 2),
    status VARCHAR(30) NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES "User"(user_id)
);

-- Create Order_Item Table
CREATE TABLE Order_Item (
    order_item_id SERIAL PRIMARY KEY,
    order_id INT NOT NULL,
    album_id INT NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT fk_order FOREIGN KEY(order_id) REFERENCES "Order"(order_id),
    CONSTRAINT fk_album FOREIGN KEY(album_id) REFERENCES Album(album_id)
);
