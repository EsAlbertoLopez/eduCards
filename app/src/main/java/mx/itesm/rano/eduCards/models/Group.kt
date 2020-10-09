package mx.itesm.rano.eduCards.models

class Group (val groupKey: String, val name: String, val courseKey: String){
    companion object{
        val groups = arrayOf(
            Group("TI38", "2A", "T138"),
            Group("TI40", "2B", "TI40"),
            Group("TI42", "2C", "TI42")
        )
    }
}