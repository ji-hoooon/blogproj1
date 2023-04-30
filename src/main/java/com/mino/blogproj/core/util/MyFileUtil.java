package com.mino.blogproj.core.util;

import com.mino.blogproj.core.exception.ssr.Exception500;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class MyFileUtil {
    public static String write(String uploadFolder, MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        String originalFilename = file.getOriginalFilename();
        String uuidFilename = uuid + "_" + originalFilename;
        try {
            //파일 저장시에 파일 사이즈를 줄여서 저장해야한다.
            //: 실서버에서는 aws파일서버를 S3 사용해야한다.
            Path filePath = Paths.get(uploadFolder + uuidFilename);
            Files.write(filePath, file.getBytes());
        } catch (Exception e) {
            throw new Exception500("파일 업로드 실패 : "+e.getMessage());
        }
        return uuidFilename;
        //단순히 파일의 이름
    }
}
