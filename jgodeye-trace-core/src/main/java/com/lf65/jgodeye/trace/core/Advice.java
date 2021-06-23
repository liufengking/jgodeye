package com.lf65.jgodeye.trace.core;

public class Advice {
    
    @net.bytebuddy.asm.Advice.OnMethodEnter
    public static void enter(@net.bytebuddy.asm.Advice.Origin Class<?> clazz,
                             @net.bytebuddy.asm.Advice.Origin("#m") String methodName) {
        PreTabStack.log(clazz.getTypeName() + "." + methodName + "()");
        PreTabStack.push();
    }
    
    @net.bytebuddy.asm.Advice.OnMethodExit
    public static void exit() {
        PreTabStack.pop();
    }
}
