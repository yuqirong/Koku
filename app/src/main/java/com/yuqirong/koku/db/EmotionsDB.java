package com.yuqirong.koku.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.yuqirong.koku.application.MyApplication;
import com.yuqirong.koku.entity.Emotion;
import com.yuqirong.koku.util.FileUtils;
import com.yuqirong.koku.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Anyway on 2015/9/22.
 */
public class EmotionsDB {

    private static final String TAG = EmotionsDB.class.getSimpleName();

    private static SQLiteDatabase emotionsDb;

    // 创建表情库
    static {
        String path = MyApplication.getContext().getFilesDir() + File.separator + "emotions_v5.db";
        File dbf = new File(path);
        if (!dbf.exists()) {
            LogUtils.i("新建表情DB");
            dbf.getParentFile().mkdirs();
            try {
                if (dbf.createNewFile())
                    emotionsDb = SQLiteDatabase.openOrCreateDatabase(dbf, null);
            } catch (IOException ioex) {
            }
        } else {
            LogUtils.i("表情DB已存在");
            emotionsDb = SQLiteDatabase.openOrCreateDatabase(dbf, null);
        }
    }

    public static void checkEmotions() {
        Cursor cursor = null;
        // 检查表是否存在
        boolean tableExist = false;
        try {
            String sql = "SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='" + EmotionTable.table + "' ";

            cursor = emotionsDb.rawQuery(sql, null);
            if (cursor != null && cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0)
                    tableExist = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            cursor = null;
        }

        // 表情表不存在，创建表情表
        if (!tableExist) {
            LogUtils.i("create emotions table");

            String sql = String.format("create table %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT , %s TEXT, %s BOLB)", EmotionTable.table,
                    EmotionTable.id, EmotionTable.key, EmotionTable.file, EmotionTable.value);
            emotionsDb.execSQL(sql);
        } else {
            LogUtils.i("emotions table exist");
        }

        boolean insertEmotions = true;
        // 表情不存在或者不全，插入表情
        try {
            cursor = emotionsDb.rawQuery(" select count(*) as c from " + EmotionTable.table, null);
            if (cursor != null && cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                if (count == 160)
                    insertEmotions = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 向数据库插入表情
        if (insertEmotions) {
            LogUtils.i("insert emotions");
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    InputStream in;
                    try {
                        in = MyApplication.getContext().getAssets().open("emotions.properties");
                        Properties properties = new Properties();
                        properties.load(new InputStreamReader(in, "utf-8"));
                        Set<Object> keySet = properties.keySet();

                        // 开启事务
                        emotionsDb.beginTransaction();
                        emotionsDb.execSQL(String.format("delete from %s", EmotionTable.table));
                        for (Object key : keySet) {
                            String value = properties.getProperty(key.toString());
                            LogUtils.i(String.format("emotion's key(%s), value(%s)", key, value));

                            ContentValues values = new ContentValues();
                            values.put(EmotionTable.key, key.toString());

                            byte[] emotion = FileUtils.readStreamToBytes(MyApplication.getContext().getAssets().open(value));
                            values.put(EmotionTable.value, emotion);
                            values.put(EmotionTable.file, value);

                            emotionsDb.insert(EmotionTable.table, EmotionTable.id, values);
                        }
                        // 结束事务
                        emotionsDb.setTransactionSuccessful();
                        emotionsDb.endTransaction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

            }.execute();
        } else {
            LogUtils.i("emotions exist");
        }
    }

    //	static ZHConverter converter;
    public static byte[] getEmotion(String key) {
//		if (converter == null)
//			converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
//		key = converter.convert(key);

        Cursor cursor = emotionsDb.rawQuery(" SELECT " + EmotionTable.value + " FROM " + EmotionTable.table + " WHERE " + EmotionTable.key + " = ? ",
                new String[]{key});

        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(cursor.getColumnIndex(EmotionTable.value));
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return null;
    }

    /**
     * 得到所有的表情
     * @return
     */
    public static List<Emotion> getAllEmotions() {
        List<Emotion> list = new ArrayList(180);

        Cursor cursor = emotionsDb.query(EmotionTable.table,null,null,null,null,null,EmotionTable.file,null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    byte[] data = cursor.getBlob(cursor.getColumnIndex(EmotionTable.value));
                    String key = cursor.getString(cursor.getColumnIndex(EmotionTable.key));

                    Emotion emotion = new Emotion();
                    emotion.setData(data);
                    emotion.setKey(key);
                    list.add(emotion);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return list;
    }

    static class EmotionTable {

        static final String table = "emotions_table";

        static final String id = "emotion_id";

        static final String key = "emotion_key";

        static final String file = "emotion_file";

        static final String value = "emotion_value";

    }

}
