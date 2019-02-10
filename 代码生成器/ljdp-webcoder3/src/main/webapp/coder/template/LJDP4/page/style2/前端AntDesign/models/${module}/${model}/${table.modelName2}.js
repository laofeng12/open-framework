import { 
	search${table.modelName},
	save${table.modelName},
	remove${table.modelName},
	get${table.modelName}, 
} from '../../services/${module}Api';
<#if baseFun.importFun == "on">
import { ljdpFileBatchProcess } from '../../services/ljdpApi';
</#if>

export default {
  namespace: '${table.modelName2}',

  state: {
    data: {
      list: [],
      pagination: {},
    },
    domain: {},
  },

  effects: {
    *fetch({ payload }, { call, put }) {
      const response = yield call(search${table.modelName}, payload);
      yield put({
        type: 'updateList',
        payload: response,
      });
    },
    *loadDomain({ payload, callback }, { call, put }) {
      const response = yield call(get${table.modelName}, payload);
      yield put({
        type: 'updateDomain',
        payload: response,
      });
      if (callback) callback(response);
    },
    *clearDomain({ payload }, { put }) {
      yield put({
        type: 'updateDomain',
        payload: { ...payload, code: 200 },
      });
    },
    *save({ payload, callback }, { call }) {
      const response = yield call(save${table.modelName}, payload);
      if (callback) callback(response);
    },
    <#if baseFun.delete == "on">
    *remove({ payload, callback }, { call }) {
      const response = yield call(remove${table.modelName}, payload);
      if (callback) callback(response);
    },
    </#if>
    <#if baseFun.importFun == "on">
    *submitBatchTask({ payload, callback }, { call }) {
      const response = yield call(ljdpFileBatchProcess, payload);
      if (callback) callback(response);
    },
    </#if>
  },

  reducers: {
    updateList(state, action) {
      const resp = action.payload;
      const result = {
        list: resp.rows,
        pagination: {
          total: resp.total,
        },
      };
      return {
        ...state,
        data: result,
      };
    },
    updateDomain(state, action) {
      const resp = action.payload;
      return {
        ...state,
        domain: resp,
      };
    },
  },
};
