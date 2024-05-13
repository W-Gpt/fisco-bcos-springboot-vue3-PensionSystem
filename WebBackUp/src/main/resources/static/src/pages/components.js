// 日期时间格式处理，把日期统一转化成 YYYY-MM-DD hh:mm:ss 的格式
const dateTime = time => {
  let newDate = new Date(time);
  let dateCollection = { y: newDate.getFullYear(), M: newDate.getMonth() + 1, d: newDate.getDate(), h: newDate.getHours(), m: newDate.getMinutes(), s: newDate.getSeconds() };
  for(let i in dateCollection) {
    if (dateCollection[i] < 10) dateCollection[i] = `0${dateCollection[i]}`;
  }
  let { y, M, d, h, m, s } = dateCollection;
  return `${y}-${M}-${d} ${h}:${m}:${s}`;
}

// 头部组件
const Header = {
  // 接受传入的登录状态、用户信息
  props: ['login', 'user'],
  template: `
    <div class="header">
      <img src="./static/images/logo.png" />
      <h3>食品溯源平台</h3>
      <span v-if="login" class="user-name">{{ user }}</span>
    </div>
    `
  }

// 食品详情展示
const FoodDetail = {
  // 接受传入的食品详情信息
  props: ['food'],
  computed: {
    // 日期转换
    date() {
      if (this.food.timestamp) return dateTime(this.food.timestamp)
    }
  },
  template: `
    <div class="fooddetail">
      <h4>食品基本信息</h4>
      <div class="content trace-content">
        <div>
          <span>生产商地址</span>
          <span>{{ food.from_address || food.address }}</span>
        </div>
        <div>
          <span>溯源码</span>
          <span>{{ food.traceNumber }}</span>
        </div>
        <div>
          <span>食品名称</span>
          <span>{{ food.name }}</span>
        </div>
        <div>
          <span>生产商</span>
          <span>{{ food.from || food.produce }}</span>
        </div>
        <div>
          <span>生产时间</span>
          <span>{{ date }}</span>
        </div>
      </div>
    </div>
  `,
}

// 溯源详情展示
const TraceDetail = {
  // 食品详情子组件，FoodDetail 展示的是农场部分的信息
  components: {
    FoodDetail
  },
  // 接受传入的溯源详情信息，及用户信息；foodDetail为数组，第一个数组元素为农场溯源信息、第二个数组元素为中间商溯源信息、第三个数组元素为超市溯源信息
  props: ['foodDetail', 'user'],
  template:`
    <div>
      <h3 class="trace-title">以下是溯源码为{{foodDetail[0].traceNumber}}的溯源信息</h3>
      <div class="tracedetail">
        <!-- 农场部分的溯源详情 -->
        <!-- 农场部分的溯源详情 -->
        <FoodDetail :food="foodDetail[0]" />

        <!-- 根据 foodDetail 的溯源细节，决定是否展示相关阶段的溯源信息， user: 2 为 超市， user: 3 为消费者 -->
        <el-divider v-if="foodDetail[1] && (user === 2 || user === 3)"></el-divider>
        <div v-if="foodDetail[1] && (user === 2 || user === 3)">
          <h4>食品流通信息</h4>
          <div  class="trace-timeline trace-content">
          <el-timeline>
            <el-timeline-item class="el-timeline-no-node el-timeline-no-tail">
              <div>
                <span>中间商地址</span>
                <span>{{ foodDetail[1].to_address }}</span>
              </div>
            </el-timeline-item>
            <el-timeline-item>
              <div>
                <span>入库时间</span>
                <span>{{ dateTime(foodDetail[1].timestamp) }}</span>
              </div>
            </el-timeline-item>
            <el-timeline-item>
              <div>
                <span>质检情况</span>
                <span>{{ foodDetail[1].quality === 0 ? '优质' : foodDetail[1].quality === 1 ? '合格' : '不合格' }}</span>
              </div>
            </el-timeline-item>
            <el-timeline-item>
              <div>
                <span>发货单位</span>
                <span>{{ foodDetail[1].from }}</span>
              </div>
            </el-timeline-item>
            <el-timeline-item  style="height:60px;" class="el-timeline-no-tail">
              <div>
                <span>收货单位</span>
                <span>{{ foodDetail[1].to }}</span>
              </div>
            </el-timeline-item>

            <!-- 根据 foodDetail 的溯源细节，决定是否展示相关阶段的溯源信息， user: 3 为消费者 -->
            <el-timeline-item class="el-timeline-no-node el-timeline-no-tail" v-if="foodDetail[2] && user === 3">
              <div>
                <span>超市地址</span>
                <span>{{ foodDetail[2].to_address }}</span>
              </div>
            </el-timeline-item>
            <el-timeline-item v-if="foodDetail[2] && user === 3">
              <div>
                <span>入库时间</span>
                <span>{{ dateTime(foodDetail[2].timestamp) }}</span>
              </div>
            </el-timeline-item>
            <el-timeline-item v-if="foodDetail[2] && user === 3">
              <div>
                <span>质检情况</span>
                <span>{{ foodDetail[2].quality === 0 ? '优质' : foodDetail[2].quality === 1 ? '合格' : '不合格' }}</span>
              </div>
            </el-timeline-item>
            <el-timeline-item v-if="foodDetail[2] && user === 3">
              <div>
                <span>发货单位</span>
                <span>{{ foodDetail[2].from }}</span>
              </div>
            </el-timeline-item>
            <el-timeline-item v-if="foodDetail[2] && user === 3">
              <div>
                <span>收货单位</span>
                <span>{{ foodDetail[2].to }}</span>
              </div>
            </el-timeline-item>
          </el-timeline>
          </div>
        </div>
      </div>
    </div>
  `,
  methods: {
    // 时间格式转化
    dateTime(val) {
      return dateTime(val)
    }
  }
}

