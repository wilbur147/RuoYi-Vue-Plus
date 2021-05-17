import request from '@/utils/request'

// 上传文件
export const uploadFile = (data) => {
  return new Promise((resolve,reject)=>{
    const res = request({
      url: '/fileUploader/upload',
      method: 'post',
      data: data
    })
    resolve(res)
  })
}

// 删除文件
export function delFile(uniqueIds) {
  return request({
    url: '/fileUploader/' + uniqueIds,
    method: 'delete'
  })
}
