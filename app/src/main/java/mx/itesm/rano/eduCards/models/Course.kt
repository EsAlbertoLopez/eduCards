package mx.itesm.rano.eduCards.models

class Course (val courseKey: String, val name: String, val startingDate: String, val endDate: String){
    companion object{
        val courses = arrayOf(
            Course("TI38", "Matematicas", "8 de octubre", "15 de octubre"),
            Course("TI40", "Fisica", "8 de octubre", "15 de octubre"),
            Course("TI42", "Quimica", "8 de octubre", "15 de octubre")
        )
    }
}