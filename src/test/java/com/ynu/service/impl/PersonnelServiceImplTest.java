package com.ynu.service.impl;

import com.ynu.dto.Condition;
import com.ynu.entity.Personnel;
import com.ynu.service.PersonnelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PersonnelServiceImplTest {

    @Autowired
    private PersonnelService personnelService;

    @Test
    void testBasicCrud() {
        // 新增
        Personnel p = new Personnel();
        p.setName("张三");
        p.setAge(25);
        p.setGender("男");
        p.setPosition("工程师");
        p.setPhone("13800000001");
        p.setEmail("1@example.com");

        personnelService.save(p);

        // JPA 保存后，ID 会自动回填到对象中
        assertNotNull(p.getId(), "保存后 ID 不应为空");
        Long savedId = p.getId();

        // 简单查询
        // 根据姓名和职位模糊查询
        List<Personnel> list = personnelService.search("张", "开发");
        assertFalse(list.isEmpty(), "未查到刚才保存的数据");
        assertEquals("张三", list.get(0).getName());

        // 修改
        p.setName("张三丰"); // 修改名字
        p.setAge(100);     // 修改年龄
        personnelService.update(p);

        // 验证修改结果（通过再次查询）
        List<Personnel> updatedList = personnelService.search("张三丰", null);
        assertEquals(1, updatedList.size());
        assertEquals(100, updatedList.get(0).getAge());

        // 删除
        personnelService.delete(savedId);

        // 验证删除结果
        List<Personnel> deletedList = personnelService.search("张三丰", null);
        assertTrue(deletedList.isEmpty(), "删除后不应再查到数据");
    }


    //测试高级查询
    @Test
    void testAdvancedSearch() {
        createPersonnel("张三", 20, "实习");
        createPersonnel("李四", 30, "前端");
        createPersonnel("王五", 40, "后端");

        // 测试等于 (eq) 和 逻辑与 (AND)
        // 查询：职位 = '前端'
        List<Condition> conditions1 = new ArrayList<>();
        Condition c1 = new Condition();
        c1.setField("position");
        c1.setOperator("eq");
        c1.setValue("前端");
        c1.setLogic("and");
        conditions1.add(c1);

        List<Personnel> result1 = personnelService.searchAdvanced(conditions1);
        assertEquals(1, result1.size());
        assertEquals("李四", result1.get(0).getName());

        // 测试大于 (gt) 针对 Age 字段
        // 查询：Age > 35
        List<Condition> conditions2 = new ArrayList<>();
        Condition c2 = new Condition();
        c2.setField("age");
        c2.setOperator("gt");
        c2.setValue("35");
        c2.setLogic("and");
        conditions2.add(c2);

        List<Personnel> result2 = personnelService.searchAdvanced(conditions2);
        assertEquals(1, result2.size());
        assertEquals("王五", result2.get(0).getName());

        // 测试逻辑或 (OR)
        // 查询：Name like '张三' OR Age > 35
        List<Condition> conditions3 = new ArrayList<>();

        Condition c3a = new Condition();
        c3a.setField("name");
        c3a.setOperator("like");
        c3a.setValue("张三");
        c3a.setLogic("and");
        conditions3.add(c3a);

        Condition c3b = new Condition();
        c3b.setField("age");
        c3b.setOperator("gt");
        c3b.setValue("35");
        c3b.setLogic("or");
        conditions3.add(c3b);

        List<Personnel> result3 = personnelService.searchAdvanced(conditions3);
        // 期望查到 张三 (20岁) 和 王五 (40岁)
        assertEquals(2, result3.size());
    }

    private void createPersonnel(String name, Integer age, String position) {
        Personnel p = new Personnel();
        p.setName(name);
        p.setAge(age);
        p.setPosition(position);
        p.setGender("男");
        personnelService.save(p);
    }
}
