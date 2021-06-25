package org.jgodeye.trace.core;

import org.jgodeye.common.Context;

import java.util.List;

public class Advice {
    
    @net.bytebuddy.asm.Advice.OnMethodEnter
    public static void enter(@net.bytebuddy.asm.Advice.Origin Class<?> clazz,
                             @net.bytebuddy.asm.Advice.Origin("#m") String methodName) {
        if (excludesContains(clazz.getTypeName())) {
            return;
        }
        org.jgodeye.trace.core.PreTabStack.log(clazz.getTypeName() + "." + methodName + "()");
        org.jgodeye.trace.core.PreTabStack.push();
    }
    
    @net.bytebuddy.asm.Advice.OnMethodExit
    public static void exit(@net.bytebuddy.asm.Advice.Origin Class<?> clazz) {
        if (excludesContains(clazz.getTypeName())) {
            return;
        }
        org.jgodeye.trace.core.PreTabStack.pop();
    }
    
    public static boolean excludesContains(String typeName) {
        List<String> excludes = Context.getPacakgeExcludes();
        for (String packet : excludes) {
            if (typeName.startsWith(packet)) {
                return true;
            }
        }
        return false;
    }
    
}
