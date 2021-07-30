package com.company;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import static java.awt.Color.*;

/**
 * This class represents a Request list panel
 * It extends JPanel class.
 * @author Leili
 */
public class RequestListPanel extends JPanel{
    private SendRequestPanel panel2; //the SendRequestPanel
    private ResponsePanel panel3; //the ResponsePanel
    private JPanel savedRequestsPanel; //a panel for saved requests
    private ArrayList<JButton> savedRequests; //an ArrayList for saved requests
    private JButton plusButton; //a plus button for new request

    /**
     * Creates a RequestListPanel with the given SendRequestPanel and ResponsePanel
     * @param panel2 the SendRequestPanel
     * @param panel3 the ResponsePanel
     */
    public RequestListPanel(SendRequestPanel panel2,ResponsePanel panel3){
        this.savedRequests=new ArrayList<>(); //a new ArrayList for savedRequests
        this.panel2=panel2;
        panel2.setPanel1(this); //set the panel1 of SendRequestPanel to this panel
        this.panel3=panel3;
        setProperties(); //set the properties
        createButtonPanel(); //create the button panel
        createSavedRequestPanel(); //create the savedRequestPanel
    }

    /**
     * A getter for the plusButton
     * @return plusButton field
     */
    public JButton getPlusButton() {
        return plusButton;
    }

    /**
     * Sets the properties of the panel
     */
    private void setProperties(){
        setPreferredSize(new Dimension(300,600));//sets the preferred size
        setBorder(new LineBorder(Color.lightGray,1)); //sets the border
        setBackground(Color.DARK_GRAY);//sets the background color
        setLayout(new BorderLayout());//sets the layout
    }

    /**
     * Creates the button panel
     */
    private  void createButtonPanel(){
        JPanel buttonPanel=new JPanel(); //a panel for buttons
        buttonPanel.setBackground(Color.DARK_GRAY);//sets the background color
        buttonPanel.setLayout(new GridLayout(1,2));//sets the layout
        buttonPanel.setPreferredSize(new Dimension(300,40));//sets the preferred size
        JTextField Filter=new JTextField("Filter");//filter textfield
        Filter.setEditable(false);
        Filter.setBackground(Color.DARK_GRAY);//sets the background color
        Filter.setForeground(WHITE);//sets the foreground color
        buttonPanel.add(Filter);//add the filter text field to the panel

        plusButton=new JButton("(+)"); //a button for new request
        plusButton.setBackground(DARK_GRAY); //sets the background color
        plusButton.setForeground(WHITE); //sets the foreground color
        buttonPanel.add(plusButton);//add the plusCombo to the panel
        add(buttonPanel,BorderLayout.NORTH);//add the buttonPanel to the north part
        //add action listener for the plus button
        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel2.resetPanel(); //reset SendRequestPanel
                panel3.resetPanel(); //reset ResponsePanel
            }
        });
    }

    /**
     * Creates the saved request panel
     */
    private void createSavedRequestPanel(){
        savedRequestsPanel=new JPanel(); //the saved requests list panel
        savedRequestsPanel.setBackground(Color.DARK_GRAY);//sets the background color
        savedRequestsPanel.setLayout(new GridLayout(80,1));//sets the layout
        savedRequestsPanel.setPreferredSize(new Dimension(40,2000));//sets the preferred size
        JLabel requestLabel=new JLabel("Requests list");//a new label for requests panel
        requestLabel.setForeground(WHITE);//sets the foreground color
        savedRequestsPanel.add(requestLabel);//add the label
        File file=new File("savedRequests"); //a new File for savedRequests
        if(file.exists()){
         ArrayList<Request> requests= Client.listSavedRequests(); //list the saved requests
         for(Request rq:requests){
             //add the request to the request panel
             addRequest(rq.getMethod(),rq.getSavedName());
         }
        }
        add(savedRequestsPanel,BorderLayout.CENTER);// add the savedRequestPanel to the center part
        JScrollPane scrollPane=new JScrollPane(savedRequestsPanel); //a scrollPanel for the savedRequestsPanel
        scrollPane.setOpaque(true); //set the opaque
        scrollPane.setPreferredSize(new Dimension(40,220)); //set the preferred size
        add(scrollPane); //add the scroll pane to the panel
    }

    /**
     * Adds a request to the savedRequestsPanel and saveRequests ArrayList
     * @param method the request's method
     * @param name the request's name
     */
    public void addRequest(String method,String name){
        JButton button=new JButton(); //a button for the saved request
        button.setBackground(WHITE); //set the background color
        button.setForeground(new Color(100,0,200)); //set the foreground color
        button.setText(method+"      "+ name); //set the button text
        savedRequests.add(button); //add the button to the savedRequests ArrayList
        savedRequestsPanel.add(button); //add the button to the savedRequestsPanel
        //add an action listener for the button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for(JButton b:savedRequests){
                    if(actionEvent.getSource().equals(b)){
                        // get the selected request by calling the listSavedRequests method of Client class
                        Request request= Client.listSavedRequests().get(savedRequests.indexOf(b));
                        ShowSavedRequestHandler handler=new ShowSavedRequestHandler(panel3,panel2,request); //a new ShowSavedRequestHandler
                        handler.execute(); //execute the ShowSavedRequestHandler
                    }
                }
            }
        });
        ResponsePanel.updatePanel (savedRequestsPanel);//update the savedRequestPanel
    }

}
