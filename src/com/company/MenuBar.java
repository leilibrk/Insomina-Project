package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class represents a MenuBar.
 * It extends JMenuBar class.
 * @author Leili
 */
public class MenuBar extends JMenuBar {
    private JMenu applicationMenu; //a JMenu for applications
    private JMenu viewMenu; //a JMenu for view
    private JMenu helpMenu; //a JMenu for help
    private boolean followRedirect; //the followRedirect checkBox's status
    private boolean systemTray; //the system tray checkBox's status
    private JFrame mainFrame; //the main frame
    private RequestListPanel panel1; //the request list panel
    private SendRequestPanel panel2; //the send request panel

    /**
     * Creates a MenuBar with the given frame,a RequestListPanel and a SendRequestPanel
     * @param mainFrame the main frame
     * @param panel1 the request list panel
     * @param panel2 the send request panel
     */
    public MenuBar(JFrame mainFrame,RequestListPanel panel1,SendRequestPanel panel2){
        this.mainFrame=mainFrame;
        this.panel1=panel1;
        this.panel2=panel2;
        try {
            setFollowRedirectAndSystemTray(); //set the followRedirect and systemTray's status
        } catch (IOException e) {
            System.err.println (e.getMessage ()); //an error message
        }
        createApplicationMenu(); //create the application menu
        createViewMenu(); //create the view menu
        createHelpMenu(); //create the help menu
        //add the Menus to the JMenuBar
        this.add(applicationMenu);
        this.add(viewMenu);
        this.add(helpMenu);
        panel2.setFollowRedirect(followRedirect); //set the panel2's followRedirect status
    }

