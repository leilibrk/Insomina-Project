package com.company;


import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.HashMap;
import static java.awt.Color.*;

/**
 * This class represents a SendRequestPanel
 * It extends JPanel class
 */
public class SendRequestPanel extends JPanel {
    private RequestListPanel panel1; //the RequestListPanel
    private ResponsePanel panel3; //the ResponsePanel
    private JTextField http; //a textField for HTTP
    private String method; //a String for the request's method
    private JComboBox methods; //a ComboBox for the methods
    private JComboBox body; //a ComboBox for the body
    private JButton saveButton; //a button for saving the request
    private JButton sendButton; //a button for sending the request
    private HashMap<JTextField,JTextField> reqPropertiesHashMap; //a HashMap for headers keys and values TextFields
    private HashMap<JTextField,JTextField> formDataHashMap; //a HashMap for form data keys and values TextFields
    private JTextArea jsonData; //a TextArea for json body
    private JTextArea binaryData; //a TextArea for binary body
    private HashMap<JTextField, JTextField> queryHashMap; //a HashMap for query keys and values TextFields
    private JPanel reqPropertyPanel; //a panel for request properties(request headers)
    private JPanel formDataPanel; //a panel for form data
    private boolean followRedirect=false; //a boolean for follow redirect
    private JTextField queryURL; //a TextField for Query URL
    private JPanel queryKeyAndValuePanel; //a panel for query key and value panel
    private HashMap<JTextField, JTextField> urlencodedHashMap; //a HashMap for urlencoded keys and values TextFields
    private JPanel urlencodedPanel; //a panel for urlencoded data
    private ButtonHandler handler=new ButtonHandler(); //a ButtonHandler
    /**
     * Creates a SendRequestPanel with the given ResponsePanel and RequestListPanel
     * @param panel3 the ResponsePanel
     * @param panel1 the RequestListPanel
     */
    public SendRequestPanel(RequestListPanel panel1, ResponsePanel panel3) {
        reqPropertiesHashMap=new HashMap<>(); //a new HashMap for request properties(request headers)
        formDataHashMap=new HashMap<>(); //a new HashMap for formDataHashMap
        queryHashMap=new HashMap<>(); //a new HashMap for queryHashMap
        urlencodedHashMap=new HashMap<>(); //a new HashMap for urlencodedHashMap
        method="GET"; //set the default method to GET
        this.panel3 = panel3;
        this.panel1=panel1;
        setProperties(); //set the properties
        createNorthPanel(); //create the SendRequestPanel's north panel
        createCenterPanel(); //create the SendRequestPanel's center panel
    }

    /**
     * Sets the follow redirect status
     * @param followRedirect a boolean represents the followRedirect's status
     */
    public void setFollowRedirect(boolean followRedirect) {
        this.followRedirect = followRedirect;
    }
    /**
     * Sets the query URL
     * @param queryURL a String represents the query url
     */
    public void setQueryURL(String queryURL) {
        this.queryURL.setText(queryURL);
    }
    /**
     * A setter for the panel1
     * @param panel1 a RequestListPanel
     */
    public void setPanel1(RequestListPanel panel1) {
        this.panel1 = panel1;
    }

    /**
     * A setter for the http's text
     * @param URL a String represents the URL
     */
    public void setURL(String URL){
        http.setText(URL);
    }

    /**
     * Sets the selected item of the methods comboBox to the given method
     * @param method a String represents the method
     */
    public void setMethod(String method) {
        methods.setSelectedItem(method);
    }

    /**
     * A getter for the queryKeyAndValuePanel
     * @return queryKeyAndValuePanel field
     */
    public JPanel getQueryKeyAndValuePanel() {
        return queryKeyAndValuePanel;
    }

    /**
     * A getter for the URL
     * @return http's text field
     */
    public String getURL(){
        return http.getText();
    }

    /**
     * A getter for the method
     * @return method field
     */
    public String getMethod() {
        return method;
    }

    /**
     * A getter for the reqPropertyPanel
     * @return reqPropertyPanel
     */
    public JPanel getReqPropertyPanel() {
        return reqPropertyPanel;
    }

