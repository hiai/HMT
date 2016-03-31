package cn.edu.scau.hometown.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2015/11/16.
 */
public class SecondHandMarketGoodsDetailBean implements Serializable {
    /**
     * secondgoods_id : 96
     * secondgoods_name : 超级多
     * secondgoods_hownew : 九成新
     * secondgoods_price : 78
     * secondgoods_delivertype : 当面交易
     * secondgoods_paidtype : 现金
     * secondgoods_goodsnums : 1
     * secondgoods_bewrite : 不知道什么鬼
     * secondgoods_efficiency : 交易正常
     * secondgoods_postdate : 1444066671
     * secondgoods_pastdate : 1444325871
     * secondgoods_tradetype : 收购
     * secondgoods_views : 0
     * secondgoods_picture : ["http://localhost/hometown/Uploads/2015-10-06/5612b56fc66ee.png","http://localhost/hometown/Uploads/2015-10-06/5612b56fd4bc0.png","http://localhost/hometown/Uploads/2015-10-06/5612b56fd5b48.png"]
     * secondgoods_linkman : {"members_username":" 1234","members_mobie":"11111111111","members_qqnum":"111111"}
     */

    private String secondgoods_id;
    private String secondgoods_name;
    private String secondgoods_hownew;
    private String secondgoods_price;
    private String secondgoods_delivertype;
    private String secondgoods_paidtype;
    private String secondgoods_goodsnums;
    private String secondgoods_bewrite;
    private String secondgoods_efficiency;
    private String secondgoods_postdate;
    private String secondgoods_pastdate;
    private String secondgoods_tradetype;
    private String secondgoods_views;
    private SecondgoodsLinkmanEntity secondgoods_linkman;
    private List<String> secondgoods_picture;

    public void setSecondgoods_id(String secondgoods_id) {
        this.secondgoods_id = secondgoods_id;
    }

    public void setSecondgoods_name(String secondgoods_name) {
        this.secondgoods_name = secondgoods_name;
    }

    public void setSecondgoods_hownew(String secondgoods_hownew) {
        this.secondgoods_hownew = secondgoods_hownew;
    }

    public void setSecondgoods_price(String secondgoods_price) {
        this.secondgoods_price = secondgoods_price;
    }

    public void setSecondgoods_delivertype(String secondgoods_delivertype) {
        this.secondgoods_delivertype = secondgoods_delivertype;
    }

    public void setSecondgoods_paidtype(String secondgoods_paidtype) {
        this.secondgoods_paidtype = secondgoods_paidtype;
    }

    public void setSecondgoods_goodsnums(String secondgoods_goodsnums) {
        this.secondgoods_goodsnums = secondgoods_goodsnums;
    }

    public void setSecondgoods_bewrite(String secondgoods_bewrite) {
        this.secondgoods_bewrite = secondgoods_bewrite;
    }

    public void setSecondgoods_efficiency(String secondgoods_efficiency) {
        this.secondgoods_efficiency = secondgoods_efficiency;
    }

    public void setSecondgoods_postdate(String secondgoods_postdate) {
        this.secondgoods_postdate = secondgoods_postdate;
    }

    public void setSecondgoods_pastdate(String secondgoods_pastdate) {
        this.secondgoods_pastdate = secondgoods_pastdate;
    }

    public void setSecondgoods_tradetype(String secondgoods_tradetype) {
        this.secondgoods_tradetype = secondgoods_tradetype;
    }

    public void setSecondgoods_views(String secondgoods_views) {
        this.secondgoods_views = secondgoods_views;
    }

    public void setSecondgoods_linkman(SecondgoodsLinkmanEntity secondgoods_linkman) {
        this.secondgoods_linkman = secondgoods_linkman;
    }

    public void setSecondgoods_picture(List<String> secondgoods_picture) {
        this.secondgoods_picture = secondgoods_picture;
    }

    public String getSecondgoods_id() {
        return secondgoods_id;
    }

    public String getSecondgoods_name() {
        return secondgoods_name;
    }

    public String getSecondgoods_hownew() {
        return secondgoods_hownew;
    }

    public String getSecondgoods_price() {
        return secondgoods_price;
    }

    public String getSecondgoods_delivertype() {
        return secondgoods_delivertype;
    }

    public String getSecondgoods_paidtype() {
        return secondgoods_paidtype;
    }

    public String getSecondgoods_goodsnums() {
        return secondgoods_goodsnums;
    }

    public String getSecondgoods_bewrite() {
        return secondgoods_bewrite;
    }

    public String getSecondgoods_efficiency() {
        return secondgoods_efficiency;
    }

    public String getSecondgoods_postdate() {
        return secondgoods_postdate;
    }

    public String getSecondgoods_pastdate() {
        return secondgoods_pastdate;
    }

    public String getSecondgoods_tradetype() {
        return secondgoods_tradetype;
    }

    public String getSecondgoods_views() {
        return secondgoods_views;
    }

    public SecondgoodsLinkmanEntity getSecondgoods_linkman() {
        return secondgoods_linkman;
    }

    public List<String> getSecondgoods_picture() {
        return secondgoods_picture;
    }

    public static class SecondgoodsLinkmanEntity implements Serializable{
        /**
         * members_username :  1234
         * members_mobie : 11111111111
         * members_qqnum : 111111
         */

        private String members_username;
        private String members_mobie;
        private String members_qqnum;

        public void setMembers_username(String members_username) {
            this.members_username = members_username;
        }

        public void setMembers_mobie(String members_mobie) {
            this.members_mobie = members_mobie;
        }

        public void setMembers_qqnum(String members_qqnum) {
            this.members_qqnum = members_qqnum;
        }

        public String getMembers_username() {
            return members_username;
        }

        public String getMembers_mobie() {
            return members_mobie;
        }

        public String getMembers_qqnum() {
            return members_qqnum;
        }
    }
}