// 添加蔬菜组件
const AddVegetable = {
  // 溯源详情组件注册
  components: {
    TraceDetail
  },
  data() {
    return {
      form: {
        traceName: '', // 收货单位
        quality: '', // 质检情况
      },
      formLabelWidth: '180px', // 弹窗宽度
    }
  },
  // 接受传入的食品详情、可视化、用户信息
  props: ['dialogFormVisible', 'food', 'user'],
  template:`
    <div>
      <el-dialog title="添加食品信息" :visible.sync="dialogFormVisible" :show-close="false" width="40%" :center="true">
        <el-form :model="form" ref="form" label-position="left" :label-width="formLabelWidth">
          <el-form-item 
            label="质检情况"
            prop="quality"
            :rules="[
              { required: true, message: '请选择质检情况', trigger: 'change' }
            ]">
            <el-radio-group v-model="form.quality">
              <el-radio label="0">优质</el-radio>
              <el-radio label="1">合格</el-radio>
              <el-radio label="2">不合格</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item
            label="收货单位(本单位)"
            prop="traceName"
            :rules="[
              { required: true, message: '请输入收货单位' }
            ]">
            <el-input v-model="form.traceName" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div>
          <el-button @click="$emit('popup', false)">取 消</el-button>
          <el-button type="primary" @click="submitForm('form')">确 定</el-button>
        </div>
        <el-divider></el-divider>

        <!-- 食品详情信息 -->
        <TraceDetail :food-detail="food" :user="user" />
      </el-dialog>
    </div>
  `,
  methods: {
    // 处理添加蔬菜提交， $emit 到 父组件处理
    submitForm(formName) {
      // 参数校验
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$emit('confirmPopup', this.form);
        } else {
          return false;
        }
      });
    },
  }
}

