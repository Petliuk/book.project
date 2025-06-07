DELETE FROM books_categories;
DELETE FROM books;
DELETE FROM categories WHERE id IN (1, 2, 3);