package org.webswing.server.common.util;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor.JavaType;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import com.google.protobuf.ProtocolMessageEnum;
import org.webswing.model.CommonMsg;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class ProtoMapper {

  public static final String PROTO_PACKAGE_APPFRAME_IN =
      "org.webswing.model.appframe.proto.AppFrameProtoIn";
  public static final String PROTO_PACKAGE_APPFRAME_OUT =
      "org.webswing.model.appframe.proto.AppFrameProtoOut";
  public static final String PROTO_PACKAGE_SERVER_APP_FRAME =
      "org.webswing.model.app.proto.ServerAppFrameProto";
  public static final String PROTO_PACKAGE_SERVER_BROWSER_FRAME =
      "org.webswing.model.browser.proto.ServerBrowserFrameProto";
  public static final String PROTO_PACKAGE_ADMIN_CONSOLE_FRAME =
      "org.webswing.model.adminconsole.proto.AdminConsoleProto";
  public static final String PROTO_PACKAGE_CLUSTER_POOL_SERVER =
      "org.webswing.cluster.common.model.proto.Cluster";
  public static final String PROTO_PACKAGE_JWT =
      "org.webswing.server.common.model.security.proto.Jwt";

  private static final String PROTO_PACKAGE_SERVER_COMMON =
      "org.webswing.model.common.proto.CommonProto";

  private final String basicProtoPackageEncode;
  private final String basicProtoPackageDecode;

  private Map<Class<?>, Class<?>> classProtoMap = new HashMap<Class<?>, Class<?>>();

  private ClassLoader classLoader;

  public ProtoMapper(String basicProtoPackageEncodeDecode) {
    this.basicProtoPackageEncode = basicProtoPackageEncodeDecode;
    this.basicProtoPackageDecode = basicProtoPackageEncodeDecode;
  }

  public ProtoMapper(String basicProtoPackageEncodeDecode, ClassLoader classLoader) {
    this.basicProtoPackageEncode = basicProtoPackageEncodeDecode;
    this.basicProtoPackageDecode = basicProtoPackageEncodeDecode;
    this.classLoader = classLoader;
  }

  public ProtoMapper(String basicProtoPackageEncode, String basicProtoPackageDecode) {
    this.basicProtoPackageEncode = basicProtoPackageEncode;
    this.basicProtoPackageDecode = basicProtoPackageDecode;
  }

  public ProtoMapper(String basicProtoPackageEncode, String basicProtoPackageDecode,
      ClassLoader classLoader) {
    this.basicProtoPackageEncode = basicProtoPackageEncode;
    this.basicProtoPackageDecode = basicProtoPackageDecode;
    this.classLoader = classLoader;
  }

  public byte[] encodeProto(Serializable msg) throws IOException {
    Message result = encodeMessage(msg);
    return result.toByteArray();
  }

  public <T> T decodeProto(byte[] bytes, Class<T> msgInClass) throws IOException {
    T result = decodeMessage(bytes, msgInClass);
    return result;
  }

  private <T> T decodeMessage(byte[] bytes, Class<T> c) throws IOException {
    if (bytes == null || bytes.length == 0) {
      return null;
    }
    try {
      Class<?> protoClass = resolveProtoCounterpartClass(c, basicProtoPackageDecode);
      GeneratedMessageV3 protoMsg = (GeneratedMessageV3) protoClass
          .getDeclaredMethod("parseFrom", byte[].class).invoke(null, bytes);
      return decodeMessage(protoMsg, c);
    } catch (IOException e) {
      throw e;
    } catch (Exception e) {
      throw new IOException("decoding " + c + " from ProtoBuffer format failed!", e);
    }

  }

  @SuppressWarnings("rawtypes")
  private <T> T decodeMessage(Message protoMsg, Class<T> c) throws IOException {
    try {
      T result = c.getDeclaredConstructor().newInstance();
      Map<FieldDescriptor, Object> valueMap = protoMsg.getAllFields();
      for (FieldDescriptor fd : valueMap.keySet()) {
        Field field = c.getDeclaredField(fd.getName());
        field.setAccessible(true);
        if (fd.isRepeated()) {
          if (field.getType() == List.class
              && field.getGenericType() instanceof ParameterizedType) {
            List<Object> decodedList = new ArrayList<Object>();
            ParameterizedType type = (ParameterizedType) field.getGenericType();
            Class<?> param = (Class<?>) type.getActualTypeArguments()[0];
            for (Object o : (List) valueMap.get(fd)) {
              if (fd.getJavaType().equals(JavaType.MESSAGE)) {
                decodedList.add(decodeMessage((Message) o, param));
              } else if (fd.getJavaType().equals(JavaType.ENUM)) {
                Enum<?> value = decodeEnum((EnumValueDescriptor) o, param);
                decodedList.add(value);
              } else if (fd.getJavaType().equals(JavaType.BYTE_STRING)) {
                byte[] value = ((ByteString) o).toByteArray();
                decodedList.add(value);
              } else {
                decodedList.add(o);
              }
            }
            field.set(result, decodedList);
          } else {
            throw new IOException(
                "Field '" + fd.getName() + "' of " + c + " has to be List with generics type!");
          }
        } else {
          if (fd.getJavaType().equals(JavaType.MESSAGE)) {
            Object o = decodeMessage((Message) valueMap.get(fd), field.getType());
            field.set(result, o);
          } else if (fd.getJavaType().equals(JavaType.ENUM)) {
            Enum<?> value = decodeEnum((EnumValueDescriptor) valueMap.get(fd), field.getType());
            field.set(result, value);
          } else if (fd.getJavaType().equals(JavaType.BYTE_STRING)) {
            byte[] value = ((ByteString) valueMap.get(fd)).toByteArray();
            field.set(result, value);
          } else {
            field.set(result, valueMap.get(fd));
          }
        }
      }
      return result;
    } catch (Exception e) {
      throw new IOException("decoding " + c + " from ProtoBuffer format failed!", e);
    }
  }

  private Message encodeMessage(Object msg) throws IOException {
    if (msg == null) {
      return null;
    }
    try {
      Class<?> c = msg.getClass();
      Class<?> protoClass = resolveProtoCounterpartClass(c, basicProtoPackageEncode);
      Builder b = null;
      try {
        b = (Builder) protoClass.getDeclaredMethod("newBuilder").invoke(null);
      } catch (Exception e) {
        throw new IOException(
            "Not able to resolve ProtoBuffer builder for " + protoClass.getName() + ".", e);
      }
      if (b != null) {
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
          field.setAccessible(true);
          Object value = field.get(msg);
          if (Modifier.isStatic(field.getModifiers()) || value == null
              || Modifier.isTransient(field.getModifiers())) {
            continue;
          }
          String fieldName = field.getName();
          FieldDescriptor protoField = b.getDescriptorForType().findFieldByName(fieldName);
          if (protoField == null) {
            throw new IOException("Field '" + fieldName + "' not found in " + protoClass);
          }
          if (Collection.class.isAssignableFrom(field.getType())) {
            Collection<?> list = (Collection<?>) value;
            for (Object val : list) {
              encodeAndSetMessageValue(b, protoField, val, true);
            }
          } else {
            encodeAndSetMessageValue(b, protoField, value, false);
          }
        }
      }
      return b.build();
    } catch (Exception e) {
      throw new IOException("encoding " + msg.getClass() + " to ProtoBuffer format failed!", e);
    }
  }

  private Enum<?> decodeEnum(EnumValueDescriptor pe, Class<?> type) throws IOException {
    if (pe == null) {
      return null;
    }
    try {
      List<?> enumCons = Arrays.asList(type.getEnumConstants());
      for (Object e : enumCons) {
        Enum<?> enm = (Enum<?>) e;
        if (pe.getName().equals(enm.name())) {
          return enm;
        }
      }
    } catch (Exception e) {
      throw new IOException("Resolving enum " + type + " from ProtoBuffer format failed!", e);
    }
    return null;
  }

  private EnumValueDescriptor encodeEnum(Enum<?> enm) throws IOException {
    if (enm == null) {
      return null;
    }
    try {
      Class<?> protoClass = resolveProtoCounterpartClass(enm.getClass(), basicProtoPackageEncode);
      if (protoClass.isEnum()) {
        List<?> protoEnumCons = Arrays.asList(protoClass.getEnumConstants());
        for (Object pe : protoEnumCons) {
          ProtocolMessageEnum penm = (ProtocolMessageEnum) pe;
          if (penm.getValueDescriptor().getName().equals(enm.name())) {
            return penm.getValueDescriptor();
          }
        }
      }
      throw new IOException(
          "Could not resolve proto counterpart for " + enm.getClass() + "." + enm);
    } catch (Exception e) {
      throw new IOException("resolving Enum " + enm.getClass() + " to ProtoBuffer format failed!",
          e);
    }
  }

  private Class<?> resolveProtoCounterpartClass(Class<?> c, String protoPackage)
      throws ClassNotFoundException {
    if (!classProtoMap.containsKey(c)) {
      ClassLoader cl = classLoader;
      if (cl == null) {
        cl = c.getClassLoader();
      }
      String className = c.getName().substring(c.getName().lastIndexOf(".") + 1);
      String protoClassName = null;
      if (CommonMsg.class.isAssignableFrom(c)) {
        protoClassName =
            PROTO_PACKAGE_SERVER_COMMON + "$" + (className.replaceAll("\\$", "Proto\\$")) + "Proto";
      } else {
        protoClassName = protoPackage + "$" + (className.replaceAll("\\$", "Proto\\$")) + "Proto";
      }
      Class<?> protoClass = cl.loadClass(protoClassName);
      classProtoMap.put(c, protoClass);
    }
    Class<?> protoClass = classProtoMap.get(c);
    return protoClass;
  }

  private void encodeAndSetMessageValue(Builder b, FieldDescriptor protoField, Object value,
      boolean list) throws IOException {
    if (protoField.getJavaType().equals(JavaType.MESSAGE)) {
      setField(b, protoField, encodeMessage(value), list);
    } else if (protoField.getJavaType().equals(JavaType.ENUM)) {
      setField(b, protoField, encodeEnum((Enum<?>) value), list);
    } else if (protoField.getJavaType().equals(JavaType.BYTE_STRING)) {
      setField(b, protoField, ByteString.readFrom(new ByteArrayInputStream((byte[]) value)), list);
    } else {
      setField(b, protoField, value, list);
    }
  }

  private void setField(Builder b, FieldDescriptor protoField, Object message, boolean list)
      throws IOException {
    try {
      if (list) {
        b.addRepeatedField(protoField, message);
      } else {
        b.setField(protoField, message);
      }
    } catch (Exception e) {
      throw new IOException(
          "Could not set " + message.getClass() + " to " + protoField.getFullName(), e);
    }
  }

}
