## work with cglib or asm throw "java.lang.VerifyError: Stack map does not match the one at exception"
- add vm args ```-noverify``` 
## work with spring boot throw "IncompatibleClassChangeError"
- AgentBuider add 
```
new AgentBuilder.Default()
    .disableClassFormatChanges()
    .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
    .type(initJunction())
    .transform(
        ((builder, typeDescription, classLoader, module) ->
            builder.visit(net.bytebuddy.asm.Advice.to(Advice.class).on(ElementMatchers.any())))
    )
    .installOn(inst);
```
