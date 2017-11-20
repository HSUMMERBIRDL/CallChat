package com.hl.foundation.library.utils;

import android.content.Context;
import android.os.Environment;

import com.hl.foundation.frame.app.BaseApplication;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * Created by Freddy on 2015/11/20.
 * chenshichao@outlook.com
 */
public class FileUtil {

    /**
     * 检测SD卡是否存在
     */
    public static boolean checkSDcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    /**
     * 获取缓存目录文件夹
     *
     * @param folderName
     *            缓存目录下的文件夹名
     * @return 返回SD卡下的指定文件夹对象，若文件夹不存在则创建
     */
    public static File getCacheFolder(String folderName) {
        File file = null;
        if (checkSDcard()) {
            file = BaseApplication.getAppContext().getExternalCacheDir();
        }
        if (null == file) {
            file = BaseApplication.getAppContext().getCacheDir();
        }
        file = new File(file.getAbsoluteFile() + File.separator + folderName
                + File.separator);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getCacheFolder() {
        File file = null;
        if (checkSDcard()) {
            file = BaseApplication.getAppContext().getExternalCacheDir();
        }
        if (null == file) {
            file = BaseApplication.getAppContext().getCacheDir();
        }
        return file;
    }

    /**
     * 路径转换，去除父路径
     * @param absolutePath
     * @return
     */
    public static String convertPath(String absolutePath) {
        if(StringUtils.isEmpty(absolutePath)) {
            return null;
        }

        if(absolutePath.contains("/")) {
            return absolutePath.substring(absolutePath.lastIndexOf("/"), absolutePath.length());
        }

        return absolutePath;
    }

    /**
     * 输入流转字符串
     *
     * @param is
     * @return 一个流中的字符串
     */
    public static String inputStream2String(InputStream is) {
        if (null == is) {
            return null;
        }
        StringBuilder resultSb = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            resultSb = new StringBuilder();
            String len;
            while (null != (len = br.readLine())) {
                resultSb.append(len);
            }
        } catch (Exception ex) {
        } finally {
            closeIO(is);
        }
        return null == resultSb ? null : resultSb.toString();
    }

    /**
     * 从assets中读取文本
     *
     * @param name
     * @return
     */
    public static String readFileFromAssets(Context context, String name) {
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream2String(is);
    }

    /**
     * 关闭流
     *
     * @param closeables
     */
    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                throw new RuntimeException(FileUtil.class.getClass().getName(), e);
            }
        }
    }

    /**
     * 写入文件到文本
     * @param filePath
     * @param data
     */
    public static void writeFile(String filePath, String data) {
        if (null == filePath) {
            return;
        }
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(filePath);
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件中读取文本
     *
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) {
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
        } catch (Exception e) {
            throw new RuntimeException(FileUtil.class.getName() + "readFile---->"
                    + filePath + " not found");
        }
        return inputStream2String(is);
    }

    public static boolean saveContentToFile(String content, File fileForSave) {
        Lock writeLock = getLock(fileForSave.getAbsolutePath()).writeLock();
        boolean succeed = true;
        FileOutputStream out = null;
        if (writeLock.tryLock()) {
            try
            {
                out = new FileOutputStream(fileForSave, false);
                out.write(content.getBytes("utf-8"));
            }
            catch (Exception e) {
                e.printStackTrace();
                succeed = false;
            } finally {
                if (out != null) {
                    closeQuietly(out);
                }
                writeLock.unlock();
            }
        }
        return succeed;
    }

    public static String readContentFromFile(File fileForRead) {
        Lock readLock = getLock(fileForRead.getAbsolutePath()).readLock();
        readLock.lock();
        FileInputStream in = null;
        InputStreamReader reader = null;
        BufferedReader breader = null;
        try {
            in = new FileInputStream(fileForRead);
            reader = new InputStreamReader(in, "utf-8");
            breader = new BufferedReader(reader);

            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = breader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(breader);
            closeQuietly(reader);
            closeQuietly(in);
            readLock.unlock();
        }
        return null;
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) closeable.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ConcurrentHashMap<String, ReentrantReadWriteLock> fileLocks = new ConcurrentHashMap();
    private static ReentrantReadWriteLock getLock(String path) {
        ReentrantReadWriteLock lock = (ReentrantReadWriteLock)fileLocks.get(path);
        if (lock == null) {
            lock = new ReentrantReadWriteLock();
            ReentrantReadWriteLock oldLock = (ReentrantReadWriteLock)fileLocks.putIfAbsent(path, lock);
            if (oldLock != null) {
                lock = oldLock;
            }
        }
        return lock;
    }
}
