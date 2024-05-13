const vue = new Vue({
  // 注册 Header、Login 子组件
  components: {
    Header,
    Login,
  },
  // 实例绑定的元素
  el: '#app',
  data: {
    currPage: 1, // 当前页
    pageSize: 10, // 每页显示数据
    total: 0, // 总数据
    showComponent: Farm, // 当前展示的动态组件
    login: false, // 登录状态
    user: '', // 当前用户
  },
  template: `
    <el-container>

      <!-- 头部导航组件 -->
      <el-header class="header"><Header :user="user" :login="login" @logout="(val) => {this.login = val;}" /></el-header>

      <!-- 主要内容组件 -->
      <el-container>
        <el-main>

          <!-- 登录组件 -->
          <Login v-if="!login" @login="(val) => {this.login = true; this.showComponent = val.component; this.user = val.user;}" />


          <!-- 动态组件，根据 showComponent 动态渲染当前用户 -->
          <component v-else :is="showComponent"
            @logout="(val) => {this.login = val;}" 
            @total="(val) => {this.total = val;}"
            :curr-page.sync="currPage" :page-size.sync="pageSize">
          </component>

          <!-- 分页组件 -->
          <el-pagination 
            style="text-align: center; margin-top: 10px;"
            v-if="login && showComponent.name !== 'Consumer'"
            background
            :hide-on-single-page="false"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page.sync="currPage"
            :page-sizes="[10, 20, 50]"
            :page-size.sync="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
          </el-pagination>
        </el-main>
      </el-container>
    </el-container>
    `,
  methods: {
    // 处理每页数据渲染数
    handleSizeChange(val) {
      this.pageSize = val;
    },
    // 处理翻页
    handleCurrentChange(val) {
      this.currPage = val;
    },
  }
})