package ed_proyectofinal;

import java.util.ArrayList;

public class ResultadoAlgoritmo {
    public String nombre;
    public String descripcion;
    public ArrayList<Integer> ordenado;
    public double tiempoArray;
    public double tiempoList;

    // Campos opcionales para paso a paso
    public ArrayList<int[]> pasos;
    public int totalPasos;

    public ResultadoAlgoritmo(String nombre, String descripcion, ArrayList<Integer> ordenado,
                              double tiempoArray, double tiempoList) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ordenado = ordenado;
        this.tiempoArray = tiempoArray;
        this.tiempoList = tiempoList;
        this.pasos = new ArrayList<>();
        this.totalPasos = 0;
    }
}