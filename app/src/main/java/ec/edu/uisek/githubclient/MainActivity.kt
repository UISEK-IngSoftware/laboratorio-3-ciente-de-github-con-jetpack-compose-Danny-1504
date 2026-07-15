package ec.edu.uisek.githubclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.uisek.githubclient.ui.screens.RepoForm
import ec.edu.uisek.githubclient.ui.screens.RepoList
import ec.edu.uisek.githubclient.ui.theme.GithubClientTheme
import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclient.services.AuthService
import ec.edu.uisek.githubclient.ui.screens.LoginForm
import ec.edu.uisek.githubclient.viewmodels.RepoListViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authService = AuthService(context = this)
        setContent {
            GithubClientTheme {

                val listViewModel: RepoListViewModel = viewModel()
                var currentScreen by remember { mutableStateOf(if (authService.isLoggedIn()) "repoList" else "login") }

                // Estado para manejar la edición
                var repoToEdit by remember { mutableStateOf<Repository?>(null) }

                when (currentScreen) {
                    "login"-> LoginForm (
                        onLoginSuccess = {currentScreen="repoList"}
                    )
                    "repoList" -> RepoList(
                        onNavigateForm = {
                            repoToEdit = null  // Limpiar modo edición (crear nuevo)
                            currentScreen = "repoForm"
                        },
                        onEditRepo = { repo ->  // Callback para editar
                            repoToEdit = repo
                            currentScreen = "repoForm"
                        },
                        viewModel = listViewModel
                    )

                    "repoForm" -> RepoForm(
                        // Pasar datos de edición si existen
                        editRepoName = repoToEdit?.name,
                        editRepoDescription = repoToEdit?.description,
                        editOwner = repoToEdit?.owner?.login,
                        editRepoCurrentName = repoToEdit?.name,
                        onSaveClick = {
                            listViewModel.fetcheRepos()
                            currentScreen = "repoList"
                        },
                        onBackClick = {
                            currentScreen = "repoList"
                        },
                        onCancel = {
                            currentScreen = "repoList"
                        }
                    )
                }
            }
        }
    }
}