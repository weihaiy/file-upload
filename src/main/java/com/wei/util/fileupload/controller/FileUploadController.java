package com.wei.util.fileupload.controller;

import com.wei.util.fileupload.commons.Result;
import com.wei.util.fileupload.commons.editor.Success;
import com.wei.util.fileupload.commons.editor.SuccessMsg;
import com.wei.util.fileupload.utils.FdfsUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("/file/upload")
@CrossOrigin
@Tag(name = "文件上传接口")
public class FileUploadController {
    @Autowired
    private FdfsUtil fdfsUtil;

    @PostMapping(value = "/thumb", consumes = "multipart/form-data")
    @Operation(summary = "上传图片缩略图")
    public Result uploadImg(@RequestParam("file") MultipartFile file) {
        return fdfsUtil.uploadThumbImg(file);
    }
    @PostMapping(value = "/file", consumes = "multipart/form-data")
    @Operation(summary = "文件上传")
    public Result uploadFile(@RequestParam("file") MultipartFile file){
        return fdfsUtil.uploadFile(file);
    }

    @PostMapping("/txt")
    @Operation(summary = "上传一段文本，并指定后缀名")
    public Result uploadTxt(String text,String ext){
        return fdfsUtil.uploadFile(text,ext);
    }
    @GetMapping("/download")
    @Operation(summary = "根据指定的url下载文件")
    public ResponseEntity<InputStreamResource> download(String url) throws FileNotFoundException {
        byte[] fileBytes = fdfsUtil.downloadFile(url);
        File file = fdfsUtil.byteArrayToFile(fileBytes);
        InputStreamResource fileResource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length())
                .body(fileResource);
    }
    @DeleteMapping("/del")
    @Operation(summary = "根据指定的url地址删除文件")
    public Result delete(String url){
        return fdfsUtil.deleteFile(url);
    }

    @PostMapping(value = "/editor/file", consumes = "multipart/form-data")
    @Operation(summary = "富文本编辑器文件上传")
    public Success editorUploadFile(@RequestParam("file") MultipartFile file){
        Result result = fdfsUtil.uploadFile(file);
        Success success = new Success();
        if (result.getSuccess()){
            success.setErrno(0);
            SuccessMsg successMsg = new SuccessMsg();
            successMsg.setUrl(result.getMessage());
            success.setData(successMsg);
        }else {
            success.setErrno(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return success;
    }
}
