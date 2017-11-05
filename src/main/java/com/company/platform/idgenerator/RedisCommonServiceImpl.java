package com.company.platform.idgenerator;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;

public class RedisCommonServiceImpl {
	@Resource(name = "redisTemplate")
	protected RedisTemplate<Object, Object> redisTemplate;

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
						return startTime;
					}
					// 如果是第一次进来，最好判断一下是否老的已经过期，否则会死锁
					else if (!needWait) {
						needWait = true;
						String requestTimeS = (String) redisTemplate.opsForValue().get(lockKey);
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

	public void releaseCommonLock(String lockKey, long requestTime) {
		try {
			String requestTimeS = (String) redisTemplate.opsForValue().get(lockKey);
			long requestTimeL = Long.parseLong(requestTimeS);
			if (requestTimeL == requestTime) {
				redisTemplate.delete(lockKey);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
