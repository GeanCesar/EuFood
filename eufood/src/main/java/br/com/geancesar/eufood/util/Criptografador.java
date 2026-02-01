package br.com.geancesar.eufood.util;

import org.apache.commons.codec.digest.DigestUtils;

public class Criptografador {

	private static Criptografador instance;

	private final static String HASH = "bb6b1570733367a34545cb3c8d26eff0";

	public static Criptografador getInstance() {
		if (instance == null) {
			instance = new Criptografador();
		}
		return instance;
	}

	private Criptografador() {
	}

	public String criptografaSenha(String senha) {
		StringBuilder sb = new StringBuilder();
		sb.append(senha);
		sb.append(HASH);

		return DigestUtils.md5Hex(sb.toString()).toUpperCase();
	}

}
