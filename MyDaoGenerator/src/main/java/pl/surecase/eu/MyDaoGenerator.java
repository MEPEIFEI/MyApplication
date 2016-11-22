package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {


    public static void main(String args[]) throws Exception {
        Schema schema = new Schema( 1, "com.xxx.bean");
        // 1: 数据库版本号
        // com.xxx.bean:自动生成的Bean对象会放到/java-gen/com/xxx/bean中

        schema.setDefaultJavaPackageDao("com.xxx.dao");
        // DaoMaster.java、DaoSession.java、BeanDao.java会放到/java-gen/com/xxx/dao中

        // 上面这两个文件夹路径都可以自定义，也可以不设置

        initUserBean(schema); // 初始化Bean了

        new DaoGenerator().generateAll(schema, args[0]);// 自动创建
    }

    private static void initUserBean(Schema schema) {
        Entity userBean = schema.addEntity("UserBean");// 表名
        userBean.setTableName("user"); // 可以对表重命名
        userBean.addStringProperty("id").primaryKey().index();// 主键，索引
        userBean.addStringProperty("phone");
        userBean.addStringProperty("profile_picture");
        userBean.addStringProperty("client_id");
        userBean.addStringProperty("name");
        userBean.addStringProperty("location");
        userBean.addStringProperty("gender");
    }
}
