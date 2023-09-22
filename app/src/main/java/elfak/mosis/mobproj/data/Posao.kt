package elfak.mosis.mobproj.data

import org.checkerframework.checker.index.qual.SubstringIndexBottom

data class Posao(var Id: String? = null,
                 var userId: String? = null,
                 var name: String? = null,
                 var description: String? = null,
                 var stars: String? = null,
                 var salary: String? = null,
                 var longitude: String? = null,
                 var latitude: String? = null,
                var names: List<String?>? = null){
    override fun toString() : String= name.toString()
}


