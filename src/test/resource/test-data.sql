/*CREATE TABLE IF NOT EXISTS "customer"
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
    );*/


INSERT INTO "customer" ("name", "address", "email", "phone_number")
VALUES
    ('John Doe', '123 Main St', 'mihovilleapwise@gmail.com', '1234567890'),
    ('Jane Smith', '456 Oak Ave', 'mihovilleapwise@gmail.com', '9876543210'),
    ('John Doe2', '1232 Main St', 'mihovilleapwise@gmail.com', '12345678902'),
    ('Jane Smith2', '4562 Oak Ave', 'mihovilleapwise@gmail.com', '98765432102');

INSERT INTO "account" ("account_number", "account_type", "balance", "past_month_turnover", "customer_id")
VALUES
    ('123456789', 'Savings', 5000.00, 3000.00, 1),
    ('987654321', 'Checking', 10000.00, 3000.00, 2),
    ('123457789', 'Savings', 2000.00, 3000.00, 3),
    ('987650321', 'Checking', 10000.00, 3000.00, 4);

INSERT INTO "transaction" ("sender_account_id", "receiver_account_id", "amount", "currency_id", "message", "created_at")
VALUES
    (1, 2, 1000.00, 'USD', 'Payment for services', CURRENT_TIMESTAMP),
    (2, 3, 500.00, 'USD', 'Transfer to savings', CURRENT_TIMESTAMP),
    (3, 4, 200.00, 'USD', 'Online purchase', CURRENT_TIMESTAMP),
    (4, 1, 300.00, 'USD', 'Payment for rent', CURRENT_TIMESTAMP);

