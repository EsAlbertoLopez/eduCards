package mx.itesm.rano.eduCards.models

class Course (val courseKey: String, val name: String){
    companion object{
        val courses = arrayOf(
            Course("TI38", "Matematicas"),
            Course("TI40", "Fisica"),
            Course("TI42", "Quimica")
        )
    }
}