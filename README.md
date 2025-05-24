# mybatis-plus-cy
A tool for simplifying query operations, which is made by cyp.
## Dependency
```
    <dependencies>
        <!-- mybatis-plus-cy-spring-boot-starter 就是核心依赖，依赖版本和mybatis-plus保持一致 -->
        <dependency>
            <groupId>io.github.caoyipeng1</groupId>
            <artifactId>mybatis-plus-cy-spring-boot-starter</artifactId>
            <version>3.5.9</version>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
            <version>3.5.9</version>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>9.1.0</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
            <version>3.4.4</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>3.4.4</version>
        </dependency>
    </dependencies>
```
## Example
* service
```java
@Service
public class TestUserServiceImpl extends ServiceImpl<TestUserMapper, TestUser> implements TestUserService {
    /**
     * TestUser表 与 TestUserAttribute表 的一对多联合查询
     */
    public List<Map<String, Object>> iGetO2M(){
        // 单表查询 TestUser表
        List<TestUser> testUserList = list();
        /*
        * 将单表查询结果testUserList被new JdbcArrayList<>()包裹，并调用o2m一对多方法;
        * o2m方法的 第一个参数是TestUser的表的关联字段Id，第二个参数是TestUserAttribute表的关联字段testUserId
        * 应为这里没有传入手动结果映射参数，所以会自动结果映射，映射结果为List<Map<String, Object>>,这个就是一对多的查询结果，不需要你再专门去为他编写Vo等来包装一对多结果；
        * */
        List<Map<String, Object>> rs1 = new JdbcArrayList<>(testUserList).o2m(TestUser::getId, TestUserAttribute::getTestUserId);
        return rs1;
    }
}
```

* entity
```java
/**
 * mybatis-plus-cy 框架已适配mybatis-plus注解，仍然你可以继续使用mybatis-plus的这些注解来实现 表 与 对象 的映射关系
 */
@TableName("test_user")
public class TestUser {
    @TableId
    private Long id;
    @TableField("user_name")
    private String userName;
    @TableField("create_time")
    private LocalDateTime createTime;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
 ```
```java
@TableName("test_user_attribute")
public class TestUserAttribute {
    @TableId
    private Long id;
    @TableField("test_user_id")
    private Long testUserId;
    @TableField("attribute_name")
    private String attributeName;
    public Long getId() {
        return id;
    }
    public Long getTestUserId() {
        return testUserId;
    }
    public String getAttributeName() {
        return attributeName;
    }
}
```
* mapper
```java
@Mapper
public interface TestUserAttributeMapper extends BaseMapper<TestUserAttribute> {
}
```
```java
@Mapper
public interface TestUserMapper extends BaseMapper<TestUser> {
}

```
* sql
```sql
insert into test_user (id, user_name, create_time)
values  (1, '张三', '2025-05-24 20:37:29'),
        (2, '李四', '2025-02-24 20:38:31'),
        (3, '王五', '2025-05-16 10:39:21');
```
```sql
insert into test_user_attribute (id, test_user_id, attribute_name)
values  (1, 1, '喜欢唱歌'),
        (2, 1, '性别男'),
        (3, 2, '喜欢游泳'),
        (4, 2, '性别女'),
        (5, 3, '喜欢画画'),
        (6, 3, '喜欢乒乓球'),
        (7, 3, '性别男');
```

## Usage
...