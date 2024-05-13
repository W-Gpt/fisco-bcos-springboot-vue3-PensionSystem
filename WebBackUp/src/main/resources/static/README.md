## 前端部分


### 前端代码结构
```bash
|-- app
  |-- static
    |-- style # 样式
    |-- images  # favicon
    |-- lib # 前端库
    |-- src 
      |-- pages
        |-- main.js # 四个角色组件
        |-- login.js  # 四个角色的登录组件
        |-- components.js # 通用组件
      index.js   # 主 js 文件
    index.html  # 有 农场、中间商、超市、消费者 四个角色
```

> 启动服务时，需要有 农场、中间商、超市、消费者 四个角色，服务相应 / 请求返回 index.html;