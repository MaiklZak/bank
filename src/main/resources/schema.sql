CREATE TABLE IF NOT EXISTS client (
    id UUID PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    passport VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS  credit (
    id UUID PRIMARY KEY,
    limit_on DECIMAL(12, 2) NOT NULL,
    interest_rate DECIMAL(5, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS  bank (
    id UUID PRIMARY KEY,
    client_id UUID NOT NULL,
    credit_id UUID NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE,
    FOREIGN KEY (credit_id) REFERENCES credit(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS offer (
    id UUID PRIMARY KEY,
    client_id UUID NOT NULL,
    credit_id UUID NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE,
    FOREIGN KEY (credit_id) REFERENCES credit(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS schedule_payment (
    id UUID PRIMARY KEY,
    date_payment DATE NOT NULL,
    amount_payment DECIMAL(12, 2) NOT NULL,
    body_repayment DECIMAL(12, 2) NOT NULL,
    interest_repayment DECIMAL(12, 2) NOT NULL,
    offer_id UUID NOT NULL,
    FOREIGN KEY (offer_id) REFERENCES offer(id) ON DELETE CASCADE
);