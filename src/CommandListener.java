import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.vexsoftware.votifier.Votifier;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VoteListener;

/**
 * Created by Vinnie on 10/17/14.
 */
public class CommandListener implements VoteListener {
    private Logger log = Logger.getLogger("CommandListener");
    private List<String> commands = new ArrayList<String>();

    public CommandListener() {
        File configFile = new File("./plugins_mod/RainbowVotifier/CommandListener.txt");
        if (!configFile.exists())
        {
            try {
                configFile.createNewFile();

                String defaultCommand = "diw echo Thanks &a{username} for voting on &9{serviceName}!";
                commands.add(defaultCommand);

                FileWriter fw = new FileWriter(configFile);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.newLine();
                bw.write(defaultCommand);
                bw.close();
            } catch (IOException e) {
                log.warning("Error creating default CommandListener configuration.");
            }
        }
        else
        {
            BufferedReader br = null;
            try {
                String currentLine;
                br = new BufferedReader(new FileReader(configFile));
                while ((currentLine = br.readLine()) != null) {
                    commands.add(currentLine);
                }
            } catch (IOException e) {
                log.warning("Error loading CommandListener configuration.");
            } finally {
                try {
                    if (br != null)
                        br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void voteMade(Vote vote) {
        log.info("Received: " + vote);
        if (vote.getUsername() != null) {
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
                Votifier.getServer().executeCommand(command);
            }
        }
    }
}
