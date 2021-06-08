import request from '@/utils/request'

// 查询专题列表
export function listTopic(query) {
  return request({
    url: '/content/topic/list',
    method: 'get',
    params: query
  })
}

// 查询专题详细
export function getTopic(conTopicId) {
  return request({
    url: '/content/topic/' + conTopicId,
    method: 'get'
  })
}

// 新增专题
export function addTopic(data) {
  return request({
    url: '/content/topic',
    method: 'post',
    data: data
  })
}

// 修改专题
export function updateTopic(data) {
  return request({
    url: '/content/topic',
    method: 'put',
    data: data
  })
}

// 删除专题
export function delTopic(conTopicId) {
  return request({
    url: '/content/topic/' + conTopicId,
    method: 'delete'
  })
}

// 导出专题
export function exportTopic(query) {
  return request({
    url: '/content/topic/export',
    method: 'get',
    params: query
  })
}
