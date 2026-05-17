package ec.edu.uisek.githubclient.Viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.uisek.githubclient.models.RepositoryPayload
import ec.edu.uisek.githubclientcompose.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RepoFormViewModel : ViewModel() {

    private var isEditMode = false
    private var currentOwner: String = ""
    private var currentRepoName: String = ""

    private val apiService = RetrofitClient.apiService

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg.asStateFlow()

    // Guardar datos iniciales para edición
    private val _initialName = MutableStateFlow("")
    val initialName: StateFlow<String> = _initialName.asStateFlow()

    private val _initialDescription = MutableStateFlow<String?>(null)
    val initialDescription: StateFlow<String?> = _initialDescription.asStateFlow()

    // TU FUNCIÓN ORIGINAL - NO MODIFICADA
    fun createRepository(name: String, description: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null

            try {
                val payload = RepositoryPayload(name, description)
                apiService.createRepository(payload)
                _isSuccess.value = true
            } catch (e: Exception) {
                _errorMsg.value =
                    "Error al crear repositorio: ${e.localizedMessage ?: "Error desconocido"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // NUEVA FUNCIÓN PARA ACTUALIZAR (EDITAR)
    fun updateRepository(name: String, description: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null

            try {
                val payload = RepositoryPayload(name, description)
                apiService.updateRepository(currentOwner, currentRepoName, payload)
                _isSuccess.value = true
            } catch (e: IOException) {
                _errorMsg.value = "Error de red: ${e.localizedMessage ?: "Verifica tu conexión"}"
            } catch (e: HttpException) {
                _errorMsg.value = "Error HTTP: ${e.code()} - ${e.message()}"
            } catch (e: Exception) {
                _errorMsg.value = "Error al actualizar repositorio: ${e.localizedMessage ?: "Error desconocido"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // FUNCIÓN PARA ELIMINAR
    fun deleteRepository(owner: String, repoName: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null

            try {
                val response = apiService.deleteRepository(owner, repoName)
                if (response.isSuccessful) {
                    onSuccess.invoke()
                } else {
                    _errorMsg.value = "Error al eliminar: ${response.code()} - ${response.message()}"
                }
            } catch (e: IOException) {
                _errorMsg.value = "Error de red: ${e.localizedMessage ?: "Verifica tu conexión"}"
            } catch (e: HttpException) {
                _errorMsg.value = "Error HTTP: ${e.code()} - ${e.message()}"
            } catch (e: Exception) {
                _errorMsg.value = "Error al eliminar: ${e.localizedMessage ?: "Error desconocido"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Configurar modo edición
    fun setEditMode(owner: String, repoName: String, currentName: String, currentDescription: String?) {
        isEditMode = true
        currentOwner = owner
        currentRepoName = repoName
        _initialName.value = currentName
        _initialDescription.value = currentDescription
    }

    // Resetear a modo creación
    fun resetToCreateMode() {
        isEditMode = false
        currentOwner = ""
        currentRepoName = ""
        _initialName.value = ""
        _initialDescription.value = null
    }

    // Verificar si está en modo edición
    fun isInEditMode(): Boolean = isEditMode

    // TU FUNCIÓN ORIGINAL - NO MODIFICADA
    fun resetSuccess() {
        _isSuccess.value = false
    }
}