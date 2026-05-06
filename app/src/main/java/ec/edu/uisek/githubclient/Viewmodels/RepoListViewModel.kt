package ec.edu.uisek.githubclient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclientcompose.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepoListViewModel : ViewModel(){
    //Maneja el estado de la lista de repositorios
    private val _repos= MutableStateFlow<List<Repository>>(emptyList())
    val repos: StateFlow<List<Repository>> =_repos.asStateFlow()
    //maneja el estado del progreso de la caraga de repositorios
    private val _isLoading= MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    //Maneja el estado del mensaje de errror
    private val _errorMsg = MutableStateFlow<String?>(value = null)
    val errorMsg: StateFlow<String?> = _errorMsg.asStateFlow()

    init {
        fetcheRepos()
    }


    fun fetcheRepos(){
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                val response = RetrofitClient.apiService.getRespository()
                _repos.value = response
            }catch (e: Exception){
                _errorMsg.value = "Error al cargar repositorio: ${e.localizedMessage}"
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }
        }
    }
}