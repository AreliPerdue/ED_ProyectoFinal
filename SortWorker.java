package ed_proyectofinal;

import ed_proyectofinal.Main.AlgorithmCollection;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class SortWorker implements Runnable {
    private final String algoName;
    private final boolean isArrayVariant;
    private final int[] baseArray;
    private final ArrayList<Integer> baseList;
    private final long endTimeMillis;
    private final CountDownLatch startLatch;
    private final StatsCollector stats;
    private final AlgorithmCollection algos;

    public SortWorker(String algoName, boolean isArrayVariant, int[] baseArray, ArrayList<Integer> baseList, long endTimeMillis, CountDownLatch startLatch, StatsCollector stats, AlgorithmCollection algos) {
        this.algoName = algoName;
        this.isArrayVariant = isArrayVariant;
        this.baseArray = baseArray;
        this.baseList = baseList;
        this.endTimeMillis = endTimeMillis;
        this.startLatch = startLatch;
        this.stats = stats;
        this.algos = algos;
    }

    @Override
    public void run() {
        try {
            // Esperar hasta que todos los hilos estén listos
            startLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        while (System.currentTimeMillis() < endTimeMillis) {
            if (isArrayVariant) {
                int[] working = DataGenerator.cloneArray(baseArray);
                long t0 = System.nanoTime();
                try {
                    algos.sortArrayByName(algoName, working);
                    long t1 = System.nanoTime();
                    long millis = (t1 - t0) / 1_000_000;
                    stats.record(algoName + "Array", baseArray.length, millis);
                } catch (Exception e) {
                    System.out.println(Thread.currentThread().getName() + " -> Error en " + algoName + ": " + e.getMessage());
                    break;
                }
            } else {
                ArrayList<Integer> workingList = DataGenerator.cloneList(baseList);
                long t0 = System.nanoTime();
                try {
                    algos.sortListByName(algoName, workingList);
                    long t1 = System.nanoTime();
                    long millis = (t1 - t0) / 1_000_000;
                    stats.record(algoName + "List", baseList.size(), millis);
                } catch (Exception e) {
                    System.out.println(Thread.currentThread().getName() + " -> Error en " + algoName + ": " + e.getMessage());
                    break;
                }
            }

            try {
                Thread.sleep(1); // evita saturar CPU
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}