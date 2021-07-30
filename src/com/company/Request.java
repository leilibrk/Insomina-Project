package com.company;

//import org.json.JSONException;
//import org.json.JSONObject;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * This class represents a request
 * It extends Thread class and implements Serializable
 * @author Leili
 */

public class Request extends Thread implements Serializable {
    private URL url; //an url
    private String method; //a string for the method
    private String data; //a string for the data
    private String[] reqProperties; //an array of strings for the request properties(request headers)
    private boolean followRedirect=false; //follow redirect status
    private boolean showHeaders=false;//show the headers
    private transient HttpURLConnection connection; //a http url connection
    private String fileForSaving; //a fileName for saving data
    private String statusCode; //the status code of the request
    private String durationTime; //the duration time of the request
    private String size; //the size of the request
    private transient String responseBody; //the response body of the request
    private HashMap<String,String> responseHeaders; //the response headers of the request
    private String savedName; //the saved name
    private DataType dataType; //the data type of the request body
    private transient Instant start; //an Instant for start time

    /**
     * Creates a new request with the given URL
     * @param url the given URL
     */
    public Request(URL url){
        this.url=url;
    }

    /**
     * a setter for the saved name
     * @param savedName the saved name
     */
    public void setSavedName(String savedName) {
        this.savedName = savedName;
    }

    /**
     * a setter for the request body data
     * @param data a String for data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * a setter for the data type of the request body
     * @param dataType a DataType
     */
    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    /**
     * a setter for the method of the request
     * @param method a String for method type
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * a setter for the request headers
     * @param keyAndValues an Array of Strings for headers
     */
    public void setHeaders(String[] keyAndValues) {
        this.reqProperties=keyAndValues;
    }


    /**
     * a setter for the followRedirect boolean
     * @param followRedirect a boolean
     */
    public void setFollowRedirect(boolean followRedirect) {
        this.followRedirect = followRedirect;
    }

    /**
     * a setter for showing the headers of the response
     * @param showHeaders a boolean
     */
    public void setShowHeaders(boolean showHeaders) {
        this.showHeaders = showHeaders;
    }

    /**
     * a setter for the file for saving
     * @param fileForSaving a file name for saving response
     */
    public void setFileForSaving(String fileForSaving){
        this.fileForSaving=fileForSaving;
    }

    /**
     * a getter for the request's method
     * @return method field
     */

    public String getMethod() {
        return method;
    }

    /**
     * a getter for the request's saved name
     * @return savedName field
     */
    public String getSavedName() {
        return savedName;
    }

    /**
     * a getter for the request's URL
     * @return url field
     */
    public URL getUrl() {
        return url;
    }

    /**
     * a getter for the request's data
     * @return data field
     */
    public String getData() {
        return data;
    }

    /**
     * a getter for the request properties(request headers)
     * @return reqProperties field
     */
    public String[] getReqProperties() {
        return reqProperties;
    }

    /**
     * a getter for the data type of the request's body
     * @return dataType field
     */
    public DataType getDataType() {
        return dataType;
    }

    /**
     * a getter for the status code of the request
     * @return statusCode field
     */
    public String getStatusCode(){
        return this.statusCode;
    }

    /**
     * a getter for the response body of the request
     * @return responseBody field
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * a getter for the size of the request's response
     * @return size field
     */
    public String getSize() {
        return size;
    }

    /**
     * a getter for the duration time of sending the request and getting response
     * @return durationTime field
     */
    public String getTime() {
        return durationTime;
    }

    /**
     * a getter for the response headers of the request
     * @return responseHeaders field
     */
    public HashMap<String,String> getResponseHeaders() {
        return this.responseHeaders;
    }

    /**
     * a getter for the showHeaders boolean
     * @return showHeaders field
     */
    public boolean isShowHeaders() {
        return showHeaders;
    }

