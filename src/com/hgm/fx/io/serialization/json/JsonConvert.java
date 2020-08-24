package com.hgm.fx.io.serialization.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hgm.fx.io.files.FileHandle;

import java.io.IOException;

public final class JsonConvert {

	static {
		mapper = new ObjectMapper()
				.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
				//.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
				.enable(SerializationFeature.INDENT_OUTPUT);
	}

	private static ObjectMapper mapper;

	public static String load(FileHandle file) {
		try {
			return new String(file.read().readAllBytes());
			//return new String(Files.readAllBytes(file.file().toPath()));
		} catch(IOException e) { e.printStackTrace(); }

		return null;
	}

	public static String serialize(Object value) {
		try {
			return mapper.writeValueAsString(value);
		} catch(JsonProcessingException e) { e.printStackTrace(); }

		return "";
	}

	public static String serialize(Object value, Security security) {
		String jsonUnencrypted = serialize(value);
		if(security == Security.ENCRYPT)
			jsonUnencrypted = JsonEncrypt.encrypt(jsonUnencrypted);
		return jsonUnencrypted;
	}

	public static <T> T deserialize(String json, Class<T> valueClass) {
		T obj = null;

		try {
			if(isValidJSON(json))
				obj = mapper.readValue(json, valueClass);
			else {
				json = JsonEncrypt.decrypt(json);
				if(!isValidJSON(json))
					throw new RuntimeException("JSON value isn't valid after decryption attempt.");
				else
					obj = mapper.readValue(json, valueClass);
			}
		} catch(Exception e) { e.printStackTrace(); }

		return obj;
	}

	public static <T> T deserialize(FileHandle file, Class<T> valueClass) {
		return deserialize(load(file), valueClass);
	}

	public static boolean isValidJSON(final String json) {
		try {
			// throws an exception if the JSON isn't valid
			mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
			mapper.readTree(json);
			mapper.disable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	public static boolean isValidJSON(final FileHandle jsonFile) {
		final String json = load(jsonFile);
		try {
			// throws an exception if the JSON isn't valid
			mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
			mapper.readTree(json);
			mapper.disable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
}
