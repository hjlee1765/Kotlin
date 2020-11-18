import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

//[새차원, 코틀린 코루틴(Coroutines)] 4. Composing Suspending Functions
fun main(){
    //sequentialTest() //마법코드
    asyncTest()
    //asyncLazyTest()
}

//runBlocking 하위에 coroutine 2개 하이라키로 생성. structured concurrency 에 의해 둘 다 끝나야 상위 coroutine 끝남.
//두 개의 coroutine 실행하는 동안, runBlocking 에 의해 메인스레드는 Blocked.
fun asyncTest() = runBlocking<Unit> {
    println("main")
    val time = measureTimeMillis {
        println("the answer is ${concurrentSum()}")
    }
    println("Complete d in $time ms")
}

// structured concurrency를 사용했을경우 exception 전파가 용이하다.
// exception 발생시 상위 coroutine을 포함하여 아래에 있는 모든 coroutine으로 전파되면서 모두 취소가 된다.
// 아래 예시에서는 onw 에서 exception이발생시, two에도 전파가 된다.
// 단, global scope에서 실행시키면 전파가 안된다
suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }

    println("processing..")
/*  delay(1000L)
    println("Exception")
    throw Exception()*/

    one.await() + two.await()
}

fun asyncLazyTest() = runBlocking {
    println("main")
    val time = measureTimeMillis {
        val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
        val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
        one.start()
        two.start()
        println("the answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
}

//마법같은 코드. (coroutine 1개 기준)
//비동기 함수를 순차적으로 작성했더니..순차실행이 되며 결과도 문제없이 나온다.
//그 동안 메인스레드의 ui를 block하지 않는다.
fun sequentialTest() = runBlocking {
    println("main")
    val time = measureTimeMillis {
        //CPS 에 의해 callback 방식으로, 코드가 변환된다.
        val one = doSomethingUsefulOne()
        val two = doSomethingUsefulTwo()
        println("The answer is ${one + two}")
    }
    println("Completed in $time ms")
}

suspend fun doSomethingUsefulOne(): Int{
    println("start one")
    //시간이 걸리는 일을 수행하는동안 coroutine 이 중단된다.
    delay(1000L)
    //작업이 끝나면 해당 coroutine 이 재개된다. (Resume)
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