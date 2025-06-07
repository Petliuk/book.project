DELETE FROM books_categories;
DELETE FROM books;
DELETE FROM categories;

INSERT INTO categories (id, name, description, is_deleted)
VALUES (1, 'Fiction', 'Fiction books', false),
       (2, 'Non-Fiction', 'Non-fiction books', false),
       (3, 'Science', 'Science books', false);

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'Test Book 1', 'Test Author 1', '1234567890', 29.99, 'A test book', 'cover1.jpg', false),
       (2, 'Test Book 2', 'Test Author 2', '0987654321', 39.99, 'Another test book', 'cover2.jpg', false),
       (3, 'Science Book', 'Science Author', '1122334455', 49.99, 'Science book', 'cover3.jpg', false);

INSERT INTO books_categories (book_id, category_id) VALUES (1, 1), (2, 2), (3, 1), (3, 3);