    /**
     * The override version of run method from Thread class
     */
    @Override
    public void run() {
        try {
            if(url==null){
                //their is nothing to do
                return;
            }
            if(url.getProtocol().equals("http")) {
                start=Instant.now();
                //cast to http url connection
                connection = (HttpURLConnection) url.openConnection();
            }
            else if(url.getProtocol().equals("https")){
                start=Instant.now();
                //cast to https url connection
                connection=(HttpsURLConnection) url.openConnection();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage()); //an error message
            System.exit(1); //exit the program
        }

        sendRequestHeaders(); //send the request headers

        switch (method) {
            case "GET":
                //GET request
                GET(); //call the GET method
                break;
            case "POST":
                //POST request
                try {
                    connection.setRequestMethod("POST"); //set the connection request method to POST
                } catch (ProtocolException e) {
                    System.err.println(e.getMessage()); //an error message
                    System.exit(1); //exit the program
                }
                sendRequestData(); //send the request body
                break;
            case "PUT":
                //PUT request
                try {
                    connection.setRequestMethod("PUT"); //set the connection request method to PUT
                } catch (ProtocolException e) {
                    System.err.println(e.getMessage()); //an error message
                    System.exit(1);//exit the program
                }
                sendRequestData(); //send the request body
                break;
            case "DELETE":
                //DELETE request
                DELETE(); //call the DELETE method
                break;
            case "PATCH":
                //PATCH request
                PATCH();  //call the PATCH method
                break;
        }
    }

    /**
     * Sets the connection's request properties
     */
    private void sendRequestHeaders(){
        if(reqProperties!=null){
            //their are some request properties to set
            for (String keyAndValue : reqProperties) {
                if(keyAndValue==null){
                    continue;
                }
                String[] s = keyAndValue.split(":"); //split the key and value from each other
                connection.setRequestProperty(s[0], s[1]); //set the request property
            }
        }
    }

    /**
     * This method is for GET requests
     * It checks the follow redirect boolean first and if it's true, it calls the redirect method
     * and then prints the result.
     */
    public void GET(){
        if(followRedirect){
            //follow redirect is true
            try {
                redirect(); //call the redirect method
            } catch (IOException e) {
                System.err.println("Cannot redirect!");
            }
        }
        getResult(); //prints the result
    }

    /**
     * Sets the request body
     * If the data type is json, it calls the JSONData method
     * Else if the data type is multi part form data or form url encoded , it calls the formData method
     * Else if the data type is binary, it calls the uploadBinary method
     */
    public void sendRequestData(){
        if(data!=null) {
            if(dataType.equals(DataType.JSON)){
               /* try {
                    //the data is in JSON format
                    JSONData(new JSONObject(data)); //call the JSONData method
                } catch (JSONException e) {
                    System.err.println("Cannot send json data");//an error message
                    statusCode="ERROR";
                }

                */
            }
            else if(dataType.equals(DataType.multipartFormData)){
                //the data is form data
                formData(DataType.multipartFormData);//call the formData method
            }
            else if(dataType.equals(DataType.Binary)){
                if(data.equals ("")){
                    System.err.println ("No file!"); //an error message
                    statusCode="ERROR";
                    return;
                }
                //the data is binary
                File file = new File(data);
                if(file.exists ()) {
                    uploadBinary (file);//call the uploadBinary method
                }
                else{
                    System.err.println ("The file doesn't exist"); //an error message
                    statusCode="ERROR";
                }
            }
            else if(dataType.equals(DataType.fromUrlEncoded)){
                //the data is form url encoded
                formData(DataType.fromUrlEncoded);
            }
        }
        else {
            getResult(); //get the result
        }

    }

    /**
     * This method is for DELETE requests
     */
    public void DELETE(){
        connection.setDoOutput(true); //set do output to true
        try {
            connection.setRequestMethod("DELETE"); //set the request method to DELETE
        } catch (ProtocolException e) {
            System.err.println(e.getMessage()); //an error message
            System.exit(1); //exit the program
        }
        sendRequestData(); //send the request body
    }

