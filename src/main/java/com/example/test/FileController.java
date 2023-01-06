package com.example.test;

import com.example.common.R;
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
public class FileController {

    @Value("${img.path}")
    private String bathPath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileName = UUID.randomUUID()+substring;

        File dir = new File(bathPath);

        if(!dir.exists()){
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(bathPath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            FileInputStream fileInputStream = new FileInputStream(bathPath+name);

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
