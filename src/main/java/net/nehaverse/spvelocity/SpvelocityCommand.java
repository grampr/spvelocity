package net.nehaverse.spvelocity;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SpvelocityCommand implements CommandExecutor {

    private final SpvelocityManager manager;

    public SpvelocityCommand(SpvelocityManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             Command cmd,
                             String label,
                             String[] args) {

        if (args.length == 0) {
            sender.sendMessage("/proxy <start|stop|status>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start":
                if (manager.isRunning()) {
                    sender.sendMessage("§e既に起動しています。");
                    break;
                }
                sender.sendMessage("§a起動します…");
                manager.startAsync().whenComplete((v, t) -> {
                    if (t != null) sender.sendMessage("§c失敗: " + t.getMessage());
                    else sender.sendMessage("§aVelocity を起動しました。");
                });
                break;

            case "stop":
                if (!manager.isRunning()) {
                    sender.sendMessage("§eまだ起動していません。");
                    break;
                }
                manager.shutdown();
                sender.sendMessage("§a停止しました。");
                break;

            case "status":
                sender.sendMessage(manager.isRunning()
                        ? "§aVelocity は起動中です。"
                        : "§cVelocity は停止中です。");
                break;

            default:
                sender.sendMessage("/proxy <start|stop|status>");
        }
        return true;
    }
}