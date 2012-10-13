package edu.sjsu.comp295b.helper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassInspector {

	private static final Logger logger = LoggerFactory.getLogger(ClassInspector.class);
			
	public static void inspect(Object object) {
		
		Class<?> clazz = object.getClass();
		Field[] fields = clazz.getDeclaredFields();
		
		for (Field field : fields) {
			
			try {
				
				logger.debug(field.getName() + ": " + field.get(object));
			} catch (IllegalArgumentException e) {

				logger.error("inspectAttributes", e);
			} catch (IllegalAccessException e) {

				logger.error("inspectAttributes", e);
			}
		}
	}
	
	public static Map<String, String> getFieldTypeMap(Object object) {
		
		Class<?> clazz = object.getClass();
		Field[] fields = clazz.getDeclaredFields();
		
		Map<String, String> fieldTypeMap = new HashMap<String, String>();
		
		for (Field field : fields) {
			
			try {
				
				logger.debug(field.getName() + ": " + field.get(object) + ": " + field.getType().toString());
				
				fieldTypeMap.put(field.getName(), field.getType().toString());
			} catch (IllegalArgumentException e) {

				logger.error("inspectAttributes", e);
			} catch (IllegalAccessException e) {

				logger.error("inspectAttributes", e);
			}
		}
		
		return fieldTypeMap;
	}
}