    /**
     * This method is for PATCH requests
     */
    private void PATCH(){
        try {
            Field methodsField = HttpURLConnection.class.getDeclaredField("methods"); //get the methods fields

            Field modifiersField = Field.class.getDeclaredField("modifiers"); //get the modifiers fields
            modifiersField.setAccessible(true); //set the modifiers field accessible
            modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

            methodsField.setAccessible(true); //set the methods field accessible

            String[] oldMethods = (String[]) methodsField.get(null); //old methods
            Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
            methodsSet.add("PATCH");
            String[] newMethods = methodsSet.toArray(new String[0]); //new methods

            methodsField.set(null/*static field*/, newMethods);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        try {
            connection.setRequestMethod("PATCH"); //set the request method to PATCH
        } catch (ProtocolException e) {
            System.err.println(e.getMessage()); //an error message
            System.exit(1); //exit the program
        }
        sendRequestData(); //send the request data
    }

    /**
     * Gets the result of the request and set the response body and response headers
     * If their is a file for saving, it will write the response body in the file
     */
    public void getResult() {
        try {
            statusCode=connection.getResponseCode()+" "+connection.getResponseMessage(); //get the status response code and message
        } catch (IOException e) {
            statusCode="";
            return;
        }
        responseBody="";
        responseHeaders=new HashMap<>();
        for (String key:connection.getHeaderFields().keySet()){
            //headers fields
            if(key==null || key.equals("null")){
                continue;
            }
            StringBuilder value= new StringBuilder(); //an String builder
            for(String v:connection.getHeaderFields().get(key)){
                value.append(v); //append the value
            }
            responseHeaders.put(key, value.toString());
        }


        BufferedReader bufferedReader = null; //a buffer reader
        try {
            if (connection.getResponseCode()/100==2 || connection.getResponseCode()/100==3) {
                //successful connection
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            else{
                //unsuccessful connection
                bufferedReader=new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());//an error message
            return;
        }

        String currentLine;
        while (true) {
            try {
                currentLine = bufferedReader.readLine(); //read a line
                if(currentLine==null){
                    //finished
                    break;
                }
                responseBody+=currentLine; //add it to the responseBody
                responseBody+="\n"; //a new Line

            } catch (IOException e) {
                System.err.println(e.getMessage());//an error message
            }
        }


        if(fileForSaving!=null){
            //their is a file for saving the response in it
            saveToFile();
        }
        Instant end=Instant.now(); //end time
        Duration time= null; //the duration time
        if (start != null) {
            time = Duration.between(start,end);
        }
        if (time != null) {
            if(time.toMillis()<1000){
                durationTime=time.toMillis()+"ms"; //time to milli seconds
            }
            else{
                durationTime=time.toSeconds()+"s"; //time to seconds
            }
        }
        int size; //
        if(connection.getHeaderField("Content-Length")!=null){
            size= Integer.parseInt(connection.getHeaderField("Content-Length")); //the size of the response
        }
        else{
            size=responseBody.getBytes().length; //the size of the response
        }
        if(size/1000>=1){
            this.size=(float)size/1000+"KB"; //the size in Kilo Byte
        }
        else{
            this.size=size+"B"; //the size in Byte
        }
    }

    /**
     * Saves the response in a file due to the content type.
     */
    private void saveToFile () {
        int i=fileForSaving.lastIndexOf (".");
        if(i<0) {
            String s = responseHeaders.get ("Content-Type");
            if (s == null) {
                fileForSaving = fileForSaving + ".txt";
            } else if (s.contains ("png") || s.contains ("image")) {
                //the content type is image
                fileForSaving = fileForSaving + ".png";
            } else if (s.contains ("pdf")) {
                //the content type is pdf
                fileForSaving = fileForSaving + ".pdf";
            } else if (s.contains ("text") && !s.contains ("html")) {
                //the content type is text
                fileForSaving = fileForSaving + ".txt";
            } else if (s.contains ("html")) {
                //the content type is html
                fileForSaving = fileForSaving + ".html";
            } else {
                //the content type is something else
                fileForSaving = fileForSaving + ".txt";
            }
        }
        try {
            FileOutputStream outputStream=new FileOutputStream(new File (fileForSaving)); //a file output stream
            outputStream.write(responseBody.getBytes()); //write the response body in the file

        } catch (IOException e) {
            System.err.println(e.getMessage()); //an error message
        }
    }

    /**
     * @param body a HashMap of the body
     * @param boundary a String for the boundary
     * @param bufferedOutputStream a BufferOutputStream
     * @throws IOException e
     */
    public void bufferOutFormData(HashMap<String, String> body, String boundary, BufferedOutputStream bufferedOutputStream) throws IOException {
        for (String key : body.keySet()) {
            bufferedOutputStream.write(("--" + boundary + "\r\n").getBytes());
            if (key.contains("file")) {
                bufferedOutputStream.write(("Content-Disposition: form-data; filename=\"" + (new File(body.get(key))).getName() + "\"\r\nContent-Type: Auto\r\n\r\n").getBytes());
                try {
                    BufferedInputStream tempBufferedInputStream = new BufferedInputStream(new FileInputStream(new File(body.get(key))));
                    byte[] filesBytes = tempBufferedInputStream.readAllBytes();
                    bufferedOutputStream.write(filesBytes);
                    bufferedOutputStream.write("\r\n".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bufferedOutputStream.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes());
                bufferedOutputStream.write((body.get(key) + "\r\n").getBytes());
            }
        }
        bufferedOutputStream.write(("--" + boundary + "--\r\n").getBytes());
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }

    /**
     * This method is for Form Data request body.
     */
    public void formData(DataType dataType) {
        HashMap<String, String> body = new HashMap<>(); //a HashMap for the body
        String[] content=data.split("&"); //split the contents
        for(String s:content){
            String[] keyAndValues=s.split("=");
            String key=keyAndValues[0]; //set the key
            String value=keyAndValues[1]; //set the value
            body.put(key,value); //put the key and the value
        }
        try {
            String boundary = System.currentTimeMillis() + ""; //a boundary
            connection.setDoOutput(true); //set the do output
            if(dataType.equals(DataType.fromUrlEncoded)){
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); //set the content type
            }
            else {
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary); //set the content type
            }
            BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream()); //an output stream of the connection output stream
            bufferOutFormData(body, boundary, os); //call the bufferOutFormData method
            getResult(); //get the result
        }
        catch (IOException e) {
            System.err.println("Error: Couldn't resolve host name\nCheck your internet connection or the URL you have entered!");
        }
    }

