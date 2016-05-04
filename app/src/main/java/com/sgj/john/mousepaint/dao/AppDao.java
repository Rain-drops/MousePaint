package com.sgj.john.mousepaint.dao;

import com.sgj.john.http.Http;
import com.sgj.john.listener.CallbackListener;
import com.sgj.john.mousepaint.model.AllBookModels;
import com.sgj.john.mousepaint.model.AdvModel;
import com.sgj.john.mousepaint.model.CategoryModel;
import com.sgj.john.mousepaint.model.DetialComicBookModel;
import com.sgj.john.mousepaint.model.LoginModel;
import com.sgj.john.mousepaint.model.RegisterModel;
import com.sgj.john.mousepaint.model.SubscribeModel;
import com.sgj.john.mousepaint.url.HttpUrl;
import com.sgj.john.mousepaint.utils.EncryptionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by John on 2015/12/30.
 */
public class AppDao {

    private static final String FromType="2";
    private static AppDao instance;

    public AppDao() {
    }

    public static AppDao getInstance() {
        if(instance == null){
            instance = new AppDao();
        }
        return instance;
    }

    public Map<String, String> createMap(){
        Map<String,String> map = new HashMap<>();
        return map;
    }

    /**
     * 登录
     * @param email
     * @param pass
     * @param listener
     */
    public void userLogin(String email, String pass, CallbackListener<LoginModel> listener){
        Map<String,String> map = createMap();
        map.put("Email", email);
        map.put("Password", EncryptionUtils.createMd5(pass));
        map.put("FromType",FromType);
        Http.post(HttpUrl.URL_USER_LOGIN,map,listener);

    }


    /**
     * 注册
     * @param email
     * @param pass
     * @param listener
     */
    public void userRegister(String email, String pass, CallbackListener<RegisterModel> listener){
        Map<String, String> map = createMap();
        map.put("Email", email);
        map.put("Password", pass);
        map.put("FormType", FromType);
        Http.post(HttpUrl.URL_USER_REGISTER, map, listener);
    }

    /**
     * 订阅漫画
     * @param id
     * @param flag
     * @param listener
     */
    public void subscribeBook(String id, boolean flag, CallbackListener<SubscribeModel> listener){
        Map<String,String> map = createMap();
        map.put("isSubscribe", String.valueOf(flag));
        map.put("bookid",id);
        Http.post(HttpUrl.URL_USER_SUBSCRIBE, map, listener);
    }

    public void getAllBook(CallbackListener<AllBookModels> listener){
        Http.post(HttpUrl.GET_ALL_BOOK, listener);
    }

    /**
     * 获取用户订阅的列表
     * @param Subscribe
     * @param listener
     */
    public void subscribeByUser(String Subscribe, CallbackListener<AllBookModels> listener){
        Map<String, String> map = createMap();
        map.put("Subscribe", Subscribe);
        Http.post(HttpUrl.URL_SUBCRIBE_USER, map, listener);
    }

    /**
     * 获取单本漫画所有章节
     * @param id
     * @param pageIndex
     * @param listener
     */
    public void getBookComicData(String id, String pageIndex, CallbackListener<DetialComicBookModel> listener){

        Map<String, String> map = createMap();
        map.put("id",id);
        map.put("PageIndex",pageIndex);
        Http.post(HttpUrl.URL_COMIC_BOOK_DATA, map, listener);

    }

    /**
     * 获取幻灯片接口
     */
    public void getSlideData(CallbackListener<AdvModel> listener){
        Http.get(HttpUrl.URL_SLIDE_DATA, listener);
    }

    /**
     * 获取某一分类30条记录
     * ClassifyId分类标识，0热血，1国产，2同人，3鼠绘
     * Size每次获取的消息条数，最大值为30
     * PageIndex获取第几页的数据
     */
    public void getCategoryData(String ClassifyId, String Size, String PageIndex, CallbackListener<CategoryModel> listener){
        Map<String,String> map = createMap();
        map.put("ClassifyId", ClassifyId);
        map.put("Size",Size);
        map.put("PageIndex",PageIndex);
        Http.post(HttpUrl.URL_CATEGORY_DATA, map, listener);
    }

    /**
     * 获取最新推荐漫画
     */
    public void getRecommendBook(String Recommended,String Subscribe,CallbackListener<AllBookModels> listener){
        Map<String, String> map = createMap();
        map.put("Recommended", Recommended);
        map.put("Subscribe",Subscribe);
        Http.post(HttpUrl.URL_RECOMMEND, map, listener);
    }

    /**
     * 本周更新
     * @param Days
     * @param Subscribe
     * @param listener
     */
    public void getWeekBook(String Days, String Subscribe, CallbackListener<AllBookModels> listener){
        Map<String, String> map = createMap();
        map.put("Days", Days);
        map.put("Subscribe", Subscribe);
        Http.post(HttpUrl.URL_WEEK_SEVEN, map, listener);
    }
}
