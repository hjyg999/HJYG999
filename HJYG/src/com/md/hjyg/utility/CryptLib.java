package com.md.hjyg.utility;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class CryptLib {

	/**
	 * Encryption mode enumeration
	 */
	private enum EncryptMode {
		ENCRYPT, DECRYPT;
	}

	// cipher to be used for encryption and decryption
	Cipher _cx;

	// encryption key and initialization vector
	byte[] _key, _iv;

	public CryptLib() throws NoSuchAlgorithmException, NoSuchPaddingException {
		// initialize the cipher with transformation AES/CBC/PKCS5Padding
		_cx = Cipher.getInstance("AES/CBC/PKCS5Padding");
		_key = new byte[32]; // 256 bit key space
		_iv = new byte[16]; // 128 bit IV
	}

	// This function generates md5 hash of the input string //

	public static final String md5(final String inputString) {
		final String MD5 = "MD5";
		try {
			// Create MD5 Hash
			MessageDigest digest = MessageDigest.getInstance(MD5);
			digest.update(inputString.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuilder hexString = new StringBuilder();
			for (byte aMessageDigest : messageDigest) {
				String h = Integer.toHexString(0xFF & aMessageDigest);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	private String encryptDecrypt(String _inputText, String _encryptionKey,
			EncryptMode _mode, String _initVector)
			throws UnsupportedEncodingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException {
		String _out = "";// output string
		// _encryptionKey = md5(_encryptionKey);
		// System.out.println("key="+_encryptionKey);

		int len = _encryptionKey.getBytes("UTF-8").length; // length of the key
															// provided

		if (_encryptionKey.getBytes("UTF-8").length > _key.length)
			len = _key.length;

		int ivlen = _initVector.getBytes("UTF-8").length;

		if (_initVector.getBytes("UTF-8").length > _iv.length)
			ivlen = _iv.length;

		System.arraycopy(_encryptionKey.getBytes("UTF-8"), 0, _key, 0, len);
		System.arraycopy(_initVector.getBytes("UTF-8"), 0, _iv, 0, ivlen);
		// KeyGenerator _keyGen = KeyGenerator.getInstance("AES");
		// _keyGen.init(128);

		SecretKeySpec keySpec = new SecretKeySpec(_key, "AES"); // Create a new
																// SecretKeySpec
		// for the
		// specified key
		// data and
		// algorithm
		// name.

		IvParameterSpec ivSpec = new IvParameterSpec(_iv); // Create a new
		// IvParameterSpec
		// instance with the
		// bytes from the
		// specified buffer
		// iv used as
		// initialization
		// vector.

		// encryption
		if (_mode.equals(EncryptMode.ENCRYPT)) {

			_cx.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);// Initialize this
															// cipher instance
			byte[] results = _cx.doFinal(_inputText.getBytes("UTF-8")); // Finish
			// multi-part
			// transformation
			// (encryption)
			_out = Base64.encodeToString(results, Base64.DEFAULT); // ciphertext
			// output
		}

		// decryption
		if (_mode.equals(EncryptMode.DECRYPT)) {
			_cx.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);// Initialize this
															// ipher instance

			byte[] decodedValue = Base64.decode(_inputText.getBytes(),
					Base64.DEFAULT);
			byte[] decryptedVal = _cx.doFinal(decodedValue); // Finish
			// multi-part
			// transformation
			// (decryption)
			_out = new String(decryptedVal);
		}
		System.out.println(_out);
		return _out; // return encrypted/decrypted string
	}

	// This function computes the SHA256 hash of input string //

	public static String SHA256(String text, int length)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {

		String resultStr;
		MessageDigest md = MessageDigest.getInstance("SHA-256");

		md.update(text.getBytes("UTF-8"));
		byte[] digest = md.digest();

		StringBuffer result = new StringBuffer();
		for (byte b : digest) {
			result.append(String.format("%02x", b)); // convert to hex
		}
		// return result.toString();

		if (length > result.toString().length()) {
			resultStr = result.toString();
		} else {
			resultStr = result.toString().substring(0, length);
		}

		return resultStr;

	}

	// This function encrypts the plain text to cipher text using the key
	// provided. You'll have to use the same key for decryption //

	public String encrypt(String _plainText, String _key, String _iv)
			throws InvalidKeyException, UnsupportedEncodingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException {
		return encryptDecrypt(_plainText, _key, EncryptMode.ENCRYPT, _iv);
	}

	/**
	 * this function generates random string for given length
	 */
	public static String generateRandomIV(int length) {
		SecureRandom ranGen = new SecureRandom();
		byte[] aesKey = new byte[16];
		ranGen.nextBytes(aesKey);
		StringBuffer result = new StringBuffer();
		for (byte b : aesKey) {
			result.append(String.format("%02x", b)); // convert to hex
		}
		if (length > result.toString().length()) {
			return result.toString();
		} else {
			return result.toString().substring(0, length);
		}
	}
}
