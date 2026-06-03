CREATE INDEX IF NOT EXISTS idx_member_email ON member(email);
CREATE INDEX IF NOT EXISTS idx_member_name ON member(name);
CREATE INDEX IF NOT EXISTS idx_member_org_member_id ON member_organization(member_id);
CREATE INDEX IF NOT EXISTS idx_upvote_feedback_board_item_id ON up_vote(feedback_board_item_id);
CREATE INDEX IF NOT EXISTS idx_comment_feedback_board_item_id ON comment(feedback_board_item_id);
CREATE INDEX IF NOT EXISTS idx_subcomment_comment_id ON sub_comment(comment_id);
