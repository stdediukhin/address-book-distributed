DROP DATABASE IF EXISTS address_book_accommodation_dev;
DROP DATABASE IF EXISTS address_book_history_dev;
DROP DATABASE IF EXISTS address_book_registration_dev;
DROP DATABASE IF EXISTS address_book_leasing_dev;
DROP DATABASE IF EXISTS address_book_accommodation_test;
DROP DATABASE IF EXISTS address_book_history_test;
DROP DATABASE IF EXISTS address_book_registration_test;
DROP DATABASE IF EXISTS address_book_leasing_test;

CREATE USER IF NOT EXISTS 'address_book'@'localhost'
  identified by '';
GRANT ALL PRIVILEGES ON *.* TO 'address_book' @'localhost';

CREATE DATABASE address_book_accommodation_dev;
CREATE DATABASE address_book_history_dev;
CREATE DATABASE address_book_registration_dev;
CREATE DATABASE address_book_leasing_dev;
CREATE DATABASE address_book_accommodation_test;
CREATE DATABASE address_book_history_test;
CREATE DATABASE address_book_registration_test;
CREATE DATABASE address_book_leasing_test;