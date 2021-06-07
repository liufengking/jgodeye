package com.lf65.mt;

import net.bytebuddy.asm.Advice;

public class MethodTraceAdvice {
    
    @Advice.OnMethodEnter
    public static void enter(@Advice.Origin Class<?> clazz,
                             @Advice.Origin("#m") String methodName) {
        MethodTraceStack.log(clazz.getTypeName() + "." + methodName + "()");
        MethodTraceStack.push();
    }
    
    @Advice.OnMethodExit
    public static void exit() {
        MethodTraceStack.pop();
    }
}
