package mx.itesm.rano.eduCards.models

class Student(val studentId: String, val name: String, val description: String) {
    companion object {
        val students = arrayOf(
            Student("XXXXXXXXXX", "Juan", "Aventar papeles"),
            Student("YYYYYYYYYY", "Pancho", "Tirar basura"),
            Student("ZZZZZZZZZZ", "Don Juan", "Ligue Inapropiado"),
            Student("AAAAAAAAAA", "Natalia", "Aventar regla")
        )
    }
}