    /**
     * A getter for the reqPropertiesHashMap
     * @return reqPropertiesHashMap field
     */
    public HashMap<JTextField, JTextField> getReqPropertiesHashMap() {
        return reqPropertiesHashMap;
    }

    /**
     * A getter for the queryHashMap
     * @return queryHashMap field
     */
    public HashMap<JTextField, JTextField> getQueryHashMap() {
        return queryHashMap;
    }

    /**
     * Sets the form data
     * It is called when the request body data type is multipart form Data
     * @param data a String
     */
    public void setFormData(String data){
        body.setSelectedItem("Multipart Form Data"); //set the selected item of the body combo box
        formDataPanel.removeAll(); //remove all the components
        String[]s=data.split("&");
        for(String key:s){
            String[] keyValue=key.split("=");
            //create a key and value for each form data
            KeyAndValue keyAndValue=new KeyAndValue(formDataPanel,formDataHashMap,keyValue[0],keyValue[1]);
        }
        //create an empty key and value
        KeyAndValue keyAndValue=new KeyAndValue(formDataPanel,formDataHashMap,null,null);
        ResponsePanel.updatePanel (formDataPanel);//update the formDataPanel
    }

    /**
     * Sets the json data
     * It is called when the request body data type is json
     * @param data a String
     */
    public void setJsonData(String data){
        body.setSelectedItem("JSON"); //set the selected item of the body combo box
        jsonData.setText(data); //set the data of the json TextArea
    }

    /**
     * Sets the binary data
     * It is called when the request body data type is binary
     * @param data a String
     */
    public void setBinaryData(String data){
        body.setSelectedItem("Binary Data"); //set the selected item of the body combo box
        binaryData.setText(data);//set the data of the binaryData TextArea
    }

    /**
     * Sets the URL encoded data
     * It is called when the request body data type is form url encoded
     * @param data a String
     */
    public void setUrlencodedData(String data){
        body.setSelectedItem("Form URL Encoded");//set the selected item of the body combo box
        urlencodedPanel.removeAll();//remove all the components
        String[]s=data.split("&");
        for(String key:s){
            String[] keyValue=key.split("=");
            //create a key and value for each form url encoded data
            KeyAndValue keyAndValue=new KeyAndValue(urlencodedPanel,urlencodedHashMap,keyValue[0],keyValue[1]);
        }
        //create an empty key and value
        KeyAndValue keyAndValue=new KeyAndValue(urlencodedPanel,urlencodedHashMap,null,null);
        ResponsePanel.updatePanel (urlencodedPanel);//update the urlencodedPanel
    }

    /**
     * Resets the request body panel
     */
    public void bodyReset(){
        formDataHashMap=new HashMap<>(); //a new HashMap for the formDataHashMap
        urlencodedHashMap=new HashMap<>(); //a new HashMap for the urlencodedHashMap
        formDataPanel.removeAll(); //remove all the components
        urlencodedPanel.removeAll(); //remove all the components
        binaryData.setText("No File Selected"); //set the binaryData's textField
        jsonData.setText(""); //set the jsonData's textField
        body.setSelectedItem("Body"); //set the selected item of the body comboBox to Body
        //create empty KeyAndValue for the formDataPanel and urlencodedPanel
        KeyAndValue keyAndValue=new KeyAndValue(formDataPanel,formDataHashMap,null,null);
        KeyAndValue keyAndValue1=new KeyAndValue(urlencodedPanel,urlencodedHashMap,null,null);
        ResponsePanel.updatePanel (formDataPanel);//update the formDataPanel
        ResponsePanel.updatePanel (urlencodedPanel);//update the urlencodedPanel
    }

