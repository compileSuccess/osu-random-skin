import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

// RandomSkin but with a GUI
// Add the keyword "Mania" anywhere in your mania skins so that you can specifically choose to play with a mania skin

public class SkinGui extends JPanel implements ActionListener {
    JButton b1,b2,b3,b4;
    JLabel stat;
    int status = 0;

    private static String username;

    public SkinGui() {
        stat = new JLabel("Skin selection mode: Any Skin");

        b1 = new JButton("Any");
        b1.setActionCommand("any");

        b2 = new JButton("Osu");
        b2.setActionCommand("osu");

        b3 = new JButton("Mania");
        b3.setActionCommand("mania");

        b4 = new JButton("Play");
        b4.setActionCommand("play");

        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);

        b1.setToolTipText("Start Osu with a random skin.");
        b2.setToolTipText("Start Osu with an osu! skin.");
        b3.setToolTipText("Start Osu with an osu!mania skin.");
        b4.setToolTipText("Start Osu.");

        add(stat);
        add(b1);
        add(b2);
        add(b3);
        add(b4);
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "any":
                status = 0;
                stat.setText("Skin selection mode: Any Skin");
                break;
            case "osu":
                status = 1;
                stat.setText("Skin selection mode: Osu");
                break;
            case "mania":
                status = 2;
                stat.setText("Skin selection mode: Mania");
                break;
            case "play":
                try {
                    BufferedReader file = new BufferedReader(new FileReader("C:\\Users\\" + username + "\\AppData\\Local\\osu!\\osu!." + username + ".cfg"));
                    StringBuffer inputBuffer = new StringBuffer();
                    String line = file.readLine();

                    while (line.length() < 7 || !line.substring(0,7).equals("Skin = ")) {
                        inputBuffer.append(line);
                        inputBuffer.append('\n');
                        line = file.readLine();
                    }
                    inputBuffer.append("Skin = " + getRandomSkin() + "\n");
                    while ((line = file.readLine()) != null) {
                        inputBuffer.append(line);
                        inputBuffer.append('\n');
                    }
                    file.close();

                    FileOutputStream fileOut = new FileOutputStream("C:\\Users\\" + username + "\\AppData\\Local\\osu!\\osu!." + username + ".cfg");
                    fileOut.write(inputBuffer.toString().getBytes());
                    fileOut.close();

                    new ProcessBuilder("C:\\Users\\" + username + "\\AppData\\Local\\osu!\\osu!.exe").start();
                    System.exit(0);
                } catch (FileNotFoundException ex) {
                    JPanel panel = new JPanel();
                    JOptionPane.showMessageDialog(panel, "Osu! or the configuration file is not installed in the default directory (%AppData%\\Local\\osu!)", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    JPanel panel = new JPanel();
                    JOptionPane.showMessageDialog(panel, "Could not read/write to config file", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JPanel panel = new JPanel();
                    JOptionPane.showMessageDialog(panel, "Something went wrong", "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
        }
    }

    public String getRandomSkin() {
        File folder = new File("C:\\Users\\" + username + "\\AppData\\Local\\osu!\\Skins");
        File[] skins = folder.listFiles();
        if (skins == null || skins.length == 0) {
            JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, "No skins detected in the default Skins folder (%AppData%\\Local\\osu!\\Skins)", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        String selection = "";
        do {
            int index = (int) (Math.random() * skins.length);
            selection = skins[index].getName();
        } while (status != 0 && (status != 1 || selection.contains("Mania")) && (status != 2 || !selection.contains("Mania")));
        //System.out.println(selection);
        return selection;
    }

    public static void main(String[] args) {
        username = System.getenv("username");

        JFrame frame = new JFrame("Skin Randomizer");
        frame.getContentPane().setSize(800,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SkinGui buttons = new SkinGui();
        buttons.setOpaque(true);
        frame.setContentPane(buttons);

        frame.pack();
        frame.setVisible(true);
    }
}
