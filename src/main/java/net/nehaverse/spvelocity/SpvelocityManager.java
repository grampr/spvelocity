package net.nehaverse.spvelocity;

import org.bukkit.plugin.Plugin;              // ← これが必須
import org.apache.commons.io.FileUtils;       // Commons-IO は元パッケージで OK

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class SpvelocityManager {

    private final Plugin plugin;
    private Process velocityProcess;

    private final String downloadUrl;
    private final String jarName;
    private final int proxyPort;

    public SpvelocityManager(Plugin plugin) {
        this.plugin = plugin;
        downloadUrl = plugin.getConfig().getString(
                "velocity.download-url",
                "https://api.papermc.io/v2/projects/velocity/versions/3.3.0-SNAPSHOT/builds/261/downloads/velocity-3.3.0-SNAPSHOT-261.jar");
        jarName = plugin.getConfig().getString("velocity.jar-name", "velocity.jar");
        proxyPort = plugin.getConfig().getInt("velocity.port", 25577);
    }

    public boolean isRunning() {
        return velocityProcess != null && velocityProcess.isAlive();
    }

    public CompletableFuture<Void> startAsync() {
        return CompletableFuture.runAsync(() -> {
            if (isRunning()) throw new IllegalStateException("Velocity is already running.");

            try {
                File jar = prepareJar();
                ProcessBuilder pb = new ProcessBuilder(
                        "java",
                        "-jar", jar.getAbsolutePath(),
                        "--port", String.valueOf(proxyPort));
                pb.directory(jar.getParentFile());
                pb.redirectErrorStream(true);
                pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);

                velocityProcess = pb.start();
                plugin.getLogger().info("Velocity started on port " + proxyPort);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void shutdown() {
        if (isRunning()) {
            velocityProcess.destroy();
            try { velocityProcess.waitFor(); } catch (InterruptedException ignored) {}
            plugin.getLogger().info("Velocity stopped.");
        }
    }

    private File prepareJar() throws IOException {
        Path dir = plugin.getDataFolder().toPath().resolve("proxy");
        if (!dir.toFile().exists()) dir.toFile().mkdirs();

        File jar = dir.resolve(jarName).toFile();
        if (!jar.exists()) {
            plugin.getLogger().info("Velocity jar をダウンロード中 …");
            FileUtils.copyURLToFile(new URL(downloadUrl), jar);
            plugin.getLogger().info("ダウンロード完了: " + jar.getName());
        }
        return jar;
    }
}