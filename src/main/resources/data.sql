-- Vuelo( ID, Aerolínea, conexión, destino, horario, origen, precio );
insert into vuelos values(1, 'Aerolineas Argentinas', false, 'Mendoza', '2023-07-09 12:30', 'Bs. As', 45000.0),
(2, 'FlyBondi', false, 'Jujuy', '2023-07-09 11:00', 'Bs. As', 42380.5),
(3, 'Air France', true, 'Tierra del Fuego', '2023-07-10 02:00', 'Bs. As.', 58760.0),
(4, 'FlyBondi', false, 'Tierra del Fuego', '2023-07-30 05:00', 'Bs. As.', 60600.5),
(5, 'Aerolineas Argentinas', false, 'Mendoza', '2023-07-20 17:00', 'Bs. As.', 45000.0);

--Asiento( ID, Nombre asiento, ocupado, pasajero, ubicacion, vuelo_id )
insert into asientos values ( 1, '1V', false, null, 'ventanilla', 1 )
, ( 2, '1P', false, null, 'pasillo', 1 )
, ( 3, '2P', false, null, 'pasillo', 1 )
, ( 4, '2V', false, null, 'ventanilla', 1 )
, ( 5, '3V', false, null, 'ventanilla', 1 )
, ( 6, '3P', false, null, 'pasillo', 1 )
;

--Usuario
insert into usuarios values
(1,'Maxi Correa',1100000001,0),
(2,'Cristina F',1100000002,0),
(3,'Nestor K',1100000003,1),
(4,'Javier M',1100000004,1),
(5,'Pelado L',1100000005,2),
(6,'Pato B',1100000006,2);

--Reserva (Reserva_Id, Estado_Reserva, Fecha_Reserva, UsuarioID, VueloID)
INSERT INTO reservas VALUES
(1,TRUE,'2023-07-10 02:00',5,1),
(2,FALSE,'2023-07-10 02:00',5,2),
(3,TRUE,'2023-07-10 02:00',6,3);
