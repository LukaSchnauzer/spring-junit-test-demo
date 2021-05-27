package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    private StudentService testSubject;

    /*
    //This is done by annotation @ExtendWith(MockitoExtension.class)
    private AutoCloseable autoCloseable;

    @AfterEach
    void tearDown() throws Exception {autoCloseable.close();}
    */

    @BeforeEach
    void setUp(){
        testSubject = new StudentService(studentRepository);
    }

    @Test
    void getAllStudentsTest() {
        // when
        testSubject.getAllStudents();

        // then
        // Don't want to test StudentRepository while testing StudentService, because it was tested before
        // instead we just verify if the method used findAll from StudentRepository, which is already tested
        verify(studentRepository).findAll();
    }

    @Test
    void testAddStudent() {
        // given
        Student student = new Student(
                "test",
                "test@gmail.com",
                Gender.MALE
        );

        // when
        testSubject.addStudent(student);

        // then
        // verify that the StudentRepository method save was called with the same Student object
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        // Capture Student
        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void testAddStudentEmailTaken() {
        // given
        Student student = new Student(
                "test",
                "test@gmail.com",
                Gender.MALE
        );

        // Ensure that StudentRepository will return true to selectExistsEmail
        given(studentRepository.selectExistsEmail(anyString())).willReturn(true);

        // Test specific Exception is thrown
       assertThatThrownBy(() -> testSubject.addStudent(student))
               .isInstanceOf(BadRequestException.class)
               .hasMessageContaining("Email " + student.getEmail() + " taken");

       // Test that StudentRepository never tried to save the Student
       verify(studentRepository, never()).save(any());
    }


    @Test
    void deleteStudent() {
        // given
        Long studentId = 3L;

        given(studentRepository.existsById(anyLong())).willReturn(true);

        // when
        testSubject.deleteStudent(studentId);

        // then
        ArgumentCaptor<Long> studentIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(studentRepository).deleteById(studentIdArgumentCaptor.capture());

        Long capturedStudentId = studentIdArgumentCaptor.getValue();
        assertThat(capturedStudentId).isEqualTo(studentId);
    }

    @Test
    void deleteStudentIdDoesntExists() {
        Long studentId = 3L;

        given(studentRepository.existsById(anyLong())).willReturn(false);

        assertThatThrownBy(() -> testSubject.deleteStudent(studentId))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + studentId + " does not exists");

        verify(studentRepository, never()).deleteById(anyLong());
    }
}