package com.neoderm.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * json工具类
 */
@Slf4j
public class JsonUtil {
	private static ObjectMapper mapper = new ObjectMapper();

	static {
		// 对LocalDateTime类，提供统一的序列化方式
		SimpleModule simpleModule = new SimpleModule("JsonMapSerializer", Version.unknownVersion());
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		simpleModule.addSerializer(new LocalDateTimeSerializer(dateTimeFormatter));
		simpleModule.addSerializer(new LocalDateSerializer(dateFormatter));
		simpleModule.addSerializer(new LocalTimeSerializer(timeFormatter));
		simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
		simpleModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
		simpleModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));
		mapper.registerModule(simpleModule);
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 允许单引号的字段名
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		// 允许没加引号的字段名
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
	}

	/**
	 * 因为性能原因，mapper的属性只能set一次，所以创建了2个
	 */
	private static ObjectMapper mapper_all = new ObjectMapper();

	static {
		mapper_all.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 允许单引号的字段名
		mapper_all.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		// 允许没加引号的字段名
		mapper_all.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		mapper_all.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
	}

	/**
	 * 判断对象是否为基本类型（primvate + string）
	 *
	 * @param object
	 * @return
	 */
	public static boolean objectIsPrimitiveOrString(Object object) {
		return (object != null && (object.getClass().isPrimitive() || object.getClass().equals(String.class)
				|| object.getClass().equals(Integer.class) || object.getClass().equals(Long.class)
				|| object.getClass().equals(Double.class) || object.getClass().equals(Float.class)
				|| object.getClass().equals(Boolean.class) || object.getClass().equals(Short.class)
				|| object.getClass().equals(Byte.class) || object.getClass().equals(Character.class)));
	}

	/**
	 * java bean to json字符串
	 *
	 * @param bean
	 * @return
	 */
	public static String beanToJsonString(Object bean) {
		return beanToJsonString(bean, false);
	}

	/**
	 * java bean to json字符串
	 *
	 * @param bean
	 * @return
	 */
	public static String beanToJsonString(Object bean, boolean allField) {
		if (bean == null) {
			return null;
		}
		try {
			if (allField) {
				return mapper_all.writeValueAsString(bean);
			} else {
				return mapper.writeValueAsString(bean);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * java bean to json字符串
	 *
	 * @param bean
	 * @return
	 */
	public static String beanToJsonString(ObjectMapper objectMapper, Object bean) {
		if (bean == null) {
			return null;
		}
		try {
			if (objectMapper != null) {
				return objectMapper.writeValueAsString(bean);
			} else {
				return mapper.writeValueAsString(bean);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * java bean to json字符串
	 *
	 * @param bean
	 * @return
	 */
	public static String beanToJsonStringPretty(Object bean) {
		return beanToJsonStringPretty(bean, false);
	}

	/**
	 * java bean to json字符串
	 *
	 * @param bean
	 * @return
	 */
	public static String beanToJsonStringPretty(Object bean, boolean allField) {
		if (bean == null) {
			return null;
		}
		try {
			if (allField) {
				return mapper_all.writerWithDefaultPrettyPrinter().writeValueAsString(bean);
			} else {
				return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bean);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * java bean to json字符串
	 *
	 * @param bean
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String beanToJsonStringThrowException(Object bean) throws JsonProcessingException {
		if (bean == null) {
			return null;
		}
		return mapper.writeValueAsString(bean);
	}

	/**
	 * createObjectNode
	 *
	 * @return
	 */
	public static ObjectNode createObjectNode() {
		return mapper.createObjectNode();
	}

	/**
	 * json字符串 to JsonNode
	 *
	 * @param jsonString
	 * @return
	 */
	public static JsonNode jsonStringToTree(String jsonString) {
		if (StringUtils.isBlank(jsonString)) {
			return null;
		}
		try {
			JsonNode t = mapper.readTree(jsonString);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * json字符串 to Java Bean
	 *
	 * @param jsonString
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object jsonStringToBean(String jsonString, String className) {
		if (StringUtils.isBlank(jsonString)) {
			return null;
		}
		try {
			Class beanClass = Class.forName(className);
			Object t = mapper.readValue(jsonString, beanClass);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * json字符串 to Java Bean
	 *
	 * @param jsonString
	 * @return
	 */
	public static <T extends Object> T jsonStringToBean(String jsonString, TypeReference<T> typeReference) {
		if (StringUtils.isBlank(jsonString)) {
			return null;
		}
		try {
			T t = mapper.readValue(jsonString, typeReference);
			return t;
		} catch (Exception e) {
			log.error("jsonStringToBean 异常");
		}
		return null;
	}

	/**
	 * 获取泛型的JavaType
	 *
	 * @param paramClass
	 * @param elementClasses
	 * @return
	 */
	public static JavaType buildJavaType(Class<?> paramClass, Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(paramClass, elementClasses);
	}

	/**
	 * 获取泛型的JavaType
	 *
	 * @param paramClass
	 * @param javaTypes
	 * @return
	 */
	public static JavaType buildJavaType(Class<?> paramClass, JavaType... javaTypes) {
		return mapper.getTypeFactory().constructParametricType(paramClass, javaTypes);
	}

	/**
	 * json字符串 to Java Bean
	 *
	 * @param jsonString
	 * @param paramClass
	 * @param elementClasses
	 * @return
	 * @throws Exception
	 */
	public static <T> T jsonStringToBean(String jsonString, Class<?> paramClass, Class<?>... elementClasses) {
		return jsonStringToBean(jsonString, buildJavaType(paramClass, elementClasses));
	}

	/**
	 * json字符串 to Java Bean
	 *
	 * @param jsonString
	 * @return
	 */
	public static <T extends Object> T jsonStringToBean(String jsonString, JavaType javaType) {
		if (StringUtils.isBlank(jsonString)) {
			return null;
		}
		try {
			return mapper.readValue(jsonString, javaType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * json字符串 to Java Object
	 *
	 * @param jsonString
	 * @return
	 */
	public static Object jsonStringToObject(String jsonString, Class<?> beanClass) {
		if (StringUtils.isBlank(jsonString)) {
			return null;
		}
		try {
			return mapper.readValue(jsonString, beanClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * json字符串 to Java Bean
	 *
	 * @param jsonString
	 * @return
	 */
	public static <T extends Object> T jsonStringToBean(String jsonString, Class<T> beanClass) {
		if (StringUtils.isBlank(jsonString)) {
			return null;
		}
		try {
			return mapper.readValue(jsonString, beanClass);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("jsonStringToBean fail=@@" + jsonString + "@@");
		}
		return null;
	}

	/**
	 * json字符串 to Java Bean
	 *
	 * @param jsonString
	 * @return
	 */
	public static <T extends Object> T jsonStringToBean(ObjectMapper objectMapper, String jsonString, Class<T> beanClass) {
		if (StringUtils.isBlank(jsonString)) {
			return null;
		}
		try {
			if (objectMapper != null) {
				return objectMapper.readValue(jsonString, beanClass);
			} else {
				return mapper.readValue(jsonString, beanClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("jsonStringToBean fail=@@" + jsonString + "@@");
		}
		return null;
	}

	/**
	 * json字符串变为json树节点（JsonNode）
	 *
	 * @param jsonString
	 * @return
	 */
	public static JsonNode jsonStringToJsonNode(String jsonString) {
		if (StringUtils.isBlank(jsonString)) {
			return null;
		}
		try {
			return mapper.readTree(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 对象 to Map
	 *
	 * @param object
	 * @param mapClass
	 * @param keyClass
	 * @param valueClass
	 * @return
	 */
	public static <K extends Object, V extends Object> Map<K, V> objectToMap(Object object, Class<?> mapClass,
			Class<K> keyClass, Class<V> valueClass) {
		if (object == null) {
			return null;
		}
		try {
			JavaType t = mapper.getTypeFactory().constructParametricType(mapClass, keyClass, valueClass);
			return mapper.readValue(beanToJsonString(object), t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * string to Map
	 *
	 * @param string
	 * @param mapClass
	 * @param keyClass
	 * @param valueClass
	 * @return
	 */
	public static <K extends Object, V extends Object> Map<K, V> jsonStringToMap(String string, Class<?> mapClass,
			Class<K> keyClass, Class<V> valueClass) {
		try {
			if (StringUtils.isNotBlank(string)) {
				JavaType t = mapper.getTypeFactory().constructParametricType(mapClass, keyClass, valueClass);
				return mapper.readValue(string, t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * json字符串 to Java Bean
	 *
	 * @param object
	 * @return
	 */
	public static <T extends Object> T objectToBean(Object object, Class<T> beanClass) {
		if (object == null) {
			return null;
		}
		return jsonStringToBean(beanToJsonString(object), beanClass);
	}

	/**
	 * json字符串 to Java Bean
	 *
	 * @param jsonString
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public static <T extends Object> T jsonStringToBeanThrowException(ObjectMapper objectMapper, String jsonString, Class<T> beanClass)
			throws JsonParseException, JsonMappingException, IOException {
		if (objectMapper != null) {
			objectMapper.readValue(jsonString, beanClass);
		}
		return mapper.readValue(jsonString, beanClass);
	}

	/**
	 * json字符串 to Java Bean List
	 *
	 * @param jsonString
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@SuppressWarnings("unchecked")
	public final static <T extends Object> List<T> jsonStringToBeanListThrowException(String jsonString,
			Class<T> beanClass) throws JsonParseException, JsonMappingException, IOException {
		if (StringUtils.isBlank(jsonString)) {
			return null;
		}
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, beanClass);
		return (List<T>) mapper.readValue(jsonString, javaType);
	}

	/**
	 * json字符串 to Java Bean List
	 *
	 * @param jsonString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final static <T extends Object> List<T> jsonStringToBeanList(String jsonString, Class<T> beanClass) {
		if (StringUtils.isBlank(jsonString)) {
			return null;
		}
		try {
			JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, beanClass);
			return (List<T>) mapper.readValue(jsonString, javaType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}
}
