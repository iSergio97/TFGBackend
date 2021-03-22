-- Creación de las tablas maestras
use poh;

INSERT INTO PAIS (ID, NOMBRE) VALUES (1, 'ESPAÑA');

INSERT INTO PROVINCIA (ID, NOMBRE, PAIS_ID) VALUES (2, 'SEVILLA', 1);

INSERT INTO MUNICIPIO (ID, NOMBRE, PROVINCIA_ID) VALUES (3, 'ECIJA',  2);

INSERT INTO CALLE (ID, MUNICIPIO_ID, NOMBRE) VALUES (4, 3, 'CALLE SALTO');
INSERT INTO CALLE (ID, MUNICIPIO_ID, NOMBRE) VALUES (5, 3, 'AVENIDA DE LOS EMIGRANTES');
INSERT INTO CALLE (ID, MUNICIPIO_ID, NOMBRE) VALUES (6, 3, 'RONDA DEL FERROCARRILA');

INSERT INTO VIVIENDA (ID, NUMERO, CALLE_ID) VALUES (7, 10, 4);
INSERT INTO VIVIENDA (ID, NUMERO, CALLE_ID) VALUES (8, 8, 4);
INSERT INTO VIVIENDA (ID, NUMERO, CALLE_ID) VALUES (9, 9, 4);
INSERT INTO VIVIENDA (ID, NUMERO, CALLE_ID) VALUES (10, 35, 5);
INSERT INTO VIVIENDA (ID, NUMERO, CALLE_ID) VALUES (11, 15, 5);
INSERT INTO VIVIENDA (ID, NUMERO, CALLE_ID) VALUES (12, 21, 5);
INSERT INTO VIVIENDA (ID, NUMERO, CALLE_ID) VALUES (13, 98, 6);
INSERT INTO VIVIENDA (ID, NUMERO, CALLE_ID) VALUES (14, 110, 6);
INSERT INTO VIVIENDA (ID, NUMERO, CALLE_ID) VALUES (15, 40, 6);

INSERT INTO IDENTIFICACION (ID, CODIGO_TARJETA, NOMBRE_TARJETA) VALUES (16, 0, 'MENOR DE EDAD');
INSERT INTO IDENTIFICACION (ID, CODIGO_TARJETA, NOMBRE_TARJETA) VALUES (17, 1, 'NIF');
INSERT INTO IDENTIFICACION (ID, CODIGO_TARJETA, NOMBRE_TARJETA) VALUES (18, 2, 'PASAPORTE');
INSERT INTO IDENTIFICACION (ID, CODIGO_TARJETA, NOMBRE_TARJETA) VALUES (19, 3, 'TARJETA DE EXTRANJERÍA');