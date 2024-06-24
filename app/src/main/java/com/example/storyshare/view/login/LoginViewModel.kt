
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyshare.data.UserRepository
import com.example.storyshare.data.pref.UserModel
import kotlinx.coroutines.launch

class   LoginViewModel(private val userRepository: UserRepository) : ViewModel() {


    fun authenticateUser(email: String, password: String) = userRepository.loginUser(email, password)

    fun storeUser(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveUserSession(user)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
