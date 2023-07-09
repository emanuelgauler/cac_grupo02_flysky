INSERT INTO vuelos (VUELOID, AEROLINEA,ORIGEN, DESTINO, FECHA,CONEXION, PRECIO)
VALUES ('001', 'Jestmar',  'Mendoza',           'Bs. As',  '2023-09-09 12:30',  'false', 150.50),
       ('002', 'FlyBondi', 'Jujuy',             'Bs. As',  '2022-08-20 15:35',  'true',  200.75),
       ('003', 'AirlineZ', 'Tierra del Fuego',  'Bs. As',  '2023-09-10 10:25',  'True',  180.25),
       ('004', 'FlyBondi', 'mendoza',           'cordoba', '2023-08-15 15:25',  'false', 300.25),
       ('005', 'Azul',     'Venezuela',         'Bs. As',  '2023-11-21 20:00',  'True',  1000.85);

INSERT INTO ASIENTOS (NOMBRE_ASIENTO, OCUPADO,VUELOID)
VALUES ('1V', 'false','001'),
       ('1P', 'false','001'),
       ('2V', 'false','001'),
       ('2P', 'false','001'),
       ('3V', 'false','001'),
       ('3P', 'false','001'),
       ('4V', 'false','001'),
       ('4P', 'false','001'),
       ('5V', 'false','001'),
       ('5P', 'false','001'),
       ('6V', 'false','001'),
       ('6P', 'false','001'),
       --asientos vueloID 002
       ('1V', 'false','002'),
       ('1P', 'false','002'),
       --asientos vueloID 003
       ('1V', 'true', '003'),
       ('1V', 'true', '003'),
       ('1P', 'false', '003'),
       ('2V', 'false', '003'),
       ('2P', 'false', '003'),
       ('3V', 'false', '003'),
       ('3P', 'false', '003'),
       ('4V', 'false', '003'),
       ('4P', 'false', '003'),
       ('5V', 'false', '003'),
       ('5P', 'false', '003'),
       ('6V', 'false', '003'),
       ('6P', 'false', '003'),
       --asientos vueloID 004
       ('1V', 'false','004'),
       ('1P', 'false','004');

INSERT INTO usuarios (USUARIOID, NOMBRE_COMPLETO_USUARIO, TELEFONO, TIPO_USUARIO)
VALUES (1, 'John Doe', '123456789', 0),
       (2, 'Jane Smith', '987654321', 1),
       (3, 'Michael Johnson', '555555555', 2),
       (4, 'Jim Gavidia', '987654321', 2),
       (5, 'Maria Fernandez', '555555555', 2);

--Reserva (Reserva_Id, Estado_Reserva, Fecha_Reserva, UsuarioID, VueloID)
 INSERT INTO reservas (reservaID, Estado_Reserva, Fecha_Reserva, UsuarioID,VueloID,monto)
 VALUES (1,TRUE,'2023-08-10 02:00',3,1,100),
        (2,false,'2023-07-07 02:00',3,3,150),
        (3,false,'2023-08-10 02:00',4,3,150);

--cargo una los datos del asiento id 15
UPDATE ASIENTOS
SET OCUPADO = 'true', VUELOID = '003', RESERVA_ID = 2, PASAJERO = 'jim gavidia', UBICACION = 'ventana'
WHERE ASIENTOID = 15;;


