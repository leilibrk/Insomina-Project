package com.company;


import javax.swing.*;


/**
 * This class represents a Save Request Handler for saving a request
 * It extends Request Handler class
 */
public class SaveRequestHandler extends RequestHandler {
    private String name; //a name for saving the request

    /**
     * Creates a Save request handler with the given URL, method, ResponsePanel, RequestListPanel, name, sendButton and saveButton
     * @param url a String represents the URL
     * @param method a String represents the request's method
     * @param panel3 the Response panel
     * @param panel1 the RequestListPanel
     * @param name a String represents a name for saving the request
     * @param sendButton the send button
     * @param saveButton the save button
     */
    public SaveRequestHandler(String url, String method, ResponsePanel panel3, RequestListPanel panel1, String name, JButton sendButton,JButton saveButton){
        super(url,method,panel3,sendButton,saveButton,panel1.getPlusButton());//call the super constructor
        this.name=name;
        panel1.addRequest(method,name); //add the request to the panel1
    }

    /**
     * The override version of doInBackground method
     * @return null
     */
    @Override
    protected Request doInBackground() {
        super.setDetails(); //call the setDetails method from the super class
        return null;
    }

    /**
     * The override version of done method
     */
    @Override
    protected void done() {
        super.done (); //call the done method from the super class
        super.req.setSavedName(name); //set the request's saved name
        Client.saveRequest(super.req); //save the request by calling the saveRequest method from the client class
    }

}
