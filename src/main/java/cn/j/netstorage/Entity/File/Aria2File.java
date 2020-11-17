package cn.j.netstorage.Entity.File;

import cn.j.netstorage.Entity.User.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@ToString
@Table(name = "t_aria2")
@Entity
public class Aria2File extends File {
    @Id
    String gid;

    @Column
    String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user")
    List<User> user;

}
