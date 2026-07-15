import android.app.Application
import ec.edu.uisek.githubclientcompose.services.RetrofitClient

class GithubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RetrofitClient.init(this)
    }
}