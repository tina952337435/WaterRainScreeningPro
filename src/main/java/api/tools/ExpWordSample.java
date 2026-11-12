package api.tools;
import com.alibaba.fastjson.JSON;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.policy.HackLoopTableRenderPolicy;
import com.spire.doc.FileFormat;
import freemarker.template.TemplateException;
//import sun.misc.BASE64Decoder;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExpWordSample {

    private static String templatefilepath = "D://work//UploadDoc//";

    public static void main(String[] args) throws IOException, TemplateException {

//        ColumnName param = new ColumnName();
//        param.setStrExp("{\"stime\":\"2023-07-01 08:00:00\",\"etime\":\"2023-07-31 08:00:00\",\"tm\":\"2023-07-01 08:00:00\",\"username\":\"管理员\",\"pid\":\"2023081817370935803\",\"year\":\"2023\",\"qi\":\"6\"}");
//
//        try {
//            Map<String,Object> maps = JsonToMapUtils.convertJsonToMap(param.getStrExp());
//            if(null != maps){
//                System.out.println(" username = " + JsonToMapUtils.GetValueByKey(maps,"username"));
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        String strFileName = exportDataTemplateByGXQ(null,templatefilepath,"");
//        System.out.println(" strFileName = " + strFileName);
//        Map<String, Object> map = new HashMap<>();
//        map.put("year", "2023");
//        map.put("qi", "8");
//        map.put("month", "7");
//        map.put("tm", "2023年8月1日");
//
//        //一、雨情
//        map.put("rainRes", "2023年7月，昆山市面平均月雨量为371.0mm，月雨量最大站点为高新区姜巷村委会538.1mm，最小为周庄小学153.1mm。");
//
//
//        //二、水位
//        map.put("water_63201999","太湖平均水位7月1日3.61m至7月31日3.53m，均低于警戒水位3.80m，7月23日达到最高水位3.69m。");
//        // 读取本地磁盘图片
//        map.put("water_63201999_pic", new PictureRenderData(554, 286,  FilePathUtils.getRealFilePath(templatefilepath +"TEMP//water_63201999_pic.png")));
//
//        map.put("water_63204650","阳澄区湘城水位7月1日3.27m至7月31日3.06m，均低于警戒水位3.70m，7月23日达到最高水位3.52m。");
//        // 读取本地磁盘图片
//        map.put("water_63204650_pic", new PictureRenderData(554, 286,  FilePathUtils.getRealFilePath(templatefilepath +"TEMP//water_63204650_pic.png")));
//
//        map.put("water_63204900","昆北（张家港巴城）水位7月1日3.20m至7月31日3.05m，7月20日达到最高水位3.47m。");
//        // 读取本地磁盘图片
//        map.put("water_63204900_pic", new PictureRenderData(554, 286,  FilePathUtils.getRealFilePath(templatefilepath +"TEMP//water_63204900_pic.png")));
//
//        map.put("water_63404900","昆中（吴淞江周巷）水位7月1日3.24m至7月31日3.13m，7月20日达到最高水位3.63m。");
//        // 读取本地磁盘图片
//        map.put("water_63404900_pic", new PictureRenderData(554, 286,  FilePathUtils.getRealFilePath(templatefilepath +"TEMP//water_63404900_pic.png")));
//
//        map.put("water_63403500","昆南（陈墓荡陈墓）水位7月1日3.19m至7月31日3.10m，均低于警戒水位3.60m，7月22日达到最高水位3.54m。");
//        // 读取本地磁盘图片
//        map.put("water_63403500_pic", new PictureRenderData(554, 286,  FilePathUtils.getRealFilePath(templatefilepath +"TEMP//water_63403500_pic.png")));
//
//        map.put("water_632E2472","淀山湖清水湾站水位7月1日3.17m至7月31日3.05m，均低于警戒水位3.60m，7月21日达到最高水位3.54m。");
//        // 读取本地磁盘图片
//        map.put("water_632E2472_pic", new PictureRenderData(512, 356,  FilePathUtils.getRealFilePath(templatefilepath +"TEMP//water_632E2472_pic.png")));
//
//
//        //三、主要河道流量
//        map.put("flow_63205581","道褐浦千灯镇横塘村段7月呈现往复流，道褐浦入淀山湖方向最大流量55.7m3/s，平均流量-0.03m3/s，合计7月份道褐浦千灯镇横塘村段逆流，水量7.6万m3。");
//        map.put("flow_632E2452","野尤泾7月呈现往复流，阳澄湖入傀儡湖最大流量17.3m3/s，平均流量2.29m3/s，合计7月阳澄湖经野尤泾入傀儡湖水量0.061亿m3。");
//        map.put("flow_632D2460","长牵路港7月流向基本为由西向东，最大流量81.8m3/s，平均流量13.9m3/s，7月澄湖经长牵路港入昆山水量0.372亿m3。");
//        map.put("flow_63205151","昆山（二）7月流向基本为由西向东，最大流向21.2m3/s，平均流量8.22m3/s，水量0.220亿m3。");
//
//
//
//        //四、沿江引排（供）水量
//        map.put("yjsl_63204950","杨林闸7月1日到7月31日累计排水量1.38亿m3。");
//        map.put("yjsl_63204850","七浦闸7月1日到7月31日累计引水量0.034亿m3，排水0.367亿m3。");
//        map.put("yjsl_63205050","浏河闸7月1日到7月31日累计排水量3.41亿m3。");
//
//        // 通过url读取网络图片
////        map.put("picture", new PictureRenderData(200, 400, ".png", BytePictureUtils.getUrlByteArray("https://res.wx.qq.com/a/wx_fed/weixin_portal/res/static/img/1EtCRvm.png")));
//
//        String strFilePathTemplate = FilePathUtils.getRealFilePath(templatefilepath +"MB//KS_XQJB.docx");
//        File file = new File(strFilePathTemplate);
//        XWPFTemplate template = XWPFTemplate.compile(file).render(map);
//
//        //本地生成excel临时文件
//        String outFilePath = FilePathUtils.getRealFilePath(templatefilepath +""+UUID.randomUUID() + ".docx");
//        FileOutputStream out = null;
//        try {
//            out = new FileOutputStream(new File(outFilePath));
//            template.write(out);
//            out.flush();
//            out.close();
//            template.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        // 数据及模板中，不能单独出现 % 号，需要今天替换为  %25 进行显示
//        // 所有涉及到function 格式化或计算的方法都无效
//        String[] categories = new String[] { "太湖平均", "湘城", "巴城" };
//        for(int n =0;n < 3;n++){
//
//            //图表参数
//            HashMap<String, Object> datas = new HashMap<>();
//            datas.put("title", categories[n]);
//            datas.put("categories", JSON.toJSONString(categories));
//            // 生成option字符串
//            String option = FreemarkerUtil.generateString("option.ftl", "MB\\template", datas);
//
//            // 根据option参数
//            String base64 = EchartsUtil.generateEchartsBase64(option);
//            if("" != base64.trim()){
//                generateImage(base64, "D:/work/UploadDoc/temp/echarts001"+n+".png");
//            }
////            System.out.println("BASE64:" + base64);
//        }

        String pathname="xqjb";

        String templateFileName = "[202308]_昆山市高新区梅雨水情分析.docx";
        String outFilePath = FilePathUtils.getRealFilePath(templatefilepath + pathname +"\\"+templateFileName);

        //word转换 pdf
        String inputFilePath = outFilePath;
        String outputFileName = templateFileName;
        String outputFilePath = FilePathUtils.getRealFilePath(templatefilepath + pathname +"\\"+outputFileName.replace(".docx",".pdf"));
        System.out.println(inputFilePath);
        System.out.println(outputFilePath);
        wordToPdf(inputFilePath,outputFilePath);
    }

    public static String wordTemplateExport(Map<String, Object> map,String templateFilePath,String pathname){

        String strFilePathTemplate = FilePathUtils.getRealFilePath(templateFilePath +"MB//KS_XQJB.docx");
        File file = new File(strFilePathTemplate);
        XWPFTemplate template = XWPFTemplate.compile(file).render(map);

        String templateFileName = UUID.randomUUID() + ".docx";
        //本地生成excel临时文件
        String outFilePath = FilePathUtils.getRealFilePath(templateFilePath + pathname +"/"+templateFileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(outFilePath));
            template.write(out);
            out.flush();
            out.close();
            template.close();
            return templateFileName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *
     *  生成水位过程线 图片
     *  datas echart    参数，与模板对应
     *  templaterFileName   ftl 模板名称  option.ftl
     *  templateFilePath    ftl 模板路径 "MB\\template"
     *  strFileName     生成图片名称
     *  strFilePath     生成图片路径
     *
     * */
    public static String DrawPicByWater(HashMap<String, Object> datas,String templaterFileName,String templateFilePath,String strFileName,String strFilePath) {

//        //图表参数
//        HashMap<String, Object> datas = new HashMap<>();
//        datas.put("title", title);
//        datas.put("categories", JSON.toJSONString(categories));

//        // 生成option字符串
//        String option = FreemarkerUtil.generateString(templaterFileName, templateFilePath, datas);
//
//        // 根据option参数
//        String base64 = EchartsUtil.generateEchartsBase64(option);
//        if("" != base64.trim()){
//            generateImage(base64, strFileName+strFilePath);//"D:/work/UploadDoc/temp/echarts001.png"
//        }

        try {
            // 生成option字符串
            String option = FreemarkerUtil.generateString(templaterFileName, templateFilePath, datas);

            // 根据option参数，
            String base64 = EchartsUtil.generateEchartsBase64(option);

            if("" != base64.trim()){
                generateImage(base64, strFilePath+strFileName);//"D:/work/UploadDoc/temp/echarts001.png"
                return "200";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void generateImage(String base64, String path) throws IOException {
//        BASE64Decoder decoder = new BASE64Decoder();
//        try (OutputStream out = new FileOutputStream(path)){
//            // 解密
//            byte[] b = decoder.decodeBuffer(base64);
//            for (int i = 0; i < b.length; ++i) {
//                if (b[i] < 0) {
//                    b[i] += 256;
//                }
//            }
//            out.write(b);
//            out.flush();
//        }
    }


    public static String exportDataTemplateByGXQ(Map<String, Object> map,String templateFilePath,String pathname,String filename,String templateFileNameIn) {

        //word模板文件路径地址
        String resource = FilePathUtils.getRealFilePath(templateFilePath +"MB//" + (templateFileNameIn != "" ? templateFileNameIn : "GXQ_XQJB.docx"));

        String templateFileName = "" != filename ? filename :  UUID.randomUUID() + ".docx";
        String outFilePath = FilePathUtils.getRealFilePath(templateFilePath + pathname +"/"+templateFileName);

        try {
            //渲染表格  动态行
            HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
            Configure config = Configure.newBuilder()
                    .bind("rainList", policy).bind("waterList", policy).build();

            XWPFTemplate template = XWPFTemplate.compile(resource, config).render(
//                    new HashMap<String, Object>() {{
//                        put("rainList", rainList);
//                        put("waterList", waterList);
//                        put("year", "2023");
//                        put("qi", "01");
//                        put("tm", "2023年6月18日");
//                        put("stm", "6月17日-18日");
//                        put("rainRes","昆山市于6月17日入梅，6月17日8时至6月18日11时，昆山高新区普降中到大雨，面平均雨量29.9mm，最大点雨量为培本小学西校区站，降水量为33.8mm。");
//                        put("waterRes", "受降雨和浏河排水综合影响，昆山市高新区河湖水位基本保持平稳，区内各水位站水位均低于警戒水位3.43m。");
//                    }}
                    map
            );
            //=================生成文件保存在本地D盘某目录下=================
            FileOutputStream fos = new FileOutputStream(outFilePath);
            template.write(fos);
            fos.flush();
            fos.close();
            template.close();

            //word转换 pdf
            String inputFilePath = outFilePath;
            String outputFileName = templateFileName;
            String outputFilePath = FilePathUtils.getRealFilePath(templateFilePath + pathname +"/"+outputFileName.replace(".docx",".pdf"));
            wordToPdf(inputFilePath,outputFilePath);

            return templateFileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     *
     * map 模版参数
     * templateFilePath 文件路径
     * pathname 类型名称
     * filename 生成的文件名称
     * templateFileNameIn 模版文件名称
     *
     * */
    public static String exportTemplateToWordByCity(Map<String, Object> map,String templateFilePath,String pathname,String filename,String templateFileNameIn) {

        //word模板文件路径地址
        String resource = FilePathUtils.getRealFilePath(templateFilePath +"MB//"+templateFileNameIn);//GXQ_XQJB.docx

        String templateFileName = "" != filename ? filename :  UUID.randomUUID() + ".docx";
        String outFilePath = FilePathUtils.getRealFilePath(templateFilePath + pathname +"/"+templateFileName);

        try {
            //渲染表格  动态行
            HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
            Configure config = Configure.newBuilder()
                    .bind("rainList", policy).bind("waterList", policy).bind("flowList", policy).build();

            XWPFTemplate template = XWPFTemplate.compile(resource, config).render(
//                    new HashMap<String, Object>() {{
//                        put("rainList", rainList);
//                        put("waterList", waterList);
//                        put("year", "2023");
//                        put("qi", "01");
//                        put("tm", "2023年6月18日");
//                        put("stm", "6月17日-18日");
//                        put("rainRes","昆山市于6月17日入梅，6月17日8时至6月18日11时，昆山高新区普降中到大雨，面平均雨量29.9mm，最大点雨量为培本小学西校区站，降水量为33.8mm。");
//                        put("waterRes", "受降雨和浏河排水综合影响，昆山市高新区河湖水位基本保持平稳，区内各水位站水位均低于警戒水位3.43m。");
//                    }}
                    map
            );
            //=================生成文件保存在本地D盘某目录下=================
            FileOutputStream fos = new FileOutputStream(outFilePath);
            template.write(fos);
            fos.flush();
            fos.close();
            template.close();

            //word转换 pdf
            String inputFilePath = outFilePath;
            String outputFileName = templateFileName;
            String outputFilePath = FilePathUtils.getRealFilePath(templateFilePath + pathname +"/"+outputFileName.replace(".docx",".pdf"));
            wordToPdf(inputFilePath,outputFilePath);

            return templateFileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     *  闸门开关简报
     * map 模版参数
     * templateFilePath 文件路径
     * pathname 类型名称
     * filename 生成的文件名称
     * templateFileNameIn 模版文件名称
     *
     * */
    public static String exportTemplateToWordByGate(Map<String, Object> map,String templateFilePath,String pathname,String filename,String templateFileNameIn) {

        //word模板文件路径地址
        String resource = FilePathUtils.getRealFilePath(templateFilePath +"MB//"+templateFileNameIn);//GATE_KGZJB.docx

        String templateFileName = "" != filename ? filename :  UUID.randomUUID() + ".docx";
        String outFilePath = FilePathUtils.getRealFilePath(templateFilePath + pathname +"/"+templateFileName);

        try {
            //渲染表格  动态行
            HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
            Configure config = Configure.newBuilder()
                    .bind("yzbList", policy).bind("dphList", policy).build();

            XWPFTemplate template = XWPFTemplate.compile(resource, config).render(
//                    new HashMap<String, Object>() {{
//                        put("rainList", rainList);
//                        put("waterList", waterList);
//                        put("year", "2023");
//                        put("qi", "01");
//                        put("tm", "2023年6月18日");
//                        put("stm", "6月17日-18日");
//                        put("rainRes","昆山市于6月17日入梅，6月17日8时至6月18日11时，昆山高新区普降中到大雨，面平均雨量29.9mm，最大点雨量为培本小学西校区站，降水量为33.8mm。");
//                        put("waterRes", "受降雨和浏河排水综合影响，昆山市高新区河湖水位基本保持平稳，区内各水位站水位均低于警戒水位3.43m。");
//                    }}
                    map
            );
            //=================生成文件保存在本地D盘某目录下=================
            FileOutputStream fos = new FileOutputStream(outFilePath);
            template.write(fos);
            fos.flush();
            fos.close();
            template.close();

            //word转换 pdf
            String inputFilePath = outFilePath;
            String outputFileName = templateFileName;
            String outputFilePath = FilePathUtils.getRealFilePath(templateFilePath + pathname +"/"+outputFileName.replace(".docx",".pdf"));
            wordToPdf(inputFilePath,outputFilePath);

            return templateFileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *  闸门开关简报
     * map 模版参数
     * templateFilePath 文件路径
     * pathname 类型名称
     * filename 生成的文件名称
     * templateFileNameIn 模版文件名称
     *
     * */
    public static String exportTemplateToWordBySL(Map<String, Object> map,String templateFilePath,String pathname,String filename,String templateFileNameIn) {

        //word模板文件路径地址
        String resource = FilePathUtils.getRealFilePath(templateFilePath +"MB//"+templateFileNameIn);//GATE_KGZJB.docx

        String templateFileName = "" != filename ? filename :  UUID.randomUUID() + ".docx";
        String outFilePath = FilePathUtils.getRealFilePath(templateFilePath + pathname +"/"+templateFileName);

        try {
            //渲染表格  动态行
            HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
            Configure config = Configure.newBuilder().build(); //.bind("slList", policy)

            XWPFTemplate template = XWPFTemplate.compile(resource, config).render(
//                    new HashMap<String, Object>() {{
//                        put("rainList", rainList);
//                        put("waterList", waterList);
//                        put("year", "2023");
//                        put("qi", "01");
//                        put("tm", "2023年6月18日");
//                        put("stm", "6月17日-18日");
//                        put("rainRes","昆山市于6月17日入梅，6月17日8时至6月18日11时，昆山高新区普降中到大雨，面平均雨量29.9mm，最大点雨量为培本小学西校区站，降水量为33.8mm。");
//                        put("waterRes", "受降雨和浏河排水综合影响，昆山市高新区河湖水位基本保持平稳，区内各水位站水位均低于警戒水位3.43m。");
//                    }}
                    map
            );
            //=================生成文件保存在本地D盘某目录下=================
            FileOutputStream fos = new FileOutputStream(outFilePath);
            template.write(fos);
            fos.flush();
            fos.close();
            template.close();

            //word转换 pdf
            String inputFilePath = outFilePath;
            String outputFileName = templateFileName;
            String outputFilePath = FilePathUtils.getRealFilePath(templateFilePath + pathname +"/"+outputFileName.replace(".docx",".pdf"));

            PdfUtil.doc2pdf(inputFilePath,outputFilePath);
//            wordToPdf(inputFilePath,outputFilePath);

            return templateFileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *  昆山市防汛快报
     * map 模版参数
     * templateFilePath 文件路径
     * pathname 类型名称
     * filename 生成的文件名称
     * templateFileNameIn 模版文件名称
     *
     * */
    public static String exportTemplateToWordByKsfxkb(Map<String, Object> map,String templateFilePath,String pathname,String filename,String templateFileNameIn) {

        //word模板文件路径地址
        String resource = FilePathUtils.getRealFilePath(templateFilePath +"MB//"+templateFileNameIn);//GATE_KGZJB.docx

        String templateFileName = "" != filename ? filename :  UUID.randomUUID() + ".docx";
        String outFilePath = FilePathUtils.getRealFilePath(templateFilePath + pathname +"/"+templateFileName);

        try {
            //渲染表格  动态行
            HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
            Configure config = Configure.newBuilder().bind("rainList", policy).bind("gateList", policy).bind("czList", policy).build();


            XWPFTemplate template = XWPFTemplate.compile(resource, config).render(
//                    new HashMap<String, Object>() {{
//                        put("rainList", rainList);
//                        put("waterList", waterList);
//                        put("year", "2023");
//                        put("qi", "01");
//                        put("tm", "2023年6月18日");
//                        put("stm", "6月17日-18日");
//                        put("rainRes","昆山市于6月17日入梅，6月17日8时至6月18日11时，昆山高新区普降中到大雨，面平均雨量29.9mm，最大点雨量为培本小学西校区站，降水量为33.8mm。");
//                        put("waterRes", "受降雨和浏河排水综合影响，昆山市高新区河湖水位基本保持平稳，区内各水位站水位均低于警戒水位3.43m。");
//                    }}
                    map
            );
            //=================生成文件保存在本地D盘某目录下=================
            FileOutputStream fos = new FileOutputStream(outFilePath);
            template.write(fos);
            fos.flush();
            fos.close();
            template.close();

            //word转换 pdf
            String inputFilePath = outFilePath;
            String outputFileName = templateFileName;
            String outputFilePath = FilePathUtils.getRealFilePath(templateFilePath + pathname +"/"+outputFileName.replace(".docx",".pdf"));

            PdfUtil.doc2pdf(inputFilePath,outputFilePath);
//            wordToPdf(inputFilePath,outputFilePath);

            return templateFileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     *  word 转 pdf
     *  inputFilePath   word文件路径
     *  outputFilePath  pdf存储文件路径
     * */
    public static void wordToPdf(String inputFilePath,String outputFilePath){
        // 生成pdf文件
//        XWPFDocument xwpfDocument  = null;
        try {
            com.spire.doc.Document doc = new com.spire.doc.Document();
            doc.loadFromFile(inputFilePath);
            doc.saveToFile(outputFilePath, FileFormat.PDF);


            System.out.println(" pdf转换成功! ");
//            // 指定WORD文件
//            InputStream inputStream = new FileInputStream(inputFilePath);
//            xwpfDocument = new XWPFDocument(inputStream);
//
//            // 指定PDF文件
//            OutputStream outputStream = new FileOutputStream(outputFilePath);
//            PdfOptions options = PdfOptions.create();
//
//            // 转换
//            PdfConverter.getInstance().convert(xwpfDocument, outputStream, options);
//
//            // 关闭流
//            outputStream.close();
//            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" pdf转换失败！！！ "+e.getMessage());
        }

    }
}
