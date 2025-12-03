package ed_proyectofinal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DataGenerator {
    private static final Random rnd = new Random();

    // Genera n números aleatorios de 3 dígitos (100–999)
    public static int[] generateRandomArray(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = rnd.nextInt(900) + 100;
        return a;
    }

    // Genera n números aleatorios en rango [minInclusive, maxInclusive]
    public static int[] generateRandomArrayRange(int n, int minInclusive, int maxInclusive) {
        int[] a = new int[n];
        int range = maxInclusive - minInclusive + 1;
        for (int i = 0; i < n; i++) a[i] = rnd.nextInt(range) + minInclusive;
        return a;
    }

    // Convierte arreglo a ArrayList<Integer>
    public static ArrayList<Integer> arrayToArrayList(int[] arr) {
        ArrayList<Integer> list = new ArrayList<>(arr.length);
        for (int v : arr) list.add(v);
        return list;
    }

    // Clonar arreglo
    public static int[] cloneArray(int[] src) {
        return Arrays.copyOf(src, src.length);
    }

    // Clonar lista
    public static ArrayList<Integer> cloneList(ArrayList<Integer> src) {
        return new ArrayList<>(src);
    }
}