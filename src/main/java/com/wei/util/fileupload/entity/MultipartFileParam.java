package com.wei.util.fileupload.entity;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author weihaiyang
 * @ClassDescription 类的描述
 * @since 2024/8/19 15:06 星期一
 */
public class MultipartFileParam {
    private Long chunkNumber;
    private Long totalChunks;
    private Long chunkSize;
    private String identifier;
    private MultipartFile file;

    public Long getChunkNumber() {
        return chunkNumber;
    }

    public void setChunkNumber(Long chunkNumber) {
        this.chunkNumber = chunkNumber;
    }

    public Long getTotalChunks() {
        return totalChunks;
    }

    public void setTotalChunks(Long totalChunks) {
        this.totalChunks = totalChunks;
    }

    public Long getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(Long chunkSize) {
        this.chunkSize = chunkSize;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
