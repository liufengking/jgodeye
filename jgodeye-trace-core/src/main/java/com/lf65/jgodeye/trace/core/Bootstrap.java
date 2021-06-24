package com.lf65.jgodeye.trace.core;

import com.lf65.jgodeye.common.Context;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class Bootstrap {
    
    public static void bind(String jgodeyeHome, Instrumentation inst) {
        init(jgodeyeHome);
        build(inst);
    }
    
    private static void init(String jgodeyeHome) {
        Context.initContext(jgodeyeHome);
        Logger.initLogger();
    }
    
    private static void build(Instrumentation inst) {
        new AgentBuilder.Default().type(ElementMatchers.nameStartsWith(Context.getJgodeyeTraceStartwith()))
            .transform(
                ((builder, typeDescription, classLoader, module) ->
                 builder.visit(net.bytebuddy.asm.Advice.to(Advice.class).on(ElementMatchers.any())))
            )
            .installOn(inst);
    }
}
