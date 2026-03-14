package br.com.geancesar.eufood.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

	private static Util instance;

	private Util() {
	}

	public static Util getInstance() {
		if (instance == null) {
			instance = new Util();
		}
		return instance;
	}
	
	public Date getDateTime(String data) {
		return this.getDate(data, "dd/MM/yyyy hh:MM:ss");
	}

	public Date getDate(String data) {
		return this.getDate(data, "dd/MM/yyyy");
	}

	public Date getDate(String data, String mascara) {
		SimpleDateFormat sdf = new SimpleDateFormat(mascara);
		try {
			return sdf.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
