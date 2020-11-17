import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

//[새차원, 코틀틴 코루틴(Coroutines)] 5. Coroutines under the hood
fun main(){

    //Continuation-Passing Style 은 결국 Callback 이다.
    //순차적으로 작성된 비동기 함수들이 어떻게 block하지 않고 정상동작하는가?
    // -> 컴파일 할때, label이 찍히고, resume하면서 반복적으로 Continuation 객체를 파라메터로 넘긴다.
    // -> 컴파일 되면, 순차적으로 작성된 코드들이 Switch 문으로 실행되며 labeling이된다.
    // -> Continuation객체는 상태를 가지고있으며, coroutine을 재개할 수 있는 인터페이스(resume with)를 가지고있다.
    // -> 자세한건 유튜브 영상 확인.


}