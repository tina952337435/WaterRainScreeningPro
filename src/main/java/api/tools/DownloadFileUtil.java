package api.tools;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.security.cert.X509Certificate;
/**
 * 下载文件工具类
 * @author lindr
 *
 */
@Slf4j
public class DownloadFileUtil {

    /**
     * 普通下载
     * @param path
     * @param response
     */
    public static  void download(String path, HttpServletResponse response) {
        try  {
            //  path是指欲下载的文件的路径。
            File file  =   new  File(path);
            //  取得文件名。
            String filename  =  file.getName();
            //  取得文件的后缀名。
            String ext  =  filename.substring(filename.lastIndexOf(".")+1).toUpperCase();

            //  以流的形式下载文件。
            InputStream fis  =   new  BufferedInputStream(new  FileInputStream(path));
            byte [] buffer  =   new  byte[fis.available()];
            fis.read(buffer);
            fis.close();
            //  清空response
            response.reset();
            //  设置response的Header
            response.addHeader("Content-Disposition" ,"attachment;filename="+ URLEncoder.encode(filename,"UTF-8"));
            response.addHeader("Content-Length" ,""   +  file.length());
            OutputStream toClient  =   new  BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        }  catch  (IOException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 下载本地文件
     * @param path
     * @param response
     * @throws FileNotFoundException
     */
    public static  void  downloadLocal(String path,HttpServletResponse response)  throws  FileNotFoundException {
        path = path.replace("%20", " ");
        //文件名称
        String fileName = path.split("/")[path.split("/").length - 1];
        //读到流中
        InputStream inStream  =   new  FileInputStream(path); //  文件的存放路径
        //  清空response
        response.reset();
//        response.setContentType("bin");
        try  {
            response.addHeader("Content-Disposition","attachment;filename="+URLEncoder.encode(fileName,"UTF-8"));
            //  循环取出流中的数据
            byte [] b  =   new   byte [ 100 ];
            int  len;
            while  ((len  =  inStream.read(b))  >   0 ) {
                response.getOutputStream().write(b,  0 , len);
            }
            inStream.close();
        }  catch  (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void  downloadNet(HttpServletResponse response)  throws  MalformedURLException {
        //  下载网络文件
        int  bytesum  =   0 ;
        int  byteread  =   0 ;

        URL url  =   new  URL( " windine.blogdriver.com/logo.gif " );

        try  {
            URLConnection conn  =  url.openConnection();
            InputStream inStream  =  conn.getInputStream();
            FileOutputStream fs  =   new  FileOutputStream( " c:/abc.gif " );

            byte [] buffer  =   new   byte [ 1204 ];
            int  length;
            while  ((byteread  =  inStream.read(buffer))  !=   - 1 ) {
                bytesum  +=  byteread;
                System.out.println(bytesum);
                fs.write(buffer,  0 , byteread);
            }
        }  catch  (FileNotFoundException e) {
            e.printStackTrace();
        }  catch  (IOException e) {
            e.printStackTrace();
        }
    }


    //支持在线打开文件的一种方式
    public  static void  downLoad(String filePath, HttpServletResponse response,  boolean  isOnLine)  throws  Exception {
        File f  =   new  File(filePath);
        if  ( ! f.exists()) {
            response.sendError( 404 ,  " File not found! " );
            return ;
        }
        BufferedInputStream br  =   new  BufferedInputStream( new  FileInputStream(f));
        byte [] buf  =   new   byte [ 1024 ];
        int  len  =   0 ;
        response.reset();  //  非常重要
        if  (isOnLine) {  //  在线打开方式
            URL u  =   new  URL( "file:///"   +  filePath);
            response.setContentType(u.openConnection().getContentType());
            response.setHeader( "Content-Disposition" ,  "inline;filename="   +  f.getName());
            //  文件名应该编码成UTF-8
        } else {  //  纯下载方式
            response.setContentType( "application/x-msdownload" );
            response.setHeader( "Content-Disposition" ,  "attachment;filename="   +  f.getName());
        }
        OutputStream out  =  response.getOutputStream();
        while  ((len  =  br.read(buf))  >   0 ) {
            out.write(buf,  0 , len);
        }
        br.close();
        out.close();
    }
    /**
     * 下载本地文件
     *
     * @param path
     * @param response
     * @throws FileNotFoundException
     */
    public static void downloadLocal(String path, String fileName, HttpServletRequest request,HttpServletResponse response) throws FileNotFoundException {
        path = path.replace("%20", " ");
        // 文件名称
        if(null==fileName || "".equals(fileName)) {
            fileName = path.split("/")[path.split("/").length - 1];
        }
        // 读到流中
        InputStream inStream = new FileInputStream(path); // 文件的存放路径
        // 清空response
        response.reset();
        response.setContentType("application/octet-stream");
        try {
            //浏览器类型判断
            String BrowsersType = request.getHeader("User-Agent");
            //谷歌和火狐的判断
            if (!BrowsersType.contains("Chrome") && !BrowsersType.contains("Firefox")) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {//其他类型的
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
            response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
            // 循环取出流中的数据
            byte[] b = new byte[100];
            int len;
            while ((len = inStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }

        } catch (IOException e) {
            String msg = e.toString();
            if(msg.indexOf("ClientAbortException")!=-1){
                System.out.println("用户取消下载...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static HttpServletResponse downloadFile(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }

    public static File downloadNet(String url,String filePath){
        //创建不同的文件夹目录
        File file = new File(filePath);
        //判断文件夹是否存在
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            // 建立链接
            URL httpUrl = new URL(url);
            conn = (HttpURLConnection) httpUrl.openConnection();
            //以Post方式提交表单，默认get方式
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // post方式不能使用缓存
            conn.setUseCaches(false);
            //连接指定的资源
            conn.connect();
            //获取网络输入流
            inputStream = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            //判断文件的保存路径后面是否以/结尾
            if (!filePath.endsWith("/")) {
                filePath += "/";
            }
            //写入到文件（注意文件保存路径的后面一定要加上文件的名称）
            String path = httpUrl.getPath();
            String fileName = path.substring(path.lastIndexOf("/")+1);
            fileOut = new FileOutputStream(filePath + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fileOut);
            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            //保存文件
            while(length != -1) {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            bis.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static InputStream getInputStreamByUrl(String strUrl) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 * 1000);
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(conn.getInputStream(), output);
            return new ByteArrayInputStream(output.toByteArray());
        } catch (Exception e) {
            log.error("getInputStreamByUrl 异常,exception is {}", e);
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
            }
        }
        return null;
    }


    public static void download(HttpServletRequest request, HttpServletResponse response, String filePath){
        File file = new File(filePath);
        // 取得文件名。
        String fileName = file.getName();
        InputStream fis = null;
        try {
            fis = new FileInputStream(file);
            request.setCharacterEncoding("UTF-8");
            String agent = request.getHeader("User-Agent").toUpperCase();
            if ((agent.indexOf("MSIE") > 0) || ((agent.indexOf("RV") != -1) && (agent.indexOf("FIREFOX") == -1))){
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }
            else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
            response.reset();
            response.setCharacterEncoding("UTF-8");
            // 设置强制下载不打开
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setHeader("Content-Length", String.valueOf(file.length()));
            byte[] b = new byte[1024];
            int len;
            while ((len = fis.read(b)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
            response.flushBuffer();
            fis.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void disableSSLVerification() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }
    public static void downloadNet( String HttpfileName,String tofile,boolean flag) throws MalformedURLException {
//
        try {
            // 禁用SSL验证（仅测试）
            disableSSLVerification();

            // 原始URL（检查加号是否需要编码）
            String urlStr = HttpfileName;

            URL url = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 检查响应码
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream in = conn.getInputStream();
                     FileOutputStream out = new FileOutputStream(tofile)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
                System.out.println("文件下载成功！");
            } else {
                System.out.println("请求失败，响应码：" + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static boolean downloadNetHttp(String HttpfileName,String tofile){
        boolean state = false;
        try {
            // 原始URL（检查加号是否需要编码）
            String urlStr = HttpfileName;

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 检查响应码
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream in = conn.getInputStream();
                     FileOutputStream out = new FileOutputStream(tofile)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
                state = true;
                System.out.println("文件下载成功！");
            } else {
                System.out.println("请求失败，响应码：" + responseCode);
            }
        } catch (Exception e) {
            System.out.println("下载：" + HttpfileName+"报错："+e.getMessage());
            e.printStackTrace();
        }
        return state;
    }
    public static void downloadNetWav(String fileURL,String savePath){
        try {
            // 创建URL对象
            URL url = new URL(fileURL);

            // 打开连接并获取输入流
            try (BufferedInputStream in = new BufferedInputStream(url.openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {
                byte dataBuffer[] = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 4096)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            }

            System.out.println("File downloaded successfully to: " + savePath);
        } catch (IOException e) {
            System.err.println("Error occurred while downloading the file.");
            e.printStackTrace();
        }
    }
}