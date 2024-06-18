ALTER TABLE urls
    ADD user_id BIGINT;

ALTER TABLE urls
    ADD creation_date DATE NOT NULL DEFAULT '2024-01-01';

ALTER TABLE urls
    ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE urls
    ADD click_count INTEGER DEFAULT 0;

CREATE INDEX uuid_index on urls (uuid);