    /**
     * Creates the Application Menu
     */
    private void createApplicationMenu(){
        applicationMenu=new JMenu("Application"); //a new menu
        applicationMenu.setMnemonic('A'); //set the mnemonic of the menu1
        JMenuItem options=new JMenuItem("Options");  //a menu item
        options.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,KeyEvent.ALT_DOWN_MASK)); //set the accelerator
        //add action listener for options menu item
        options.addActionListener(actionEvent -> {
            JFrame option=new JFrame("Option"); //a new frame
            option.setSize(100,100); //sets the size of it
            option.setLocationRelativeTo(options); //sets the location relative to options menu item
            JPanel optionPanel=new JPanel(); //an new frame
            JCheckBox followRedirectCheck=new JCheckBox("Follow Redirect"); //a new checkbox for follow redirect setting
            if(followRedirect){
                //the followRedirect boolean is true.So,it has been checked before.
                followRedirectCheck.doClick();
            }
            followRedirectCheck.addActionListener(actionEvent1 -> {
                //if the followRedirect is true,it changes it to false and conversely
                followRedirect= !followRedirect;
                try {
                    saveOptionMenu(); //save the new changes to the file
                } catch (IOException e) {
                    //the file cannot be opened/founded
                    System.err.println (e.getMessage ());//an error message
                }
                panel2.setFollowRedirect(followRedirect); //set the panel2's follow redirect status
            });
            JCheckBox systemTrayCheck=new JCheckBox("System Tray"); //a new checkbox for system tray setting
            if(systemTray){
                //the systemTray boolean is true.So,it has been checked before.
                systemTrayCheck.doClick();
            }
            systemTrayCheck.addActionListener(actionEvent12 -> {
                //if the systemTray is true,it changes it to false and conversely
                systemTray= !systemTray;
                if(systemTray){
                    //the program should be hidden on system tray by pressing the close button
                    mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    mainFrame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            hideOnSystemTray();//call hide on system tray's method
                        }
                    });

                }
                else{
                    //the program should be closed completely by pressing the close button
                   mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
                try {
                    //save the new changes to the file
                    saveOptionMenu();
                } catch (IOException e) {
                    //the file cannot be opened/founded
                    System.err.println (e.getMessage ());//an error message
                }
            });
            //add the checkboxes to the panel
            optionPanel.add(followRedirectCheck);
            optionPanel.add(systemTrayCheck);
            option.setContentPane(optionPanel); //add the panel to the frame
            option.setVisible(true);//set the frame visible
        });
        JMenuItem exit=new JMenuItem("Exit");  //a new menu item
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,KeyEvent.ALT_DOWN_MASK)); //set the accelerator
        //add the action listener for the exit menu item
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(systemTray){
                    //the program should be hidden on system tray by pressing the close button
                    setVisible(false);
                    hideOnSystemTray();
                }
                else{
                    //the program should be closed completely by pressing the close button
                    System.exit(0);
                    System.out.println("Exit performed");
                }
                try{
                    //save the new changes to the file
                    saveOptionMenu();
                }
                catch (IOException e){
                    //the file cannot be opened/founded
                    System.err.println (e.getMessage ());//an error message

                }
            }

        });
        applicationMenu.add(options); //add the menu
        applicationMenu.add(exit); //add the menu
    }

    /**
     * Creates the view menu
     */
    private void createViewMenu(){
        viewMenu=new JMenu("View"); //a new menu
        viewMenu.setMnemonic('V');//set the mnemonic
        JMenuItem fullScreen=new JMenuItem("Toggle Full Screen"); //a new menu item
        fullScreen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,KeyEvent.ALT_DOWN_MASK)); //set the accelerator
        //add action listener for fullScreen item
        fullScreen.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed (ActionEvent actionEvent) {
                if (mainFrame.getExtendedState () == JFrame.MAXIMIZED_BOTH) {
                    //the frame is maximized
                    mainFrame.setSize (20000, 600);
                } else {
                    //the frame is not maximized
                    mainFrame.setExtendedState (JFrame.MAXIMIZED_BOTH);
                }

            }
        });
        JMenuItem sidebar=new JMenuItem("Toggle Sidebar");// a new menu item
        sidebar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.ALT_DOWN_MASK));//set the accelerator
        //add action listener for sidebar item
        sidebar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(panel1.isVisible()){
                    //panel1(requests panel) is visible
                    panel1.setVisible(false); //makes it not visible
                }
                else{
                    //panel1(requests panel) in not visible
                    panel1.setVisible(true); //makes it visible
                }
            }
        });
        viewMenu.add(fullScreen); //add the menu item
        viewMenu.add(sidebar); //add the menu item
    }

    /**
     * Creates the help menu
     */
    private void createHelpMenu(){
        helpMenu=new JMenu("Help"); //a new menu
        helpMenu.setMnemonic('H'); //set the mnemonic
        JMenuItem about=new JMenuItem("About"); //a new menu item
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,KeyEvent.SHIFT_DOWN_MASK)); //set the accelerator
        //add action listener for about menu item
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame info=new JFrame("About"); //a new frame
                info.setLocationRelativeTo(null); //set location relative to null
                info.setSize(200,200); //set the frame's size
                JPanel infoPanel=new JPanel(); //a new panel
                BoxLayout b=new BoxLayout(infoPanel,BoxLayout.Y_AXIS);
                infoPanel.setLayout(b); //set the panel's layout
                //some labels
                JLabel title=new JLabel("Developer information:");
                JLabel name=new JLabel("Name: Leili Barekatain");
                JLabel id=new JLabel("Student Id: 9831074");
                JLabel email=new JLabel("Email: barekatinleili@gmail.com");
                //add the labels to the panel
                infoPanel.add(title);
                infoPanel.add(name);
                infoPanel.add(id);
                infoPanel.add(email);
                info.setContentPane(infoPanel);
                info.setVisible(true); //set the panel visible
            }
        });
        JMenuItem help=new JMenuItem("Insomnia Help"); //a new menu item
        help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,KeyEvent.SHIFT_DOWN_MASK)); //set the accelerator
        //add the action listener
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame help=new JFrame("Insomnia Help"); //a new frame
                help.setSize(650,300);// sets the size
                help.setLocationRelativeTo(null); //sets the location relative to null
                JTextArea textArea=new JTextArea(); //text area
                textArea.setEditable(false);//set editable false
                help(textArea);//call the help method
                help.add(textArea); //add the text area to the frame
                help.setVisible(true); //set the frame visible
            }
        });
        helpMenu.add(about);//add the about item to menu3
        helpMenu.add(help);//add the help item to menu3
    }

    /**
     * Adds some information about the program to the given text area
     * @param textArea a JTextArea for adding the information
     */
    private void help(JTextArea textArea) {
        String s="  To create a new request, click the plus icon at the top of the left panel\n" +
                "  Set the URL and click the Send button beside it to execute the request.\n"+
                "  Once the request completes, the details will be visible in the right panel.\n" +
                "  Here, you will see status code, duration time, size, the response body and the headers in the tabs below\n"+
                "  To set the request body,choose your request body from the Body combo box of the body tab and enter the details\n"+
                "  To set the request headers,click on the Header tab of the middle panel and enter the keys and values\n"+
                "  To set the request Query,click on the Query tab of the middle tab and enter the keys and values\n"+
                "  If you want to save your request,click the Save button next to the Send button and then\n"+
                "  a new frame will be opened and you should enter a name for your request and press Save\n"+
                "  By pressing the Save button, the request will be added to the request list in the left panel\n"+
                "  By pressing a request name in the left panel, the details will be shown in the middle panel\n"+
                "  For more details visit https://support.insomnia.rest/article/11-getting-started website\n";
        textArea.setText(s);
    }

    /**
     * Sets the follow redirect and system tray's status.
     * It reads a file called "savedData.txt".In the file there are two ints which are 0 or 1.
     * The first one represents the status of followRedirect's checkbox and the second one represents the status of systemTray's checkbox.
     * For example : If the first one is 1 -> the follow redirect check box is checked,So it makes the followRedirect boolean true.
     * Else it is unchecked,So it makes the followRedirect boolean false.
     * And the same for the system Tray.
     * @throws IOException if the file cannot be opened or cannot be read.
     */
    private void setFollowRedirectAndSystemTray() throws IOException {
        FileReader fileReader = new FileReader("./savedData.txt");
        try {
            if(fileReader.read()-48==0){
                //follow redirect checkbox is unchecked
                followRedirect=false;
            }
            else{
                //follow redirect checkbox is checked
                followRedirect=true;
            }
            if(fileReader.read()-48==0){
                //system tray checkbox is unchecked
                systemTray=false;
            }
            else{
                //system tray checkbox is checked
                systemTray=true;
            }
        } catch (IOException e) {
            System.err.println (e.getMessage ()); //an error message
        }
        if(systemTray){
            //the program should be hidden on system tray by pressing close button
            mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            mainFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    hideOnSystemTray(); //call the hide on system tray's method
                }
            });
        }
        else{
            //the program should be closed by pressing close button
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        fileReader.close(); //close the file
    }

    /**
     * Saves the option menu to a file(savedData.txt)
     * It puts two ints(0 or 1) representing the status of the follow redirect and system Tray check box.
     * For example: if the follow redirect checkbox is checked-> the followRedirect boolean is true -> it writes an 1 in the file.
     * @throws IOException if the file cannot be opened or founded or we cannot write in it
     */
    private void saveOptionMenu() throws IOException {
        FileWriter fileWriter=new FileWriter("./savedData.txt");
        if(followRedirect){
            //the follow Redirect's checkbox is checked
            fileWriter.write("1");
        }
        else{
            //the follow Redirect's checkbox is unchecked
            fileWriter.write("0");
        }
        if(systemTray){
            //the system tray's checkbox is checked
            fileWriter.write("1");
        }
        else{
            //the system tray's checkbox is unchecked
            fileWriter.write("0");
        }

        fileWriter.close(); //close the file
    }

    /**
     * Hides the program on system tray,when the program is closed.
     */
    private void hideOnSystemTray(){
        if(SystemTray.isSupported()){
            //system tray is supported
            SystemTray systemTray=SystemTray.getSystemTray();//a systemTray
            Image image = Toolkit.getDefaultToolkit().getImage("./insomniaIcon.png"); //an image for the icon of the program
            PopupMenu menu=new PopupMenu();//a popup menu
            MenuItem Exit=new MenuItem("EXIT");//a menu item
            Exit.addActionListener(new ActionListener() { //add actionListener for the exit item
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    //exit the program
                    System.exit(0);
                }
            });
            menu.add(Exit);//add the item to the menu
            TrayIcon trayIcon=new TrayIcon(image,"Insomnia",menu);//a tray icon
            trayIcon.setImageAutoSize(true);// make the image auto size
            try {
                //tray icon can be added
                systemTray.add(trayIcon);
            } catch (AWTException e) {
                //tray icon cannot be added
                System.err.println("TrayIcon could not be added.");
            }
        }
        else{
            //system tray is not supported
            System.out.println("System tray not supported");
            System.exit(0);
        }
    }
}
