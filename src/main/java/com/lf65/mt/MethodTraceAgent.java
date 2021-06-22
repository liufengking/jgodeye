package com.lf65.mt;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class MethodTraceAgent {
    
    public static void premain(String agentArgs, Instrumentation inst) {
        initAgent(agentArgs);
        new AgentBuilder
                .Default()
                .type(ElementMatchers.nameStartsWith(MethodTraceConfig.getArgsStartWith()))
                .transform(
                    ((builder, typeDescription, classLoader, module) ->
                     builder.visit(Advice.to(MethodTraceAdvice.class).on(ElementMatchers.any())))
                )
                .installOn(inst);
    }
    
    public static void initAgent(String args) {
        MethodTraceConfig.initConfig(args);
        MethodTraceLogger.initLogger();
    }
}
