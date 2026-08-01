-- Insert 10 Books
INSERT INTO book (book_name, writer_name, selling_price, stock_quantity, category) VALUES
('Finance magazine', 'The Nature Team', 340, 100, 'FINANCE'),
('Spring Boot Deep Dive', 'Joshua Bloch', 850, 50, 'SCIENCE'),
('The Jaipur History', 'R.K. Narayan', 450, 30, 'ARTS'),
('Investing 101', 'Benjamin Graham', 600, 120, 'FINANCE'),
('Physics for Kids', 'Albert E.', 299, 200, 'KIDS'),
('The Silent Patient', 'Alex Michaelides', 350, 80, 'NOVEL'),
('Atomic Habits', 'James Clear', 550, 150, 'SCIENCE'),
('Stories of Malgudi', 'R.K. Narayan', 250, 40, 'KIDS'),
('Java: The Complete Reference', 'Herbert Schildt', 950, 60, 'SCIENCE'),
('The Alchemist', 'Paulo Coelho', 300, 90, 'NOVEL');

-- Insert 10 Users
INSERT INTO store_users (user_name, gender, user_status) VALUES
('Hitesh_Jaipur', 'MALE', 'PROFESSIONAL'),
('Anjali_Sharma', 'FEMALE', 'STUDENT'),
('Rahul_Verma', 'MALE', 'PROFESSIONAL'),
('Priya_Singh', 'FEMALE', 'HOMEMAKER'),
('Amit_Gupta', 'MALE', 'STUDENT'),
('Suresh_Raina', 'MALE', 'PROFESSIONAL'),
('Megha_Jain', 'FEMALE', 'STUDENT'),
('Vikram_Aditya', 'MALE', 'PROFESSIONAL'),
('Sonia_Gandhi', 'FEMALE', 'HOMEMAKER'),
('Karan_Johar', 'NOT_SPECIFIED', 'PROFESSIONAL');