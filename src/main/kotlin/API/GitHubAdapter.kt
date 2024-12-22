package com.demo.API

import com.demo.model.CommitDetails
import com.demo.model.PullRequestDetails
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GitHub

class GitHubAdapter(private val github: GitHub) {

    /**
     * Gets the list of repositories of the authenticated user.
     *
     * @return the list of repositories
     */
    fun getRepositories(): List<GHRepository> {
        return github.myself.listRepositories().toList()
    }


    /**
     * Checks if a branch with the given name (starting with "hello-world")
     * exists in the repository, to ensure consistency and relevancy of branch names.
     */
    fun branchExists(repository: GHRepository, branchName: String): Boolean {
        return try {
            repository.getRef("heads/$branchName")
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Creates a new branch with the given name, in the repository chosen by the user.
     *
     * @param repository the repository to create the branch in
     * @param prDetails the details of the pull request, from which the branch name is taken
     */
    fun createBranch(repository: GHRepository, prDetails: PullRequestDetails){
        val defaultBranchName = repository.defaultBranch
        val defaultBranchSha = repository.getRef("heads/$defaultBranchName").`object`.sha

        repository.createRef("refs/heads/${prDetails.branchName}", defaultBranchSha)
    }

    /**
     * Creates a commit with the given details.
     *
     * @param repository the repository to create the commit in
     * @param prDetails the details of the pull request, from which the branch name is taken
     * @param commitDetails the details of the commit, including the file path, content, and message
     */
    fun createCommit(repository: GHRepository, prDetails: PullRequestDetails, commitDetails: CommitDetails){
        repository.createContent()
            .path(commitDetails.filePath)
            .content(commitDetails.fileContent)
            .branch(prDetails.branchName)
            .message(commitDetails.commitMessage)
            .commit()
    }

    /**
     * Creates a pull request with the changes made in the branch.
     */
    fun createPullRequest(repository: GHRepository, prDetails: PullRequestDetails){
        val pullRequest = repository.createPullRequest(
            prDetails.pullRequestTitle,
            prDetails.branchName,
            repository.defaultBranch,
            prDetails.pullRequestBody
        )

        println("See the created pull request: ${pullRequest.htmlUrl}")
    }

    companion object {
        /**
         * Connects to the GitHub API using the given access OAuth token.
         *
         * @param accessToken the access token to authenticate the user
         * @return the GitHubAdapter instance
         */
        fun connect(accessToken: String): GitHubAdapter {
            val github = GitHub.connectUsingOAuth(accessToken)
            return GitHubAdapter(github)
        }
    }
}