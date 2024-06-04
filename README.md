# 2048 by ura

![Static Badge](https://img.shields.io/badge/CS109-ongoing-green) ![JAVA](https://img.shields.io/badge/JAVA-red) ![javaFx](https://img.shields.io/badge/JAVAFX-yellow)

这是 SUSTech CS109 课程大作业的项目仓库，ura 是一个由两个人组成的小队。本游戏使用 maven 作为项目管理器构建，使用 javafx 作为图形库。

本项目 GUI 由 FXML 实现。使用序列化技术保存游戏状态和用户信息。使用 WebSocket 通信技术实现多人游戏。

## 项目结构

本项目使用 MVC 结构。项目结构如下：

```angular2html
src
├── main
│   ├── java
│   │   └── devproject
│   │       ├── controller
│   │       ├── model
│   │       ├── server
│   │       ├── view
│   │       └── GameLauncher.java 程序入口
│   └── resources
│       ├── css
│       ├── fxml
│       ├── images
│       └── sounds
└── test
└── java
└── devproject
└── model
```

## 功能

### 窗口

创建 2 个窗口（stage）。

#### 主窗口
游戏界面，登录等等。主窗口需要保留系统样式，可以调整大小。

#### 工具窗口
音乐播放和暂停，社交功能等等。工具窗口去除系统样式，自动设置大小，保留移动窗口的控制器。

### 服务器

#### 加密

#### 排行榜

### 账号

#### 加密
账号密码使用 BASE_64 加密。

### 游戏模式

首先，有三种最初始的模式，分别是经典模式，障碍模式和时间模式。三种模式分别计分，每种模式计：score 和 maxNumber。

然后，考虑添加 6x6 模式，也有 3-3-3 合成的模式。分别计分。

### 游戏相关

#### 在线游戏
自动存档功能：每移动一次存档一次。

撤销功能：可以回溯至少5步。

#### 离线游戏
手动存档功能。

### 版本管理
使用 git 进行版本管理。

## 人员分工

- Grada：框架搭建
- FragileHao: 游戏逻辑

## 系统要求

**Windows PC**
- Windows11/10/8/7/Vista
- Core2Duo 2.4Ghz
- 8 GB RAM
- 100MB available space

**Mac**
- macOS 10.12
- Apple silicon M1
- 8 GB RAM
