import request from '@/utils/request'

// 查询素材资源列表
export function listResources(query) {
  return request({
    url: '/material/resources/list',
    method: 'get',
    params: query
  })
}

// 查询素材资源详细
export function getResources(materialResourcesId) {
  return request({
    url: '/material/resources/' + materialResourcesId,
    method: 'get'
  })
}

// 新增素材资源
export function addResources(data) {
  return request({
    url: '/material/resources',
    method: 'post',
    data: data
  })
}

// 修改素材资源
export function updateResources(data) {
  return request({
    url: '/material/resources',
    method: 'put',
    data: data
  })
}

// 删除素材资源
export function delResources(materialResourcesId) {
  return request({
    url: '/material/resources/' + materialResourcesId,
    method: 'delete'
  })
}

// 下载素材资源
export function downloadResources(materialResourcesIds) {
  return request({
    url: '/material/resources/download/' + materialResourcesIds,
    method: 'get'
  })
}
