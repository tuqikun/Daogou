package com.tutu.daogou.util;


import com.google.gson.Gson;
import com.tutu.daogou.enums.CodeEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by ivy on 16/8/12.
 */

public class Result extends HashMap<String, Object> {
    private static final long serialVersionUID = 2203513787220720192L;
    private static final Log LOGGER = LogFactory.getLog(Result.class.getName());
    private static Gson gson = new Gson();
    public enum Result_Fields {
      success, code, note
    }

    //////////////////////////////// create//////////////////////////////////
    public static Result create() {
      return new Result();
    }

    public static Result create(String key, Object value) {
      return new Result().add(key, value);
    }

    public static Result create(String json) {
      Result jo = new Result();
      try {
          Map<String, Object> fromJson =gson.fromJson(json, Map.class);
          for (Entry<String, Object> entry : fromJson.entrySet()) {
              jo.put(entry.getKey(), entry.getValue());
          }
      } catch (Exception e) {
        LOGGER.error("Result.create 解析 JSON 失败", e);
        return Result.FAIL(CodeEnum.DEAL_RESULT_ERROR);
      }
      return jo;
    }

    /////////////////////////////////////////// SUCCESS/////////////////////////

    public static Result SUCCESS(Integer code, String note) {
      Result jo = new Result();
      jo.put(Result_Fields.success.name(), true);
      jo.put(Result_Fields.code.name(), code);
      jo.put(Result_Fields.note.name(), note);
      return jo;
    }

    public static Result SUCCESS(String json) {
      Result jo = create(json);
      jo.put(Result_Fields.success.name(), true);
      return jo;
    }

    public static Result SUCCESS(CodeEnum code) {
      return SUCCESS(code.code, code.note);
    }

    public static Result SUCCESS(CodeEnum codeEnum,String language) {
        return SUCCESS(codeEnum.code, codeEnum.note);
    }

    ///////////////////////////////////////////////// FAIL////////////////////////////
    public static Result FAIL(Integer code, String note) {
      Result jo = new Result();
      jo.put(Result_Fields.success.name(), false);
      jo.put(Result_Fields.code.name(), code);
      jo.put(Result_Fields.note.name(), note);
      return jo;
    }

    public static Result FAIL(CodeEnum codeEnum,String language) {
        return FAIL(codeEnum.code, codeEnum.note);
    }

    public static Result FAIL(CodeEnum code) {
      return FAIL(code.code, code.note);
    }

    public static Result FAIL(CodeEnum code, Exception e) {
      return FAIL(code.code, Utils.stacktrace(e));
    }

    //////////////////////////////////// GETTER SETTER///////////////////////////
    public Boolean is_success() {
      return (Boolean) this.getOrDefault(Result_Fields.success.name(), false);
    }

    public Integer get_code() {
      return (Integer) this.getOrDefault(Result_Fields.code.name(), CodeEnum.FAIL.code);
    }

    public String get_note() {
      return (String) this.getOrDefault(Result_Fields.note.name(), "");
    }

    //////////////////////// @Override/////////////////////////////////////
    @Override
    public Result put(String key, Object value) {
      super.put(key, value);
      return this;
    }

    public Result add(String key, Object value) {
      super.put(key, value);
      return this;
    }

    public String toJson() {
        Gson gson = new Gson();
      try {
        return gson.toJson(this);
      } catch (Exception e) {
        LOGGER.error("json 解析失败:", e);
        return gson.toJson(Result.FAIL(CodeEnum.DEAL_RESULT_ERROR));
      }
    }
}
