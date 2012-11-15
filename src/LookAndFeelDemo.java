import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class LookAndFeelDemo implements ActionListener {
    private static String labelPrefix = "Number of button clicks: ";
    private int numClicks = 0;
    final JLabel label = new JLabel(labelPrefix + "0    ");
    private JTextField input1;
    private JTextField input2;
    private JTextField ans;

    // Specify the look and feel to use by defining the LOOKANDFEEL constant
    // Valid values are: null (use the default), "Metal", "System", "Motif",
    // and "GTK"
    static String LOOKANDFEEL = "System";
    
    // If you choose the Metal L&F, you can also choose a theme.
    // Specify the theme to use by defining the THEME constant
    // Valid values are: "DefaultMetal", "Ocean",  and "Test"
    final static String THEME = "Test";
    
    static Integer SECDELAY = 5;
    private static JFrame theFrame;
    

    public Component createComponents() {
        JButton button = new JButton("Run!");
        button.setMnemonic(KeyEvent.VK_I);
        button.addActionListener(this);
        label.setLabelFor(button);

        JPanel pane   = new JPanel(new GridLayout(0, 1));
        JPanel eqPane = new JPanel();
        eqPane.setLayout(new BoxLayout(eqPane, BoxLayout.X_AXIS));
        this.input1 = new JTextField();
        eqPane.add(this.input1);
        eqPane.add(new JLabel(" x "));
        this.input2 = new JTextField();
        eqPane.add(this.input2);
        eqPane.add(new JLabel(" = "));
        this.ans = new JTextField();
        this.ans.setEditable(false);
        eqPane.add(this.ans);
        
        pane.add(eqPane);
        pane.add(button);
        pane.add(label);
        pane.setBorder(BorderFactory.createEmptyBorder(
                                        30, //top
                                        30, //left
                                        10, //bottom
                                        30) //right
                                        );

        return pane;
    }

    public void actionPerformed(ActionEvent e) {
        numClicks++;
        label.setText(labelPrefix + numClicks);
        try {
            final int milliseconds = SECDELAY * 1000;
            Integer in1 = Integer.valueOf(this.input1.getText());
            Integer in2 = Integer.valueOf(this.input2.getText());
            final int result = in1 * in2;
            final JDialog dlg = new JDialog(theFrame, "Progress Dialog", true);
            final JProgressBar dpb = new JProgressBar(0, milliseconds);
            dpb.setBorder(new EmptyBorder(10, 10, 10, 10));
            dlg.add(BorderLayout.CENTER, dpb);
            JLabel label = new JLabel("Progress...");
            label.setBorder(new EmptyBorder(10, 10, 10, 0));
            dlg.add(BorderLayout.NORTH, label);
            dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            dlg.setSize(300, 90);
            dlg.setLocationRelativeTo(theFrame);
            
            new Thread(new Runnable() {

                @Override
                public void run() {
                    dlg.setVisible(true);
                }
                
            }).start();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(100);

                        int totalTime = 0;
                        
                        int step = 100;
                        while (totalTime < milliseconds) {
                            Thread.sleep(step);
                            dpb.setValue(dpb.getValue() + step);
                            totalTime += step;
                        }
                    } catch (InterruptedException e) {
                        //nothing to do 
                    }
                    dlg.setVisible(false);
                    ans.setText("" + result);
                }
                
            }).start();
            
        } catch (Exception ex) {
            //do nothing...
        }
    }

    private static void initLookAndFeel() {
        String lookAndFeel = null;
       
        if (LOOKANDFEEL != null) {
            if (LOOKANDFEEL.equals("Metal")) {
                lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
              //  an alternative way to set the Metal L&F is to replace the 
              // previous line with:
              // lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";
                
            }
            
            else if (LOOKANDFEEL.equals("System")) {
                lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            } 
            
            else if (LOOKANDFEEL.equals("Motif")) {
                lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            } 
            
            else if (LOOKANDFEEL.equals("GTK")) { 
                lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
            } 
            
            else {
                System.err.println("Unexpected value of LOOKANDFEEL specified: "
                                   + LOOKANDFEEL);
                lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            }

            try {
                
                
                UIManager.setLookAndFeel(lookAndFeel);
                
                // If L&F = "Metal", set the theme
                
                if (LOOKANDFEEL.equals("Metal")) {
                  if (THEME.equals("DefaultMetal"))
                     MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
//                  else if (THEME.equals("Ocean"))
//                     MetalLookAndFeel.setCurrentTheme(new OceanTheme());
//                  else
//                     MetalLookAndFeel.setCurrentTheme(new TestTheme());
                     
                  UIManager.setLookAndFeel(new MetalLookAndFeel()); 
                }   
                    
                    
                  
                
            } 
            
            catch (ClassNotFoundException e) {
                System.err.println("Couldn't find class for specified look and feel:"
                                   + lookAndFeel);
                System.err.println("Did you include the L&F library in the class path?");
                System.err.println("Using the default look and feel.");
            } 
            
            catch (UnsupportedLookAndFeelException e) {
                System.err.println("Can't use the specified look and feel ("
                                   + lookAndFeel
                                   + ") on this platform.");
                System.err.println("Using the default look and feel.");
            } 
            
            catch (Exception e) {
                System.err.println("Couldn't get specified look and feel ("
                                   + lookAndFeel
                                   + "), for some reason.");
                System.err.println("Using the default look and feel.");
                e.printStackTrace();
            }
        }
    }

    private static void createAndShowGUI() {
        //Set the look and feel.
        initLookAndFeel();

        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("Add Two Numbers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LookAndFeelDemo app = new LookAndFeelDemo();
        Component contents = app.createComponents();
        frame.getContentPane().add(contents, BorderLayout.CENTER);

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        
        //Display the window.
        frame.pack();        
        frame.setLocation(screenWidth / 2 - frame.getWidth() / 2, screenHeight / 2 - frame.getHeight() / 2);
        frame.setVisible(true);
        LookAndFeelDemo.theFrame = frame;
    }

    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.equals("old")) {
                LOOKANDFEEL = "Motif";
            } else if (arg.equals("new")) {
                LOOKANDFEEL = "System";
            } else {
                try {
                    SECDELAY = Integer.valueOf(arg);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            
            
        }
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}