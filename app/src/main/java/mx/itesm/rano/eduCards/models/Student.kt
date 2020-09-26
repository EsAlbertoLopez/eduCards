package mx.itesm.rano.eduCards.models

class Student(val studentId: String, val name: String, val description: String, val type: String) {
    companion object {
        val students = arrayOf(
            Student("XXXXXXXXXX", "Juan", "Aventar papeles", "Comportamiento Inadecuado"),
            Student("YYYYYYYYYY", "Pancho", "Tirar basura", "Comportamiento Inadecuado"),
            Student("ZZZZZZZZZZ", "Don Juan", "Ligue Inapropiado", "Comportamiento Inadecuado"),
            Student("AAAAAAAAAA", "Natalia", "Aventar regla", "Violencia Física")
        )
    }
}