package Model;

import java.util.concurrent.atomic.AtomicInteger;

public class Client implements Comparable{
    private static int staticID = 0;
    private int ID;
    private int arrivalTime;
    private AtomicInteger serviceTime;

    public Client() {
    }

    public Client(int arrivalTime, int sT) {
        this.ID = ++staticID;
        this.arrivalTime = arrivalTime;
        serviceTime = new AtomicInteger(sT);
    }

    public int getID() {
        return ID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime.get();
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime.getAndSet(serviceTime);
    }

    @Override
    public int compareTo(Object o) {
        Client c = (Client) o;
        return this.arrivalTime - c.arrivalTime;
    }

    @Override
    public String toString(){
        return "(" + ID + "; " + arrivalTime + "; " + serviceTime + ")";
    }
}
