CREATE TABLE client (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        password VARCHAR(255),
                        username VARCHAR(100)
);


CREATE TABLE parking_sessions (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  license_plate VARCHAR(255) NOT NULL UNIQUE,
                                  entry_time DATETIME NOT NULL,
                                  exit_time DATETIME
);


CREATE TABLE detection (
                           detection_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           confidence DOUBLE NOT NULL,
                           detection_date DATETIME,
                           license_plate VARCHAR(255) NOT NULL,
                           message VARCHAR(255) NOT NULL,
                           status VARCHAR(255) NOT NULL
);
