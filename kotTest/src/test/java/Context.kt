import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis
import org.junit.jupiter.api.Test

//[새차원, 코틀린 코루틴(Coroutines)] 6. Coroutine Context and Dispatchers

internal class ContextClass{

    //코루틴은 coroutine context 에서 실행이 된다. coroutine context 에 dispatcher가 저장된다. job도 저장된다.
    //dispatcher 는 코루틴이 어떤 스레드에서 실행될지 결정하게 해주는 요소.
    //모든 코루틴 빌더는 옵셔널 파라메터로 context를 받고있다.
    @Test
    fun dispatcherAndThread(){
        runBlocking {
            //자신을 호출한 코루틴 스코프의 context를 상속받아 실행된다. 즉 같은거 씀.
            launch {
                println("main runBlocking: thead ${Thread.currentThread().name}")
            }
            launch(Dispatchers.Unconfined) {
                println("Unconfined: thead ${Thread.currentThread().name}")
            }
            launch(Dispatchers.Default) {
                println("Default: thead ${Thread.currentThread().name}")
            }
            //새롭게 스레드 생성해서 코루틴 스콥을 연다. use를 사용하면 close()를 해줘서 메모리 릭 방지해준다.
            newSingleThreadContext("MyOwnThread").use {
                launch(it) {
                    println("newSingleThreadContext: thead ${Thread.currentThread().name}")
                }
            }
        }
    }

    private fun log(msg: String) = println("[${Thread.currentThread().name}]" + msg)

    //withContext를 사용해서 스레드 점프 가능하다.
    @Test
    fun withContext(){
        newSingleThreadContext("Ctx1").use{ ctx1 ->
            newSingleThreadContext("Ctx2").use{ ctx2 ->
                runBlocking(ctx1) {
                    log("Started in ctx1")
                    withContext(ctx2){
                        log("Working in ctx2")
                    }
                    log("Back to ctx1")
                }
            }
        }
    }

}
