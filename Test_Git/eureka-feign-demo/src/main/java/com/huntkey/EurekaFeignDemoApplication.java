package com.huntkey;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class EurekaFeignDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaFeignDemoApplication.class, args);
	}


	/**
	 * 注入FastJson配置
	 * @return
	 */
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		fastConverter.setFastJsonConfig(fastJsonConfig);

		List<MediaType> mediaTypes = new ArrayList<>();
		mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);

		/*
		 * 由于json默认的mediaType是*\/*, feign在做请求序列化时需要一种特定格式，则会导致异常，这里需要手动设置mediaType
		 * exception: feign 'Content-Type' cannot contain wildcard type '*'
		 * 设置的mediaType为：application/json;charset=UTF-8
		 */
		fastConverter.setSupportedMediaTypes(mediaTypes);

		HttpMessageConverter<?> converter = fastConverter;

		return new HttpMessageConverters(converter);
	}
}
