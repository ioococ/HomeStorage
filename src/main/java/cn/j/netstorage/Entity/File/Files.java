package cn.j.netstorage.Entity.File;

import cn.j.netstorage.Entity.Type;
import cn.j.netstorage.Entity.User.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "t_Files")
@Setter
@Getter
@ToString
public class Files extends File {

    @Id
    @GeneratedValue
    private long fid;

    @Column
    private String selfName;

    @Column
    private String parentName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "origin")
    private Set<OriginFile> originFile;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user")
    private List<User> user;

    @Column
    private short isDir;

    @Column
    private Date createDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "aria2File")
    private Set<Aria2File> aria2File;

    @Column
    private String type;


    public static short is_dir=1;
    public static short no_dir=0;

}
