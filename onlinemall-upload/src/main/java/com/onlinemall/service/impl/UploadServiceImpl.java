package com.onlinemall.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.onlinemall.bean.ParamConfiguration;
import com.onlinemall.service.UploadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private ParamConfiguration paramConfiguration;

    @Autowired
    private FastFileStorageClient storageClient;

    @Override
    public String upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();

        //校验文件的类型
        String contentType = file.getContentType();
        if(!CONTENT_TYPES.contains(contentType)){
            //文件类型不合法, 直接返回null
            LOGGER.info("文件类型不合法:{}",originalFilename);
            return null;
        }

        try {
            //检验文件的内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if(bufferedImage == null){
                LOGGER.info("文件内容不合法:{}",originalFilename);
                return null;
            }
            //保存到服务器
            String ext = StringUtils.substringAfterLast(originalFilename, ".");
            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);

            //生成url地址, 返回
            return paramConfiguration.getBaseUrl() + storePath.getFullPath();
        }catch (IOException e){
            LOGGER.info("服务器内部错误:{}", originalFilename);
            e.printStackTrace();
        }
        return null;
    }
}
