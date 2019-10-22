package www.dugaolong.com.xianshishigongjiao;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.qq.e.comm.util.FileUtil;
import com.qq.e.comm.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * UncaughtException处理类,当系统发生Uncaught异常时，将相关log和机器信息保存并上传到服务器
 *
 * @author 陈志泉
 * @ClassName CrashHandler
 * @date 2015年4月7日 上午10:30:54
 */
public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final int MAX_CRASH_LOG_SIZE = 10 * 1024 * 1024;
    private Thread.UncaughtExceptionHandler mDefaultHandler;// 系统默认的UncaughtException处理类
    private static CrashHandler INSTANCE = new CrashHandler();// CrashHandler实例
    private Context mContext;
    private Map<String, String> info = new HashMap<String, String>();// 用来存储设备信息和异常信息
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");// 用于格式化日期

    private Lock lock = null;//防止多线程中的异常导致读写不同步问题的lock

    private boolean mUploadCrashReports = false;

    private List<String> fileList = new ArrayList<String>();

    private SharedPreferences crashInfo = null;//崩溃后无法保存role，每次启动时缓存role信息

    private CrashHandler() {
        lock = new ReentrantLock(true);
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context context, boolean uploadCrashReports) {
        mContext = context;
        mUploadCrashReports = uploadCrashReports;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        clearOldCrashLog();
    }

    /*
     * 当UncaughtException发生时会转入该重写的方法来处理
     */
    public void uncaughtException(Thread thread, Throwable ex) {
//        CollectData.loadActiveEvent(mContext, System.currentTimeMillis());

        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
        }

        try {
            thread.sleep(2000);// 让程序继续运行2秒再退出
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        exitApp();
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex 异常信息
     * @return true 如果处理了该异常信息;否则返回false.
     */
    public boolean handleException(Throwable ex) {
        if (ex == null)
            return false;
        ex.printStackTrace();
        new Thread() {
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, mContext.getResources().getString(R.string.toast_app_crash), Toast.LENGTH_SHORT).show();
                /*
                new AlertDialog.Builder(mContext).setTitle("提示")
				.setCancelable(false).setMessage("亲，程序马上崩溃了...")
				.setNegativeButton("没关系", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						
					}
				}).create().show();
				*/
                Looper.loop();
            }
        }.start();

        // 收集设备参数信息
        collectDeviceInfo(mContext);

        // 保存日志文件
        String filePath = saveCrashInfo2File(ex);
        if (mUploadCrashReports) {
            sendCrashReportsToServer();
        }
        return true;
    }

    public void sendCrashReportsToServer() {
        searchLogFile();
        if (null != fileList && fileList.size() > 0) {
//			File cr = new File(filePath);			
            postReport(fileList.get(0));
//			cr.delete();// 删除已发送的报告
        }
    }

    private String readLog(String filePath) {
        String path = filePath;
        String log = null;

        File file = new File(path);
        if (file.isDirectory()) {
            Log.d(TAG, "The File is not exist filePath = " + filePath);
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (null != instream) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    while (null != (line = buffreader.readLine())) {
                        log += line + "\n";
                    }
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                Log.d(TAG, "The File is not exist! e = " + e.getMessage());
                return null;
            } catch (IOException e) {
                Log.d(TAG, "reading File err: " + e.getMessage());
            }
        }

        return log != null ? log : null;
