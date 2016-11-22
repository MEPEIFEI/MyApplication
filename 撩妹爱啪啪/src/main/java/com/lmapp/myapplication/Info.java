package com.lmapp.myapplication;

import java.io.Serializable;

/**
 *
 * Created by Administrator on 2016/6/25 0025.
 */
public class Info implements Serializable {
    public String[] infoString = new String[38];
    public String _id;
    public String student_num;    //"学号"
    public String name ;          // '姓名',
    public String used_name;     // '曾用名',
    public String sex;            // '性别',
    public String academy;       // '学院',
    public String major;//'专业名称',
    public String xingzhenban;//'行政班',
    public String short_num;//'短号',
    public String long_num;//'长号',
    public String grade;//'年级',
    public String dormitory;//'宿舍楼',
    public String dormitory_num;//'宿舍号',
    public String bed_num;//'床号',
    public String birthday_num;//'出生日期',
    public String state;//'政治面貌',
    public String nation;//'民族',
    public String place;// '籍贯',
    public String residence;//'户口所在地',
    public String student_type;//'学生类别',
    public String education;//'学制',
    public String student_period;//'学习年限',
    public String student_status;//'学籍状态',
    public String major_direct;//'专业方向',
    public String enter_school_date;//'入学日期',
    public String middle_school; //'毕业中学',
    public String phone;//'联系电话',
    public String examinee_number;//'准考证号',
    public String id_number;//'身份证号',
    public String major_number;//'专业代码',
    public String level;//'层次',
    public String examinee_type;//'考生类别',
    public String examinee_num;// '考生号',
    public String home_place;// '家庭所在地',
    public String province;//'来源省',
    public String deposit;// '托管学院',
    public String native_num;// '国家统编专业代码',
    public String picUrl;//头像url
}
