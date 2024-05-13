# FISCO-BCOS区块链养老保险项目

本项目是一个基于FISCO-BCOS区块链的养老保险项目，使用Solidity 0.4.25编写智能合约，后端采用Spring Boot框架，前端使用Vue框架。

## 项目结构

PensionSystem/
├── WebBackUp/ # 后端代码目录
│ ├── src/
│ │ └── ...
│ └── pom.xml # Maven依赖配置文件
├── WebFrontUp/ # 前端代码目录
│ ├── WebFrontUp/ # 前端启动目录
│ │ ├── src/
│ │ │ └── ...
│ │ ├── package.json # 前端依赖配置文件
│ │ └── ...
└── contracts/ # 智能合约目录
├── PensionInsurance.sol # 养老保险智能合约文件
└── ...
