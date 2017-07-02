import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ALUTest {
	ALU alu = new ALU();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIntegerRepresentation1() {
		assertEquals("00011001", alu.integerRepresentation("25", 8));
	}

	@Test
	public void testIntegerRepresentation2() {
		assertEquals("11100111", alu.integerRepresentation("-25", 8));
	}

	@Test
	public void testIntegerRepresentation3() {
		assertEquals("00000000", alu.integerRepresentation("0", 8));
	}

	@Test
	public void testFloatRepresentation1() {
		assertEquals("011111111000000", alu.floatRepresentation("+Inf", 8, 6));
	}

	@Test
	public void testFloatRepresentation2() {
		assertEquals("0000000000000000000", alu.floatRepresentation("0.0", 8, 10));
	}

	@Test
	public void testFloatRepresentation3() {
		assertEquals("01000000101011000", alu.floatRepresentation("385.375", 8, 10));
	}

	@Test
	public void testFloatRepresentation4() {
		assertEquals("NaN", alu.floatRepresentation("NaN", 8, 10));
	}

	@Test
	public void testIeee754_1() {
		assertEquals("01000001110010110000000000000000", alu.ieee754("25.375", 32));
	}

	@Test
	public void testIeee754_2() {
		assertEquals("11000001110010110000000000000000", alu.ieee754("-25.375", 32));
	}

	@Test
	public void testIntegerTrueValue1() {
		assertEquals("25", alu.integerTrueValue("011001"));
	}

	@Test
	public void testIntegerTrueValue2() {
		assertEquals("-7", alu.integerTrueValue("111001"));
	}

	@Test
	public void testFloatTrueValue1() {
		assertEquals("+Inf", alu.floatTrueValue("01111111100000000", 8, 8));
	}

	@Test
	public void testFloatTrueValue2() {
		assertEquals("NaN", alu.floatTrueValue("01111111100000110", 8, 8));
	}

	@Test
	public void testFloatTrueValue3() {
		assertEquals("25.375", alu.floatTrueValue("01000001110010110", 8, 8));
	}

	//@Test
	public void testFloatTrueValue4() {
		assertEquals(-Math.pow(2.0, -149), alu.floatTrueValue("10000000000000000000000000000001", 8, 23));
	}

	//@Test
	public void testFloatTrueValue5() {
		assertEquals(Math.pow(2, -127), alu.floatTrueValue("00000000010000000", 8, 8));
	}

	@Test
	public void testNegation1() {
		assertEquals("1010101010", alu.negation("0101010101"));
	}

	@Test
	public void testNegation2() {
		assertEquals("000110010", alu.negation("111001101"));
	}

	@Test
	public void testLeftShift1() {
		assertEquals("11000000", alu.leftShift("00111100", 4));
	}

	@Test
	public void testLeftShift2() {
		assertEquals("00000000", alu.leftShift("00111100", 8));
	}

	@Test
	public void testLogRightShift() {
		assertEquals("00000011", alu.logRightShift("00111100", 4));
	}

	@Test
	public void testAriRightShift() {
		assertEquals("11111100", alu.ariRightShift("11001100", 4));
	}

	@Test
	public void testFullAdder1() {
		assertEquals("00", alu.fullAdder('0', '0', '0'));
	}

	@Test
	public void testFullAdder2() {
		assertEquals("01", alu.fullAdder('1', '0', '0'));
	}

	@Test
	public void testFullAdder3() {
		assertEquals("10", alu.fullAdder('0', '1', '1'));
	}

	@Test
	public void testClaAdder1() {
		assertEquals("10001", alu.claAdder("1010", "0110", '1'));
	}

	@Test
	public void testClaAdder2() {
		assertEquals("10010", alu.claAdder("1010", "0111", '1'));
	}

	@Test
	public void testOneAdder1() {
		assertEquals("0100110", alu.oneAdder("100101"));
	}

	@Test
	public void testOneAdder2() {
		assertEquals("0000000", alu.oneAdder("111111"));
	}

	@Test
	public void testAdder1() {
		assertEquals("000000001", alu.adder("1101", "0011", '1', 8));
	}

	@Test
	public void testAdder2() {
		assertEquals("11001", alu.adder("0101", "0011", '1', 4));
	}

	@Test
	public void testAdder3() {
		assertEquals("00000", alu.adder("1101", "0011", '0', 4));
	}

	@Test
	public void testIntegerAddition1() {
		assertEquals("10111", alu.integerAddition("1000", "1111", 4));
	}

	@Test
	public void testIntegerAddition2() {
		assertEquals("00010", alu.integerAddition("0110", "1100", 4));
	}

	@Test
	public void testIntegerAddition3() {
		assertEquals("000000111", alu.integerAddition("0100", "0011", 8));
	}

	@Test
	public void testIntegerSubtraction1() {
		assertEquals("000000101", alu.integerSubtraction("0100", "1111", 8));
	}

	@Test
	public void testIntegerSubtraction2() {
		assertEquals("000000001", alu.integerSubtraction("0100", "0011", 8));
	}

	@Test
	public void testIntegerSubtraction3() {
		assertEquals("011111001", alu.integerSubtraction("1000", "1111", 8));
	}

	@Test
	public void testSignedAddition1() {
		assertEquals("001110", alu.signedAddition("01111", "11", 4));
	}

	@Test
	public void testSignedAddition2() {
		assertEquals("100000", alu.signedAddition("01111", "01", 4));
	}

	@Test
	public void testSignedAddition3() {
		assertEquals("001010", alu.signedAddition("0011", "00111", 4));
	}

	@Test
	public void testSignedAddition4() {
		assertEquals("0100000111", alu.signedAddition("1100", "1011", 8));
	}

	@Test
	public void testIntegerMultiplication1() {
		assertEquals("000001100", alu.integerMultiplication("0011", "0100", 8));
	}

	@Test
	public void testIntegerMultiplication2() {
		assertEquals("111111100", alu.integerMultiplication("01111111", "0100", 8));
	}

	@Test
	public void testIntegerMultiplication3() {
		assertEquals("000000000", alu.integerMultiplication("0011", "0000", 8));
	}

	@Test
	public void testIntegerMultiplication4() {
		assertEquals("011101110", alu.integerMultiplication("1101", "0110", 8));
	}

	@Test
	public void testIntegerDivision1() {
		assertEquals("011101111", alu.integerDivision("1001", "0011", 4));
	}

	@Test
	public void testIntegerDivision2() {
		assertEquals("01111111011111111", alu.integerDivision("1001", "0011", 8));
	}

	@Test
	public void testIntegerDivision3() {
		assertEquals("000110000", alu.integerDivision("0110", "0010", 4));
	}

	@Test
	public void testFloatAddition1() {
		assertEquals("000111011000001000", alu.floatAddition("00111011000001000", "00000000000000000", 8, 8, 8));
	}

	@Test
	public void testFloatAddition2() {
		assertEquals("000111011100000110", alu.floatAddition("00111011000001000", "00111011000000100", 8, 8, 8));
	}

	@Test
	public void testFloatAddition3() {
		assertEquals("010111011100000110", alu.floatAddition("10111011000001000", "10111011000000100", 8, 8, 8));
	}

	@Test
	public void testFloatAddition4() {
		assertEquals("000000000100000010", alu.floatAddition("00000000100000010", "10000000100000000", 8, 8, 8));
	}

	@Test
	public void testFloatAddition5() {
		assertEquals("000111010100000000", alu.floatAddition("00111011010000000", "10111011000000000", 8, 8, 8));
	}

	@Test
	public void testFloatSubtraction1() {
		assertEquals("010111011100000110", alu.floatSubtraction("10111011000001000", "00111011000000100", 8, 8, 8));
	}

	@Test
	public void testFloatSubtraction2() {
		assertEquals("000111011100000110", alu.floatSubtraction("00111011000001000", "10111011000000100", 8, 8, 8));
	}

	@Test
	public void testFloatSubtraction3() {
		assertEquals("000111011100000110", alu.floatSubtraction("00111011000001000", "10111011000000100", 8, 8, 8));
	}

	@Test
	public void testFloatMultiplication1() {
		assertEquals("000111110011000000", alu.floatMultiplication("00111111000000000", "00111110111000000", 8, 8));
	}

	@Test
	public void testFloatMultiplication2() {
		assertEquals("100000000000000000", alu.floatMultiplication("00111111000000000", "00000000010000000", 8, 8));
	}

	@Test
	public void testFloatMultiplication3() {
		assertEquals("100000000000000000", alu.floatMultiplication("00000000000000100", "00000000000000100", 8, 8));
	}

	@Test
	public void testFloatDivision1() {
		assertEquals("000111111011000000", alu.floatDivision("00111110111000000", "00111111000000000", 8, 8));
	}

	@Test
	public void testFloatDivision2() {
		assertEquals("000000000000000000", alu.floatDivision("00000000100000000", "00" + "000000010000000", 8, 8));
	}

	// @Test
	public void testFloatDivision3() {
		assertEquals("001000000000000000", alu.floatDivision("00000000100000000", "00" + "000000001000000", 8, 8));
	}
}
