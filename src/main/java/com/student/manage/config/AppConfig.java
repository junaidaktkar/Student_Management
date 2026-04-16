package com.student.manage.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.student.manage.interceptor.LoggingInterceptor;

@Configuration
						//AsyncConfigurer always use this thread task executor
public class AppConfig implements AsyncConfigurer,WebMvcConfigurer{
	
	@Autowired(required = false)
	private LoggingInterceptor loggingInterceptor;
	
	@Bean(name = "taskExecutor")
	ThreadPoolTaskExecutor taskExecutor() {
	    ThreadPoolTaskExecutor t = new ThreadPoolTaskExecutor();
	    t.setCorePoolSize(4);
	    t.setMaxPoolSize(8);
	    t.setQueueCapacity(4);
	    t.setThreadNamePrefix("Async Threads -");
	    t.initialize(); // IMPORTANT
	    return t;
	}

	//Caching Configuration
	@Bean
	org.springframework.cache.CacheManager localCacheManager() {
	    return new org.springframework.cache.concurrent.ConcurrentMapCacheManager("students");
	}
	
	//Works Only if async's are void
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		AsyncUncaughtExceptionHandler a= (ex, method, params) -> {
			System.out.println("Async UncaughtException : "+ex.getMessage());
			System.out.println("Exception in Method : "+method.toGenericString());
		};
		return a;
	}
	
	//Interceptor Configuration
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if(loggingInterceptor!=null)
		registry.addInterceptor(loggingInterceptor)
				.addPathPatterns("/admin/**");
				
	}
}
