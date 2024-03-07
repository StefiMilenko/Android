package elfak.mosis.mobproj

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import elfak.mosis.mobproj.adapter.PosaoAdapter
import elfak.mosis.mobproj.model.PosaoViewModel

class PosaoViewFragment : Fragment() {
    private val posaoViewModel: PosaoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posao_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        val posaoAdapter = PosaoAdapter(emptyList())
        recyclerView.adapter = posaoAdapter
        posaoViewModel.posaoLiveData.observe(viewLifecycleOwner, Observer { posaoList ->
            posaoAdapter.submitList(posaoList)
        })

        posaoAdapter.setOnItemClickListener { posao ->
            val salary = posao.salary
            val rating = posao.stars
        }
   }

}
