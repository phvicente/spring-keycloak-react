CREATE TABLE customer
(
    customer_id      UUID NOT NULL,
    keycloak_user_id UUID NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE,
    updated_at       TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_customer PRIMARY KEY (customer_id)
);

ALTER TABLE customer
    ADD CONSTRAINT uc_customer_keycloakuserid UNIQUE (keycloak_user_id);

CREATE TABLE loan
(
    loan_id             UUID             NOT NULL,
    customer_id         UUID             NOT NULL,
    loan_value          DOUBLE PRECISION NOT NULL,
    tax_loan            INTEGER          NOT NULL,
    total_loan_value    DOUBLE PRECISION NOT NULL,
    number_installments INTEGER          NOT NULL,
    status              VARCHAR(255),
    created_at          TIMESTAMP WITHOUT TIME ZONE,
    updated_at          TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_loan PRIMARY KEY (loan_id)
);

ALTER TABLE loan
    ADD CONSTRAINT FK_LOAN_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (customer_id);