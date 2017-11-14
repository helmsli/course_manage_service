
package com.company.fileManager.fastDfs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;
import com.github.tobato.fastdfs.FdfsClientConfig;

//@Configuration
//@ConditionalOnClass({ Client.class, TransportClientFactoryBean.class })
//@ConditionalOnProperty(prefix = "fileSystem.fastdfs", name = "trackerList", matchIfMissing = false)
//@EnableConfigurationProperties(FastDfsProperties.class)
@Configuration
@ConditionalOnProperty(name = "fdfs.serverUrl")
@Import(FdfsClientConfig.class)
// 解决jmx重复注册bean的问题
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FastDfsAutoConfiguration {
	/*
	private final FastDfsProperties properties;
public FastDfsAutoConfiguration(FastDfsProperties properties) {
		this.properties = properties;
	}
	*/
}
