import com.tecknobit.githubmanager.releases.releaseassets.records.ReleaseAsset;
import com.tecknobit.githubmanager.releases.releases.GitHubReleasesManager;
import com.tecknobit.githubmanager.releases.releases.records.Release;
import com.tecknobit.githubmanager.repositories.repositories.GitHubRepositoriesManager;
import com.tecknobit.githubmanager.repositories.repositories.records.Repository;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static com.tecknobit.apimanager.apis.APIRequest.downloadFile;
import static java.awt.Desktop.getDesktop;
import static java.awt.Desktop.isDesktopSupported;

public class KDUWorker {

    private enum OS {

        LINUX,

        WINDOWS,

        MACOS

    }

    private final GitHubReleasesManager releasesManager;

    private final Release lastRelease;

    private final Repository repository;

    public KDUWorker(String accessToken, String owner, String repo) {
        releasesManager = new GitHubReleasesManager(accessToken);
        GitHubRepositoriesManager repositoriesManager = new GitHubRepositoriesManager();
        Repository repository;
        Release lastRelease;
        try {
            repository = repositoriesManager.getRepository(owner, repo);
            lastRelease = releasesManager.getLatestRelease(repository);
        } catch (IOException e) {
            repository = null;
            lastRelease = null;
        }
        this.repository = repository;
        this.lastRelease = lastRelease;
    }

    public boolean canBeUpdated(String currentVersion) {
        if(repository != null) {
            try {
                Release currentRelease = releasesManager.getReleaseByTagName(repository, currentVersion);
                return lastRelease.getCreatedAtTimestamp() > currentRelease.getCreatedAtTimestamp();
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    public void installNewVersion() {
        OS currentOs = getCurrentOs();
        if(currentOs != null) {
            Desktop desktop = getDesktop();
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
            if(downloadUrl != null) {
                if(isDesktopSupported()) {
                    // TO-DO: CHECK IF OPENED CORRECTLY AND INSTALLED
                    try {
                        desktop.open(downloadFile(downloadUrl, "prova." + executableSuffix, false));
                    } catch (IOException ignored) {
                        ignored.printStackTrace();
                    }
                } else {

                }
            }
        }
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

}
