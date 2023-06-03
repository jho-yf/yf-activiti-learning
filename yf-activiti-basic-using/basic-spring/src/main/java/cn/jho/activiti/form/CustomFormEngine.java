package cn.jho.activiti.form;

import org.activiti.engine.form.FormData;
import org.activiti.engine.impl.form.JuelFormEngine;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * <p>CustomFormEngine</p>
 *
 * @author JHO xu-jihong@qq.com
 */
public class CustomFormEngine extends JuelFormEngine {

    public static final String MINE_FORM_ENGINE_NAME = "custom";

    @Override
    protected String getFormTemplateString(FormData formInstance, String formKey) {
        try {
            File file = ResourceUtils.getFile("classpath:" + formKey);
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String getName() {
        return MINE_FORM_ENGINE_NAME;
    }
}
