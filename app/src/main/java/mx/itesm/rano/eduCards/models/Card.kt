package mx.itesm.rano.eduCards.models

class Card (val key: String, val type: String, val descrption:String){
    companion object{
        val registers = arrayOf(
            Card("11111", "Violencia Física","Golpeo a pepe")
        )
    }
}