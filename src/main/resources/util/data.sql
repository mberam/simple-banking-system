
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