// mixin， 这里主要处理中间商和超市的耦合业务
const mixin = {
  // 注册添加蔬菜信息、食品详情子组件
  components: {
    AddVegetable,
    FoodDetail
  },
  // 接受传入的分页信息
  props: ['currPage', 'pageSize'],
  data() {
    return {
      popup: false, // 控制添加蔬菜信息弹窗
      traceNumber: '', // 溯源码
      foodDetail: '', // 溯源食品信息
      foodList: [], // 食品信息列表
      popoverData: {}, // 弹窗食品信息数据
    }
  },
  computed: {
    // 当前页的数据渲染
    renderList() {
      return this.foodList.slice((this.currPage - 1) * this.pageSize, this.currPage * this.pageSize)
    }
  },
  template: `
    <div>
      <el-input
        placeholder="请输入溯源码"
        v-model.number="traceNumber"
        clearable>
      </el-input> 
      <el-button type="primary" @click="addVegetable">添加食品信息</el-button>
      <el-button type="danger" @click="$emit('logout', false)" class="button-logout">退出</el-button>

      <!-- 食品流转信息列表 -->
      <el-table
        :data="renderList"
        style="width: 100%"
        :default-sort = "{prop: 'date', order: 'descending'}"
        >
        <el-table-column
          prop="traceNumber"
          label="溯源码"
          >
          <template slot-scope="scope">

        <!-- 鼠标点击食品细节弹窗信息 -->
        <el-popover trigger="click" placement="top" @show="handlePopover(scope.row)" :width="400">
          <FoodDetail :food="popoverData"  />
          <div slot="reference">
            <span style="color: blue; cursor: pointer;">{{ scope.row.traceNumber }}</span>
          </div>
        </el-popover>
      </template>
        </el-table-column>
        <el-table-column
          prop="name"
          label="食品名称"
          >
        </el-table-column>
        <el-table-column
        prop="date"
        label="上架时间"
        sortable
        >
        </el-table-column>
        <el-table-column
          prop="quality"
          label="质检情况"
        >
        </el-table-column>
        <el-table-column
          prop="from"
          label="发货单位"
        >
        </el-table-column>
        <el-table-column
          prop="to"
          label="收获单位"
        >
        </el-table-column>
      </el-table>

      <!-- 添加蔬菜信息组件 -->
      <AddVegetable :dialogFormVisible="popup" :user="user" :food="foodDetail"  @popup="handlePopup" @confirmPopup="formSubmit" />

    </div>
  `,
  mounted() {
    // 获取数据
    this.getData();
  },
  methods: {
    // 数据获取，根据 user 用户类型来分别获取中间商和超市的数据
    getData() {
      let pathname = this.user === 1 ? 'distributing' : 'retailing';
      axios({
        method: 'get',
        url: `/${pathname}`
      })
      .then(ret => {
        this.foodList = ret.data
          .map(item => {
            let quality = item.quality === 0 ? '优质' : item.quality === 1 ? '合格' : '不合格'
            return ({
              ...item,
              date: dateTime(item.timestamp),
              quality,
            })
          })
          .reverse();
        this.$emit('total', this.foodList.length);
      })
      .catch(err => {
        console.log(err)
      })
    },
    // 表格中食品详情弹窗
    handlePopover(data) {
      // 中间商已有的信息足有支持显示，不需要进行额外 http 请求
      if (this.user === 1) {
        this.popoverData = data;
        return;
      }
      // 超市则需要额外发送 http　请求获取更多细节
      axios({
        method: 'get',
        url: `/food?traceNumber=${data.traceNumber}`
      })
      .then(ret => {
        this.popoverData = ret.data;
      })
      .catch(err => {
        console.log(err)
      })
    },
    // 处理添加蔬菜信息
    addVegetable() {
      if (!this.traceNumber) {
        this.$message.error('请输入溯源码');
        return;
      }
      axios({
        method: 'get',
        url: `/trace?traceNumber=${this.traceNumber}`
      })
      .then(ret => {
        this.foodDetail = ret.data || []
        this.popup = true;
      })
      .catch(err => {
        console.log(err)
      })
    },
    // 添加蔬菜信息弹窗
    handlePopup(val) {
      this.popup = val;
    },
    // 添加蔬菜信息提交，根据 user 进行不同的请求
    formSubmit(val) {
      let pathname = this.user === 1 ? 'adddistribution' : 'addretail';
      axios({
        method: 'post',
        url: `/${pathname}`, 
        data: {
          ...val,
          quality: parseInt(val.quality),
          traceNumber: this.foodDetail[0].traceNumber
        }
      })
      .then(ret => {
        if (ret.data.ret !== 1) {
          this.$message({
            message: '提交失败',
            type: 'error',
            center: true
          });
          return
        }
        this.$message({
          message: '提交成功',
          type: 'success',
          center: true
        });
        this.getData();
      })
      .catch(err => {
        console.log(err)
      })
      this.popup = false;
    }
  }
}

// 创建蔬菜组件
const CreateVegetable = {
  data() {
    return {
      form: {
        foodName: '', // 食品名称
        traceName: '', // 生产商名称
        traceNumber: '', // 溯源码
        quality: '', // 质检情况
      },
      formLabelWidth: '120px'
    }
  },
  // 接受传入的可视化参数
  props: ['dialogFormVisible'],
  template:`
    <div>
      <el-dialog title="新建食品" :visible.sync="dialogFormVisible" :show-close="false" width="35%" :center="true">
        <el-form :model="form" ref="form" label-position="left" :label-width="formLabelWidth">
          <el-form-item 
            label="溯源码" 
            prop="traceNumber" 
            :rules="[
              { required: true, message: '请输入溯源码' }
            ]" 
          >
            <el-input v-model.number="form.traceNumber" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item 
            label="食品名称" 
            prop="foodName" 
            :rules="[
              { required: true, message: '请输入食品名称' }
            ]" 
          >
            <el-input v-model="form.foodName" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item 
            label="生产商" 
            prop="traceName" 
            :rules="[
              { required: true, message: '请输入生产商' }
            ]" 
          >
            <el-input v-model="form.traceName" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item 
            label="质检情况"
            prop="quality"
            :rules="[
              { required: true, message: '请选择质检情况', trigger: 'change' }
            ]" 
          >
            <el-radio-group v-model="form.quality">
              <el-radio label="0">优质</el-radio>
              <el-radio label="1">合格</el-radio>
              <el-radio label="2">不合格</el-radio>
            </el-radio-group>
          </el-form-item>
          
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="$emit('popup', false)">取 消</el-button>
          <el-button type="primary" @click="submitForm('form')">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  `,
  methods: {
    // 提交信息
    submitForm(formName) {
      // 参数校验
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$emit('confirmPopup', this.form)
        } else {
          return false;
        }
      });
    },
  }
}



