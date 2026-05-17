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
import ec.edu.uisek.githubclient.models.Repository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GithubClientTheme {

                val listViewModel: RepoListViewModel = viewModel()
                var currentScreen by remember { mutableStateOf("repoList") }

                // Estado para manejar la edición
                var repoToEdit by remember { mutableStateOf<Repository?>(null) }

                when (currentScreen) {
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
                            listViewModel.fetcheRepos()  // ← Aquí usamos tu función original
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