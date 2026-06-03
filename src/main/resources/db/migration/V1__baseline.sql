CREATE TABLE IF NOT EXISTS member (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255),
    joined_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS organization (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS member_organization (
    id BIGSERIAL PRIMARY KEY,
    role VARCHAR(255) NOT NULL CHECK (role IN ('OWNER', 'TESTER')),
    member_id BIGINT NOT NULL REFERENCES member(id),
    organization_id BIGINT NOT NULL REFERENCES organization(id),
    CONSTRAINT uq_member_organization UNIQUE (member_id, organization_id)
);

CREATE TABLE IF NOT EXISTS feedback_board (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    organization_id BIGINT REFERENCES organization(id)
);

CREATE TABLE IF NOT EXISTS feed_back_board_item (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    details VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    category VARCHAR(255) NOT NULL CHECK (category IN ('ALL', 'UI', 'UX', 'ENHANCEMENT', 'BUG', 'FEATURE')),
    state VARCHAR(255) CHECK (state IN ('PLANNED', 'IN_PROGRESS', 'COMPLETED')),
    board_id BIGINT REFERENCES feedback_board(id),
    created_by_id BIGINT REFERENCES member(id)
);

CREATE TABLE IF NOT EXISTS up_vote (
    id BIGSERIAL PRIMARY KEY,
    upvoted_at TIMESTAMP,
    feedback_board_item_id BIGINT REFERENCES feed_back_board_item(id),
    member_id BIGINT REFERENCES member(id),
    CONSTRAINT uq_upvote UNIQUE (feedback_board_item_id, member_id)
);

CREATE TABLE IF NOT EXISTS comment (
    id BIGSERIAL PRIMARY KEY,
    body VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    feedback_board_item_id BIGINT REFERENCES feed_back_board_item(id),
    author_id BIGINT REFERENCES member(id)
);

CREATE TABLE IF NOT EXISTS sub_comment (
    id BIGSERIAL PRIMARY KEY,
    body VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    author_id BIGINT REFERENCES member(id),
    comment_id BIGINT REFERENCES comment(id),
    parent_sub_comment_id BIGINT REFERENCES sub_comment(id)
);
