package com.medrasoft.pruebamascotas.data;

public class VacunaDB extends DataBaseDefinition {
    public static final String TABLE = "vacuna";
    public static final String NOMBRE = "nombre";
    public static final String FECHA = "fecha";
    public static final String DOSIS = "dosis";
    public static final String MASCOTA_ID = "mascota_id";

    public static String createStatement(){
        return "CREATE TABLE `vacuna`\n" +
                "(\n" +
                "  `id` int PRIMARY KEY,\n" +
                "  `nombre` varchar, \n" +
                "  `fecha` varchar, \n" +
                "  `dosis` int, \n" +
                "  `mascota_id` int,\n" +
                "FOREIGN KEY (mascota_id) REFERENCES mascota (id) \n" +
                "  ON UPDATE SET NULL\n" +
                ")";
    }
}
