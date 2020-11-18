import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlin.system.measureTimeMillis

//https://wooooooak.github.io/kotlin/2019/08/25/코틀린-코루틴-개념-익히기/
//동시성 프로그래밍 테스트.
//코루틴은 병행성이 아닌, 동시성을 지원하는 개념이다.

//코루틴은, 진입점과 탈출점이 여러개다. 언제든 나갔다 들어올 수 있다.
//suspend 함수를 만나면 바로 코루틴을 나간다. 해당 suspend함수 작업이 완료되면 다시 코루틴으로 진입해서 하위 코드를 실행한다.
fun main() {
    kotlin.io.println("main start")
    GlobalScope.launch {
        println("coroutine start")
        foo().forEach { println(it) }
        println("coroutine end")
    }
    kotlin.io.println("something UI processing")
    Thread.sleep(10000L)
}

suspend fun foo(): List<Int>{
    delay(2000L)
    return listOf(1,2,3)
}

suspend fun foo2(): Sequence<Int> = sequence {
    for(i in 1..3){
        Thread.sleep(100)
        yield(i)
    }
}