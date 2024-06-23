CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS roles
(
    id        BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS users_roles
(
    role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);