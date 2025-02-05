CREATE TABLE IF NOT EXISTS tbl_wallet (
                            id SERIAL PRIMARY KEY,
                            balance DECIMAL(10,2) NOT NULL,
                            wallet_type SMALLINT NOT NULL
);

CREATE TABLE IF NOT EXISTS tbl_common (
                        id SERIAL PRIMARY KEY,
                        full_name VARCHAR(255) NOT NULL,
                        cpf VARCHAR(11) UNIQUE NOT NULL,
                        email VARCHAR(255) UNIQUE NOT NULL,
                        password TEXT NOT NULL,
                        wallet_id BIGINT UNIQUE,
                        CONSTRAINT fk_wallet FOREIGN KEY (wallet_id) REFERENCES tbl_wallet(id)
);

CREATE TABLE IF NOT EXISTS tbl_shopkeeper (
                        id SERIAL PRIMARY KEY,
                        full_name VARCHAR(255) NOT NULL,
                        cnpj VARCHAR(14) UNIQUE NOT NULL,
                        email VARCHAR(255) UNIQUE NOT NULL,
                        password TEXT NOT NULL,
                        wallet_id BIGINT UNIQUE,
                        CONSTRAINT fk_wallet FOREIGN KEY (wallet_id) REFERENCES tbl_wallet(id)
);


CREATE TABLE IF NOT EXISTS tbl_transaction (
                              id SERIAL PRIMARY KEY,
                              amount DECIMAL(10,2) NOT NULL,
                              payee_wallet_id BIGINT NOT NULL,
                              payer_wallet_id BIGINT NOT NULL,
                              date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT fk_payee FOREIGN KEY (payee_wallet_id) REFERENCES tbl_wallet(id),
                              CONSTRAINT fk_payer FOREIGN KEY (payer_wallet_id) REFERENCES tbl_wallet(id)
);
