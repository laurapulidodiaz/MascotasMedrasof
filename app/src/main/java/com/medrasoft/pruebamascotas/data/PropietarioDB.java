package com.medrasoft.pruebamascotas.data;

public class PropietarioDB extends DataBaseDefinition {
    public static final String TABLE = "propietario";
    public static final String CEDULA = "cedula";
    public static final String NOMBRE = "nombre";
    public static final String TELEFONO = "telefono";

    public static String createStatement(){
        return "CREATE TABLE `propietario`\n" +
                "(\n" +
                "  `id` int PRIMARY KEY,\n" +
                "  `cedula` int,\n" +
                "  `nombre` varchar(255),\n" +
                "  `telefono` int \n" +
                ")";
    }
}
