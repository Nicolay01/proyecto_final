# proyecto_final
proyecto final de segundo semestre
Usuario
CREATE TABLE usuario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255),
    usuario VARCHAR(255),
    contrasena VARCHAR(255)
);

informacionventa
CREATE TABLE informacionventa (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fecha DATE,
    bollo_angelito_traido INT,
    bollo_angelito_quedado INT,
    bollo_angelito_total INT,
    bollo_limpias_traido INT,
    bollo_limpias_quedado INT,
    bollo_limpias_total INT,
    bollo_mazorca_traido INT,
    bollo_mazorca_quedado INT,
    bollo_mazorca_total INT,
    bollo_yuca_traido INT,
    bollo_yuca_quedado INT,
    bollo_yuca_total INT,
    bollo_yuca_dulce_traido INT,
    bollo_yuca_dulce_quedado INT,
    bollo_yuca_dulce_total INT,
    usuario_id INT,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

gastos
CREATE TABLE gastos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fecha DATE,
    servilletas INT,
    bolsas INT,
    pasajes INT,
    usuario_idGasto INT,
    FOREIGN KEY (usuario_idGasto) REFERENCES usuario(id)
);
