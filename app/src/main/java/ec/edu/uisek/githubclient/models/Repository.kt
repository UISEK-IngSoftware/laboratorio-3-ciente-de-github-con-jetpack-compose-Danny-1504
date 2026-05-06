package ec.edu.uisek.githubclient.models

class Repository (
    val id: String,
    val name: String,
    val owner: GithubUser,
    val description: String?,
    val language: String?,
)
