package ed_proyectofinal;

/*
 Roberto Naredo Medellin AL07149679
 Rodrigo Mejia Rocha AL03028726
 Areli Soraya Perdue Centeno AL03072223
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import javax.swing.*;


public class Main {

    /* --------------------------- CONFIG --------------------------- */
    private static final int SIZE_A = 100;        // Opcion A
    private static final int SIZE_B = 50000;      // Opcion B
    private static final int SIZE_C = 100000;     // Opcion C
    private static final int SIZE_D = 100000;     // Opcion D (1..5)
    private static final int PRUEBA = 10;         // LISTA DE PRUEBA (10 elementos)
    
    
    /* --------------------------- MAIN --------------------------- */
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        AlgorithmCollection algos = new AlgorithmCollection();
        boolean keepRunning = true;

        System.out.println("Proyecto: Comparacion concurrente de algoritmos de ordenamiento");
        System.out.println("Primero se ejecutaran pruebas iniciales con 10 elementos de 3 digitos para validar las 12 funciones.");

        // Generar base de 10 elementos (prueba inicial)
        int[] base10Array = DataGenerator.generateRandomArray(PRUEBA);
        ArrayList<Integer> base10List = DataGenerator.arrayToArrayList(base10Array);

        // Ejecutar las 12 funciones una vez cada una (secuencialmente, no paralelo)
        System.out.println("\n--- Prueba inicial (10 elementos) - Ejecutando las 12 funciones una vez cada una ---\n");
        runInitialTests(algos, base10Array, base10List);
        
        // Abrir la ventana gráfica con los mismos datos
        SwingUtilities.invokeLater(() -> {
            new VentanaPruebaFuncionamiento(algos, base10Array, base10List).setVisible(true);
        });


// Bucle principal del menú
do {
    try {
        // Pedir opción al usuario
        String opcion = ""; // ✅ Declarada fuera del while
        while (!opcion.matches("[ABCDabcd]")) {
            System.out.print("Selecciona opción (A/B/C/D): ");
            opcion = sc.nextLine().trim();
        }

        // Pedir tiempo al usuario
        int segundos = 0;
        while (segundos <= 0) {
            System.out.print("Ingresa tiempo total en segundos (ej. 180 para 3 minutos): ");
            try {
                segundos = Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                segundos = 0;
            }
        }

        // Ejecutar prueba y capturar resultados
            StatsCollector stats = runComparison(opcion.charAt(0), segundos, algos);

            // Declarar variables finales para usarlas en la lambda
            final char casoFinal = opcion.charAt(0);   // ✅ aquí defines casoFinal
            final int segundosFinal = segundos;
            final StatsCollector statsFinal = stats;

            // Mostrar ventana de finalización con resultados reales
            SwingUtilities.invokeLater(() -> {
                new VentanasResultados.VentanaFinal(casoFinal, segundosFinal, statsFinal).setVisible(true);
});



       

    } catch (Exception e) {
        System.err.println("Ha ocurrido un error durante la ejecución: " + e.getMessage());
    }

    // Preguntar si desea otra comparación
    String continueOption = "";
    while (!continueOption.matches("[SsNn]")) {
        System.out.print("\n¿Deseas realizar otra comparación? (S/N): ");
        continueOption = sc.nextLine().trim();
    }
    keepRunning = continueOption.equalsIgnoreCase("S");

} while (keepRunning);



        System.out.println("\nFin del programa..");
        sc.close();
    }

    /* --------------------------- Prueba inicial --------------------------- */
 public static void runInitialTests(AlgorithmCollection algos, int[] baseArray, ArrayList<Integer> baseList) {
    System.out.println("=== ARREGLO ORIGINAL ===");
    System.out.println(Arrays.toString(baseArray));
    System.out.println();

    for (String name : AlgorithmCollection.ALGO_NAMES) {
        try {
            // Explicacion breve
            System.out.println(name + ": " + getDescription(name));

            // Array variant
            int[] copyArr = Arrays.copyOf(baseArray, baseArray.length);
            long t0 = System.nanoTime();
            ArrayList<Integer> sortedFromArray = algos.sortArrayByName(name, copyArr);
            long t1 = System.nanoTime();
            double elapsedMsArr = (t1 - t0) / 1_000_000.0;
            System.out.println("Ordenado: " + sortedFromArray);
            System.out.printf("%sArray -> funcionando (%.3f ms)%n", name, elapsedMsArr);

            // List variant
            ArrayList<Integer> copyList = new ArrayList<>(baseList);
            long l0 = System.nanoTime();
            ArrayList<Integer> sortedFromList = algos.sortListByName(name, copyList);
            long l1 = System.nanoTime();
            double elapsedMsList = (l1 - l0) / 1_000_000.0;
            System.out.printf("%sList  -> funcionando (%.3f ms)%n%n", name, elapsedMsList);

        } catch (Exception e) {
            System.out.println("Error en prueba inicial para " + name + ": " + e.getMessage());
        }
    }

    System.out.println("===== FIN DE LA PRUEBA INICIAL =====");
}

