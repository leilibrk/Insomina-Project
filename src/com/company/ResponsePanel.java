package com.company;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import static java.awt.Color.*;

/**
 * This class represents a ResponsePanel
 * It extends JPanel class
 */
public class ResponsePanel extends JPanel {
    private JPanel labels; //a panel for labels
    private JLabel statusCodeLabel; //a label for status code
    private JLabel timeLabel; //a label for duration time
    private JLabel sizeLabel; //a label for response size
    private JTextArea rawTxt; //a textArea for raw response
    private JTextArea jsonTxt; //a textArea for json response
    private JPanel previewImagePanel; //a panel for image response
    private JPanel headerPanel; //a panel for response's headers
    private JButton copyButton; //a button for copy
    private HashMap<String,String>headersMap;//a HashMap for headers

    /**
     * Creates a Response Panel
     */
    public ResponsePanel(){
        setProperties(); //set the properties of the panel
        labels=new JPanel(new FlowLayout()); //a new panel for the labels
        add(labels);//add the labels panel to  panel3
        createResponseBodyPanel(); //create the response body panel
        createNorthPanel(); //create the north panel
        headersMap=new HashMap<> (); //a new HashMap
    }

    /**
     * Resets the panel
     */
    public void resetPanel(){
        statusCodeLabel.setText(""); //resets the status code label
        statusCodeLabel.setBackground(GREEN);//sets the background color
        timeLabel.setText(""); //resets the time label
        sizeLabel.setText(""); //resets the size label
        rawTxt.setText(""); //resets the raw textArea
        jsonTxt.setText(""); //resets the json textArea
        previewImagePanel.removeAll(); //remove all the components from the previewImagePanel
        headerPanel.removeAll(); //remove all the keys and values from the headerPanel
        copyButton.setEnabled(false); //make the copy button not enabled
    }

    /**
     * Sets the panel's properties
     */
    private void setProperties(){
        setBorder(new LineBorder(Color.lightGray,1));//set the border
        setPreferredSize(new Dimension(500,600));//set the preferred size
        BoxLayout b=new BoxLayout(this,BoxLayout.Y_AXIS);
        setLayout(b);//set the layout of the third panel
    }

    /**
     * Creates the north panel
     */
    private void createNorthPanel(){
        labels.setBackground(Color.DARK_GRAY);//set the background color
        this.statusCodeLabel=new JLabel(); //a new label for code
        this.statusCodeLabel.setOpaque(true);
        this.statusCodeLabel.setPreferredSize(new Dimension(90,30));//set the preferred size
        this.statusCodeLabel.setBackground(Color.GREEN);
        labels.add(statusCodeLabel);//add it to the labels panel
        timeLabel=new JLabel();//a new label for time
        timeLabel.setBackground(Color.GRAY);//set the background color
        timeLabel.setOpaque(true);
        timeLabel.setPreferredSize(new Dimension(90,30));//set the preferred size
        labels.add(timeLabel);//add it to the labels panel
        sizeLabel=new JLabel();//a new label for size
        sizeLabel.setBackground(Color.YELLOW);//set the background color
        sizeLabel.setOpaque(true);
        sizeLabel.setPreferredSize(new Dimension(90,30));//set the preferred size
        labels.add(sizeLabel);//add it to the labels panel
    }

    /**
     * Creates the response body panel
     */
    private void createResponseBodyPanel(){
        JPanel panel=new JPanel();//a new panel for tabbedPane
        panel.setPreferredSize(new Dimension(400,600));//set the preferred size
        panel.setBackground(Color.DARK_GRAY);//set the background color
        JTabbedPane tabbedPane=new JTabbedPane();//a new tabbedPane
        JPanel p1=new JPanel(); //a new panel for the first tab
        p1.setPreferredSize(new Dimension(400,440));//set the preferred size
        createMessageBody(p1); //create the message body panel(tab)
        JPanel p2=new JPanel();//a new panel for the second tab
        p2.setPreferredSize(new Dimension(400,440));//set the preferred size
        createHeaderPanel(p2);// create the header panel(tab)
        tabbedPane.add("Message Body",p1);//add a new tab
        tabbedPane.add("Header",p2);//add a new tab
        panel.add(tabbedPane);//add the tabbedPane to the panel
        add(panel);//add the panel to panel3
    }

