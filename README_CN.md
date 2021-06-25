**中文** | [English](README.md)
## jgodeye
![avatar](https://jgodeye.oss-cn-beijing.aliyuncs.com/jgodeye.png)
神眼是一个以上帝视角俯瞰java程序的源码分析工具,可以用来生成java程序运行时调用脉络树。
### 快速开始 
- 调试环境 
  - 使用maven命令 ```mvn clean compile package``` 打包生成dist目录: xxx/jgodeye/dist/jgodeye-0.0.1-bin。
  - 添加jvm参数 ```-javaagent:xxx/jgodeye/dist/jgodeye-0.0.1-bin/jgodeye-trace-agent.jar=jgodeyeHome:xxx/jgodeye/dist/jgodeye-0.0.1-bin```。
  - 编辑 xxx/jgodeye/dist/jgodeye-0.0.1-bin/jgodeye.properties 文件,控制需要跟踪和排除的包。 
- 生产环境 
  - 使用命令 ```wget https://jgodeye.oss-cn-beijing.aliyuncs.com/jgodeye-0.0.1-bin.zip``` 下载神眼安装包。 
  - 使用命令 ```unzip xxx/jgodeye/dist/jgodeye-0.0.1-bin.zip```解压安装包。 
  - 添加jvm参数 ```-javaagent:xxx/jgodeye/dist/jgodeye-0.0.1-bin/jgodeye-trace-agent.jar=jgodeyeHome:xxx/jgodeye/dist/jgodeye-0.0.1-bin```。
  - 编辑 xxx/jgodeye/dist/jgodeye-0.0.1-bin/jgodeye.properties 文件,控制需要跟踪和排除的包。
### 跟踪结果 
- the trace result produce each thread stack with a markdown file, actually， you can view it by ide like this
- 神眼会把java进程的每个线程的运行时调用树输出到单独的markdown文件，可以用markdown阅读器折叠不关心的调用分支。 
  ![avatar](https://jgodeye.oss-cn-beijing.aliyuncs.com/example.jpeg)
### 路线
- 更多参数细化跟踪粒度 
- 增加attach方式 
- 开发intellij插件 
### License
The jgodeye is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).