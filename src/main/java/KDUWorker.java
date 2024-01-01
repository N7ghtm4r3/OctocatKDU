import com.tecknobit.apimanager.apis.ConsolePainter;
import com.tecknobit.apimanager.apis.ConsolePainter.ANSIColor;
import com.tecknobit.githubmanager.releases.releaseassets.records.ReleaseAsset;
import com.tecknobit.githubmanager.releases.releases.GitHubReleasesManager;
import com.tecknobit.githubmanager.releases.releases.records.Release;
import com.tecknobit.githubmanager.repositories.repositories.GitHubRepositoriesManager;
import com.tecknobit.githubmanager.repositories.repositories.records.Repository;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static com.tecknobit.apimanager.apis.APIRequest.DEFAULT_ERROR_RESPONSE;
import static com.tecknobit.apimanager.apis.APIRequest.downloadFile;
import static com.tecknobit.apimanager.apis.ConsolePainter.ANSIColor.RED;
import static com.tecknobit.apimanager.formatters.TimeFormatter.getDate;
import static java.awt.Desktop.isDesktopSupported;
import static java.lang.System.exit;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class KDUWorker {

    private enum OS {

        LINUX,

        WINDOWS,

        MACOS

    }

    private static final Desktop desktop = Desktop.getDesktop();

    private static final ConsolePainter painter = new ConsolePainter();

    private ExecutorService executor = newCachedThreadPool();

    private final GitHubReleasesManager releasesManager;

    private final Release lastRelease;

    private final Repository repository;

    private final String appName;

    private String executablePath;

    public KDUWorker(String accessToken, String owner, String repo, String appName) {
        releasesManager = new GitHubReleasesManager(accessToken);
        GitHubRepositoriesManager repositoriesManager = new GitHubRepositoriesManager();
        Repository repository;
        Release lastRelease;
        try {
            repository = repositoriesManager.getRepository(owner, repo);
            lastRelease = releasesManager.getLatestRelease(repository);
        } catch (Exception e) {
            if(!repositoriesManager.getErrorResponse().equals(DEFAULT_ERROR_RESPONSE))
                logError("Incorrect repository data");
            repository = null;
            lastRelease = null;
        }
        this.repository = repository;
        this.lastRelease = lastRelease;
        this.appName = appName;
    }

    public boolean canBeUpdated(String currentVersion) {
        if(repository != null) {
            try {
                Release currentRelease = releasesManager.getReleaseByTagName(repository, currentVersion);
                return lastRelease.getCreatedAtTimestamp() > currentRelease.getCreatedAtTimestamp();
            } catch (Exception e) {
                if(!releasesManager.getErrorResponse().equals(DEFAULT_ERROR_RESPONSE))
                    logError("No releases found with this version");
                return false;
            }
        }
        return false;
    }

    public void installNewVersion() {
        OS currentOs = getCurrentOs();
        if(currentOs != null) {
            String downloadUrl = null;
            String executableSuffix = null;
            boolean foundCorrectExecutable = false;
            ArrayList<ReleaseAsset> assets = lastRelease.getAssets();
            for(int j = 0; j < assets.size() && !foundCorrectExecutable; j++) {
                ReleaseAsset asset = assets.get(j);
                downloadUrl = asset.getBrowserDownloadUrl();
                executableSuffix = new StringBuilder(new StringBuilder(downloadUrl).reverse().toString()
                        .split("\\.")[0]).reverse().toString();
                switch (currentOs) {
                    case LINUX -> {
                        if(isLinuxExecutable(executableSuffix))
                            foundCorrectExecutable = true;
                    }
                    case WINDOWS -> {
                        if(isWindowsExecutable(executableSuffix))
                            foundCorrectExecutable = true;
                    }
                    case MACOS -> {
                        if(isMacOsExecutable(executableSuffix))
                            foundCorrectExecutable = true;
                    }
                }
            }
            installAndRunExecutable(downloadUrl, executableSuffix);
        }
    }

    private void installAndRunExecutable(String downloadUrl, String executableSuffix) {
        if(downloadUrl != null) {
            try {
                if(isDesktopSupported()) {
                    executor.execute(() -> {
                        executablePath = System.getProperty("user.home") + "/Downloads/" + appName + "-"
                                + lastRelease.getTagName() + "." + executableSuffix;
                        try {
                            File executable = downloadFile(downloadUrl, executablePath, true);
                            desktop.open(executable);
                            exit(0);
                        } catch (IOException ignored) {}
                    });
                } else {
                    desktop.browse(URI.create(downloadUrl));
                    exit(0);
                }
            } catch (IOException ignored) {
            }
        }
    }

    public void stopInstallation() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS))
                executor.shutdownNow();
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        executor = newCachedThreadPool();
        ExecutorService service = newSingleThreadExecutor();
        service.execute(() -> {
            File executableFile = new File(executablePath);
            if (executableFile.exists()) {
                while (!executableFile.delete());
                executablePath = null;
            }
        });
    }

    public static void logError(String message) {
        logMessage(message, RED);
    }

    public static void logMessage(String message, ANSIColor color) {
        if(!message.startsWith(" "))
            message = " " + message;
        painter.printBold("[OctocatKDU - " + getDate(System.currentTimeMillis()) + "]:" + message, color);
    }

    private OS getCurrentOs() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win"))
            return OS.WINDOWS;
        else if (os.contains("osx"))
            return OS.MACOS;
        else if (os.contains("nix") || os.contains("aix") || os.contains("nux"))
            return OS.LINUX;
        return null;
    }

    private boolean isLinuxExecutable(String suffix) {
        return suffix.equals("appimage") || suffix.equals("deb") || suffix.equals("rpm");
    }

    private boolean isWindowsExecutable(String suffix) {
        return suffix.equals("exe") || suffix.equals("msi");
    }

    private boolean isMacOsExecutable(String suffix) {
        return suffix.equals("dmg") || suffix.equals("pkg");
    }

    public String getLastVersionCode() {
        if(lastRelease == null)
            return null;
        return " v. " + lastRelease.getTagName();
    }

}
