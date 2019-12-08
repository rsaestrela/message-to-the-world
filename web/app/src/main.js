import Vue from 'vue'
import App from './App.vue'
import Meta from 'vue-meta';
import router from './router'
import store from './store'

Vue.config.productionTip = false

Vue.use(Meta);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
