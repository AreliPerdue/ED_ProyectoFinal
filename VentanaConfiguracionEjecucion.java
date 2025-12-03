package ed_proyectofinal;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaConfiguracionEjecucion extends JFrame {
    
    public VentanaConfiguracionEjecucion() {
        // Título de la ventana (barra superior)
        setTitle("Prueba de comparación de eficiencia");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // Encabezado interno
        JLabel tituloContenido = new JLabel("Configuración de ejecución", SwingConstants.CENTER);
        tituloContenido.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(tituloContenido, gbc);

        gbc.gridy++;
        JLabel lblCantidad = new JLabel("Cantidad de elementos aleatorios:");
        lblCantidad.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(lblCantidad, gbc);

        ButtonGroup grupoOpciones = new ButtonGroup();
        JRadioButton opcionA = new JRadioButton("A) 100 elementos");
        JRadioButton opcionB = new JRadioButton("B) 50,000 elementos");
        JRadioButton opcionC = new JRadioButton("C) 100,000 elementos");
        JRadioButton opcionD = new JRadioButton("D) 100,000 elementos (solo números del 1 al 5)");

        opcionA.setFont(new Font("Arial", Font.PLAIN, 14));
        opcionB.setFont(new Font("Arial", Font.PLAIN, 14));
        opcionC.setFont(new Font("Arial", Font.PLAIN, 14));
        opcionD.setFont(new Font("Arial", Font.PLAIN, 14));

        grupoOpciones.add(opcionA);
        grupoOpciones.add(opcionB);
        grupoOpciones.add(opcionC);
        grupoOpciones.add(opcionD);

        gbc.gridy++;
        panel.add(opcionA, gbc);
        gbc.gridy++;
        panel.add(opcionB, gbc);
        gbc.gridy++;
        panel.add(opcionC, gbc);
        gbc.gridy++;
        panel.add(opcionD, gbc);

        gbc.gridy++;
        JLabel lblTiempo = new JLabel("Ingresa el tiempo en segundos que quieres que la prueba se ejecute:");
        lblTiempo.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(lblTiempo, gbc);

        gbc.gridy++;
        JPanel panelTiempo = new JPanel(new BorderLayout());
        JTextField campoNumero = new JTextField();
        campoNumero.setFont(new Font("Arial", Font.PLAIN, 16));
        campoNumero.setPreferredSize(new Dimension(300, 34));

        JLabel etiquetaSegundos = new JLabel(" segundos");
        etiquetaSegundos.setFont(new Font("Arial", Font.PLAIN, 16));
        etiquetaSegundos.setForeground(Color.GRAY);

        panelTiempo.add(campoNumero, BorderLayout.CENTER);
        panelTiempo.add(etiquetaSegundos, BorderLayout.EAST);
        panel.add(panelTiempo, gbc);

        gbc.gridy++;
        JLabel notaEjemplo = new JLabel("Eje. 180 segundos para 3 minutos");
        notaEjemplo.setFont(new Font("Arial", Font.ITALIC, 13));
        notaEjemplo.setForeground(Color.GRAY);
        panel.add(notaEjemplo, gbc);

        gbc.gridy++;
        JButton btnIniciar = new JButton("Iniciar prueba");
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 16));
        btnIniciar.setEnabled(false);
        panel.add(btnIniciar, gbc);

        // Validación dinámica
        DocumentListener validador = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validarCampos(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validarCampos(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validarCampos(); }

            private void validarCampos() {
                String texto = campoNumero.getText().trim();
                boolean tiempoValido = texto.matches("\\d+");
                boolean opcionSeleccionada = opcionA.isSelected() || opcionB.isSelected() || opcionC.isSelected() || opcionD.isSelected();
                btnIniciar.setEnabled(tiempoValido && opcionSeleccionada);
            }
        };
        campoNumero.getDocument().addDocumentListener(validador);

        // Listener para radios
        ActionListener radioListener = e -> {
            String texto = campoNumero.getText().trim();
            boolean tiempoValido = texto.matches("\\d+");
            boolean opcionSeleccionada = opcionA.isSelected() || opcionB.isSelected() || opcionC.isSelected() || opcionD.isSelected();
            btnIniciar.setEnabled(tiempoValido && opcionSeleccionada);
        };
        opcionA.addActionListener(radioListener);
        opcionB.addActionListener(radioListener);
        opcionC.addActionListener(radioListener);
        opcionD.addActionListener(radioListener);

        // Bloquear letras y permitir borrar
        campoNumero.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.CHAR_UNDEFINED) {
                    e.consume();
                    JOptionPane.showMessageDialog(panel, "Solo puede haber números en este campo", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción del botón
        btnIniciar.addActionListener(e -> {
            char caso = 'A';
            if (opcionB.isSelected()) caso = 'B';
            else if (opcionC.isSelected()) caso = 'C';
            else if (opcionD.isSelected()) caso = 'D';

            int segundos = Integer.parseInt(campoNumero.getText().trim());
            new VentanaConfirmacionDatos(caso, segundos).setVisible(true);
            this.dispose();
        });
        getRootPane().setDefaultButton(btnIniciar);

        add(panel);
    }
}