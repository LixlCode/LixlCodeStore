package com.wecome.demo.data;

import java.util.Map;
import java.util.TreeMap;

public class SearchData {

    // ===================================白牌带下划线的OpenApi=====================================

    public static final String showMap = "meizu_baidumap://map/show?center=40.057406655722,116.29644071728&zoom=11&traffic=on&bounds = 37.8608310000,112.5963090000,42.1942670000,118.9491260000";

    public static final String marker = "meizu_baidumap://map/marker?location=40.057406655722,116.2964407172&title=Marker&content=makeamarker&traffic=on&status=bottom";

    public static final String map = "meizu_baidumap://map?";

    public static final String geocoder = "meizu_baidumap://map/geocoder?src=openApiDemo&address=北京市海淀区上地信息路9号奎科科技大厦";

    public static final String ungeocoder = "meizu_baidumap://map/geocoder?location=39.98871,116.43234";

    public static final String search = "meizu_baidumap://map/place/search?query=美食&region=beijing&location=39.915168,116.403875&radius=1000&bounds=37.8608310000,112.5963090000,42.1942670000,118.9491260000";

    public static final String direction_transit = "meizu_baidumap://map/direction?origin=name:对外经贸大学|latlng:39.98871,116.43234&destination=name:西直门&mode=transit&sy=3&index=0&target=1";

    public static final String direction_driving = "meizu_baidumap://map/direction?region=beijing&origin=39.98871,116.43234&destination=name:西直门&mode=driving";

    public static final String direction_walking = "meizu_baidumap://map/direction?region=beijing&origin=39.98871,116.43234&destination=40.057406655722,116.2964407172&mode=walking";

    public static final String direction_riding = "meizu_baidumap://map/direction?region=beijing&origin=39.98871,116.43234&destination=40.057406655722,116.2964407172&mode=riding";

    public static final String line = "meizu_baidumap://map/line?region=北京&name=996";

    public static final String nearbysearch = "meizu_baidumap://map/nearbysearch?query=团购";

    public static final String navi = "meizu_baidumap://map/navi?query=故宫";

    public static final String bikenavi = "meizu_baidumap://map/bikenavi?origin=39.98871,116.43234&destination=39.91441,116.40405";

    public static final String walknavi = "meizu_baidumap://map/walknavi?origin=40.057406655722,116.2964407172&destination=39.91441,116.40405";

    public static final String common = "meizu_baidumap://map/navi/common?addr=home";

    public static final String detail = "meizu_baidumap://map/place/detail?uid=09185c56d24f7e44f1193763&show_type=detail_page";

    public static final String offlinemap_anvi = "meizu_baidumap://map/navi/offlinemap";

    public static final String offlinemap_page = "meizu_baidumap://map/page/offlinemap?mode= NORMAL_MAP_MODE";

    public static final String routepage_set = "meizu_baidumap://map/routepage?type=car&action=set_home_action";

    public static final String routepage = "meizu_baidumap://map/routepage?type=car";

    public static final String realtimebus = "meizu_baidumap://map/page/realtimebus?mode= NORMAL_MAP_MODE";

    public static final String cost_share = "meizu_baidumap://map/cost_share?mode=NORMAL_MAP_MODE&url=https://opn.baidu.com/map/2017/duanwufaban?fr=ts0614";



    // ===================================白牌不带下划线的OpenApi=====================================

    public static final String showMap2 = "meizubaidumap://map/show?center=40.057406655722,116.29644071728&zoom=11&traffic=on&bounds = 37.8608310000,112.5963090000,42.1942670000,118.9491260000";

    public static final String marker2 = "meizubaidumap://map/marker?location=40.057406655722,116.2964407172&title=Marker&content=makeamarker&traffic=on&status=bottom";

    public static final String map2 = "meizubaidumap://map?";

    public static final String geocoder2 = "meizubaidumap://map/geocoder?src=openApiDemo&address=北京市海淀区上地信息路9号奎科科技大厦";

