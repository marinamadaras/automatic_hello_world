package com.demo.services

import com.demo.model.CommitDetails
import com.demo.model.PullRequestDetails
import com.demo.API.GitHubAdapter
import org.kohsuke.github.GHRepository

class GitHubService(private val ghAPI: GitHubAdapter) {

    /**
     * Creates a new branch _"hello-world"_,
     * commits to a txt file _"Hello.txt"_,
     * and creates a pull request with the changes.
     */
    fun createHelloWorldPullRequest(){
        val prDetails = PullRequestDetails()
        val commitDetails = CommitDetails()

        val repositories = ghAPI.getRepositories()
        val repository = getSelectedRepository(repositories) ?: return

        ensureBranchDoesNotExistAlready(repository, prDetails)
        ghAPI.createBranch(repository, prDetails)
        ghAPI.createCommit(repository, prDetails, commitDetails)
        ghAPI.createPullRequest(repository, prDetails)
    }

    private fun ensureBranchDoesNotExistAlready(repository: GHRepository, prDetails: PullRequestDetails){
        var suffix = 1

        while (ghAPI.branchExists(repository, prDetails.branchName)) {
            prDetails.branchName = "${prDetails.branchName}-$suffix"
            suffix++
        }
    }


    /**
     * Prompts the user to select a repository from the list of repositories.
     * They can choose a repository by entering the corresponding number or
     * type 'exit' to quit.
     *
     * @param repositories the list of repositories
     * @return the selected repository or null if the user exits
     */
    private fun getSelectedRepository(repositories: List<GHRepository>): GHRepository? {
        val repoNames = repositories.map { it.name }
        while (true) {
            println("Available Repositories:")
            repoNames.forEachIndexed { index, name -> println("${index + 1}. $name") }

            print("Please select the repository for creating the pull request by entering its corresponding number: (or type 'exit' to quit)\n")
            val input = readlnOrNull()

            if (input.equals("exit", ignoreCase = true)) {
                println("Exiting...")
                return null
            }

            val selectedIndex = input?.toIntOrNull()?.minus(1)
            if (selectedIndex != null && selectedIndex in repoNames.indices) {
                return repositories[selectedIndex]
            }
            println("Invalid selection. Please try again.")
        }
    }

}