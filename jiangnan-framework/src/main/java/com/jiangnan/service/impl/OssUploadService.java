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
@ConfigurationProperties(prefix = "myoss")
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

//----------------------------------上传文件到七牛云----------------------------------------

    //注意要从application.yml读取属性数据，下面的3个成员变量的名字必须对应application.yml的myoss属性的三个子属性名字
    private String xxaccessKey;
    private String xxsecretKey;
    private String xxbucket;

    //上传文件的具体代码。MultipartFile是spring提供的接口，作用是实现文件上传
    private String uploadOss(MultipartFile imgFile, String filePath){
        //构造一个带指定 Region 对象的配置类。你的七牛云OSS创建的是哪个区域的，那么就调用Region的什么方法即可
        Configuration cfg = new Configuration(Region.huanan());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        UploadManager uploadManager = new UploadManager(cfg);

        //打开七牛云，把鼠标悬浮在右上角的个人头像，然后就会看到'密钥管理'，点击进入就有你的密钥，把其中的AK和SK复制到下面两行
        //String accessKey = "_ibGP9wytjLCAZPqcFaWQNxbw7fMUvofSOvOFFR3";
        //String secretKey = "QSOAU-cv3sSDGNfVNPF6iXz-PsP5X9QTrjFI9zYw";
        //String bucket = "huanf-blog";
        //为避免上面3行暴露信息，我们会把信息写到application.yml里面，然后添加ConfigurationProperties注解、3个成员变量即可读取

        //文件名，如果写成null的话，就以文件内容的hash值作为文件名
        String key = filePath;

        try {

            //byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            //ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);

            //上面两行是官方写的(注释掉)，下面那几行是我们写的
            //把前端传过来的文件转换成InputStream对象
            InputStream xxinputStream = imgFile.getInputStream();

            Auth auth = Auth.create(xxaccessKey, xxsecretKey);
            String upToken = auth.uploadToken(xxbucket);

            try {
                //把前端传过来的xxinputStream图片上传到七牛云
                Response response = uploadManager.put(xxinputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println("上传成功! 生成的key是: "+putRet.key);
                System.out.println("上传成功! 生成的hash是: "+putRet.hash);
                return "http://sg7jqxqvk.hn-bkt.clouddn.com/"+key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        }catch (Exception e) {
            //ignore
        }
        return "上传失败";
    }
}
