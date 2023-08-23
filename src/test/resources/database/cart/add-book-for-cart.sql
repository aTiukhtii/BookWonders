INSERT INTO books (id, title, author, isbn, price, cover_image, description)
VALUES (3, 'Created book', 'Stephen', '6576543210', 24.99, 'kniga_created.jpg', 'Horror book');

INSERT INTO book_category (book_id, category_id) VALUES (3, 2);
