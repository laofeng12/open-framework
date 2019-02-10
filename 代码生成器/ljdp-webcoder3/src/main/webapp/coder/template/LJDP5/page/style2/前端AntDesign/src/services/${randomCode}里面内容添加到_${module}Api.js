<#assign d = "$"/>
import { stringify } from 'qs';
import request from '@/utils/request';

export async function search${table.modelName}(params) {
  return request(`/api/${sys}/${module}/${table.modelName2}/search?${d}{stringify(params)}`);
}
export async function get${table.modelName}(params) {
  return request(`/api/${sys}/${module}/${table.modelName2}/${d}{params}`);
}
export async function save${table.modelName}(params) {
  return request(`/api/${sys}/${module}/${table.modelName2}`, {
    method: 'POST',
    'Content-Type': 'application/x-www-form-urlencoded;',
    body: params,
  });
}
export async function remove${table.modelName}(params) {
  return request(`/api/${sys}/${module}/${table.modelName2}/remove`, {
    method: 'POST',
    'Content-Type': 'application/x-www-form-urlencoded;',
    body: params,
  });
}