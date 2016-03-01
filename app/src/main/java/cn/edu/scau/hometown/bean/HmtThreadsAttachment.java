package cn.edu.scau.hometown.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/30 0030.
 */
public class HmtThreadsAttachment implements Serializable{

    /**
     * data : {"dateline":"1442539843","filename":"11.jpg","filesize":"1695617","attachment":"http://hometown.scau.edu.cn/bbs/data/attachment/forum/201509/18/093043b8x9r018dxdrk39e.jpg","description":"","isimage":"-1","thumb":"0","picid":"0"}
     * status : success
     */

    private DataEntity data;
    private String status;
    private String error;
    public void setError(String error){
        this.error=error;
    }
    public String getError(){
        return  error;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataEntity getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public static class DataEntity implements Serializable{
        /**
         * dateline : 1442539843
         * filename : 11.jpg
         * filesize : 1695617
         * attachment : http://hometown.scau.edu.cn/bbs/data/attachment/forum/201509/18/093043b8x9r018dxdrk39e.jpg
         * description :
         * isimage : -1
         * thumb : 0
         * picid : 0
         */

        private String dateline;
        private String filename;
        private String filesize;
        private String attachment;
        private String description;
        private String isimage;
        private String thumb;
        private String picid;

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public void setFilesize(String filesize) {
            this.filesize = filesize;
        }

        public void setAttachment(String attachment) {
            this.attachment = attachment;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setIsimage(String isimage) {
            this.isimage = isimage;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public void setPicid(String picid) {
            this.picid = picid;
        }

        public String getDateline() {
            return dateline;
        }

        public String getFilename() {
            return filename;
        }

        public String getFilesize() {
            return filesize;
        }

        public String getAttachment() {
            return attachment;
        }

        public String getDescription() {
            return description;
        }

        public String getIsimage() {
            return isimage;
        }

        public String getThumb() {
            return thumb;
        }

        public String getPicid() {
            return picid;
        }
    }
}
