package elfak.mosis.mobproj

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.AdapterView.AdapterContextMenuInfo
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import elfak.mosis.mobproj.data.Posao
import elfak.mosis.mobproj.databinding.FragmentListBinding
import elfak.mosis.mobproj.model.PosaoViewModel

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private var auth: FirebaseAuth = Firebase.auth
    private val binding get() = _binding!!
    private val posaoViewModel: PosaoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        posaoViewModel.fetchAllPosao()
        val posaoList: ListView = requireView().findViewById<ListView>(R.id.mobproj_list)
        posaoList.adapter = ArrayAdapter<Posao>(view.context, android.R.layout.simple_list_item_1, posaoViewModel.PosaoList)
        posaoList.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var posao: Posao = p0?.adapter?.getItem(p2) as Posao
                posaoViewModel.selected = posao
                findNavController().navigate(R.id.action_ListFragment_to_ViewFragment)
            }
        })
        posaoList.setOnCreateContextMenuListener(object: View.OnCreateContextMenuListener{
        override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
            val info = menuInfo as AdapterContextMenuInfo
            val posao: Posao = posaoViewModel.PosaoList[info.position]
            menu.setHeaderTitle(posao.name)
            menu.add(0, 1, 1, "View posao")
            menu.add(0, 2, 2, "Edit posao")
            menu.add(0, 3, 3, "Delete posao")
            menu.add(0, 4, 4, "Show on map")
        }
    })
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterContextMenuInfo
        if (item.itemId ==1) {
            posaoViewModel.selected = posaoViewModel.PosaoList[info.position]
            this.findNavController().navigate(R.id.action_ListFragment_to_ViewFragment)
        }
        else if (item.itemId == 2) {
            if (posaoViewModel.PosaoList[info.position].userId == auth.currentUser!!.uid)
            {
                posaoViewModel.selected=posaoViewModel.PosaoList[info.position]
                this.findNavController().navigate(R.id.action_ListFragment_to_EditFragment)
            }
            else{ Toast. makeText(getActivity(), "Ne mozete azurirati tudi posao!", Toast. LENGTH_SHORT).show() }
        }
        else if (item.itemId == 3){
            if (posaoViewModel.PosaoList[info.position].userId == auth.currentUser!!.uid){
                posaoViewModel.PosaoList.removeAt(info.position)
                val posaoList: ListView = requireView().findViewById<ListView>(R.id.mobproj_list)
                posaoList.adapter = this@ListFragment.context?.let{ ArrayAdapter<Posao>(it, android.R.layout.simple_list_item_1,posaoViewModel.PosaoList)}
            }
            else{ Toast. makeText(getActivity(), "Ne mozete obrisati tudi posao!", Toast. LENGTH_SHORT).show() }
            }
        else if (item.itemId == 4){
            posaoViewModel.selected = posaoViewModel.PosaoList[info.position]
            this.findNavController().navigate(R.id.action_ListFragment_to_MapFragment)
        }
        return super.onContextItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        posaoViewModel.selected = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_edit ->{
                this.findNavController().navigate(R.id.action_ListFragment_to_EditFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.action_list)
        item.isVisible = false
    }
}