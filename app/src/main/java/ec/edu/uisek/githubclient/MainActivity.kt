package ec.edu.uisek.githubclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.uisek.githubclient.ui.screens.RepoForm
import ec.edu.uisek.githubclient.ui.screens.RepoList
import ec.edu.uisek.githubclient.ui.theme.GithubClientTheme
import ec.edu.uisek.githubclient.viewmodels.RepoListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GithubClientTheme {

                val listViewModel: RepoListViewModel = viewModel()

                var currentScreen by remember { mutableStateOf("repoList") }

                when (currentScreen) {

                    "repoList" -> RepoList(
                        onNavigateForm = {
                            currentScreen = "repoForm"
                        }
                    )

                    "repoForm" -> RepoForm(
                        onSaveClick = {
                            listViewModel.fetcheRepos()
                            currentScreen = "repoList"
                        },
                        onBackClick = {
                            currentScreen = "repoList"
                        }
                    )
                }
            }
        }
    }
}

