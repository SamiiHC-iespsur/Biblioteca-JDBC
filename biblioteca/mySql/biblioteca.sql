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
   dni CHAR(9) PRIMARY KEY,
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