    public static final String ungeocoder2 = "meizubaidumap://map/geocoder?location=39.98871,116.43234";

    public static final String search2 = "meizubaidumap://map/place/search?query=美食&region=beijing&location=39.915168,116.403875&radius=1000&bounds=37.8608310000,112.5963090000,42.1942670000,118.9491260000";

    public static final String direction_transit2 = "meizubaidumap://map/direction?origin=name:对外经贸大学|latlng:39.98871,116.43234&destination=name:西直门&mode=transit&sy=3&index=0&target=1";

    public static final String direction_driving2 = "meizubaidumap://map/direction?region=beijing&origin=39.98871,116.43234&destination=name:西直门&mode=driving";

    public static final String direction_walking2 = "meizubaidumap://map/direction?region=beijing&origin=39.98871,116.43234&destination=40.057406655722,116.2964407172&mode=walking";

    public static final String direction_riding2 = "meizubaidumap://map/direction?region=beijing&origin=39.98871,116.43234&destination=40.057406655722,116.2964407172&mode=riding";

    public static final String line2 = "meizubaidumap://map/line?region=北京&name=996";

    public static final String nearbysearch2 = "meizubaidumap://map/nearbysearch?query=团购";

    public static final String navi2 = "meizubaidumap://map/navi?query=故宫";

    public static final String bikenavi2 = "meizubaidumap://map/bikenavi?origin=39.98871,116.43234&destination=39.91441,116.40405";

    public static final String walknavi2 = "meizubaidumap://map/walknavi?origin=40.057406655722,116.2964407172&destination=39.91441,116.40405";

    public static final String common2 = "meizubaidumap://map/navi/common?addr=home";

    public static final String detail2 = "meizubaidumap://map/place/detail?uid=09185c56d24f7e44f1193763&show_type=detail_page";

    public static final String offlinemap_anvi2 = "meizubaidumap://map/navi/offlinemap";

    public static final String offlinemap_page2 = "meizubaidumap://map/page/offlinemap?mode= NORMAL_MAP_MODE";

    public static final String routepage_set2 = "meizubaidumap://map/routepage?type=car&action=set_home_action";

    public static final String routepage2 = "meizubaidumap://map/routepage?type=car";

    public static final String realtimebus2 = "meizubaidumap://map/page/realtimebus?mode= NORMAL_MAP_MODE";

    public static final String cost_share2 = "meizubaidumap://map/cost_share?mode=NORMAL_MAP_MODE&url=https://opn.baidu.com/map/2017/duanwufaban?fr=ts0614";


    // ===================================预装OpenApi=====================================

    public static final String showMap3 = "baidumap://map/show?center=40.057406655722,116.29644071728&zoom=11&traffic=on&bounds = 37.8608310000,112.5963090000,42.1942670000,118.9491260000";

    public static final String marker3 = "baidumap://map/marker?location=40.057406655722,116.2964407172&title=Marker&content=makeamarker&traffic=on&status=bottom";

    public static final String map3 = "baidumap://map?";

    public static final String geocoder3 = "baidumap://map/geocoder?src=openApiDemo&address=北京市海淀区上地信息路9号奎科科技大厦";

    public static final String ungeocoder3 = "baidumap://map/geocoder?location=39.98871,116.43234";

    public static final String search3 = "baidumap://map/place/search?query=美食&region=beijing&location=39.915168,116.403875&radius=1000&bounds=37.8608310000,112.5963090000,42.1942670000,118.9491260000";

    public static final String direction_transit3 = "baidumap://map/direction?origin=name:对外经贸大学|latlng:39.98871,116.43234&destination=name:西直门&mode=transit&sy=3&index=0&target=1";

    public static final String direction_driving3 = "baidumap://map/direction?region=beijing&origin=39.98871,116.43234&destination=name:西直门&mode=driving";

    public static final String direction_walking3 = "baidumap://map/direction?region=beijing&origin=39.98871,116.43234&destination=40.057406655722,116.2964407172&mode=walking";

