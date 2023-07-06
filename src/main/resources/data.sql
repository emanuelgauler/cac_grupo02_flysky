-- Vuelo( ID, Aerolínea, conexión, destino, horario, origen, precio );
INSERT INTO vuelos (VUELOID, AEROLINEA, CONEXION, DESTINO, FECHA, ORIGEN, PRECIO)
VALUES ('001', 'Jestmar', 'false', 'CiudadA', '2023-07-15', 'CiudadB', 150.50);

INSERT INTO vuelos (VUELOID, AEROLINEA, CONEXION, DESTINO, FECHA, ORIGEN, PRECIO)
VALUES ('002', 'AirlineY', 'true', 'CiudadC', '2022-08-20', 'CiudadD', 200.75);

INSERT INTO vuelos (VUELOID, AEROLINEA, CONEXION, DESTINO, FECHA, ORIGEN, PRECIO)
VALUES ('003', 'AirlineZ', 'True', 'CiudadE', '2023-09-10', 'CiudadF', 180.25);

INSERT INTO vuelos (VUELOID, AEROLINEA, CONEXION, DESTINO, FECHA, ORIGEN, PRECIO)
VALUES ('004', 'Azul', 'True', 'mendoza', '2023-08-15', 'cordoba', 300.25);

INSERT INTO vuelos (VUELOID, AEROLINEA, CONEXION, DESTINO, FECHA, ORIGEN, PRECIO)
VALUES ('005', 'Azul', 'True', 'Venezuela', '2023-11-21', 'Argentina', 1000.85);


--Asiento( ID, Nombre asiento, ocupado, pasajero, ubicacion, vuelo_id )
INSERT INTO ASIENTOS ( NOMBRE_ASIENTO, OCUPADO,VUELOID)
VALUES ( '1A', 'false','001'),
       ('1B', 'false', '001'),
       ('1C', 'false','001'),
       ('1D', 'false','001'),
       ('2A', 'false','001'),
       ('2B', 'false','001'),
       ('2C', 'false','001'),
       ('2D', 'false','001');
