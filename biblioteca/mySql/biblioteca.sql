DROP DATABASE IF EXISTS Biblioteca;
CREATE DATABASE Biblioteca;
USE Biblioteca;

CREATE TABLE Autor (
   id INT PRIMARY KEY AUTO_INCREMENT,
   nombre VARCHAR(100),
   anno_nacimiento INT,
   nacionalidad VARCHAR(50)
);

CREATE TABLE Libro (
   id INT PRIMARY KEY AUTO_INCREMENT,
   titulo VARCHAR(200)
);

CREATE TABLE Usuario (
   dni INT PRIMARY KEY,
   nombre VARCHAR(100),
   dir_correo VARCHAR(100),
   pswd VARCHAR(100)
);

CREATE TABLE Escribe (
   id_autor INT,
   id_libro INT,
   PRIMARY KEY (id_autor, id_libro),
   FOREIGN KEY (id_autor) REFERENCES Autor(id),
   FOREIGN KEY (id_libro) REFERENCES Libro(id)
);

CREATE TABLE Genero (
   nombre_genero VARCHAR(50),
   id_libro INT,
   PRIMARY KEY (nombre_genero, id_libro),
   FOREIGN KEY (id_libro) REFERENCES Libro(id)
);

CREATE TABLE Edicion (
   isbn VARCHAR(20) PRIMARY KEY,
   anno_publicacion INT,
   editorial VARCHAR(100),
   idioma VARCHAR(50),
   id_libro INT,
   FOREIGN KEY (id_libro) REFERENCES Libro(id)
);

CREATE TABLE Ejemplar (
   id INT PRIMARY KEY AUTO_INCREMENT,
   estado VARCHAR(50),
   isbn_edicion VARCHAR(20),
   FOREIGN KEY (isbn_edicion) REFERENCES Edicion(isbn)
);

CREATE TABLE Prestamo (
   cod_prestamo INT PRIMARY KEY,
   fecha DATE,
   fecha_devolucion DATE,
   id_usuario INT,
   FOREIGN KEY (id_usuario) REFERENCES Usuario(dni)
);

CREATE TABLE asigna (
   id_ejemplar INT,
   cod_prestamo INT,
   PRIMARY KEY (id_ejemplar, cod_prestamo),
   FOREIGN KEY (id_ejemplar) REFERENCES Ejemplar(id),
   FOREIGN KEY (cod_prestamo) REFERENCES Prestamo(cod_prestamo)
);

CREATE TABLE Review (
   dni_usuario INT,
   isbn_edicion VARCHAR(20),
   valoracion INT,
   descripcion TEXT,
   PRIMARY KEY (dni_usuario, isbn_edicion),
   FOREIGN KEY (dni_usuario) REFERENCES Usuario(dni),
   FOREIGN KEY (isbn_edicion) REFERENCES Edicion(isbn)
);

INSERT INTO Autor (nombre, anno_nacimiento, nacionalidad) VALUES
('Gabriel Garcia Marquez', 1927, 'Colombiana'),
('Isabel Allende', 1942, 'Chilena'),
('J.K. Rowling', 1965, 'Britanica'),
('Haruki Murakami', 1949, 'Japonesa'),
('Chinua Achebe', 1930, 'Nigeriana'),
('Margaret Atwood', 1939, 'Canadiense');

INSERT INTO Libro (titulo) VALUES
('Cien Años de Soledad'),
('La Casa de los Espíritus'),
('Harry Potter y la Piedra Filosofal'),
('Kafka en la Orilla'),
('Todo se Desmorona'),
('El Cuento de la Criada'),
('1984');

INSERT INTO Edicion (isbn, anno_publicacion, editorial, idioma, id_libro) VALUES
('978-3-16-148410-0', 1967, 'Editorial Sudamericana', 'Español', 1),
('978-0-06-112241-5', 1993, 'HarperCollins', 'Inglés', 2),
('978-0-7475-3274-6', 1997, 'Bloomsbury', 'Inglés', 3),
('978-0-307-59394-4', 2005, 'Knopf', 'Inglés', 4),
('978-0-14-303995-2', 1958, 'Penguin Books', 'Inglés', 5),
('978-0-385-49081-0', 1985, 'Doubleday', 'Inglés', 6),
('978-0-452-28423-4', 1949, 'Harcourt, Brace & Company', 'Inglés', 7),
('978-84-376-0494-7', 2000, 'Plaza & Janés', 'Español', 2),
('978-84-204-2475-2', 2001, 'Salamandra', 'Español', 4);

