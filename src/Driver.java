import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Driver extends JFrame implements ActionListener{

    Panel panel = new Panel();
    public static JTextArea textBox = new JTextArea(30, 10);
    JScrollPane scroller = new JScrollPane(textBox);
    boolean maximized = false;

    // ------------------------------ Menu Items ------------------------------------------
    JMenuItem quitItem = new JMenuItem("Exit");
    JMenuItem viewItem = new JMenuItem("Toggle Fullscreen");
    JMenuItem  helpItem= new JMenuItem ("Help & Learning");
    JMenuItem  aboutItem = new JMenuItem ("About...");
    JMenuItem settingsItem = new JMenuItem ("Settings");
    JMenuItem hardCore = new JRadioButtonMenuItem("Hardcore");
    JMenuItem casualMode = new JRadioButtonMenuItem("Casual");
    // -------------------------------------------------------------------------------------

    public Driver (String title){
        super(title);

        //=========================================================== Menus and MenuBar ===================================================================
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Application");
        JMenu viewMenu = new JMenu("View");
        JMenu settingsMenu = new JMenu("Settings");
        JMenu helpMenu = new JMenu("Help");
        //=============================================================== Hotkeys ====================================================================
        fileMenu.setMnemonic('A');
        viewMenu.setMnemonic('V');
        settingsMenu.setMnemonic('S');
        helpMenu.setMnemonic('H');
        //============================================================= Action Listeners =========================================================================

        quitItem.addActionListener(this);
        viewItem.addActionListener(this);
        helpItem.addActionListener(this);
        aboutItem.addActionListener(this);
        settingsItem.addActionListener(this);
        casualMode.addActionListener(this);
        hardCore.addActionListener(this);

        ButtonGroup saveSetting = new ButtonGroup();
        saveSetting.add(casualMode);
        saveSetting.add(hardCore);


        //=============================================================== Adding Items to Menus ==================================================================
        fileMenu.add(quitItem);
        viewMenu.add(viewItem);
        helpMenu.add(helpItem);
        helpMenu.add(aboutItem);
        settingsMenu.add(casualMode);
        settingsMenu.add(hardCore);
        casualMode.setSelected(true);
        //============================================================== Adding Menus to MenuBar ====================================================================
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(settingsMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        this.setPreferredSize(new Dimension(800, 800));
        this.getContentPane().add(panel, BorderLayout.NORTH);
        this.add(scroller, BorderLayout.CENTER);
        this.setBackground(Color.WHITE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    //--------------------------------------------------------------------- Menu Items are Clicked --------------------------------------------------------------------------------------------------------------

    public void actionPerformed(ActionEvent event) {      // call a method depending on what button is clicked

        if (event.getSource() == quitItem){
            quitClicked();
        }
        else if (event.getSource() == viewItem){
            fullscreen();
        }
        else if (event.getSource() == helpItem) {
            helpClicked();
        }
        else if (event.getSource() == aboutItem){
            aboutClicked();
        }
        else if (event.getSource() == casualMode){
            setCasual();
        }
        else if (event.getSource() == hardCore){
            setHardCore();
        }
    }
    //============================================================== Menu Items Clicked (Methods) ====================================================================================

    public void quitClicked() { // close application
        System.exit(0);
    }

    public void fullscreen(){
        if (!maximized){
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.setVisible(true);
        } else if (maximized) {
            this.pack();
        }
        maximized = !maximized;
    }

    public void helpClicked(){   // Help button clicked
        panel.showHelp();
    }

    public void aboutClicked(){ // About... button clicked
        panel.showAbout();
    }

    public void setCasual(){
        panel.setCasual();
    }

    public void setHardCore(){
        panel.setHardcore();
    }


    public static void main (String [] args){
        Driver frame = new Driver("The Counterfeit Coin Problem");
    }
}
