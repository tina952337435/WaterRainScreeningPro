package api.tools;


import api.pojo.*;
import api.pojo.swzzzjk.TSDBPojo;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.*;
import java.util.*;

@Component
@SuppressWarnings("unchecked")
public class CommonUtills {
    //随机id --start
    private static final FieldPosition HELPER_POSITION = new FieldPosition(0);

    /**
     * This Format for format the api.data to special format.
     */
    private final static Format dateFormat = new SimpleDateFormat("MMddHHmmssS");

    /**
     * This Format for format the number to special format.
     */
    private final static NumberFormat numberFormat = new DecimalFormat("0000");

    /**
     * This int is the sequence number ,the default value is 0.
     */
    private static int seq = 0;

    private static final int MAX = 9999;
    //随机id --end
    private final String SuccessContent = "操作成功!";

    private final String FailContent = "操作失败!";

    /**
     * 查询是否有数据
     *
     * @param list 传入集合
     * @return 返回成功失败
     */
    public String returnSelect(List list) {
        if (list.size() > 0) {
            return "true";
        }
        return "false";

    }

    /**
     * @param s 传入int值
     * @return 返回成功失败
     */
    public String returnInOut(int s) {
        if (s > 0) {
            return "true";
        }
        return "false";

    }

    /**
     *字符串转Map
     */
    public static List<ColumnParam> getStringToMap(String str){
        if(null==str||"".equals(str)){
            return null;
        }
        String [] strings=str.split(",");
        int mapLength=strings.length;
        List<ColumnParam> map=new ArrayList<>();
        //循环加入map集合
       for(int num=0;num<strings.length;num++){
           ColumnParam param=new ColumnParam();
            String strVal=strings[num];
           param.setName(strVal);
            param.setValue(String.valueOf(num));
            map.add(param);
       }
       return map;
    }
    /**
     * 返回封装
     *
     * @param type      查询请传入select  删除delete 更新update
     * @param resultInt 更新和删除传入数据库Int返回值 大于0 自动返回true
     * @param obj       查询时间返回查询结果给前台
     * @return
     */
    public Map<String, Object> returnJson(String type, int resultInt, Object obj) {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        //创建true或者false
        String resultType = "";
        //如果type不是s根据操作返回的数据判断是否成功
        if (resultInt > 0) {
            resultType = "true";
        } else {
            resultType = "false";
        }

        //查询返回数据
        if (type.equals("select")) {
            objectObjectHashMap.put("success", resultType);
            objectObjectHashMap.put("result", obj);
            objectObjectHashMap.put("total", resultInt);
            return objectObjectHashMap;
        } else {
            //更新和删除返回状态
            objectObjectHashMap.put("success", resultType);
            objectObjectHashMap.put("content", SuccessContent);
            objectObjectHashMap.put("total", resultInt);
            return objectObjectHashMap;
        }
    }


    /**
     * 返回封装
     *
     * @param type      查询请传入select  删除delete 更新update
     * @param resultInt 更新和删除传入数据库Int返回值 大于0 自动返回true
     * @param obj       查询时间返回查询结果给前台
     * @return
     */
    public Map<String, Object> returnJsonChart(String type, int resultInt, Object obj, Object echart) {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        //创建true或者false
        String resultType = "";
        //如果type不是s根据操作返回的数据判断是否成功
        if (resultInt > 0) {
            resultType = "true";
        } else {
            resultType = "false";
        }

        //查询返回数据
        if (type.equals("select")) {
            objectObjectHashMap.put("success", resultType);
            objectObjectHashMap.put("api/data", obj);
            objectObjectHashMap.put("total", getObjectSize(obj));
            objectObjectHashMap.put("chart", echart);

            return objectObjectHashMap;
        } else {
            //更新和删除返回状态
            objectObjectHashMap.put("success", resultType);
            objectObjectHashMap.put("content", SuccessContent);
            return objectObjectHashMap;
        }
    }


    /**
     * 返回封装 适应地图字段
     *
     * @param type      查询请传入select  删除delete 更新update
     * @param resultInt 更新和删除传入数据库Int返回值 大于0 自动返回true
     * @param obj       查询时间返回查询结果给前台
     * @return
     */
    public Map<String, Object> returnJsonDt(String type, int resultInt, Object obj) {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        //创建true或者false
        String resultType = "";
        String status = "";
        //如果type不是s根据操作返回的数据判断是否成功
        if (resultInt > 0) {
            resultType = "true";
            status = "200";
        } else {
            resultType = "false";
        }

        //查询返回数据
        if (type.equals("select")) {
            objectObjectHashMap.put("status", status);
            objectObjectHashMap.put("success", resultType);
            objectObjectHashMap.put("rows", obj);
            objectObjectHashMap.put("total", getObjectSize(obj));
            return objectObjectHashMap;
        } else {
            //更新和删除返回状态
            objectObjectHashMap.put("success", resultType);
            objectObjectHashMap.put("content", SuccessContent);
            return objectObjectHashMap;
        }
    }



