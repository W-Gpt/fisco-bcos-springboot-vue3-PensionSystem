// 农场组件
const Farm = {
  // 接受分页属性，用来控制翻页
  props: ['currPage', 'pageSize'],
  data() {
    return {
      popup: false, // 创建蔬菜弹窗
      foodList: [], // 食品信息
  }
  },
  computed: {
    // 当前页渲染数据
    renderList() {
      return this.foodList.slice((this.currPage - 1) * this.pageSize, this.currPage * this.pageSize)
    }
  },
  // 注册创建蔬菜子组件
  components: {
    CreateVegetable
  },
  template: `
    <div>
      <!-- 创建蔬菜按钮 -->
      <el-button type="primary" @click="popup = true;">新建食品</el-button>

      <!-- 退出按钮 -->
      <el-button type="danger" @click="$emit('logout', false)" class="button-logout">退出</el-button>

      <!-- 食品信息列表 -->
      <el-table
        :data="renderList"
        style="width: 100%"
        :default-sort = "{prop: 'date', order: 'descending'}"
      >
        <el-table-column
          prop="traceNumber"
          label="溯源码"
        >
        </el-table-column>
        <el-table-column
          prop="foodName"
          label="食品名称"
        >
        </el-table-column>
        <el-table-column
          prop="traceName"
          label="生产商"
        >
        </el-table-column>
        <el-table-column
          prop="date"
          label="生产时间"
          sortable
        >
        </el-table-column>
      </el-table>

      <!-- 创建蔬菜子组件 -->
      <CreateVegetable :dialogFormVisible="popup" @popup="handlePopup" @confirmPopup="formSubmit" />
    </div>
  `,
  mounted() {
    // 挂载时获取农场数据
    this.getData();
  },
  methods: {
    // 处理数据请求
    getData() {
      axios({
        method: 'get',
        url: `/producing`
      })
      .then(ret => {
        // 对数据进行处理
        this.foodList = ret.data
          .map(item => ({
            ...item,
            date: dateTime(item.timestamp),
            traceNumber: item.traceNumber,
            traceName: item.from,
            foodName: item.name
          }))
          .reverse();     
        this.$emit('total', this.foodList.length);
      })
      .catch(err => {
        console.log(err)
      })
    },
    // 添加蔬菜弹窗
    handlePopup(val) {
      this.popup = val;
    },
    // 添加蔬菜提交
    formSubmit(val) {
      axios({
        method: 'post',
        url: '/produce', 
        data: {
          ...val,
          quality: parseInt(val.quality),
          user: 0
        }
      })
      .then(ret => {
        if (ret.data.ret !== 1) {
          // 已有溯源码校验
          if (ret.data.ret === 0 && ret.data.msg === 'traceNumber already exist') {
            this.$message({
              message: '该溯源码已存在，请重新创建',
              type: 'error',
              center: true
            });
          } else {
            this.$message({
              message: '提交失败',
              type: 'error',
              center: true
            });
          }
          return
        }
        this.$message({
          message: '提交成功',
          type: 'success',
          center: true
        });
        // 提交成功后重新获取数据
        this.getData();
      })
      .catch(err => {
        console.log(err)
      })
      this.popup = false;
    }
  }
}

// 中间商组件
const Agent = {
  // 和超市的职能差不多，放在 mixin 里面
  mixins: [mixin],
  data() {
    return {
      user: 1, // user: 1 为中间商
    }
  }
}

// 超市组件
const Mall = {
  // 和中间商的职能差不多，放在 mixin 里面
  mixins: [mixin],
  data() {
    return {
      user: 2, // user: 2 为超市
    }
  }
}

// 消费者组件
const Consumer = {
  // 溯源详情子组件注册
  components: {
    TraceDetail
  },
  data() {
    return {
      form: {
        traceNumber: '', // 输入的溯源码
      },
      foodDetail: '', // 溯源详细信息
      onSearch: false, // 搜索情况
    }
  },
  template:`
    <el-row style="height: 100%;" class="consumer">
      <!-- 左边为溯源查询 -->
      <el-col :span="7" style="height: 100%;">
        <div class="grid-content bg-purple">
          <el-form :model="form" ref="form">
            <h4>溯源码搜索</h4>
            <el-divider></el-divider>
            <el-form-item 
              label="请输入您想要查询的溯源码" 
              prop="traceNumber" 
            >
              <el-input v-model.number="form.traceNumber" type="textarea" :rows="3" autocomplete="off" @clear="onSearch = false;"></el-input>
            </el-form-item>
            <el-button type="primary" @click="onSubmit">查询</el-button>
          </el-form>
        </div>
      </el-col>

      <!-- 右边为溯源查询结果 -->
      <el-col :span="16">
        <div class="grid-content bg-purple-light">
          <div v-if ="!onSearch" class="consumer-tip">请在左侧查询栏中输入溯源码进行查询</div>  
          <div v-if ="onSearch && foodDetail.length === 0" class="consumer-tip">该溯源码无对应信息，请确认后重新查询</div>  

          <!-- 溯源查询成功后才展示 -->
          <div v-if ="onSearch && foodDetail.length">
            <!-- 溯源详情组件， user: 3 为消费者 -->
            <TraceDetail :food-detail="foodDetail" :user="3" />
          </div>  
        </div>
      </el-col>
      <el-col :span="1">
        <!--退出按钮 -->
        <el-button type="danger" @click="$emit('logout', false)" class="button-logout">退出</el-button>
      </el-col>
    </el-row>
  `,
  methods: {
    // 处理按溯源码查询
    onSubmit() {
      if (!this.form.traceNumber) {
        this.$message.error('请输入溯源码');
        return;
      }
      this.onSearch = true;
      axios({
        method: 'get',
        url: `/trace?traceNumber=${this.form.traceNumber}`
      })
      .then(ret => {
        this.foodDetail = ret.data || []
      })
      .catch(err => {
        console.log(err);
      })
    }
  }
}
