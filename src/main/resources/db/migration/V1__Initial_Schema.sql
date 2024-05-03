CREATE TABLE IF NOT EXISTS "customer"
(
    "id"          BIGINT AUTO_INCREMENT PRIMARY KEY,
    "name"        VARCHAR(255) NOT NULL,
    "address"     VARCHAR(255),
    "email"       VARCHAR(255),
    "phone_number" VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS "account"
(
    "id"                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    "account_number"      VARCHAR(20) NOT NULL,
    "account_type"        VARCHAR(50),
    "balance"             DECIMAL(19, 2),
    "past_month_turnover" DECIMAL(19, 2),
    "customer_id"         BIGINT,
    FOREIGN KEY ("customer_id") REFERENCES "customer" ("id")
);

CREATE TABLE IF NOT EXISTS "transaction"
(
    "id"                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    "sender_account_id"   BIGINT,
    "receiver_account_id" BIGINT,
    "amount"              DECIMAL(19, 2) NOT NULL,
    "currency_id"         VARCHAR(10),
    "message"             VARCHAR(255),
    "created_at"          TIMESTAMP,
    FOREIGN KEY ("sender_account_id") REFERENCES "account" ("id"),
    FOREIGN KEY ("receiver_account_id") REFERENCES "account" ("id")
);



