package com.demo

import com.demo.config.PropertiesConfig
import com.demo.services.GitHubService
import com.demo.API.GitHubAdapter

fun main() {
    val ghAPI = GitHubAdapter.connect(PropertiesConfig.get("oauth"))
    val gitHubService = GitHubService(ghAPI)
    gitHubService.createHelloWorldPullRequest()
}