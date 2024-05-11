ALTER TABLE urls
ADD user_id BIGINT,
ADD creation_date DATE NOT NULL;

ALTER TABLE urls
ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id);