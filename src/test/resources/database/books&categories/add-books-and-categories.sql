INSERT INTO books (title, author, isbn, price, cover_image, description)
VALUES ('Kobzar', 'Taras Shevchenko', '1234567890', 19.99, 'kniga_kobzar.jpg', 'Kobzar Book');

INSERT INTO books (title, author, isbn, price, cover_image, description)
VALUES ('It', 'Stephen King', '9876543210', 24.99, 'kniga_it.jpg', 'Horror book');

INSERT INTO categories (name, description) VALUES ('Fiction', 'Fiction books');
INSERT INTO categories (name, description) VALUES ('Horror', 'Horror books');

INSERT INTO book_category (book_id, category_id) VALUES (1, 1);
INSERT INTO book_category (book_id, category_id) VALUES (1, 2);
INSERT INTO book_category (book_id, category_id) VALUES (2, 2);

