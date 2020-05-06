package com.idragon.dfdemo.server;

import com.idragon.dfdemo.configure.FcmFileConfigure;
import com.idragon.dfdemo.util.WordUtils;
import com.idragon.dfdemo.util.fcm.BeanParseUtils;
import com.idragon.dfdemo.util.fcm.FcmWordUtils;
import com.idragon.dfdemo.util.fcm.dto.BeanInfo;
import com.idragon.dfdemo.util.fcm.word.BeanInfoWordUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenxinjun
 * word文档处理工具
 */
@Service
public class FcmWordService {
    @Autowired
    FcmFileConfigure fcmFileConfigure;
    @Autowired
    DataLoaderService dataLoaderService;

    /**
     * 创建实体文档
     * @param excelFileName 数据原始文件
     * @param beanDir  实体文件名称
     * @throws Exception
     */
    public void buildBeansWordFile(String excelFileName,String beanDir) throws Exception {
        String targetDir= fcmFileConfigure.getFileBasePath()+ fcmFileConfigure.getBeansDir(beanDir)+"/";
        WordUtils utils=new WordUtils();
        List<BeanInfo> beanInfos = dataLoaderService.getBeanList(excelFileName);
        BeanParseUtils beanParseUtils =new BeanParseUtils(beanInfos);
        BeanInfoWordUtils beanInfoWordUtils=new BeanInfoWordUtils();
        for(BeanInfo info:beanInfos){
            XWPFDocument content = beanInfoWordUtils.getBeanDocumentWithData(info, beanParseUtils);
            utils.exportFile(content,targetDir+info.getCode()+".docx");
        }
    }

    /**
     * 生成目标文档
     * @param excelFileName excel 名称
     * @param modelFileName 原始文件
     * @param resultFileName 生成文件
     * @throws Exception
     */
    public void buildWorFile(String excelFileName,String modelFileName,String resultFileName) throws Exception {
        FcmWordUtils utils=new FcmWordUtils(fcmFileConfigure);
        utils.buildWordDocument(excelFileName, modelFileName, resultFileName);
    }

}
