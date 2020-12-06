package com.wecome.demo.constants;

public class SettingCenterConstants {

    /**
     * 我的积分URL
     */
    public static final String MYJF_URL = "https://newclient.map.baidu.com/client/incentive/duiba/getautourl";

    /**
     * 地点贡献URL
     */
    public static final String MY_CONTRIBUTION_URL = "https://map.baidu.com/node/hybrid/contribution/page/main/";

    /**
     * 地点贡献URL
     */
    public static final String MY_STOP_CAR_HELP =
            "https://oil.baidu.com/static/smart-parking/index.html#/?from=PCenter";

    /**
     * 充电桩地图URL
     */
    public static final String MY_CHARGE_PILE_MAP =
            "https://oil.baidu.com/static/chargemap/index.html?from=PersonalCenter";

    /**
     * 车主服务URL
     */
    public static final String MY_CAR_SERVICE =
            "baidumap://map/component?comName=carowner&target=open_web_page&popRoot=no&param="
                    + "%7B%22url%22%3A%22https%3A%2F%2Fcarowner.baidu.com%2Flegal%2Flegal.html%3Ffr%"
                    + "3Dpersonal_center_carservice%23%2FserviceIndex%22%2C%22from%"
                    + "22%3A%22blah%22%2C%22showShare%22%3A%220%22%7D";

    /**
     * 停车助手URL
     */
    public static final String PARKING_ASS = "https://oil.baidu.com/static/smart-parking/index.html?from=PCenter#/";

    /**
     * 查违章
     */
    public static final String CHECK_VIOLATION =
            "baidumap://map/component?comName=violation&target=violation&param="
                    + "%7B%22src_from%22%3A%22go_violation_carservices%22%7D";


    /**
     * 导航保障URL
     */
//    public static String MY_NAV_PROTECT_URL = "http://webpage.navi.baidu.com/static/"
//            + "webpage/daohangpeifu/dist/page/index.html";

    /**
     * 我的积分URL
     */
    public static String MY_SIGN_GET_SCORE_URL = MYJF_URL;


    /**
     * 消息中心URL
     */
//    public static final String SCHEME = UrlProviderFactory.getUrlProvider().getScheme();
//    public static final String MSG_HOST = UrlProviderFactory.getUrlProvider().getClientDomain() + "/imap/imsg/c";

    /**
     * 行程助手url
     */
//    public static String TRIP_HOST = UrlProviderFactory.getUrlProvider().getTripHelperUrl() + "/aide/";


    /**
     * 我的订单
     */
    public static final String ORDER_URL = "http://mtuan.baidu.com/mapcenter?z=2&pos=idx&type=userIcon&next=ucenter";
    public static final String ORDER_TITLE = "我的订单";

    public static final String ORDER_FILTER_TITLE = "订单筛选";

    public static final String STREETSCAPE_RECOMMAND_URL = "http://client.map.baidu.com/streetscape/index.html";
    public static final String STREETSCAPE_RECOMMAND_TITLE = "全景推荐";

//    public static final String DTYYHD_URL = UrlProviderFactory.getUrlProvider().getOperationWebUrl();
    public static final String DTYYHD_TITLE = "活动专区";

    /**
     * 违章查询
     */
//    public static final String TRAFFIC_VIOLATION_QUERY_URL =
//            UrlProviderFactory.getUrlProvider().getScheme() + "://map.baidu.com/mobile/webapp/third/peccancy/nostat=1";

    /**
     * 用户中心用户信息url（来自ugc）
     */
//    public static final String USER_CENTER_USER_INFO_URL = UGCHttpReqUtils.UGC_DOMAIN + "v1/user/info?";

    /**
     * 签到URL
     */
//    public static final String USER_CENTER_USER_SIGN_IN_URL = UGCHttpReqUtils.UGC_DOMAIN + "v1/integral/sign";

