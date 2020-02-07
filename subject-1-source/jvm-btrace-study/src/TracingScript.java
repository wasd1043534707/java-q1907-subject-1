/* BTrace Script Template */

import com.sun.btrace.annotations.*;

import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class TracingScript {
    /* put your code here */
    @OnMethod(
            clazz = "java.nio.ByteBuffer",
            method = "allocateDirect"
    )
    public static void traceExecute() {
        println("who call ByteBuffer.allocateDirect :");
        jstack();
    }
}