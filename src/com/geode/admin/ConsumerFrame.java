/*
 * Created by JFormDesigner on Wed Jan 27 10:36:01 CET 2021
 */

package com.geode.admin;

import java.awt.*;
import javax.swing.*;

/**
 * @author unknown
 */
public class ConsumerFrame extends JFrame {
    public ConsumerFrame() {
        initComponents();
    }

    public JTextArea textArea;

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Arthur Detrembleur
        scrollPane1 = new JScrollPane();
        textArea1 = new JTextArea();

        //======== this ========
        setTitle("Consumer");
        setMinimumSize(new Dimension(800, 700));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

        //======== scrollPane1 ========
        {

            //---- textArea1 ----
            textArea1.setEditable(false);
            scrollPane1.setViewportView(textArea1);
        }
        contentPane.add(scrollPane1);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Arthur Detrembleur
    private JScrollPane scrollPane1;
    public JTextArea textArea1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