    /**
     * 请求路径
     */
//    public static final String HOST = UrlProviderFactory.getUrlProvider().getClientDomain();
    public static final String PATH = "/phpui2/";
    /**
     * http请求参数校验md5 key
     */
    public static final String HTTP_SIGN_KEY = "sign";

    public static final int BDUSS_ERROR = 10002;

    /**
     * PB result is ok
     */
    public static final int RESPONSE_SUCCESS = 0;

    public static final int USER_ACTIVITY = 1;
    public static final int USER_FEEDBACK = 2;
    public static final int USER_NAVI = 3;
    public static final int USER_CRUISER = 4;
    public static final int USER_NEW_TTS = 5;
    public static final int USER_OFFLINE = 6;
    public static final int USER_NAVI_DOWNLOAD = 7;
    public static final int USER_SHARE_POSITION = 8;
    public static final int USER_STREETSCAPE = 9;
    public static final int USER_SUBWAY = 10;
    public static final int USER_TAXI = 11;
    public static final int USER_MEASURE_DISTANCE = 12;
    public static final int USER_CAPTURE = 13;
    public static final int USER_APP_RECOMMENDED = 15;
    public static final int USER_SETTING = 17;
    public static final int USER_EXITAP = 18;
    public static final int USER_OPENMAP = 19;

    public static final int USER_TRAVEL = 20;
    public static final int USER_SET_HOME = 21;
    public static final int USER_ORDER = 22;

    public static final int USER_NOACTION = 23;
    public static final int USER_OPENMAP_DETAIL = 24;
    public static final int USER_OPENMAP_RETARY = 25;

    public static final int USER_TRAFFIC_VIOLATION_QUERY = 26;

    public static final int USER_COMPONENT_MANAGEMENT = 27;
    public static final int USER_MYLOCATION = 28;

    /**
     * 设置页
     */
    public static final int SETTING_KEEP_AWAKE = 1;
    public static final int SETTING_SHOW_IMG = 2;
    public static final int SETTING_RECIEVE_MSG = 3;
    public static final int SETTING_REVOLVE_MAP = 4;
    public static final int SETTING_CHANGE_VIEWPOINT = 5;
    public static final int SETTING_AUTO_UPDATE = 6;
    public static final int SETTING_SHAKE_VOICE = 7;
    public static final int SETTING_SHOW_MAP_BUBBLE = 8;
    public static final int SETTING_NAVI_CONFIG = 9;
    public static final int SETTING_CHOOSE_SD = 10;
    public static final int SETTING_ABOUT = 13;
    public static final int SETTING_NEW_FUNCTION = 14;
    public static final int SETTING_APP_RECOMM = 15;
    public static final int SETTING_ROUTE_CONDITION = 16;
    public static final int SETTING_PRIVACY = 17;
    public static final int SETTING_ROADCONDITION_PRE = 18;
    public static final int SETTING_VOICE_WAKEUP = 19;
    public static final int SETTING_COMMON_ADDRESS_LAYER_SHOW = 20;
    public static final int SETTING_DIG_COMMON_ADDRESS_SHOW = 21;
    public static final int SETTING_ACCOUNT_CANCEL = 22;
    public static final int SETTING_VOICE_CONFIG = 23;
    /**
     * “我的”里面使用的ExpandListView的item样式
     */
    public static final int EXPANDLIST_STYLE_NORMAL = 0;
    public static final int EXPANDLIST_STYLE_CHECKADBE = 1;

    /**
     * 导航语音
     */
    public static final int USER_NAVI_VOICE = 29;
    /**
     * 我的语音导览
     */
    public static final int USER_AUDIO_GUIDE = 30;

    public static final int USER_FAVORITE = 31;
    public static final int USER_CONTRIBUTION = 32;
    public static final int USER_WALLET = 34;
    public static final int USER_DISCOUNT = 35;
    public static final int USER_BUSINESS = 37;
    public static final int USER_NAVPROTECT = 38;
    public static final int USER_TRAFFIC_NUM = 39;
    public static final String COMPONENT_ID = "component_id";

}
