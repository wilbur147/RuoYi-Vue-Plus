package com.ruoyi.file.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.ruoyi.common.exception.file.FileException;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


/**
 * @author Ruoyi
 * @Description
 * @create 2021-03-18 15:00
 **/
public class FileUploadUtil
{

    /**
     * 编码文件名
     */
    public static final String createNewFileName(String fileName)
    {
        String suffix = FileUtil.getSuffix(fileName);
        String fileType = FileUploadUtil.getFileType(suffix);
        fileName = fileType + "/" + DateUtils.datePath() + "/" + IdUtil.fastUUID() + "." + suffix;
        return fileName;
    }

    public static final String createNewFileName(MultipartFile file)
    {
        String fileName = file.getOriginalFilename();
        String suffix = FileUtil.getSuffix(fileName);
        String fileType = FileUploadUtil.getFileType(suffix);
        fileName = fileType + "/" + DateUtils.datePath() + "/" + IdUtil.fastUUID() + "." + suffix;
        return fileName;
    }

    /**
     * 检查文件路径是否存在，不存在就创建一个
     * @param rootFilePath
     */
    public static File checkFilePath(String rootFilePath) {
        if (StringUtils.isEmpty(rootFilePath)) {
            return null;
        }
        File parentDir = new File(rootFilePath);
        if (!parentDir.getParentFile().exists()) {
            parentDir.getParentFile().mkdirs();
        }
        return parentDir;
    }

    /**
     * 复制InputStream
     *
     * @param is InputStream
     * @return
     */
    public static InputStream clone(InputStream is) {
        if(null == is){
            throw new FileException("无法获取文件流，文件不可用！", null);
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return new ByteArrayInputStream(baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("无法复制当前文件流！", e);
        }
    }

    public static String getFileType(String suffix){
        String fileTypeName = "";
        String [] imgArr = { "bmp", "gif", "jpg", "jpeg", "png" };
        String [] videoArr = { "flv", "mp4", "wmv", "avi", "mpg", "rm", "rmvb" };
        String [] audioArr = { "wav", "mp3", "ogg" };
        for (String img : imgArr) if (suffix.indexOf(img) != -1) { fileTypeName = "IMAGE";return fileTypeName; }
        for (String video : videoArr) if (suffix.indexOf(video) != -1) {fileTypeName = "VIDEO"; return fileTypeName;}
        for (String audio : audioArr) if (suffix.indexOf(audio) != -1) {fileTypeName = "AUDIO"; return fileTypeName;}
        return fileTypeName;
    }

    /**
     * 下载文件名重新编码
     *
     * @param response 响应对象
     * @param realFileName 真实文件名
     * @return
     */
    public static void setAttachmentResponseHeader(HttpServletResponse response, String realFileName) throws UnsupportedEncodingException
    {
        String percentEncodedFileName = percentEncode(realFileName);

        StringBuilder contentDispositionValue = new StringBuilder();
        contentDispositionValue.append("attachment; filename=")
                .append(percentEncodedFileName)
                .append(";")
                .append("filename*=")
                .append("utf-8''")
                .append(percentEncodedFileName);

        response.setHeader("Content-disposition", contentDispositionValue.toString());
    }

    /**
     * 百分号编码工具方法
     *
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    public static String percentEncode(String s) throws UnsupportedEncodingException
    {
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        return encode.replaceAll("\\+", "%20");
    }

    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os 输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException
    {
        FileInputStream fis = null;
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0)
            {
                os.write(b, 0, length);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

}
