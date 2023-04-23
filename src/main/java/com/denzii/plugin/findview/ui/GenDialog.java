package com.dengzii.plugin.findview.ui;

import com.dengzii.plugin.findview.tools.ui.XDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GenDialog extends JDialog {
    JPanel contentPane;
    JButton buttonOK;
    JButton buttonCancel;
    JTable tableFieldMapping;
    JComboBox<String> comboBoxLayoutRes;
    JRadioButton startRadioButton;
    JRadioButton endRadioButton;
    JRadioButton cursorRadioButton;
    JRadioButton mCamelCaseRadioButton;
    JRadioButton camelCaseRadioButton;
    JRadioButton under_lineRadioButton;

    public GenDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setPreferredSize(new Dimension(800, 600));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    protected void onOK() {
        // add your code here
        dispose();
    }

    protected void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        GenDialog dialog = new GenDialog();
        dialog.pack();
        dialog.setVisible(true);
        //System.exit(0);
    }
}
