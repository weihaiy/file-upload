package com.wei.util.fileupload.utils;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.domain.upload.FastImageFile;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.wei.util.fileupload.commons.FastDFSConfig;
import com.wei.util.fileupload.commons.FileType;
import com.wei.util.fileupload.commons.Result;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class FdfsUtil {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Autowired
    private ThumbImageConfig thumbImageConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${fdfs.web-server-url}")
    private String BASE_URL;

    /**
     * 文件上传, byte 流类型
     *
     * @param bytes     文件字节
     * @param fileSize  文件大小
     * @param extension 文件扩展名
     */
    public Result uploadFile(byte[] bytes, long fileSize, String extension) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        StorePath storePath = fastFileStorageClient.uploadFile(
                byteArrayInputStream,
                fileSize,
                extension,
                null);
        logger.info("url:" + storePath.getFullPath());
        return new Result(true, storePath.getFullPath());
    }

    /**
     * MultipartFile类型的文件上传ַ
     *
     * @param file
     */
    public Result uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        StorePath storePath = null;
        try {
            storePath = fastFileStorageClient.uploadFile(
                    file.getInputStream(),
                    file.getSize(),
                    FileNameUtil.getSuffix(fileName),
                    null);
        } catch (IOException e) {
            logger.error("", e);
            return new Result(false, "文件错误");
        }
        logger.info(fileName + " -> url:" + BASE_URL + storePath.getFullPath());
        return new Result(true, BASE_URL + storePath.getFullPath());
    }

    public Result uploadThumbImg(MultipartFile multipartFile) {
        StorePath storePath = null;
        InputStream fileStream = null;
        try {
            fileStream = multipartFile.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String fileName = multipartFile.getOriginalFilename();
        if (StrUtil.isEmpty(fileName)) {
            logger.error("文件错误：文件名为空！");
            return new Result(false, "文件错误：文件名为空！");
        }
        String fileExt = FilenameUtils.getExtension(fileName);
        if (StrUtil.equalsIgnoreCase(fileExt, FileType.JPEG.toString())
                || StrUtil.equalsIgnoreCase(fileExt, FileType.JPG.toString())
                || StrUtil.equalsIgnoreCase(fileExt, FileType.PNG.toString())) {
            storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(fileStream, multipartFile.getSize(), fileExt, null);
        } else {
            logger.error("上传失败：" + fileExt + " 不是图片");
            return new Result(false, fileExt + " 不是图片");
        }
        String thumbPath = BASE_URL + storePath.getGroup() + "/" + thumbImageConfig.getThumbImagePath(storePath.getPath());
        logger.info(fileName + " -> 图片缩略图url:" + thumbPath);
        return new Result(true, thumbPath);
    }

    /**
     * 普通文件上传
     *
     * @param file
     * @return
     * @throws IOException
     */
    public Result uploadFile(File file) {
        String fileName = file.getName();
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.error("", e);
            return new Result(false, "文件错误");
        }
        StorePath storePath = fastFileStorageClient.uploadFile(
                inputStream,
                file.length(),
                FileNameUtil.getSuffix(file),
                null);
        logger.info(fileName + " -> url:" + BASE_URL + storePath.getFullPath());
        return new Result(true, BASE_URL + storePath.getFullPath());
    }


    public Result createFileAndUpload(String sourceFilePath) throws Exception {
        File file = new File(sourceFilePath);
        return uploadFile(file);
    }

    /**
     * 带输入流形式的文件上传
     *
     * @param inputStream
     * @param size
     * @param fileName
     * @return
     */
    public Result uploadFile(InputStream inputStream, long size, String fileName) {
        StorePath storePath = fastFileStorageClient.uploadFile(inputStream, size, fileName, null);
        logger.info(fileName + " -> url:" + storePath.getFullPath());
        return new Result(true, storePath.getFullPath());
    }

    /**
     * 将一段文本文件写到fastdfs的服务器上
     *
     * @param content
     * @param fileExtension
     * @return
     */
    public Result uploadFile(String content, String fileExtension) {
        byte[] buff = content.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
        StorePath storePath = fastFileStorageClient.uploadFile(stream, buff.length, fileExtension, null);
        logger.info("url:" + BASE_URL + storePath.getFullPath());
        return new Result(true, BASE_URL + storePath.getFullPath());
    }

    /**
     * 下载文件
     *
     * @param fileUrl 文件URL
     * @return 文件字节
     */
    public byte[] downloadFile(String fileUrl) {
        String group = fileUrl.substring(0, fileUrl.indexOf("/"));
        String path = fileUrl.substring(fileUrl.indexOf("/") + 1);
        DownloadByteArray downloadByteArray = new DownloadByteArray();
        logger.info("fdfs下载文件的group:{}", group);
        logger.info("fdfs下载文件的path:{}", path);
        return fastFileStorageClient.downloadFile(group, path, downloadByteArray);
    }

    public Result deleteFile(String fileUrl) {
        if (StrUtil.isBlank(fileUrl)) {
            logger.warn("文件地址为空！");
            return new Result(false, "文件地址为空！");
        }
        try {
            StorePath storePath = StorePath.parseFromUrl(fileUrl);
            fastFileStorageClient.deleteFile(storePath.getGroup(), storePath.getPath());
            logger.info("文件地址为：" + fileUrl + " 的文件删除成功");
            return new Result(true, "文件地址为：" + fileUrl + " 的文件删除成功");
        } catch (Exception e) {
            logger.error("文件删除异常：", e);
            return new Result(false, "文件删除失败");
        }
    }


    public File byteArrayToFile(byte[] byteArray) {
        try {
            // 创建临时文件
            File file = File.createTempFile(UUID.randomUUID().toString(), null);
            // 将字节数组写入临时文件
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(byteArray);
                return file;
            } catch (IOException e) {
                logger.error("字节写入文件失败:", e);
                return null;
            }
        } catch (IOException e) {
            logger.error("创建临时文件失败", e);
            return null;
        }
    }
}