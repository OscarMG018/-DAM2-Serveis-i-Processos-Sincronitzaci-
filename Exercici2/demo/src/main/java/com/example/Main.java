package com.example;

import java.util.concurrent.Semaphore;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*

# Exercici 2

Una aplicació de gestió d'aparcaments ha de controlar l'accés a un 
aparcament amb capacitat limitada. Els cotxes només poden entrar si
hi ha espai disponible, i quan un cotxe surt, un altre pot entrar.

**Objectiu**

Implementa un sistema en Java que simuli l'entrada i sortida de cotxes
en un aparcament utilitzant un semàfor per controlar la capacitat.

**Requisits**

- Crea una classe Java amb un mètode main.

- Defineix una classe ParkingLot que tingui un semàfor amb un nombre limitat de permisos (igual a la capacitat de l'aparcament).

- Defineix tasques (Runnable) que simulin l'entrada i sortida de cotxes a l'aparcament. Quan un cotxe entra, adquireix un permís, i quan surt, allibera un permís.

- Utilitza un ExecutorService amb un pool de fils per simular l'entrada i sortida de cotxes de manera concurrent.

- Mostra a la consola cada vegada que un cotxe entra o surt de l'aparcament i quan un cotxe espera perquè l'aparcament està ple.

- Assegura't de tancar l'executor al final.

**Important**: Fes servir el format MVN habitual, i no t'oblidis dels arxius 'run.ps1' i 'run.sh' */


class Car implements Runnable {
    private final String name;
    private final ParkingLot parkingLot;

    public Car(String name, ParkingLot parkingLot) {
        this.name = name;
        this.parkingLot = parkingLot;
    }

    @Override
    public void run() {
        System.out.println(name + " is waiting to enter the parking lot");
        try {
            parkingLot.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + " is entering the parking lot");
        Random random = new Random();
        try {
            Thread.sleep(random.nextInt(1000, 5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + " is exiting the parking lot");
        parkingLot.getSemaphore().release();
    }
}

class ParkingLot {
    private final Semaphore semaphore;

    public ParkingLot(int capacity) {
        this.semaphore = new Semaphore(capacity);
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }
}


public class Main {

    static final int CARS_COUNT = 10;
    static final int PARKING_LOT_CAPACITY = 2;
    static final ParkingLot parkingLot = new ParkingLot(PARKING_LOT_CAPACITY);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(CARS_COUNT);
        for (int i = 0; i < CARS_COUNT; i++) {
            executorService.execute(new Car("Car " + i, parkingLot));
        }
        executorService.shutdown();
    }
}