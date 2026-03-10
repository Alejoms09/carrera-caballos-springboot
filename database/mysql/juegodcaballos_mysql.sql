CREATE DATABASE IF NOT EXISTS juegodcaballos
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE juegodcaballos;

CREATE TABLE IF NOT EXISTS game_group (
  id BIGINT NOT NULL AUTO_INCREMENT,
  code VARCHAR(30) NOT NULL,
  created_at DATETIME(6) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uk_game_group_code UNIQUE (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS app_user (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(40) NOT NULL,
  password_hash VARCHAR(100) NOT NULL,
  points BIGINT NOT NULL,
  group_id BIGINT NOT NULL,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uk_app_user_username UNIQUE (username),
  CONSTRAINT fk_user_group FOREIGN KEY (group_id) REFERENCES game_group(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS bet_record (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  chosen_suit VARCHAR(20) NOT NULL,
  winner_suit VARCHAR(20) NOT NULL,
  track_length INT NOT NULL,
  faltering_enabled BIT(1) NOT NULL,
  bet_points BIGINT NOT NULL,
  payout_points BIGINT NOT NULL,
  won BIT(1) NOT NULL,
  created_at DATETIME(6) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_bet_user FOREIGN KEY (user_id) REFERENCES app_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS point_purchase (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  packages_count INT NOT NULL,
  points_added BIGINT NOT NULL,
  amount_cop BIGINT NOT NULL,
  created_at DATETIME(6) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_purchase_user FOREIGN KEY (user_id) REFERENCES app_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_bet_user_created ON bet_record (user_id, created_at);
CREATE INDEX idx_purchase_user_created ON point_purchase (user_id, created_at);

INSERT INTO game_group (code, created_at)
SELECT 'GRUPO-1', NOW(6)
WHERE NOT EXISTS (SELECT 1 FROM game_group WHERE code = 'GRUPO-1');

INSERT INTO game_group (code, created_at)
SELECT 'GRUPO-2', NOW(6)
WHERE NOT EXISTS (SELECT 1 FROM game_group WHERE code = 'GRUPO-2');

INSERT INTO game_group (code, created_at)
SELECT 'GRUPO-3', NOW(6)
WHERE NOT EXISTS (SELECT 1 FROM game_group WHERE code = 'GRUPO-3');

INSERT INTO game_group (code, created_at)
SELECT 'GRUPO-4', NOW(6)
WHERE NOT EXISTS (SELECT 1 FROM game_group WHERE code = 'GRUPO-4');
