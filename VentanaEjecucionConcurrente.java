package ed_proyectofinal;

import javax.swing.*;
import java.awt.*;

public class VentanaEjecucionConcurrente extends JFrame {

    private JLabel lblContador;
    private JLabel lblSegundos; // ahora es atributo para poder modificarlo
    private final int segundos;
    private final char caso;
    private StatsCollector stats; // apunta a la clase interna de Main
    private boolean reporteMostrado = false; // flag para evitar abrir reporte más de una vez

    public VentanaEjecucionConcurrente(char caso, int segundos) {
        this.segundos = segundos;
        this.caso = caso;

        setTitle("Ejecución concurrente");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior (negro)
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(Color.BLACK);
        panelSuperior.setPreferredSize(new Dimension(600, 100));
        JLabel lblTitulo = new JLabel("Iniciando ejecución concurrente con 12 hilos...");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelSuperior.setLayout(new BorderLayout());
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        // Panel inferior (gris)
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(Color.LIGHT_GRAY);
        panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));

        lblContador = new JLabel(String.valueOf(segundos));
        lblContador.setFont(new Font("Arial", Font.BOLD, 80));
        lblContador.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblContador.setForeground(Color.WHITE);
        lblContador.setOpaque(true);
        lblContador.setBackground(Color.DARK_GRAY);

        lblSegundos = new JLabel("Segundos"); // inicializado aquí
        lblSegundos.setFont(new Font("Arial", Font.PLAIN, 24));
        lblSegundos.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelInferior.add(lblContador);
        panelInferior.add(Box.createVerticalStrut(10));
        panelInferior.add(lblSegundos);

        add(panelInferior, BorderLayout.CENTER);

        // Iniciar ejecución y cuenta regresiva
        ejecutarPruebaConcurrente();
        iniciarCuentaRegresiva();
    }

    private void ejecutarPruebaConcurrente() {
        new Thread(() -> {
            try {
                stats = Main.runComparison(caso, segundos, new Main.AlgorithmCollection());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void iniciarCuentaRegresiva() {
        Timer timer = new Timer(1000, e -> {
            int actual = Integer.parseInt(lblContador.getText());
            actual--;

            if (actual <= 0) {
                ((Timer) e.getSource()).stop();

                // Mensaje final claro
                lblContador.setText("⏱ Tiempo terminado! Generando reporte...");
                lblContador.setFont(new Font("Arial", Font.BOLD, 20));
                lblContador.setHorizontalAlignment(SwingConstants.CENTER);

                // Cambiar texto inferior
                lblSegundos.setText("Por favor espere.");

                // Esperar 1 segundo antes de mostrar VentanaFinal
                new Timer(1000, e2 -> {
                    if (stats != null && !reporteMostrado) {
                        reporteMostrado = true; // marcar que ya se abrió
                        //  Ahora pasamos también el caso
                        new VentanasResultados.VentanaFinal(caso, segundos, stats).setVisible(true);
                        SwingUtilities.getWindowAncestor(lblContador).dispose();
                    }
                }).start();

            } else {
                lblContador.setText(String.valueOf(actual));
                lblSegundos.setText("Segundos"); // mantener mientras corre
            }
        });
        timer.start();
    }
}
