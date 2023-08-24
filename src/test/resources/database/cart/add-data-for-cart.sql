INSERT INTO books (id, title, author, isbn, price, cover_image, description)
VALUES (1 ,'Kobzar', 'Taras Shevchenko', '1234567890', "19.99", 'kniga_kobzar.jpg', 'Kobzar Book');

INSERT INTO books (id, title, author, isbn, price, cover_image, description)
VALUES (2, 'It', 'Stephen King', '9876543210', 24.99, 'kniga_it.jpg', 'Horror book');

INSERT INTO categories (id, name, description) VALUES (1, 'Fiction', 'Fiction books');
INSERT INTO categories (id, name, description) VALUES (2, 'Horror', 'Horror books');

INSERT INTO book_category (book_id, category_id) VALUES (1, 1);
INSERT INTO book_category (book_id, category_id) VALUES (1, 2);
INSERT INTO book_category (book_id, category_id) VALUES (2, 2);

INSERT INTO users (id, email, password, first_name, last_name, shipping_address)
VALUES (1, 'test@example.com', '$2a$10$dI4u349ROIW8hHvw4rKcjeix4YCYhq/qOdf0uDatSjBglv7aimHZS', 'Test', 'User', 'Test Address');


INSERT INTO shopping_carts (user_id)
VALUES (1);

INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity)
VALUES (1, 1, 1, 2);

INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity)
VALUES (2, 1, 2, 4);

