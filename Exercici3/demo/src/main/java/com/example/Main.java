package com.example;

import java.util.concurrent.*;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*

# Exercici 3

Un servidor web ha de gestionar l'accés concurrent a una pàgina amb contingut
limitat. Per evitar una sobrecàrrega, es limita el nombre màxim de connexions simultànies a la pàgina.

**Objectiu**

Implementa un sistema en Java que limiti el nombre de connexions simultànies a una pàgina web utilitzant un semàfor.

**Requisits**

- Crea una classe Java amb un mètode main.

- Defineix una classe WebPage que contingui un semàfor que limiti el nombre de connexions simultànies.

- Defineix tasques (Runnable) que simulin les connexions d'usuaris a la pàgina web. Cada connexió ha
d'adquirir un permís abans d'accedir a la pàgina i l'ha de lliurar quan es desconnecta.

- Simula múltiples connexions concurrentment utilitzant un ExecutorService amb un pool de fils.

- Mostra a la consola quan una connexió s'estableix, quan es desconnecta i quan una connexió ha d'esperar
perquè s'han superat les connexions màximes permeses.

- Assegura't de tancar l'executor al final.

**Important**: Fes servir el format MVN habitual, i no t'oblidis dels arxius 'run.ps1' i 'run.sh' */


class User implements Runnable {
    private final String name;
    private final WebPage webPage;

    public User(String name, WebPage webPage) {
        this.name = name;
        this.webPage = webPage;
    }

    @Override
    public void run() {
        System.out.println(name + " is loading the web page");
        try {
            webPage.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + " has connected to the web page");
        Random random = new Random();
        try {
            Thread.sleep(random.nextInt(1000, 5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + " has disconnected from the web page");
        webPage.getSemaphore().release();
    }
}

class WebPage {
    private final Semaphore semaphore;

    public WebPage(int capacity) {
        this.semaphore = new Semaphore(capacity);
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }
}


public class Main {

    static final int USERS_COUNT = 100;
    static final int WEB_PAGE_CAPACITY = 10;
    static final WebPage webPage = new WebPage(WEB_PAGE_CAPACITY);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(USERS_COUNT);
        for (int i = 0; i < USERS_COUNT; i++) {
            executorService.execute(new User("User " + i, webPage));
        }
        executorService.shutdown();
    }
}