    public static final String direction_riding3 = "baidumap://map/direction?region=beijing&origin=39.98871,116.43234&destination=40.057406655722,116.2964407172&mode=riding";

    public static final String line3 = "baidumap://map/line?region=北京&name=996";

    public static final String nearbysearch3 = "baidumap://map/nearbysearch?query=团购";

    public static final String navi3 = "baidumap://map/navi?query=故宫";

    public static final String bikenavi3 = "baidumap://map/bikenavi?origin=39.98871,116.43234&destination=39.91441,116.40405";

    public static final String walknavi3 = "baidumap://map/walknavi?origin=40.057406655722,116.2964407172&destination=39.91441,116.40405";

    public static final String common3 = "baidumap://map/navi/common?addr=home";

    public static final String detail3 = "baidumap://map/place/detail?uid=09185c56d24f7e44f1193763&show_type=detail_page";

    public static final String offlinemap_anvi3 = "baidumap://map/navi/offlinemap";

    public static final String offlinemap_page3 = "baidumap://map/page/offlinemap?mode= NORMAL_MAP_MODE";

    public static final String routepage_set3 = "baidumap://map/routepage?type=car&action=set_home_action";

    public static final String routepage3 = "baidumap://map/routepage?type=car";

    public static final String realtimebus3 = "baidumap://map/page/realtimebus?mode= NORMAL_MAP_MODE";

    public static final String cost_share3 = "baidumap://map/cost_share?mode=NORMAL_MAP_MODE&url=https://opn.baidu.com/map/2017/duanwufaban?fr=ts0614";


    public static Map<String, String> getOpenApiDataYes(){
        Map<String, String> data = new TreeMap<>();
        data.put("meizu_baidumap_showMap", showMap);
        data.put("meizu_baidumap_marker", marker);
        data.put("meizu_baidumap_map", map);
        data.put("meizu_baidumap_geocoder", geocoder);
        data.put("meizu_baidumap_ungeocoder", ungeocoder);
        data.put("meizu_baidumap_search", search);
        data.put("meizu_baidumap_direction_transit", direction_transit);
        data.put("meizu_baidumap_direction_driving", direction_driving);
        data.put("meizu_baidumap_direction_walking", direction_walking);
        data.put("meizu_baidumap_direction_riding", direction_riding);
        data.put("meizu_baidumap_ine", line);
        data.put("meizu_baidumap_nearbysearch", nearbysearch);
        data.put("meizu_baidumap_navi", navi);
        data.put("meizu_baidumap_bikenavi", bikenavi);
        data.put("meizu_baidumap_walknavi", walknavi);
        data.put("meizu_baidumap_common", common);
        data.put("meizu_baidumap_detail", detail);
        data.put("meizu_baidumap_offlinemap_anvi", offlinemap_anvi);
        data.put("meizu_baidumap_offlinemap_page", offlinemap_page);
        data.put("meizu_baidumap_routepage_set", routepage_set);
        data.put("meizu_baidumap_routepage", routepage);
        data.put("meizu_baidumap_realtimebus", realtimebus);
        data.put("meizu_baidumap_cost_share", cost_share);

        return data;
    }

    public static Map<String, String> getOpenApiDataNo(){
        Map<String, String> data = new TreeMap<>();
        data.put("meizubaidumap_showMap", showMap2);
        data.put("meizubaidumap_marker", marker2);
        data.put("meizubaidumap_map", map2);
        data.put("meizubaidumap_geocoder", geocoder2);
        data.put("meizubaidumap_ungeocoder", ungeocoder2);
        data.put("meizubaidumap_search", search2);
        data.put("meizubaidumap_direction_transit", direction_transit2);
        data.put("meizubaidumap_direction_driving", direction_driving2);
        data.put("meizubaidumap_direction_walking", direction_walking2);
        data.put("meizubaidumap_direction_riding", direction_riding2);
        data.put("meizubaidumap_line", line2);
        data.put("meizubaidumap_nearbysearch", nearbysearch2);
        data.put("meizubaidumap_navi", navi2);
        data.put("meizubaidumap_bikenavi", bikenavi2);
        data.put("meizubaidumap_walknavi", walknavi2);
        data.put("meizubaidumap_common", common2);
        data.put("meizubaidumap_detail", detail2);
        data.put("meizubaidumap_offlinemap_anvi", offlinemap_anvi2);
        data.put("meizubaidumap_offlinemap_page", offlinemap_page2);
        data.put("meizubaidumap_routepage_set", routepage_set2);
        data.put("meizubaidumap_routepage", routepage2);
        data.put("meizubaidumap_realtimebus", realtimebus2);
        data.put("meizubaidumap_cost_share", cost_share2);

        return data;
    }

