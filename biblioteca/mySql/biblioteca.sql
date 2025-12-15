DROP DATABASE IF EXISTS Biblioteca;
CREATE DATABASE Biblioteca;
USE Biblioteca;

CREATE TABLE Autor (
   id INT PRIMARY KEY AUTO_INCREMENT,
   nombre VARCHAR(100) NOT NULL,
   anno_nacimiento INT,
   nacionalidad VARCHAR(50)
);

CREATE TABLE Libro (
   id INT PRIMARY KEY AUTO_INCREMENT,
   titulo VARCHAR(200) NOT NULL
);

CREATE TABLE Usuario (
   dni CHAR(9) PRIMARY KEY,
   nombre VARCHAR(100) NOT NULL,
   dir_correo VARCHAR(100) NOT NULL,
   pswd VARCHAR(100) NOT NULL
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
   estado ENUM('Nuevo', 'Bueno', 'Aceptable'),
   isbn_edicion VARCHAR(20),
   FOREIGN KEY (isbn_edicion) REFERENCES Edicion(isbn)
);

CREATE TABLE Prestamo (
   cod_prestamo INT PRIMARY KEY,
   fecha DATE,
   fecha_devolucion DATE,
   activo BOOLEAN DEFAULT TRUE,
   id_usuario CHAR(9),
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
   dni_usuario CHAR(9),
   isbn_edicion VARCHAR(20),
   valoracion INT NOT NULL CHECK (valoracion BETWEEN 1 AND 5),
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
(6, 7);

INSERT INTO Genero (nombre_genero, id_libro) VALUES
('Realismo Magico', 1),
('Ficcion Historica', 2),
('Fantasia', 3),
('Ficcion Contemporanea', 4),
('Ficcion Postcolonial', 5),
('Distopia', 6),
('Distopia', 7);

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

DELIMITER //
CREATE TRIGGER trg_asigna_no_overlap
BEFORE INSERT ON asigna
FOR EACH ROW
BEGIN
   IF EXISTS (
       SELECT 1
       FROM asigna a
       JOIN Prestamo p ON a.cod_prestamo = p.cod_prestamo
       WHERE a.id_ejemplar = NEW.id_ejemplar
         AND p.activo = TRUE
   ) THEN
       SIGNAL SQLSTATE '45000'
           SET MESSAGE_TEXT = 'El ejemplar ya está prestado y activo';
   END IF;
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER trg_asigna_max_10
BEFORE INSERT ON asigna
FOR EACH ROW
BEGIN
   IF (
      SELECT COUNT(*) FROM asigna WHERE cod_prestamo = NEW.cod_prestamo
   ) >= 10 THEN
      SIGNAL SQLSTATE '45000'
         SET MESSAGE_TEXT = 'Un préstamo no puede tener más de 10 ejemplares';
   END IF;
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER trg_asigna_unica_edicion
BEFORE INSERT ON asigna
FOR EACH ROW
BEGIN
   IF EXISTS (
      SELECT 1
      FROM asigna a
      JOIN Ejemplar e ON a.id_ejemplar = e.id
      WHERE a.cod_prestamo = NEW.cod_prestamo
        AND e.isbn_edicion = (SELECT isbn_edicion FROM Ejemplar WHERE id = NEW.id_ejemplar)
   ) THEN
      SIGNAL SQLSTATE '45000'
         SET MESSAGE_TEXT = 'Un préstamo no puede tener dos ejemplares de la misma edición';
   END IF;
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER trg_prestamo_set_fechas
BEFORE INSERT ON Prestamo
FOR EACH ROW
BEGIN
   IF NEW.fecha IS NULL THEN
      SET NEW.fecha = CURRENT_DATE();
   END IF;
   SET NEW.fecha_devolucion = DATE_ADD(NEW.fecha, INTERVAL 1 MONTH);
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE adelantar_devolucion(IN p_cod_prestamo INT)
BEGIN
   UPDATE Prestamo
   SET fecha_devolucion = GREATEST(fecha, DATE_SUB(fecha_devolucion, INTERVAL 7 DAY))
   WHERE cod_prestamo = p_cod_prestamo;
END //
DELIMITER ;

