
package com.company.pay.wechat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

import com.company.pay.wechat.sdk.WXPay;
import com.company.pay.wechat.sdk.WXPayConfig;
import com.github.tobato.fastdfs.FdfsClientConfig;

//@Configuration
//@ConditionalOnClass({ Client.class, TransportClientFactoryBean.class })
//@ConditionalOnProperty(prefix = "fileSystem.fastdfs", name = "trackerList", matchIfMissing = false)
//@EnableConfigurationProperties(FastDfsProperties.class)
@Configuration
@ConditionalOnProperty(name = "pay.webChat.appID")
//@Import(WXPayProperties.class)
@EnableConfigurationProperties(WXPayProperties.class)
// 解决jmx重复注册bean的问题
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class WXPayAutoConfiguration {
	
	
	
}