    public static Map<String, String> getOpenApiDataBaidu(){
        Map<String, String> data = new TreeMap<>();
        data.put("baidumap_showMap", showMap3);
        data.put("baidumap_marker", marker3);
        data.put("baidumap_map", map3);
        data.put("baidumap_geocoder", geocoder3);
        data.put("baidumap_ungeocoder", ungeocoder3);
        data.put("baidumap_search", search3);
        data.put("baidumap_direction_transit", direction_transit3);
        data.put("baidumap_direction_driving", direction_driving3);
        data.put("baidumap_direction_walking", direction_walking3);
        data.put("baidumap_direction_riding", direction_riding3);
        data.put("baidumap_line", line3);
        data.put("baidumap_nearbysearch", nearbysearch3);
        data.put("baidumap_navi", navi3);
        data.put("baidumap_bikenavi", bikenavi3);
        data.put("baidumap_walknavi", walknavi3);
        data.put("baidumap_common", common3);
        data.put("baidumap_detail", detail3);
        data.put("baidumap_offlinemap_anvi", offlinemap_anvi3);
        data.put("baidumap_offlinemap_page", offlinemap_page3);
        data.put("baidumap_outepage_set", routepage_set3);
        data.put("baidumap_routepage", routepage3);
        data.put("baidumap_realtimebus", realtimebus3);
        data.put("baidumap_cost_share", cost_share3);

        return data;
    }

    public static Map<String, String> getLoadMore(int pageSize){
        Map<String, String> data = new TreeMap<>();

        for (int i = 0; i <= pageSize; i++){
            data.put("无效数据——"+i, "");
        }

        return data;
    }