    /**
     * Creates the message body
     * @param p1 the messageBody panel(the response panel)
     */
    private void createMessageBody(JPanel p1){
        p1.setLayout(new BorderLayout()); //sets the layout
        String[] options={"Raw","Preview","JSON"}; //a string of options for the combo box
        JComboBox comboBox=new JComboBox(options); //a new combobox
        comboBox.setOpaque(true);
        comboBox.setBackground(Color.DARK_GRAY);//sets the background color
        comboBox.setForeground(WHITE);//sets the foreground color
        p1.add(comboBox,BorderLayout.NORTH); //add the combo box to the p1 panel

        JPanel rawPanel=new JPanel(); //a new panel for rawPanel
        rawPanel.setBackground(Color.DARK_GRAY);//sets the background color
        rawTxt=new JTextArea();//a text area for raw panel
        rawTxt.setBackground(Color.DARK_GRAY);//sets the background color
        rawTxt.setForeground(WHITE);//sets the foreground color
        rawTxt.setPreferredSize(new Dimension(20000,5000));//sets the preferred size
        rawTxt.setBorder(new EmptyBorder(5,8,5,1));
        rawTxt.setEditable(false);
        rawPanel.add(rawTxt);//add the raw text to the raw panel
        JScrollPane scrollPane=new JScrollPane(rawTxt);//a new scrollPane for the rawTxt text area
        scrollPane.setOpaque(true);
        scrollPane.setPreferredSize(new Dimension(400,410));//sets the preferred size
        rawPanel.add(scrollPane);//add the scrollPane to the rawPanel

        previewImagePanel=new JPanel(); //a new panel for preview panel
        previewImagePanel.setBackground(Color.DARK_GRAY);//sets the background color


        JPanel jsonPanel=new JPanel(); //a new panel for JSON panel
        jsonPanel.setBackground(Color.DARK_GRAY);//sets the background color
        jsonTxt=new JTextArea();//a text area for the JSON panel
        jsonTxt.setEditable(false);
        jsonTxt.setPreferredSize(new Dimension(700,3000));//sets the preferred size
        jsonTxt.setOpaque(true);
        jsonTxt.setBackground(DARK_GRAY);//sets the background color
        jsonPanel.add(jsonTxt);//add the json text to the json panel
        JScrollPane scrollPanel3=new JScrollPane(jsonTxt);//a scrollPane for the json text
        scrollPanel3.setPreferredSize(new Dimension(400,410));//sets the preferred size
        jsonPanel.add(scrollPanel3);//add the scrollPane to the jsonPanel

        p1.add(rawPanel,BorderLayout.CENTER);//add the rawPanel to p1 panel
        //add action listener for the comboBox
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String s=(String) comboBox.getSelectedItem();
                switch (s) {
                    case "Raw":
                        previewImagePanel.setVisible(false);//make the previewPanel not visible

                        jsonPanel.setVisible(false);//make the jsonPanel not visible

                        rawPanel.setVisible(true);//make the rawPanel visible

                        break;
                    case "Preview":
                        jsonPanel.setVisible(false);//make the jsonPanel not visible

                        rawPanel.setVisible(false);//make the rawPanel not visible

                        previewImagePanel.setVisible(true);//make the previewPanel visible

                        p1.add(previewImagePanel, BorderLayout.CENTER);//add the previewPanel to p1 panel

                        break;
                    case "JSON":
                        previewImagePanel.setVisible(false);//make the previewPanel not visible

                        rawPanel.setVisible(false);//make the rawPanel not visible

                        jsonPanel.setVisible(true);//make the jsonPanel visible

                        p1.add(jsonPanel, BorderLayout.CENTER);//add the jsonPanel to p1 panel

                        break;
                }

            }
        });
    }

    /**
     * Creates the header panel
     * @param p2 a panel for the headers
     */
    private void createHeaderPanel(JPanel p2){
        JPanel mainPanel=new JPanel(); //a new panel for the key and values
        mainPanel.setPreferredSize(new Dimension(400,2000));//sets the preferred size
        mainPanel.setBackground(Color.DARK_GRAY);//sets the background color
        mainPanel.setForeground(WHITE);//sets the foreground color
        headerPanel=new JPanel(new GridLayout(40,2)); //a new panel for a key and a value
        headerPanel.setBackground(DARK_GRAY);//sets the background color

        mainPanel.add(headerPanel);//add the keyAndValue to the mainPanel

        JPanel southPanel=new JPanel();//a new panel for south part
        southPanel.setBackground(DARK_GRAY);//sets the background color
        copyButton=new JButton("Copy to Clipboard");//a new button for copy key and values to clipboard
        copyButton.setEnabled(false);

        JTextArea emptyTxt=new JTextArea();//an empty text area
        emptyTxt.setEditable(false);
        emptyTxt.setBorder(new LineBorder(DARK_GRAY));//sets the border
        emptyTxt.setPreferredSize(new Dimension(200,30));//sets the preferred size
        emptyTxt.setBackground(DARK_GRAY);//sets the background color
        southPanel.add(emptyTxt);//add the empty text field
        southPanel.add(copyButton);//add the copy button
        mainPanel.add(southPanel);//add the panel to the main panel

        p2.add(mainPanel); //add the mainPanel to the p2 panel
        JScrollPane scrollPane=new JScrollPane(mainPanel); //a scrollPane for the mainPanel
        scrollPane.setPreferredSize(new Dimension(390,430));//sets the preferred size
        p2.add(scrollPane); //add the scrollPane to the p2 panel
    }

    /**
     * Sets the labels of the north panel
     * @param statusCode a String represents the status code and message
     * @param time a String represents the duration time
     * @param size a String represents the response size
     */
    public void setLabels(String statusCode,String time,String size){
        if(statusCode.equals("ERROR")){
            statusCodeLabel.setBackground(RED);//set the background color to red
        }
        else if(Integer.parseInt(statusCode.split(" ")[0])/100==3){
            statusCodeLabel.setBackground(new Color(200,0,200)); //set the background color to pink
        }
        else if(Integer.parseInt(statusCode.split(" ")[0])/100==5){
            statusCodeLabel.setBackground(RED); //set the background color to red
        }
        else if(Integer.parseInt(statusCode.split(" ")[0])/100==4){
            statusCodeLabel.setBackground(ORANGE); //set the background color to orange
        }
        else if(Integer.parseInt(statusCode.split(" ")[0])/100==2){
            statusCodeLabel.setBackground(GREEN); //set the background color to green
        }
        statusCodeLabel.setText(statusCode); //set the status code label
        timeLabel.setText(time); //set the time label
        sizeLabel.setText(size); //set the size label
    }

    /**
     * Sets the response headers with the given HashMap
     * @param headers a HashMap of Strings represents the response headers
     */
    public void setResponseHeaders(HashMap<String,String>headers){
        headersMap=new HashMap<> ();//a new HashMap for headers
        headersMap.putAll (headers); //put all the given headers in the headersMap
        headerPanel.removeAll(); //remove all the components
        int rows=headers.keySet().size(); //the number of headers
        headerPanel.setLayout(new GridLayout(rows,2)); //sets the layout
        for(String s:headers.keySet()){
            JTextArea key=new JTextArea("\n    "+s);//a textfield for key
            key.setOpaque(true);
            key.setBackground(Color.DARK_GRAY);//sets the background color
            key.setForeground(WHITE);//sets the foreground color
            key.setBorder(new LineBorder(WHITE,1));
            key.setEditable(false);
            key.setPreferredSize(new Dimension(160,60));//sets the preferred size
            headerPanel.add(key); //add the key text field to the panel

            JTextArea value = new JTextArea("\n   "+headers.get(s));//a new text field for the value
            if(value.getText().length()>=20){
                char[] chars=value.getText().toCharArray();
                String newValue="   ";
                for(int i=0;i<20;i++){
                    newValue+=chars[i];
                }
                newValue+="\n   ";
                for(int i=20;i<chars.length;i++){
                    newValue+=chars[i];
                }
                value.setText(newValue);
            }
            value.setOpaque(true);
            value.setBackground(Color.DARK_GRAY);//sets the background color
            value.setForeground(WHITE);//sets the foreground color
            value.setPreferredSize(new Dimension(160,60));//sets the preferred size
            value.setEditable(false);
            value.setBorder(new LineBorder(WHITE,1));
            headerPanel.add(value); //add the value text field to the panel
        }
        copyButton.setEnabled(true);
        copyButton.setOpaque(true);
        copyButton.setBackground(Color.DARK_GRAY);//sets the background color
        copyButton.setForeground(WHITE);//sets the foreground color
        //add action listener for the copy button
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
               String data="";
                for(String s:headersMap.keySet()){
                    data+=s;
                    data+=" : ";
                    data+=headersMap.get(s);
                    data+="\n";
                }
                StringSelection stringSelection=new StringSelection(data); //a new String Selection
                Clipboard cb=Toolkit.getDefaultToolkit().getSystemClipboard(); //a new Clipboard
                cb.setContents(stringSelection,stringSelection); //set the contents of the clipBoard
            }
        });
    }

    /**
     * Sets the json textArea to the given string
     * @param text a String represents the json response
     */
    public void setJsonTxt(String text){
        this.jsonTxt.setBackground(DARK_GRAY); //sets the background color
        this.jsonTxt.setForeground(YELLOW); //sets the foreground color
        char[] chars=text.toCharArray(); //split the text to a char array
        String newText="";
        for (char c:chars){
            newText+=c;
            if(c==','){
                newText+="\n";
            }
        }
        this.jsonTxt.setText(newText); //sets the json text
    }

    /**
     * Sets the raw textArea to the given String
     * @param text a String represents the raw response
     */
    public void setRawTxt(String text){
        this.rawTxt.setText(text);
    }

    /**
     * Sets the preview image with the given label
     * @param previewImage a label represents the preview image
     */
    public void setPreviewImage(JLabel previewImage){
        this.previewImagePanel.removeAll(); //remove all the components
        this.previewImagePanel.add(previewImage); //add the label tot the panel
        updatePanel (previewImagePanel); //update panel
    }

    /**
     * Updates a panel by make it invisible and then make it visible
     * @param panel the panel
     */
    public static void updatePanel(JPanel panel){
        panel.setVisible (false); //make the panel not visible
        panel.setVisible (true); //make the panel visible
    }
}
