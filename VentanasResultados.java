package ed_proyectofinal;

import ed_proyectofinal.Main.AlgorithmCollection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.*;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import org.jfree.chart.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class VentanasResultados {
 /* ----------------------- Ventana Final ------------------------------ */
public static class VentanaFinal extends JFrame {
    public VentanaFinal(char caso, int segundos, StatsCollector stats) {
        setTitle("Métodos de clasificación");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        // Encabezado
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Prueba completada con éxito", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ✅ Subtítulo dinámico según el caso
        String descripcionCaso = switch (caso) {
            case 'A' -> "100 elementos aleatorios";
            case 'B' -> "50,000 elementos aleatorios";
            case 'C' -> "100,000 elementos aleatorios";
            case 'D' -> "100,000 elementos aleatorios (1–5)";
            default -> stats.getChosenSize() + " elementos aleatorios";
        };

        String subtituloTexto = descripcionCaso + " por " + segundos + " segundos";
        JLabel subtitulo = new JLabel(subtituloTexto, SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitulo.setForeground(Color.DARK_GRAY);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(titulo);
        header.add(Box.createVerticalStrut(6));
        header.add(subtitulo);

        root.add(header, BorderLayout.NORTH);

        // Área central con resultados
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setLineWrap(false);
        area.setWrapStyleWord(false);

        StringBuilder sb = new StringBuilder();
        sb.append("\n📊 Reporte por función:\n\n");
        for (String clave : stats.getAllKeys()) {
            int count = stats.getCount(clave);
            long total = stats.getTotalTime(clave);
            double avg = stats.getAverage(clave);
            String avgStr = Double.isNaN(avg) ? "N/A" : String.format("%.3f ms", avg);
            sb.append(String.format("%-14s → Count: %4d | Total: %6d ms | Promedio: %8s\n",
                    clave, count, total, avgStr));
        }

        // Ranking por promedio
        sb.append("\n🏆 Ranking por promedio (menor es más eficiente):\n\n");
        Map<String, Double> map = new HashMap<>();
        for (String clave : stats.getAllKeys()) {
            double avg = stats.getCount(clave) > 0 ? stats.getAverage(clave) : Double.POSITIVE_INFINITY;
            map.put(clave, avg);
        }

        List<Map.Entry<String, Double>> ordenados = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(java.util.stream.Collectors.toList());

        int rank = 1;
        for (Map.Entry<String, Double> entry : ordenados) {
            String clave = entry.getKey();
            double valor = entry.getValue();
            String avgStr = (valor == Double.POSITIVE_INFINITY) ? "N/A" : String.format("%.3f ms", valor);
            sb.append(String.format("%2d. %-14s → %8s\n", rank++, clave, avgStr));
        }

        // ✅ Algoritmos que completaron todas las colecciones
        sb.append("\n✅ Algoritmos que completaron todas las colecciones:\n\n");
        Set<String> baseNames = stats.getAllKeys().stream()
                .map(k -> k.endsWith("Array") ? k.substring(0, k.length() - "Array".length())
                        : k.endsWith("List") ? k.substring(0, k.length() - "List".length()) : k)
                .collect(Collectors.toCollection(HashSet::new));

        List<String> completaronTodos = new ArrayList<>();
        for (String base : baseNames) {
            boolean okArrays = true, okList = true;
            for (int size : stats.getSizesForKey(base + "Array")) {
                if (Double.isNaN(stats.getAverageBySize(base + "Array", size))) okArrays = false;
            }
            for (int size : stats.getSizesForKey(base + "List")) {
                if (Double.isNaN(stats.getAverageBySize(base + "List", size))) okList = false;
            }
            if (okArrays && okList) completaronTodos.add(base);
        }

        if (completaronTodos.isEmpty()) {
            sb.append("Ninguno (o la ejecución fue por un solo tamaño).\n");
        } else {
            for (String nombre : completaronTodos) {
                sb.append("• ").append(nombre).append("\n");
            }
        }

        // Complejidades teóricas
        sb.append("\n📘 Complejidades teóricas:\n\n");
        sb.append("BubbleSort, SelectionSort, InsertionSort → O(n²)\n");
        sb.append("ShellSort → O(n log² n)\n");
        sb.append("MergeSort → O(n log n)\n");
        sb.append("QuickSort → O(n log n) promedio, O(n²) peor caso\n");

        area.setText(sb.toString());
        area.setCaretPosition(0);

        JScrollPane scroll = new JScrollPane(area,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        root.add(scroll, BorderLayout.CENTER);

        // Botonera inferior
        JPanel botones = new JPanel(new GridLayout(1, 4, 20, 10));

        JButton btnOtraPrueba = new JButton("Otra prueba");
        btnOtraPrueba.addActionListener(e -> {
            new VentanaConfiguracionEjecucion().setVisible(true);
            this.dispose();
        });

        JButton btnComplejidades = new JButton("Comprobar complejidades teóricas");
        btnComplejidades.addActionListener(e -> {
            AlgorithmCollection algos = new AlgorithmCollection();
            new VentanaComplejidades(segundos, algos).setVisible(true);
        });

        // 🔹 Nuevo botón
        JButton btnLimitados = new JButton("¿Qué pasa con #1–5?");
        btnLimitados.addActionListener(e -> {
            AlgorithmCollection algos = new AlgorithmCollection();
            new VentanaComplejidadesLimitadas(segundos, algos).setVisible(true);
        });

        JButton btnCerrar = new JButton("Cerrar programa");
        btnCerrar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Gracias por usar el programa. ¡Hasta pronto!");
            System.exit(0);
        });

        botones.add(btnOtraPrueba);
        botones.add(btnComplejidades);
        botones.add(btnLimitados);
        botones.add(btnCerrar);

        root.add(botones, BorderLayout.SOUTH);
        setContentPane(root);
    }
}



  /* ----------------------- Ventana Complejidades ------------------------------ */
public static class VentanaComplejidades extends JFrame {
    public VentanaComplejidades(int segundos, AlgorithmCollection algos) {
        setTitle("Comprobación de complejidades teóricas");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        JLabel titulo = new JLabel("Resultados de comprobación teórica", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        root.add(titulo, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        JLabel loading = new JLabel("Cargando resultados…", SwingConstants.CENTER);
        center.add(loading, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);
        setContentPane(root);

        SwingWorker<StatsCollector, Void> worker = new SwingWorker<StatsCollector, Void>() {
            @Override
            protected StatsCollector doInBackground() throws Exception {
                return runComparisonMultiSizes(segundos, algos);
            }

            @Override
            protected void done() {
                try {
                    StatsCollector stats = get();

                    DefaultCategoryDataset datasetGlobal = new DefaultCategoryDataset();
                    for (String algoritmo : stats.getAllKeys()) {
                        List<Integer> ordenados = new ArrayList<>(stats.getSizesForKey(algoritmo));
                        Collections.sort(ordenados);
                        for (int tamaño : ordenados) {
                            double tiempo = stats.getAverageBySize(algoritmo, tamaño) * 1_000_000.0;
                            datasetGlobal.addValue(tiempo, algoritmo, String.valueOf(tamaño));
                        }
                    }

                    JFreeChart chartGlobal = ChartFactory.createLineChart(
                            "Comparación global de algoritmos",
                            "Tamaño",
                            "Tiempo promedio (ns)",
                            datasetGlobal
                    );

                    CategoryPlot plot = chartGlobal.getCategoryPlot();
                    LineAndShapeRenderer renderer = new LineAndShapeRenderer();
                    plot.setRenderer(renderer);

                    ChartPanel chartPanelGlobal = new ChartPanel(chartGlobal);
                    JPanel panelGrafica = new JPanel(new BorderLayout());
                    panelGrafica.add(chartPanelGlobal, BorderLayout.CENTER);

                    // 🔹 Botones Top 4, Mid 4, Bottom 4
                    JButton btnTop4 = new JButton("Ver top 4");
                    btnTop4.addActionListener(e -> new VentanaTop4(stats).setVisible(true));

                    JButton btnMid4 = new JButton("Ver mid 4");
                    btnMid4.addActionListener(e -> new VentanaMid4(stats).setVisible(true));

                    JButton btnBottom4 = new JButton("Ver bottom 4");
                    btnBottom4.addActionListener(e -> new VentanaBottom4(stats).setVisible(true));

                    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
                    panelBotones.add(btnTop4);
                    panelBotones.add(btnMid4);
                    panelBotones.add(btnBottom4);

                    panelGrafica.add(panelBotones, BorderLayout.SOUTH);

                    center.removeAll();
                    center.add(panelGrafica, BorderLayout.CENTER);
                    center.revalidate();
                    center.repaint();

                } catch (Exception e) {
                    center.removeAll();
                    center.add(new JLabel("Error: " + e.getMessage(), SwingConstants.CENTER), BorderLayout.CENTER);
                }
            }
        };
        worker.execute();
    }
}

public static class VentanaComplejidadesLimitadas extends JFrame {
    public VentanaComplejidadesLimitadas(int segundos, AlgorithmCollection algos) {
        setTitle("Comprobación de complejidades teóricas (datos 1–5)");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        JLabel titulo = new JLabel("Resultados con datos limitados (1–5)", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        root.add(titulo, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        JLabel loading = new JLabel("Cargando resultados…", SwingConstants.CENTER);
        center.add(loading, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);
        setContentPane(root);

        SwingWorker<StatsCollector, Void> worker = new SwingWorker<StatsCollector, Void>() {
            @Override
            protected StatsCollector doInBackground() throws Exception {
                return runComparisonMultiSizesLimitados(segundos, algos);
            }

            @Override
            protected void done() {
                try {
                    StatsCollector stats = get();

                    DefaultCategoryDataset datasetGlobal = new DefaultCategoryDataset();
                    for (String algoritmo : stats.getAllKeys()) {
                        List<Integer> ordenados = new ArrayList<>(stats.getSizesForKey(algoritmo));
                        Collections.sort(ordenados);
                        for (int tamaño : ordenados) {
                            double tiempo = stats.getAverageBySize(algoritmo, tamaño) * 1_000_000.0;
                            datasetGlobal.addValue(tiempo, algoritmo, String.valueOf(tamaño));
                        }
                    }

                    JFreeChart chartGlobal = ChartFactory.createLineChart(
                            "Comparación global de algoritmos (datos 1–5)",
                            "Tamaño",
                            "Tiempo promedio (ns)",
                            datasetGlobal
                    );

                    CategoryPlot plot = chartGlobal.getCategoryPlot();
                    plot.setRenderer(new LineAndShapeRenderer());

                    ChartPanel chartPanelGlobal = new ChartPanel(chartGlobal);
                    JPanel panelGrafica = new JPanel(new BorderLayout());
                    panelGrafica.add(chartPanelGlobal, BorderLayout.CENTER);

                    // 🔹 Botones Top 4, Mid 4, Bottom 4
                    JButton btnTop4 = new JButton("Ver top 4");
                    btnTop4.addActionListener(e -> new VentanaTop4(stats).setVisible(true));

                    JButton btnMid4 = new JButton("Ver mid 4");
                    btnMid4.addActionListener(e -> new VentanaMid4(stats).setVisible(true));

                    JButton btnBottom4 = new JButton("Ver bottom 4");
                    btnBottom4.addActionListener(e -> new VentanaBottom4(stats).setVisible(true));

                    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
                    panelBotones.add(btnTop4);
                    panelBotones.add(btnMid4);
                    panelBotones.add(btnBottom4);

                    panelGrafica.add(panelBotones, BorderLayout.SOUTH);

                    center.removeAll();
                    center.add(panelGrafica, BorderLayout.CENTER);
                    center.revalidate();
                    center.repaint();

                } catch (Exception e) {
                    center.removeAll();
                    center.add(new JLabel("Error: " + e.getMessage(), SwingConstants.CENTER), BorderLayout.CENTER);
                }
            }
        };
        worker.execute();
    }
}

public static StatsCollector runComparisonMultiSizesLimitados(int segundos, AlgorithmCollection algos) throws InterruptedException {
    int[] tamanos = {100, 1000, 10000, 50000, 100000, 200000};
    StatsCollector stats = new StatsCollector();
    Random rand = new Random();

    for (int chosenSize : tamanos) {
        int[] baseArray = new int[chosenSize];
        for (int i = 0; i < chosenSize; i++) {
            baseArray[i] = rand.nextInt(5) + 1; // 🔹 valores entre 1 y 5
        }
        ArrayList<Integer> baseList = DataGenerator.arrayToArrayList(baseArray);

        CountDownLatch startLatch = new CountDownLatch(1);
        Thread[] threads = new Thread[AlgorithmCollection.ALGO_NAMES.length * 2];
        int idx = 0;
        long endTimeMillis = System.currentTimeMillis() + segundos * 1000L;

        for (String algoName : AlgorithmCollection.ALGO_NAMES) {
            threads[idx++] = new Thread(new SortWorker(algoName, true, baseArray, baseList,
                    endTimeMillis, startLatch, stats, algos));
        }
        for (String algoName : AlgorithmCollection.ALGO_NAMES) {
            threads[idx++] = new Thread(new SortWorker(algoName, false, baseArray, baseList,
                    endTimeMillis, startLatch, stats, algos));
        }

        for (Thread t : threads) t.start();
        Thread.sleep(50);
        startLatch.countDown();
        for (Thread t : threads) t.join();
    }

    return stats;
}


  /* ----------------------- Ventana Top4 ------------------------------ */
  public static class VentanaTop4 extends JFrame {
    public VentanaTop4(StatsCollector stats) {
        setTitle("Top 4 algoritmos más eficientes");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        JLabel titulo = new JLabel("Comparativa de los 4 algoritmos más rápidos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        root.add(titulo, BorderLayout.NORTH);

        DefaultCategoryDataset datasetTop4 = new DefaultCategoryDataset();

        // 🔹 Obtener top 4 por promedio
        Map<String, Double> map = new HashMap<>();
        for (String clave : stats.getAllKeys()) {
            double avg = stats.getCount(clave) > 0 ? stats.getAverage(clave) : Double.POSITIVE_INFINITY;
            map.put(clave, avg);
        }

        List<String> top4 = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(4)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 🔹 Usar mismos tamaños que la gráfica global
        int[] tamanos = {100, 1000, 10000, 50000, 100000, 200000};
        for (String algoritmo : top4) {
            for (int tamaño : tamanos) {
                double tiempo = stats.getAverageBySize(algoritmo, tamaño) * 1_000_000.0;
                datasetTop4.addValue(tiempo, algoritmo, String.valueOf(tamaño));
            }
        }

        JFreeChart chartTop4 = ChartFactory.createLineChart(
                "Top 4 algoritmos por eficiencia",
                "Tamaño",
                "Tiempo promedio (ns)",
                datasetTop4
        );

        CategoryPlot plotTop4 = chartTop4.getCategoryPlot();
        LineAndShapeRenderer rendererTop4 = new LineAndShapeRenderer();
        plotTop4.setRenderer(rendererTop4);

        ChartPanel chartPanelTop4 = new ChartPanel(chartTop4);
        root.add(chartPanelTop4, BorderLayout.CENTER);
        setContentPane(root);
    }
}
  
    /* ----------------------- Ventana Mid4 ------------------------------ */
  public static class VentanaMid4 extends JFrame {
    public VentanaMid4(StatsCollector stats) {
        setTitle("Mid 4 algoritmos intermedios");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        JLabel titulo = new JLabel("Comparativa de los 4 algoritmos intermedios", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        root.add(titulo, BorderLayout.NORTH);

        DefaultCategoryDataset datasetMid4 = new DefaultCategoryDataset();

        Map<String, Double> map = new HashMap<>();
        for (String clave : stats.getAllKeys()) {
            double avg = stats.getCount(clave) > 0 ? stats.getAverage(clave) : Double.POSITIVE_INFINITY;
            map.put(clave, avg);
        }

        List<String> ordenados = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Tomar los 4 del medio
        int start = (ordenados.size() / 2) - 2;
        if (start < 0) start = 0;
        List<String> mid4 = ordenados.subList(start, Math.min(start + 4, ordenados.size()));

        int[] tamanos = {100, 1000, 10000, 50000, 100000, 200000};
        for (String algoritmo : mid4) {
            for (int tamaño : tamanos) {
                double tiempo = stats.getAverageBySize(algoritmo, tamaño) * 1_000_000.0;
                datasetMid4.addValue(tiempo, algoritmo, String.valueOf(tamaño));
            }
        }

        JFreeChart chartMid4 = ChartFactory.createLineChart(
                "Mid 4 algoritmos por eficiencia",
                "Tamaño",
                "Tiempo promedio (ns)",
                datasetMid4
        );

        CategoryPlot plotMid4 = chartMid4.getCategoryPlot();
        plotMid4.setRenderer(new LineAndShapeRenderer());

        ChartPanel chartPanelMid4 = new ChartPanel(chartMid4);
        root.add(chartPanelMid4, BorderLayout.CENTER);
        setContentPane(root);
    }
}

  
    /* ----------------------- Ventana Bottom4 ------------------------------ */
  public static class VentanaBottom4 extends JFrame {
    public VentanaBottom4(StatsCollector stats) {
        setTitle("Bottom 4 algoritmos menos eficientes");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        JLabel titulo = new JLabel("Comparativa de los 4 algoritmos más lentos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        root.add(titulo, BorderLayout.NORTH);

        DefaultCategoryDataset datasetBottom4 = new DefaultCategoryDataset();

        Map<String, Double> map = new HashMap<>();
        for (String clave : stats.getAllKeys()) {
            double avg = stats.getCount(clave) > 0 ? stats.getAverage(clave) : Double.POSITIVE_INFINITY;
            map.put(clave, avg);
        }

        List<String> bottom4 = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(4)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        int[] tamanos = {100, 1000, 10000, 50000, 100000, 200000};
        for (String algoritmo : bottom4) {
            for (int tamaño : tamanos) {
                double tiempo = stats.getAverageBySize(algoritmo, tamaño) * 1_000_000.0;
                datasetBottom4.addValue(tiempo, algoritmo, String.valueOf(tamaño));
            }
        }

        JFreeChart chartBottom4 = ChartFactory.createLineChart(
                "Bottom 4 algoritmos por eficiencia",
                "Tamaño",
                "Tiempo promedio (ns)",
                datasetBottom4
        );

        CategoryPlot plotBottom4 = chartBottom4.getCategoryPlot();
        plotBottom4.setRenderer(new LineAndShapeRenderer());

        ChartPanel chartPanelBottom4 = new ChartPanel(chartBottom4);
        root.add(chartPanelBottom4, BorderLayout.CENTER);
        setContentPane(root);
    }
}


    /* ----------------------- Comparación automática ------------------------------ */
    public static StatsCollector runComparisonMultiSizes(int segundos, ed_proyectofinal.Main.AlgorithmCollection algos) throws InterruptedException {
        int[] tamanos = {100, 1000, 10000, 50000, 100000, 200000};
        StatsCollector stats = new StatsCollector();

        for (int chosenSize : tamanos) {
            int[] baseArray = DataGenerator.generateRandomArray(chosenSize);
            java.util.ArrayList<Integer> baseList = DataGenerator.arrayToArrayList(baseArray);

            CountDownLatch startLatch = new CountDownLatch(1);
            Thread[] threads = new Thread[ed_proyectofinal.Main.AlgorithmCollection.ALGO_NAMES.length * 2];
            int idx = 0;
            long endTimeMillis = System.currentTimeMillis() + segundos * 1000L;

            for (String algoName : ed_proyectofinal.Main.AlgorithmCollection.ALGO_NAMES) {
                threads[idx++] = new Thread(new SortWorker(algoName, true, baseArray, baseList,
                        endTimeMillis, startLatch, stats, algos));
            }
            for (String algoName : ed_proyectofinal.Main.AlgorithmCollection.ALGO_NAMES) {
                threads[idx++] = new Thread(new SortWorker(algoName, false, baseArray, baseList,
                        endTimeMillis, startLatch, stats, algos));
            }

            for (Thread t : threads) t.start();
            Thread.sleep(50);
            startLatch.countDown();
            for (Thread t : threads) t.join();
        }

        stats.printReport();
        stats.printRanking();
        return stats;
    }
}