    //=======================================
//    private static String OpenApi = "baidumap://map/streetscape?panoid=01002200001307231430061545B&pid=01002200001307231430061545B&panotype=street&from_source=share";
//    private static String OpenApi = "baidumap://map/cost_share?mode=NORMAL_MAP_MODE&url=https://opn.baidu.com/map/2017/duanwufaban?fr=ts0614";
//    private static String OpenApi = "baidumap://map/walknavi?origin=39.915291,116.403857&destination=40.056858,116.308194&mode=walking_ar";
//    private static String OpenApi = "baidumap://map/component?popRoot=no&needLocation=yes&needCloud=yes&comName=rentcar&target=rentcar_entrance&mode=MAP_MODE&param={ \"src_from\":\"map_hotel\",\"clear_pos\":\"true\",\"start_latitude\":\"XXX\",\"start_longitude\":\"XXXX\",\"start_keyword\":\"XXXX\",\"end_latitude\":\"4825169.16\",\"end_longitude\":\"12961485.37\",\"end_keyword\":\"北京站\"}";
//    private static String OpenApi = "baidumap://map/component?popRoot=no&needLocation=yes&needCloud=yes&comName=aitravel&target=aitravel_scene&mode=MAP_MODE&param={\"start_latitude\":\"4825169.16\",\"start_longitude\":\"12961485.37\",\"start_keyword\":\"北京站\",\"start_uid\":\"1232132321\",\"end_latitude\":\"4825169.16\",\"end_longitude\":\"12961485.37\",\"end_keyword\":\"北京站\",\"end_uid:\"1232131231\", \"src_from\":\"map_hotel\"}";
//    private static String OpenApi = "baidumap://map/component?target=subway_page_openapi&comName=subway";
//    private static String OpenApi = "baidumap://map/component?comName=international&target=international_travel_schedule&param=%7B%22cityId%22%3A%2226041%22%2C%22pageType%22%3A%22card%22%2C%22travelId%22%3A%22111%22%7D";
//    private static String OpenApi = "baidumap://map/component?comName=violation&target=violation&param={\"src_from\":”around_violation_carservices\"}";
//    private static String OpenApi = "baidumap://map/component?popRoot=no&comName=international&target=international_tourism_guide&param=%7B%22cityid%22%3A26022%2C%22url%22%3A%22http%3A%5C%2F%5C%2Fmapglobal.baidu.com%5C%2Fmapsguide%5C%2Fgetinfo%3FcId%3D26022%22%2C%22tid%22%3A4%7D";
//    private static String OpenApi = "baidumap://map/component?comName=international&target=international_page&param=%7b%22cityid%22%3a%222912%22%2c%22cityname%22%3a%22%e9%a6%99%e6%b8%af%22%2c%22geo%22%3a%2212711279.5%2c2530455.45%22%7d\n";
//    private static String OpenApi = "baidumap://map/component?comName=international&target=international_city_explore_mappage&param=%7b%22cityid%22%3a2912%2c%22maplevel%22%3a%2212%22%2c%22geo%22%3a%2212711279.5%2c2530455.45%22%7d";
//    private static String OpenApi = "baidumap://map/component?comName=international&target=international_exchangerate_page";
//    private static String OpenApi = "baidumap://map/component?comName=international&target=international_tourist_bus_mainpage";
//    private static String OpenApi = "baidumap://map/component?comName=subway&target=subway_page_openapi&popRoot=no&param=%7B%22cityid%22:%2260732%22%7D";
//    private static String OpenApi = "baidumap://map/component?comName=international&target=international_translation_page&param=%7b%22url%22%3a%22https%3a%2f%2fm.baidu.com%2fsf_fanyi%2f%3faldtype%3d16047%26tpltype%3dsigma%23zh%2fen%2f%22%7d";
//    private static String OpenApi = "baidumap://map/component?comName=international&target=international_webview_page&param=%7b%22url%22%3a%22http%3a%2f%2fwww.baidu.com%22%2c%22title%22%3a%22%e7%99%be%e5%ba%a6%22%2c%22is_need_login%22%3a%221%22%7d";
//    private static String OpenApi = "baidumap://map/component?comName=international&target=international_nearby_explore_page&param=%7b%22jumpLevel%22%3a%2212.0%22%2c%22city_point_x%22%3a%2212709611.045404179%22%2c%22city_point_y%22%3a%222529889.7816065005%22%2c%22city_id%22%3a%222912%22%2c%22select_uid%22%3a%22a0a65228d139f925c829155c%22%7d";
//    private static String OpenApi = "baidumap://map/component?comName=international&target=international_travel_city_explore_page&param=%7b%22jump_level%22%3a%2212.0%22%2c%22city_point_x%22%3a%2213523298.87%22%2c%22city_point_y%22%3a%223641065.98%22%2c%22tab%22%3a%223%22%2c%22city_id%22%3a%22289%22%2c%22city_name%22%3a%22%e4%b8%8a%e6%b5%b7%22%7d";
//    private static String OpenApi = "baidumap://map/component?comName=international&target=reset_page_stack_size&param=%7B%22historySize%22%3A2%7D";
//    private static String OpenApi = "baidumap://map/component?comName=scenery&target=scenery_ar_page&src_from=baidu_seach&param=%7B%22from%22%3A%22baidu_seach%22%2C%22mode%22%3A%22ar%22%2C%22uid%22%3A%2206d2dffda107b0ef89f15db6%22%2C%22switch%22%3A%220%22%7D";
//    private static String OpenApi = "baidumap://map/component?comName=scenery&target=scenery_route_guide_page&src_from=baidu_seach&param=%7B%22from%22%3A%22baidu_seach%22%2C%22uid%22%3A%2206d2dffda107b0ef89f15db6%22%2C%22routeIndex%22%3A%222%22%7D";
//    private static String OpenApi = "baidumap://map/component?comName=scenery&target=scenery_voice_guide_page&src_from=baidu_seach&param=%7b%22from%22%3a%22baidu_seach%22%2c%22uid%22%3a%2206d2dffda107b0ef89f15db6%22%7d";
//    private static String OpenApi = "baidumap://map/component?comName=scenery&target=show_poi_detail_page&src_from=poi_detail&param=%7b%22from%22%3a%22poi_detail%22%2c%22uid%22%3a%22550fd5f9f5681979931fc97e%22%2c%22speaking_tag%22%3a%221%22%7d";
//    private static String OpenApi = "baidumap://map/component?comName=scenery&target=show_bestshootingpoint_page&param=%7b%22from%22%3a%22poi_detail_card%22%2c%22uid%22%3a%222a7a25ecf9cf13636d3e1bad%22%7d";
//    private static String OpenApi = "baidumap://map/component?comName=streetscape&target=street_scape_page&param=%7b%22pid%22%3a%2201002200001308291545423025D%22%2c%22from_source%22%3a%22share%22%7d";
//    private static String OpenApi = "baidumap://map/component?target=street_scape_page&comName=streetscape&param=%7b%22uid%22%3a%221a30c5f8cbb55eff71210b02%22%2c%22panotype%22%3a%22street%22%2c%22from_source%22%3a%22share%22%7d";
//    private static String OpenApi = "baidumap://map/component?comName=indoorguide&target=indoor_guide_page&src_from=baidu_seach&param=%7b%22from%22%3a%22baidu_seach%22%2c%22uid%22%3a%221260176407175102463%22%7d";
//    private static String OpenApi = "baidumap://map/component?comName=indoorguide&target=indoor_ar_page&src_from=baidu_seach&param=%7b%22from%22%3a%22baidu_seach%22%2c%22uid%22%3a%221260176407175102463%22%7d";
//    private static String OpenApi = "baidumap://map/component?comName=indoorguide&target=indoor_search_page&src_from=baidu_seach&param=%7b%22from%22%3a%22baidu_seach%22%2c%22uid%22%3a%221260176407175102463%22%7d";
//    private static String OpenApi = "baidumap://map/component?comName=indoorguide&target=indoor_parking&param=%7b%22from%22%3a%22change_parking%22%2c%22uid%22%3a%221260176407175102463%22%2c%22cuid%22%3a%22A42FA5BC4F348F3085DBBC223A163CA3%7c660472930063468%22%7d";
//    private static String OpenApi = "baidumap://map/component?comName=indoorguide&target=indoor_find_car&param=%7b%22from%22%3a%22detail_page%22%2c%22uid%22%3a%221266541600247382015%22%2c%22cuid%22%3a%22A42FA5BC4F348F3085DBBC223A163CA3%7c660472930063468%22%2c%22loc%22%3a%224835288.203585%2c1.2946704994169E7%22%2c%22code%22%3a%22C-283%22%2c%22floor%22%3a%22B2%22%7d";
//    private static String OpenApi = "baidumap://map/component?comName=indoorguide&target=smarttravel_citytravel_page&src_from=baidu_seach&param=%7b%22from%22%3a%22baidu_seach%22%2c%22cityid%22%3a%22131%22%7d";
//    private static String OpenApi = "baidumap://map/component?comName=international&target=international_travel_city_explore_page&param=%7b%22jump_level%22%3a%2212.0%22%2c%22city_point_x%22%3a%2213523298.87%22%2c%22city_point_y%22%3a%223641065.98%22%2c%22tab%22%3a%223%22%2c%22city_id%22%3a%22289%22%2c%22city_name%22%3a%22%e4%b8%8a%e6%b5%b7%22%7d";
//    private static String OpenApi = "baidumap://map/component?comName=international&target=international_travel_schedule&param=%7B%22cityId%22%3A%22131%22%2C%22pageType%22%3A%22list%22%2C%22travelId%22%3A%22111%22%7D";
//    private static String OpenApi = "baidumap://map/component?comName=mapbasear&target=show_armodel_page&param=%7b%22arkey%22%3a%2210089876%22%2c%22arkey_gltf %22%3a%2210287008%22%2c%22from%22%3a%22poi_detail%22%2c%22scene%22%3a%223%22%2c%22ar_type%22%3a%228%22%2c%22is_euler_angle%22%3a%22yes%22%7d";
//    private static String OpenApi = "baidumap://map/component?comName=mapbasear&target=show_arexplore_page&param=%7b%22from%22%3a%22walk_finished%22%7d";
//    private static String OpenApi = "baidumap://map/component?comName=mapbasear&target=show_ardirection_page&param=%7b%22from%22%3a%22walk_finished%22%2c%22loc%22%3a%224838687.14%2c12945185.86%22%2c%22name%22%3a%22%e9%a2%90%e5%92%8c%e5%9b%ad%e6%96%87%e6%98%8c%e9%99%a2%22%2c%22uid%22%3a%22550fd5f9f5681979931fc97e%22%7d";
//    private static String OpenApi = "baidumap://map/component?comName=mapbasear&target=show_ar_around_page&param=%7b%22from%22%3a%22search_surround%22%7d";
//    private static String OpenApi = "baidumap://map/component?target=show_express_page&comName=brand&param=%7B%22express_action%22%3A%22check%22%2C%22business_id%22%3A%221%22%2C%22from_source%22%3A%22poiDetail%22%7D";
//    private static String OpenApi = "baidumap://map/place/detail?uid=06d2dffda107b0ef89f15db6";
//    private static String OpenApi = "baidumap://map/component?comName=international&target=international_travel_schedule";
//    private static String OpenApi = "baidumap://map/component?comName=carowner&target=push_car_owner_main_page";
//    private static String OpenApi = "baidumap://map/walknavi?origin=39.915291,116.403857&destination=40.056858,116.308194";
//    private static String OpenApi = "baidumap://map/component?comName=carowner&target=open_web_page";
//    private static String OpenApi = "baidumap://map/component?comName=indoorguide&target=indoor_search_page&src_from=baidu_seach";
//    private static String OpenApi = "baidumap://map/component?comName=international&target=international_travel_schedule";
//    private static String OpenApi = "baidumap://map/component?comName=international&target=international_travel_city_explore_page";
//    private static String OpenApi = "baidumap://map/component?comName=international&target=international_nearby_explore_page";
//    private static String OpenApi = "baidumap://map/cost_share?url=https://ugc.map.baidu.com/static/specialmap/index.html?spId=9&needLocation=yes&src=webapp.cost_share.specialmap.autojump";
//    private static String OpenApi = "baidumap://map/cost_share?url=https://ugc.map.baidu.com/static/specialmap/index.html?spId=11&needLocation=yes&src=webapp.cost_share.specialmap.autojump";
//    private static String OpenApi = "baidumap://map/cost_share?url=https://ugc.map.baidu.com/static/specialmap/index.html?spId=8&needLocation=yes&src=webapp.cost_share.specialmap.autojump";
//    private static String OpenApi = "baidumap://map/cost_share?url=https://ugc.map.baidu.com/static/specialmap/index.html?spId=3&needLocation=yes&src=webapp.cost_share.specialmap.autojump";
//    public static String OpenApi = "baidumap://map/component?target=subway_page_openapi&comName=subway";
//    public static String OpenApi = "baidumap://map/component?target=subway_page_openapi&comName=subway&src=huawei.hag";
    public static String OpenApi = "baidumap://map/navi/instruction?qt=quit_navi &version=1.1&src=com.huawei.voiceassist";



}