INSERT INTO Usuario (dni, nombre, dir_correo, pswd) VALUES
('12345678P', 'Ana Perez', 'ana.perez@example.com', 'password123'),
('87654321L', 'Luis Gomez', 'luis.gomez@example.com', 'password456');

INSERT INTO Escribe (id_autor, id_libro) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(6, 7),
(2, 8),
(4, 9);

INSERT INTO Genero (nombre_genero, id_libro) VALUES
('Realismo Magico', 1),
('Ficcion Historica', 2),
('Fantasia', 3),
('Ficcion Contemporanea', 4),
('Ficcion Postcolonial', 5),
('Distopia', 6),
('Distopia', 7),
('Ficcion Historica', 8),
('Ficcion Contemporanea', 9);

INSERT INTO Ejemplar (estado, isbn_edicion) VALUES
('Nuevo', '978-3-16-148410-0'),
('Bueno', '978-3-16-148410-0'),
('Aceptable', '978-0-06-112241-5'),
('Nuevo', '978-0-06-112241-5'),
('Bueno', '978-0-7475-3274-6'),
('Nuevo', '978-0-307-59394-4'),
('Aceptable', '978-0-14-303995-2');

INSERT INTO Prestamo (cod_prestamo, fecha, fecha_devolucion, id_usuario) VALUES
(1, '2024-01-15', '2024-02-15', '12345678P'),
(2, '2024-01-20', '2024-02-20', '87654321L');

INSERT INTO asigna (id_ejemplar, cod_prestamo) VALUES
(1, 1),
(2, 1),
(4, 2),
(3, 2);

INSERT INTO Review (dni_usuario, isbn_edicion, valoracion, descripcion) VALUES
('12345678P', '978-3-16-148410-0', 5, 'Una obra maestra del realismo magico.'),
('87654321L', '978-0-06-112241-5', 4, 'Una lectura fascinante sobre la historia familiar.');

-- =============================================
-- RESTRICCION: Un usuario no puede tener > 10 ejemplares activos
-- Se considera "activo" un préstamo cuyo campo fecha_devolucion es NULL
-- =============================================
DELIMITER $$
CREATE TRIGGER before_prestamo_insert
BEFORE INSERT ON Prestamo
FOR EACH ROW
BEGIN
      DECLARE current_count INT;
      -- Cuenta ejemplares actualmente asignados en préstamos activos del usuario
      SELECT COUNT(*) INTO current_count
      FROM asigna a
      JOIN Prestamo p ON a.cod_prestamo = p.cod_prestamo
      WHERE p.id_usuario = NEW.id_usuario
         AND p.fecha_devolucion IS NULL;

      IF current_count >= 10 THEN
            SIGNAL SQLSTATE '45000'
                  SET MESSAGE_TEXT = 'El usuario ya tiene 10 ejemplares activos; no se puede crear otro préstamo.';
      END IF;
END $$

CREATE TRIGGER before_asigna_insert
BEFORE INSERT ON asigna
FOR EACH ROW
BEGIN
      DECLARE current_count INT;
      DECLARE usuario_actual INT;

      -- Obtiene el usuario dueño del préstamo al que se asigna el ejemplar
      SELECT id_usuario INTO usuario_actual FROM Prestamo WHERE cod_prestamo = NEW.cod_prestamo;

      -- Cuenta ejemplares actualmente asignados en préstamos activos del usuario
      SELECT COUNT(*) INTO current_count
      FROM asigna a
      JOIN Prestamo p ON a.cod_prestamo = p.cod_prestamo
      WHERE p.id_usuario = usuario_actual
         AND p.fecha_devolucion IS NULL;

      IF current_count >= 10 THEN
            SIGNAL SQLSTATE '45000'
                  SET MESSAGE_TEXT = 'El usuario excedería el máximo de 10 ejemplares activos.';
      END IF;
END $$
DELIMITER ;

-- Índice recomendado para acelerar la comprobación
CREATE INDEX idx_prestamo_usuario_dev ON Prestamo(id_usuario, fecha_devolucion);

-- =============================================
-- STORED PROCEDURES: Transacciones para préstamos y devoluciones
-- =============================================

DELIMITER $$

