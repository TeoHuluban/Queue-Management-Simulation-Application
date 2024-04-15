package Logic;

import Model.Client;
import Model.Server;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TimeStrategy implements Strategy {
    @Override
    public void addClient(List<Server> servers, Client t) {
        AtomicInteger minTime = new AtomicInteger(Integer.MAX_VALUE);
        Server aux = null;

        for(Server q: servers){
            if(minTime.get() > q.getWaitingPeriod().get()) {
                minTime.set(q.getWaitingPeriod().get());
                aux = q;
            }
        }

        aux.addClient(t);
    }
}
