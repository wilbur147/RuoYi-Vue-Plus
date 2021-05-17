import request from '@/utils/request'

// 查询博客标签列表
export function listTag(query) {
  return request({
    url: '/blog/tag/list',
    method: 'get',
    params: query
  })
}

// 查询博客标签详细
export function getTag(blogTagId) {
  return request({
    url: '/blog/tag/' + blogTagId,
    method: 'get'
  })
}

// 新增博客标签
export function addTag(data) {
  return request({
    url: '/blog/tag',
    method: 'post',
    data: data
  })
}

// 修改博客标签
export function updateTag(data) {
  return request({
    url: '/blog/tag',
    method: 'put',
    data: data
  })
}


// 置顶博客标签
export function clickTop(data) {
  return request({
    url: '/blog/tag/clickTop',
    method: 'put',
    data: data
  })
}

// 删除博客标签
export function delTag(blogTagId) {
  return request({
    url: '/blog/tag/' + blogTagId,
    method: 'delete'
  })
}

// 导出博客标签
export function exportTag(query) {
  return request({
    url: '/blog/tag/export',
    method: 'get',
    params: query
  })
}