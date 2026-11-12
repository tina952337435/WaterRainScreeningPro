package api.tools;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 带文件过滤器的版本
 */
public class FileListerWithFilter {
    public static List<File> getAllFiles(String folderPath, FileFilter filter) {
        List<File> fileList = new ArrayList<>();
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            return fileList;
        }

        listFilesWithFilter(folder, fileList, filter);
        return fileList;
    }

    private static void listFilesWithFilter(File folder, List<File> fileList, FileFilter filter) {
        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isFile() && (filter == null || filter.accept(file))) {
                fileList.add(file);
            } else if (file.isDirectory()) {
                listFilesWithFilter(file, fileList, filter);
            }
        }
    }

//    public static void main(String[] args) {
//        // 示例：只列出.txt文件
//        FileFilter txtFilter = file -> file.getName().toLowerCase().endsWith(".txt");
//        List<File> txtFiles = getAllFiles("D:/test", txtFilter);
//
//        System.out.println("找到 " + txtFiles.size() + " 个txt文件:");
//        txtFiles.forEach(file -> System.out.println(file.getAbsolutePath()));
//    }
}
