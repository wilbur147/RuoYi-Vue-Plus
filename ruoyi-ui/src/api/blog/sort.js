import request from '@/utils/request'

// 查询博客分类列表
export function listSort(query) {
  return request({
    url: '/blog/sort/list',
    method: 'get',
    params: query
  })
}

// 查询博客分类详细
export function getSort(blogSortId) {
  return request({
    url: '/blog/sort/' + blogSortId,
    method: 'get'
  })
}

// 新增博客分类
export function addSort(data) {
  return request({
    url: '/blog/sort',
    method: 'post',
    data: data
  })
}

// 修改博客分类
export function updateSort(data) {
  return request({
    url: '/blog/sort',
    method: 'put',
    data: data
  })
}

// 置顶博客分类
export function clickTop(data) {
  return request({
    url: '/blog/sort/clickTop',
    method: 'put',
    data: data
  })
}

// 删除博客分类
export function delSort(blogSortId) {
  return request({
    url: '/blog/sort/' + blogSortId,
    method: 'delete'
  })
}

// 导出博客分类
export function exportSort(query) {
  return request({
    url: '/blog/sort/export',
    method: 'get',
    params: query
  })
}