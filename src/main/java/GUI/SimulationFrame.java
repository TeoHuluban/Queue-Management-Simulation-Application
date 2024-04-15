package GUI;

import Model.Client;
import Model.Server;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimulationFrame extends JFrame {
    private List<Client> generatedClients;
    private List<Server> servers;
    private int noQueues;
    private Integer currentTime = 0;

    public SimulationFrame(List<Client> generatedClients, List<Server> servers, int noQueues, int currentTime) {
        this.generatedClients = generatedClients;
        this.servers = servers;
        this.noQueues = noQueues;
        this.currentTime = currentTime;
        setTitle("Simulation");
        setSize(1000, 700);
        setLocationRelativeTo(null);

        DrawingPanel drawingPanel = new DrawingPanel();
        JScrollPane scrollPane = new JScrollPane(drawingPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        getContentPane().add(scrollPane);
        setVisible(true);
    }

    private class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawComponents(g);
        }
    }

    private void drawComponents(Graphics g) {
        g.drawString(currentTime.toString(), 50, 20);
        int x = 30, y = 50, w = 50, h = 50;

        for(Client c: generatedClients){
            g.setColor(Color.black);
            g.drawString(c.toString(), x, y + h + 5);
            g.setColor(Color.pink);
            g.fillOval(x, y, w - 10, h - 10);

            y += 80;
        }

        x = 850;
        y = 50;
        for (int i = 0; i < noQueues; i++) {
            g.setColor(Color.black);
            g.drawString("Queue " + (i + 1), x + 2, y + h + 15);
            g.fillRect(x, y, w, h);

            y += 80;
        }

        y = 55;
        for (Server s : servers) {
            x = 790;
            for (Client c : s.getClients()) {
                g.setColor(Color.black);
                g.drawString(c.toString(), x, y + h + 5);
                g.setColor(Color.pink);
                g.fillOval(x, y, w - 10, h - 10);

                x -= 80;
            }
            y += 80;
        }
        currentTime++;
    }
}
