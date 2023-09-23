package elfak.mosis.mobproj.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import elfak.mosis.mobproj.R
import elfak.mosis.mobproj.data.Posao

class PosaoAdapter(private var posaoList: List<Posao>) : RecyclerView.Adapter<PosaoAdapter.ViewHolder>() {

    private var onItemClickListener: ((Posao) -> Unit)? = null

    fun setOnItemClickListener(listener: (Posao) -> Unit) {
        onItemClickListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val posaoNameTextView: TextView = itemView.findViewById(R.id.posaoNameTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val posao = posaoList[position]
                    onItemClickListener?.invoke(posao)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_posao, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = posaoList[position]
        holder.posaoNameTextView.text = currentItem.name
    }

    override fun getItemCount() = posaoList.size

    fun submitList(list: List<Posao>?) {
        this.posaoList = list!!
    }
}