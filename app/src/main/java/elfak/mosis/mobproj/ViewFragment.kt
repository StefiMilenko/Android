package elfak.mosis.mobproj

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import elfak.mosis.mobproj.databinding.FragmentViewBinding
import elfak.mosis.mobproj.model.PosaoViewModel


class ViewFragment : Fragment() {
    private val posaoViewModel: PosaoViewModel by activityViewModels()
    private var _binding: FragmentViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewposaoNameText.text=posaoViewModel.selected?.name
        binding.viewposaoDescText.text=posaoViewModel.selected?.description
        binding.viewposaoFinishButton.setOnClickListener{
            findNavController().popBackStack()
        }
    }
    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }
}