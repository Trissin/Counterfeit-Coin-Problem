import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Panel extends JPanel implements MouseListener, MouseMotionListener {
    Random rng = new Random();
    Coin[] allCoins = new Coin[12];
    private int lWeight;           // weight of left side of scale
    private int rWeight;           // weight of right side of scale
    int mouseXpos;
    int mouseYpos;
    int numLeft = 0;
    int numRight = 0;
    boolean gameOver = false;
    boolean victory = false;
    boolean hardcoreMode = false;
    boolean returnValue = false;
    boolean cGOdisplayed = false;
    boolean gFakenotWeight = false;
    boolean fakeGuessCancelled = false;
    boolean restarted = false;
    public int guessCounter = 0;
    private int selectionHolder = -1;
    private int fakeSelector = -1;
    Scale scale = new Scale();

    public Panel() {
        super.addMouseListener(this);
        super.addMouseMotionListener(this);

        this.setPreferredSize(new Dimension(800, 600)); // set panel size
        this.setBackground(Color.WHITE);

        JButton measureButton = new JButton("Measure");
        measureButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent useScale) {
                measure();
            }
        });
        this.add(measureButton);

        JButton emptyLbutton = new JButton("Empty Left Side");
        emptyLbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent emptyLeft) {
                emptyLeft();
            }
        });
        this.add(emptyLbutton);

        JButton emptyRbutton = new JButton("Empty Right Side");
        emptyRbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent emptyRight) {
                emptyRight();
            }
        });
        this.add(emptyRbutton);

        JButton emptyAbutton = new JButton("Empty Both Sides");
        emptyAbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent emptyBoth) {
                emptyAll();
            }
        });
        this.add(emptyAbutton);

        JButton gFakeButton = new JButton("Guess!");
        gFakeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent guessFake) {
                checkFake();
            }
        });
        this.add(gFakeButton);

        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent reset) {
                initialize();
                Driver.textBox.append("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nGAME RESET!");
            }
        });
        this.add(restartButton);

        initialize();
    }

    public void initialize() {
        numLeft = 0;
        numRight = 0;
        guessCounter = 0;
        selectionHolder = -1;
        fakeSelector = -1;
        cGOdisplayed = false;
        victory = false;
        returnValue = false;
        gFakenotWeight = false;
        restarted = false;

        fakeSelector = rng.nextInt(12);  // randomly select a number from 0-12
        for (int i = 0; i < allCoins.length; i++) {  // loop thru all coins
            allCoins[i] = new Coin(0, 0); // initialize (NEED THIS LINE)
            allCoins[i].setLabel(i); // label 0-12
            if (i == fakeSelector) {  // if randomly selected fake
                allCoins[i].setFake();  // set fake
            }
            allCoins[i].adjustWeight(); // set weights
        }
        scale.resetScale();
        this.repaint();
    }

    public void setCasual() {
        initialize();
        hardcoreMode = false;
        Driver.textBox.append("\nGame is now set to \"Casual\" mode.");
    }

    public void setHardcore() {
        initialize();
        hardcoreMode = true;
        Driver.textBox.append("\nGame is now set to \"Hardcore\" mode.");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseXpos = e.getPoint().getLocation().x;
        mouseYpos = e.getPoint().getLocation().y;
        checkClickLocation();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    private void checkClickLocation() {
        // Clicked a coin
        int coinCounter = 0;
        for (int i = 20; i < 140; i += 70) {
            for (int j = 80; j < 480; j += 70) {
                if (i <= mouseXpos && mouseXpos <= i + 50 && j <= mouseYpos && mouseYpos <= j + 50) {
                    if (!allCoins[coinCounter].onScale()) {
                        allCoins[coinCounter].select();
                        this.repaint();
                    } else {
                        if (allCoins[coinCounter].onLeft) {
                            Driver.textBox.append("\nCoin " + coinCounter + " is already on the left side of the scale.");
                        } else {
                            Driver.textBox.append("\nCoin " + coinCounter + " is already on the right side of the scale.");
                        }

                    }

                }
                coinCounter++;
            }
        }
        // Clicked Scale
        if (190 <= mouseXpos && mouseXpos <= 340 && 230 <= mouseYpos && mouseYpos <= 450) {
            for (int i = 0; i < 12; i++) {
                if (allCoins[i].isSelected()) {
                    allCoins[i].placeLeft();
                    numLeft++;
                }
            }
            this.repaint();
        } else if (490 <= mouseXpos && mouseXpos <= 640 && 230 <= mouseYpos && mouseYpos <= 450) {
            for (int i = 0; i < 12; i++) {
                if (allCoins[i].isSelected()) {
                    allCoins[i].placeRight();
                    numRight++;
                }
            }
            this.repaint();
        }
    }

    public void measure() {
        lWeight = 0;
        rWeight = 0;
        for (int i = 0; i < 12; i++) {
            if (allCoins[i].onLeft) {
                lWeight += allCoins[i].getWeight();
            } else if (allCoins[i].onRight) {
                rWeight += allCoins[i].getWeight();
            } else if (!allCoins[i].onScale()) {
                // the coins that are not on the scale
            }
        }
        scale.compare(lWeight, rWeight);
        this.repaint();
        if (scale.getNumUses() == 4){
            victory = false;
            gFakenotWeight = false;
            gameOver();
        }
    }

    public void emptyLeft() {
        for (int i = 0; i < 12; i++) {
            if (allCoins[i].onLeft) {
                allCoins[i].takeOffLeft();
            }
        }
        numLeft = 0;
        scale.setBalanced();
        this.repaint();
    }

    public void emptyRight() {
        for (int i = 0; i < 12; i++) {
            if (allCoins[i].onRight) {
                allCoins[i].takeOffRight();
            }
        }
        numRight = 0;
        scale.setBalanced();
        this.repaint();
    }

    public void emptyAll() {
        for (int i = 0; i < 12; i++) {
            if (allCoins[i].onScale()) {
                allCoins[i].takeOffScale();
            }
        }
        numLeft = 0;
        numRight = 0;
        scale.setBalanced();
        this.repaint();
    }

    public void checkFake() {
        int numSelected = 0;
        selectionHolder = -1;
        for (int i = 0; i < 12; i++) {
            if (allCoins[i].isSelected()) {
                numSelected++;
                selectionHolder = i;
            }
        }
        if (numSelected == 0) {
            Driver.textBox.append("\nYou have not selected a coin to guess!");
        } else if (numSelected == 1) {
            if (selectionHolder < 0) {
                Driver.textBox.append("\nERROR, SELECTEDHOLDER VARIABLE IS NOT FUNCTIONING PROPERLY! Resetting to 0.");
                selectionHolder = 0;
            }

            if (allCoins[selectionHolder].getValidity()) {
                Driver.textBox.append("\nThis isn't the fake coin!");
                victory = false;

            } else if (!allCoins[selectionHolder].getValidity()) {
                if (inspectFake()) {
                    victory = true;
                }
                else{
                    victory = false;
                    gFakenotWeight = true;
                }
            }
            if (!fakeGuessCancelled){
                guessCounter++;
                gameOver();
                if (!restarted){
                    if (guessCounter == 1) {
                        Driver.textBox.append("\nYou have now guessed 1 time.");
                    } else {
                        Driver.textBox.append("\nYou have now guessed " + guessCounter + " times.");
                    }
                }
                else{
                    restarted = false;
                }
            }
            else{
                fakeGuessCancelled = false;
            }
        } else {
            Driver.textBox.append("\nYou have selected more than one coin!");
        }
    }

    private boolean fakeHeavyCheck() {
        boolean heavier = true;
        if (allCoins[selectionHolder].getWeight() == 2) {
            Driver.textBox.append("\nERROR, FAKE COIN CANNOT BE REGULAR WEIGHT. PLEASE RESTART APPLICATION.");
            heavier = false;
        } else if (allCoins[selectionHolder].getWeight() < 2) {
            heavier = false;
        }
        return heavier;
    }

    public void showHelp() {
        JDialog helpPane = new JDialog(); // JDialog holds the option pane
        // Help & Learning Output to User
        JOptionPane.showMessageDialog(helpPane, "You're the realm's greatest mathematician, and one day, you're suddenly brought " +
                "before the Emperor of the realm for advice.\nOne of his twelve governors has been convicted of paying his taxes with a " +
                "counterfeit coin, which has already made its way into the treasury.\nAs the kingdom's greatest mathematician, you've been " +
                "granted a chance to prove yourself by identifying the fake.\nBefore you are twelve identical looking coins, and a balance " +
                "scale.\nYou know that the false coin will be very slightly lighter, or heavier, than the rest.\nUnfortunately, the Emperor " +
                "is not a patient man.\nYou may only use the scale three times, or you'll be thrown into the dungeon.\nYou look around for " +
                "anything else you can use, but there's nothing in the room - just the coins and the scale.\n\nHow do you identify the " +
                "counterfeit?\n\nIn casual mode, you can use the scale as much as you'd like, and guess as many times as you want.\n" +
                "In hardcore mode, you can only use the scale 3 times, and you may only guess once. If you fail, you are forced to restart.");
    }

    public void showAbout() {
        JDialog aboutPane = new JDialog(); // JDialog holds the message pane
        // Output to User
        JOptionPane.showMessageDialog(aboutPane, "\"The Counterfeit Coin Riddle\", Lesson by Jennifer Lu.\nView the original source here:\n" +
                "https://ed.ted.com/lessons/can-you-solve-the-counterfeit-coin-riddle-jennifer-lu\n\nMeet the Creators\nEducator: Jennifer Lu" +
                "\nDirector: Outis\nScript Editor: Alex Gendler\nAssociate Producer: Jessica Ruby\nContent Producer: Gerta Xhelo\n" +
                "Editorial Producer: Alex Rosenthal\nNarrator: Addison Anderson\n\n\nThis application \"The Counterfeit Coin Problem\" created by James Pan.");
    }

    public void gameOver() {
        if (victory) {
            if (hardcoreMode){
                Driver.textBox.append("\nCongratulations! You have successfully solved the Counterfeit Coin Problem, with no errors, on Hardcore mode!");
                String winnerName = JOptionPane.showInputDialog(null, "Congratulation on solving the Counterfeit Coin Riddle! Please enter your name below.");
                Driver.textBox.append("\n Congratulations once again, " + winnerName + "!");
            }
            else {
                Driver.textBox.append("\nCongratulations, you have successfully identified the fake coin and its weight on Casual mode! When you're ready, try \"Hardcore\"!");
                cGOdisplayed = false;
            }
        } else if (!victory) {
            if (hardcoreMode) {
                JDialog forceRestart = new JDialog();  // this JDialog holds the JOptionPane
                JButton restartButton = new JButton("Restart");
                restartButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent restartClicked) {
                        forceRestart.dispose(); // close the popup
                    }
                });
                JButton[] options3 = {restartButton}; // JButton array holding previous buttons
                JOptionPane.showOptionDialog(forceRestart, "GAME OVER! In hardcore mode, you must restart the game.", // JOptionPane
                        "Game Over!", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options3, options3[0]);
                initialize();
                Driver.textBox.append("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nGAME RESET!");
                restarted = true;

            } else {
                if (gFakenotWeight){
                    Driver.textBox.append("\nIncorrect - you the fake coin, but not its weight. Click \"Restart\" to try again!");
                    cGOdisplayed = false;
                }
            }
        }

        if (!hardcoreMode && !cGOdisplayed){
            JDialog restartPrompt = new JDialog();  // this JDialog holds the JOptionPane

            JButton restartButton = new JButton("Restart");
            restartButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent restartClicked) {
                    initialize();
                    Driver.textBox.append("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nGAME RESET!");
                    restartPrompt.dispose(); // close the popup
                }
            });

            JButton continueButton = new JButton("Continue Playing");
            continueButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent continueClicked) {
                    gameOver = false;
                    restartPrompt.dispose(); // close the popup
                }
            });

            JButton[] options2 = {restartButton, continueButton}; // JButton array holding previous buttons

            JOptionPane.showOptionDialog(restartPrompt, "The game is now finished - would you like to restart or keep playing?", // JOptionPane
                    "Game Finished", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options2, options2[0]);
            restartPrompt.dispose(); // automatically close this dialog
        }
        cGOdisplayed = true;
    }

    private boolean inspectFake() {
        JDialog optionHolder = new JDialog();  // this JDialog holds the JOptionPane

        JButton gHeavy = new JButton("Heavier");
        gHeavy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent guessHeavier) {
                if (fakeHeavyCheck()) {
                    returnValue = true;
                } else if (!fakeHeavyCheck()) {
                    returnValue = false;
                }
                optionHolder.dispose(); // close the popup
            }
        });

        JButton gLight = new JButton("Lighter");
        gLight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent guessLighter) {
                if (!fakeHeavyCheck()) {
                    returnValue = true;
                } else if (fakeHeavyCheck()) {
                    returnValue = false;
                }
                optionHolder.dispose(); // close the popup
            }
        });

        JButton cancelButton = new JButton("Cancel");  // cancel
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent cancel) {
                optionHolder.dispose(); // close the popup
                fakeGuessCancelled = true;
            }
        });

        JButton[] options = {gHeavy, gLight, cancelButton}; // JButton array holding previous 3 buttons

        JOptionPane.showOptionDialog(optionHolder, "Is the fake coin heavier or lighter than normal?", // JOptionPane
                "Check the Fake", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        optionHolder.dispose(); // automatically close this dialog
        return returnValue;
    }



    public void paintComponent(Graphics g){ // draw method to visually show the frame
        super.paintComponent(g);
        scale.paint(g);
        int coinCount = 0;
        int xPos = 20;
        for (int i = 0; i < 2; i++){
            int yPos = 80;
            for (int j = 0; j < 6; j++){
                allCoins[coinCount].setXpos(xPos);
                allCoins[coinCount].setYpos(yPos);
                allCoins[coinCount].paint(g);
                yPos += 70;
                coinCount++;
            }
            xPos += 70;
        }
        Color leftCoin = new Color(255, 0, 0, 200);
        Color rightCoin = new Color(0, 0, 255, 200);
        Color coinColor = new Color(218, 165, 32);

        if (scale.balanced){
            g.setColor(leftCoin);
            g.drawRect(190,230,150, 220);
            g.setColor(rightCoin);
            g.drawRect(490,230,150, 220);

            g.setColor(coinColor);
            for (int i = 0; i < numLeft; i++){
                g.fillRect(236,400-(14*i),50,10);
            }
            for (int i = 0; i < numRight; i++){
                g.fillRect(536,400-(14*i),50,10);
            }
        }
        else if (scale.lHeavy){
            g.setColor(leftCoin);
            g.drawRect(190,285,150, 220);
            g.setColor(rightCoin);
            g.drawRect(490,155,150, 220);

            g.setColor(coinColor);
            for (int i = 0; i < numLeft; i++){
                g.fillRect(236,455-(14*i),50,10);
            }
            for (int i = 0; i < numRight; i++){
                g.fillRect(536,325-(14*i),50,10);
            }
        }
        else if (scale.rHeavy){
            g.setColor(leftCoin);
            g.drawRect(190,155,150, 220);
            g.setColor(rightCoin);
            g.drawRect(490,285,150, 220);

            g.setColor(coinColor);
            for (int i = 0; i < numLeft; i++){
                g.fillRect(236,325-(14*i),50,10);
            }
            for (int i = 0; i < numRight; i++){
                g.fillRect(536,455-(14*i),50,10);
            }
        }

    }

}
