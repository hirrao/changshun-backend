package com.pig4cloud.pig.patient.config;

import java.time.LocalTime;
import java.util.List;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <h3>patient</h3>
 *
 * @author HuangJiayu
 * @description <p>自定义时间(小时:分钟)序列化器</p>
 * @date 2024-08-05 21:46
 **/
public class LocalTimeListSerializer extends JsonSerializer<List<LocalTime>> {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	
	@Override
	public void serialize(List<LocalTime> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartArray();
		for (LocalTime time : value) {
			gen.writeString(time.format(formatter));
		}
		gen.writeEndArray();
	}
}