-- Procedimiento para realizar un préstamo (transacción)
-- Crea el préstamo, asigna ejemplares y marca los ejemplares como 'Prestado'
CREATE PROCEDURE realizar_prestamo(
    IN p_cod_prestamo INT,
    IN p_dni_usuario INT,
    IN p_fecha DATE,
    IN p_ejemplares_ids VARCHAR(500) -- IDs separados por comas, ej: "1,2,3"
)
BEGIN
    DECLARE current_count INT;
    DECLARE ejemplar_id INT;
    DECLARE ejemplar_estado VARCHAR(50);
    DECLARE i INT DEFAULT 1;
    DECLARE total_ejemplares INT;
    DECLARE ejemplar_actual VARCHAR(10);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Error en la transacción de préstamo. Operación cancelada.';
    END;

    START TRANSACTION;

    -- Verificar que el usuario existe
    IF NOT EXISTS (SELECT 1 FROM Usuario WHERE dni = p_dni_usuario) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El usuario especificado no existe.';
    END IF;

    -- Contar ejemplares actualmente prestados al usuario
    SELECT COUNT(*) INTO current_count
    FROM asigna a
    JOIN Prestamo p ON a.cod_prestamo = p.cod_prestamo
    WHERE p.id_usuario = p_dni_usuario
      AND p.fecha_devolucion IS NULL;

    -- Contar ejemplares a prestar
    SET total_ejemplares = (LENGTH(p_ejemplares_ids) - LENGTH(REPLACE(p_ejemplares_ids, ',', '')) + 1);

    -- Verificar límite de 10 ejemplares
    IF (current_count + total_ejemplares) > 10 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El préstamo excedería el límite de 10 ejemplares activos.';
    END IF;

    -- Crear el registro de préstamo (fecha_devolucion NULL = activo)
    INSERT INTO Prestamo (cod_prestamo, fecha, fecha_devolucion, id_usuario)
    VALUES (p_cod_prestamo, p_fecha, NULL, p_dni_usuario);

    -- Procesar cada ejemplar
    WHILE i <= total_ejemplares DO
        SET ejemplar_actual = TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(p_ejemplares_ids, ',', i), ',', -1));
        SET ejemplar_id = CAST(ejemplar_actual AS UNSIGNED);

        -- Verificar que el ejemplar existe y está disponible
        SELECT estado INTO ejemplar_estado FROM Ejemplar WHERE id = ejemplar_id;
        
        IF ejemplar_estado IS NULL THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = CONCAT('El ejemplar ID ', ejemplar_id, ' no existe.');
        END IF;

        IF ejemplar_estado = 'Prestado' THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = CONCAT('El ejemplar ID ', ejemplar_id, ' ya está prestado.');
        END IF;

        -- Asignar ejemplar al préstamo
        INSERT INTO asigna (id_ejemplar, cod_prestamo) VALUES (ejemplar_id, p_cod_prestamo);

        -- Actualizar estado del ejemplar a 'Prestado'
        UPDATE Ejemplar SET estado = 'Prestado' WHERE id = ejemplar_id;

        SET i = i + 1;
    END WHILE;

    COMMIT;
END $$

-- Procedimiento para completar una devolución (transacción)
-- Actualiza fecha_devolucion y marca ejemplares como 'Disponible'
CREATE PROCEDURE completar_devolucion(
    IN p_cod_prestamo INT,
    IN p_fecha_devolucion DATE,
    IN p_nuevo_estado VARCHAR(50) -- Estado al que pasan los ejemplares: 'Disponible', 'Bueno', etc.
)
BEGIN
    DECLARE ejemplar_cursor CURSOR FOR
        SELECT id_ejemplar FROM asigna WHERE cod_prestamo = p_cod_prestamo;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET @finished = 1;
    DECLARE ejemplar_id INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Error en la transacción de devolución. Operación cancelada.';
    END;

    START TRANSACTION;

    -- Verificar que el préstamo existe
    IF NOT EXISTS (SELECT 1 FROM Prestamo WHERE cod_prestamo = p_cod_prestamo) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El préstamo especificado no existe.';
    END IF;

    -- Verificar que el préstamo está activo (no devuelto)
    IF EXISTS (SELECT 1 FROM Prestamo WHERE cod_prestamo = p_cod_prestamo AND fecha_devolucion IS NOT NULL) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El préstamo ya fue devuelto anteriormente.';
    END IF;

    -- Actualizar fecha de devolución
    UPDATE Prestamo SET fecha_devolucion = p_fecha_devolucion WHERE cod_prestamo = p_cod_prestamo;

    -- Actualizar estado de todos los ejemplares asociados
    SET @finished = 0;
    OPEN ejemplar_cursor;

    read_loop: LOOP
        FETCH ejemplar_cursor INTO ejemplar_id;
        IF @finished = 1 THEN
            LEAVE read_loop;
        END IF;

        UPDATE Ejemplar SET estado = p_nuevo_estado WHERE id = ejemplar_id;
    END LOOP;

    CLOSE ejemplar_cursor;

    COMMIT;
END $$

DELIMITER ;