import com.vexsoftware.votifier.Votifier;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VoteListener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Vinnie on 12/17/14.
 */
public class CommandListener implements VoteListener {
    // The logger instance
    private static final Logger LOGGER = Logger.getLogger("CommandListener");
    // The vote reward commands
    private final ArrayList<String> commands;

    public CommandListener() {
        commands = new ArrayList<String>();
        loadConfig();
    }

    @Override
    public void voteMade(Vote vote) {
        LOGGER.log(Level.INFO, "[CommandListener] Received: {0}", vote);
        ArrayList<String> runCommands = new ArrayList<String>();
        for (String command : commands) {
            // Voter's Username
            command = command.replaceAll("\\{username\\}", vote.getUsername());
            // Website voted on
            command = command.replaceAll("\\{serviceName\\}", vote.getServiceName());
            // Voter's IP Address
            command = command.replaceAll("\\{address\\}", vote.getAddress());
            // Time of Vote
            command = command.replaceAll("\\{timeStamp\\}", vote.getTimeStamp());
            // Run command
            runCommands.add(command);
        }
        Bukkit.getScheduler().runTask(Votifier.getInstance(), new CommandExecuteTask(runCommands));
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
                // Convert color codes
                if (currentLine.startsWith("color=")) {
                    String command = currentLine.substring(6);
                    if (!command.isEmpty()) {
                        commands.add(ChatColor.translateAlternateColorCodes('&', command));
                    }
                    continue;
                }
                commands.add(currentLine);
            }
            br.close();
        } catch (FileNotFoundException ex) {
            LOGGER.warning("Could not find CommandListener configuration. Creating default.");
            createDefaultConfig(configFile);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error loading CommandListener configuration.", ex);
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
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error creating default CommandListener configuration.", ex);
        }
    }
}
