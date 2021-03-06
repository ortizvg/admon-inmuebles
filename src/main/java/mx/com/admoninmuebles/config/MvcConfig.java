package mx.com.admoninmuebles.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import mx.com.admoninmuebles.storage.StorageProperties;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    private StorageProperties storageProperties;

    @Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(0);
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(new Locale("es", "MX"));
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
    
    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/" + storageProperties.getLocation() + "/**").addResourceLocations("file:" + storageProperties.getLocation() + "/");
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(byteArrayHttpMessageConverter());
    }
     
    @Bean
    public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
        ByteArrayHttpMessageConverter arrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        arrayHttpMessageConverter.setSupportedMediaTypes(getSupportedMediaTypes());
        return arrayHttpMessageConverter;
    }
     
    private List<MediaType> getSupportedMediaTypes() {
        List<MediaType> list = new ArrayList<MediaType>();
        list.add(MediaType.IMAGE_JPEG);
        list.add(MediaType.IMAGE_PNG);
        list.add(MediaType.IMAGE_GIF);
        list.add(MediaType.APPLICATION_PDF);
        list.add(new MediaType("text", "csv"));
        list.add(new MediaType("application", "csv"));
        list.add(MediaType.APPLICATION_OCTET_STREAM);
        return list;
    }
    
//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
//        registrar.setUseIsoFormat(true);
//        registrar.registerFormatters(registry);
//    }
    
//    @Bean
//    public FormattingConversionService conversionService() {
//        DefaultFormattingConversionService conversionService = 
//          new DefaultFormattingConversionService(false);
// 
//        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
//        registrar.setDateFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
//        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
//        registrar.registerFormatters(conversionService);
// 
//        return conversionService;
//    }

}