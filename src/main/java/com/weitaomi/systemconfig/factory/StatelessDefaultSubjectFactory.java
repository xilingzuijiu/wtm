package com.weitaomi.systemconfig.factory;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * Created by supumall on 2016/8/6.
 */
public class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory {
    @Override
    public Subject createSubject(SubjectContext subjectContext){
        subjectContext.setSessionCreationEnabled(false);
        return super.createSubject(subjectContext);
    }

}
