/*
 * Copyright 2019 wjybxx
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.noah.common.utils;


import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassFinder {

    /**
     * 工程项目文件夹
     * target为生成的class文件目录
     */
    private static final Set<String> ignoreDirs = new HashSet<>(Arrays.asList(".git", ".svn", ".idea"));

    private ClassFinder() {

    }

    /**
     * 从包package中获取所有的Class
     *
     * @param pkgName java包名,eg: com.noah.common
     * @return classSet
     */
    public static Set<Class<?>> findAllClass(String pkgName) {
        return findClasses(pkgName, clazzName -> true, clazz -> true);
    }

    /**
     * 加载指定包下符合条件的class
     *
     * @param pkgName         java包名,eg: com.noah.common
     * @param classNameFilter 过滤要加载的类，避免加载过多无用的类 test返回true的才会加载
     * @param classFilter     对加载后的类进行再次确认 test返回true的才会添加到结果集中
     * @return 符合过滤条件的class文件
     */
    public static Set<Class<?>> findClasses(String pkgName, Predicate<String> classNameFilter, Predicate<Class<?>> classFilter) {
        Set<Class<?>> classes = new LinkedHashSet<>();
        String pkgDirName = pkgName.replace('.', '/');

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> urls = classLoader.getResources(pkgDirName);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findClassesByFile(classLoader, pkgName, filePath, classes, classNameFilter, classFilter);
                } else if ("jar".equals(protocol)) {
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    findClassesByJar(classLoader, pkgName, jar, classes, classNameFilter, classFilter);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * 从文件夹中加载class文件
     *
     * @param classLoader     类加载器
     * @param pkgName         java包名
     * @param pkgPath         文件夹路径
     * @param classes         结果输出
     * @param classNameFilter 过滤要加载的类，避免加载过多无用的类
     * @param classFilter     对加载后的类进行再次确认
     */
    public static void findClassesByFile(ClassLoader classLoader, String pkgName, String pkgPath, Set<Class<?>> classes,
                                         Predicate<String> classNameFilter, Predicate<Class<?>> classFilter) {
        File dir = new File(pkgPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] dirFiles = dir.listFiles(file -> file.isDirectory() || file.getName().endsWith(".class"));
        if (dirFiles == null || dirFiles.length == 0) {
            return;
        }

        for (File file : dirFiles) {
            if (file.isDirectory()) {
                if (ignoreDirs.contains(file.getName())) {
                    continue;
                }
                findClassesByFile(classLoader, pkgName + "." + file.getName(), pkgPath + "/" + file.getName(), classes,
                        classNameFilter, classFilter);
                continue;
            }

            // 如果是java类文件 去掉后面的.class 只留下类名
            String className = pkgName + "." + file.getName().substring(0, file.getName().length() - 6);
            // 不需要加载
            if (!classNameFilter.test(className)) {
                continue;
            }

            try {
                Class clazz = classLoader.loadClass(className);
                if (classFilter.test(clazz)) {
                    classes.add(clazz);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从jar包中搜索class文件
     *
     * @param classLoader     类加载器
     * @param pkgName         java包名
     * @param jar             jar包对象
     * @param classes         结果输出
     * @param classNameFilter 过滤要加载的类，避免加载过多无用的类
     * @param classFilter     对加载后的类进行再次确认
     */
    public static void findClassesByJar(ClassLoader classLoader, String pkgName, JarFile jar, Set<Class<?>> classes,
                                        Predicate<String> classNameFilter, Predicate<Class<?>> classFilter) {
        // 这里需要 + "/"，避免startWith判断错误的情况
        final String pkgDir = pkgName.replace(".", "/") + "/";
        final Enumeration<JarEntry> entry = jar.entries();

        while (entry.hasMoreElements()) {
            JarEntry jarEntry = entry.nextElement();
            if (jarEntry.isDirectory()) {
                continue;
            }

            String name = jarEntry.getName();
            if (!name.startsWith(pkgDir) || !name.endsWith(".class")) {
                continue;
            }

            // 如果是一个.class文件，去掉后面的".class" 获取真正的类名
            String className = name.substring(0, name.length() - 6).replace("/", ".");
            if (!classNameFilter.test(className)) {
                continue;
            }

            try {
                Class<?> clazz = classLoader.loadClass(className);
                if (classFilter.test(clazz)) {
                    classes.add(clazz);
                }
            } catch (ClassNotFoundException | NoClassDefFoundError e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Set<Class<?>> allClass = findAllClass("com.noah.common");
        allClass.forEach(System.out::println);
    }
}
