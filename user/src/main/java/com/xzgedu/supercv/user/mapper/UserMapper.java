package com.xzgedu.supercv.user.mapper;

import com.xzgedu.supercv.user.domain.User;
import com.xzgedu.supercv.user.mapper.sql.UserProvider;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserMapper {
    @Results(id = "User", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "telephone", column = "telephone"),
            @Result(property = "unionId", column = "union_id"),
            @Result(property = "openId", column = "open_id"),
            @Result(property = "nickName", column = "nick_name"),
            @Result(property = "headImgUrl", column = "head_img_url"),
    })
    @Select("select * from cv_user where id=#{id}")
    User selectUserById(@Param("id") long id);

    @ResultMap("User")
    @Select("select * from cv_user where open_id=#{openId}")
    User selectUserByOpenId(@Param("openId") String openId);

    @ResultMap("User")
    @Select("select * from cv_user where union_id=#{unionId}")
    User selectUserByUnionId(@Param("unionId") String unionId);

    @ResultMap("User")
    @Select("select * from cv_user where telephone=#{telephone}")
    User selectUserByTelephone(@Param("telephone") String telephone);

    @ResultMap("User")
    @Select("select * from cv_user where nick_name=#{nickName} order by create_time desc " +
            "limit #{limitOffset}, #{limitSize}")
    List<User> selectUsersByNickName(@Param("nickName") String nickName,
                                     @Param("limitOffset") int limitOffset,
                                     @Param("limitSize") int limitSize);

    @Select("select count(*) from cv_user where nick_name=#{nickName}")
    int countUsersByNickName(@Param("nickName") String nickName);

    @ResultMap("User")
    @Select("select * from cv_user order by create_time desc limit #{limitOffset}, #{limitSize}")
    List<User> listUsers(@Param("limitOffset") int limitOffset,
                         @Param("limitSize") int limitSize);

    @SelectProvider(type = UserProvider.class, method = "countUsersByDuration")
    int countUsersByDuration(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @ResultMap("User")
    @SelectProvider(type = UserProvider.class, method = "selectUsersByUids")
    List<User> selectUsersByUids(List<Long> uids);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into cv_user(telephone, open_id, union_id, nick_name, head_img_url) " +
            "values(#{telephone}, #{openId}, #{unionId}, #{nickName}, #{headImgUrl})")
    int insertUser(User user);

    @Update("update cv_user set telephone=#{telephone} where id=#{uid}")
    int updateTelephone(@Param("uid") long uid, @Param("telephone") String telephone);

    @Update("update cv_user set nick_name=#{nickName} where id=#{uid}")
    int updateNickName(@Param("uid") long uid, @Param("nickName") String nickName);

    @Update("update cv_user set head_img_url=#{headImgUrl} where id=#{uid}")
    int updateHeadImgUrl(@Param("uid") long uid, @Param("headImgUrl") String headImgUrl);
}
