package common.kotlin

class SomeKotlinClass {
    fun call() {
        val ddd = SomeData()
        ddd.age = 12
        println(ddd)
    }
}


fun main() {
    println("something")
    val obj = SomePojo()
    obj.name = "test"
    val s: String = obj.name
    obj.age = 12
    val v = obj.isHuman
    obj.isHuman = !v
    println(obj)

    val ddd = SomeData()

    // JavaUsage.cycleUsage()
}