package elfak.mosis.mobproj.model

import androidx.lifecycle.ViewModel
import elfak.mosis.mobproj.data.Posao

class PosaoViewModel: ViewModel() {
    val PosaoList: ArrayList<Posao> = ArrayList()

    fun addPosao (posao: Posao){
        PosaoList.add(posao)
    }
    fun getPosaoList(): List<Posao> {
        return PosaoList
    }
    var selected: Posao? = null
}