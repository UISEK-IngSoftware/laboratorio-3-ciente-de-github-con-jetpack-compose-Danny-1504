package ec.edu.uisek.githubclient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclient.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RepoListViewModel : ViewModel(){
    //Maneja el estado de la lista de repositorios
    private val _repos = MutableStateFlow<List<Repository>>(emptyList())
    val repos: StateFlow<List<Repository>> = _repos.asStateFlow()

    //maneja el estado del progreso de la carga de repositorios
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    //Maneja el estado del mensaje de error
    private val _errorMsg = MutableStateFlow<String?>(value = null)
    val errorMsg: StateFlow<String?> = _errorMsg.asStateFlow()

    fun fetcheRepos(){
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                val response = RetrofitClient.apiService.getRespository()
                _repos.value = response
            } catch (e: Exception){
                _errorMsg.value = "Error al cargar repositorio: ${e.localizedMessage}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // NUEVA FUNCIÓN: Eliminar repositorio
    fun deleteRepository(owner: String, repoName: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null

            try {
                val response = RetrofitClient.apiService.deleteRepository(owner, repoName)
                if (response.isSuccessful) {
                    onSuccess.invoke()  // Recargar la lista después de eliminar
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
}