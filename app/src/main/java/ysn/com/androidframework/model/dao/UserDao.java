package ysn.com.androidframework.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ysn.com.androidframework.model.bean.User;

/**
 * @Author yangsanning
 * @ClassName UserDao
 * @Description 一句话概括作用
 * @Date 2020/5/9
 */
@Dao
public interface UserDao {

    /**
     * 返回值是插入成功的行id
     */
    @Insert
    List<Long> insert(User... users);

    @Delete
    void delete(User... users);

    @Delete
    int delete(User user);

    @Update
    void update(User... users);

    @Update
    int update(User user);

    /**
     * 删除全部数据
     */
    @Query("DELETE FROM User")
    void deleteAll();

    /**
     * 查询所有对象 且 观察数据。用背压Flowable可以实现，如果需要一次性查询，可以用别的类型
     */
    @Query("Select * from User")
    Flowable<List<User>> getAll();

    /**
     * 根据字段去查找数据
     */
    @Query("SELECT * FROM User WHERE id= :id")
    Single<User> getUserById(int id);
}
