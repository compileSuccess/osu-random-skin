import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.io.*;

// this program randomly selects a skin in the Skins folder, modifies the configuration file, and launches osu
// this way, if you want to play with a random skin each time you launch osu, you just need to run this program

public class RandomSkin {
    private static String username;

    private static String getRandomSkin() {
        File folder = new File("C:\\Users\\" + username + "\\AppData\\Local\\osu!\\Skins");
        File[] skins = folder.listFiles();
        if (skins == null || skins.length == 0) {
            JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, "No skins detected in the default Skins folder (%AppData%\\Local\\osu!\\Skins)", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        int index = (int) (Math.random() * skins.length);
        return skins[index].getName();
    }

    public static void main(String[] args) {
        username = System.getenv("username");
        try {
            BufferedReader file = new BufferedReader(new FileReader("C:\\Users\\" + username + "\\AppData\\Local\\osu!\\osu!." + username + ".cfg"));
            StringBuilder inputBuffer = new StringBuilder();
            String line = file.readLine();
            // parse until the skin configuration line
            while (line.length() < 7 || !line.substring(0,7).equals("Skin = ")) {
                inputBuffer.append(line);
                inputBuffer.append('\n');
                line = file.readLine();
            }
            // replace the skin configuration line with new random skin
            String newSkin = "Skin = " + getRandomSkin() + "\n";
            inputBuffer.append(newSkin);
            // parse the rest of the file
            while ((line = file.readLine()) != null) {
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            file.close();
            // rewrite the entire file with the parsed stream and replaced skin
            FileOutputStream fileOut = new FileOutputStream("C:\\Users\\" + username + "\\AppData\\Local\\osu!\\osu!." + username + ".cfg");
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
            // launch osu
            new ProcessBuilder("C:\\Users\\" + username + "\\AppData\\Local\\osu!\\osu!.exe").start();
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
    }
}
