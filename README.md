# fisco-bcos区块链养老保险项目

本项目是一个基于FISCO-BCOS区块链的养老保险项目，使用Solidity 0.4.25编写智能合约，后端采用Spring Boot框架，前端使用Vue框架。

## 项目结构

##### PensionSystem/
##### ├── WebFrontUp/
##### │ ├── views/
##### │ ├── src/
##### │ ├── utils/
##### ├── WebBackUp/
##### │ ├── .idea/
##### │ ├── src/
##### │ ├── pom.xml
##### ├── contract/
##### │ ├── MainContract.sol

perl
Copy code

## 快速开始

### 前端启动

在 `WebFrontUp` 目录下执行以下命令安装依赖：

```shell
npm install
然后启动前端服务：

shell
Copy code
npm run dev
后端启动
使用IDEA打开 WebBackUp 目录，等待IDEA加载完毕后，运行 Application.java 启动后端服务。

技术栈
Solidity 0.4.25
Spring Boot
Vue.js
贡献
欢迎提出问题和建议！如果你有兴趣为项目贡献代码，请提交 Pull Request。

许可证
该项目采用 MIT 许可证。
