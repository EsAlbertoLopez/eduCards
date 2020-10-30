package mx.itesm.rano.eduCards.models

class Instructor(val key: String="", var name: String="", var institute: String="",
                 var arrCourses: MutableList<String> = mutableListOf<String>(),
                 var arrGroups: MutableList<String> = mutableListOf<String>(),
                 var arrCards: MutableList<String> = mutableListOf<String>()) {
}