private static String getDescription(String name) {
    switch (name) {
        case "Bubble":
            return "compara elementos adyacentes y los intercambia si estan en orden incorrecto.";
        case "Selection":
            return "busca el menor elemento y lo coloca al inicio, repitiendo el proceso.";
        case "Insertion":
            return "inserta cada elemento en su posicion correcta dentro de la parte ya ordenada.";
        case "Shell":
            return "mejora insertion sort usando saltos para mover elementos mas rapido.";
        case "Merge":
            return "divide el arreglo en mitades, las ordena y luego las combina.";
        case "Quick":
            return "elige un pivote y separa los menores y mayores, ordenando recursivamente.";
        default:
            return "algoritmo desconocido.";
    }
}
 
public static ArrayList<ResultadoAlgoritmo> runInitialTestsConDatos(AlgorithmCollection algos, int[] baseArray, ArrayList<Integer> baseList) {
    ArrayList<ResultadoAlgoritmo> resultados = new ArrayList<>();

    for (String name : AlgorithmCollection.ALGO_NAMES) {
        try {
            String descripcion = getDescription(name);

            // Caso especial: Bubble con 10 elementos → guardar pasos
            if (name.equals("Bubble") && baseArray.length == 10) {
                int[] copyArr = Arrays.copyOf(baseArray, baseArray.length);
                ArrayList<int[]> pasos = new ArrayList<>();

                long t0 = System.nanoTime();
                // Bubble instrumentado SOLO en prueba inicial
                for (int i = 0; i < copyArr.length - 1; i++) {
                    for (int j = 0; j < copyArr.length - i - 1; j++) {
                        if (copyArr[j] > copyArr[j + 1]) {
                            int temp = copyArr[j];
                            copyArr[j] = copyArr[j + 1];
                            copyArr[j + 1] = temp;
                            pasos.add(Arrays.copyOf(copyArr, copyArr.length)); // guardar estado
                        }
                    }
                }
                long t1 = System.nanoTime();
                double tiempoArray = (t1 - t0) / 1_000_000.0;

                // Lista también (usando tu método normal)
                ArrayList<Integer> copyList = new ArrayList<>(baseList);
                long l0 = System.nanoTime();
                ArrayList<Integer> sortedFromList = algos.sortListByName(name, copyList);
                long l1 = System.nanoTime();
                double tiempoList = (l1 - l0) / 1_000_000.0;

                // Convertir arreglo final a ArrayList<Integer>
                ArrayList<Integer> sortedFromArray = new ArrayList<>();
                for (int val : copyArr) {
                    sortedFromArray.add(val);
                }

                ResultadoAlgoritmo r = new ResultadoAlgoritmo(name, descripcion, sortedFromArray, tiempoArray, tiempoList);
                r.pasos = pasos;
                r.totalPasos = pasos.size();
                resultados.add(r);

            } else {
                // Los demás algoritmos se ejecutan como siempre
                int[] copyArr = Arrays.copyOf(baseArray, baseArray.length);
                long t0 = System.nanoTime();
                ArrayList<Integer> sortedFromArray = algos.sortArrayByName(name, copyArr);
                long t1 = System.nanoTime();
                double tiempoArray = (t1 - t0) / 1_000_000.0;

                ArrayList<Integer> copyList = new ArrayList<>(baseList);
                long l0 = System.nanoTime();
                ArrayList<Integer> sortedFromList = algos.sortListByName(name, copyList);
                long l1 = System.nanoTime();
                double tiempoList = (l1 - l0) / 1_000_000.0;

                resultados.add(new ResultadoAlgoritmo(name, descripcion, sortedFromArray, tiempoArray, tiempoList));
            }

        } catch (Exception e) {
            System.out.println("Error en prueba inicial para " + name + ": " + e.getMessage());
        }
    }

    return resultados;
}


    /* --------------------------- Comparacion concurrente --------------------------- */
