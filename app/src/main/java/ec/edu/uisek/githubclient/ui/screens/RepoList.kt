package ec.edu.uisek.githubclient.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ec.edu.uisek.githubclient.ui.components.RepoItem
import ec.edu.uisek.githubclient.ui.theme.GithubClientTheme

@Composable
fun RepoList () {
    Column(
        modifier = Modifier.padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        RepoItem(
            repoName = "Repositorio de Android",
            repoDescription = "Respositorio creado en Kotlin con JetPack Compose",
            repoLanguaje = "Kotlin",
            repoImage = "https://i0.wp.com/unaaldia.hispasec.com/wp-content/uploads/2015/09/2e303-android-logo.png?resize=200%2C200&ssl=1"
        )
        RepoItem(
            repoName = "Repositorio de IOS",
            repoDescription = "Respositorio creado en Kotlin con JetPack Compose",
            repoLanguaje = "Swift",
            repoImage = "https://www.comparasoftware.ec/media/1784"
        )
        RepoItem(
            repoName = "Repositorio de Django",
            repoDescription = "Respositorio creado en Kotlin con JetPack Compose",
            repoLanguaje = "Phyton",
            repoImage = "https://i0.wp.com/unaaldia.hispasec.com/wp-content/uploads/2016/03/1b04b-django-logo.png?resize=320%2C111&ssl=1"
        )
        RepoItem(
            repoName = "Repositorio de React",
            repoDescription = "Respositorio creado en Kotlin con JetPack Compose",
            repoLanguaje = "Java",
            repoImage = "https://opensource.fb.com/img/projects/react.jpg"
        )

    }
}
@Preview(showBackground = true)
@Composable
fun RepoItemPreview () {
    GithubClientTheme {
        RepoList()
    }
}