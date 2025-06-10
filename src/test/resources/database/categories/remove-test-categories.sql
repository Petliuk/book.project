DELETE FROM books_categories WHERE book_id IN (1, 2) OR category_id IN (1, 2);

UPDATE books SET is_deleted = false WHERE id IN (1, 2);
DELETE FROM books WHERE id IN (1, 2);

UPDATE categories SET is_deleted = false WHERE id IN (1, 2);
DELETE FROM categories WHERE id IN (1, 2);