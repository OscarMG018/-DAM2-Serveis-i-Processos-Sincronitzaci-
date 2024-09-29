package com.example;

import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

/*

# Exercici 0

Una empresa està desenvolupant una aplicació basada en microserveis,
on cada microservei processa una part específica de les dades.
La tasca de cada microservei ha de ser sincronitzada amb les altres per
garantir que les dades processades estiguin correctament agregades abans d'enviar una resposta al client.

**Objectiu**

Implementa un sistema en Java que simuli l'execució de tres microserveis diferents que processen
dades de manera concurrent. Utilitza una **CyclicBarrier** per sincronitzar aquestes tasques i garantir
que totes han acabat el seu treball abans de combinar els resultats finals. Aquesta sincronització és
crucial per assegurar que la resposta enviada al client inclou tots els resultats processats correctament.

**Requisits**

- Crea una classe Java amb un mètode main.

- Defineix tres tasques (Runnable) que simulin el processament de dades en tres microserveis
diferents. Cadascuna d'elles ha de retornar un resultat parcial.

- Utilitza una CyclicBarrier per sincronitzar les tres tasques, de manera que només es procedeix
 a la següent fase quan totes hagin acabat.

- Quan totes les tasques hagin completat el seu treball, combina els resultats parcials en un resultat
final i mostra'l a la consola.

- Utilitza un ExecutorService amb un pool de fils per executar les tasques en paral·lel.

- Assegura't de tancar l'executor al final.

**Important**: Fes servir el format MVN habitual, i no t'oblidis dels arxius 'run.ps1' i 'run.sh' */

class MicroserviceTask implements Callable<String> {
    private final int serviceId;
    private final CyclicBarrier barrier;

    public MicroserviceTask(int serviceId, CyclicBarrier barrier) {
        this.serviceId = serviceId;
        this.barrier = barrier;
    }

    @Override
    public String call() throws Exception {
        System.out.println("Microservice " + serviceId + " started processing.");
        Thread.sleep((long) (Math.random() * 1000)); // Simulate processing time
        String result = "Result_" + serviceId;
        System.out.println("Microservice " + serviceId + " finished processing.");
        barrier.await();
        return result;
    }
}

public class Main {
    private static final int NUM_SERVICES = 3;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_SERVICES);
        CyclicBarrier barrier = new CyclicBarrier(NUM_SERVICES, () -> System.out.println("All microservices have completed their tasks."));
        List<Future<String>> results = new ArrayList<>();

        try {
            for (int i = 0; i < NUM_SERVICES; i++) {
                final int serviceId = i + 1;
                results.add(executor.submit(new MicroserviceTask(serviceId, barrier)));
            }

            List<String> processedResults = new ArrayList<>();
            for (Future<String> result : results) {
                processedResults.add(result.get());
            }

            String finalResult = String.join(" ", processedResults);
            System.out.println("Final combined result: " + finalResult);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }
    }
}