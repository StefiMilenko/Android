package elfak.mosis.mobproj.data

data class Posao(var name:String, var description:String, var longitude:String, var latitude:String){
    override fun toString(): String = name
}
