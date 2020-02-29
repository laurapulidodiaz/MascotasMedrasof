package com.medrasoft.pruebamascotas.data;

public class MascotaDB extends DataBaseDefinition {
    public static final String TABLE = "mascota";
    public static final String CEDULA = "cedula";
    public static final String NOMBRE = "nombre";
    public static final String EDAD = "edad";
    public static final String RAZA = "raza";
    public static final String TIPO = "tipo";
    public static final String PROPIETARIO_ID = "propietario_id";

    public static String createStatement(){
        return "CREATE TABLE `mascota`\n" +
                "(\n" +
                "  `id` int PRIMARY KEY,\n" +
                "  `cedula` int, \n" +
                "  `nombre` varchar, \n" +
                "  `edad` int, \n" +
                "  `raza` varchar, \n" +
                "  `tipo` varchar, \n" +
                "  `propietario_id` int,\n" +
                "FOREIGN KEY (propietario_id) REFERENCES propietario (id) \n" +
                "  ON UPDATE SET NULL\n" +
                ")";
    }
}
