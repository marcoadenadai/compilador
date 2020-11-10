import VM.VirtualMachine;
import com.formdev.flatlaf.*;
import javax.swing.*;
import GUI.*;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( new FlatDarkLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        //Home.getInstance().setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Interface.getInstance().setVisible(true);
                //Interface.getInstance().setContentPane(new Interface().MainPanel);
            }
        });

    }
}