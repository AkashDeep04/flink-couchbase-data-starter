package com.aman.flink.utility;

import com.aman.flink.Env;
import com.aman.flink.constants.Constant;
import com.aman.flink.model.StarterJsonDocument;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.java.utils.ParameterTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Util {

	private static final Logger LOG = LoggerFactory.getLogger(Util.class);

	private static ParameterTool parameter = null;

	private Util() {

	}

	/**
	 * sets application properties to global env properties
	 */
	public static synchronized void setApplicationProperties(String... args) {
		if (null == parameter) {
			ParameterTool params = ParameterTool.fromArgs(args);
			try {
				String filePath = params.get(Constant.CONFIG_PROP_PARAM_NAME);
				parameter = ParameterTool.fromPropertiesFile(filePath);
			} catch (IOException e) {
				LOG.warn(e.getMessage());
			}
			Env.instance.getExecutionEnv().getConfig().setGlobalJobParameters(parameter);
		}
	}

	/**
	 * gets application properties from global env properties
	 */
	public static synchronized ParameterTool getApplicationProperties() {
		return (ParameterTool) Env.instance.getExecutionEnv().getConfig().getGlobalJobParameters();
	}

	/**
	 * Try deserializing json doc list string to list of @StarterJsonDocument if possible
	 */
	public static List<StarterJsonDocument> acceptJsonStringAsDocument(String jsonDocString) {
		try {
			return new ObjectMapper().readValue(jsonDocString,
					StarterJsonDocument.getStarterJsonDocumentTypeReference());
		} catch (Exception ex) {
			LOG.warn(ex.getMessage());
			return Collections.emptyList();
		}
	}
}
