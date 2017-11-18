CREATE TABLE "tokens" (
  "id"      BIGSERIAL PRIMARY KEY,
  "user_id" BIGINT,
  "token"   VARCHAR NOT NULL
);
ALTER TABLE "tokens" ADD CONSTRAINT "USER_FK" FOREIGN KEY ("user_id") REFERENCES "me.server.projections.users" ("id") ON UPDATE RESTRICT ON DELETE CASCADE;