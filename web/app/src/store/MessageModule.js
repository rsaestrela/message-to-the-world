import Vue from 'vue';
import { getMessage } from '../service/getMessage';

const defaultState = {
  message: [],
};

const moduleState = Object.assign({}, defaultState);

const mutations = {
  setMessage(state, message) {
  },
};

const actions = {
  async getMessage({ commit, state }) {
    try {
      const response = await getMessage();
      commit('setMessage', response);
    } catch (ex) {
      //Vue.$log.warn(ex);
    }
  },
};

const getters = {
    message: ({ message }) => message,
};

export default {
  namespaced: true,
  state: moduleState,
  mutations,
  actions,
  getters,
};
