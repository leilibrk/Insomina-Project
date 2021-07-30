package com.company;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * This class represents a Client for Http requests
 * @author Leili
 */

public class Client {
    private ArrayList<String> inputs; //an ArrayList of inputs
    private Request request; //a Request

    /**
     * Creates a client with the given ArrayList of Strings
     * @param inputs an ArrayList of inputs
     */
    public Client(ArrayList<String> inputs) {
        this.inputs =inputs;

        checkInputs(); //check the inputs

        if (inputs.size() == 2 && (inputs.get(1).equals("--help") || inputs.get(1).equals("--h"))) {
            //help request
            help(); //call help method
            return;
        }
        else if(inputs.size()==2 && (inputs.get(1).equals("list"))){
            //show the list of saved requests
            int count=1;
            for (Request r:listSavedRequests()){
                System.out.println(count+" . "+r.toString()); //print the request details
                count++;
            }
            return;
        }
        else if(inputs.contains("fire")){
            fire(); //fire some requests
            return;
        }

        createRequest();//create a request

        if (inputs.size() == 2) {
            //default get method
            request.setMethod("GET"); //set the request method to GET
            sendRequest(); //send the request
            return;
        }
        if(inputs.contains("-i")){
            request.setShowHeaders(true);//set the showHeaders boolean to true
        }
        if(inputs.contains("-H")||inputs.contains("--headers")){
            setHeaders(); //call setHeaders method for setting the request headers
        }

        if(inputs.contains("-f")){
            request.setFollowRedirect(true); //set the FollowRedirect to true for redirecting while connecting to the server
        }
        if (inputs.contains("-O") || inputs.contains("--output")) {
            saveToFile(); //call the saveToFile method for saving the response in a file
        }
        if (inputs.contains("--method") || inputs.contains("-M")) {
            setMethod(); //set the request's method
        } else {
            //default get method
            request.setMethod("GET"); //set the request method to GET
        }
        sendRequest(); //send the request
        if(inputs.contains("-S")||inputs.contains("--save")){
            setSavedName(); //set the saved name
            saveRequest(request); //save the request
        }
    }
    /**
     * Creates a new URL and if it is valid,it creates a new Request with the URL.
     */
    private void createRequest() {
        URL url=null; //an URL
        try {
            if(inputs.get(1).startsWith("http") || inputs.get(1).startsWith("https")) {
                url = new URL(inputs.get(1)); //a new URL
            }
            else{
                //set a default protocol (http) for the given url
                url =new URL("http://"+inputs.get(1));
            }

        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());//an error message
            System.exit(1); //exit the program
        }
        request=new Request(url); //a new request with the given url
    }

    /**
     * Sets the method of the request
     */
    private void setMethod() {
        ArrayList<String> methods = new ArrayList<>(); //an ArrayList of supported methods
        //add supported methods
        methods.add("GET");
        methods.add("POST");
        methods.add("PUT");
        methods.add("DELETE");
        methods.add("PATCH");

        int i;
        if (inputs.contains("--method")) {
            i = inputs.indexOf("--method");
        } else {
            i = inputs.indexOf("-M");
        }
        i++;
        if (inputs.size() == i) {
            //after the --method /-M their isn't any given method
            System.err.println("No method!");//an error message
            System.exit(1); //exit the program
        }
        if (methods.contains(inputs.get(i))) {
            //the selected method is supported by this client
            switch (inputs.get(i)) {
                case "GET":
                    request.setMethod("GET"); //set the request method to GET
                    break;
                case "POST":
                    request.setMethod("POST"); //set the request method to POST
                    setData(); //call setData method
                    break;
                case "PUT":
                    request.setMethod("PUT"); //set the request method to PUT
                    setData(); //call the setData method
                    break;
                case "DELETE":
                    request.setMethod("DELETE"); //set the request method to DELETE
                    setData ();//call the setData method
                    break;
                case "PATCH":
                    request.setMethod("PATCH"); //set the request method to PATCH
                    setData(); //call the setData method
                    break;
            }

        } else {
            //the method is not supported by this client
            System.err.println("The selected method is not supported!"); //an error message
            System.exit(1);//exit the program
        }
    }

    /**
     * Starts the request and prints the response of the request
     */

    private void sendRequest() {
        request.start(); //start the request
        try {
            request.join(); //jon the request
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());//an error message
            System.exit(1); //exit the program
        }
        if(request.getResponseBody()==null && request.getStatusCode().equals ("")){
            //the response body and the status code is null,So their is a problem while connecting to the server
            System.err.println("Error: Couldn't resolve host name\nCheck your internet connection or the URL you have entered!");
            System.exit(1);//exit the program
        }
        else {
            if (request.isShowHeaders()) {
                //show headers
                for(String s:request.getResponseHeaders().keySet()){
                    //prints the headers
                    System.out.println(s+"  :  "+request.getResponseHeaders().get(s));
                }
                System.out.println(); //an empty line
            }
            System.out.println(request.getResponseBody()); //prints the response body of the request
        }
    }

    /**
     * Checks the inputs
     * It has an ArrayList of supported arguments and checks the inputs with this arguments
     */
    private void checkInputs() {
        if(inputs.size()==1){
            //incorrect input
            System.err.println("Incorrect input"); //an error message
            System.exit(1);//exit the program
        }
        //an ArrayList of supported arguments
        ArrayList<String> supportedArguments = new ArrayList<>();
        //the supported arguments
        String[] arguments={"-O","--output","-M","--method","-H","--headers","-i","-S","--save","-d","--data","-j","--json","--upload","-f","--help","-urlencoded"};
        supportedArguments.addAll(Arrays.asList(arguments)); //add all the supported arguments
        for (String s:inputs){
            if(s.contains("-")){
                if(!supportedArguments.contains(s)){
                    //it isn't a supported argument!
                    System.err.println("unsupported argument!"); //an error message
                    System.exit(1);//exit the program
                }
            }
        }
    }

    /**
     * Prints some information about supported arguments
     */
    private void help() {
        String information="-M | --method :   Request method: GET-POST-PUT-DELETE\n"+
                "-i            :   Show headers in response body\n"+
                "-H | --headers:   Set headers for request \n"+
                "-f |          :   Follow redirect\n"+
                "-d | --data   :   Form data message body\n"+
                "--json | -j   :   JSON message body\n" +
                "--upload      :   Binary message body\n" +
                "-urlencoded   :   Form url encoded message body\n"+
                "--save | -S   :   Save the request\n" +
                "--output | -O :   Save the response body in a given file or a specified file by the program\n" +
                "list          :   Requests list\n"+
                "fire          :   Fire some selected requests\n";

        System.out.println(information); //prints the information

    }

    /**
     * Saves the response body to a file by calling the setFileForSaving method for the request.
     * If their isn't any given file name, it will save the response body in to a file named "output_CurrentDate".
     */
    private void saveToFile(){
        String fileName; //the file name
        int index;
        if (inputs.contains("-O")) {
            index = inputs.indexOf("-O");
        } else {
            index = inputs.indexOf("--output");
        }
        index++;
        if (inputs.size() == index || inputs.get(index).contains("-")) {
            //their isn't any given file name
            DateFormat f = new SimpleDateFormat("dd_MM_yy_HH_mm_ss"); //a new Date Format
            Date date = new Date(); //current date
            fileName = "output_" + f.format(date); //the file name
        } else {
            //their is a given file name
            fileName = inputs.get(index);
        }
        request.setFileForSaving(fileName);  //set the file for saving
    }

    /**
     * Sets the request Headers by calling the setHeaders method of the request
     */
    private void setHeaders(){
        int index;
        if(inputs.contains("-H")){
            index=inputs.indexOf("-H");
        }
        else{
            index=inputs.indexOf("--headers");
        }
        index++;
        if(inputs.size()==index){
            //their isn't any data after the --headers/-H
            System.err.println("Incorrect input"); //an error message
            System.exit(1);//exit the program
        }
        String[] keysAndValues=inputs.get(index).split(";"); //split the given headers to keys and values
        request.setHeaders(keysAndValues); //call the setHeaders method for the request
    }

    /**
     * Sets the request body by calling the setData method for the request and set the data type by calling
     * the setDataType method for the request.
     */
    private void setData(){

        if(inputs.contains("-d")||inputs.contains("--data") || inputs.contains("--upload") || inputs.contains("--json") || inputs.contains("-j") || inputs.contains("-urlencoded")){
            int index=0;
            if(inputs.contains("-d")){
                index=inputs.indexOf("-d");
                request.setDataType(DataType.multipartFormData); //set the data type to Multipart Form Data
            }
            else if(inputs.contains("--data")){
                index=inputs.indexOf("--data");
                request.setDataType(DataType.multipartFormData); //set the data type to Multipart Form Data
            }
            else if(inputs.contains("--upload")){
                index=inputs.indexOf("--upload");
                request.setDataType(DataType.Binary); //set the data type to Binary
            }
            else if(inputs.contains("--json")){
                index=inputs.indexOf("--json");
                request.setDataType(DataType.JSON); //set the data type to JSON
            }
            else if(inputs.contains("-j")){
                index=inputs.indexOf("-j");
                request.setDataType(DataType.JSON); //set the data type to JSON
            }
            else if(inputs.contains("-urlencoded")){
                index=inputs.indexOf("-urlencoded");
                request.setDataType(DataType.fromUrlEncoded); //set the data type tp form url encoded
            }
            index++;
            if(inputs.size()==index){
                //their isn't any given data after the arguments
                System.err.println("Empty data"); //an error message
                System.exit(1);//exit the program
            }
            request.setData(inputs.get(index)); //set the request data
        }
    }

    /**
     * Sets the saved name of the request
     */
    private void setSavedName() {
        int index;
        if(inputs.contains("-S")){
            index=inputs.indexOf("-S");
        }
        else{
            index=inputs.indexOf("--save");
        }
        index++;
        if(index==inputs.size() || inputs.get(index).contains("-")){
            //set a default save name
            request.setSavedName("My request");
        }
        else{
            //set the saved name
            request.setSavedName(inputs.get(index));
        }
    }


    /**
     * Saves the request in a file by using object serialization
     */
    public static void saveRequest(Request request){
        File file=new File("savedRequests");
        boolean append=file.exists(); //check if the file exists or not
        if(append){
            //the file exists
            ArrayList<Request>requests=listSavedRequests(); //Call the listSavedRequests method for list of the saved requests
            try (ObjectOutputStream os1 = new ObjectOutputStream(new FileOutputStream(file))) {
                for(Request r:requests) {
                    //rewrite the requests in the file
                    os1.writeObject(r);
                }
                os1.writeObject(request); //write the current request in the file
            } catch (IOException e) {
                System.err.println(e.getMessage()); //an error message
                System.exit(1); //exit the program
            }
        }
        else {
            //the file doesn't exist
            try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file))) {
                os.writeObject(request); //write the current request in the file
            } catch (IOException e) {
                System.err.println(e.getMessage()); //an error message
                System.exit(1); //exit the program
            }
        }

    }

    /**
     * It reads the savedRequests file and create an ArrayList of saved requests
     * @return an ArrayList of saved requests
     */
    public static ArrayList<Request> listSavedRequests(){
        ArrayList<Request> requests=new ArrayList<>(); //an ArrayList of requests
        try(ObjectInputStream os=new ObjectInputStream(new FileInputStream("savedRequests"))){
            while(true){
                try {
                    requests.add((Request) os.readObject()); //add the request to the list
                }catch (EOFException ex){
                    //reach the end of file
                    break;
                }
            }
        }catch (FileNotFoundException ex){
            //their isn't any saved requests!
            System.out.println("No saved request!");
            System.exit(0); //exit the program
        }
        catch (IOException  | ClassNotFoundException e){
            System.err.println(e.getMessage()); //an error message
            System.exit(1); //exit the program
        }
        return requests; //return the requests ArrayList
    }

    /**
     * Fires some requests
     */
    private void fire() {
        int index=inputs.indexOf("fire");
        index++;
        if(index==inputs.size()){
            //invalid input
            System.err.println("Invalid input");
            System.exit(1);//exit the program
        }
        while (index<inputs.size()) {
            try {
                request = listSavedRequests().get(Integer.parseInt(inputs.get(index))-1); //get the selected request from the requests list
                sendRequest(); //send the request
            }catch (NumberFormatException | IndexOutOfBoundsException e){
                System.err.println("\nIncorrect input!"); //an error message
                System.exit(1);//exit the program
            }
            System.out.println(); //an empty line
            System.out.println("--------------------------");
            System.out.println(); //an empty line
            index++;
        }//end of while

    }//end of fire method


}//end of Client class
