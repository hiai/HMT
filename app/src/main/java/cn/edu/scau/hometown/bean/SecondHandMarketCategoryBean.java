package cn.edu.scau.hometown.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2015/11/16.
 */
public class SecondHandMarketCategoryBean implements Serializable {

    private String page;
    private List<GoodsEntity> goods;

    public void setPage(String page) {
        this.page = page;
    }

    public void setGoods(List<GoodsEntity> goods) {
        this.goods = goods;
    }

    public String getPage() {
        return page;
    }

    public List<GoodsEntity> getGoods() {
        return goods;
    }

    public static class GoodsEntity implements Serializable{
        /**
         * secondgoods_id : 58
         * secondgoods_price : 12
         * secondgoods_name : 商品fromalextest7
         * secondgoods_views : 12
         * secondgoods_bewrite : 疯狂点开始放假
         * secondgoods_postdate : 1435635503
         * secondgoods_pastdate : 1435894703
         * secondgoods_picture : null
         */

        private String secondgoods_id;
        private String secondgoods_price;
        private String secondgoods_name;
        private String secondgoods_views;
        private String secondgoods_bewrite;
        private String secondgoods_postdate;
        private String secondgoods_pastdate;
        private String  secondgoods_picture;

        public void setSecondgoods_id(String secondgoods_id) {
            this.secondgoods_id = secondgoods_id;
        }

        public void setSecondgoods_price(String secondgoods_price) {
            this.secondgoods_price = secondgoods_price;
        }

        public void setSecondgoods_name(String secondgoods_name) {
            this.secondgoods_name = secondgoods_name;
        }

        public void setSecondgoods_views(String secondgoods_views) {
            this.secondgoods_views = secondgoods_views;
        }

        public void setSecondgoods_bewrite(String secondgoods_bewrite) {
            this.secondgoods_bewrite = secondgoods_bewrite;
        }

        public void setSecondgoods_postdate(String secondgoods_postdate) {
            this.secondgoods_postdate = secondgoods_postdate;
        }

        public void setSecondgoods_pastdate(String secondgoods_pastdate) {
            this.secondgoods_pastdate = secondgoods_pastdate;
        }


        public String getSecondgoods_id() {
            return secondgoods_id;
        }

        public String getSecondgoods_price() {
            return secondgoods_price;
        }

        public String getSecondgoods_name() {
            return secondgoods_name;
        }

        public String getSecondgoods_views() {
            return secondgoods_views;
        }

        public String getSecondgoods_bewrite() {
            return secondgoods_bewrite;
        }

        public String getSecondgoods_postdate() {
            return secondgoods_postdate;
        }

        public String getSecondgoods_pastdate() {
            return secondgoods_pastdate;
        }


        public String getSecondgoods_picture() {
            return secondgoods_picture;
        }

        public void setSecondgoods_picture(String secondgoods_picture) {
            this.secondgoods_picture = secondgoods_picture;
        }
    }
}
