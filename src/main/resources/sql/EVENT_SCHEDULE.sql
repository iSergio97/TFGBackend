-- SHOW PROCESSLIST;
-- SHOW EVENTS;
-- SET GLOBAL event_scheduler = ON;
-- SET GLOBAL event_scheduler = OFF;

CREATE EVENT IF NOT EXISTS ACTUALIZA_SOLICITUDES
ON SCHEDULE EVERY '1' DAY
DO
UPDATE SOLICITUD SET ESTADO = 'C', JUSTIFICACION = 'Solicitud cancelada de forma automática tras permaner 7 días o más sin actualizarse' WHERE ESTADO = 'P' AND FECHA > NOW() - INTERVAL 7 DAY;