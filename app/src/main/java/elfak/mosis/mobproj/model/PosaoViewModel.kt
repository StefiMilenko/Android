package elfak.mosis.mobproj.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.database.ktx.database
import elfak.mosis.mobproj.data.Posao
import elfak.mosis.mobproj.state.ActionState
import java.util.*
import kotlin.collections.ArrayList

class PosaoViewModel: ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth

    private lateinit var db: FirebaseDatabase
    private lateinit var ref: DatabaseReference

    private val storage = Firebase.storage
    private val _actionState = MutableLiveData<ActionState>()
    val actionState: LiveData<ActionState> = _actionState

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name
    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description
    private val _stars = MutableLiveData<String>()
    val stars: LiveData<String> = _stars
    private val _salary = MutableLiveData<String>()
    val salary: LiveData<String> = _salary
    private val _longitude = MutableLiveData<String>()
    val longitude: LiveData<String> = _longitude
    private val _latitude = MutableLiveData<String>()
    val latitude: LiveData<String> = _latitude

    var funUpdatePosao: ((Posao)-> Unit)? = null


    val PosaoList: ArrayList<Posao> = ArrayList()

    fun addPosao (posao: Posao){
        PosaoList.add(posao)
    }
    fun getPosaoList(): List<Posao> {
        return PosaoList
    }
    var selected: Posao? = null

    private fun makeSubstrings (string: String): List<String>{
        val size = string.length
        val substrings: MutableList<String> = mutableListOf()
        for (substringSize in 1 ..size){
            substrings.add(string.substring(0, substringSize))
        }
        return substrings
    }

    fun putPosaotoDB(){
        val uuid = UUID.randomUUID().toString()
    }

    fun putPosao(uuid: String){
        val posao = Posao(uuid,
            auth.currentUser!!.uid,
            _name.value,
            _description.value,
            _salary.value,
            _stars.value,
            _longitude.value,
            _latitude.value,
            makeSubstrings(_name.value!!))
        ref = FirebaseDatabase.getInstance().getReference("Posao")
        ref.child(name.toString()).setValue(posao)
            .addOnSuccessListener{
                if (funUpdatePosao != null) {
                    funUpdatePosao!!.invoke(posao)
                }
                _actionState.value = ActionState.Success
            }
            .addOnFailureListener { e->
                _actionState.value = ActionState.ActionError("Upload error: ${e.message}")
            }
    }

    fun setValues(posao: Posao) {
        _name.value = posao.name!!
        _description.value = posao.description!!
        _salary.value = posao.salary!!
        _stars.value=posao.stars!!
    }

    fun Check(): Boolean{
        if (_name.value!!.isBlank()){
            _actionState.value = ActionState.ActionError("Morate dodati ime!")
            return false
        }
        return true
    }

    /*fun setNameQuery(){
        if (posaoNameFilter.value != null && _nameFilterOn.value!!) {
            query = db.cillection("posao").orderBy("name")
                .WhereArrayContains("nameQueryList", posaoNameFilter.value!!)
        }
    }
    private fun resetNameFilter(){
        query = db.collection("posao").orderBy("name")
    }
*/
}