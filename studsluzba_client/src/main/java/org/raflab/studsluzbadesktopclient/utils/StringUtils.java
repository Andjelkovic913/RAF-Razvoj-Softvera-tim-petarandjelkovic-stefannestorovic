package org.raflab.studsluzbadesktopclient.utils;

public class StringUtils {

//	public static boolean allContains(String filter, Object ...objects ) {
//		for(Object obj: objects) {
//			if(obj!=null && !obj.toString().contains(filter))
//				return false;
//		}
//		return true;
//	}
	
//	public static boolean atLeastOneContains(String filter, Object ...objects ) {
//		for(Object obj: objects) {
//			if(obj!=null && obj.toString().toLowerCase().contains(filter.toLowerCase()))
//				return true;
//		}
//		return false;
//	}
	
//	  đ/Đ -> d/D, č/Č -> c/C, ć/Ć -> c/C, š/Š -> s/S, ž/Ž -> z/Z

	public static String normalizeSerbianDiacritics(String text) {
		if (text == null) return null;
		return text
			.replace("đ", "d").replace("Đ", "D")
			.replace("č", "c").replace("Č", "C")
			.replace("ć", "c").replace("Ć", "C")
			.replace("š", "s").replace("Š", "S")
			.replace("ž", "z").replace("Ž", "Z");
	}
}
