import com.box.sdk.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Handler;
import java.util.concurrent.Looper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoxSync {

  private static final String CLIENT_ID = "YOUR_CLIENT_ID";
  private static final String CLIENT_SECRET = "YOUR_CLIENT_SECRET";
  private static final String REDIRECT_URI = "YOUR_REDIRECT_URI";
  private static final String AUTH_URL = "https://account.box.com/api/oauth2/authorize?client_id=" + CLIENT_ID + "&response_type=code&redirect_uri=" + REDIRECT_URI;
  private static BoxDeveloperEditionAPIConnection api;
  private static JFileChooser localDirectoryChooser;
  private static JFileChooser boxDirectoryChooser;
  private static JButton syncButton;

  public static void main(String[] args) {
    JFrame frame = new JFrame("Box Sync");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);

    JLabel localDirectoryLabel = new JLabel("Local Directory:");
    JLabel boxDirectoryLabel = new JLabel("Box Directory:");
    localDirectoryChooser = new JFileChooser();
    boxDirectoryChooser = new JFileChooser();
    syncButton = new JButton("Sync");

    localDirectoryLabel.setBounds(50, 50, 100, 25);
    localDirectoryChooser.setBounds(160, 50, 400, 25);
    boxDirectoryLabel.setBounds(50, 100, 100, 25);
    boxDirectoryChooser.setBounds(160, 100, 400, 25);
    syncButton.setBounds(50, 150, 100, 25);

    frame.add(localDirectoryLabel);
    frame.add(localDirectoryChooser);
    frame.add(boxDirectoryLabel);
    frame.add(boxDirectoryChooser);
    frame.add(syncButton);

    syncButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        File localDirectory = localDirectoryChooser.getSelectedFile();
        File boxDirectory = boxDirectoryChooser.getSelectedFile();
        if (localDirectory != null && boxDirectory != null) {
          synchronize(localDirectory, boxDirectory);
        }
      }
    });

    frame.setLayout(null);
    frame.setVisible(true);

    JOptionPane.showMessageDialog(frame, "Please authorize this application to access your Box account.", "Box OAuth Authentication", JOptionPane.INFORMATION_MESSAGE, null);
    Desktop.getDesktop().browse(java.net.URI.create(AUTH_URL));

    String authorizationCode = JOptionPane.showInputDialog(frame, "Enter the authorization code:");
    BoxConfig boxConfig = new BoxConfig(CLIENT_ID, CLIENT_SECRET, R
