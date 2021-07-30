package com.company;

//import org.json.JSONException;
//import org.json.JSONObject;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * This class represents a Request Handler for sending a request
 * It extends SwingWorker
 * @author Leili
 */
public class RequestHandler extends SwingWorker {
    protected URL url; //an URL
    protected String method; //a String for the method
    protected String[] headers; //an Array of Strings for the headers
    protected String data; //a String for the data
    protected ResponsePanel p3; //the ResponsePanel
    protected Request req; //a request
    protected JButton sendButton; //the sendButton
    protected JButton saveButton; //the saveButton
    protected boolean followRedirect=false; //the follow redirect status
    protected  JButton plusButton; //a plus button for new request
    private DataType dataType; //a data type

    /**
     * Creates a request handler with the given URL,method,Response panel,Send button,Save button and plus button
     * @param url a String represents the URL
     * @param method a String represents the request's method
     * @param p3 the Response panel
     * @param sendButton the send button
     * @param saveButton the save button
     * @param plusButton the plus button
     */
    public RequestHandler(String url,String method,ResponsePanel p3,JButton sendButton,JButton saveButton,JButton plusButton){
        this.sendButton=sendButton;
        this.saveButton=saveButton;
        this.plusButton=plusButton;
        this.p3=p3;
        try {
            if(url.startsWith("http")|| url.startsWith("https")) {
                //sets the url
                this.url = new URL(url);
            }
            else{
                //sets the default protocol to http
                this.url=new URL("http://"+url);
            }
        } catch (MalformedURLException e) {
            p3.setRawTxt("Error: Couldn't resolve host name\nCheck your URL!"); //sets the error message in raw panel
            p3.setLabels("ERROR","0 ms","0 B"); //sets the labels of the response panel
            return;
        }
        this.method=method;
    }

    /**
     * a setter for the follow redirect
     * @param followRedirect a boolean represents the follow redirect status
     */
    public void setFollowRedirect(boolean followRedirect) {
        this.followRedirect = followRedirect;
    }

    /**
     * a setter for the data
     * @param data a String represents the data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * a setter for the data type
     * @param dataType a DataType
     */
    public void setDataType(DataType dataType){
        this.dataType=dataType;
    }

    /**
     * a setter for the request headers
     * @param keysAndValues a HashMap of JTextFields
     */
    public void setHeaders(HashMap<JTextField,JTextField>keysAndValues){
        if(keysAndValues==null){
            return;
        }
        headers=new String[keysAndValues.size()]; //a new Array of Strings
        int i=0;
        for(JTextField key:keysAndValues.keySet()){
            if(key.getText().equals("")){
                continue;
            }
            String s=key.getText()+":"+keysAndValues.get(key).getText(); //a String for key and value
            headers[i]=s; //add the String to the Array
            i++;
        }
    }

    /**
     * Creates a new request and set the details of it
     */
    protected  void setDetails(){
        //make the buttons disabled
        saveButton.setEnabled(false);
        sendButton.setEnabled(false);
        plusButton.setEnabled(false);
        req=new Request(url); //a new Request
        req.setMethod(method); //set the request's method
        if(headers!=null){
            req.setHeaders(headers); //set the request's headers
        }
        req.setFollowRedirect(followRedirect); //set the follow redirect status
        req.setDataType(dataType); //set the data type of the request
        req.setData(data); //set the data of the request
    }

    /**
     * Sets the response of the request.
     * It checks the Content-Type of the response and due to that, it sets the response body of the request
     */
    private void setResponse () {
        p3.resetPanel (); //reset the response panel
        if(req.getResponseBody()==null && req.getStatusCode ().equals ("")){
            //their isn't any response,So an error occurred
            p3.setRawTxt("Error: Couldn't resolve host name\nCheck your internet connection or the URL you have entered!"); //set the raw text
            p3.setLabels("ERROR","0 ms","0 B"); //set the labels
        }
        else {
            p3.setLabels(req.getStatusCode(), req.getTime(), req.getSize()); //set the labels
            p3.setResponseHeaders(req.getResponseHeaders()); //set the response headers
            p3.setRawTxt(req.getResponseBody()); //set the raw text
            if(req.getResponseHeaders ().get("Content-Type").contains ("json")) {
                //the content type is json
                /*JSONObject json; //a JSON object
                try {
                    json = new JSONObject (req.getResponseBody ()); //a new object with the request response body
                    p3.setJsonTxt (json.toString ()); //set the json text
                } catch (JSONException e) {
                    System.err.println (e.getMessage ());//an error message
                }

                 */
            }
            if(req.getResponseHeaders ().get ("Content-Type").contains ("png") || req.getResponseHeaders ().get ("Content-Type").contains ("image")) {
                //the content type is png
                Image image = null; //an Image
                try {
                    image = ImageIO.read (url);
                } catch (IOException e) {
                    System.err.println (e.getMessage ()); //an error message
                }
                JLabel label = new JLabel (new ImageIcon (image)); //a new label with the image
                p3.setPreviewImage (label); //set the preview image to the label
            }

        }
    }

    /**
     * The override version of doInBackground method
     * @return a Request
     */
    @Override
    protected Request doInBackground(){
        setDetails(); //set the request details
        req.start(); //start the request
        try {
            req.join(); //join
        } catch (InterruptedException e) {
            System.err.println (e.getMessage ()); //an error message
        }
        setResponse(); //set the response of the request
        return req;
    }

    /**
     * The override version of done method
     */
    @Override
    protected void done() {
        //enable the buttons
        sendButton.setEnabled(true);
        saveButton.setEnabled(true);
        plusButton.setEnabled(true);
    }



}
