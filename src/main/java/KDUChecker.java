import com.tecknobit.githubmanager.releases.releases.GitHubReleasesManager;
import com.tecknobit.githubmanager.releases.releases.records.Release;
import com.tecknobit.githubmanager.repositories.repositories.GitHubRepositoriesManager;
import com.tecknobit.githubmanager.repositories.repositories.records.Repository;

import java.io.IOException;

public class KDUChecker {

    //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

    private final GitHubReleasesManager releasesManager;

    private final Repository repository;

    public KDUChecker(String accessToken, String owner, String repo) {
        releasesManager = new GitHubReleasesManager(accessToken);
        GitHubRepositoriesManager repositoriesManager = new GitHubRepositoriesManager();
        Repository repository;
        try {
            repository = repositoriesManager.getRepository(owner, repo);
        } catch (IOException e) {
            repository = null;
        }
        this.repository = repository;
    }

    public boolean canBeUpdated(String currentVersion) {
        if(repository != null) {
            try {
                Release release = releasesManager.getLatestRelease(repository);
                Release currentRelease = releasesManager.getReleaseByTagName(repository, currentVersion);
                return release.getCreatedAtTimestamp() > currentRelease.getCreatedAtTimestamp();
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

}
