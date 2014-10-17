import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.vexsoftware.votifier.Votifier;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VoteListener;

/**
 * Created by Vinnie on 10/17/14.
 */
public class CommandListener implements VoteListener {
    private List<String> commands = new ArrayList<String>();

    public CommandListener() {
        File configFile = new File("./plugins_mod/RainbowVotifier/CommandListener.txt");
        if (!configFile.exists())
        {
            try {
                configFile.createNewFile();

                String defaultCommand = "say Thanks {username} for voting on {serviceName}!";
                commands.add(defaultCommand);

                FileWriter fw = new FileWriter(configFile);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(defaultCommand);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
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
                e.printStackTrace();
            } finally {
                try {
                    if (br != null)br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void voteMade(Vote vote) {
        if (vote.getUsername() != null) {
            for (int i = 0; i < commands.size(); i++) {
                String command = commands.get(i);
                if(command.contains("{username}")) {
                    command = command.replace("{username}", vote.getUsername());
                }
                if(command.contains("{address}")) {
                    command = command.replace("{address}", vote.getAddress());
                }
                if(command.contains("{serviceName}")) {
                    command = command.replace("{serviceName}", vote.getServiceName());
                }
                if(command.contains("{timeStamp}")) {
                    command = command.replace("{timeStamp}", vote.getTimeStamp());
                }
                Votifier.getServer().executeCommand(command);
            }
        }
    }
}
