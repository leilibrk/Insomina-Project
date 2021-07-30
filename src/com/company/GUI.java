package com.company;


import javax.swing.*;

/**
 * This class represents a GUI for the Insomnia program
 * It extends the JFrame class
 * @author Leili
 */
public class GUI extends JFrame {

    /**
     * Creates a GUI for the Insomnia application
     */
    public GUI(){
        super("Insomnia"); //set the frame's title
        setSize(2000,600); //set the frame's size
        setLayout(new BoxLayout(getContentPane(),BoxLayout.X_AXIS)); //set the frame's layout
        setLocationRelativeTo(null); //set the location  relative to null
        ResponsePanel responsePanel=new ResponsePanel(); //create a response panel
        SendRequestPanel sendRequestPanel=new SendRequestPanel(null,responsePanel); //create a send request panel
        RequestListPanel requestListPanel=new RequestListPanel(sendRequestPanel,responsePanel); //create a request list panel
        //add the panels to the  frame
        add(requestListPanel);
        add(sendRequestPanel);
        add(responsePanel);
        setJMenuBar(new MenuBar(this,requestListPanel,sendRequestPanel));//set the frame's JMenu bar to menuBar
    }

    /**
     * Shows the gui.
     * It makes the frame visible
     */
    public void showGUI(){
        setVisible(true);
    }

}
