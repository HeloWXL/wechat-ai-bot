import Vue from 'vue'
import App from './App.vue'

Vue.config.productionTip = false

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css'

Vue.use(ElementUI);

// 第二步: 创建router文件夹 引入实例
import router from './router'

// websocket 地址
Vue.prototype.$wsServerUrl =  'ws://localhost:8081/chat'

new Vue({
  render: h => h(App),
  router,
}).$mount('#app')
