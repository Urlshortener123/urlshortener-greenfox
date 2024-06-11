ALTER TABLE urls
    ADD user_id BIGINT;

ALTER TABLE urls
    ADD creation_date DATE NOT NULL DEFAULT '2024-01-01';

ALTER TABLE urls
    ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE users
    ADD email VARCHAR(255) NOT NULL;

ALTER TABLE users
    ADD email_verified BOOLEAN DEFAULT FALSE;

CREATE TABLE IF NOT EXISTS pending_user_verifications
(
    id      BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    hash    VARCHAR(50) NOT NULL,
    user_id BIGINT      NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);