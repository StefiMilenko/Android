package elfak.mosis.mobproj

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import elfak.mosis.mobproj.data.Posao
import elfak.mosis.mobproj.databinding.FragmentEditBinding
import elfak.mosis.mobproj.model.LocationViewModel
import elfak.mosis.mobproj.model.PosaoViewModel
import elfak.mosis.mobproj.state.ActionState
import java.util.*



class EditFragment : Fragment() {
    private val _actionState = MutableLiveData<ActionState>()
    val actionState: LiveData<ActionState> = _actionState

    private val posaoViewModel: PosaoViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by activityViewModels()

    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var db: DatabaseReference
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        db = Firebase.database.reference

        val editName: EditText = requireView().findViewById<EditText>(R.id.editposao_name_edit)
        val editDesc: EditText = requireView().findViewById<EditText>(R.id.editposao_desc_edit)
        val editSalary: EditText = requireView().findViewById<EditText>(R.id.editposao_salary_edit)
        val editStars: EditText = requireView().findViewById<EditText>(R.id.editposao_stars_edit)
        /*val editLong: EditText = requireView().findViewById<EditText>(R.id.editposao_long_edit)
        val longObserver = Observer<String> { newValue ->
            editLong.setText(newValue.toString())
        }
        locationViewModel.longitude.observe(viewLifecycleOwner, longObserver)
        val editLat: EditText = requireView().findViewById<EditText>(R.id.editposao_lat_edit)
        val latObserver = Observer<String> { newValue ->
            editLat.setText(newValue.toString())
        }
        locationViewModel.latitude.observe(viewLifecycleOwner, latObserver)*/
        if (posaoViewModel.selected!=null){
            editName.setText(posaoViewModel.selected?.name)
            editDesc.setText(posaoViewModel.selected?.description)
            editSalary.setText(posaoViewModel.selected?.salary)
            editStars.setText(posaoViewModel.selected?.stars)
        }
        val addButton: Button = requireView().findViewById<Button>(R.id.editposao_finish_button)
        addButton.isEnabled= false
        if (posaoViewModel.selected!= null){
            addButton.setText(R.string.editposao_name_label)
        }
        editName.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?){
                addButton.isEnabled=(editName.text.length>0)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int){

            }
        })
        addButton.setOnClickListener{
            val name:String =editName.text.toString()
            val desc: String = editDesc.text.toString()
            val salary: String = editSalary.text.toString()
            val stars: String = editStars.text.toString()
            //val long:String = editLong.text.toString()
            //val lat:String = editLat.text.toString()
            val long:String = locationViewModel.userlongitude.value!!
            val lat:String = locationViewModel.userlatitude.value!!
            if(posaoViewModel.selected!=null){
                posaoViewModel.selected?.name = name
                posaoViewModel.selected?.description = desc
                posaoViewModel.selected?.salary = salary
                posaoViewModel.selected?.stars = stars
                posaoViewModel.selected?.longitude= long
                posaoViewModel.selected?.latitude = lat
                val uuid = UUID.randomUUID().toString()
                val uid = auth.currentUser!!.uid
                _actionState.value = ActionState.Success
                putPosao(uuid, uid, name, desc, salary, stars, long, lat)
                locationViewModel.setLocation = true
            }
            else{
                val uuid = UUID.randomUUID().toString()
                val uid = auth.currentUser!!.uid
                _actionState.value = ActionState.Success
                putPosao(uuid, uid, name, desc, salary, stars, long, lat)
                locationViewModel.setLocation = true
            }

            posaoViewModel.selected = null
            locationViewModel.setLocation("", "")
            findNavController().navigate(R.id.action_EditFragment_to_MapFragment)
        }
        val cancelButton: Button = requireView().findViewById<Button>(R.id.editposao_cancel_button)
        cancelButton.setOnClickListener{
            posaoViewModel.selected = null
            locationViewModel.setLocation("", "")
            findNavController().popBackStack()

        }
    }

    private fun putPosao(
        uuid: String,
        uid: String,
        name: String,
        desc: String,
        salary: String,
        stars: String,
        long: String,
        lat: String
    ) {
        val posao = Posao(uuid,
            uid,
            name,
            desc,
            salary,
            stars,
            long,
            lat,
            makeSubstrings(name!!))
        _actionState.value = ActionState.Success
        val database = Firebase.database("https://mobproj-1b699-default-rtdb.europe-west1.firebasedatabase.app/")

        val myRef = database.reference.child("posao").child(uuid).setValue(posao)
            .addOnSuccessListener{
                _actionState.value = ActionState.Success
            }
            .addOnFailureListener { e->
                _actionState.value = ActionState.ActionError("Upload error: ${e.message}")
            }

    }
    private fun makeSubstrings (string: String): List<String>{
        val size = string.length
        val substrings: MutableList<String> = mutableListOf()
        for (substringSize in 1 ..size){
            substrings.add(string.substring(0, substringSize))
        }
        return substrings
    }

    override fun onDestroyView() {
        super.onDestroyView()
        posaoViewModel.selected=null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_list-> {
                this.findNavController().navigate(R.id.action_EditFragment_to_ListFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

}
