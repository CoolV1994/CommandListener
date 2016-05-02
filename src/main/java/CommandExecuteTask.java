import org.bukkit.Bukkit;

import java.util.List;

/**
 * Created by Vinnie on 5/1/2016.
 */
public class CommandExecuteTask implements Runnable {
    private List<String> commands;

    public CommandExecuteTask() {
    }

    public CommandExecuteTask(List<String> commands) {
        this.commands = commands;
    }

    @Override
    public void run() {
        for (String command : commands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
}
