/**
 * 
 */
package com.truextend.s4.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * Initialize web app for spring.
 * @author arielsalazar
 */
@Order(1)
public class S4WebAppInitializer implements WebApplicationInitializer {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStartup(ServletContext container) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class);
        container.addListener(new ContextLoaderListener(context));
        container.setInitParameter("contextConfigLocation", "");
    }
}