public static StatsCollector runComparison(char option, int segundos, AlgorithmCollection algos) throws InterruptedException {
    int chosenSize;
    int[] baseArray;
    ArrayList<Integer> baseList;

    // Selección del tamaño según la opción
    switch (Character.toUpperCase(option)) {
        case 'A':
            chosenSize = SIZE_A;
            baseArray = DataGenerator.generateRandomArray(chosenSize);
            break;
        case 'B':
            chosenSize = SIZE_B;
            baseArray = DataGenerator.generateRandomArray(chosenSize);
            break;
        case 'C':
            chosenSize = SIZE_C;
            baseArray = DataGenerator.generateRandomArray(chosenSize);
            break;
        case 'D':
        default:
            chosenSize = SIZE_D;
            baseArray = DataGenerator.generateRandomArrayRange(chosenSize, 1, 5);
            break;
    }
    baseList = DataGenerator.arrayToArrayList(baseArray);

    System.out.println("Colección seleccionada: " + option + " (" + chosenSize + " elementos)");
    System.out.println("Tiempo total (segundos): " + segundos);
    System.out.println("Iniciando ejecución concurrente con 12 hilos...\n");

    // ✅ Crear StatsCollector y guardar el tamaño elegido
    StatsCollector stats = new StatsCollector();
    stats.setChosenSize(chosenSize);

    CountDownLatch startLatch = new CountDownLatch(1);
    Thread[] threads = new Thread[12];
    int idx = 0;
    long endTimeMillis = System.currentTimeMillis() + segundos * 1000L;

    // Crear hilos para arrays
    for (String algoName : AlgorithmCollection.ALGO_NAMES) {
        threads[idx++] = new Thread(new SortWorker(algoName, true, baseArray, baseList, endTimeMillis, startLatch, stats, algos));
    }
    // Crear hilos para listas
    for (String algoName : AlgorithmCollection.ALGO_NAMES) {
        threads[idx++] = new Thread(new SortWorker(algoName, false, baseArray, baseList, endTimeMillis, startLatch, stats, algos));
    }

    // Lanzar hilos
    for (Thread t : threads) t.start();
    Thread.sleep(50);
    long realStart = System.currentTimeMillis();
    startLatch.countDown();

    for (Thread t : threads) t.join();

    long realEnd = System.currentTimeMillis();
    System.out.println("\nEjecución finalizada. Tiempo total real (ms): " + (realEnd - realStart));

    stats.printReport();
    stats.printRanking();
    return stats;
    
}





    /* --------------------------- Util de verificacion --------------------------- */
    private static boolean isSortedList(ArrayList<Integer> list) {
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i - 1) > list.get(i)) return false;
        }
        return true;
    }





    /* --------------------------- AlgorithmCollection --------------------------- */
    static class AlgorithmCollection {
        public static final String[] ALGO_NAMES = {"Bubble", "Selection", "Insertion", "Shell", "Merge", "Quick"};

        private final BubbleSort bubble = new BubbleSort();
        private final SelectionSort selection = new SelectionSort();
        private final InsertionSort insertion = new InsertionSort();
        private final ShellSort shell = new ShellSort();
        private final MergeSort merge = new MergeSort();
        private final QuickSort quick = new QuickSort();

        public ArrayList<Integer> sortArrayByName(String name, int[] arr) {
            switch (name) {
                case "Bubble": return bubble.sortArray(arr);
                case "Selection": return selection.sortArray(arr);
                case "Insertion": return insertion.sortArray(arr);
                case "Shell": return shell.sortArray(arr);
                case "Merge": return merge.sortArray(arr);
                case "Quick": return quick.sortArray(arr);
                default: throw new IllegalArgumentException("Algoritmo desconocido: " + name);
            }
        }

        public ArrayList<Integer> sortListByName(String name, ArrayList<Integer> list) {
            switch (name) {
                case "Bubble": return bubble.sortList(list);
                case "Selection": return selection.sortList(list);
                case "Insertion": return insertion.sortList(list);
                case "Shell": return shell.sortList(list);
                case "Merge": return merge.sortList(list);
                case "Quick": return quick.sortList(list);
                default: throw new IllegalArgumentException("Algoritmo desconocido: " + name);
            }
        }
    }

 
    /* --------------------------- Sorters (implementaciones) --------------------------- */
    interface Sorter {
        ArrayList<Integer> sortArray(int[] data);
        ArrayList<Integer> sortList(ArrayList<Integer> data);
    }

    static class BubbleSort implements Sorter {
        @Override
        public ArrayList<Integer> sortArray(int[] data) {
            int n = data.length;
            int[] arr = Arrays.copyOf(data, n);
            for (int i = 0; i < n - 1; i++) {
                boolean swapped = false;
                for (int j = 0; j < n - 1 - i; j++) {
                    if (arr[j] > arr[j + 1]) {
                        int tmp = arr[j]; arr[j] = arr[j + 1]; arr[j + 1] = tmp;
                        swapped = true;
                    }
                }
                if (!swapped) break;
            }
            return DataGenerator.arrayToArrayList(arr);
        }

        @Override
        public ArrayList<Integer> sortList(ArrayList<Integer> data) {
            int[] arr = listToArray(data);
            return sortArray(arr);
        }
    }

    static class SelectionSort implements Sorter {
        @Override
        public ArrayList<Integer> sortArray(int[] data) {
            int n = data.length;
            int[] arr = Arrays.copyOf(data, n);
            for (int i = 0; i < n - 1; i++) {
                int minIdx = i;
                for (int j = i + 1; j < n; j++) {
                    if (arr[j] < arr[minIdx]) minIdx = j;
                }
                int tmp = arr[minIdx]; arr[minIdx] = arr[i]; arr[i] = tmp;
            }
            return DataGenerator.arrayToArrayList(arr);
        }

        @Override
        public ArrayList<Integer> sortList(ArrayList<Integer> data) {
            int[] arr = listToArray(data);
            return sortArray(arr);
        }
    }

    static class InsertionSort implements Sorter {
        @Override
        public ArrayList<Integer> sortArray(int[] data) {
            int n = data.length;
            int[] arr = Arrays.copyOf(data, n);
            for (int i = 1; i < n; i++) {
                int key = arr[i];
                int j = i - 1;
                while (j >= 0 && arr[j] > key) {
                    arr[j + 1] = arr[j];
                    j--;
                }
                arr[j + 1] = key;
            }
            return DataGenerator.arrayToArrayList(arr);
        }

        @Override
        public ArrayList<Integer> sortList(ArrayList<Integer> data) {
            int[] arr = listToArray(data);
            return sortArray(arr);
        }
    }

    static class ShellSort implements Sorter {
        @Override
        public ArrayList<Integer> sortArray(int[] data) {
            int n = data.length;
            int[] arr = Arrays.copyOf(data, n);
            for (int gap = n / 2; gap > 0; gap /= 2) {
                for (int i = gap; i < n; i++) {
                    int temp = arr[i];
                    int j = i;
                    while (j >= gap && arr[j - gap] > temp) {
                        arr[j] = arr[j - gap];
                        j -= gap;
                    }
                    arr[j] = temp;
                }
            }
            return DataGenerator.arrayToArrayList(arr);
        }

        @Override
        public ArrayList<Integer> sortList(ArrayList<Integer> data) {
            int[] arr = listToArray(data);
            return sortArray(arr);
        }
    }

    static class MergeSort implements Sorter {
        @Override
        public ArrayList<Integer> sortArray(int[] data) {
            int[] arr = Arrays.copyOf(data, data.length);
            mergeSort(arr, 0, arr.length - 1);
            return DataGenerator.arrayToArrayList(arr);
        }

        private void mergeSort(int[] a, int left, int right) {
            if (left >= right) return;
            int mid = left + (right - left) / 2;
            mergeSort(a, left, mid);
            mergeSort(a, mid + 1, right);
            merge(a, left, mid, right);
        }

        private void merge(int[] a, int left, int mid, int right) {
            int n1 = mid - left + 1;
            int n2 = right - mid;
            int[] L = new int[n1];
            int[] R = new int[n2];
            System.arraycopy(a, left, L, 0, n1);
            System.arraycopy(a, mid + 1, R, 0, n2);
            int i = 0, j = 0, k = left;
            while (i < n1 && j < n2) {
                if (L[i] <= R[j]) a[k++] = L[i++];
                else a[k++] = R[j++];
            }
            while (i < n1) a[k++] = L[i++];
            while (j < n2) a[k++] = R[j++];
        }

        @Override
        public ArrayList<Integer> sortList(ArrayList<Integer> data) {
            int[] arr = listToArray(data);
            return sortArray(arr);
        }
    }

    static class QuickSort implements Sorter {
        @Override
        public ArrayList<Integer> sortArray(int[] data) {
            int[] arr = Arrays.copyOf(data, data.length);
            quickSort(arr, 0, arr.length - 1);
            return DataGenerator.arrayToArrayList(arr);
        }

        private void quickSort(int[] a, int lo, int hi) {
            if (hi - lo > 10000) {
                insertionSort(a, lo, hi);
                return;
            }
            if (lo >= hi) return;

            int p = partition(a, lo, hi);
            quickSort(a, lo, p - 1);
            quickSort(a, p + 1, hi);
        }

        private int partition(int[] a, int lo, int hi) {
            int pivot = a[hi];
            int i = lo - 1;
            for (int j = lo; j < hi; j++) {
                if (a[j] <= pivot) {
                    i++;
                    int t = a[i]; a[i] = a[j]; a[j] = t;
                }
            }
            int t = a[i + 1]; a[i + 1] = a[hi]; a[hi] = t;
            return i + 1;
        }

        private void insertionSort(int[] a, int lo, int hi) {
            for (int i = lo + 1; i <= hi; i++) {
                int key = a[i];
                int j = i - 1;
                while (j >= lo && a[j] > key) {
                    a[j + 1] = a[j];
                    j--;
                }
                a[j + 1] = key;
            }
        }

        @Override
        public ArrayList<Integer> sortList(ArrayList<Integer> data) {
            int[] arr = listToArray(data);
            return sortArray(arr);
        }
    }

    /* --------------------------- Utilities --------------------------- */
    private static int[] listToArray(ArrayList<Integer> list) {
        int[] arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        return arr;
    }
    
}