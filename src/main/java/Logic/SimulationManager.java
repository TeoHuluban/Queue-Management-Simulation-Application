package Logic;

import GUI.SimulationFrame;
import Model.Client;
import Model.Server;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SimulationManager implements Runnable {
    //data read from UI
    public int timeLimit; //maximum processing time - read from UI
    public int minArrivalTime;
    public int maxArrivalTime;
    public int minProcessingTime;
    public int maxProcessingTime;
    public int numberOfQueues;
    public int numberOfClients;
    public SelectionPolicy selectionPolicy;

    //entity responsible with queue management and client distribution
    private Scheduler scheduler;
    //frame for displaying simulation
    private SimulationFrame frame;
    //pool of tasks (client shopping in the store)
    private List<Client> generatedClients;
    public int currentTime = 0;

    public SimulationManager(int timeLimit, int minArrivalTime,  int maxArrivalTime, int minProcessingTime, int maxProcessingTime, int numberOfQueues, int numberOfClients, SelectionPolicy selectionPolicy) {
        this.timeLimit = timeLimit;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minProcessingTime = minProcessingTime;
        this.maxProcessingTime = maxProcessingTime;
        this.numberOfQueues = numberOfQueues;
        this.numberOfClients = numberOfClients;
        this.selectionPolicy = selectionPolicy;

        scheduler = new Scheduler(numberOfQueues, numberOfClients);
        scheduler.changeStrategy(selectionPolicy);
        generatedClients = new LinkedList<Client>();
        generateNRandomClients();
        frame = new SimulationFrame(generatedClients, scheduler.getServers(), numberOfQueues, currentTime);
    }

    private void generateNRandomClients() {
        for(int i=0;i<numberOfClients;i++){
            Random rand = new Random();
            int arrvT = rand.nextInt(minArrivalTime, maxArrivalTime);
            int servT = rand.nextInt(minProcessingTime, maxProcessingTime);
            Client c = new Client(arrvT, servT);
            generatedClients.add(c);
        }

        if (generatedClients != null)
            Collections.sort(generatedClients);
    }

    synchronized void fileWrite(FileWriter writer, int currentTime){
        try {
            writer.write("\n\nTime: " + currentTime + "\nWaiting clients: ");

            for(Client c: generatedClients)
                writer.write(c + "; ");

            for (Server server : scheduler.getServers()) {
                writer.write("\nQueue" + server.getID() + ": ");
                if(server.getClients().isEmpty())
                    writer.write("closed");
                else
                    for(Client c: server.getClients()) {
                        writer.write(c + "; ");
                    }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getWaitingPeriodPerClient(Client cl){
        for (Server server : scheduler.getServers())
            for(Client c: server.getClients())
                if(c == cl) {
                    return server.getWaitingPeriod().get() - c.getServiceTime();
                }
        return 0;
    }

    @Override
    public void run() { try {
        int currentTime = 0;
        double totalWaitingTime = 0;
        double totalServiceTime = 0;
        int peekHourTime = Integer.MIN_VALUE;
        int peekHour = 0;

        FileWriter writer = null;
        String fileName = "log.txt";
        writer = new FileWriter(fileName, false);
        writer = new FileWriter(fileName, true);

        int ok = 1;
        while (currentTime < timeLimit && (!generatedClients.isEmpty() || ok == 1)) {
            Iterator<Client> iterator = generatedClients.iterator();
            while (iterator.hasNext()) {
                Client c = iterator.next();
                if (c.getArrivalTime() == currentTime) {
                    scheduler.dispatchClient(c);
                    totalWaitingTime += getWaitingPeriodPerClient(c);
                    System.out.println(c);
                    System.out.println(getWaitingPeriodPerClient(c));
                    totalServiceTime += c.getServiceTime();
                    iterator.remove();
                }
            }

            // update UI frame
            frame.repaint();
            // write in log.txt
            fileWrite(writer, currentTime);
            // wait an interval of 1 second
            Thread.sleep(1000);

            // check if the servers are empty + calc. peek hour
            ok = 0;
            int wT = 0;
            for (Server server : scheduler.getServers()){
                wT += server.getWaitingPeriod().get();
                if(!server.getClients().isEmpty())
                    ok = 1;
            }
            if(wT > peekHourTime) {
                peekHour = currentTime;
                peekHourTime = wT;
            }

            // increment time
            currentTime++;
        }

        frame.repaint();
        fileWrite(writer, currentTime);

        if(currentTime >= timeLimit)
            writer.write("\nTime is up!");
        writer.write("\n\nAverage waiting time: " + String.format("%.2f", totalWaitingTime/numberOfClients));
        writer.write("\n\nAverage service time: " + String.format("%.2f",totalServiceTime/numberOfClients));
        writer.write("\n\nPeek hour: " + peekHour);
        writer.close();

        for (Server server : scheduler.getServers()) {
            server.interrupt();
        }

        JOptionPane.showMessageDialog(new JFrame(),
                "Simulation ended: \n\nAverage waiting time: " + String.format("%.2f", totalWaitingTime/numberOfClients)
                + "\nAverage service time: " + String.format("%.2f",totalServiceTime/numberOfClients)
                + "\nPeek hour: " + peekHour, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SimulationManager sM = new SimulationManager(100, 1, 3, 1, 7, 2, 11, SelectionPolicy.SHORTEST_QUEUE);
        Thread t = new Thread(sM);
        t.start();
    }

}