import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VoteListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * Created by Vinnie on 12/17/14.
 */
public class CommandListener implements VoteListener {
    private Logger log = Logger.getLogger("CommandListener");
    private ArrayList<String> commands = new ArrayList<String>();

    public CommandListener() {
        loadConfig();
    }

    @Override
    public void voteMade(Vote vote) {
        log.info("Received: " + vote);
        for (String command : commands) {
            // Voter's Username
            if(command.contains("{username}")) {
                command = command.replace("{username}", vote.getUsername());
            }
            // Website voted on
            if(command.contains("{serviceName}")) {
                command = command.replace("{serviceName}", vote.getServiceName());
            }
            // Voter's IP Address
            if(command.contains("{address}")) {
                command = command.replace("{address}", vote.getAddress());
            }
            // Time of Vote
            if(command.contains("{timeStamp}")) {
                command = command.replace("{timeStamp}", vote.getTimeStamp());
            }
            // Run command
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }

    private void loadConfig() {
        File configFile = new File("plugins/Votifier/CommandListener.txt");
        String currentLine;
        try {
            BufferedReader br = new BufferedReader(new FileReader(configFile));
            while ((currentLine = br.readLine()) != null) {
                // Ignore comment
                if (currentLine.startsWith("#")) {
                    continue;
                }
                if (currentLine.startsWith("color=")) {
                    String msg = currentLine.substring(6);
                    if (msg != null) {
                        commands.add(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                    continue;
                }
                commands.add(currentLine);
            }
            br.close();
        } catch (FileNotFoundException ex) {
            log.warning("Could not find CommandListener configuration. Creating default.");
            createDefaultConfig(configFile);
        } catch (IOException e) {
            log.severe("Error loading CommandListener configuration.");
        }
    }

    private void createDefaultConfig(File configFile) {
        String default1 = "color=say Thanks &b{username}&r for voting on &9{serviceName}!";
        String default2 = "give {username} diamond 1";
        commands.add(default1);
        commands.add(default2);
        try {
            FileWriter fw = new FileWriter(configFile);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("# CommandListener Configuration");
            bw.newLine();
            bw.write(default1);
            bw.newLine();
            bw.write(default2);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            log.warning("Error creating default CommandListener configuration.");
        }
    }
}
