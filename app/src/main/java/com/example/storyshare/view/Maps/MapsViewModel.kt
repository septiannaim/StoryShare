import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyshare.data.UserRepository
import com.example.storyshare.data.pref.UserModel
import com.example.storyshare.utils.Outcome
import com.example.storyshare.response.StoriesResponse

class MapsViewModel(private val repository: UserRepository) : ViewModel() {

    fun getLocationStory(token: String): LiveData<Outcome<StoriesResponse>> = repository.getAllStoryLocation(token)

    fun getUser(): LiveData<UserModel> {
        return repository.fetchCurrentUserData()
    }
}
