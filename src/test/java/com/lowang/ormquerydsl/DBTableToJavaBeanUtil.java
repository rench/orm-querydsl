package com.lowang.ormquerydsl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class DBTableToJavaBeanUtil {

  public static Connection connection;

  // mysql DB连接字符串
  private static String DB_URL =
      "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&noDatetimeStringSync=true&serverTimezone=UTC";

  private static String DB_USER = "root"; // 账号

  private static String DB_PASSWD = "root"; // 密码

  // mysql 驱动全限定名称
  public static String DB_DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

  static {
    try {
      Class.forName(DB_DRIVER_CLASS_NAME);
      if (connection == null || connection.isClosed())
        // 获得链接
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
    } catch (ClassNotFoundException ex) {
      ex.printStackTrace();
      System.out.println(ex.getMessage());
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
  }

  public DBTableToJavaBeanUtil() {}

  /**
   * 表元数据
   *
   * @param table
   * @return
   */
  public static Map<String, String> getDBTableMeta(String table) {
    Map<String, String> colAndTypes = new HashMap<>();
    String sql = "select * from " + table;
    try {
      PreparedStatement statement = connection.prepareStatement(sql);
      // 表 元数据
      ResultSetMetaData metadata = statement.getMetaData();
      // 表 列
      int len = metadata.getColumnCount();
      for (int i = 1; i <= len; i++) {
        colAndTypes.put(metadata.getColumnName(i), sqlType2JavaType(metadata.getColumnTypeName(i)));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return colAndTypes;
  }

  /**
   * sql type <=> java type
   *
   * @param sqlType
   * @return
   */
  public static String sqlType2JavaType(String sqlType) {
    switch (sqlType.toLowerCase()) {
      case "bit":
        return "boolean";
      case "tinyint":
        return "byte";
      case "smallint":
        return "short";
      case "int":
        return "int";
      case "bigint":
        return "long";
      case "float":
        return "float";
      case "decimal":
      case "numeric":
      case "real":
      case "money":
      case "smallmoney":
        return "double";
      case "varchar":
      case "char":
      case "nvarchar":
      case "nchar":
      case "text":
        return "String";
      case "datetime":
      case "date":
        return "Date";
      case "image":
        return "Blob";
      case "timestamp":
        return "Timestamp";
      default:
        return "String";
    }
  }

  /**
   * table 2 class
   *
   * @param table 表名称
   * @param path 保存类文件路径
   */
  public static String tableToClass(String table, String path, String pack) {
    Map<String, String> colAndTypes = getDBTableMeta(table);
    // 类字符串
    StringBuilder classStr = new StringBuilder();

    if (colAndTypes.size() == 0) return "";

    if (!isEmpty(pack)) {
      classStr.append("package " + pack + ";");
    }

    classStr.append(genImport(colAndTypes.values()));

    // 驼峰bean名称
    classStr.append("public class " + dealName(table, 1) + " {\r\n");

    // 类字段
    for (Map.Entry<String, String> entry : colAndTypes.entrySet()) {
      classStr.append(genFieldStr(entry.getKey(), entry.getValue()));
    }

    // get，Set
    for (Map.Entry<String, String> entry : colAndTypes.entrySet()) {
      classStr.append(genGetMethodStr(entry.getKey(), entry.getValue()));
      classStr.append(genSetMethodStr(entry.getKey(), entry.getValue()));
    }

    classStr.append("}\r\n");

    // 保存
    path = isEmpty(path) ? "" : path;
    File file = new File(path + dealName(table, 1) + ".java");
    try (BufferedWriter write = new BufferedWriter(new FileWriter(file))) {
      write.write(classStr.toString());
      write.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return classStr.toString();
  }

  public static String genImport(Collection<String> types) {
    StringBuilder sb = new StringBuilder();
    if (types.contains("Date")) {
      sb.append("import java.util.Date;\r\n");
    }
    if (types.contains("Blob")) {
      sb.append("import java.sql.Blob;\r\n");
    }
    if (types.contains("Timestamp")) {
      sb.append("import java.sql.Timestamp;\r\n");
    }
    return sb.toString();
  }

  /**
   * 属性构造
   *
   * @param name
   * @param type
   * @return
   */
  public static String genFieldStr(String name, String type) {
    if (isEmpty(name) || isEmpty(type)) {
      return "";
    }
    return String.format("    private %s %s;\n\r", type, dealName(name, 0));
  }

  public static boolean isEmpty(String str) {
    if (str == null || str == "") return true;

    return false;
  }

  /**
   * get method construct
   *
   * @param name
   * @param type
   * @return
   */
  private static String genGetMethodStr(String name, String type) {
    if (isEmpty(name) || isEmpty(type)) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("    public %s get%s(){\n\r", type, dealName(name, 1)));
    sb.append(String.format("        return this.%s;\r\n", dealName(name, 0)));
    sb.append("    }\r\n");
    return sb.toString();
  }

  /**
   * 驼峰名称处理
   *
   * @param name
   * @param type
   * @return
   */
  public static String dealName(String name, int type) {
    String[] names = StringUtils.split(StringUtils.trim(name), "_");
    if (names.length > 0) {
      StringBuilder sb = new StringBuilder();
      for (String s : names) {
        sb.append(StringUtils.upperCase(StringUtils.substring(s, 0, 1)));
        sb.append(StringUtils.lowerCase(StringUtils.substring(s, 1)));
      }
      if (type == 0) {
        sb.replace(0, 1, StringUtils.lowerCase(sb.substring(0, 1)));
      }
      return sb.toString();
    }
    return "";
  }

  /**
   * set method contruct
   *
   * @param name
   * @param type
   * @return
   */
  public static String genSetMethodStr(String name, String type) {
    if (isEmpty(name) || isEmpty(type)) {
      return "";
    }
    String fieldName = dealName(name, 0);
    StringBuilder sb = new StringBuilder();
    sb.append(
        String.format("    public void set%s(%s %s){\n\r", dealName(name, 1), type, fieldName));
    sb.append(String.format("        this.%s = %s;\r\n", fieldName, fieldName));
    sb.append("    }\r\n");
    return sb.toString();
  }

  public static void close() {
    try {
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    System.out.println(DBTableToJavaBeanUtil.tableToClass("test", "d:\\", null));
    close();
  }
}
