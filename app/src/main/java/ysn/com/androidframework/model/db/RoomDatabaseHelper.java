package ysn.com.androidframework.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import ysn.com.androidframework.model.bean.User;
import ysn.com.androidframework.model.dao.UserDao;

/**
 * @Author yangsanning
 * @ClassName RoomDatabaseHelper
 * @Description 一句话概括作用
 * @Date 2020/5/9
 */
//注解指定了database的表映射实体数据以及版本等信息
@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class RoomDatabaseHelper extends RoomDatabase {

    public static RoomDatabaseHelper getDefault(Context context) {
        return databaseBuilder(context, RoomDatabaseHelper.class, "AndroidFramework");
    }

    /**
     * 为持久数据库创建一个RoomDatabase.Builder。
     */
    private static <T extends RoomDatabase> T databaseBuilder(@NonNull Context context,
                                                              @NonNull Class<T> klass, @NonNull String tableName) {
        return Room.databaseBuilder(context.getApplicationContext(), klass, tableName)
                .allowMainThreadQueries()
                .setJournalMode(JournalMode.TRUNCATE)
                .build();
    }

    public abstract UserDao getUserDao();
}