    /**
     * This method is for JSON Data request body
     * @param jsonObject a json object
     */
   /* public void JSONData(JSONObject jsonObject){

        connection.setDoOutput(true); //set the do output to true
        connection.setRequestProperty("Content-Type","application/json"); //set the content type
        try(OutputStream os = connection.getOutputStream()) {
            //write the jsonObject to the connection output stream
            os.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println(e.getMessage());//an error message
        }
        getResult(); //get the result
    }

    */

    /**
     * This method is for File request body
     * @param file an uploaded file
     */
    public void uploadBinary(File file) {
        try {
            connection.setDoOutput(true); //set the do output to true
            connection.setRequestProperty("Content-Type", "application/octet-stream"); //set the content type
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(connection.getOutputStream());
            BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(file)); //a file input stream
            bufferedOutputStream.write(fileInputStream.readAllBytes());//write the fileInputStream to the buffer output stream
            bufferedOutputStream.flush(); //flush
            bufferedOutputStream.close(); //close the buffer output stream
            getResult(); //get the result
        } catch (IOException e) {
            System.out.println(e.getMessage());//an error message
        }

    }

    /**
     * Redirects the connection to the specified location
     * @throws IOException e
     */
    public void redirect() throws IOException {
        connection.setInstanceFollowRedirects(true); //set instance follow redirect to true
        URL newURL=null; //a new URL
        while (connection.getResponseCode()/100==3){
            //redirection is needed
            String newAddress=connection.getHeaderField("Location"); //get the new url address
            String cookies=connection.getHeaderField("Set-Cookie"); //get the cookies
            newURL=new URL(newAddress); //create a new URL with the new address
            connection=(HttpURLConnection)newURL.openConnection(); //open connection
            connection.setRequestProperty("Cookie",cookies); //set the cookies
            sendRequestHeaders(); //send the headers
        }
        System.out.println();
        System.out.println("\033[0;33m"+"Redirect to:"+newURL+"\033[0m");
        System.out.println();
    }

    /**
     * The override version of toString method
     * @return a String of request's details
     */
    @Override
    public String toString() {
        String s;
        s="url: " + url + "  | method: '" + method + '\'';
        if(reqProperties!=null){
            s+="  | headers:" + Arrays.toString(reqProperties);
        }

        if(data!=null) {
            s += "  | data:" + data+"  |data type:"+dataType;
        }
        if(followRedirect){
            s+="  | follow redirect";
        }
        if(fileForSaving!=null){
            s+=" | file for saving: "+fileForSaving;
        }
        return s;
    }

}
