package com.rlogin.common.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 */
public class JSONUtils {

	private static final ObjectMapper mapper = new ObjectMapper();

	/**
	 * Bean对象转换json字符串
	 * 
	 * @param value
	 * @return
	 */
	public final static String objectToJson(Object value) {
		StringWriter writer = new StringWriter();
		JsonGenerator jsonGenerator = null;
		try {
			jsonGenerator = mapper.getJsonFactory().createJsonGenerator(writer);
			mapper.writeValue(jsonGenerator, value);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (jsonGenerator != null) {
					jsonGenerator.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return writer.toString();
	}

	public static String objectToJson(Object pojo, boolean prettyPrint) {
		StringWriter sw = new StringWriter();
		JsonGenerator jg = null;
		try {
			jg = mapper.getJsonFactory().createJsonGenerator(sw);
			if (prettyPrint) {
				jg.useDefaultPrettyPrinter();
			}
			mapper.writeValue(jg, pojo);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (jg != null) {
					jg.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sw.toString();
	}

	public final static ObjectMapper getMapperInstance() {
		return mapper;
	}

	/**
	 * json字符串转换为对应Bean对象
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <V> V jsonToObject(String json, Class<V> clazz) {
		V value = null;
		try {
			if (clazz == String.class) {
				return clazz.cast(json);
			}
			mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			value = mapper.readValue(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static <V> V jsonToList(String json, Class<V> clazz) {
		V value = null;
		try {
			mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			value = mapper.readValue(json, new TypeReference<List<V>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> jsonToMap(String json) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = mapper.readValue(json, Map.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

}
