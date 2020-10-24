package com.idragon.dfdemo.util.fcm;

import freemarker.template.DefaultMapAdapter;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.List;

/**
 * @author chenxinjun
 */
public class FreeMarkUtils implements TemplateMethodModelEx {
    @Override
    public Object exec(List list) throws TemplateModelException {
        DefaultMapAdapter map= (DefaultMapAdapter) list.get(0);
        SimpleScalar code= (SimpleScalar) list.get(1);
        return map.get(code.getAsString());
    }
}
