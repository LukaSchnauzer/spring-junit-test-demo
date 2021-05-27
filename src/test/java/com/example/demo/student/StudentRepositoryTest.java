package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository testSubject;

    //Clear in-memory db after each test
    @AfterEach
    void tearDown(){
        testSubject.deleteAll();
    }

    @Test
    void testEmailExists() {
        // given
        String email = "test@gmail.com";
        Student student = new Student(
                "test",
                email,
                Gender.MALE
        );
        testSubject.save(student);

        // when
        Boolean exists = testSubject.selectExistsEmail(email);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void testEmailDoesNotExists() {
        // given
        String email = "test@gmail.com";

        // when
        Boolean exists = testSubject.selectExistsEmail(email);

        // then
        assertThat(exists).isFalse();
    }
}