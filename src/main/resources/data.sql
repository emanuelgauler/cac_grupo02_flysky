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

--Usuario (reservas, tipo_usuario,nombre_completo_usuario,telefono)
insert into usuarios values
(1,'AGENTE_VENTAS','Maxi Correa',11000000001),
(2,'CLIENTE','Pepe Luis',11000000002),
(3,'ADMINISTRADOR','Pechugas Laru',11000000003);