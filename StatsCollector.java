package ed_proyectofinal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class StatsCollector {
    private final ConcurrentHashMap<String, AtomicInteger> counts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> totalTimes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> minTimes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> maxTimes = new ConcurrentHashMap<>();
    private int chosenSize;

    // Tiempos por algoritmo y tamaño
    private final ConcurrentHashMap<String, ConcurrentHashMap<Integer, List<Long>>> timesBySize = new ConcurrentHashMap<>();

    // Registrar ejecución global
    public void record(String key, long millis) {
        counts.computeIfAbsent(key, k -> new AtomicInteger(0)).incrementAndGet();
        totalTimes.merge(key, millis, Long::sum);
        minTimes.merge(key, millis, (oldVal, newVal) -> Math.min(oldVal, newVal));
        maxTimes.merge(key, millis, (oldVal, newVal) -> Math.max(oldVal, newVal));
    }

    // Registrar ejecución con tamaño
    public void record(String key, int size, long millis) {
        record(key, millis);
        timesBySize
            .computeIfAbsent(key, k -> new ConcurrentHashMap<>())
            .computeIfAbsent(size, s -> new ArrayList<>())
            .add(millis);
    }

    // Getters de métricas
    public int getCount(String key) {
        AtomicInteger v = counts.get(key);
        return v == null ? 0 : v.get();
    }

    public long getTotalTime(String key) {
        return totalTimes.getOrDefault(key, 0L);
    }

    public double getAverage(String key) {
        int c = getCount(key);
        if (c == 0) return Double.NaN;
        return (double) getTotalTime(key) / c;
    }

    public long getMin(String key) {
        return minTimes.getOrDefault(key, 0L);
    }

    public long getMax(String key) {
        return maxTimes.getOrDefault(key, 0L);
    }

    public double getAverageBySize(String key, int size) {
        List<Long> lista = timesBySize
                .getOrDefault(key, new ConcurrentHashMap<>())
                .getOrDefault(size, new ArrayList<>());
        if (lista.isEmpty()) return Double.NaN;
        return lista.stream().mapToLong(Long::longValue).average().orElse(Double.NaN);
    }

    public Set<Integer> getSizesForKey(String key) {
        return timesBySize.getOrDefault(key, new ConcurrentHashMap<>()).keySet();
    }

    public Set<String> getAllKeys() {
        Set<String> todas = new HashSet<>(counts.keySet());
        todas.addAll(totalTimes.keySet());
        todas.addAll(timesBySize.keySet());
        return todas;
    }

    public void setChosenSize(int size) {
        this.chosenSize = size;
    }

    public int getChosenSize() {
        return chosenSize;
    }

    // Reporte en consola (desacoplado de AlgorithmCollection)
    public void printReport() {
        System.out.println("\n--- Reporte por función ---");

        // Derivar nombres base (sin sufijo Array/List) a partir de claves registradas
        Set<String> baseNames = getAllKeys().stream()
                .map(k -> k.endsWith("Array") ? k.substring(0, k.length() - "Array".length())
                        : k.endsWith("List") ? k.substring(0, k.length() - "List".length()) : k)
                .collect(Collectors.toCollection(HashSet::new));

        List<String> orden = new ArrayList<>(baseNames);
        Collections.sort(orden);

        for (String base : orden) {
            String kArr = base + "Array";
            String kList = base + "List";

            if (counts.containsKey(kArr) || totalTimes.containsKey(kArr)) {
                System.out.printf("%s - Count: %d, Total ms: %d, Promedio ms: %.3f, Min: %d, Max: %d%n",
                        kArr, getCount(kArr), getTotalTime(kArr), getAverage(kArr), getMin(kArr), getMax(kArr));
            }
            if (counts.containsKey(kList) || totalTimes.containsKey(kList)) {
                System.out.printf("%s - Count: %d, Total ms: %d, Promedio ms: %.3f, Min: %d, Max: %d%n%n",
                        kList, getCount(kList), getTotalTime(kList), getAverage(kList), getMin(kList), getMax(kList));
            }
        }
    }

    // Ranking por promedio (menor = más eficiente)
    public void printRanking() {
        System.out.println("\n--- Ranking por promedio (menor = más eficiente) ---");
        Map<String, Double> map = new HashMap<>();
        for (String key : getAllKeys()) {
            double avg = getCount(key) > 0 ? getAverage(key) : Double.POSITIVE_INFINITY;
            map.put(key, avg);
        }

        List<Map.Entry<String, Double>> ordenados = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        int rank = 1;
        for (Map.Entry<String, Double> entry : ordenados) {
            String clave = entry.getKey();
            double valor = entry.getValue();
            String avgStr = (valor == Double.POSITIVE_INFINITY) ? "N/A" : String.format("%.3f ms", valor);
            System.out.printf("%2d. %s --> %s%n", rank++, clave, avgStr);
        }
    }
}