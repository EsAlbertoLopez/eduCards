package mx.itesm.rano.eduCards.models

class Instructor(val key: String="", var name: String="",
                 var arrCourses: MutableList<String>,
                 var arrGroups: MutableList<String>,
                 var arrCards: MutableList<String>) {
}