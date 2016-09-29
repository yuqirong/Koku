package com.yuqirong.koku.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yuqirong.koku.app.MyApplication;
import com.yuqirong.koku.module.model.entity.Draft;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anyway on 2015/10/10.
 */
public class DraftDB {

    private static final String TAG = DraftDB.class.getSimpleName();

    private static SQLiteDatabase draftDb;

    // 创建表情库
    static {
        File dbf = MyApplication.getContext().getDatabasePath("draft.db");
        if (!dbf.exists()) {
            LogUtils.i("新建草稿箱DB");
            dbf.getParentFile().mkdirs();
            try {
                if (dbf.createNewFile())
                    draftDb = SQLiteDatabase.openOrCreateDatabase(dbf, null);
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        } else {
            LogUtils.i("草稿箱DB已存在");
            draftDb = SQLiteDatabase.openOrCreateDatabase(dbf, null);
        }
    }

    public static void checkDraft() {
        Cursor cursor = null;
        // 检查表是否存在
        boolean tableExist = false;
        try {
            String sql = "SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='" + DraftTable.table + "' ";

            cursor = draftDb.rawQuery(sql, null);
            if (cursor != null && cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0)
                    tableExist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

        // 表情表不存在，创建表情表
        if (!tableExist) {
            LogUtils.i("create draft table");

            String sql = String.format("create table %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT,%s INTEGER, %s TEXT , %s TEXT , %s TEXT)", DraftTable.table,
                    DraftTable.id, DraftTable.type, DraftTable.text, DraftTable.pic_urls, DraftTable.weibo_idstr);
            draftDb.execSQL(sql);
        } else {
            LogUtils.i("draft table exist");
        }
    }

    /**
     * 得到数据库中的Draft
     *
     * @return
     */
    public static List<Draft> getDraftList() {
        Draft draft;
        List<Draft> drafts = new ArrayList<>();
        Cursor cursor = draftDb.rawQuery(" SELECT * FROM " + DraftTable.table, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DraftTable.id));
                int type = cursor.getInt(cursor.getColumnIndex(DraftTable.type));
                String text = cursor.getString(cursor.getColumnIndex(DraftTable.text));
                String pic_urls = cursor.getString(cursor.getColumnIndex(DraftTable.pic_urls));
                List<String> urls = StringUtils.convertStringToList(pic_urls);
                String weibo_idstr = cursor.getString(cursor.getColumnIndex(DraftTable.weibo_idstr));
                draft = new Draft(id, type, text, urls, weibo_idstr);
                drafts.add(draft);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return drafts;
    }

    /**
     * 删除数据库中的Draft
     *
     * @param id draft id
     * @return true：删除成功
     */
    public static boolean deleteDraft(int id) {
        int temp = draftDb.delete(DraftTable.table, DraftTable.id + " = ?", new String[]{String.valueOf(id)});
        if (temp != 0) {
            LogUtils.i("delete draft successfully which it's id =" + id);
            return true;
        } else {
            LogUtils.i("delete draft failed  which it's id =" + id);
            return false;
        }
    }

    /**
     * 更新Draft
     *
     * @param d
     * @return
     */
    public static boolean updateDraft(Draft d) {
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(DraftTable.text, d.text);
        mContentValues.put(DraftTable.pic_urls, StringUtils.convertListToString(d.pic_urls));
        int temp = draftDb.update(DraftTable.table, mContentValues, DraftTable.id + " = ?", new String[]{String.valueOf(d.id)});
        if (temp != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 添加Draft
     *
     * @param d
     * @return
     */
    public static boolean addDraft(Draft d) {
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(DraftTable.type, d.type);
        mContentValues.put(DraftTable.text, d.text);
        mContentValues.put(DraftTable.pic_urls, StringUtils.convertListToString(d.pic_urls));
        mContentValues.put(DraftTable.weibo_idstr, d.idstr);
        long temp = draftDb.insert(DraftTable.table, null, mContentValues);
        if (temp != -1) {
            return true;
        } else {
            return false;
        }
    }

    static class DraftTable {

        static final String table = "draft_table";

        static final String id = "draft_id";

        static final String type = "draft_type";

        static final String text = "draft_text";

        static final String pic_urls = "draft_pic_urls";

        static final String weibo_idstr = "weibo_idstr";

    }

}
