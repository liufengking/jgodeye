## method-invoke-trace
This is an agent tool which can dynamic trace java method invoke stack.
### how to use
- use ```mvn clean compile package``` command to generate target jar, for example: xxx/method-invoke-trace/target/method-invoke-trace-1.0-SNAPSHOT-jar-with-dependencies.jar
- use ```-javaagent:xxx/method-invoke-trace/target/method-invoke-trace-1.0-SNAPSHOT-jar-with-dependencies.jar=startWith:xxx,outPut:xxx``` comand to trace.
### trace result
- the trace result produce each thread stack with a markdown file, actually， you can view it by ide like this
  ![avatar](imgs/example.jpeg)
### road map
- more args to control trace info
- add attach java progress 
- idea plugin
### License
The method-invoke-tracer is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).