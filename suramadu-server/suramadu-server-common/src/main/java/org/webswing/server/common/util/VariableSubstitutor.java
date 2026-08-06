package org.webswing.server.common.util;

import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.webswing.Constants;
import org.webswing.server.common.model.SecuredPathConfig;
import org.webswing.server.common.model.SwingConfig;
import tools.jackson.core.JacksonException;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;


public class VariableSubstitutor {

  private final Map<String, String> customVars;
  private final StrSubstitutor subs;

  public static VariableSubstitutor basic() {
    return new VariableSubstitutor(new HashMap<String, String>());
  }

  public static VariableSubstitutor forSwingInstance(SwingConfig config, String user,
      Map<String, String> userAttributes, String instanceId, String clientIp, String locale,
      String timeZone, String customArgs) {
    Map<String, String> result = new HashMap<String, String>();
    if (user != null) {
      result.put(Constants.USER_NAME_SUBSTITUTE, user);
    }
    if (userAttributes != null) {
      for (Entry<String, String> e : userAttributes.entrySet()) {
        try {
          result.put(Constants.USER_NAME_SUBSTITUTE + "." + e.getKey(), e.getValue());
        } catch (Exception e1) {
          // ignore
        }
      }
    }
    if (instanceId != null) {
      result.put(Constants.INSTANCE_ID_SUBSTITUTE, instanceId);
    }
    if (clientIp != null) {
      result.put(Constants.SESSION_IP_SUBSTITUTE, clientIp);
    }
    if (locale != null) {
      result.put(Constants.SESSION_LOCALE_SUBSTITUTE, locale);
    }
    if (timeZone != null) {
      result.put(Constants.SESSION_TIMEZONE_SUBSTITUTE, timeZone);
    }
    if (customArgs != null) {
      result.put(Constants.SESSION_CUSTOMARGS_SUBSTITUTE, customArgs);
      try {
        Map<String, Object> map = WebswingObjectMapper.get().readValue(customArgs, Map.class);
        flatten(Constants.SESSION_CUSTOMARGS_SUBSTITUTE, map.entrySet().stream())
            .forEach(e -> result.put(e.getKey(), String.valueOf(e.getValue())));
      } catch (JacksonException ignore) {
        // ignore
      }
    }

    VariableSubstitutor substitutor = basic();
    substitutor.extendCustomVars(result);
    return substitutor;
  }

  private static Stream<Entry<String, Object>> flatten(String prefix,
      Stream<Entry<String, Object>> stream) {
    return stream.flatMap(entry -> {
      if (entry.getValue() != null && entry.getValue() instanceof Map) {
        return flatten(prefix + "." + entry.getKey(),
            ((Map<String, Object>) entry.getValue()).entrySet().stream());
      } else {
        return Stream
            .of(new AbstractMap.SimpleEntry<>(prefix + "." + entry.getKey(), entry.getValue()));
      }
    });
  }

  public static VariableSubstitutor forSwingApp(SecuredPathConfig config) {
    Map<String, String> result = new HashMap<String, String>();
    if (config != null) {
      result.put(Constants.APP_HOME_FOLDER_SUBSTITUTE, config.getWebHomeDir());
      result.put(Constants.APP_CONTEXT_PATH_SUBSTITUTE, config.getPath());
    }
    return new VariableSubstitutor(result);
  }


  public VariableSubstitutor(Map<String, String> customVars) {
    this.customVars = customVars;
    this.subs = new StrSubstitutor(new StrLookup<String>() {

      @Override
      public String lookup(String key) {
        String value = VariableSubstitutor.this.customVars.get(key);
        if (value != null) {
          return value;
        }
        value = System.getProperty(key);
        if (value != null) {
          return value;
        }
        value = System.getenv(key);
        if (value != null) {
          return value;
        }
        return null;
      }
    });
  }

  public String replace(String string) {
    return subs.replace(string);
  }

  private Map<String, String> getVariableMap() {
    Map<String, String> result = new HashMap<String, String>();
    result.putAll(System.getenv());
    for (final String name : System.getProperties().stringPropertyNames()) {
      result.put(name, System.getProperties().getProperty(name));
    }
    result.putAll(customVars);
    return result;
  }

  private void extendCustomVars(Map<String, String> customVars) {
    this.customVars.putAll(customVars);
  }

  public Map<String, String> searchVariables(String searchSequence) {
    final int VARIABLES_RESULT_COUNT = 10;
    if (searchSequence == null) {
      searchSequence = "";
    }

    // transform search sequence - this is case insensitive search
    searchSequence = searchSequence.toLowerCase();

    // alphabetically sorted variables, whose name (key of the map entry) starts with search
    // sequence
    Map<String, String> searchResultStartBy = new TreeMap<>();
    // alphabetically sorted variables, whose name (key of the map entry) contains search sequence
    Map<String, String> searchResultContains = new TreeMap<>();

    // first, sort all existing variables by alphabetically order,
    // because empty search string - getting list of first 10th variables, not first 10th found
    // unordered variables
    SortedMap<String, String> variables = new TreeMap<>(getVariableMap());

    for (Map.Entry<String, String> variable : variables.entrySet()) {
      if (searchResultStartBy.size() + searchResultContains.size() == VARIABLES_RESULT_COUNT) {
        break;
      }

      // search in variable names
      //
      String variableLowerCase = variable.getKey().toLowerCase();

      if (variableLowerCase.startsWith(searchSequence)) {
        searchResultStartBy.put(variable.getKey(), variable.getValue());
        continue;
      }
      if (variableLowerCase.contains(searchSequence)) {
        searchResultContains.put(variable.getKey(), variable.getValue());
        continue;
      }

      // search in variable values
      //
      String valueLowerCase = variable.getValue().toLowerCase();

      if (valueLowerCase.contains(searchSequence)) {
        searchResultContains.put(variable.getKey(), variable.getValue());
        // noinspection UnnecessaryContinue
        continue;
      }
    }

    // all results, ordered and sorted per result category
    Map<String, String> allResults = new LinkedHashMap<>();

    allResults.putAll(searchResultStartBy);
    allResults.putAll(searchResultContains);

    return allResults;
  }

}
