DROP DATABASE IF EXISTS  quiz_test_db;
CREATE DATABASE quiz_test_db;
DROP USER IF EXISTS quiz_user;
CREATE USER quiz_user WITH PASSWORD 'quiz__test_123';
GRANT ALL PRIVILEGES ON DATABASE
"quiz_test_db" to quiz_user;