    /**
     * Resets the panel
     */
    public void resetPanel(){
        bodyReset(); //resets the body
        setQueryURL(""); //reset the query url
        setURL(""); //reset the url
        setMethod("GET"); //reset the method
        queryKeyAndValuePanel.removeAll(); //remove all the components
        queryHashMap=new HashMap<>(); //a new HashMap
        reqPropertiesHashMap=new HashMap<>(); //a new HashMap
        reqPropertyPanel.removeAll(); //remove all the components
        //create empty KeyAndValue for queryPanel and reqPropertyPanel
        KeyAndValue keyAndValue=new KeyAndValue(queryKeyAndValuePanel,queryHashMap,null,null);
        KeyAndValue keyAndValue1=new KeyAndValue(reqPropertyPanel,reqPropertiesHashMap,null,null);
        ResponsePanel.updatePanel (queryKeyAndValuePanel);//update the query panel
        ResponsePanel.updatePanel (reqPropertyPanel);//update the request property panel(header panel)
    }

    /**
     * Set the panel's properties
     */
    private void setProperties() {
        setBorder(new LineBorder(Color.lightGray, 1));//sets the border
        setBackground(Color.DARK_GRAY);//sets the background color
        setPreferredSize(new Dimension(700, 600));//sets the preferred size
        setLayout(new BorderLayout());//sets the layout
    }

