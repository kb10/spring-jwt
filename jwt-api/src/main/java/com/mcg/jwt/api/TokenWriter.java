package com.mcg.jwt.api;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public abstract class TokenWriter<T> {

	@Autowired
	private PrivateKeyProvider privateKeyProvider;

	@Value("${jwt.algo:'RSA'}")
	private String algorithm = "RSA";
	
	public String createToken(T in, Date expires) throws NoSuchAlgorithmException {
		Key key = privateKeyProvider.getPrivateKey();
		JwtBuilder b = Jwts.builder();
		b = b.setExpiration(expires);
		b = b.addClaims(map(in));
		if(getAlgorithm().equals("RSA")) {
			b = b.signWith(SignatureAlgorithm.RS256, key);
		} else if(getAlgorithm().equals("EC")) {
			b = b.signWith(SignatureAlgorithm.ES256, key);
		} else {
			throw new NoSuchAlgorithmException();
		}
		return b.compact();
		
	}
	
	public abstract Map<String,Object> map(T in);

	public PrivateKeyProvider getPrivateKeyProvider() {
		return privateKeyProvider;
	}

	public void setPrivateKeyProvider(PrivateKeyProvider publicKeyProvider) {
		this.privateKeyProvider = publicKeyProvider;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
	
}
