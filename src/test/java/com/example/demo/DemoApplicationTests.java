package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DemoApplicationTests {

	Calculator testSubject = new Calculator();

	@Test
	void itShouldAddNumbers() {
		// given
		int numberOne = 10;
		int numberTwo = 7;

		// when
		int result = testSubject.add(numberOne, numberTwo);

		//then
		int expected = 17;
		assertThat(result).isEqualTo(expected);
	}

	class Calculator {
		int add(int a, int b){ return a + b; }
	}
}
