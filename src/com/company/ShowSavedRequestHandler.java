package com.company;

//import org.json.JSONException;
//import org.json.JSONObject;
import javax.swing.*;
import java.io.File;

/**
 * This class represents a Show Saved Request Handler for showing a saved request
 * It extends Swing Worker
 */
public class ShowSavedRequestHandler extends SwingWorker {
    private ResponsePanel responsePanel; //the response panel
    private SendRequestPanel sendRequestPanel; //the send request panel
    private Request request; //the request

    /**
     * Creates a ShowSavedRequestHandler with the given responsePanel, sendRequestPanel and the request
     * @param responsePanel the responsePanel
     * @param sendRequestPanel the sendRequestPanel
     * @param request the request
     */
    public ShowSavedRequestHandler(ResponsePanel responsePanel,SendRequestPanel sendRequestPanel,Request request){
        this.responsePanel=responsePanel;
        this.sendRequestPanel=sendRequestPanel;
        this.request=request;
    }

    /**
     * The override version of doInBackground method
     * @return null
     */
    @Override
    protected Object doInBackground(){
        responsePanel.resetPanel(); //reset the responsePanel
        sendRequestPanel.resetPanel(); //reset the sendRequestPanel
        if(request.getUrl().toString().contains("?")){
            //it has some queries to add
            sendRequestPanel.setQueryURL(request.getUrl().toString()); //set the query URL
            String[] s=request.getUrl().toString().split("\\?"); //split the request url by ?
            sendRequestPanel.setURL(s[0]); //set the request url
            String[] ss=s[1].split("&"); //split the queries
            for (String value : ss) {
                String[] keyValues = value.split("="); //split to key and value
                //create a keyAndValue for each query
                KeyAndValue keyAndValue=new KeyAndValue(sendRequestPanel.getQueryKeyAndValuePanel(), sendRequestPanel.getQueryHashMap(), keyValues[0], keyValues[1]);
            }
            ResponsePanel.updatePanel (sendRequestPanel.getQueryKeyAndValuePanel ());//update query panel
        }
        else{
            sendRequestPanel.setURL(request.getUrl().toString()); //set the sendRequestPanel's URL
            sendRequestPanel.setQueryURL(request.getUrl().toString()); //set the query URL
        }
        sendRequestPanel.setMethod(request.getMethod()); //set the sendRequestPanel's method
        if(request.getReqProperties()!=null){
            //their are some request properties(request headers)
            for(String s:request.getReqProperties()) {
                if(s==null){
                    continue;
                }
                String[] keyValue=s.split(":");
                //create a KeyAndValue for each request property
                KeyAndValue keyAndValue1=new KeyAndValue(sendRequestPanel.getReqPropertyPanel(),sendRequestPanel.getReqPropertiesHashMap (),keyValue[0],keyValue[1]);
                ResponsePanel.updatePanel (sendRequestPanel.getReqPropertyPanel ());//update request property panel
            }

        }
        if(request.getData()!=null){
            //their are some data
            if(request.getDataType().equals(DataType.multipartFormData)){
                //the request body is multipart form data
                sendRequestPanel.setFormData(request.getData()); //set the form data
            }
            else if(request.getDataType().equals(DataType.JSON)){
                //the request body is json
              /*  JSONObject jsonData; //an JSON object
                try{
                    jsonData=new JSONObject(request.getData()); //a new JSON object with the request data
                    sendRequestPanel.setJsonData(jsonData.toString()); //set the json data
                }catch (JSONException e) {
                    System.err.println("Cannot send JSON data"); //an error message
                }
                
               */
            }
            else if(request.getDataType().equals(DataType.Binary)) {
                //the request body is binary
                File file=new File(request.getData()); //a new file with the request data
                if(file.exists()){
                    sendRequestPanel.setBinaryData(file.getAbsolutePath()); //set the binary data
                }
            }
            else if(request.getDataType().equals(DataType.fromUrlEncoded)){
                //the request body is form url encoded
                sendRequestPanel.setUrlencodedData(request.getData()); //set the url encoded data
            }
        }
        return null;
    }
}
