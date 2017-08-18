package br.com.imovelcontrol.config.init;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

import br.com.imovelcontrol.config.JPAConfig;
import br.com.imovelcontrol.config.MailConfig;
import br.com.imovelcontrol.config.SecurityConfig;
import br.com.imovelcontrol.config.ServiceConfig;
import br.com.imovelcontrol.config.WebConfig;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{JPAConfig.class, MailConfig.class, ServiceConfig.class, SecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        HttpPutFormContentFilter httpPutFormContentFilter = new HttpPutFormContentFilter();
        return new Filter[]{httpPutFormContentFilter};
    }

    @Override
    protected void customizeRegistration(Dynamic registration) {
        registration.setMultipartConfig(new MultipartConfigElement(""));
    }
}
