package mx.itesm.rano.eduCards.models

class register (val key: String, val type: String, val descrption:String){
    companion object{
        val registers = arrayOf(
            register("11111", "Violencia Física","Golpeo a pepe")
        )
    }
}