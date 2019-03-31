package top.songm.controller;

import com.alibaba.fastjson.JSONArray;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.songm.exception.UploadException;
import top.songm.model.response.Msg;
import top.songm.utils.Constant;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

/**
 * @author songm
 * @datetime 2019/3/4 22:32
 */
@RestController
@RequestMapping("/image")
public class ImageController {

    @PostMapping("/upload")
    public Msg upload(Msg msg, MultipartFile imgFile, HttpServletRequest request) {
        try {
            String originalFilename = imgFile.getOriginalFilename();
            String fileName = UUID.randomUUID().toString().replace("-", "") + originalFilename.substring(originalFilename.lastIndexOf("."));
            String filePath = Constant.getUplodFilePath() + fileName;
            imgFile.transferTo(new File(filePath));
            msg.setData(request.getScheme() +"://" + request.getServerName()  + ":" +request.getServerPort()  + "/upload/" + fileName);
        } catch (Exception e) {
            throw new UploadException(e);
        }
        return msg;
    }

    @PostMapping("/uploadMore")
    public Msg uploadMore(Msg msg, MultipartFile[] imgFiles, HttpServletRequest request) {
        JSONArray array = new JSONArray();
        try {
            for (MultipartFile imgFile : imgFiles) {
                String originalFilename = imgFile.getOriginalFilename();
                String fileName = UUID.randomUUID().toString().replace("-", "") + originalFilename.substring(originalFilename.lastIndexOf("."));
                String filePath = Constant.getUplodFilePath() + fileName;
                imgFile.transferTo(new File(filePath));
                array.add(request.getScheme() +"://" + request.getServerName()  + ":" +request.getServerPort()  + "/upload/" + fileName);
            }
        } catch (Exception e) {
            throw new UploadException(e);
        }
        msg.setData(array);
        return msg;
    }
}