    /**
     * 返回封装
     *
     * @param type      查询请传入select  删除delete 更新update
     * @param resultInt 更新和删除传入数据库Int返回值 大于0 自动返回true
     * @param obj       查询时间返回查询结果给前台
     * @return
     */
    public Map<String, Object> returnJsonPage(String type, int resultInt, Object obj,long pageSize,long pageIndex) {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        //创建true或者false
        String resultType = "";
        //如果type不是s根据操作返回的数据判断是否成功
        if (resultInt > 0) {
            resultType = "true";
        } else {
            resultType = "false";
        }

        //查询返回数据
        if (type.equals("select")) {
            objectObjectHashMap.put("success", resultType);
            objectObjectHashMap.put("result", obj);
            objectObjectHashMap.put("total", resultInt);
            objectObjectHashMap.put("pageSize", pageSize);
            objectObjectHashMap.put("pageIndex", pageIndex);
            return objectObjectHashMap;
        } else {
            //更新和删除返回状态
            objectObjectHashMap.put("success", resultType);
            objectObjectHashMap.put("content", SuccessContent);
            objectObjectHashMap.put("total", resultInt);
            return objectObjectHashMap;
        }
    }


    /**
     * 根据object实际参数获取长度
     */
    public String getObjectSize(Object obj) {
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            return String.valueOf(length);
        }
        if (obj instanceof List) {
            return String.valueOf(((List) obj).size());
        }
        if (obj instanceof Set) {
            return String.valueOf(((Set) obj).size());
        }
        if (obj instanceof Map) {
            return String.valueOf(((Map) obj).size());
        }
        return "未标明的数据格式";
    }


    /**
     * 时间格式生成序列
     *
     * @return String
     */
    public static String getUUid() {

        Calendar rightNow = Calendar.getInstance();

        StringBuffer sb = new StringBuffer();

        dateFormat.format(rightNow.getTime(), sb, HELPER_POSITION);

        numberFormat.format(seq, sb, HELPER_POSITION);

        if (seq == MAX) {
            seq = 0;
        } else {
            seq++;
        }
        return sb.toString();
    }
    public static String get20UUID(){
         UUID id=UUID.randomUUID();
         String[] idd=id.toString().split("-");
         return idd[0]+idd[1]+idd[2]+idd[3];
    }

    /**
     * 调用http接口获取数据
     *
     * @param path
     * @return
     */

    public static String getHttpInterface(String path) {
        BufferedReader in = null;
        StringBuffer result = null;
        try {
            URL url = new URL(path);
            //打开和url之间的连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "utf-8");
            connection.connect();

            result = new StringBuffer();
            //读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 反射清空实体类
     *
     * @param obj
     */
    @SuppressWarnings("unchecked")
    public static void setObjectFieldsEmpty(Object obj) {
        // 对obj反射
        Class objClass = obj.getClass();
        Method[] objmethods = objClass.getDeclaredMethods();
        Map objMeMap = new HashMap();
        for (int i = 0; i < objmethods.length; i++) {
            Method method = objmethods[i];
            objMeMap.put(method.getName(), method);
        }
        for (int i = 0; i < objmethods.length; i++) {
            {
                String methodName = objmethods[i].getName();
                if (methodName != null && methodName.startsWith("get")) {
                    try {
                        Object returnObj = objmethods[i].invoke(obj,
                                new Object[0]);
                        Method setmethod = (Method) objMeMap.get("set"
                                + methodName.split("get")[1]);
                        if (returnObj != null) {
                            returnObj = null;
                        }
                        setmethod.invoke(obj, returnObj);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     *
     * @param beginDate
     * @param endDate
     * @return List
     */
    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<Date>();
        lDate.add(beginDate);// 把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.HOUR_OF_DAY, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        lDate.add(endDate);// 把结束时间加入集合
        return lDate;
    }


    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     *
     * @param beginDate
     * @param endDate
     * @return List
     */
    public static List<Date> getDayCalendar(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<Date>();
        lDate.add(beginDate);// 把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {

                break;
            }
        }
        lDate.add(endDate);// 把结束时间加入集合
        cal.setTime(endDate);  //结束时间加入集合之后再多加入一天
        cal.add(Calendar.DAY_OF_MONTH, 1);
        lDate.add(cal.getTime());

        return lDate;
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     *
     * @param beginDate
     * @param endDate
     * @return List
     */
    public static List<Date> getDayCalendarSz(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<Date>();
        lDate.add(beginDate);// 把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {

                break;
            }
        }
       // lDate.add(endDate);// 把结束时间加入集合

        return lDate;
    }


    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     *
     * @param beginDate
     * @param endDate
     * @return List
     */
    public static List<Date> getDaySz(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<Date>();
        lDate.add(beginDate);// 把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {

                break;
            }
        }
        lDate.add(endDate);// 把结束时间加入集合
        return lDate;
    }










    /**
     * 获取一段时间内的小时时间序列
     *
     * @param start
     * @param end
     * @return
     * @throws ParseException
     */
    public List<String> getDataList(String start, String end) throws ParseException {
        ArrayList<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dBegin = sdf.parse(start);
        Date dEnd = sdf.parse(end);
        List<Date> listDate = getDatesBetweenTwoDate(dBegin, dEnd);
        for (int i = 0; i < listDate.size(); i++) {
            result.add(sdf.format(listDate.get(i)));
        }

        return result;
    }

    /**
     * 获取一段时间内的天时间序列
     *
     * @param start
     * @param end
     * @return
     * @throws
     */
    public List<String> getDayList(String start, String end) throws ParseException {
        ArrayList<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dBegin = sdf.parse(start);
        Date dEnd = sdf.parse(end);
        List<Date> listDate = getDayCalendar(dBegin, dEnd);
        for (int i = 0; i < listDate.size(); i++) {
            result.add(sdf.format(listDate.get(i)));
        }

        return result;
    }


    /**
     * 多站水质获取一段时间内的天时间序列
     *
     * @param start
     * @param end
     * @return
     * @throws
     */
    public List<String> getDayListSz(String start, String end) throws ParseException {
        ArrayList<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dBegin = sdf.parse(start);
        Date dEnd = sdf.parse(end);
        List<Date> listDate = getDayCalendarSz(dBegin, dEnd);
        for (int i = 0; i < listDate.size(); i++) {
            result.add(sdf.format(listDate.get(i)));
        }

        return result;
    }





    /**
     * 站码排列成in sql 可使用格式
     *
     * @return
     */
    public String getStcds(String stcds) {
        String stcd = "";
        String[] split = stcds.split(",");
        for (int i = 0; i < split.length; i++) {
            if (i == split.length - 1) {
                stcd += "'" + split[i] + "'";
            } else {

                stcd += "'" + split[i] + "',";
            }
        }

        return stcd;
    }

    /**
     * 判断文件格式
     *
     * @param inStr
     * @param fileName
     * @return
     * @throws Exception
     */
    public static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook workbook = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));

        if (".xls".equals(fileType)) {
            workbook = new HSSFWorkbook(inStr);
        } else if (".xlsx".equals(fileType)) {
            workbook = new XSSFWorkbook(inStr);
        } else {
            throw new Exception("请上传excel文件！");
        }
        return workbook;
    }



    // 调用http接口获取数据
    public  String getHttpPost(String path) {
        BufferedReader in = null;
        StringBuffer result = null;
        try {
            URL url = new URL(path);
            //打开和url之间的连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("accessToken", "szy");
            connection.setRequestProperty("Charset", "utf-8");
            connection.connect();

            result = new StringBuffer();
            //读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }







    /**
     * 对象非空判断 空为true
     *
     * @param
     * @return
     * @author 刘彬
     * @date
     */
    public static boolean isEmpty(Object obj) {
        if (obj instanceof String) {
            obj = ((String) obj).trim();
        }
        return ObjectUtils.isEmpty(obj);
    }

    /**
     * 集合非空判断
     *
     * @param
     * @return
     * @author 刘彬
     * @date 2018年8月8日
     */
    public static boolean collectionIsNull(Object collection) {
        boolean isNull = false;
        if (isEmpty(collection)) {
            isNull = true;
            return isNull;
        }
        if (collection instanceof Map) {
            Map map = (Map) collection;
            isNull = map.isEmpty();
        } else if (collection instanceof List) {
            List list = (List) collection;
            isNull = list.isEmpty();
        } else if (collection instanceof String[]) {
            String[] list = (String[]) collection;
            if (list != null || list.length > 0) {
                isNull = false;
            }
        } else if (collection instanceof Set) {
            Set set = (Set) collection;
            isNull = set.isEmpty();
        }
        return isNull;
    }

    public static String get_zhangluo(String theString)
    {
        String s_WPTN = "";
        if (theString.trim().equals("4"))
        {
            s_WPTN = "落";
        }
        else if (theString.trim().equals("5"))
        {
            s_WPTN = "涨";
        }
        else if (theString.trim().equals("6"))
        {
            s_WPTN = "平";
        }
        return s_WPTN;
    }


    public static String get_zhashui(String theString)
    {
        String s_WPTN = "";
        if (theString.trim().equals("1"))
        {
            s_WPTN = "干涸";
        }
        else if (theString.trim().equals("2"))
        {
            s_WPTN = "断流";
        }
        else if (theString.trim().equals("3"))
        {
            s_WPTN = "流向不定";
        }
        else if (theString.trim().equals("4"))
        {
            s_WPTN = "逆流";
        }
        else if (theString.trim().equals("5"))
        {
            s_WPTN = "起涨";
        }
        else if (theString.trim().equals("6"))
        {
            s_WPTN = "洪峰";
        }
        else if (theString.trim().equals("P"))
        {
            s_WPTN = "发电流量";
        }

        return s_WPTN;
    }



    // 通过反射获取字段值
    public  Double getFieldValue(TSDBPojo tsdb, String fieldName) {
        try {
            java.lang.reflect.Field field = TSDBPojo.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (Double) field.get(tsdb);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}