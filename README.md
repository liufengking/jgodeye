[中文](README_CN.md) | **English**
## jgodeye
![avatar](https://jgodeye.oss-cn-beijing.aliyuncs.com/jgodeye.png)
This is an agent tool which can dynamic trace java method invoke stack.
### how to use
- local environment
  - use ```mvn clean compile package``` command to generate target bin, for example, target dir: xxx/jgodeye/dist/jgodeye-0.0.1-bin
  - add jvm args ```-javaagent:xxx/jgodeye/dist/jgodeye-0.0.1-bin/jgodeye-trace-agent.jar=jgodeyeHome:xxx/jgodeye/dist/jgodeye-0.0.1-bin``` to trace.
  - modify xxx/jgodeye/dist/jgodeye-0.0.1-bin/jgodeye.properties to controll trace info 
- prod environment
  - use ```wget https://jgodeye.oss-cn-beijing.aliyuncs.com/jgodeye-0.0.1-bin.zip``` command to download the file 
  - use ```unzip xxx/jgodeye/dist/jgodeye-0.0.1-bin.zip``` command to unzip file.
  - add jvm args ```-javaagent:xxx/jgodeye/dist/jgodeye-0.0.1-bin/jgodeye-trace-agent.jar=jgodeyeHome:xxx/jgodeye/dist/jgodeye-0.0.1-bin``` to trace.
  - modify xxx/jgodeye/dist/jgodeye-0.0.1-bin/jgodeye.properties to controll trace info
### trace result
- the trace result produce each thread stack with a markdown file, actually， you can view it by ide like this
  ![avatar](https://jgodeye.oss-cn-beijing.aliyuncs.com/example.jpeg)
### road map
- more args to control trace info
- add attach java progress 
- idea plugin
### License
The jgodeye is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).