package com.ynu;

import com.ynu.entity.Personnel;
import com.ynu.repository.PersonnelRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // 每个 Test 结束后自动回滚事务
public class PersonnelRepositoryTest {
    @Resource
    private PersonnelRepository personnelRepository;

    @Test
    public void testSavePersonnel() {
        Personnel person = new Personnel();
        person.setName("张三");
        person.setAge(24);
        person.setGender("男");
        person.setPosition("工程师");
        person.setPhone("1311111111");
        person.setEmail("1@example.com");

        Personnel savedPerson = personnelRepository.save(person);

        //断言验证
        assertNotNull(savedPerson.getId(), "ID不应为空");
        assertEquals("张三", savedPerson.getName());
    }

    @Test
    public void testFindPersonnel() {
        Personnel person = new Personnel();
        person.setName("李四");
        personnelRepository.save(person);

        List<Personnel> list = personnelRepository.findAll();

        assertTrue(list.size() > 0);
    }

    @Test
    public void testUpdatePersonnel() {
        Personnel person = new Personnel();
        person.setName("王五");
        person.setPosition("前端");
        Personnel saved = personnelRepository.save(person);

        saved.setPosition("后端");
        Personnel updated = personnelRepository.save(saved);

        assertEquals("后端", updated.getPosition());
    }

    @Test
    public void testDeletePersonnel() {
        Personnel person = new Personnel();
        person.setName("赵六");
        Personnel saved = personnelRepository.save(person);

        personnelRepository.deleteById(saved.getId());

        Optional<Personnel> result = personnelRepository.findById(saved.getId());
        assertFalse(result.isPresent(), "未删除成功");
    }
}
