# FlySky

FlySky es un sistema de reserva de vuelos que ofrece a los usuarios una forma rápida y sencilla de buscar, seleccionar y reservar vuelos. El sistema ha sido desarrollado en Java utilizando Spring y JPA, y se basa en una estructura modular que incluye controladores, DTOs (Objetos de Transferencia de Datos), entidades, excepciones, repositorios, servicios y pruebas, tanto con mocking, como sin él.

El sistema de ventas de vuelos de FlySky tiene como objetivo mejorar la experiencia del cliente al simplificar el proceso de reserva de boletos, ofrecer opciones de vuelo más completas y garantizar transacciones seguras. Para lograr esto, se ha desarrollado una API RESTful en Java que implementa dichas funcionalidades. Al utilizar esta API, FlySky busca fortalecer su posición en el mercado de agencias de viajes y brindar un servicio excepcional a sus clientes, superando sus expectativas y fomentando relaciones comerciales duraderas.

## Características principales

### Obtener lista de vuelos disponibles (Consultar Vuelos)

Los usuarios pueden acceder a una lista de vuelos disponibles con información detallada, como horarios, precios y aerolíneas, lo que les permite elegir el vuelo que mejor se adapte a sus necesidades.

### Proceso de reserva de boletos (Reservar Vuelo)

Los usuarios pueden realizar reservas de vuelos ingresando los detalles requeridos, como nombres de pasajeros, fechas de viaje y preferencias de asientos. Este proceso guía a los usuarios paso a paso, asegurándose de que tengan un asiento reservado en el vuelo deseado.

### Integración de pagos seguros (Pagar Reserva)

El sistema cuenta con un módulo de pago seguro que ofrece diversas opciones de pago, como tarjetas de crédito, tarjeta de débito, efectivo, transferencias bancarias. Los usuarios pueden realizar transacciones de forma segura y recibir confirmaciones de pago inmediatas.

### Gestión de clientes (Historial Reservas)

FlySky permite a los agentes de ventas, y al sistema en sí, administrar y mantener un registro actualizado de los datos de los clientes. Esto incluye información personal, historial de reservas, preferencias de viaje y detalles de contacto. Con esta funcionalidad, se puede brindar un servicio personalizado, promociones especiales y recomendaciones basadas en las preferencias individuales de cada cliente.

## Estructura del proyecto

El proyecto sigue una estructura organizada en varios componentes:

- **Controladores (controllers)**: Manejan las solicitudes HTTP y definen los puntos de entrada de la API.
- **DTOs (Data Transfer Objects)**: Utilizados para intercambiar información entre la capa de presentación y la capa de servicios.
- **Entidades (entities)**: Representan los modelos de datos en la base de datos.
- **Excepciones (exceptions)**: Personalizadas para manejar casos de error específicos del negocio.
- **Repositorios (repositories)**: Interfaces que definen las operaciones de persistencia y consultas en la base de datos.
- **Servicios (services)**: Implementan la lógica de negocio y actúan como intermediarios entre los controladores y los repositorios.
- **Pruebas (tests)**: Incluye pruebas unitarias y de integración, con y sin mocks, para garantizar el correcto funcionamiento del sistema.

## Configuración

El proyecto requiere Java, Spring y JPA. Asegúrate de tener las versiones adecuadas instaladas en tu entorno de desarrollo.

Para ejecutar las pruebas, se recomienda utilizar herramientas como JUnit y Mockito para las pruebas con mocks. También se puede configurar una base de datos de prueba para las pruebas de integración sin mocks.

### Configuración de pruebas

El proyecto incluye un archivo ***data.sql*** predefinido que permite precargar datos de prueba en la base de datos. Para utilizarlo en las pruebas, asegúrate de configurar correctamente tu entorno:

  1. Verifica que tienes una base de datos de prueba configurada y accesible.
  1. Asegúrate de que las configuraciones de conexión a la base de datos en el archivo de configuración de pruebas (***application-test.properties*** o similar) sean correctas.
  1. Antes de ejecutar las pruebas, la base de datos de prueba debe estar vacía o en un estado inicial limpio.
  1. Durante la ejecución de las pruebas, el sistema cargará automáticamente los datos predefinidos del archivo ***data.sql*** en la base de datos de prueba.
  1. Utiliza los datos precargados en las pruebas para verificar el funcionamiento del sistema en diferentes escenarios y casos de uso.

Recuerda que las pruebas deben garantizar la integridad y la calidad del código, y verificar que el sistema funcione correctamente, tanto en condiciones normales, como en situaciones excepcionales.

Si encuentras algún problema durante las pruebas o tienes alguna pregunta relacionada con la configuración de pruebas, no dudes en contactar a nuestro equipo de desarrollo para recibir asistencia.

### Ejecutar pruebas

Para ejecutar las pruebas en el proyecto, sigue los pasos a continuación:

  1. Asegúrate de que tu entorno de desarrollo esté correctamente configurado y todas las dependencias estén instaladas.
  1. Abre tu entorno de desarrollo y accede al proyecto **FlySky**.
  1. Ejecuta las pruebas utilizando herramientas como JUnit o el comando de ejecución de pruebas proporcionado por tu entorno de desarrollo.
  1. Verifica los resultados de las pruebas y asegúrate de que todas las pruebas pasen correctamente.
  1. Si alguna prueba falla, investiga y corrige los problemas identificados antes de realizar cualquier cambio en el código base.

Recuerda que las pruebas son una parte fundamental del proceso de desarrollo ya que garantizan que el sistema funcione correctamente y cumpla con los requisitos establecidos.

## Contribuciones

¡Tus contribuciones son bienvenidas! Si deseas colaborar en el desarrollo de FlySky, sigue estos pasos:

  1. Realiza un fork de este repositorio.
  1. Crea una rama para tu nueva funcionalidad (`git checkout -b nueva-funcionalidad`).
  1. Realiza los cambios necesarios y haz commit de tus modificaciones (`git commit -am 'Agrega nueva funcionalidad'`).
  1. Sube los cambios a tu repositorio remoto (`git push origin nueva-funcionalidad`).
  1. Realiza una solicitud de extracción (pull request) desde tu repositorio a este repositorio principal.

Agradecemos cualquier aporte que pueda mejorar FlySky y hacerlo aún más útil y confiable.

## Autores

  - **Cecilia Granda** - [CeciGG](https://github.com/CeciGG)
  - **Didier Watson** - [kernelpanic93](https://github.com/kernelpanic93)
  - **Emanuel E. Gauler** - [emanuelgauler](https://github.com/emanuelgauler)
  - **Federico Bencini** - [FedeBencini](https://github.com/FedeBencini)
  - **Jim Gavidia** - [JIMGA8](https://github.com/JIMGA8)
  - **Alberto Maximiliano Correa Pietrobon** - [MaxCPietro](https://github.com/MaxCPietro)
  - **Miriam Monzon** - [Miriam-777](https://github.com/Miriam-777)
