package elfak.mosis.mobproj.model

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginRegistrationViewModel : ViewModel() {
    private var auth: FirebaseAuth = Firebase.auth

    private val _picture = MutableLiveData<Bitmap>()
    val picture: LiveData<Bitmap> = _picture

    private val _fName = MutableLiveData<String>()
    val fName: LiveData<String> = _fName

    private val _lName = MutableLiveData<String>()
    val lName: LiveData<String> = _lName

    private val _password = MutableLiveData<String>()
    val password:LiveData<String> = _password

    private val _email = MutableLiveData<String>()
    val email:LiveData<String> = _email
}
