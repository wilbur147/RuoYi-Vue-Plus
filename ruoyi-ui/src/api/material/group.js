import request from '@/utils/request'

// 查询素材分组列表
export function listGroup(query) {
  return request({
    url: '/material/group/list',
    method: 'get',
    params: query
  })
}

// 查询素材分组详细
export function getGroup(materialGroupId) {
  return request({
    url: '/material/group/' + materialGroupId,
    method: 'get'
  })
}

// 新增素材分组
export function addGroup(data) {
  return request({
    url: '/material/group',
    method: 'post',
    data: data
  })
}

// 修改素材分组
export function updateGroup(data) {
  return request({
    url: '/material/group',
    method: 'put',
    data: data
  })
}

// 删除素材分组
export function delGroup(materialGroupId) {
  return request({
    url: '/material/group/' + materialGroupId,
    method: 'delete'
  })
}

// 导出素材分组
export function exportGroup(query) {
  return request({
    url: '/material/group/export',
    method: 'get',
    params: query
  })
}