/**
 * 
 */
package com.company.review.utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @notes 
 * 
 * @author wangjiamin
 * 
 * @version 2018年7月7日 下午5:54:36
 */
@Component
public class RedisUtils {
	private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);

	// redis 失效时间 秒
	@Value("${redis.expire.seconds:10}")
	private long expireSecond;

	@Resource
	protected RedisTemplate<String, String> redisTemplate;

	@Autowired
	private StringRedisTemplate redisTemplate2;

	/**
	 * 取数据 String
	 * @param key
	 * @return
	 */
	public String getJsonObject(String key) {
		try {
			String json = redisTemplate2.opsForValue().get(key);
			logger.info("get json from redis success,key=" + key + "json=" + json);
			return json;
		} catch (Exception e) {
			logger.error("get json from redis error,key=" + key, e);
		}
		return null;
	}

	/**
	 * 取数据 List<String>
	 * @param key
	 * @return
	 */
	public List<String> getStringList(String key) {
		try {
			List<String> list = redisTemplate2.opsForList().range(key, 0, -1);
			logger.info("get List<String> from redis success,key=" + key);
			return list;
		} catch (Exception e) {
			logger.error("get List<String> from redis error,key=" + key, e);
		}
		return null;
	}

	/**
	 * 存数据，默认过期时间20minutes
	 * @param key
	 * @param json
	 * @return
	 */
	public boolean setJsonObject(String key, String json) {
		try {
			redisTemplate2.opsForValue().set(key, json, expireSecond, TimeUnit.SECONDS);
			logger.info("set json to redis success,key=" + key + "json=" + json);
		} catch (Exception e) {
			logger.error("set json to redis error,key=" + key, e);
		}
		return true;
	}

	/**
	 * 存数据,设置过期时间
	 * @param key
	 * @param json
	 * @param minutes
	 * @return
	 */
	public boolean setJsonObject(String key, String json, int minutes) {
		try {
			redisTemplate2.opsForValue().set(key, json, minutes, TimeUnit.MINUTES);
			logger.info("set json to redis success,key=" + key);
		} catch (Exception e) {
			logger.error("set json to redis error,key=" + key, e);
		}
		return true;
	}

	/**
	 * 删除数据
	 * @param key
	 * @return
	 */
	public boolean deleteJsonObject(String key) {
		try {
			redisTemplate2.delete(key);
			logger.info("delete json from redis success,key=" + key);
		} catch (Exception e) {
			logger.error("delete json from redis error,key=" + key, e);
		}
		return true;
	}

	/**
	 * 申请通用锁，等待30秒
	 * 
	 * @param lockKey
	 * @return 0--失败
	 */
	public long getCommonLock(String lockKey, int lockSeconds) {
		try {
			long startTime = System.currentTimeMillis();
			boolean needWait = false;
			while (true) {
				{
					if (redisTemplate.opsForValue().setIfAbsent(lockKey, String.valueOf(startTime))) {
						startTime = System.currentTimeMillis();
						redisTemplate.opsForValue().set(lockKey, String.valueOf(startTime), lockSeconds,
								TimeUnit.SECONDS);
						String requestTimeS = redisTemplate.opsForValue().get(lockKey);
						return startTime;
					}
					// 如果是第一次进来，最好判断一下是否老的已经过期，否则会死锁
					else if (!needWait) {
						needWait = true;
						String requestTimeS = redisTemplate.opsForValue().get(lockKey);
						// 获取时间
						if (requestTimeS != null) {
							long requestTimeL = 0;
							try {
								requestTimeL = Long.parseLong(requestTimeS);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// 如果没有获取到，并且已经超时
							if (System.currentTimeMillis() - requestTimeL > lockSeconds * 1000) {
								redisTemplate.delete(lockKey);
								continue;
							} else {
								needWait = true;
							}
						}
						// 如果没有时间
						else {
							redisTemplate.delete(lockKey);
							continue;
						}
					}
				}
				// 如果没有获取到，并且已经超时
				if (System.currentTimeMillis() - startTime > lockSeconds * 1000) {
					return 0;
				}
				// 延迟一段时间
				Thread.sleep(300);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	// 释放redis锁
	public void releaseCommonLock(String lockKey, long requestTime) {
		try {
			String requestTimeS = redisTemplate.opsForValue().get(lockKey);
			if (requestTimeS != null) {
				long requestTimeL = Long.parseLong(requestTimeS);
				if (requestTimeL == requestTime) {
					redisTemplate.delete(lockKey);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
