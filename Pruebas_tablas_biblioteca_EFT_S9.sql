/* relacion entre libros y categorias */
SELECT
    l.id,
    l.titulo,
    l.autor,
    c.nombre AS categoria,
    l.stock
FROM libros l
INNER JOIN categorias c
    ON l.id_categoria = c.id;
    
/* cada prestamo este correctamente relacionado libro - estudiante */    
SELECT
    p.id,
    e.nombre AS estudiante,
    l.titulo AS libro,
    p.fecha_prestamo,
    p.fecha_devolucion,
    p.devuelto
FROM prestamos p
INNER JOIN estudiantes e
    ON p.id_estudiante = e.id
INNER JOIN libros l
    ON p.id_libro = l.id;
    
    
SELECT *
FROM estudiantes;    

USE biblioteca;

SELECT
    p.id,
    e.nombre AS estudiante,
    l.titulo AS libro,
    p.fecha_prestamo,
    p.fecha_devolucion,
    p.devuelto
FROM prestamos p
INNER JOIN estudiantes e
    ON p.id_estudiante = e.id
INNER JOIN libros l
    ON p.id_libro = l.id
ORDER BY p.id DESC;

SELECT
    id,
    titulo,
    stock
FROM libros
ORDER BY id;