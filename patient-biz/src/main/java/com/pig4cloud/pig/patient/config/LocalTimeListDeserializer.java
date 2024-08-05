package com.pig4cloud.pig.patient.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <h3>patient</h3>
 *
 * @author HuangJiayu
 * @description <p>自定义时间(HH:mm)反序列化器</p>
 * @date 2024-08-05 21:47
 **/
public class LocalTimeListDeserializer extends JsonDeserializer<List<LocalTime>> {
	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	
	@Override
	public List<LocalTime> deserialize(JsonParser p, DeserializationContext ctxt)
	 throws IOException, JsonProcessingException {
		List<LocalTime> times = new ArrayList<>();
		while (p.nextToken() != JsonToken.END_ARRAY) {
			times.add(LocalTime.parse(p.getText(), formatter));
		}
		return times;
	}
}
