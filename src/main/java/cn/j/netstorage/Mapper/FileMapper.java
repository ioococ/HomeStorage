package cn.j.netstorage.Mapper;

import cn.j.netstorage.Entity.File.File;
import cn.j.netstorage.Entity.File.Files;
import cn.j.netstorage.Entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.annotation.Resource;
import java.util.List;


public interface FileMapper extends JpaRepository<Files,Long> {

    List<Files> findAllByParentNameAndUser_uidAndIsDir(String parentName,long uid,Short isDir);


    Files findByParentNameAndAndUserAndAndSelfName(String parentName, User user, String selfName);

    Integer deleteAllByFidIsOrParentNameIsLike(Long fid,String parentName);

    Integer deleteByFid(Long fid);

    List<Files> findAllBySelfNameContainingAndUserIs(String selfName,User user);
}
