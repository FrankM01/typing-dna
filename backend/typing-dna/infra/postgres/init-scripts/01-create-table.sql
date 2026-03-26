-- Creación de la tabla Usuario
CREATE TABLE usuarios
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username      VARCHAR(50) UNIQUE  NOT NULL,
    email         VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    is_active     BOOLEAN                      DEFAULT true,
    created_at    TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Creación de la tabla BiometricProfile
CREATE TABLE biometric_profiles
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id       BIGINT UNIQUE NOT NULL, -- UNIQUE para asegurar la relación OneToOne
    dwell_vector  JSONB         NOT NULL,
    flight_vector JSONB         NOT NULL,
    samples_count INTEGER                DEFAULT 0,
    updated_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_usuario_biometric
        FOREIGN KEY (user_id)
            REFERENCES usuarios (id)
            ON DELETE CASCADE
);

-- Creación de la tabla AuthAttempt
CREATE TABLE auth_attempts
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id          BIGINT           NOT NULL,
    similarity_score DOUBLE PRECISION NOT NULL,
    success          BOOLEAN          NOT NULL,
    ip_address       VARCHAR(45) NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Agregado por buena práctica

    CONSTRAINT fk_usuario_attempt
        FOREIGN KEY (user_id)
            REFERENCES usuarios (id)
            ON DELETE CASCADE
);

ALTER TABLE biometric_profiles ADD COLUMN threshold DOUBLE PRECISION DEFAULT 0.75;
