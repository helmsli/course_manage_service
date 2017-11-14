package com.company.fileManager.fastDfs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.github.tobato.fastdfs.domain.MateData;

@Component
@ConditionalOnProperty(name = "fdfs.serverUrl")
public class FastDFSClientWrapper {
	@Value("${fdfs.serverUrl}")
	private String fdfsServerUrl;
	@Autowired
    protected FastFileStorageClient storageClient;
	protected  Logger LOGGER = LoggerFactory.getLogger(getClass());
    @Autowired
    private ThumbImageConfig thumbImageConfig;

    	public String getCrtThumbImagePrefixName()
    	{
    		return thumbImageConfig.getPrefixName();
    	}

	    /**
	     * 上传文件
	     * @param file 文件对象
	     * @return 文件访问地址
	     * @throws IOException
	     */
	    public String uploadFile(MultipartFile file) throws IOException {
	        StorePath storePath = storageClient.uploadFile(file.getInputStream(),file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()),null);
	        return getResAccessUrl(storePath);
	    }
	    public List<String> uploadFileAndCrtThumbImage(MultipartFile file) throws IOException {
	    	List<String> fileFullPathList = new ArrayList<String>();
	    	String prefixName = this.getCrtThumbImagePrefixName();
	    	String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
	        StorePath storePath = this.uploadImageAndCrtThumbImage(file, null);
	        String originalFullPath= getResAccessUrl(storePath);
	        fileFullPathList.add(originalFullPath);
	        String originalPath = storePath.getPath();
	        String crtThumbImage = originalPath.substring(0,originalPath.length() - fileExtension.length()-1);
	        storePath.setPath(crtThumbImage + prefixName + "." + fileExtension);
	        String thumbImageFullPath =  getResAccessUrl(storePath);
	        fileFullPathList.add(thumbImageFullPath);
	        return fileFullPathList;
	    }
	    /*
	     * public void testUploadImageAndCrtThumbImage() {
        LOGGER.debug("##上传文件..##");
        Set<MateData> metaDataSet = createMateData();
        StorePath path = uploadImageAndCrtThumbImage(TestConstants.PERFORM_FILE_PATH, metaDataSet);
        LOGGER.debug("上传文件路径{}", path);

        // 验证获取MataData
        LOGGER.debug("##获取Metadata##");
        Set<MateData> fetchMateData = storageClient.getMetadata(path.getGroup(), path.getPath());
        assertEquals(fetchMateData, metaDataSet);

        // 验证获取从文件
        LOGGER.debug("##获取Metadata##");
        // 这里需要一个获取从文件名的能力，所以从文件名配置以后就最好不要改了
        String slavePath = thumbImageConfig.getThumbImagePath(path.getPath());
        // 或者由客户端再记录一下从文件的前缀
        FileInfo slaveFile = storageClient.queryFileInfo(path.getGroup(), slavePath);
        assertNotNull(slaveFile);
        LOGGER.debug("##获取到从文件##{}", slaveFile);

    }
	     */

	    /**
	     * 将一段字符串生成一个文件上传
	     * @param content 文件内容
	     * @param fileExtension
	     * @return
	     */
	    public String uploadFile(String content, String fileExtension) {
	        byte[] buff = content.getBytes(Charset.forName("UTF-8"));
	        StorePath storePath;
	        ByteArrayInputStream stream=null;
			try {
				 stream = new ByteArrayInputStream(buff);
				storePath = storageClient.uploadFile(stream,buff.length, fileExtension,null);
				return getResAccessUrl(storePath);	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				if(stream!=null)
				{
					try {
						stream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return null;
	        
	    }

	    // 封装图片完整URL地址
	    private String getResAccessUrl(StorePath storePath) {
	        String fileUrl = fdfsServerUrl + storePath.getFullPath();
	        return fileUrl;
	    }

	    /**
	     * 删除文件
	     * @param fileUrl 文件访问地址
	     * @return
	     */
	    public void deleteFile(String fileUrl) {
	        if (StringUtils.isEmpty(fileUrl)) {
	            return;
	        }
	        try {
	            StorePath storePath = StorePath.praseFromUrl(fileUrl);
	            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
	        } catch (FdfsUnsupportStorePathException e) {
	           e.printStackTrace();
	        }
	    }
	    
	    /**
	     * 上传并压缩图片
	     * @param file
	     * @param metaDataSet
	     * @return
	     */
	    private StorePath uploadImageAndCrtThumbImage(MultipartFile file, Set<MateData> metaDataSet) {
	        try {
	            
	            return storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), metaDataSet);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	           
	        }
	        return null;

	    }

	    private Set<MateData> createMateData() {
	        Set<MateData> metaDataSet = new HashSet<MateData>();
	        metaDataSet.add(new MateData("Author", "wyf"));
	        metaDataSet.add(new MateData("CreateDate", "2016-01-05"));
	        return metaDataSet;
	    }

}
