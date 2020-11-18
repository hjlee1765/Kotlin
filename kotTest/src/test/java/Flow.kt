import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis
import org.junit.jupiter.api.Test

//https://medium.com/hongbeomi-dev/코틀린의-코루틴-5-asynchronous-flow-1부-600877d99b16

internal class FlowTestClass{

    private fun foo(): Flow<Int> = flow{
        for ( i in 1..3){
            kotlin.io.println("Flow started")
            delay(100)
            //Thread.sleep(100)
            emit(i)
        }
    }

    //두개의 코루틴이 있고, 번갈아가면서 처리하는 예제.
    //suspend 함수에 걸리면 runBlocking 밖으로 나가게되고, 메인스레드는 중단되지 않고
    //다른 suspend 함수를 찾거나 resume 되어지는 다른 코드들을 찾는다.
    @Test
    fun flowConcurrencyTest() = runBlocking {
        println("start")
        launch {
            for(k in 1..3){
                println("not blocked")
                delay(100)
            }
        }
        foo().collect { println(it) }
    }

    //flow 는 cold stream 이라, flow.collect 하기 전에는 실행되지 않는다.
    @Test
    fun flowColdStream() = runBlocking {
        println("Calling foo..")
        val flow = foo()
        println("Calling collect...")
        flow.collect { println(it) }
        println("Calling collect again...")
        flow.collect { println(it) }
    }

    private suspend fun performRequest(request: Int): String {
        delay(1000) // 장기간에 걸친 비동기 작업
        return "response $request"
    }

    //transform을 사용하면 flow의 복잡한 변환도 가능하다. 임의의 값을 임의의 횟수로 방출 가능하다.
    //collect 를 하지않으면 애초에 실행도 안된다.
    //emit 하는 대로, collect 에 의해 print된다.
    @Test
    fun flowTransform(){
        runBlocking {
            (1..3).asFlow()
                    .transform{ request ->
                        emit("Making request $request")
                        emit(performRequest(request))
                    }
                    .collect{println(it)}
        }
    }

    //Flow 는 오퍼레이션 사용 가능하다. Flow<String>으로 변환됨.
    @Test
    fun flowMap(){
        runBlocking {
            (1..3).asFlow()
                    .map { request -> performRequest(request) }
                    .collect { response -> println(response) }
        }
    }

    fun numbers(): Flow<Int> = flow{
        try {
            emit(1)
            emit(2)
            println("This line will not execute")
            emit(3)
        }finally {
            println("Finally in numbers")
        }
    }

    //Take 함수. 크기제한. 취소시 exception발생하고, finally에서 자원 관리(해제) 가능하다.
    @Test
    fun flowTake(){
        runBlocking {
            numbers()
                    .take(2)
                    .collect { println(it) }
        }
    }
}
