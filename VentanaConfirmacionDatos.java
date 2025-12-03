package ed_proyectofinal;

import javax.swing.*;
import java.awt.*;

public class VentanaConfirmacionDatos extends JFrame {
    
    public VentanaConfirmacionDatos(char caso, int segundos) {
        setTitle("Confirmación de configuración");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String descripcionCaso = switch (caso) {
            case 'A' -> "100 elementos aleatorios";
            case 'B' -> "50,000 elementos aleatorios";
            case 'C' -> "100,000 elementos aleatorios";
            case 'D' -> "100,000 elementos aleatorios solo números del 1 al 5";
            default -> "Configuración desconocida";
        };

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Ícono de confirmación
        Icon iconoPregunta = UIManager.getIcon("OptionPane.questionIcon");
        JLabel iconoLabel = new JLabel(iconoPregunta);
        iconoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(iconoLabel);

        panel.add(Box.createVerticalStrut(20));

        JLabel titulo = new JLabel("¿Deseas ejecutar la prueba con:");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);

        panel.add(Box.createVerticalStrut(20));

        JLabel detalle = new JLabel(descripcionCaso + " por " + segundos + " segundos");
        detalle.setFont(new Font("Arial", Font.PLAIN, 16));
        detalle.setForeground(new Color(0, 128, 0)); // Verde oscuro
        detalle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(detalle);

        panel.add(Box.createVerticalStrut(30));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnSi = new JButton("Sí, empieza la prueba");
btnSi.setFont(new Font("Arial", Font.BOLD, 14));
btnSi.addActionListener(e -> {
    new VentanaEjecucionConcurrente(caso, segundos).setVisible(true);
    this.dispose();
});

// Esta línea hace que Enter ejecute el botón
getRootPane().setDefaultButton(btnSi);

   

           

        JButton btnNo = new JButton("No, quiero cambiar algo");
        btnNo.setFont(new Font("Arial", Font.PLAIN, 14));
        btnNo.addActionListener(e -> {
            new VentanaConfiguracionEjecucion().setVisible(true);
            this.dispose();
        });

        botones.add(btnSi);
        botones.add(btnNo);
        panel.add(botones);

        add(panel);
    }
}