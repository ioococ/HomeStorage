package cn.j.netstorage.Entity.Vo;

import cn.j.netstorage.Entity.File.Files;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class FilesVo {

    @NotNull
    @Length(max = 100,min = 1)
    private String parentName;
    @NotNull
    @Length(max = 100,min = 1)
    private String selfName;
    private Short is_Dir;
    public FilesVo(Files files){
        is_Dir=files.getIsDir();
    }
}
