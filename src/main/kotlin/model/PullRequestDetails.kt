package com.demo.model

data class PullRequestDetails(var branchName: String = "hello-world",
                              val pullRequestTitle: String = "Add Hello.txt",
                              val pullRequestBody: String = "This pull request was automatically created.\n" +
                                      "It adds a Hello.txt file containing 'Hello world'.")
