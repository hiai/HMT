package cn.edu.scau.hometown.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2016/2/5.
 */
public class SecondHandMarketPicBean implements Serializable {
    /*data:[
   {
       "seconduploads_id": "11",
           "seconduploads_code": null,
           "seconduploads_usercode": "27",
           "seconduploads_location": "2015-06-04/",
           "seconduploads_bewrite": "556fb0ee6e1c3.jpg",
           "seconduploads_date": "1433383150",
           "seconduploads_size": "1787626",
           "seconduploads_type": "image/jpeg",
           "seconduploads_goodscode": "46"
   },
   {
       "seconduploads_id": "12",
           "seconduploads_code": null,
           "seconduploads_usercode": "27",
           "seconduploads_location": "2015-06-04/",
           "seconduploads_bewrite": "556fc2c62f38a.jpg",
           "seconduploads_date": "1433387718",
           "seconduploads_size": "1787626",
           "seconduploads_type": "image/jpeg",
           "seconduploads_goodscode": "48"
   },
   {
       "seconduploads_id": "18",
           "seconduploads_code": null,
           "seconduploads_usercode": "0",
           "seconduploads_location": "2015-06-06/",
           "seconduploads_bewrite": "55725475dba69.jpg",
           "seconduploads_date": "1433556086",
           "seconduploads_size": "1787626",
           "seconduploads_type": "image/jpeg",
           "seconduploads_goodscode": "57"
   }
   ]*/
    private List<Pic> pics;

    public List<Pic> getPics() {
        return pics;
    }

    public void setPics(List<Pic> pics) {
        this.pics = pics;
    }

    public static class Pic implements Serializable {
        private String seconduploads_goodscode;
        private String seconduploads_location;
        private String seconduploads_bewrite;

        public String getSeconduploads_goodscode() {
            return seconduploads_goodscode;
        }

        public void setSeconduploads_goodscode(String seconduploads_goodscode) {
            this.seconduploads_goodscode = seconduploads_goodscode;
        }


        public String getSeconduploads_location() {
            return seconduploads_location;
        }

        public void setSeconduploads_location(String seconguploads_location) {
            this.seconduploads_location = seconguploads_location;
        }

        public String getSeconduploads_bewrite() {
            return seconduploads_bewrite;
        }

        public void setSeconduploads_bewrite(String seconduploads_bewrite) {
            this.seconduploads_bewrite = seconduploads_bewrite;
        }

    }
}
