import javax.swing.*;
import java.awt.*;

public class Scale extends JPanel{
    private int numUses = 0;
    public boolean lHeavy;
    public boolean rHeavy;
    public boolean balanced;

    ImageIcon bPic = new ImageIcon(new ImageIcon("bScale.png").getImage().getScaledInstance(424, 500, Image.SCALE_SMOOTH));
    ImageIcon lPic = new ImageIcon(new ImageIcon("lScale.png").getImage().getScaledInstance(424, 500, Image.SCALE_SMOOTH));
    ImageIcon rPic = new ImageIcon(new ImageIcon("rScale.png").getImage().getScaledInstance(424, 500, Image.SCALE_SMOOTH));


    JLabel picHolder = new JLabel(bPic, JLabel.CENTER);

    public Scale(){
        setBalanced();
    }

    public void compare(int lWeight, int rWeight){
        lHeavy = false;
        rHeavy = false;
        balanced = false;

        if (lWeight > rWeight){
            lHeavy = true;
            picHolder.setIcon(lPic);
            Driver.textBox.append("\nThe left side of the scale is heavier.");
        }
        else if (lWeight < rWeight){
            rHeavy = true;
            picHolder.setIcon(rPic);
            Driver.textBox.append("\nThe right side of the scale is heavier.");
        }
        else if (lWeight == rWeight){
            balanced = true;
            picHolder.setIcon(bPic);
            Driver.textBox.append("\nBoth sides of the scale are equal in weight.");
        }
        numUses++;
        if (numUses == 1){
            Driver.textBox.append("\nThe scale has been used 1 time.");
        }
        else{
            Driver.textBox.append("\nThe scale has been used " + numUses + " times.");
        }
    }

    public void setBalanced(){
        balanced = true;
        lHeavy = false;
        rHeavy = false;
        picHolder.setIcon(bPic);
    }

    public void resetScale(){
        numUses = 0;
        setBalanced();
    }

    public int getNumUses(){
        return numUses;
    }

    public void paint(Graphics g){
        super.paintComponent(g);
        picHolder.getIcon().paintIcon(this, g, 200, 50);

    }
}
