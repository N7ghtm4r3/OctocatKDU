import com.tecknobit.apimanager.apis.ConsolePainter;
import com.tecknobit.apimanager.apis.ConsolePainter.ANSIColor;
import com.tecknobit.apimanager.formatters.JsonHelper;
import com.tecknobit.githubmanager.releases.releaseassets.records.ReleaseAsset;
import com.tecknobit.githubmanager.releases.releases.GitHubReleasesManager;
import com.tecknobit.githubmanager.releases.releases.records.Release;
import com.tecknobit.githubmanager.repositories.repositories.GitHubRepositoriesManager;
import com.tecknobit.githubmanager.repositories.repositories.records.Repository;

import java.awt.*;
import java.io.*;
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

/**
 * The {@code KDUWorker} class is useful to check whether there is a last release available and in that case manage the
 * installation of the correct executable to update the current version
 *
 * @author N7ghtm4r3 - Tecknobit
 */
public class KDUWorker {

    /**
     * {@code OS} list of available OS
     */
    private enum OS {

        /**
         * {@code LINUX} OS
         */
        LINUX,

        /**
         * {@code WINDOWS} OS
         */
        WINDOWS,

        /**
         * {@code MACOS} OS
         */
        MACOS

    }

    /**
     * {@code OCTOCAT_KDU_CONFIGS_PATH} the path where are stored the credentials for the GitHub
     * application repository
     *
     * @apiNote you must keep this file safe and not share in public repositories, but keep in local
     */
    public static final String OCTOCAT_KDU_CONFIGS_PATH = "octocat_kdu.config";

    /**
     * {@code desktop} the current desktop environment where the application is running
     */
    private static final Desktop desktop = Desktop.getDesktop();

    /**
     * {@code painter} instance to manage the console painter
     */
    private static final ConsolePainter painter = new ConsolePainter();

    /**
     * {@code executor} to launch the background tasks to not freeze the UI
     */
    private ExecutorService executor = newCachedThreadPool();

    /**
     * {@code releasesManager} manager to get the releases of the application
     */
    private final GitHubReleasesManager releasesManager;

    /**
     * {@code lastRelease} the latest release available
     */
    private final Release lastRelease;

    /**
     * {@code repository} the repository of the application
     */
    private final Repository repository;

    /**
     * {@code appName} the name of the application where the dialog will be shown
     */
    private final String appName;

    /**
     * {@code executablePath} the path where save the executable file and launch to install it
     */
    private String executablePath;

    /**
     * Constructor to init the {@link KDUWorker} class
     *
     * @param appName:{@code appName} the name of the application where the dialog will be shown
     *
     */
    public KDUWorker(String appName) {
        try {
            JsonHelper config = getConfig();
            String accessToken = config.getString("personal_access_token", "");
            String owner = config.getString("owner", "");
            String repo = config.getString("repo", "");
            releasesManager = new GitHubReleasesManager(accessToken);
            GitHubRepositoriesManager repositoriesManager = new GitHubRepositoriesManager();
            Repository repository;
            Release lastRelease;
            try {
                repository = repositoriesManager.getRepository(owner, repo);
                lastRelease = releasesManager.getLatestRelease(repository);
            } catch (Exception e) {
                if (!repositoriesManager.getErrorResponse().equals(DEFAULT_ERROR_RESPONSE))
                    logError("Incorrect repository data");
                repository = null;
                lastRelease = null;
            }
            this.repository = repository;
            this.lastRelease = lastRelease;
            this.appName = appName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to read the current resources stored from  and to load the
     * instance <br>
     * No-any params required
     * @throws IOException when an error reading the resources file occurred
     */
    private JsonHelper getConfig() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(OCTOCAT_KDU_CONFIGS_PATH);
        if(inputStream == null)
            throw new IOException(OCTOCAT_KDU_CONFIGS_PATH + " not found!\nYou must save in the resources folder");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null)
            stringBuilder.append(line);
        return new JsonHelper(stringBuilder);
    }

    /**
     * Method to check whether the application can be updated to the latest version available
     * @param currentVersion: the current version of the application
     * @return whether the application can be updated to the latest version available as boolean
     */
    public boolean canBeUpdated(String currentVersion) {
        if (repository != null) {
            try {
                Release currentRelease = releasesManager.getReleaseByTagName(repository, currentVersion);
                return lastRelease.getCreatedAtTimestamp() > currentRelease.getCreatedAtTimestamp();
            } catch (Exception e) {
                if (!releasesManager.getErrorResponse().equals(DEFAULT_ERROR_RESPONSE))
                    logError("No releases found with this version");
                return false;
            }
        }
        return false;
    }

