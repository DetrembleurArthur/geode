/*
 * Created by JFormDesigner on Wed Jan 27 15:03:48 CET 2021
 */

package com.geode.admin;

import com.geode.net.Client;
import com.geode.net.Query;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * @author Arthur Detrembleur
 */
public class Producer extends JFrame {

    private Client client;
    public Producer(Client client) {
        this.client = client;
        initComponents();
    }

    private void button1ActionPerformed(ActionEvent e) {
        client.getHandlerSafe().send(Query.topicNotify("message").pack(textArea1.getText()));
        textArea1.setText("");
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Arthur Detrembleur
        scrollPane1 = new JScrollPane();
        textArea1 = new JTextArea();
        button1 = new JButton();

        //======== this ========
        setTitle("Producer");
        setMinimumSize(new Dimension(800, 700));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(textArea1);
        }
        contentPane.add(scrollPane1);

        //---- button1 ----
        button1.setText("send");
        button1.addActionListener(e -> button1ActionPerformed(e));
        contentPane.add(button1);
        pack();
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Arthur Detrembleur
    private JScrollPane scrollPane1;
    private JTextArea textArea1;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
