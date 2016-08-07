package com.lizi.shanghaisandtmuseums.utils;


public class FuzzySearchUtil {
	public static boolean fuzzySearch(String searchKey, double fuzzyRate,
			String searchStr) {
		int count = -1;
		String[] searchKeys = searchKey.split("");

		for (int i = 0; i < searchKeys.length; i++) {
			// Log.d("test",searchKeys[i]);
			if (searchKeys[i].equals("(") || searchKeys[i].equals("-")) {
				// Log.d("test",searchKey.substring(0, i).length()+"  "+count);
				if (1.0 * count / (searchKey.substring(0, i).length()) >= fuzzyRate)
					return true;
				break;
			}
			if (searchStr.indexOf(searchKeys[i]) != -1) {
				count++;
			}
			if (1.0 * count / searchKey.length() >= fuzzyRate)
				return true;
		}
		return false;
	}
}
