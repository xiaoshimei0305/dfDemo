package com.idragon.dfdemo.configure;

import com.idragon.dfdemo.Application;
import com.idragon.tool.base.StringUtils;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @author chenxinjun
 */
@Data
@Component
@org.springframework.context.annotation.Configuration
@ConfigurationProperties(prefix = "freemarker")
public class FreemarkConfigure {
    /**
     * 模版存储路径
     */
    String templateDirectory;
    String charset;
    @Bean
    public Configuration getFreemarkerConfigure() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setDirectoryForTemplateLoading(new File(Application.class.getResource("/").getPath()+StringUtils.getValue(templateDirectory,"templates/")));
        cfg.setDefaultEncoding(StringUtils.getValue(charset,"UTF-8"));
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return cfg;
    }
}
