import request from '@/utils/request'

// 查询内容列表
export function listContent(query) {
  return request({
    url: '/content/content/list',
    method: 'get',
    params: query
  })
}

// 查询内容详细
export function getContent(contentId) {
  return request({
    url: '/content/content/' + contentId,
    method: 'get'
  })
}

// 新增内容
export function addContent(data) {
  return request({
    url: '/content/content',
    method: 'post',
    data: data
  })
}

// 修改内容
export function updateContent(data) {
  return request({
    url: '/content/content',
    method: 'put',
    data: data
  })
}

// 修改状态
export function editContentStatus(data) {
  return request({
    url: '/content/content/editStatus',
    method: 'put',
    data: data
  })
}

// 删除内容
export function delContent(contentId) {
  return request({
    url: '/content/content/' + contentId,
    method: 'delete'
  })
}

// 导出内容
export function exportContent(query) {
  return request({
    url: '/content/content/export',
    method: 'get',
    params: query
  })
}
