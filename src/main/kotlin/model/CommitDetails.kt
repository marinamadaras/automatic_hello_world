package com.demo.model

data class CommitDetails(val filePath: String = "Hello.txt",
                         val fileContent: String = "Hello world",
                         val commitMessage: String = "Write 'Hello world' in Hello.txt")
