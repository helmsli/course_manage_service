package com.company.fileManager.fastDfs;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fileSystem.fastdfs")
public class FastDfsProperties {
	private int soTimeout;
	private int connectTimeout;
	private TrackerList trackerList;
	private ThumbImage thumbImage;
	
	
	
	public int getSoTimeout() {
		return soTimeout;
	}

	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public TrackerList getTrackerList() {
		return trackerList;
	}

	public void setTrackerList(TrackerList trackerList) {
		this.trackerList = trackerList;
	}

	public ThumbImage getThumbImage() {
		return thumbImage;
	}

	public void setThumbImage(ThumbImage thumbImage) {
		this.thumbImage = thumbImage;
	}

	public static class ThumbImage {
		private int width;
		private int height;
		public int getWidth() {
			return width;
		}
		public void setWidth(int width) {
			this.width = width;
		}
		public int getHeight() {
			return height;
		}
		public void setHeight(int height) {
			this.height = height;
		}
		
	}
	
	public static class TrackerList {

		/**
		 * Comma-separated list of "host:port" pairs to bootstrap from. This represents an
		 * "initial" list of cluster nodes and is required to have at least one entry.
		 */
		private List<String> nodes;

		/**
		 * Maximum number of redirects to follow when executing commands across the
		 * cluster.
		 */
		
		public List<String> getNodes() {
			return this.nodes;
		}

		public void setNodes(List<String> nodes) {
			this.nodes = nodes;
		}

		
	}

}
