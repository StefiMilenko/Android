package elfak.mosis.mobproj

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import elfak.mosis.mobproj.data.Posao
import elfak.mosis.mobproj.model.LocationViewModel
import elfak.mosis.mobproj.model.PosaoViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [EditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditFragment : Fragment() {
    private val posaoViewModel: PosaoViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editName: EditText = requireView().findViewById<EditText>(R.id.editposao_name_edit)
        val editDesc: EditText = requireView().findViewById<EditText>(R.id.editposao_desc_edit)
        val editLong: EditText = requireView().findViewById<EditText>(R.id.editposao_long_edit)
        val longObserver = Observer<String> { newValue ->
            editLong.setText(newValue.toString())
        }
        locationViewModel.longitude.observe(viewLifecycleOwner, longObserver)
        val editLat: EditText = requireView().findViewById<EditText>(R.id.editposao_lat_edit)
        val latObserver = Observer<String> { newValue ->
            editLat.setText(newValue.toString())
        }
        locationViewModel.latitude.observe(viewLifecycleOwner, latObserver)
        if (posaoViewModel.selected!=null){
            editName.setText(posaoViewModel.selected?.name)
            editName.setText(posaoViewModel.selected?.description)
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
            val long:String = editLong.text.toString()
            val lat:String = editLat.text.toString()
            if(posaoViewModel.selected!=null){
                posaoViewModel.selected?.name = name
                posaoViewModel.selected?.description = desc
                posaoViewModel.selected?.longitude= long
                posaoViewModel.selected?.latitude = lat
                locationViewModel.setLocation = true
            }
            else
                posaoViewModel.addPosao( Posao(name, desc, long, lat))
                locationViewModel.setLocation = true

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

    override fun onDestroyView() {
        super.onDestroyView()
        posaoViewModel.selected=null
    }
}