    /**
     * Method to install the new version. <br>
     * After the installation of the correct executable file will be closed the application and launch that executable
     * to install the new version <br>
     * No-any params required
     *
     * @apiNote will be invoked {@link #installAndRunExecutable(String, String)} to perform this task
     */
    public void installNewVersion() {
        OS currentOs = getCurrentOs();
        if (currentOs != null) {
            String downloadUrl = null;
            String executableSuffix = null;
            boolean foundCorrectExecutable = false;
            ArrayList<ReleaseAsset> assets = lastRelease.getAssets();
            for (int j = 0; j < assets.size() && !foundCorrectExecutable; j++) {
                ReleaseAsset asset = assets.get(j);
                downloadUrl = asset.getBrowserDownloadUrl();
                executableSuffix = new StringBuilder(new StringBuilder(downloadUrl).reverse().toString()
                        .split("\\.")[0]).reverse().toString();
                switch (currentOs) {
                    case LINUX -> {
                        if (isLinuxExecutable(executableSuffix))
                            foundCorrectExecutable = true;
                    }
                    case WINDOWS -> {
                        if (isWindowsExecutable(executableSuffix))
                            foundCorrectExecutable = true;
                    }
                    case MACOS -> {
                        if (isMacOsExecutable(executableSuffix))
                            foundCorrectExecutable = true;
                    }
                }
            }
            installAndRunExecutable(downloadUrl, executableSuffix);
        }
    }

    /**
     * Method to effectively install and run the executable for the installation
     *
     * @param downloadUrl: the url to download the executable
     * @param executableSuffix: the suffix of the executable: appimage, deb, rmp, dkg, pkg, exe or msi
     */
    private void installAndRunExecutable(String downloadUrl, String executableSuffix) {
        if (downloadUrl != null) {
            try {
                if (isDesktopSupported()) {
                    executor.execute(() -> {
                        executablePath = System.getProperty("user.home") + "/Downloads/" + appName + "-"
                                + lastRelease.getTagName() + "." + executableSuffix;
                        try {
                            File executable = downloadFile(downloadUrl, executablePath, true);
                            desktop.open(executable);
                            exit(0);
                        } catch (IOException ignored) {
                        }
                    });
                } else {
                    desktop.browse(URI.create(downloadUrl));
                    exit(0);
                }
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Method to stop the current installation, will be also deleted the executable file if has been already saved <br>
     * No-any params required
     */
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
                while (!executableFile.delete()) ;
                executablePath = null;
            }
        });
    }

    /**
     * Method to log an error on run console
     * @param error: the error to log
     */
    public static void logError(String error) {
        logMessage(error, RED);
    }

    /**
     * Method to log a message on run console
     * @param message: the message to log
     */
    public static void logMessage(String message, ANSIColor color) {
        if (!message.startsWith(" "))
            message = " " + message;
        painter.printBold("[OctocatKDU - " + getDate(System.currentTimeMillis()) + "]:" + message, color);
    }

    /**
     * Method to get the current OS where the application is running <br>
     * No-any params required
     *
     * @return the current OS where the application is running instance as {@link OS}
     */
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

    /**
     * Method to get whether the suffix argument is correct for Linux
     * @param suffix: the suffix to check
     * @return whether the suffix argument is correct for Linux as boolean
     */
    private boolean isLinuxExecutable(String suffix) {
        return suffix.equals("appimage") || suffix.equals("deb") || suffix.equals("rpm");
    }

    /**
     * Method to get whether the suffix argument is correct for Windows
     * @param suffix: the suffix to check
     * @return whether the suffix argument is correct for Windows as boolean
     */
    private boolean isWindowsExecutable(String suffix) {
        return suffix.equals("exe") || suffix.equals("msi");
    }

    /**
     * Method to get whether the suffix argument is correct for macOS
     * @param suffix: the suffix to check
     * @return whether the suffix argument is correct for macOS as boolean
     */
    private boolean isMacOsExecutable(String suffix) {
        return suffix.equals("dmg") || suffix.equals("pkg");
    }

    /**
     * Method to get the latest version code <br>
     * No-any params required
     *
     * @return the latest version code as {@link String}
     */
    public String getLastVersionCode() {
        if (lastRelease == null)
            return null;
        return " v. " + lastRelease.getTagName();
    }

}
