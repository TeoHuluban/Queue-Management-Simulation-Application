package Logic;

import Model.Client;
import Model.Server;

import java.util.List;

public class ShortestQueueStrategy implements Strategy{
    @Override
    public void addClient(List<Server> servers, Client t) {
        int minClients = Integer.MAX_VALUE;
        Server aux = null;

        for(Server q: servers) {
            int cTotal = 0;
            for(Client c: q.getClients())
                cTotal++;

            if(cTotal < minClients) {
                minClients = cTotal;
                aux = q;
            }
        }

        aux.addClient(t);
    }
}
