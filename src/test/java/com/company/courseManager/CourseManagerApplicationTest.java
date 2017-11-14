package com.company.courseManager;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.company.fileManager.fastDfs.FastDFSClientWrapper;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CourseManagerApplicationTest {
	@Autowired
	private FastDFSClientWrapper fastDFSClientWrapper;

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	public void testUploadFileWithoutMateData() {

		// LOGGER.debug("##上传文件..##");
		try {
			File file = new File("C:\\Users\\helmsli\\Pictures\\邀请.jpg");
			FileInputStream in_file = new FileInputStream(file);
			MultipartFile multi = new MockMultipartFile("邀请1.jpg","邀请1.jpg",null, in_file);
			
			// CommonsMultipartFile file = new CommonsMultipartFile(fileItem);
			String exte = FilenameUtils.getExtension(multi.getOriginalFilename());
			String path = fastDFSClientWrapper.uploadFile(multi);
			System.out.println(path);
			
			 List<String>pathes = fastDFSClientWrapper.uploadFileAndCrtThumbImage(multi);
			System.out.println(pathes);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
