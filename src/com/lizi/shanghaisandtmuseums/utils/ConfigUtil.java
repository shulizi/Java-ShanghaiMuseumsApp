package com.lizi.shanghaisandtmuseums.utils;

public class ConfigUtil {
	public static final String GET_NEWS_URL = "http://121.42.159.177/news_connect/getJSON.php";
	public static String JSON_RESULT;
	public static String FOUNDATION_MUSEUM = "基础场馆";
	public static String COMPREHENSIVE_MUSEUM = "综合场馆";
	public static String THEME_MUSEUM = "主题场馆";
	public static String MUSEUM = "科技馆";
	public static String SHANGHAI = "上海";
	public static String SEARCH = "搜索";
	public static String MUSEUM_INTRODUCTION = "场馆介绍";
	public static String APP_NAME = "上海科技馆大全";
	public static String NO_SEARCH_MUSEUM = "搜索不到，换个关键词试试……";

	public static String[] SHANGHAI_DISTRICTS = { "普陀区", "静安区", "杨浦区", "黄浦区",
			"南汇区", "嘉定区", "徐汇区", "奉贤区", "闸北区", "卢湾区", "长宁区", "闵行区", "青浦区",
			"金山区", "宝山区", "虹口区", "浦东新区", "崇明岛 " };
	public static double[][] DISTRICTS_LOCATION = {
			{ 121.464049, 31.248293, 200 }, { 121.464049, 31.248293, 200 },
			{ 121.464049, 31.248293, 200 }, { 121.464049, 31.248293, 200 },
			{ 121.464049, 31.248293, 200 }, { 121.464049, 31.248293, 200 },
			{ 121.464049, 31.248293, 200 }, { 121.464049, 31.248293, 200 },
			{ 121.464049, 31.248293, 200 }, { 121.464049, 31.248293, 200 },
			{ 121.464049, 31.248293, 200 }, { 121.464049, 31.248293, 200 },
			{ 121.464049, 31.248293, 200 }, { 121.464049, 31.248293, 200 },
			{ 121.464049, 31.248293, 200 }, { 121.464049, 31.248293, 200 },
			{ 121.464049, 31.248293, 200 }, { 121.464049, 31.248293, 200 },
			{ 121.464049, 31.248293, 200 }, { 121.464049, 31.248293, 200 },
			{ 121.464049, 31.248293, 200 }, { 121.464049, 31.248293, 200 },
			{ 121.464049, 31.248293, 200 }, { 121.464049, 31.248293, 200 },};
	public static int COLOR_GRAY = 0xffd0c4c4;
	public static int COLOR_PINK = 0xfffb9797;
	public static int COLOR_GREEN = 0xff9bfb97;
	public static int COLOR_BLUE = 0xff8ab2ed;
	public static int COLOR_PURPLE = 0xffe991ea;
	public static int COLOR_YELLOW = 0xfff8fd70;
	public static int COLOR_RED = 0xfff88585;
	public static int[] COLORS = { COLOR_GRAY, COLOR_PINK, COLOR_GREEN,
			COLOR_BLUE, COLOR_PURPLE, COLOR_YELLOW, COLOR_RED };
}
