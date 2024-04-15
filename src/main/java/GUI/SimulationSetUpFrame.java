package GUI;

import Logic.SelectionPolicy;
import Logic.SimulationManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Integer.parseInt;

public class SimulationSetUpFrame extends JFrame {
    private JLabel nrOfClientsLabel;
    private JLabel nrOfQueuesLabel;
    private JLabel simFinalLabel;
    private JLabel minArTimeLabel;
    private JLabel maxArTimeLabel;
    private JLabel minServTimeLabel;
    private JLabel maxServTimeLabel;
    private JTextField nrOfClientsField;
    private JTextField nrOfQueuesField;
    private JTextField simFinalField;
    private JTextField minArTimeField;
    private JTextField maxArTimeField;
    private JTextField minServTimeField;
    private JTextField maxServTimeField;
    private JButton validateButton;
    private JButton startButton;
    private JRadioButton st1;
    private JRadioButton st2;

    private boolean valid = false;
    public SimulationSetUpFrame(){
        setTitle("Simulation Set Up");
        setSize(600, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        nrOfClientsLabel = new JLabel("Number of clients:");
        nrOfQueuesLabel = new JLabel("Number of queues:");
        simFinalLabel = new JLabel("Simulation end time:");
        minArTimeLabel = new JLabel("Minimum arrival time:");
        maxArTimeLabel = new JLabel("Maximum arrival time:");
        minServTimeLabel = new JLabel("Minimum service time:");
        maxServTimeLabel = new JLabel("Maximum service time:");
        nrOfClientsField = new JTextField(20);
        nrOfQueuesField = new JTextField(20);
        simFinalField = new JTextField(20);
        minArTimeField = new JTextField(20);
        maxArTimeField = new JTextField(20);
        minServTimeField = new JTextField(20);
        maxServTimeField = new JTextField(20);
        validateButton = new JButton("Validate data");
        startButton = new JButton("Start simulation");
        st1 = new JRadioButton("Shortest time strategy");
        st2 = new JRadioButton("Shortest queue strategy");
        ButtonGroup group = new ButtonGroup();
        group.add(st1);
        group.add(st2);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        getContentPane().add(panel);

        JPanel panel1 = new JPanel();
        panel1.add(nrOfClientsLabel);
        panel1.add(nrOfClientsField);
        JPanel panel2 = new JPanel();
        panel2.add(nrOfQueuesLabel);
        panel2.add(nrOfQueuesField);
        JPanel panel4 = new JPanel();
        panel4.add(simFinalLabel);
        panel4.add(simFinalField);
        JPanel panel5 = new JPanel();
        panel5.add(minArTimeLabel);
        panel5.add(minArTimeField);
        JPanel panel6 = new JPanel();
        panel6.add(maxArTimeLabel);
        panel6.add(maxArTimeField);
        JPanel panel7 = new JPanel();
        panel7.add(minServTimeLabel);
        panel7.add(minServTimeField);
        JPanel panel8 = new JPanel();
        panel8.add(maxServTimeLabel);
        panel8.add(maxServTimeField);
        JPanel panel9 = new JPanel();
        panel9.add(st1);
        panel9.add(st2);
        JPanel panel10 = new JPanel();
        panel10.add(validateButton);
        JPanel panel11 = new JPanel();
        panel11.add(startButton);

        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel4);
        panel.add(panel5);
        panel.add(panel6);
        panel.add(panel7);
        panel.add(panel8);
        panel.add(panel9);
        panel.add(panel10);
        panel.add(panel11);

        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int clients = parseInt(nrOfClientsField.getText());
                    int queues = parseInt(nrOfQueuesField.getText());
                    int simFinal = parseInt(simFinalField.getText());
                    int minArT = parseInt(minArTimeField.getText());
                    int maxArT = parseInt(maxArTimeField.getText());
                    int minPT = parseInt(minServTimeField.getText());
                    int maxPT = parseInt(maxServTimeField.getText());
                    if(!(st1.isSelected() || st2.isSelected())) {
                        throw new NumberFormatException();
                    }
                    if(minArT >= maxArT) {
                        throw new NumberFormatException();
                    }
                    if(minPT >= maxPT) {
                        throw new NumberFormatException();
                    }
                    valid = true;
                    JOptionPane.showMessageDialog(new JFrame(), "Valid input data, press Start!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ee) {
                    JOptionPane.showMessageDialog(new JFrame(), "Invalid input data!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(valid) {
                    int clients = parseInt(nrOfClientsField.getText());
                    int queues = parseInt(nrOfQueuesField.getText());
                    int simFinal = parseInt(simFinalField.getText());
                    int minArT = parseInt(minArTimeField.getText());
                    int maxArT = parseInt(maxArTimeField.getText());
                    int minPT = parseInt(minServTimeField.getText());
                    int maxPT = parseInt(maxServTimeField.getText());

                    SelectionPolicy sp;
                    if(st1.isSelected())
                        sp = SelectionPolicy.SHORTEST_TIME;
                    else
                        sp = SelectionPolicy.SHORTEST_QUEUE;

                    SimulationManager sM = new SimulationManager(simFinal, minArT, maxArT, minPT, maxPT, queues, clients, sp);
                    Thread t = new Thread(sM);
                    t.start();
                }
                else JOptionPane.showMessageDialog(new JFrame(), "Invalid input data!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    public static void main(String[] args) {
        new SimulationSetUpFrame().setVisible(true);
    }
}
