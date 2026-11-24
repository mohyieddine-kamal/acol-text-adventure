package jeu.vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class IHMGraphique extends IHMTextuelle implements ActionListener {
    private JFrame frame;
    private JTextArea outputArea;
    private JTextField inputField;
    private BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();

    public IHMGraphique() {
        frame = new JFrame("Jeu d'Aventure");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        inputField = new JTextField();
        inputField.addActionListener(this);

        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputField, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    @Override
    public void afficherMessage(String msg) {
        SwingUtilities.invokeLater(() -> {
            outputArea.append(msg + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        });
    }

    @Override
    public String lireCommande() {
        try {
            return inputQueue.take();
        } catch (InterruptedException e) {
            return "quit";
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = inputField.getText().trim();
        inputField.setText("");
        inputQueue.offer(command);
    }
}