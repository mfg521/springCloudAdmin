package com.mfg.auth.admin.biz;


import com.mfg.auth.admin.vo.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class OdmPersonRepo {

    @Autowired
    private LdapTemplate ldapTemplate;

    public Person create(Person person){
        ldapTemplate.create(person);
        return person;
    }

    public Person findByUid(String uid){return ldapTemplate.findOne(query().where("uid").is(uid),Person.class);}

    public Person findByCn(String cn){
        return ldapTemplate.findOne(query().where("cn").is(cn),Person.class);
    }

    public Person modifyPerson(Person person){
        ldapTemplate.update(person);
        return person;
    }

    public void deletePerson(Person person){
        ldapTemplate.delete(person);
    }



}
