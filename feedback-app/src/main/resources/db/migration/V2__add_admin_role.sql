ALTER TABLE member_organization DROP CONSTRAINT member_organization_role_check;
ALTER TABLE member_organization ADD CONSTRAINT member_organization_role_check
    CHECK (role IN ('ADMIN', 'OWNER', 'TESTER'));
