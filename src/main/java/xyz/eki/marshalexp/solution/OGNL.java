package xyz.eki.marshalexp.solution;

import ognl.Ognl;
import ognl.OgnlContext;

public class OGNL {
    static class SchoolMaster{
        String name = "wanghua";
        public SchoolMaster(String name){
            this.name = name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class School
    {
        String name = "tsinghua";
        SchoolMaster schoolMaster;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setSchoolMaster(SchoolMaster schoolMaster) {
            this.schoolMaster = schoolMaster;
        }

        public SchoolMaster getSchoolMaster() {
            return schoolMaster;
        }
    }

    static class Student{
        String name = "xiaoming";
        School school;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setSchool(School school) {
            this.school = school;
        }

        public School getSchool() {
            return school;
        }
    }

    public static void main(String[] args) throws Exception{
        School school = new School();
        school.setName("tsinghua");
        school.setSchoolMaster(new SchoolMaster("wanghua"));
        Student student1 = new Student();
        student1.setName("xiaoming");
        student1.setSchool(school);
        Student student2 = new Student();
        student2.setName("zhangsan");
        student2.setSchool(school);

// 创建上下文环境
        OgnlContext context = new OgnlContext();
// 设置跟对象root
        context.setRoot(student1);
        context.put("student2", student2);
// 获取ognl的root相关值
//        Object name1 = Ognl.getValue("name", context, context.getRoot());
//        Object school1 = Ognl.getValue("school.name", context, context.getRoot());
//        Object schoolMaster1 = Ognl.getValue("school.schoolMaster.name", context, context.getRoot());
//        System.out.println(name1 + ":学校-" + school1 + ",校长-"+schoolMaster1);
//// 获取ognl非root相关值
//        Object name2 = Ognl.getValue("#student2.name", context, context.getRoot());
//        Object school2 = Ognl.getValue("#student2.school.name", context, context.getRoot());
//        Object schoolMaster2 = Ognl.getValue("#student2.school.schoolMaster.name", context, context.getRoot());
//        System.out.println(name2 + ":学校-" + school2 + ",校长-"+schoolMaster2);
        //@java.lang.Character@toString(98)


        Object value = Ognl.getValue("@java.lang.Character@toString(114).concat(@java.lang.Character@toString(113))",context,context.getRoot());
        System.out.println(value);
    }
}
