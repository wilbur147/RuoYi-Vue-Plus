import request from '@/utils/request'
// 查询系统配置详细
export function getSystemConfig(query) {
  return request({
    url: '/system/config/getAll',
    method: 'get',
    params: query
  })
}
// 修改系统配置
export function editSystemConfig(data) {
  return request({
    url: '/system/config/editValueByKey',
    method: 'put',
    data: data
  })
}
