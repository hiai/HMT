package cn.edu.scau.hometown.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/10/11 0011.
 */
public class SecondHandMarketHomeBean implements Serializable{

    /**
     * goods : [{"secondgoods_id":"58","secondgoods_price":"12","secondgoods_name":"商品fromalextest7","secondgoods_views":"12","secondgoods_bewrite":"疯狂点开始放假","secondgoods_postdate":"1435635503","secondgoods_pastdate":"1435894703","secondgoods_picture":null},{"secondgoods_id":"47","secondgoods_price":"12","secondgoods_name":"佛挡杀佛","secondgoods_views":"4","secondgoods_bewrite":"撒地方第三方","secondgoods_postdate":"1433385044","secondgoods_pastdate":"1433644244","secondgoods_picture":null},{"secondgoods_id":"46","secondgoods_price":"11","secondgoods_name":"第三方第三方","secondgoods_views":"6","secondgoods_bewrite":"顺风娃娃饭店","secondgoods_postdate":"1433383150","secondgoods_pastdate":"1433642350","secondgoods_picture":"http://192.168.253.73/hometown_market/hometownMarket/Uploads/2015-06-04/556fb0ee6d65d.JPG"},{"secondgoods_id":"45","secondgoods_price":"12","secondgoods_name":"第三方二二二","secondgoods_views":"0","secondgoods_bewrite":"粉色为法国","secondgoods_postdate":"1433382871","secondgoods_pastdate":"1433642071","secondgoods_picture":"http://192.168.253.73/hometown_market/hometownMarket/Uploads/2015-06-04/556fafd7681b3.jpg"},{"secondgoods_id":"44","secondgoods_price":"32","secondgoods_name":"啊啊啊啊","secondgoods_views":"0","secondgoods_bewrite":"的撒范德萨","secondgoods_postdate":"1433382608","secondgoods_pastdate":"1433641808","secondgoods_picture":"http://192.168.253.73/hometown_market/hometownMarket/Uploads/2015-06-04/556faed020c2b.jpg"},{"secondgoods_id":"43","secondgoods_price":"2121","secondgoods_name":"出口将分类肯定是","secondgoods_views":"0","secondgoods_bewrite":"额呵呵呵呵","secondgoods_postdate":"1433380668","secondgoods_pastdate":"1433639868","secondgoods_picture":"http://192.168.253.73/hometown_market/hometownMarket/Uploads/2015-06-04/556fa73c33741.jpg"},{"secondgoods_id":"42","secondgoods_price":"12","secondgoods_name":"啊啊啊啊","secondgoods_views":"0","secondgoods_bewrite":"啊啊啊啊啊啊啊啊啊","secondgoods_postdate":"1433378255","secondgoods_pastdate":"1433637455","secondgoods_picture":null},{"secondgoods_id":"40","secondgoods_price":"222","secondgoods_name":"打算放豆腐","secondgoods_views":"0","secondgoods_bewrite":"大送飞吻丰富","secondgoods_postdate":"1433346336","secondgoods_pastdate":"1433605536","secondgoods_picture":null},{"secondgoods_id":"38","secondgoods_price":"1234","secondgoods_name":"怎么又是买电脑","secondgoods_views":"2","secondgoods_bewrite":"呵呵呵呵呵呵呵呵","secondgoods_postdate":"1433345494","secondgoods_pastdate":"1433604694","secondgoods_picture":null},{"secondgoods_id":"37","secondgoods_price":"2222","secondgoods_name":"打电脑大点挠","secondgoods_views":"0","secondgoods_bewrite":"弄弄弄弄弄弄弄弄","secondgoods_postdate":"1433345367","secondgoods_pastdate":"1433604567","secondgoods_picture":null}]
     * page : <nav><ul class="pagination">  <li><span class="current">1</span></li><li><a class="num" href="/hometown_market/hometownMarket/index.php/Home/Api/index/p/2.html">2</a></li><li><a class="num" href="/hometown_market/hometownMarket/index.php/Home/Api/index/p/3.html">3</a></li> <li><a class="next" href="/hometown_market/hometownMarket/index.php/Home/Api/index/p/2.html">>></a></li> </ul></nav>
     */
    private List<GoodsEntity> goods;
    private String page;

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
        private String secondgoods_picture;

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
