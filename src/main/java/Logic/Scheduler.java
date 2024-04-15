package Logic;

import Model.Client;
import Model.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxClientsPerServer;
    private Strategy strategy;

    public Scheduler (int maxNoServers, int maxClientsPerServer) {

        this.servers = new ArrayList<Server>();
        this.maxClientsPerServer = maxClientsPerServer;
        this.maxNoServers = maxNoServers;

        ExecutorService es = Executors.newFixedThreadPool(maxNoServers);
        for (int i = 0; i < maxNoServers; i++){
            Server s = new Server();
            servers.add(s);
            es.execute(s);
        }
        es.shutdown();
    }

    public void changeStrategy (SelectionPolicy policy) {
        //apply strategy pattern to instantiate the strategy with the concrete strategy corresponding to policy
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ShortestQueueStrategy();
        }
        if (policy == SelectionPolicy.SHORTEST_TIME){
            strategy = new TimeStrategy();
        }
    }
    public void dispatchClient(Client t){
        strategy.addClient(servers, t); //call the strategy addClient method
    }

    public List<Server> getServers() {
        return servers;
    }

    public int getMaxNoServers() {
        return maxNoServers;
    }

    public int getMaxClientsPerServer() {
        return maxClientsPerServer;
    }

    public Strategy getStrategy() {
        return strategy;
    }
}
