package com.call.yhck;

/**
 * Created by YH_CK on 2017/07/12.
 */

public class CallInfo {
  private String number; // 号码
  private String date;     // 日期
  private int type;      // 类型：来电、去电、未接
  private  String name; //联系人姓名
  private String durtion;//通话时长

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDurtion() {
    return durtion;
  }

  public void setDurtion(String durtion) {
    this.durtion = durtion;
  }

  @Override
  public String toString() {
    return "CallInfo{" +
      "number='" + number + '\'' +
      ", date=" + date +
      ", type=" + type +
      ", name=" + name +
      ", durtion=" + durtion +
      '}';
  }
}
