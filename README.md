# 2048 by ura

![Static Badge](https://img.shields.io/badge/CS109-ongoing-green) ![JAVA](https://img.shields.io/badge/JAVA-red) ![javaFx](https://img.shields.io/badge/JAVAFX-yellow)

这是 SUSTech CS109 课程大作业的项目仓库，ura 是一个由两个人组成的小队。本游戏使用 maven 作为项目管理器构建，使用 javafx 作为图形库，并使用 fxgl 作为游戏引擎。

本项目 GUI 由 FXML 实现。使用序列化技术保存游戏状态和用户信息。使用 WebSocket 通信技术实现多人游戏。

## 项目结构

本项目使用 MVC结构。项目结构如下：

- `src/main/java/ura/`：项目源代码
  - `controller/`：控制器
  - `model/`：模型
  - `view/`：视图
  - `GameLauncher.java`：程序入口

## 人员分工