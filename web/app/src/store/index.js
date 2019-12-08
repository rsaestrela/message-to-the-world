import Vue from 'vue';
import Vuex from 'vuex';
import MessageModule from './MessageModule'

Vue.use(Vuex);

const store = new Vuex.Store({
  modules: {
    MessageModule,
  }
})

export default store;