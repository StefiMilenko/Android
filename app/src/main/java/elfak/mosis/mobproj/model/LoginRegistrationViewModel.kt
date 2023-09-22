package elfak.mosis.mobproj.model

import android.graphics.Bitmap
import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import elfak.mosis.mobproj.state.ActionState

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

    private val _actionState = MutableLiveData<ActionState>(ActionState.Idle)
    val actionState: LiveData<ActionState> = _actionState
    fun setPicture(picture:Bitmap){
        _picture.value=picture
    }
    fun onFNameTextChanged(p0: Editable?){
        _fName.value = p0.toString()
    }
}
