package com.business.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    // 模擬縣市與地區數據
    // 模擬縣市與地區數據
    private static final Map<String, List<String>> LOCATION_MAP;

    static {
        Map<String, List<String>> tempMap = new LinkedHashMap<>();

        // 北部地區
        tempMap.put("台北市", List.of("中正區", "大同區", "中山區", "松山區", "大安區", "萬華區", "信義區", "士林區", "北投區", "內湖區", "南港區", "文山區"));
        tempMap.put("新北市", List.of("板橋區", "新莊區", "中和區", "永和區", "土城區", "樹林區", "三峽區", "鶯歌區", "三重區", "蘆洲區", "五股區", "泰山區", "林口區", "淡水區", "金山區", "萬里區", "汐止區", "瑞芳區", "貢寮區", "平溪區", "雙溪區", "深坑區", "石碇區", "新店區", "坪林區", "烏來區"));
        tempMap.put("基隆市", List.of("仁愛區", "信義區", "中正區", "中山區", "安樂區", "暖暖區", "七堵區"));

        // 桃竹苗
        tempMap.put("桃園市", List.of("桃園區", "中壢區", "平鎮區", "八德區", "楊梅區", "蘆竹區", "大溪區", "龍潭區", "龜山區", "大園區", "觀音區", "新屋區", "復興區"));
        tempMap.put("新竹市", List.of("東區", "北區", "香山區"));
        tempMap.put("新竹縣", List.of("竹北市", "竹東鎮", "新埔鎮", "關西鎮", "湖口鄉", "寶山鄉", "芎林鄉", "橫山鄉", "北埔鄉", "峨眉鄉", "尖石鄉", "五峰鄉"));
        tempMap.put("苗栗縣", List.of("苗栗市", "頭份市", "竹南鎮", "後龍鎮", "卓蘭鎮", "通霄鎮", "苑裡鎮", "三灣鄉", "造橋鄉", "頭屋鄉", "公館鄉", "銅鑼鄉", "南庄鄉", "大湖鄉", "獅潭鄉", "泰安鄉"));

        // 中部地區
        tempMap.put("台中市", List.of("中區", "東區", "南區", "西區", "北區", "北屯區", "西屯區", "南屯區", "太平區", "大里區", "霧峰區", "烏日區", "豐原區", "后里區", "石岡區", "東勢區", "和平區", "新社區", "潭子區", "大雅區", "神岡區", "大肚區", "沙鹿區", "龍井區", "梧棲區", "清水區", "大甲區", "外埔區", "大安區"));
        tempMap.put("彰化縣", List.of("彰化市", "鹿港鎮", "和美鎮", "線西鄉", "伸港鄉", "福興鄉", "秀水鄉", "花壇鄉", "芬園鄉", "員林市", "溪湖鎮", "田中鎮", "大村鄉", "埔鹽鄉", "埔心鄉", "永靖鄉", "社頭鄉", "二水鄉", "北斗鎮", "二林鎮", "田尾鄉", "埤頭鄉", "溪州鄉"));
        tempMap.put("南投縣", List.of("南投市", "埔里鎮", "草屯鎮", "竹山鎮", "集集鎮", "名間鄉", "鹿谷鄉", "中寮鄉", "魚池鄉", "國姓鄉", "水里鄉", "信義鄉", "仁愛鄉"));

        // 南部地區
        tempMap.put("嘉義市", List.of("東區", "西區"));
        tempMap.put("嘉義縣", List.of("太保市", "朴子市", "布袋鎮", "大林鎮", "民雄鄉", "溪口鄉", "新港鄉", "六腳鄉", "東石鄉", "義竹鄉", "鹿草鄉", "水上鄉", "中埔鄉", "竹崎鄉", "梅山鄉", "番路鄉", "大埔鄉", "阿里山鄉"));
        tempMap.put("台南市", List.of("中西區", "東區", "南區", "北區", "安平區", "安南區", "永康區", "歸仁區", "新化區", "左鎮區", "玉井區", "楠西區", "南化區", "仁德區", "關廟區", "龍崎區", "官田區", "麻豆區", "佳里區", "西港區", "七股區", "將軍區", "學甲區", "北門區", "新營區", "後壁區", "白河區", "東山區", "六甲區", "下營區", "柳營區", "鹽水區", "善化區", "大內區", "山上區", "新市區", "安定區"));
        tempMap.put("高雄市", List.of("新興區", "前金區", "苓雅區", "鹽埕區", "鼓山區", "旗津區", "前鎮區", "三民區", "楠梓區", "小港區", "左營區", "仁武區", "大社區", "岡山區", "路竹區", "阿蓮區", "田寮區", "燕巢區", "橋頭區", "梓官區", "彌陀區", "永安區", "湖內區", "鳳山區", "大寮區", "林園區", "鳥松區", "大樹區", "旗山區", "美濃區", "六龜區", "內門區", "杉林區", "甲仙區", "桃源區", "那瑪夏區", "茂林區", "茄萣區"));
        tempMap.put("屏東縣", List.of("屏東市", "潮州鎮", "東港鎮", "恆春鎮", "萬丹鄉", "長治鄉", "麟洛鄉", "九如鄉", "里港鄉", "鹽埔鄉", "高樹鄉", "萬巒鄉", "內埔鄉", "竹田鄉", "新埤鄉", "枋寮鄉", "新園鄉", "崁頂鄉", "林邊鄉", "南州鄉", "佳冬鄉", "琉球鄉", "車城鄉", "滿州鄉", "枋山鄉", "霧台鄉", "瑪家鄉", "泰武鄉", "來義鄉", "春日鄉", "獅子鄉", "牡丹鄉", "三地門鄉"));

        // 東部地區
        tempMap.put("宜蘭縣", List.of("宜蘭市", "羅東鎮", "蘇澳鎮", "頭城鎮", "礁溪鄉", "壯圍鄉", "員山鄉", "冬山鄉", "五結鄉", "三星鄉", "大同鄉", "南澳鄉"));
        tempMap.put("花蓮縣", List.of("花蓮市", "鳳林鎮", "玉里鎮", "新城鄉", "吉安鄉", "壽豐鄉", "秀林鄉", "光復鄉", "豐濱鄉", "瑞穗鄉", "萬榮鄉", "卓溪鄉", "富里鄉"));
        tempMap.put("台東縣", List.of("台東市", "成功鎮", "關山鎮", "卑南鄉", "鹿野鄉", "池上鄉", "東河鄉", "長濱鄉", "太麻里鄉", "大武鄉", "綠島鄉", "海端鄉", "延平鄉", "金峰鄉", "達仁鄉", "蘭嶼鄉"));

        // 離島地區
        tempMap.put("澎湖縣", List.of("馬公市", "湖西鄉", "白沙鄉", "西嶼鄉", "望安鄉", "七美鄉"));
        tempMap.put("金門縣", List.of("金城鎮", "金湖鎮", "金沙鎮", "金寧鄉", "烈嶼鄉", "烏坵鄉"));
        tempMap.put("連江縣", List.of("南竿鄉", "北竿鄉", "莒光鄉", "東引鄉"));

        LOCATION_MAP = tempMap;
    }


    // 獲取所有縣市
    @GetMapping("/cities")
    public List<String> getCities() {
        return new ArrayList<>(LOCATION_MAP.keySet());
    }

    // 獲取指定縣市的地區
    @GetMapping("/districts")
    public List<String> getDistricts(@RequestParam String city) {
        return LOCATION_MAP.getOrDefault(city, Collections.emptyList());
    }
}
