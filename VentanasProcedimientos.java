package ed_proyectofinal;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class VentanasProcedimientos extends JFrame {

    public VentanasProcedimientos(ResultadoAlgoritmo resultado, ArrayList<String> pasos, int[] arregloOriginal) {
        setTitle("Procedimiento: " + resultado.nombre);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel titulo = new JLabel("Método de clasificación: " + resultado.nombre);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        StringBuilder sb = new StringBuilder();

        if (resultado.nombre.equals("Bubble")) {
            sb.append("Descripción: ").append(resultado.descripcion).append("\n\n");
            sb.append("Arreglo original: ").append(Arrays.toString(arregloOriginal)).append("\n\n");
            sb.append("Ordenado: ").append(resultado.ordenado).append("\n\n");
            sb.append(String.format("Tiempo Array: %.3f ms\n", resultado.tiempoArray));
            sb.append(String.format("Tiempo List : %.3f ms\n\n", resultado.tiempoList));
            sb.append("Procedimiento paso por paso:\n\n");
            for (String paso : pasos) sb.append(paso).append("\n");
            sb.append("Total de pasos: ").append(pasos.size());
        }

        else if (resultado.nombre.equals("Insertion")) {
            sb.append("Descripción: ").append(resultado.descripcion).append("\n\n");
            sb.append("Arreglo original: ").append(Arrays.toString(arregloOriginal)).append("\n\n");
            sb.append("Ordenado: ").append(resultado.ordenado).append("\n\n");
            sb.append(String.format("Tiempo Array: %.3f ms\n", resultado.tiempoArray));
            sb.append(String.format("Tiempo List : %.3f ms\n\n", resultado.tiempoList));
            sb.append("Procedimiento paso por paso:\n\n");
            for (String paso : pasos) sb.append(paso).append("\n");
            sb.append("Total de pasos: ").append(pasos.size());
        }

        else if (resultado.nombre.equals("Selection")) {
            sb.append("Descripción: ").append(resultado.descripcion).append("\n\n");
            sb.append("Arreglo original: ").append(Arrays.toString(arregloOriginal)).append("\n\n");
            sb.append("Ordenado: ").append(resultado.ordenado).append("\n\n");
            sb.append(String.format("Tiempo Array: %.3f ms\n", resultado.tiempoArray));
            sb.append(String.format("Tiempo List : %.3f ms\n\n", resultado.tiempoList));
            sb.append("Procedimiento paso por paso:\n\n");
            for (String paso : pasos) sb.append(paso).append("\n");
            sb.append("Total de pasos: ").append(pasos.size());
        }

        else if (resultado.nombre.equals("Shell")) {
            sb.append("Descripción: ").append(resultado.descripcion).append("\n\n");
            sb.append("Arreglo original: ").append(Arrays.toString(arregloOriginal)).append("\n\n");
            sb.append("Ordenado: ").append(resultado.ordenado).append("\n\n");
            sb.append(String.format("Tiempo Array: %.3f ms\n", resultado.tiempoArray));
            sb.append(String.format("Tiempo List : %.3f ms\n\n", resultado.tiempoList));
            sb.append("NOTA: iN se refiere a la posición, por ejemplo i5 = posición 5 del array/lista\n\n");
            sb.append("Procedimiento paso por paso:\n\n");
            for (String paso : pasos) sb.append(paso).append("\n");
            sb.append("Total de pasos: ").append(pasos.size());
        }
        /*-----Merge----*/
        else if (resultado.nombre.equals("Merge")) {
            sb.append("Descripción: ").append(resultado.descripcion).append("\n\n");
            sb.append("Arreglo original: ").append(Arrays.toString(arregloOriginal)).append("\n\n");
            sb.append("Ordenado: ").append(resultado.ordenado).append("\n\n");
            sb.append(String.format("Tiempo Array: %.3f ms\n", resultado.tiempoArray));
            sb.append(String.format("Tiempo List : %.3f ms\n\n", resultado.tiempoList));
            sb.append("Procedimiento paso por paso:\n\n");

            for (String pasoTexto : pasos) {
                sb.append(pasoTexto).append("\n");
            } 
        }

        /*-----Quick----*/
        else if (resultado.nombre.equals("Quick")) {
            sb.append("Descripción: ").append(resultado.descripcion).append("\n\n");
            sb.append("Arreglo original: ").append(Arrays.toString(arregloOriginal)).append("\n\n");
            sb.append("Ordenado: ").append(resultado.ordenado).append("\n\n");
            sb.append(String.format("Tiempo Array: %.3f ms\n", resultado.tiempoArray));
            sb.append(String.format("Tiempo List : %.3f ms\n\n", resultado.tiempoList));
            sb.append("Procedimiento paso por paso:\n\n");

            for (String pasoTexto : pasos) {
                sb.append(pasoTexto).append("\n");
            }

           
        }

        area.setText(sb.toString());
        panelPrincipal.add(new JScrollPane(area), BorderLayout.CENTER);

        // Botón para volver
        JButton btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));
        btnVolver.addActionListener(e -> this.dispose());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.add(btnVolver);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        add(panelPrincipal);
    }
}