import request from '@/utils/request'

// 查询文章列表
export function listBlog(query) {
  return request({
    url: '/blog/blog/list',
    method: 'get',
    params: query
  })
}

// 查询文章详细
export function getBlog(blogId) {
  return request({
    url: '/blog/blog/' + blogId,
    method: 'get'
  })
}

// 新增文章
export function addBlog(data) {
  return request({
    url: '/blog/blog',
    method: 'post',
    data: data
  })
}

// 修改文章
export function updateBlog(data) {
  return request({
    url: '/blog/blog',
    method: 'put',
    data: data
  })
}

// 删除文章
export function delBlog(blogId) {
  return request({
    url: '/blog/blog/' + blogId,
    method: 'delete'
  })
}

// 导出文章
export function exportBlog(query) {
  return request({
    url: '/blog/blog/export',
    method: 'get',
    params: query
  })
}