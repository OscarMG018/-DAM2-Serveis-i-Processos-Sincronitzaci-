package com.example;

import java.util.List;
import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*

# Exercici 1

Una empresa vol millorar el rendiment del càlcul de la mitjana,
la suma i la desviació estàndard d'un conjunt gran de dades.
L'empresa ha decidit dividir el treball en tres fases paral·leles
que s'han de sincronitzar abans de mostrar els resultats finals.

**Objectiu**

Implementa un sistema en Java que simuli el càlcul de la mitjana, 
la suma i la desviació estàndard en paral·lel. Utilitza una CyclicBarrier 
per assegurar que tots els càlculs han estat realitzats abans de mostrar 
els resultats finals.

**Requisits**

- Crea una classe Java amb un mètode main.

- Defineix tres tasques (Runnable) que facin els càlculs de la mitjana, la suma i la desviació estàndard d'un conjunt de dades.

- Utilitza una CyclicBarrier per sincronitzar les tres tasques, de manera que només es mostrin els resultats finals quan totes hagin acabat.

- Quan tots els càlculs hagin completat el seu treball, mostra els resultats finals a la consola.

- Utilitza un ExecutorService amb un pool de fils per executar les tasques en paral·lel.

- Assegura't de tancar l'executor al final.

**Important**: Fes servir el format MVN habitual, i no t'oblidis dels arxius 'run.ps1' i 'run.sh' */

class MitjanaTask implements Runnable {
    private List<Double> dades;
    private CyclicBarrier barrier;

    public MitjanaTask(List<Double> dades, CyclicBarrier barrier) {
        this.dades = dades;
        this.barrier = barrier;
    }
    
    @Override
    public void run() {
        double mitjana = 0;
        double suma = 0;
        for (double d : dades) {
            suma += d;
        }
        mitjana = suma / dades.size();
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("Mitjana: " + mitjana);
    }
}

class SumaTask implements Runnable {
    private List<Double> dades;
    private CyclicBarrier barrier;

    public SumaTask(List<Double> dades, CyclicBarrier barrier) {
        this.dades = dades;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        double suma = 0;
        for (double d : dades) {
            suma += d;
        }
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("Suma: " + suma);
    }
}

class DesviacioTask implements Runnable {
    private List<Double> dades;
    private CyclicBarrier barrier;

    public DesviacioTask(List<Double> dades, CyclicBarrier barrier) {
        this.dades = dades;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        double mitjana = 0;
        double suma = 0;
        for (double d : dades) {
            suma += d;
        }
        mitjana = suma / dades.size();
        double desviacio = 0;
        for (double d : dades) {
            desviacio += Math.pow(d - mitjana, 2);
        }
        desviacio = Math.sqrt(desviacio / dades.size());
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("Desviació: " + desviacio);
    }
}

public class Main {
    public static void main(String[] args) {
        List<Double> dades = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0);
        CyclicBarrier barrier = new CyclicBarrier(3);

        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.execute(new MitjanaTask(dades, barrier));
        executor.execute(new SumaTask(dades, barrier));
        executor.execute(new DesviacioTask(dades, barrier));
        executor.shutdown();
    }
}