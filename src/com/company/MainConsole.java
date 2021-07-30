package com.company;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This program represents an HTTP client
 * @author Leili
 */
public class MainConsole {
    public static void main(String[] args){
        ArrayList<String>inputs=new ArrayList<>(); //a new ArrayList for inputs
        inputs.addAll(Arrays.asList(args)); //add all the arguments
        Client httpClient = new Client(inputs); //create a new client with the inputs

    }
}
