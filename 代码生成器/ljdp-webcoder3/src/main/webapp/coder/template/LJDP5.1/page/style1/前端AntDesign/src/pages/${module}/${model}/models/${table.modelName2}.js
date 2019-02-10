<#assign d = "$"/>
import { Modal } from 'antd';
import { 
	search${table.modelName},
	save${table.modelName},
	remove${table.modelName},
	get${table.modelName}, 
	<#if baseFun.importFun == "on">
	batch${table.modelName}Process,
	</#if>
} from '@/services/${module}Api';

// ${resName}
export default {
  namespace: '${table.modelName2}',

  state: {
    data: {
      list: [],
      pagination: {
        pageSize: 10,
      },
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
      const response = yield call(batch${table.modelName}Process, payload);
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
          current: resp.number+1,
          pageSize: resp.size,
        },
      };
      return {
        ...state,
        data: result,
      };
    },
    updateDomain(state, action) {
      const resp = action.payload;
      const {requestId, code, message, data} = resp;
      if(code !== 200){
    	  Modal.error({
			    title: `提交失败:${d}{requestId}`,
			    content: `[${d}{code}] ${d}{message}`,
			  });
    	  return state;
      }
      return {
        ...state,
        domain: data,
      };
    },
  },
};
