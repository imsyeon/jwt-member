package com.example.suemember.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    @GetMapping("/download")
    // 안 쓰는 건 날리기
    public void download(@RequestParam("downloadfile") String downloadFile) throws IOException {

        // basic auth 처리, 실제 파일 서버 접속 정보
        String username = "username";
        String password = "password";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);

        HttpEntity request = new HttpEntity(headers); // 안 뜨게끔 구현

        String fileUrl = "http://49.247.36.30:8090/revit/remote.php/dav/files/sooyeon/testfolder/" + downloadFile;

        URI url = URI.create(fileUrl);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, request, byte[].class);
        byte[] buffer = response.getBody(); // fileUrl 유무를 체크해야함

        String fileName = UUID.randomUUID().toString(); // 파일명 (랜덤생성)

        String ext = "." + StringUtils.getFilenameExtension(fileUrl); // 확장자 추출 stringUtills를 사용하지 않고 fileUrl을 추출해서 사용하는 방법도 찾아보기 (순수 java)

        Path target = Paths.get("/Users/imsooyeon/test", fileName + ext); // 파일 저장 경로

        try {
            FileCopyUtils.copy(buffer, target.toFile()); // file copy 말고 다른 것도 있음. 찾아보기
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PutMapping("/upload")
    public void uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {

        String userName = "username";
        String password = "password";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(userName, password);

        InputStream inputStream = multipartFile.getInputStream();
        byte[] data = IOUtils.toByteArray(inputStream);

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(data, headers);

        String url = "http://49.247.36.30:8090/revit/remote.php/dav/files/sooyeon/testfolder/" + multipartFile.getOriginalFilename();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
        // response 나오는 값 확인하기

    }

}
