package com.example.controller;

import com.example.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/file")
@Api(tags = "文件上传下载接口")
public class FileController {

    @Value("${img.path}")
    private String bathPath;

    @PostMapping("/upload")
    @ApiOperation("文件下载至本地")
    public R<String> upload(MultipartFile file){

        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));

        String img = UUID.randomUUID()+substring;

        File dir = new File(bathPath);

        if(!dir.exists()){
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(bathPath+img));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(img);
    }

    @GetMapping("/download")
    @ApiOperation("文件上传至页面")
    public void download(String img, HttpServletResponse response){
        try {
            FileInputStream fileInputStream = new FileInputStream(bathPath+img);

            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len =0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
