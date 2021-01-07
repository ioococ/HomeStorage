package Downloader;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
public class DownloadConfig {

    private URL url; //文件下载地址
    private File file; //下载文件保存目标文件
    private int nthread; //下载该文件需要的线程数
    private List<Block> blocks=new ArrayList<>();

    public class Block{
        private File file;
        private Long size;
        private Long totalSize;

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }

        public Long getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(Long totalSize) {
            this.totalSize = totalSize;
        }
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getNthread() {
        return nthread;
    }

    public void setNthread(int nthread) {
        this.nthread = nthread;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }
}
