package com.company.courseManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.company.courseManager.teacher.domain.CourseClassPublish;
import com.google.gson.reflect.TypeToken;
import com.xinwei.nnl.common.util.JsonUtil;

public class TestGson {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
      try {
		String ls ="[{\"chapterId\":0,\"courseList\":[{\"partitionId\":\"105\",\"classId\":\"001_001\",\"courseId\":\"6151050001\",\"chapterId\":\"0\",\"category\":\"0\",\"classTitle\":\"spring boot 瀹夎\",\"classDetail\":\"鎮ㄥ彲浠ヨ窡浣跨敤浠讳綍鏍囧噯Java搴撶殑鏂瑰紡涓€鏍蜂娇鐢⊿pring Boot銆?鍙渶瑕佸湪classpath涓嬪寘鍚浉搴旂殑spring-boot-*.jar鏂囦欢鍗冲彲銆係pring Boot涓嶉渶瑕佷换浣曠壒娈婄殑宸ュ叿鏉ラ泦鎴愶紝鎵€浠ユ偍鍙互浣跨敤浠讳綍IDE鎴栬€呮枃鏈紪杈戝櫒锛涘苟涓擲pring Boot搴旂敤涔熸病浠€涔堢壒娈婁箣澶勶紝鍥犳鍙互鍍忎换浣曞叾瀹僇ava绋嬪簭涓€鏍疯繍琛屼笌璋冭瘯\",\"durationSeconds\":0,\"owner\":\"207264\",\"createTime\":\"2018-05-12 11:19:15\",\"originalPrice\":20,\"realPrice\":20,\"priceVer\":0,\"freeDurations\":0,\"freePercent\":0,\"videoId\":\"de0ffe2e45e74c7fb98bbbf9c7b73692\",\"videoUrl\":\"video/164c3e2-16323bbe5bc-0004-bddd-b59-a48fe.mov\",\"status\":0},{\"partitionId\":\"105\",\"classId\":\"001_001\",\"courseId\":\"6151050001\",\"chapterId\":\"0\",\"category\":\"0\",\"classTitle\":\"spring boot 瀹夎\",\"classDetail\":\"鎮ㄥ彲浠ヨ窡浣跨敤浠讳綍鏍囧噯Java搴撶殑鏂瑰紡涓€鏍蜂娇鐢⊿pring Boot銆?鍙渶瑕佸湪classpath涓嬪寘鍚浉搴旂殑spring-boot-*.jar鏂囦欢鍗冲彲銆係pring Boot涓嶉渶瑕佷换浣曠壒娈婄殑宸ュ叿鏉ラ泦鎴愶紝鎵€浠ユ偍鍙互浣跨敤浠讳綍IDE鎴栬€呮枃鏈紪杈戝櫒锛涘苟涓擲pring Boot搴旂敤涔熸病浠€涔堢壒娈婁箣澶勶紝鍥犳鍙互鍍忎换浣曞叾瀹僇ava绋嬪簭涓€鏍疯繍琛屼笌璋冭瘯\",\"durationSeconds\":0,\"owner\":\"207264\",\"createTime\":\"2018-05-12 11:19:15\",\"originalPrice\":20,\"realPrice\":20,\"priceVer\":0,\"freeDurations\":0,\"freePercent\":0,\"videoId\":\"de0ffe2e45e74c7fb98bbbf9c7b73692\",\"videoUrl\":\"video/164c3e2-16323bbe5bc-0004-bddd-b59-a48fe.mov\",\"status\":0},{\"partitionId\":\"105\",\"classId\":\"001_001\",\"courseId\":\"6151050001\",\"chapterId\":\"0\",\"category\":\"0\",\"classTitle\":\"spring boot 瀹夎\",\"classDetail\":\"鎮ㄥ彲浠ヨ窡浣跨敤浠讳綍鏍囧噯Java搴撶殑鏂瑰紡涓€鏍蜂娇鐢⊿pring Boot銆?鍙渶瑕佸湪classpath涓嬪寘鍚浉搴旂殑spring-boot-*.jar鏂囦欢鍗冲彲銆係pring Boot涓嶉渶瑕佷换浣曠壒娈婄殑宸ュ叿鏉ラ泦鎴愶紝鎵€浠ユ偍鍙互浣跨敤浠讳綍IDE鎴栬€呮枃鏈紪杈戝櫒锛涘苟涓擲pring Boot搴旂敤涔熸病浠€涔堢壒娈婁箣澶勶紝鍥犳鍙互鍍忎换浣曞叾瀹僇ava绋嬪簭涓€鏍疯繍琛屼笌璋冭瘯\",\"durationSeconds\":0,\"owner\":\"207264\",\"createTime\":\"2018-05-12 10:59:27\",\"originalPrice\":20,\"realPrice\":\"10\",\"priceVer\":0,\"freeDurations\":0,\"freePercent\":0,\"videoId\":\"de0ffe2e45e74c7fb98bbbf9c7b73692\",\"videoUrl\":\"video/164c3e2-16323bbe5bc-0004-bddd-b59-a48fe.mov\",\"status\":0},{\"classId\":\"001_002\",\"category\":0,\"classTitle\":\"testSpring\",\"classDetail\":\"test\",\"originalPrice\":\"10\",\"realPrice\":\"10\",\"vdeoUrl\":\"\",\"videoId\":\"\",\"durationSeconds\":\"0\"}],\"chapterTitle\":\"绗?绔?Spring Boot绠€浠?\",\"description\":\"\"}]\r\n" + 
				"";
		Map<String,String> a = new HashMap<String,String>();
		
		
				List<CourseClassPublish> classPublishList =JsonUtil.fromJson(ls, new TypeToken<List<CourseClassPublish>>() {}.getType());
	System.out.println("okkkkkkkkkkkkkkkkkkkk");
      } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}

}
