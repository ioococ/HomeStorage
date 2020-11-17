package cn.j.netstorage.Entity.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Setter
@Getter
@ToString
@Table(name = "t_role",uniqueConstraints = {@UniqueConstraint(columnNames="name")})
public class Role {

    @Id
    @GeneratedValue
    private long rid;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "status")
    private Boolean status;

}
