package com.mfg.auth.admin.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.support.LdapNameBuilder;

import javax.naming.Name;

@Entry(base = "ou=people", objectClasses = {"organizationalPerson","person","top"})
@Data
public class Person {

    @Id
    @JsonIgnore
    private Name dn;


    @Attribute(name = "cn")
    private String cn;

    @Attribute(name = "sn")
    private String sn;

    private String userPassword;

    public Person(String cn) {
        Name dn = LdapNameBuilder.newInstance()
                .add("ou", "people")
                .add("cn", cn)
                .build();
        this.dn = dn;
    }

    public Person(){}

    public void setCn(String cn) {
        this.cn = cn;
        if(this.dn==null){
            Name dn = LdapNameBuilder.newInstance()
                    .add("ou", "people")
                    .add("cn", cn)
                    .build();
            this.dn = dn;
        }
    }
}
