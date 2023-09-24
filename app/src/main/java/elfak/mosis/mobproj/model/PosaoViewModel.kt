package elfak.mosis.mobproj.model

import android.annotation.SuppressLint
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
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

    var isUserSet: Boolean = false

    private val _userlat = MutableLiveData<String>()
    private val _userlog = MutableLiveData<String>()

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

    private val _namefilter = MutableLiveData<String>()
    val namefilter : LiveData<String> = _namefilter
    private val _salaryfilter = MutableLiveData<String>()
    val salaryfilter : LiveData<String> = _salaryfilter
    private val _starsfilter = MutableLiveData<String>()
    val starsfilter : LiveData<String> = _starsfilter
    private val _radiusfilter = MutableLiveData<String>()
    val radiusfilter : LiveData<String> = _radiusfilter


    var funUpdatePosao: ((Posao)-> Unit)? = null
    var posaoLiveData: MutableLiveData<List<Posao>> = MutableLiveData()


    var PosaoList: ArrayList<Posao> = ArrayList()

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
            var smallString = string.substring(0, substringSize).lowercase()
            smallString = smallString.lowercase()
            substrings.add(smallString.lowercase())
        }
        return substrings
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

    fun fetchAllPosao() {
        var posaoList = mutableListOf<Posao>()
        ref = FirebaseDatabase.getInstance().reference.child("posao")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val posao = dataSnapshot.getValue(Posao::class.java)
                    posao?.let {
                        posaoList.add(it)
                    }
                }
                if (_namefilter.value != null && _namefilter.value!! != "") {
                    posaoList = posaoList.filter{it.names!!.contains(_namefilter.value!!)} as MutableList<Posao>
                }
                if (_salaryfilter.value != null && _salaryfilter.value!! != "") {
                    posaoList = posaoList.filter{it.salary!!.toInt() >= _salaryfilter.value!!.toInt()} as MutableList<Posao>
                }
                if (_starsfilter.value != null && _starsfilter.value!! != "") {
                    posaoList = posaoList.filter{it.stars!!.toInt() >= _starsfilter.value!!.toInt()} as MutableList<Posao>
                }
                if (_radiusfilter.value != null && _radiusfilter.value!! != "" && isUserSet!= false) {
                    posaoList = posaoList.filter{filterByRadius(it)} as MutableList<Posao>
                }
                PosaoList = posaoList as ArrayList<Posao>
                posaoLiveData.postValue(posaoList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    private fun filterByRadius(posao: Posao) : Boolean {
        val km = _radiusfilter.value!!.toDouble()
        val rad = km / 111
        if (posao.latitude!!.toDouble()<=_userlat.value!!.toDouble()+rad &&
            posao.latitude!!.toDouble()>=_userlat.value!!.toDouble()-rad &&
            posao.longitude!!.toDouble()<=_userlog.value!!.toDouble()+rad &&
            posao.longitude!!.toDouble()>=_userlog.value!!.toDouble()-rad){
            return true
        }
        return false
    }

    fun addUserPosition(lat : String, log : String) {
        _userlat.postValue(lat)
        _userlog.postValue(log)
    }


    fun setFilters(filterName : String, filterSalary: String, filterStars: String, filterRadius : String){
        if (filterName != null && filterName != "")
        {
            _namefilter.value = filterName
        }
        if (filterSalary != null && filterSalary != "")
        {
            _salaryfilter.value = filterSalary
        }
        if (filterStars != null && filterStars != "")
        {
            _starsfilter.value = filterStars
        }
        if (filterRadius != null && filterRadius != "")
        {
            _radiusfilter.value = filterRadius
        }
    }


    /*fun setNameQuery(){
        if (posaoNameFilter.value != null && _nameFilterOn.value!!) {
            query = db.collection("posao").orderBy("name")
                .WhereArrayContains("nameQueryList", posaoNameFilter.value!!)
        }
    }
    private fun resetNameFilter(){
        query = db.collection("posao").orderBy("name")
    }
*/
}