package ec.edu.uisek.githubclient.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ec.edu.uisek.githubclient.models.GithubUser
import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclient.ui.theme.GithubClientTheme

@Composable
fun RepoItem(
    repository: Repository,
    onEditClick: () -> Unit = {},     // Callback para editar
    onDeleteClick: () -> Unit = {}    // Callback para eliminar
) {
    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                repository.owner.avatarUrl,
                repository.name,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    repository.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (!repository.description.isNullOrBlank()) {
                    Text(
                        repository.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                if (!repository.language.isNullOrBlank()) {
                    Text(
                        repository.language,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }

            // Botones de acción (editar y eliminar)
            Row(
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
            ) {
                // Botón Editar
                IconButton(
                    onClick = onEditClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar repositorio"
                    )
                }

                // Botón Eliminar
                IconButton(
                    onClick = onDeleteClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar repositorio"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RepoItemPreview() {
    GithubClientTheme {
        val repository = Repository(
            id = "1234",
            name = "Laboratorio 3",
            owner = GithubUser(
                id = "0001",
                login = "DannyCaraguay",
                avatarUrl = "https://avatars.githubusercontent.com/u/215519037?v=4",
            ),
            description = "Jetpack Compose",
            language = "Kotlin"
        )
        RepoItem(
            repository = repository,
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}