package com.jiangnan.service.impl;

import com.google.gson.Gson;
import com.jiangnan.domain.ResponseResult;
import com.jiangnan.enums.AppHttpCodeEnum;
import com.jiangnan.exception.SystemException;
import com.jiangnan.service.UploadService;
import com.jiangnan.utils.PathUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;

@Service
@Data
@ConfigurationProperties(prefix = "oss")
public class OssUploadService implements UploadService {

    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        //判断文件类型
        //获取原始文件名
        String originalFilename = img.getOriginalFilename();
        //对原始文件名进行判断
        if (!originalFilename.endsWith(".png") && !originalFilename.endsWith((".jpg"))) {
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }

        //若判断通过，则上传文件到OSS
        String filePath = PathUtils.generateFilePath(originalFilename);
        String url = uploadOss(img, filePath);//2099/2/3
        return ResponseResult.okResult(url);
    }

    private String accessKey;
    private String secretKey;
    private String bucket;


    private String uploadOss(MultipartFile imgFile, String filePath) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region1());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
//        String accessKey = "Mwk7aurh7shvgAcJpiXnVe3sCzEtMMZF5CTXuhlv";
//        String secretKey = "Tl5KufdrduO6ewMQ253lnR9tytt4TKk-PoMiLFLg";
//        String bucket = "jn--blog";

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;

        try {
//            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
//            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);

            //前端上传的文件通过流上传
            InputStream inputStream = imgFile.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return "http://sfqcl43cf.hb-bkt.clouddn.com/" + key;
            } catch (QiniuException ex) {
                ex.printStackTrace();
                if (ex.response != null) {
                    System.err.println(ex.response);

                    try {
                        String body = ex.response.toString();
                        System.err.println(body);
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return "www";
    }
}
