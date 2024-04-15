package Model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private static int staticID = 0;
    private int ID;
    private BlockingQueue<Client> clients;
    private AtomicInteger waitingPeriod;
    private boolean running;

    public Server() {
        clients = new LinkedBlockingQueue<Client>();
        waitingPeriod = new AtomicInteger(0);
        running = true;
        ID = ++staticID;
    }

    public AtomicInteger getWaitingPeriod() {
        waitingPeriod.set(0);
        for(Client c: clients)
            waitingPeriod.getAndAdd(c.getServiceTime());
        return waitingPeriod;
    }

    public BlockingQueue<Client> getClients() {
        return clients;
    }

    public void addClient (Client newClient) {
        clients.add(newClient); //add client to queue
        waitingPeriod.addAndGet(newClient.getServiceTime()); //increment the waitingPeriod
    }

    @Override
    public void run() {
        while(running) {
            try {
                synchronized (clients) {
                    if (!clients.isEmpty()) {
                        Client c = clients.peek();

                        while (c.getServiceTime() > 0) {
                            Thread.sleep(1000);
                            c.setServiceTime(c.getServiceTime() - 1);
                            waitingPeriod = getWaitingPeriod();
                        }

                        clients.remove(c);
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void interrupt() {
        running = false;
    }

    public static int getStaticID() {
        return staticID;
    }

    public int getID() {
        return ID;
    }

    public boolean isRunning() {
        return running;
    }
}