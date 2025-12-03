package ed_proyectofinal;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class VentanaPruebaFuncionamiento extends JFrame {
    public VentanaPruebaFuncionamiento(Main.AlgorithmCollection algos, int[] baseArray, ArrayList<Integer> baseList) {
        setTitle("Prueba de Funcionamiento");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblOriginal = new JLabel("Arreglo original: " + arrayToString(baseArray));
        lblOriginal.setFont(new Font("Arial", Font.BOLD, 16));
        lblOriginal.setHorizontalAlignment(SwingConstants.CENTER);
        panelPrincipal.add(lblOriginal, BorderLayout.NORTH);

        JPanel panelAlgoritmos = new JPanel(new GridLayout(0, 2, 20, 20));
        ArrayList<ResultadoAlgoritmo> resultados = Main.runInitialTestsConDatos(algos, baseArray, baseList);

        for (ResultadoAlgoritmo r : resultados) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.DARK_GRAY),
                    r.nombre,
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14)
            ));

            JTextArea area = new JTextArea();
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 13));
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            area.setText(
                    "Descripción: " + r.descripcion + "\n\n" +
                    "Ordenado: " + r.ordenado + "\n\n" +
                    String.format("Tiempo Array: %.3f ms\n", r.tiempoArray) +
                    String.format("Tiempo List : %.3f ms\n", r.tiempoList)
            );
            panel.add(area, BorderLayout.CENTER);

            /* -------------------------- BUBBLE -------------------------- */
            if (r.nombre.equals("Bubble") && baseArray.length == 10) {
                ArrayList<String> pasosConFormato = new ArrayList<>();
                int[] copyArr = Arrays.copyOf(baseArray, baseArray.length);

                for (int i = 0; i < copyArr.length - 1; i++) {
                    for (int j = 0; j < copyArr.length - i - 1; j++) {
                        int a = copyArr[j];
                        int b = copyArr[j + 1];
                        boolean haySwap = a > b;

                        String explicacion;
                        if (haySwap) {
                            explicacion = "Intercambio entre " + a + " y " + b + " porque " + a + " es mayor →";
                            int temp = copyArr[j];
                            copyArr[j] = copyArr[j + 1];
                            copyArr[j + 1] = temp;
                        } else {
                            explicacion = "Sin intercambio entre " + a + " y " + b + " porque " + a + " es menor →";
                        }

                        // Construir arreglo con resaltado
                        StringBuilder arrStr = new StringBuilder("[");
                        for (int k = 0; k < copyArr.length; k++) {
                            if (k == j || k == j + 1) {
                                arrStr.append("*").append(copyArr[k]);
                            } else {
                                arrStr.append(copyArr[k]);
                            }
                            if (k < copyArr.length - 1) arrStr.append(", ");
                        }
                        arrStr.append("]");

                        pasosConFormato.add("Paso " + (pasosConFormato.size() + 1) + ": " + explicacion + "\n" + arrStr.toString() + "\n");
                    }
                }

                JButton btnPantallaCompleta = new JButton("Ver procedimiento en pantalla completa");
                btnPantallaCompleta.setFont(new Font("Arial", Font.BOLD, 12));
                btnPantallaCompleta.addActionListener(e -> {
                    new VentanasProcedimientos(r, pasosConFormato, baseArray).setVisible(true);
                });
                panel.add(btnPantallaCompleta, BorderLayout.SOUTH);
            }

            /* -------------------------- INSERTION -------------------------- */
            if (r.nombre.equals("Insertion") && baseArray.length == 10) {
                ArrayList<String> pasosConFormato = new ArrayList<>();
                int[] copyArr = Arrays.copyOf(baseArray, baseArray.length);

                for (int i = 1; i < copyArr.length; i++) {
                    int key = copyArr[i];
                    int j = i - 1;

                    // Vista inicial con la key marcada
                    StringBuilder vistaInicial = new StringBuilder("[");
                    for (int k = 0; k < copyArr.length; k++) {
                        if (k == i) vistaInicial.append("<").append(copyArr[k]).append(">");
                        else vistaInicial.append(copyArr[k]);
                        if (k < copyArr.length - 1) vistaInicial.append(", ");
                    }
                    vistaInicial.append("]");

                    StringBuilder explicacion = new StringBuilder();
                    explicacion.append("Paso ").append(pasosConFormato.size() + 1)
                               .append(" (i = ").append(i).append("): ").append(vistaInicial).append("\n")
                               .append("            Toma el valor ").append(key).append("\n");

                    while (j >= 0) {
                        explicacion.append("            Lo compara con ").append(copyArr[j]);
                        if (copyArr[j] > key) {
                            explicacion.append(" → lo mueve\n");
                            copyArr[j + 1] = copyArr[j];
                            j--;
                        } else {
                            explicacion.append(" → no lo mueve\n");
                            break;
                        }
                    }

                    copyArr[j + 1] = key;

                    StringBuilder resultadoPaso = new StringBuilder("[");
                    for (int k = 0; k < copyArr.length; k++) {
                        resultadoPaso.append(copyArr[k]);
                        if (k < copyArr.length - 1) resultadoPaso.append(", ");
                    }
                    resultadoPaso.append("]");

                    explicacion.append("Resultado: ").append(resultadoPaso).append("\n");

                    pasosConFormato.add(explicacion.toString() + "\n");
                }

                JButton btnPantallaCompleta = new JButton("Ver procedimiento en pantalla completa");
                btnPantallaCompleta.setFont(new Font("Arial", Font.BOLD, 12));
                btnPantallaCompleta.addActionListener(e -> {
                    new VentanasProcedimientos(r, pasosConFormato, baseArray).setVisible(true);
                });
                panel.add(btnPantallaCompleta, BorderLayout.SOUTH);
            }
            /* -------------------------- SELECTION -------------------------- */
            if (r.nombre.equals("Selection") && baseArray.length == 10) {
                ArrayList<String> pasosConFormato = new ArrayList<>();
                int[] copyArr = Arrays.copyOf(baseArray, baseArray.length);

                for (int i = 0; i < copyArr.length - 1; i++) {
                    int minIndex = i;

                    // Vista inicial con la posición i marcada
                    StringBuilder vistaInicial = new StringBuilder("[");
                    for (int k = 0; k < copyArr.length; k++) {
                        if (k == i) vistaInicial.append("<").append(copyArr[k]).append(">");
                        else vistaInicial.append(copyArr[k]);
                        if (k < copyArr.length - 1) vistaInicial.append(", ");
                    }
                    vistaInicial.append("]");

                    StringBuilder explicacion = new StringBuilder();
                    explicacion.append("Paso ").append(pasosConFormato.size() + 1)
                               .append(" (i = ").append(i).append("): ").append(vistaInicial).append("\n")
                               .append("            Busca el menor desde posición ").append(i).append("\n");

                    // Buscar el menor en el subarreglo
                    for (int j = i + 1; j < copyArr.length; j++) {
                        if (copyArr[j] < copyArr[minIndex]) {
                            minIndex = j;
                        }
                    }

                    if (minIndex != i) {
                        explicacion.append("            Encuentra ").append(copyArr[minIndex]).append(" como menor\n")
                                   .append("            Intercambia ").append(copyArr[i]).append(" con ").append(copyArr[minIndex]).append(" →\n");
                        int temp = copyArr[i];
                        copyArr[i] = copyArr[minIndex];
                        copyArr[minIndex] = temp;
                    } else {
                        explicacion.append("            Encuentra ").append(copyArr[i]).append(" como menor\n")
                                   .append("            No hay intercambio →\n");
                    }

                    // Resultado del paso
                    StringBuilder resultadoPaso = new StringBuilder("[");
                    for (int k = 0; k < copyArr.length; k++) {
                        resultadoPaso.append(copyArr[k]);
                        if (k < copyArr.length - 1) resultadoPaso.append(", ");
                    }
                    resultadoPaso.append("]");

                    explicacion.append("Resultado: ").append(resultadoPaso).append("\n");

                    pasosConFormato.add(explicacion.toString() + "\n");
                }

                JButton btnPantallaCompleta = new JButton("Ver procedimiento en pantalla completa");
                btnPantallaCompleta.setFont(new Font("Arial", Font.BOLD, 12));
                btnPantallaCompleta.addActionListener(e -> {
                    new VentanasProcedimientos(r, pasosConFormato, baseArray).setVisible(true);
                });
                panel.add(btnPantallaCompleta, BorderLayout.SOUTH);
            }
                        /* -------------------------- SHELL -------------------------- */
            if (r.nombre.equals("Shell") && baseArray.length != 0) {
                ArrayList<String> pasosConFormato = new ArrayList<>();
                int[] copyArr = baseArray.clone();
                int n = baseArray.length;

                int gap = n / 2;
                int contadorPaso = 1;
                Integer gapAnteriorReal = null;

                while (gap > 0) {
                    StringBuilder paso = new StringBuilder();

                    if (contadorPaso == 1) {
                        paso.append("Paso ").append(contadorPaso)
                            .append(": se define el gap como la mitad (entera) del tamaño del arreglo. Tamaño del arreglo: ")
                            .append(n).append(" → gap = ").append(gap).append("\n");
                        paso.append("Anterior gap = (no aplica)\n");
                    } else {
                        paso.append("Paso ").append(contadorPaso)
                            .append(": como ya tenemos un gap anteriormente definido, tomamos la mitad (entera) del gap anterior. ")
                            .append("Anterior gap = ").append(gapAnteriorReal).append(" → gap = ").append(gap).append("\n");
                    }

                    for (int i = gap; i < n; i++) {
                        int temp = copyArr[i];
                        int j = i;

                        paso.append("          Compara posición ").append(i - gap)
                            .append(" con posición ").append(i)
                            .append(" → (").append(copyArr[i - gap]).append(" y ").append(temp).append(")\n");

                        if (copyArr[i - gap] > temp) {
                            paso.append("          i").append(i - gap).append(" = ").append(copyArr[i - gap])
                                .append(" es mayor que i").append(i).append(" = ").append(temp)
                                .append(" → se realizará inserción con salto (desplazamientos)\n");

                            while (j >= gap && copyArr[j - gap] > temp) {
                                copyArr[j] = copyArr[j - gap];
                                j -= gap;
                            }
                            copyArr[j] = temp;

                            paso.append("          Inserta ").append(temp).append(" en posición ").append(j).append("\n");
                        } else {
                            paso.append("          i").append(i - gap).append(" = ").append(copyArr[i - gap])
                                .append(" es menor o igual que i").append(i).append(" = ").append(temp)
                                .append(" → no intercambian posiciones\n");
                        }
                    }

                    paso.append("Arreglo tras finalizar el pase con gap ").append(gap).append(": [");
                    for (int k = 0; k < n; k++) {
                        paso.append(copyArr[k]);
                        if (k < n - 1) paso.append(", ");
                    }
                    paso.append("]\n");

                    pasosConFormato.add(paso.toString());

                    gapAnteriorReal = gap;
                    gap = gap / 2;
                    contadorPaso++;
                }

                JButton btnPantallaCompleta = new JButton("Ver procedimiento en pantalla completa");
                btnPantallaCompleta.setFont(new Font("Arial", Font.BOLD, 12));
                btnPantallaCompleta.addActionListener(e -> {
                    new VentanasProcedimientos(r, pasosConFormato, baseArray).setVisible(true);
                });
                panel.add(btnPantallaCompleta, BorderLayout.SOUTH);
            }
            /* -------------------------- MERGE SORT -------------------------- */
            if (r.nombre.equals("Merge") && baseArray.length != 0) {
                ArrayList<String> pasosConFormato = new ArrayList<>();
                int[] arr = Arrays.copyOf(baseArray, baseArray.length);
                final int[] paso = {1};

                final java.util.function.BiFunction<int[], int[], String> sliceToString = (a, range) -> {
                    int l = range[0], r2 = range[1];
                    StringBuilder sb = new StringBuilder("[");
                    for (int i = l; i <= r2; i++) {
                        sb.append(a[i]);
                        if (i < r2) sb.append(", ");
                    }
                    sb.append("]");
                    return sb.toString();
                };

                pasosConFormato.add("Arreglo original:\n" + Arrays.toString(arr) + "\n");
                pasosConFormato.add("Procedimiento paso por paso:\n");

                class MergeNarrativo {
                    void sort(int[] a, int left, int right, String ctxSide, int refStep) {
                        if (left >= right) return;

                        int mid = (left + right) / 2;
                        String actual = sliceToString.apply(a, new int[]{left, right});
                        String izq = sliceToString.apply(a, new int[]{left, mid});
                        String der = sliceToString.apply(a, new int[]{mid + 1, right});

                        if (left == 0 && right == a.length - 1) {
                            int stepSplit = paso[0]++;
                            pasosConFormato.add("Paso " + stepSplit + ": Dividimos el arreglo en 2 mitades " + actual);
                            pasosConFormato.add("→ " + izq + " y " + der + "\n");

                            sort(a, left, mid, "left", stepSplit);
                            sort(a, mid + 1, right, "right", stepSplit);

                            merge(a, left, mid, right);
                            return;
                        }

                        int stepSplit = paso[0]++;
                        if ("left".equals(ctxSide)) {
                            pasosConFormato.add("Paso " + stepSplit + ": Ahora el lado izquierdo del paso anterior " + actual + 
                                    "; lo volvemos a dividir a la mitad");
                        } else if ("right".equals(ctxSide)) {
                            pasosConFormato.add("Paso " + stepSplit + ": Ahora el lado derecho del Paso " + refStep + " " + actual + 
                                    "; lo volvemos a dividir a la mitad");
                        } else {
                            pasosConFormato.add("Paso " + stepSplit + ": Lo dividimos a la mitad " + actual);
                        }

                        pasosConFormato.add("→ " + izq + " y " + der + "\n");

                        sort(a, left, mid, "left", stepSplit);
                        sort(a, mid + 1, right, "right", stepSplit);

                        merge(a, left, mid, right);
                    }

                    void merge(int[] a, int left, int mid, int right) {
                        int[] L = Arrays.copyOfRange(a, left, mid + 1);
                        int[] R = Arrays.copyOfRange(a, mid + 1, right + 1);

                        int i = 0, j = 0, k = left;
                        while (i < L.length && j < R.length) {
                            a[k++] = (L[i] <= R[j]) ? L[i++] : R[j++];
                        }
                        while (i < L.length) a[k++] = L[i++];
                        while (j < R.length) a[k++] = R[j++];

                        pasosConFormato.add("Paso " + (paso[0]++) + ": Fusionamos " +
                                Arrays.toString(L) + " y " + Arrays.toString(R) + " de menor a mayor");
                        pasosConFormato.add("Resultado tras fusionar: " +
                                sliceToString.apply(a, new int[]{left, right}) + "\n");
                    }
                }

                new MergeNarrativo().sort(arr, 0, arr.length - 1, "root", 0);

                pasosConFormato.add("Resultado final: " + Arrays.toString(arr));
                pasosConFormato.add("\nTotal de pasos: " + (paso[0] - 1));

                JButton btnPantallaCompleta = new JButton("Ver procedimiento en pantalla completa");
                btnPantallaCompleta.setFont(new Font("Arial", Font.BOLD, 12));
                btnPantallaCompleta.addActionListener(e -> {
                    new VentanasProcedimientos(r, pasosConFormato, baseArray).setVisible(true);
                });
                panel.add(btnPantallaCompleta, BorderLayout.SOUTH);
            }

                /* -------------------------- QUICK SORT -------------------------- */
                if (r.nombre.equals("Quick") && baseArray.length != 0) {
                    ArrayList<String> pasosConFormato = new ArrayList<>();
                    int[] arr = Arrays.copyOf(baseArray, baseArray.length);
                    final int[] paso = {1};

                    pasosConFormato.add("Arreglo original:\n" + Arrays.toString(arr) + "\n");

                    class QuickNarrativo {
                        void quickSort(int[] a, int low, int high, String contexto) {
                            if (low >= high) return;

                            // Elegimos pivote (último elemento del segmento)
                            int pivot = a[high];
                            pasosConFormato.add("Paso " + (paso[0]++) + ": " +
                                (contexto == null ? "Elegimos un pivote al azar " : contexto + "; elegimos pivote ") + pivot);

                            // Partición
                            int i = low - 1;
                            for (int j = low; j < high; j++) {
                                if (a[j] <= pivot) {
                                    i++;
                                    int temp = a[i]; a[i] = a[j]; a[j] = temp;
                                }
                            }
                            int temp = a[i + 1]; a[i + 1] = a[high]; a[high] = temp;
                            int pivotIndex = i + 1;

                            int[] menores = Arrays.copyOfRange(a, low, pivotIndex);
                            int[] mayores = Arrays.copyOfRange(a, pivotIndex + 1, high + 1);

                            pasosConFormato.add("Paso " + (paso[0]++) + ": Reordenamos los elementos: menores a la izquierda, mayores a la derecha");
                            pasosConFormato.add("→ " + Arrays.toString(menores) + " | " + pivot + " | " + Arrays.toString(mayores) + "\n");

                            // Lado izquierdo
                            if (menores.length > 1) {
                                quickSort(a, low, pivotIndex - 1, "Empezamos a trabajar con el lado izquierdo " + Arrays.toString(menores));
                            }

                            // Lado derecho
                            if (mayores.length > 1) {
                                quickSort(a, pivotIndex + 1, high, "Ahora trabajamos con el lado derecho " + Arrays.toString(mayores));
                            }
                        }
                    }

                    new QuickNarrativo().quickSort(arr, 0, arr.length - 1, null);

                    pasosConFormato.add("Resultado final: " + Arrays.toString(arr));
                    pasosConFormato.add("\nTotal de pasos: " + (paso[0] - 1));

                    JButton btnPantallaCompleta = new JButton("Ver procedimiento en pantalla completa");
                    btnPantallaCompleta.setFont(new Font("Arial", Font.BOLD, 12));
                    btnPantallaCompleta.addActionListener(e -> {
                        new VentanasProcedimientos(r, pasosConFormato, baseArray).setVisible(true);
                    });
                    panel.add(btnPantallaCompleta, BorderLayout.SOUTH);
                }

            panelAlgoritmos.add(panel);
        }

        panelPrincipal.add(panelAlgoritmos, BorderLayout.CENTER);

        // 🔹 Botones inferiores
        JButton btnRepetir = new JButton("Ejecutar prueba de funcionamiento de nuevo");
        btnRepetir.setFont(new Font("Arial", Font.BOLD, 14));
        btnRepetir.addActionListener(e -> {
            int[] nuevoArray = DataGenerator.generateRandomArray(10);
            ArrayList<Integer> nuevaLista = DataGenerator.arrayToArrayList(nuevoArray);
            new VentanaPruebaFuncionamiento(new Main.AlgorithmCollection(), nuevoArray, nuevaLista).setVisible(true);
            this.dispose();
        });

        JButton btnContinuar = new JButton("Continuar a prueba de comparación de eficiencia");
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 14));
        btnContinuar.addActionListener(e -> {
            new VentanaConfiguracionEjecucion().setVisible(true);
            this.dispose();
        });

        JLabel creditos = new JLabel("Desarrollado por Areli Perdue, Roberto Naredo y Rodrigo Mejia");
        creditos.setFont(new Font("Arial", Font.PLAIN, 12));
        creditos.setForeground(Color.GRAY);

        JPanel botonesDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        botonesDerecha.add(btnRepetir);
        botonesDerecha.add(btnContinuar);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(creditos, BorderLayout.WEST);
        panelInferior.add(botonesDerecha, BorderLayout.EAST);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private static String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}