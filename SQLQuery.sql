CREATE DATABASE db_logistic_weather_lgsm
USE db_logistic_weather_lgsm

CREATE TABLE shipment (
    id INT IDENTITY(1,1) PRIMARY KEY,
    product_code VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    dest_city VARCHAR(100) NOT NULL,
    dest_lat DECIMAL(9,6) NOT NULL,
    dest_lng DECIMAL(9,6) NOT NULL,
    dispatch_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'PLANNED',
    risk_level VARCHAR(10),
    is_deleted INT DEFAULT 0,
    created_at DATETIME2 DEFAULT GETDATE()
);

CREATE TABLE weather_cache (
    id INT IDENTITY(1,1) PRIMARY KEY,
    dest_lat DECIMAL(9,6) NOT NULL,
    dest_lng DECIMAL(9,6) NOT NULL,
    forecast_date DATE NOT NULL,
    precip_mm DECIMAL(6,2),
    weather_code INT,
    fetched_at DATETIME2 DEFAULT GETDATE()
);

INSERT INTO shipment (product_code, quantity, dest_city, dest_lat, dest_lng, dispatch_date, status, is_deleted)
VALUES 
('BRG01', 150, 'Jakarta', -6.208800, 106.845600, CAST(GETDATE() AS DATE), 'PLANNED', 0),
('BRG02', 300, 'Medan', 3.595200, 98.672200, CAST(GETDATE() AS DATE), 'DISPATCHED', 0)

SELECT * FROM shipment
SELECT * FROM  weather_cache