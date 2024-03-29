package tech.poorguy;

import lombok.extern.slf4j.Slf4j;
import tech.poorguy.annotation.Scan;
import tech.poorguy.fileUtil.FileUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Date 9/28/2019 3:58 PM
 * @Mail 494939649@qq.com
 **/
@Slf4j
@Scan(scanPackages = "tech.poorguy.bean")
public class Main {
    public static List<String> filePathList = new ArrayList<>();
    public static List<Object> objectList = new ArrayList<>();
    public static void main(String[] args){
        Class<Main> mainClass = Main.class;
        if (mainClass.isAnnotationPresent(Scan.class)) {
            Scan scanAnnotation = mainClass.getAnnotation(Scan.class);
            if ((scanAnnotation.scanPackages().length==0)) {

            } else {
                String[] scanPackages = scanAnnotation.scanPackages();
                String scanPackage = scanPackages[0];
                String packagePath = scanPackage.replaceAll("\\.", "/");
                URL url = mainClass.getClassLoader().getResource(packagePath);
                log.info(url.toString());
                if (url == null) {
                    System.out.println("@Scan(scanPackages) wrong config");
                }
                if ("file".equals(url.getProtocol())) {
                    try {
                        filePathList.addAll(FileUtil.getRelativeClassNameByDir(url.getPath()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if ("jar".equals(url.getProtocol())) {

                }
            }
        }
        log.info(filePathList.toString());
        for (String path : filePathList) {
            try {
                Class<?> clazz = mainClass.getClassLoader().loadClass(path);
                objectList.add(clazz.newInstance());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        log.info(objectList.toString());
    }
}
