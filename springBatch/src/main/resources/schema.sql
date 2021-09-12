DROP TABLE produkty IF EXISTS;

CREATE TABLE produkty (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nazwa VARCHAR(255) NOT NULL,
    kod_producenta VARCHAR(50) NOT NULL,
    cena_jednostkowa DECIMAL(10,4) NOT NULL,
    jednostka VARCHAR(5) NOT NULL
);