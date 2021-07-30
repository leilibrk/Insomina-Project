package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import static java.awt.Color.DARK_GRAY;
import static java.awt.Color.WHITE;

/**
 * This class represents a KeyAndValue
 * @author Leili
 */
public class KeyAndValue{
    private JPanel mainPanel; //the mainPanel
    private HashMap<JTextField,JTextField>hashMap; //a HashMap for the keys and values textFields
    private String keyName; //a keyName
    private String valueName; //a valueName
    private JTextField key; //a textField for key
    private JTextField value; //a textField for value
    private JPanel keysAndValuesPanel; //the keysAndValuesPanel
    private JCheckBox checkBox; //a checkBox for enable or disable
    private JButton trashBtn; //a trash button

    /**
     * Creates a KeyAndValue with the given mainPanel,HashMap, a key name and a value name
     * @param mainPanel the mainPanel
     * @param hashMap the HashMap
     * @param keyName the keyName
     * @param valueName the valueName
     */
    public KeyAndValue(JPanel mainPanel, HashMap<JTextField,JTextField>hashMap,String keyName,String valueName){
        this.mainPanel=mainPanel;
        this.hashMap=hashMap;
        this.keyName=keyName;
        this.valueName=valueName;
        mainPanel.setLayout(new GridLayout(40, 4)); //sets the panel's layout
        keysAndValuesPanel = new JPanel(new FlowLayout()); //a new panel for keys and values
        keysAndValuesPanel.setBackground(DARK_GRAY);//sets the background color
        trashBtn=new JButton (); //a new button for trash button
        checkBox=new JCheckBox (); //a new check box for the check box
        createKeyAndValue(); //create key and value
        createCheckBox(); //create the checkbox
        createTrashIcon(); //create the trash icon
        mainPanel.add (keysAndValuesPanel); //add the keysAndValuesPanel to the mainPanel
    }

    /**
     * Creates the key and the value
     */
    public void createKeyAndValue(){
        key = new JTextField();//a textfield for key
        if(keyName!=null){
            key.setText(keyName);
            trashBtn.setEnabled (true); //make the trash button enabled
        }
        else{
            trashBtn.setEnabled (false); //make the trash button disabled
        }
        key.setOpaque(true);
        key.setBackground(Color.DARK_GRAY);//sets the background color
        key.setForeground(WHITE);//sets the foreground color
        key.setPreferredSize(new Dimension(80, 40));//sets the preferred size
        keysAndValuesPanel.add(key); //add the key text field to the panel
        value = new JTextField();//a new text field for the value
        if(valueName!=null){
            value.setText(valueName);
        }
        value.setOpaque(true);
        value.setBackground(Color.DARK_GRAY);//sets the background color
        value.setForeground(WHITE);//sets the foreground color
        value.setPreferredSize(new Dimension(80, 40));//sets the preferred size
        keysAndValuesPanel.add(value); //add the value text field to the panel
        hashMap.put(key,value); //put the key and value in the hashMap
        //add mouse listener for the key textField
        key.addMouseListener (new MouseAdapter () {
            @Override
            public void mouseClicked (MouseEvent e) {
                KeyAndValue keyAndValue = new KeyAndValue(mainPanel, hashMap, null, null);
                trashBtn.setEnabled (true); //make the trash button enabled
                ResponsePanel.updatePanel (mainPanel);//update the mainPanel
                key.requestFocus ();//request focus
            }
        });
    }

    /**
     * Creates the checkbox
     */
    public void createCheckBox() {
        checkBox.setOpaque (true);
        checkBox.setBackground (Color.DARK_GRAY);//sets the background color
        checkBox.setForeground (WHITE);//sets the foreground color
        checkBox.doClick (); //click on checkbox
        keysAndValuesPanel.add (checkBox); //add it to the panel
        //add action listener for the checkBox
        checkBox.addActionListener (new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!checkBox.isSelected ()) {
                    hashMap.remove (key, value); //remove from the hashMap
                } else {
                    hashMap.put (key, value); //put the key and the value in the hashMap
                }
            }
        });
    }

    /**
     * Creates the trash icon
     */
    public void createTrashIcon() {
        Icon icon = new ImageIcon ("./trash.PNG"); //a new icon for trash button
        trashBtn.setIcon (icon); //set the trash button's icon
        trashBtn.setOpaque (true);
        trashBtn.setBackground (Color.DARK_GRAY);//sets the background color
        trashBtn.setForeground (WHITE);//sets the foreground color
        keysAndValuesPanel.add (trashBtn);//add it to the panel
        //add action listener for the trash button
        trashBtn.addActionListener (new ActionListener () {
            @Override
            public void actionPerformed (ActionEvent actionEvent) {
                hashMap.remove (key, value); //remove from the hashMap
                mainPanel.remove (keysAndValuesPanel); //remove the keysAndValuesPanel from the mainPanel
                ResponsePanel.updatePanel (mainPanel);//update the mainPanel
            }
        });

    }
}

