CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    balance DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS operation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    cost DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    operation_id BIGINT,
    user_id BIGINT,
    amount DECIMAL(10, 2) NOT NULL,
    user_balance DECIMAL(10, 2) NOT NULL,
    operation_response VARCHAR(255) NOT NULL,
    date TIMESTAMP NOT NULL,
    FOREIGN KEY (operation_id) REFERENCES operation(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);