    /**
     * Creates the north panel of the SendRequestPanel
     */
    private void createNorthPanel() {
        JPanel northPanel = new JPanel(); //create a panel
        BoxLayout b = new BoxLayout(northPanel, BoxLayout.X_AXIS);
        northPanel.setLayout(b);//sets the layout
        String[] s = {"GET", "DELETE", "POST", "PUT", "PATCH"};
        methods = new JComboBox(s); //create a combo box with the given items
        northPanel.add(methods);//add the combobox to the northPanel
        //add action listener for the methods combo box
        methods.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                method= (String)methods.getSelectedItem();
            }
        });

        http = new JTextField(); //create a text field for url address
        http.setPreferredSize(new Dimension(90, 30));//sets the preferred size
        northPanel.add(http); //add the text field to the northPanel

        sendButton = new JButton("Send"); //create a send button
        sendButton.setOpaque(true);
        sendButton.setBackground(WHITE);//sets the background color
        sendButton.addActionListener(handler);//add action listener for the send button
        northPanel.add(sendButton);//add the send button to the northPanel

        saveButton = new JButton("Save"); //create a save button
        saveButton.setOpaque(true);
        saveButton.setBackground(WHITE);//sets the background color
        saveButton.addActionListener(handler);//add action listener for the save button
        northPanel.add(saveButton);//add it to the northPanel

        add(northPanel, BorderLayout.NORTH); //add the northPanel to the north part of the panel2

    }

    /**
     * Creates the center panel of the SendRequestPanel
     */
    public void createCenterPanel() {
        JPanel centerPanel = new JPanel(); //a new panel for the tabbed pane
        centerPanel.setBackground(Color.DARK_GRAY);//set the background to dark gray color
        JTabbedPane tabbedPane = new JTabbedPane(); //a new tabbed pane
        JPanel p1 = new JPanel(); //a new panel for the first tab
        p1.setBackground(Color.DARK_GRAY);//set the background to dark gray color
        JPanel p2 = new JPanel();//a new panel for the second tab
        p2.setBackground(Color.DARK_GRAY);//set the background to dark gray color
        JPanel p3 = new JPanel();//a new panel for the third tab
        p3.setBackground(Color.DARK_GRAY);//set the background to dark gray color
        JPanel p4= new JPanel();//a new panel for the forth tab

        reqPropertyPanel = new JPanel(); //a new panel for the request properties key and values

        createBody(p1); //create the body tab
        createAuth(p2); //create the auth tab
        createQuery(p3); //create the query tab
        createKeysAndValues (p4,reqPropertyPanel,reqPropertiesHashMap); //create the header tab
        //add the tabs to the tabbed pane
        tabbedPane.add("Body", p1);
        tabbedPane.add("Auth(Bearer)", p2);
        tabbedPane.add("Query", p3);
        tabbedPane.add("Header",p4);

        centerPanel.add(tabbedPane); //add the tabbedPane to the centerPanel
        add(centerPanel, BorderLayout.CENTER); //add the centerPanel to panel2
    }
    /**
     * Creates the request body panel
     * @param bodyPanel a Panel for the body
     */
    private void createBody(JPanel bodyPanel) {
        String[] options = {"Body","Multipart Form Data", "JSON", "Binary Data","Form URL Encoded"}; //a string of options of the comboBox
        body = new JComboBox(options); //create a new comboBox with the given options
        body.setBackground(Color.DARK_GRAY);//sets the background color
        body.setForeground(WHITE);//sets the foreground color
        BoxLayout b = new BoxLayout(bodyPanel, BoxLayout.Y_AXIS);
        bodyPanel.setLayout(b);//set the layout of the bodyPanel
        bodyPanel.setPreferredSize(new Dimension(400, 400));//sets the preferred size
        bodyPanel.add(body);//add the comboBox to the panel
        JLabel label = new JLabel(" Select a body type"); //a new label
        label.setOpaque(true);
        label.setBackground(Color.DARK_GRAY);//sets the background color
        label.setForeground(WHITE);//sets the foreground color
        label.setPreferredSize(new Dimension(300, 380));//sets the preferred size
        bodyPanel.add(label);//add the label to the bodyPanel
        urlencodedPanel = new JPanel(); //a new panel for the urlencoded data key and values
        formDataPanel = new JPanel(); //a new panel for the multipart form data key and values

        JPanel formData = new JPanel(); //a new panel for Form Data tab
        formData.setPreferredSize(new Dimension(400, 900));//sets the preferred size
        createKeysAndValues (formData,formDataPanel,formDataHashMap); //create the keys and values
        formData.setVisible(false); //make it not visible
        bodyPanel.add(formData); //add it to the bodyPanel


        JPanel json = new JPanel(); //a new panel for the JSON tab
        createJson(json); //create the json tab
        json.setVisible(false); //make it not visible
        bodyPanel.add(json); //add it to the bodyPanel

        JPanel binaryData = new JPanel(); //a new panel for the Binary Data tab
        createBinaryData(binaryData); //create the Binary Data tab
        binaryData.setVisible(false); //make it not visible
        bodyPanel.add(binaryData); //add it to the bodyPanel

        JPanel urlData=new JPanel(); //a new panel for urlencoded data
        urlData.setPreferredSize(new Dimension(400, 900));//sets the preferred size
        createKeysAndValues (urlData,urlencodedPanel,urlencodedHashMap); //create the keys and values
        urlData.setVisible(false); //make it not visible
        bodyPanel.add(urlData); //add it to the bodyPanel

        body.addActionListener(actionEvent -> { //add the action listener for the body combo box
            String s = (String) body.getSelectedItem();
            if (s.equals("Multipart Form Data")) {
                //make the other panels not visible
                label.setVisible(false);
                json.setVisible(false);
                binaryData.setVisible(false);
                urlData.setVisible(false);
                formData.setVisible(true); //make the formData panel visible
            } else if (s.equals("JSON")) {
                //make the other panels not visible
                label.setVisible(false);
                formData.setVisible(false);
                binaryData.setVisible(false);
                urlData.setVisible(false);
                json.setVisible(true); //make the json panel visible
            } else if (s.equals("Binary Data")) {
                //make the other panels not visible
                label.setVisible(false);
                formData.setVisible(false);
                json.setVisible(false);
                urlData.setVisible(false);
                binaryData.setVisible(true); //make the binaryData panel visible

            }
            else if(s.equals("Body")){
                //make the other panels not visible
                formData.setVisible(false);
                json.setVisible(false);
                urlData.setVisible(false);
                binaryData.setVisible(false);
                label.setVisible(true);//make the label visible
            }
            else if(s.equals("Form URL Encoded")){
                //make the other panels not visible
                label.setVisible(false);
                formData.setVisible(false);
                json.setVisible(false);
                binaryData.setVisible(false);
                urlData.setVisible(true);//make the urlData panel visible
            }

        });

    }

    /**
     * Creates keys and values for formData,urlencodedData and request properties
     * @param mainPanel the main panel for add the keysValuesPanel to it
     * @param keysValuesPanel a panel for keys and values
     * @param hashMap a hashMap for keys and values TextFields
     */
    private void createKeysAndValues(JPanel mainPanel,JPanel keysValuesPanel,HashMap<JTextField,JTextField>hashMap){
        mainPanel.setBackground (Color.DARK_GRAY); //set the background color
        keysValuesPanel.setPreferredSize (new Dimension (400,2000)); //set the preferred size
        keysValuesPanel.setLayout(new GridBagLayout());//set the layout
        keysValuesPanel.setBackground(Color.DARK_GRAY); //set the background color
        keysValuesPanel.setForeground(WHITE); //set the foreground color
        //create an empty KeyAndValue
        KeyAndValue keyAndValue=new KeyAndValue (keysValuesPanel,hashMap,null,null);
        mainPanel.add (keysValuesPanel); //add the keyValues panel to the mainPanel
        JScrollPane scrollPane = new JScrollPane(keysValuesPanel); //a scrollPane for the keysValuesPanel
        scrollPane.setPreferredSize(new Dimension(410, 440));//sets the preferred size
        mainPanel.add(scrollPane); //add the scrollPane to the mainPanel

    }

    /**
     * Creates the json data panel( the json body )
     * @param json the jsonPanel
     */
    private void createJson(JPanel json) {
        json.setBackground(Color.DARK_GRAY);//sets the background color
        jsonData = new JTextArea(); //a text area
        jsonData.setOpaque(true);
        jsonData.setBackground(Color.DARK_GRAY);//sets the background color
        jsonData.setForeground(WHITE);//sets the foreground color
        jsonData.setPreferredSize(new Dimension(700, 2000));//sets the preferred size
        json.add(jsonData); //add the text area to the json panel
        JScrollPane scrollPane = new JScrollPane(jsonData); //a scrollPane for the text area
        scrollPane.setPreferredSize(new Dimension(420, 420));//sets the preferred size
        json.add(scrollPane); //add the scroll pane to the panel

    }

    /**
     * Creates the binary data panel (the binary request body)
     * @param binary the binary panel
     */
    private void createBinaryData(JPanel binary) {
        binary.setBackground(DARK_GRAY);//sets the background color
        JPanel mainPanel = new JPanel(); //a new panel for the labels and buttons
        mainPanel.setLayout(new GridLayout(2, 2, 5, 5));//set the layout
        mainPanel.setBackground(Color.DARK_GRAY);//sets the background color
        JLabel selected = new JLabel("SELECTED FILE"); //a new label
        selected.setOpaque(true);
        selected.setForeground(WHITE);//sets the foreground color
        selected.setBackground(Color.DARK_GRAY);//sets the background color
        mainPanel.add(selected); //add the label to the mainPanel

        binaryData = new JTextArea("No File Selected"); //a new text area for the file address
        binaryData.setOpaque(true);
        binaryData.setBackground(Color.DARK_GRAY);//sets the background color
        binaryData.setForeground(WHITE);//sets the foreground color
        binaryData.setEditable(false);
        binaryData.setPreferredSize(new Dimension(400, 40));//sets the preferred size
        binaryData.setBorder(new LineBorder(Color.lightGray, 1));
        mainPanel.add(binaryData); //add the text area to the mainPanel

        JScrollPane scrollPane = new JScrollPane(binaryData); //a new scrollPane for the file address text area
        scrollPane.setPreferredSize(new Dimension(200, 40));//sets the preferred size
        mainPanel.add(scrollPane); //add the scrollPane to the mainPanel

        JButton reset = new JButton("Reset File"); //a new Button for reset the selected file
        reset.setOpaque(true);
        reset.setForeground(WHITE);//sets the foreground color
        reset.setBackground(Color.DARK_GRAY);//sets the background color
        reset.addActionListener(new ActionListener() { //add action listener for the reset Button
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                binaryData.setText("No File Selected"); //reset the file address
            }
        });
        JButton choose = new JButton("Choose File"); //a new Button for choosing the file
        choose.setOpaque(true);
        choose.setBackground(Color.DARK_GRAY);//sets the background color
        choose.setForeground(WHITE);//sets the foreground color
        choose.addActionListener(new ActionListener() { //add action listener for the choose Button
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame fileFrame = new JFrame(); //a new frame for choosing the file
                fileFrame.setBackground(Color.DARK_GRAY);//sets the background color

                JFileChooser fileChooser = new JFileChooser(); //a new fileChooser
                fileFrame.setResizable(true);
                fileFrame.setSize(400, 400);//sets the preferred size
                fileFrame.setLocationRelativeTo(null);
                fileFrame.add(fileChooser); //add the fileChooser to the fileFrame
                fileFrame.setVisible(true); //make the fileFrame visible
                fileChooser.addActionListener(new ActionListener() { //add action listener for file chooser
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        binaryData.setText(fileChooser.getSelectedFile().getAbsolutePath()); //set the fileAddress text area to the file absolute path
                        fileFrame.setVisible(false); //set the frame not visible
                    }
                });
            }
        });
        mainPanel.add(reset);//add the reset button to the mainPanel
        mainPanel.add(choose);//add the choose button to the mainPanel

        JPanel emptyPanel = new JPanel(); //a new emptyPanel
        emptyPanel.setBackground(Color.DARK_GRAY);//sets the background color
        emptyPanel.setPreferredSize(new Dimension(200, 400));//sets the preferred size
        binary.add(mainPanel); //add the mainPanel to the binaryData panel
        binary.add(emptyPanel); //add the emptyPanel to the binaryData panel
    }
    /**
     * Creates the auth panel
     * @param authPanel the auth panel
     */
    private void createAuth(JPanel authPanel) {
        BoxLayout b = new BoxLayout(authPanel, BoxLayout.Y_AXIS);
        authPanel.setLayout(b);// set the layout
        JPanel mainPanel = new JPanel(); //a new panel for the labels and textFields
        mainPanel.setLayout(new GridLayout(3, 2, 5, 5)); //set the layout
        mainPanel.setBackground(Color.DARK_GRAY);//sets the background color

        JLabel token = new JLabel("TOKEN"); //a label for the token textField
        token.setOpaque(true);
        token.setBackground(Color.DARK_GRAY);//sets the background color
        token.setForeground(WHITE);//sets the foreground color

        JLabel prefix = new JLabel("PREFIX"); //a label for the prefix textField
        prefix.setOpaque(true);
        prefix.setBackground(Color.DARK_GRAY);//sets the background color
        prefix.setForeground(WHITE);//sets the foreground color

        JTextField tokenTxt = new JTextField(); //a text field for token
        tokenTxt.setOpaque(true);
        tokenTxt.setBackground(Color.DARK_GRAY);//sets the background color
        tokenTxt.setForeground(WHITE);//sets the foreground color

        JTextField prefixTxt = new JTextField(); //a text field for the prefix
        prefixTxt.setOpaque(true);
        prefixTxt.setBackground(Color.DARK_GRAY);//sets the background color
        prefixTxt.setForeground(WHITE);//sets the foreground color

        JCheckBox enabled = new JCheckBox("ENABLED"); //a checkBox for enabled
        enabled.setOpaque(true);
        enabled.setBackground(Color.DARK_GRAY);//sets the background color
        enabled.setForeground(WHITE);//sets the foreground color
        //add them to the mainPanel
        mainPanel.add(token);
        mainPanel.add(tokenTxt);
        mainPanel.add(prefix);
        mainPanel.add(prefixTxt);
        mainPanel.add(enabled);
        authPanel.add(mainPanel); //add the mainPanel to authPanel

        JPanel empTyPanel = new JPanel(); //a new empty panel
        empTyPanel.setBackground(Color.DARK_GRAY);//sets the background color
        empTyPanel.setPreferredSize(new Dimension(200, 200));//sets the preferred size
        authPanel.add(empTyPanel); //add the emptyPanel to authPanel
    }

    /**
     * Creates the query panel
     * @param queryPanel the query panel
     */
    private void createQuery(JPanel queryPanel) {
        queryPanel.setLayout(new BorderLayout());
        JPanel urlAndCopy = new JPanel(new FlowLayout(FlowLayout.LEFT));//a new panel for url label and text field
        urlAndCopy.setBackground(Color.DARK_GRAY);//sets the background color
        urlAndCopy.setForeground(WHITE);//sets the foreground color
        JLabel urlLabel = new JLabel("URL:"); //a new label for the URL textField
        urlLabel.setOpaque(true);
        urlLabel.setBackground(Color.DARK_GRAY);//sets the background color
        urlLabel.setForeground(WHITE);//sets the foreground color
        urlAndCopy.add(urlLabel);//add the label to the panel
        queryURL = new JTextField(); //a new textField for URL
        queryURL.setOpaque(true);
        queryURL.setBackground(Color.DARK_GRAY);//sets the background color
        queryURL.setForeground(WHITE);//sets the foreground color
        queryURL.setPreferredSize(new Dimension(300, 30));//sets the preferred size
        //add key listener for http textField
        http.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                queryURL.setText(http.getText());//set the url text to http text
            }
        });
        queryURL.setEditable(false); //make it not editable
        urlAndCopy.add(queryURL);  //add the text field to the panel

        JButton copy = new JButton("copy");//a new button for copy the url text field
        copy.setOpaque(true);
        copy.setBackground(Color.DARK_GRAY);//sets the background color
        copy.setForeground(WHITE);//sets the foreground color
        copy.addActionListener(new ActionListener() { //add action listener for the copy button
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //make a copy from url text field
                StringSelection stringSelection = new StringSelection(queryURL.getText()); //a new String Selection
                Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard(); //a new Clipboard
                cb.setContents(stringSelection, stringSelection); //set the contents of the clipBoard
            }
        });
        urlAndCopy.add(copy);//add the button to the panel
        queryPanel.add(urlAndCopy, BorderLayout.NORTH); //add the urlAndCopy panel to the queryPanel

        queryKeyAndValuePanel = new JPanel(); //a new panel for key and values
        queryKeyAndValuePanel.setBackground(Color.DARK_GRAY);//sets the background color
        queryPanel.add(queryKeyAndValuePanel, BorderLayout.CENTER);//add the keyAndValue panel to queryPanel
        queryKeyAndValuePanel.setPreferredSize(new Dimension(400, 2000));//sets the preferred size
        //create an empty KeyAndValue
        KeyAndValue keyAndValue=new KeyAndValue(queryKeyAndValuePanel,queryHashMap,null,null);
        JScrollPane scrollPane = new JScrollPane(queryKeyAndValuePanel);//a new scrollPane for keyAndVal panel
        scrollPane.setPreferredSize(new Dimension(400, 400));//sets the preferred size
        queryPanel.add(scrollPane);//add it to the queryPanel
    }


    /**
     * Appends the query keys and values to the end of the query URL
     */
    private void appendQueryParams(){
        String queryString="";
        queryURL.setText(getURL()); //set the query url text
        for(JTextField queryKey:queryHashMap.keySet()){
            if(queryKey.getText().equals("")){
                continue;
            }
            //append the keys and values to the string
            queryString+=queryKey.getText()+"="+queryHashMap.get(queryKey).getText()+"&";
        }
        queryString=queryString.replaceAll (" ","%20");
        if(!queryString.equals("")){
            queryURL.setText(getURL()+"?"+queryString); //append the string to the queryURL
        }
    }
    /**
     * This inner class represents a handler for the send button and the save button.
     * It implements ActionListener interface
     */
    class ButtonHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String data="";
            DataType dataType=null;
            if(body.getSelectedItem().equals("Body")){
                //their isn't any data for sending
                data=null;
            }
            else if(body.getSelectedItem().equals("Multipart Form Data")){
                for(JTextField key:formDataHashMap.keySet()){
                    if(key.getText().equals("")){
                        continue;
                    }
                    //append the keys and values to the data String
                    data+=key.getText()+"="+formDataHashMap.get(key).getText()+"&";
                }
                if(data.equals("")){
                    data=null;
                }
                dataType=DataType.multipartFormData; //set the dataType to multipart form data
            }
            else if(body.getSelectedItem().equals("JSON")){
                data="";
                data=jsonData.getText(); //set the data to the jsonData text area
                dataType=DataType.JSON; //set the data type to json
            }
            else if(body.getSelectedItem().equals("Binary Data")){
                data="";
                if(binaryData.getText ().equals ("No File Selected")){
                    data=""; //data is null
                }
                else {
                    data = binaryData.getText (); //set the data to binaryData text area
                }
                dataType=DataType.Binary; //set the data type to binary
            }
            else if(body.getSelectedItem().equals("Form URL Encoded")){
                for(JTextField key:urlencodedHashMap.keySet()){
                    if(key.getText().equals("")){
                        continue;
                    }
                    //append the keys and values to the data
                    data+=key.getText()+"="+urlencodedHashMap.get(key).getText()+"&";
                }
                if(data.equals("")){
                    data=null;
                }
                dataType=DataType.fromUrlEncoded; //set the data type to form url encoded
            }
            if(actionEvent.getSource().equals(sendButton)){
                //send button is pressed
                appendQueryParams(); //append the query keys and values
                RequestHandler rqHandler; //a new request handler
                if(!queryURL.getText().equals(getURL())){
                    //their are some queries to add
                    rqHandler=new RequestHandler(queryURL.getText(),getMethod(),panel3,sendButton,saveButton,panel1.getPlusButton());
                }

                else {
                    //their isn't any query
                    rqHandler = new RequestHandler(getURL(), getMethod(), panel3, sendButton, saveButton,panel1.getPlusButton());
                }
                rqHandler.setData(data); //set the data
                rqHandler.setDataType(dataType); //set the data type
                rqHandler.setHeaders(reqPropertiesHashMap); //set the request headers
                rqHandler.setFollowRedirect(followRedirect); //set the follow redirect status
                rqHandler.execute(); //execute the handler
                return;
            }
            if(actionEvent.getSource().equals(saveButton)){
                //the save button is pressed
                appendQueryParams(); //append the query params
                JFrame saveFrame = new JFrame("Save Request"); //a new frame for save request
                saveFrame.setLocationRelativeTo(null);//sets the location of save frame
                saveFrame.setSize(new Dimension(400, 400)); //sets the size of the frame
                JPanel savePanel = new JPanel(new GridLayout(4, 2));//sets the layout
                JLabel nameLabel = new JLabel("Request Name");//a new label
                JTextField nameField = new JTextField();//a new text field
                nameField.setPreferredSize(new Dimension(200, 10));//sets the size of the field
                savePanel.add(nameLabel);//add the label to the save panel
                savePanel.add(nameField); //add the name field to the save panel
                JButton saveBtn = new JButton("Save"); //a new button for save
                savePanel.add(saveBtn); //add the save button to the savePanel
                saveFrame.add(savePanel); //add the savePanel to the save frame
                saveFrame.setVisible(true); //set the frame visible
                String finalData = data; //the data
                DataType finalDataType = dataType; //the data type
                //add action listener for the save button
                saveBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        if(!nameField.getText().equals("")&& !nameField.getText().trim().equals("")) {
                            //valid name for saving the request
                            saveFrame.setVisible(false); //make the frame not visible
                            SaveRequestHandler handler; //a save request handler
                            if(!queryURL.getText().equals(getURL())) {
                                //their are some queries to add
                                handler = new SaveRequestHandler(queryURL.getText(), getMethod(), panel3, panel1, nameField.getText(), sendButton, saveButton);
                            }
                            else {
                                //their isn't any query to add
                                handler = new SaveRequestHandler(getURL(), getMethod(), panel3, panel1, nameField.getText(), sendButton, saveButton);
                            }
                            handler.setData(finalData); //set the data
                            handler.setDataType(finalDataType); //set the data type
                            handler.setHeaders(reqPropertiesHashMap); //set the request headers
                            handler.setFollowRedirect (followRedirect);//set the follow redirect status
                            handler.execute(); //execute the handler
                        }
                    }
                });

            }
        }
    }

}