//		return "test";
    }

    private String curUploadFile = null;

    /**
     * 上传log到服务器执行函数
     *
     * @param filePath
     */
    private void postReport(String filePath) {
        // TODO 发送错误报告到服务器
        curUploadFile = filePath;
        crashInfo = mContext.getSharedPreferences("crashInfo.xml", Context.MODE_PRIVATE);
        int userId = crashInfo.getInt("userId", -1);
        int userType = crashInfo.getInt("userType", -1);
        String area = crashInfo.getString("area", "");
    }

    /**
     * 检查缓存的crashLog,如果大小超过MAX_CRASH_LOG_SIZE（10M），
     * 就删除早期的log，缓存的crashLog可导出，用于分析。
     */
    private void clearOldCrashLog() {
        Log.d(TAG, "Enter clearOldCrashLog");
        List<File> crashFiles = new ArrayList<File>();
//        sreachLogFile();
//        crashFiles.addAll(fileList);
        File[] files = new File(getCashLogBackupDir()).listFiles();
        Log.d(TAG, "files_count = " + files.length);
        for (File tmpfile : files) {
            if (tmpfile.getName().indexOf("crash-") >= 0 && tmpfile.getName().indexOf(".log") >= 0) {
                crashFiles.add(tmpfile);
                Log.d(TAG, "clearOldCrashLog_log_file = " + tmpfile.getName());
            }
        }
        sortCrashLogByCreateDate(crashFiles);
        long size = 0;
        int i = 0;
        for (; i < crashFiles.size(); i++) {
            if (crashFiles.get(i) != null) {
                size += crashFiles.get(i).length();
            }
            if (size > MAX_CRASH_LOG_SIZE) {
                break;
            }
        }
        Log.d(TAG, "log_size = " + size);
        if ((i + 1) < crashFiles.size()) {
            Log.d(TAG, "delete i = " + i);
            for (; i < crashFiles.size(); i++) {
                if (crashFiles.get(i) != null) {
                    crashFiles.get(i).delete();
                }
            }
        }
        Log.d(TAG, "Exit clearOldCrashLog");
    }

    private void sortCrashLogByCreateDate(List<File> crashFiles) {
        Collections.sort(crashFiles, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                String timestamp1 = splitTimestamp(lhs.getName());
                String timestamp2 = splitTimestamp(rhs.getName());
                if (!StringUtil.isEmpty(timestamp1) && !StringUtil.isEmpty(timestamp2)) {
                    long time1 = Long.parseLong(timestamp1);
                    long time2 = Long.parseLong(timestamp2);
                    if (time1 > time2) {
                        return -1;
                    } else if (time1 < time2) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
                return 0;
            }
        });
    }

    private String splitTimestamp(String source) {
        String result = null;
        if (source != null) {
            String temp = source.split("\\.")[0];
            String[] temps = temp.split("-");
            if (temps.length > 0) {
                result = temps[temps.length - 1];
            }
        }
        return result;
    }

    private void searchLogFile() {
        fileList.clear();
        File[] files = new File(mContext.getFilesDir().getAbsolutePath()).listFiles();
        for (File tmpfile : files) {
            if (tmpfile.getName().indexOf("crash-") >= 0 && tmpfile.getName().indexOf(".log") >= 0) {
                fileList.add(mContext.getFilesDir().getAbsolutePath() + "/" + tmpfile.getName());
                Log.d(TAG, "searchLogFile log_file = " + tmpfile.getName());
            }
        }

        Log.d(TAG, "searchLogFile size = " + fileList.size());

    }

    /**
     * 收集设备参数信息
     *
     * @param context
     */
    public void collectDeviceInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();// 获得包管理器
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                info.put("versionName", versionName);
                info.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        Field[] fields = Build.class.getDeclaredFields();// 反射机制
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                info.put(field.getName(), field.get("").toString());
//				Log.d(TAG, field.getName() + ":" + field.get(""));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private String saveCrashInfo2File(Throwable ex) {
        Log.d(TAG, "Enter saveCrashInfo2File");
        File saveFile = null;
        PrintWriter printWriter = null;
        long timetamp = System.currentTimeMillis();
        String time = format.format(new Date());
        String fileName = "crash-" + time + "-" + timetamp + ".log";

        try {
            lock.tryLock();
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> entry : info.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key + "=" + value + "\r\n");
            }
                
                /*
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录
                    saveFile = new File(sdCardDir, fileName);
                     
                }else{
                     saveFile =new File(mContext.getFilesDir(),fileName);
                }
                */
            saveFile = new File(mContext.getFilesDir(), fileName);
            Log.d(TAG, "saveFile path = " + saveFile.getAbsolutePath());

            if (!saveFile.exists()) {
                saveFile.createNewFile();
            }

            printWriter = new PrintWriter(saveFile);
            String result = formatException(ex);
            sb.append(result);
            printWriter.write(sb.toString());
            printWriter.flush();
        } catch (Exception e) {
            Log.d(TAG, "saveCrashInfo2File Error:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
            lock.unlock();
        }

        return saveFile != null ? saveFile.getAbsolutePath() : null;
    }

    /**
     * 格式化异常信息
     *
     * @param e
     * @return
     */
    private String formatException(Throwable e) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackTrace = e.getStackTrace();
        if (stackTrace != null) {
            String timeStramp = format.format(new Date(System.currentTimeMillis()));
            String format = String.format("DateTime:%s\nExceptionName:%s\n\n", timeStramp, e.getLocalizedMessage());
            sb.append(format);
            for (int i = 0; i < stackTrace.length; i++) {
                StackTraceElement traceElement = stackTrace[i];
                String fileName = traceElement.getFileName();
                int lineNumber = traceElement.getLineNumber();
                String methodName = traceElement.getMethodName();
                String className = traceElement.getClassName();
                sb.append(String.format("%s\t%s[%d].%s \n", className, fileName, lineNumber, methodName));
            }
            sb.append(String.format("\n%s", e.getMessage()));
            Writer stringWriter = new StringWriter();
            PrintWriter pw = new PrintWriter(stringWriter);
            e.printStackTrace(pw);
            pw.flush();
            pw.close();

            sb.append("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
            sb.append(stringWriter.toString());
        }
        return sb.toString();
    }

    private String getCashLogBackupDir() {
        String path = mContext.getFilesDir().getAbsolutePath() + File.separator + "backup";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Log.d(TAG, "getCashLogBackupDir path = " + path);
        return path;
    }


    /**
     * 退出应用程序
     */
    public void exitApp() {
        try {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
