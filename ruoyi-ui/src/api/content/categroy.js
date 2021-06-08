import request from '@/utils/request'

// 查询分类列表
export function listCategroy(query) {
  return request({
    url: '/content/categroy/list',
    method: 'get',
    params: query
  })
}

// 查询分类详细
export function getCategroy(conCategroyId) {
  return request({
    url: '/content/categroy/' + conCategroyId,
    method: 'get'
  })
}

// 新增分类
export function addCategroy(data) {
  return request({
    url: '/content/categroy',
    method: 'post',
    data: data
  })
}

// 修改分类
export function updateCategroy(data) {
  return request({
    url: '/content/categroy',
    method: 'put',
    data: data
  })
}

// 删除分类
export function delCategroy(conCategroyId) {
  return request({
    url: '/content/categroy/' + conCategroyId,
    method: 'delete'
  })
}

// 导出分类
export function exportCategroy(query) {
  return request({
    url: '/content/categroy/export',
    method: 'get',
    params: query
  })
}
