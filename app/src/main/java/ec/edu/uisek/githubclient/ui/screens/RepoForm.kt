package ec.edu.uisek.githubclient.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.uisek.githubclient.viewmodels.RepoFormViewModel
import ec.edu.uisek.githubclient.ui.theme.GithubClientTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoForm(
    onBackClick: () -> Unit = {},
    onCancel: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    // Parámetros opcionales para edición
    editRepoName: String? = null,
    editRepoDescription: String? = null,
    editOwner: String? = null,
    editRepoCurrentName: String? = null,
    viewModel: RepoFormViewModel = viewModel()
) {

    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val errMsg by viewModel.errorMsg.collectAsState()

    // Obtener datos iniciales del ViewModel (para edición)
    val initialName by viewModel.initialName.collectAsState()
    val initialDescription by viewModel.initialDescription.collectAsState()

    // Usar los parámetros de edición o los del ViewModel
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // Configurar modo edición cuando se reciben parámetros
    LaunchedEffect(editRepoName, editOwner, editRepoCurrentName) {
        if (editRepoName != null && editOwner != null && editRepoCurrentName != null) {
            viewModel.setEditMode(
                owner = editOwner,
                repoName = editRepoCurrentName,
                currentName = editRepoName,
                currentDescription = editRepoDescription
            )
            name = editRepoName
            description = editRepoDescription ?: ""
        } else {
            // Modo creación: asegurar que está reset
            viewModel.resetToCreateMode()
            name = ""
            description = ""
        }
    }

    // También actualizar si cambian los datos del ViewModel
    LaunchedEffect(initialName, initialDescription) {
        if (viewModel.isInEditMode() && initialName.isNotEmpty()) {
            name = initialName
            description = initialDescription ?: ""
        }
    }

    LaunchedEffect(key1 = isSuccess) {
        if (isSuccess) {
            onSaveClick()
            viewModel.resetSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (viewModel.isInEditMode()) "Editar Repositorio" else "Nuevo Repositorio")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = name.isBlank() && !isLoading
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            if (errMsg != null) {
                Text(
                    text = errMsg ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        if (viewModel.isInEditMode()) {
                            viewModel.updateRepository(name, description)
                        } else {
                            viewModel.createRepository(name, description)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading && name.isNotBlank()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(if (viewModel.isInEditMode()) "Actualizar" else "Guardar")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RepoFormPreview() {
    GithubClientTheme {
        RepoForm()
    }
}