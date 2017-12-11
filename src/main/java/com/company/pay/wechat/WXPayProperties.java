package com.company.pay.wechat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.company.pay.wechat.sdk.IWXPayDomain;
import com.company.pay.wechat.sdk.WXPayConfig;
//@Configuration
//@PropertySource(value = {"classpath:weChatPay.properties"},encoding="utf-8")  
//@ConditionalOnProperty(name = "pay.webChat.enable")
@ConfigurationProperties(prefix = "pay.webChat")
public class WXPayProperties extends WXPayConfig{
	// 微信开发平台应用id
	private String appID="wx3ccbf4ac04ebd137";
	
	// 商家号
	private String mchID = "1480876362";

	// api key 校验用，重要
	private String key = "xinwei2400weixinsaomazhifu000000";
	//证书地址
	private String certPath = "D://CERT/common/apiclient_cert.p12";
    
	private String notifyUrl="http://127.0.0.1";
	
    private byte[] certData=null;
    private static WXPayProperties INSTANCE;
    
    
    
    public WXPayProperties() {
        
    }

    public static WXPayProperties getInstance() throws Exception{
        if (INSTANCE == null) {
            synchronized (WXPayProperties.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WXPayProperties();
                }
            }
        }
        return INSTANCE;
    }
    @Override
    public String getAppID() {
        return appID;
    }
    @Override
    public String getMchID() {
        return mchID;
    }
    @Override
    public String getKey() {
        return key;
    }

    
    public String getCertPath() {
		return certPath;
	}

	public void setCertPath(String certPath) {
		this.certPath = certPath;
		
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	public void setMchID(String mchID) {
		this.mchID = mchID;
	}
	@Override
	public void setKey(String key) {
		this.key = key;
	}

	
	private void readCertData()
    {
    	InputStream certStream =null;
    	try {
			File file = new File(certPath);
			certStream= new FileInputStream(file);
			this.certData = new byte[(int) file.length()];
			certStream.read(this.certData);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	finally
    	{
    		if(certStream!=null)
    		{
	    		try {
					certStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    }
    public InputStream getCertStream() {
    	if(this.certData==null)
    	{
    		synchronized(this)
    		{
    			if(this.certData==null)
    			{		
    				readCertData();    	
    			}
    		}
    	}
    	ByteArrayInputStream certBis;
        certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }


    public int getHttpConnectTimeoutMs() {
        return 2000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    public IWXPayDomain getWXPayDomain() {
        return WXPayDomainSimpleImpl.instance();
    }

    public String getPrimaryDomain() {
        return "api.mch.weixin.qq.com";
    }

    public String getAlternateDomain() {
        return "api2.mch.weixin.qq.com";
    }

    @Override
    public int getReportWorkerNum() {
        return 1;
    }

    @Override
    public int getReportBatchSize() {
        return 2;
    }

	@Override
	public String getNotifyUrl() {
		// TODO Auto-generated method stub
		return this.notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	
	
}
