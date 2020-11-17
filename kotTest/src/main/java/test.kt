import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    println("main")
    val time = measureTimeMillis {
        val one = async { doSomethingUsefulOne() }
        val two = async { doSomethingUsefulTwo() }
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
}

suspend fun doSomethingUsefulOne(): Int{
    println("start one")
    delay(1000L)
    println("end one")
    return 10
}

suspend fun doSomethingUsefulTwo(): Int{
    println("start two")
    delay(1000L)
    println("end two")
    return 20
}

fun <T>println(msg: T){
    kotlin.io.println("$msg [${Thread.currentThread().name}]")
}
