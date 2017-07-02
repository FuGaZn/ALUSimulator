import java.math.BigInteger;

/**
 * 模拟ALU进行整数和浮点数的四则运算
 * 
 * @author [161250025，伏家兴] .
 */

public class ALU {

	/**
	 * 生成十进制整数的二进制补码表示。<br/>
	 * 例：integerRepresentation("9", 8)
	 * 
	 * @param number
	 *            十进制整数。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length
	 *            二进制补码表示的长度
	 * @return number的二进制补码表示，长度为length
	 */
	public String integerRepresentation(String number, int length) {

		StringBuilder sb = new StringBuilder(length);
		int num = Integer.parseInt(number);
		for (int i = 0; i < length; i++) {
			sb.insert(0, num & 1);
			num = num >> 1;
		}
		return sb.toString();
	}

	/**
	 * 生成十进制浮点数的二进制表示。 需要考虑 0、反规格化、正负无穷（“+Inf”和“-Inf”）、 NaN等因素，具体借鉴 IEEE 754。
	 * 舍入策略为向0舍入。<br/>
	 * 例：floatRepresentation("11.375", 8, 11)
	 * 
	 * @param number
	 *            十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @return number的二进制表示，长度为 1+eLength+sLength。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String floatRepresentation(String number, int eLength, int sLength) {
		String eString = "";
		String sString = "";
		String result = "";
		int signBit = 0;

		// 符号位
		if (number.charAt(0) == '-') {
			signBit = 1;
		}
		if (number.equals("NaN")) {
			return "NaN";
		} else if (number.equals("+Inf") || number.equals("-Inf")) {
			for (int i = 0; i < eLength; i++) {
				eString = eString + "1";
			}
			for (int i = 0; i < sLength; i++) {
				sString = sString + "0";
			}
		} else if (Double.parseDouble(number) == 0) {
			for (int i = 0; i < eLength; i++) {
				eString = eString + "0";
			}
			for (int j = 0; j < sLength; j++) {
				sString = sString + "0";
			}
		} else {
			if (number.substring(0, 1).equals("-")) {
				number = number.substring(1);
			}
			int index = -1;// .的索引

			for (int i = 0; i < number.length(); i++) {
				if (number.substring(i, i + 1).equals(".")) {
					index = i;
					break;
				}
			}
			String str = number;
			if (index > 0) {
				str = number.substring(0, index);
			} else if (index == 0) {// 如果.是第一位
				str = "0";
			}

			int num = Integer.parseInt(str);
			String eStr = ""; // 整数部分的二进制
			int eNum = 0;
			int eNum2 = 0;
			eString = "";
			if (num > 0) {
				do {
					eStr = num % 2 + eStr;
					num = num / 2;
				} while (num != 0);

				eNum = receivePowerOf2(eLength - 1) + eStr.length() - 2; // 阶码值
				eNum2 = eNum;
				do {
					eString = eNum % 2 + eString;
					eNum = eNum / 2;
				} while (eNum != 0);
				if (eString.length() < eLength) { // 求出阶码
					for (int i = 0; i < eLength - eString.length() + 1; i++) {
						eString = "0" + eString;
					}
				}
			}
			// 求尾数
			if (index > 0) {

				String str2 = "0." + number.substring(index + 1, number.length());
				int eNum3 = 0;
				double num2 = Double.parseDouble(str2);
				// 求小数

				do {
					num2 = num2 * 2;
					sString = sString + String.valueOf(String.valueOf(num2).split("")[0]);
					num2 = num2 - Integer.parseInt(String.valueOf(num2).substring(0, 1));
				} while (num2 != 0);

				if (eNum2 > 0) {
					sString = eStr.substring(1, eStr.length()) + sString;
				} else {
					int index2 = -1;
					for (int i = 0; i < sString.length(); i++) {
						if (sString.substring(i, i + 1).equals("1")) {
							index2 = i;
							break;
						}
					}
					eNum = Math.abs(receivePowerOf2(eLength - 1) - 2 - index2);
					eNum3 = eNum;
					eString = "";
					do {
						eString = eNum % 2 + eString;
						eNum = eNum / 2;
					} while (eNum != 0);

					if (eString.length() < eLength) {
						while (eString.length() < eLength) { // 求出阶码
							eString = "0" + eString;
						}
					} else {
						eString = eString.substring(eString.length() - eLength, eString.length());
					}

					if (sString.length() > 2) {
						sString = sString.substring(2, sString.length());
					} else {
						sString = "";
					}
				}
				if (sString.length() <= sLength) {
					while (sString.length() < sLength) {
						sString = sString + "0";
					}
				} else {
					sString = sString.substring(sString.length() - sLength, sString.length());
					while (sString.charAt(0) == '0') {
						if (eNum3 > 0) {
							eNum3--;
							sString = sString.substring(1) + "0";
						} else {
							break;
						}
					}
					eString = "";
					do {
						eString = eNum3 % 2 + eString;
						eNum = eNum3 / 2;
					} while (eNum3 != 0);

					if (eString.length() < eLength) {
						while (eString.length() < eLength) { // 求出阶码
							eString = "0" + eString;
						}
					} else {
						eString = eString.substring(eString.length() - eLength, eString.length());
					}
				}

			} else {
				sString = eStr.substring(1, eStr.length());
				if (sString.length() <= sLength) {
					while (sString.length() < sLength) {
						sString = sString + "0";
					}
				} else {
					sString = sString.substring(0, sLength);
				}
			}
		}
		result = signBit + eString + sString;
		return result;

	}

	/**
	 * 生成十进制浮点数的IEEE 754表示，要求调用{@link #floatRepresentation(String, int, int)
	 * floatRepresentation}实现。<br/>
	 * 例：ieee754("11.375", 32)
	 * 
	 * @param number
	 *            十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length
	 *            二进制表示的长度，为32或64
	 * @return number的IEEE 754表示，长度为length。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String ieee754(String number, int length) {
		String str = "";
		if (length == 32) {
			str = floatRepresentation(number, 8, 23);
		} else if (length == 64) {
			str = floatRepresentation(number, 11, 52);
		}
		return str;
	}

	/**
	 * 计算二进制补码表示的整数的真值。<br/>
	 * 例：integerTrueValue("00001001")
	 * 
	 * @param operand
	 *            二进制补码表示的操作数
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 */
	public String integerTrueValue(String operand) {
		int result = 0;
		String[] operandStr = operand.split("");
		int b = 0;

		for (int j = 1; j < operandStr.length; j++) {
			if (operandStr[j].equals("1")) {
				b++;
			}
		}
		if (b == 0) {
			result = 0;
		} else {
			if (operandStr[0].equals("0")) {
				for (int i = 1; i < operandStr.length; i++) {
					if (Integer.parseInt(operandStr[i]) != 0) {
						result = receivePowerOf2(operand.length() - 1 - i) + result;
					}
				}
			} else {
				operand = negation(operand);
				operandStr = operand.split("");
				for (int i = 1; i < operandStr.length; i++) {
					if (Integer.parseInt(operandStr[i]) != 0) {
						result = receivePowerOf2(operand.length() - 1 - i) + result;
					}
				}
				result = -(result + 1);
			}
		}
		String str = String.valueOf(result);
		return str;
	}

	/**
	 * 求2的n次方(n>=0)
	 * 
	 * @param n
	 * @return
	 */
	public int receivePowerOf2(int n) {
		int result = 1;
		if (n == 0) {
			result = 1;
		} else if (n > 0) {
			for (int i = 0; i < n; i++) {
				result = result * 2;
			}
		}
		return result;
	}

	/**
	 * 求2的n次方(n<0)
	 * 
	 * @param n
	 * @return
	 */
	public double receivePowerOf2I(int n) {
		double result = 1;

		for (int i = n; i < 0; i++) {
			result = result / 2;
		}
		return result;
	}

	/**
	 * 求小数的真值
	 * 
	 * @param operand
	 * @return
	 */
	public double decimalsTrueValue(String operand) {
		double result = 0;
		String[] op = operand.split("");
		for (int i = 0; i < operand.length(); i++) {
			result = result + Math.pow(2, -(i + 1)) * Integer.parseInt(op[i]);
		}
		return result;
	}

	/**
	 * 计算二进制原码表示的浮点数的真值。<br/>
	 * 例：floatTrueValue("01000001001101100000", 8, 11)
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位。正负无穷分别表示为“+Inf”和“-Inf”，
	 *         NaN表示为“NaN”
	 */

	public String floatTrueValue(String operand, int eLength, int sLength) {

		String eString = operand.substring(1, eLength + 1);
		String sString = operand.substring(eLength + 1, operand.length());
		String result = "";
		if (operand.substring(0, 1).equals("1")) {
			result = "-";
		}

		// 正负无穷、非数、0
		String eInf = "";
		for (int i = 0; i < eLength; i++) {
			eInf = eInf + "1";
		}
		String eInf2 = "";
		for (int i = 0; i < eLength; i++) {
			eInf2 = eInf2 + "0";
		}
		String sInf1 = "";
		for (int i = 0; i < sLength; i++) {
			sInf1 = sInf1 + "0";
		}

		if (eString.equals(eInf) && sString.equals(sInf1)) {
			if (result.equals("-")) {
				result = "-Inf";
			} else {
				result = "+Inf";
			}
		} else if (eString.equals(eInf2) && sString.equals(sInf1)) {
			if (operand.substring(0, 1).equals("1")) {
				result = "-0.0";
			} else
				result = "0.0";
		} else if (eString.equals(eInf) && !sString.equals(sInf1)) {
			result = "NaN";
		} else {
			// 指数
			int eValue = Integer.parseInt(integerTrueValue("0" + eString)) - receivePowerOf2(eLength - 1) + 1;
			String Znum = "";
			String Xnum = "";// 小数部分
			int sZvalue = 0;// 整数值
			double sXvalue = 0; // 小数值

			if (eValue >= 0) {
				Znum = "1" + sString.substring(0, eValue);
				sZvalue = Integer.parseInt(integerTrueValue("0" + Znum));
				Xnum = sString.substring(eValue, sString.length());
				sXvalue = decimalsTrueValue(Xnum);
			} else {
				if (eString.contains("1")) {
					Xnum = "1" + sString;
				} else {
					Xnum = sString;
				}
				for (int i = 1; i < Math.abs(eValue); i++) {
					Xnum = "0" + Xnum;
				}
				sXvalue = decimalsTrueValue(Xnum);
			}
			result = result + String.valueOf(sZvalue + sXvalue);
		}
		return result;
	}

	/**
	 * 按位取反操作。<br/>
	 * 例：negation("00001001")
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @return operand按位取反的结果
	 */
	public String negation(String operand) {
		String[] str = operand.split("");
		String result = "";

		for (int i = 0; i < str.length; i++) {
			if (str[i].equals("1")) {
				str[i] = "0";
			} else if (str[i].equals("0")) {
				str[i] = "1";
			}
			result = result + str[i];
		}

		return result;

	}

	/**
	 * 左移操作。<br/>
	 * 例：leftShift("00001001", 2)
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @param n
	 *            左移的位数
	 * @return operand左移n位的结果
	 */
	public String leftShift(String operand, int n) {
		String result = operand;
		for (int i = 0; i < n; i++) {
			operand = operand + "0";
		}
		result = operand.substring(n, operand.length());

		return result;
	}

	/**
	 * 逻辑右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @param n
	 *            右移的位数
	 * @return operand逻辑右移n位的结果
	 */
	public String logRightShift(String operand, int n) {
		String result = operand;
		for (int i = 0; i < n; i++) {
			operand = "0" + operand;
		}
		result = operand.substring(0, operand.length() - n);

		return result;

	}

	/**
	 * 算术右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * 
	 * @param operand
	 *            二进制表示的操作数
	 * @param n
	 *            右移的位数
	 * @return operand算术右移n位的结果
	 */
	public String ariRightShift(String operand, int n) {

		String str = "";
		if (operand.length() >= n) {
			str = operand.substring(0, operand.length() - n);
		} else {
			n = operand.length();
		}
		if (operand.charAt(0) == '0') {
			for (int i = 0; i < n; i++) {
				str = "0" + str;
			}
		} else {
			for (int i = 0; i < n; i++) {
				str = "1" + str;
			}
		}
		return str;

	}

	/**
	 * 全加器，对两位以及进位进行加法运算。<br/>
	 * 例：fullAdder('1', '1', '0')
	 * 
	 * @param x
	 *            被加数的某一位，取0或1
	 * @param y
	 *            加数的某一位，取0或1
	 * @param c
	 *            低位对当前位的进位，取0或1
	 * @return 相加的结果，用长度为2的字符串表示，第1位表示进位，第2位表示和
	 */
	public String fullAdder(char x, char y, char c) {
		int F = 0;
		int Cout = 0;
		int x2 = x - '0';
		int y2 = y - '0';
		int c2 = c - '0';
		F = (x2) ^ (y2) ^ (c2);
		Cout = (x2 & c2) | (y2 & c2) | (x2 & y2);
		return String.valueOf(Cout) + String.valueOf(F);
	}

	/**
	 * 4位先行进位加法器。要求采用{@link #fullAdder(char, char, char) fullAdder}来实现<br/>
	 * 例：claAdder("1001", "0001", '1')
	 * 
	 * @param operand1
	 *            4位二进制表示的被加数
	 * @param operand2
	 *            4位二进制表示的加数
	 * @param c
	 *            低位对当前位的进位，取0或1
	 * @return 长度为5的字符串表示的计算结果，其中第1位是最高位进位，后4位是相加结果，其中进位不可以由循环获得
	 */
	public String claAdder(String operand1, String operand2, char c) {

		String resultStr = "";
		for (int i = operand1.length() - 1; i >= 0; i--) {
			String s = fullAdder(operand1.charAt(i), operand2.charAt(i), c);
			c = s.charAt(0);
			resultStr = s.charAt(1) + resultStr;
		}
		resultStr = c + resultStr;
		return resultStr;
	}

	/**
	 * 加一器，实现操作数加1的运算。 需要采用与门、或门、异或门等模拟，
	 * 不可以直接调用{@link #fullAdder(char, char, char) fullAdder}、
	 * {@link #claAdder(String, String, char) claAdder}、
	 * {@link #adder(String, String, char, int) adder}、
	 * {@link #integerAddition(String, String, int) integerAddition}方法。<br/>
	 * 例：oneAdder("00001001")
	 * 
	 * @param operand
	 *            二进制补码表示的操作数
	 * @return operand加1的结果，长度为operand的长度加1，其中第1位指示是否溢出（溢出为1，否则为0），其余位为相加结果
	 */
	public String oneAdder(String operand) {
		StringBuilder sb = new StringBuilder(operand.length() + 1);
		int F = 0;
		int C = 1;

		for (int i = operand.length() - 1; i >= 0; i--) {
			F = (operand.charAt(i) - '0') ^ C;
			sb.insert(0, F);
			C = (operand.charAt(i) - '0') & C;
		}

		if (operand.charAt(0) == '0' && !operand.substring(1).contains("0")) {
			return "1" + sb.toString();
		}
		return "0" + sb.toString();
	}

	/**
	 * 加法器，要求调用{@link #claAdder(String, String, char)}方法实现。<br/>
	 * 例：adder("0100", "0011", ‘0’, 8)
	 * 
	 * @param operand1
	 *            二进制补码表示的被加数
	 * @param operand2
	 *            二进制补码表示的加数
	 * @param c
	 *            最低位进位
	 * @param length
	 *            存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String adder(String operand1, String operand2, char c, int length) {

		if (operand1.length() <= length && operand2.length() <= length) {

			operand1 = addLength(operand1, length);
			operand2 = addLength(operand2, length);
			String number = claAdder(operand1, operand2, c);

			String result = number.substring(1, number.length());

			if ((operand1.charAt(0) == operand2.charAt(0)) && (operand1.charAt(0) != result.charAt(0)))
				result = "1" + result;
			else
				result = "0" + result;
			return result;
		} else {
			return "Error! operand.length()>length";
		}
	}

	/**
	 * 将字符串补足长度
	 * 
	 * @param operand
	 * @param length
	 * @return
	 */
	public String addLength(String operand, int length) {
		String result = operand;
		if (operand.length() < length) {
			for (int i = 0; i < length - operand.length(); i++) {
				result = operand.substring(0, 1) + result;
			}
		}
		return result;
	}

	/**
	 * 整数加法，要求调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerAddition("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            二进制补码表示的被加数
	 * @param operand2
	 *            二进制补码表示的加数
	 * @param length
	 *            存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */

	public String integerAddition(String operand1, String operand2, int length) {
		String st = adder(operand1, operand2, '0', length);
		return st;
	}

	/**
	 * 整数减法，可调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerSubtraction("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            二进制补码表示的被减数
	 * @param operand2
	 *            二进制补码表示的减数
	 * @param length
	 *            存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相减结果
	 */

	public String integerSubtraction(String operand1, String operand2, int length) {

		String str = operand2;
		operand2 = oneAdder(negation(operand2));
		operand2 = operand2.substring(1, operand2.length());
		String result = adder(operand1, operand2, '0', length);

		if (operand1.substring(0, 1).equals(str.substring(0, 1))) {
			result = "0" + result.substring(1, result.length());
		}
		return result;
	}

	/**
	 * 整数乘法，使用Booth算法实现，可调用{@link #adder(String, String, char, int)
	 * adder}等方法。<br/>
	 * 例：integerMultiplication("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            二进制补码表示的被乘数
	 * @param operand2
	 *            二进制补码表示的乘数
	 * @param length
	 *            存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的相乘结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相乘结果
	 */ 
	public String integerMultiplication(String operand1, String operand2, int length) {

		String operand11 = addLength(operand1, length);
		String operand22 = addLength(operand2, length);

		String signBit = "0";
		String product = operand22 + "0"; // 积
		for (int i = 0; i < length; i++)
			product = "0" + product;

		for (int i = 0; i < length; i++) {
			String part = product.substring(0, length); // 部分积
			// 比较P0和P1的值

			if (product.charAt(2 * length) - product.charAt(2 * length - 1) == 1) { // "01"
				part = integerAddition(part, operand11, length).substring(1);

			} else if (product.charAt(2 * length) - product.charAt(2 * length - 1) == -1) {// "10
				part = integerSubtraction(part, operand11, length).substring(1);
			}

			product = part + product.substring(length);
			product = ariRightShift(product, 1);
		}

		String result = product.substring(length, 2 * length);
		// String str = product.substring(0, length);
		int a = Integer.parseInt(integerTrueValue(operand1));
		int b = Integer.parseInt(integerTrueValue(operand2));

		if (Integer.parseInt(integerTrueValue(result)) != a * b) {
			signBit = "1";
		}

		return signBit + result;
	}

	/**
	 * 整数的不恢复余数除法，可调用{@link #adder(String, String, char, int) adder}等方法实现。<br/>
	 * 例：integerDivision("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            二进制补码表示的被除数
	 * @param operand2
	 *            二进制补码表示的除数
	 * @param length
	 *            存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为2*length+1的字符串表示的相除结果，其中第1位指示是否溢出（溢出为1，否则为0），其后length位为商，最后length位为余数
	 */
	public String integerDivision(String operand1, String operand2, int length) {
		// 除数为0的情况
		if (Integer.parseInt(integerTrueValue(operand2)) == 0) {
			return "NaN";
		} else if (Integer.parseInt(integerTrueValue(operand1)) == 0
				&& Integer.parseInt(integerTrueValue(operand2)) != 0) {
			String result = "";
			for (int i = 0; i < 2 * length + 1; i++) {
				result += "0";
			}
			return result;
		}

		if (operand1.charAt(0) == '1') {
			int la = operand1.length();
			int lb = operand2.length();
			operand1 = oneAdder(negation(operand1));
			operand1 = operand1.substring(operand1.length() - la, operand1.length());
			operand2 = oneAdder(negation(operand2));
			operand2 = operand2.substring(operand2.length() - lb, operand2.length());
		}

		// 被除数符号扩展
		String str1 = operand1;
		for (int i = 0; i < length * 2 - operand1.length(); i++) {
			str1 = operand1.charAt(0) + str1;
		}

		// 溢出位
		char overBit = '0';

		for (int i = 0; i < length; i++) {
			// 如果余数寄存器中的数与除数符号相同，做减法
			if (str1.charAt(0) == operand2.charAt(0)) {
				str1 = integerSubtraction(str1.substring(0, length), operand2, length).substring(1)
						+ str1.substring(length);
			} else {
				str1 = integerAddition(str1.substring(0, length), operand2, length).substring(1)
						+ str1.substring(length);
			}

			if (str1.charAt(0) == operand2.charAt(0)) {
				str1 = str1.substring(1) + "1";
			} else {
				str1 = str1.substring(1) + "0";
			}
		}

		if (str1.charAt(0) == operand2.charAt(0)) {
			str1 = integerSubtraction(str1.substring(0, length), operand2, length).substring(1)
					+ str1.substring(length);
		} else {
			str1 = integerAddition(str1.substring(0, length), operand2, length).substring(1) + str1.substring(length);
		}

		String eString = str1.substring(length);
		String reminder = str1.substring(0, length);

		// 若余数和被除数符号不同，当余数与除数符号相同，减法，否则加法
		if (reminder.charAt(0) == operand2.charAt(0)) {
			if (reminder.charAt(0) != operand1.charAt(0)) {
				reminder = integerSubtraction(reminder, operand2, length).substring(1);
			}
			eString = eString.substring(1) + "1";
		} else {
			if (reminder.charAt(0) != operand1.charAt(0))
				reminder = integerAddition(reminder, operand2, length).substring(1);
			eString = eString.substring(1, eString.length()) + "0";
		}

		// 商的修正，如果被除数和除数符号相反，商加1
		if (operand1.charAt(0) != operand2.charAt(0)) {
			eString = oneAdder(eString).substring(1);
		}

		// 溢出位+商+余数
		return overBit + eString + reminder;

	}

	/**
	 * 带符号整数加法，可以调用{@link #adder(String, String, char, int) adder}等方法，
	 * 但不能直接将操作数转换为补码后使用{@link #integerAddition(String, String, int)
	 * integerAddition}、 {@link #integerSubtraction(String, String, int)
	 * integerSubtraction}来实现。<br/>
	 * 例：signedAddition("1100", "1011", 8)
	 * 
	 * @param operand1
	 *            二进制原码表示的被加数，其中第1位为符号位
	 * @param operand2
	 *            二进制原码表示的加数，其中第1位为符号位
	 * @param length
	 *            存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度（不包含符号），当某个操作数的长度小于length时，需要将其长度扩展到length
	 * @return 长度为length+2的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），第2位为符号位，后length位是相加结果
	 */
	public String signedAddition(String operand1, String operand2, int length) {
		String sign1 = operand1.substring(0, 1);
		String sign2 = operand2.substring(0, 1);
		String result = "";
		if (sign1.equals(sign2)) {
			String res = adder("0" + operand1.substring(1, operand1.length()),
					"0" + operand2.substring(1, operand2.length()), '0', length + 1);

			result = res.substring(1, 2) + sign1 + res.substring(2, res.length());
		} else {
			String res = "";
			String va1 = integerTrueValue("0" + operand1.substring(1, operand1.length()));
			String va2 = integerTrueValue("0" + operand2.substring(1, operand2.length()));
			int value = 0;
			if (sign1.equals("1")) {
				value = Integer.parseInt(va2) - Integer.parseInt(va1);
			} else {
				value = Integer.parseInt(va1) - Integer.parseInt(va2);
			}
			res = integerRepresentation(String.valueOf(value), length + 1);
			if (value == 0) {
				result = sign1 + res;
			} else {
				result = "0" + res;
			}
		}
		return result;
	}

	/**
	 * 浮点数加法，可调用{@link #signedAddition(String, String, int)
	 * signedAddition}等方法实现。<br/>
	 * 例：floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * 
	 * @param operand1
	 *            二进制表示的被加数
	 * @param operand2
	 *            二进制表示的加数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @param gLength
	 *            保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相加结果，其中第1位指示是否指数上溢（溢出为1，否则为0），
	 *         其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */

	public String floatAddition(String operand1, String operand2, int eLength, int sLength, int gLength) {

		if (operand1.equals("NaN") || operand2.equals("NaN")) { // 非数
			return "NaN";
		} else if ((floatTrueValue(operand1, eLength, sLength).contains("Inf")) // 正负无穷
				|| (floatTrueValue(operand2, eLength, sLength).contains("Inf"))) {
			return "Inf";
		} else if (floatTrueValue(operand1, eLength, sLength).equals("0.0")
				|| floatTrueValue(operand1, eLength, sLength).equals("-0.0")) { // 0
			return "0" + operand2;
		} else if (floatTrueValue(operand2, eLength, sLength).equals("0.0")
				|| floatTrueValue(operand2, eLength, sLength).equals("-0.0")) {
			return "0" + operand1;
		} else if (operand1.substring(1, operand1.length()).equals(operand2.substring(1, operand2.length()))
				&& (!operand1.substring(0, 1).equals(operand2.substring(0, 1)))) {
			return addLength("0", 2 + eLength + sLength);
		} else {

			String res = "";
			String firstBit = "0";// 第一位
			String signBit = "0";// 符号位
			String eString = "";// 阶码
			String sString = "";// 尾数

			String sString1 = operand1.substring(eLength + 1, operand1.length());
			String sString2 = operand2.substring(eLength + 1, operand2.length());

			int eValue1 = Integer.parseInt(integerTrueValue("0" + operand1.substring(1, eLength + 1)));
			int eValue2 = Integer.parseInt(integerTrueValue("0" + operand2.substring(1, eLength + 1)));
			eString = operand1.substring(1, eLength + 1);
			int eValue = 0;// 阶码的真值

			// 对阶
			if (eValue1 < eValue2) {

				eValue = eValue2;
				eString = operand2.substring(1, eLength + 1);
				sString1 = "1" + sString1;
				sString1 = logRightShift(sString1, Math.abs(eValue1 - eValue2));
			} else if (eValue1 > eValue2) {

				eValue = eValue1;
				eString = operand1.substring(1, eLength + 1);
				sString2 = "1" + sString2;
				sString2 = logRightShift(sString2, Math.abs(eValue1 - eValue2));
			} else {
				eValue = eValue1;
				eString = operand1.substring(1, eLength + 1);
				sString1 = "1" + sString1;// 尾数隐藏的一位1
				sString2 = "1" + sString2;
			}

			sString = signedAddition(operand1.substring(0, 1) + sString1, operand2.substring(0, 1) + sString2,
					sLength + 1);

			// 符号位
			double a = Double.parseDouble(floatTrueValue(operand1, eLength, sLength));
			double b = Double.parseDouble(floatTrueValue(operand2, eLength, sLength));
			if (Math.abs(a) >= Math.abs(b)) {
				signBit = operand1.substring(0, 1);
			} else {
				signBit = operand2.substring(0, 1);
			}

			if (sString.charAt(0) == '1') { // 判断尾数是否有进位
				eString = oneAdder(eString);
				if (eString.charAt(0) == '1') {// 判断是否溢出
					firstBit = "1";
				}
				eString = eString.substring(1, eString.length());
				sString = sString.substring(0, 1) + sString.substring(2, sString.length() - 1);
			} else {
				sString = sString.substring(2, sString.length() - 1);// 尾数
			}

			// 规格化操作(左规)
			if (sString.substring(0, 1).equals("0")) {
				int i = 0;
				while (i < sString.length()) {
					if (sString.charAt(i) == '0') {
						eValue--;
						i++;
					} else {
						break;
					}
				}
				if (eValue > 0) {
					eString = integerRepresentation(String.valueOf(eValue), eLength);
					sString = sString.substring(i, sString.length());
				}
			}

			if (sString.length() > 1) {
				sString = sString.substring(1, sString.length());
			} else {
				sString = "";
			}

			while (sString.length() < sLength) {
				sString = sString + "0";
			}
			res = firstBit + signBit + eString + sString;
			return res;
		}

	}

	/**
	 * 浮点数减法，可调用{@link #floatAddition(String, String, int, int, int)
	 * floatAddition}方法实现。<br/>
	 * 例：floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * 
	 * @param operand1
	 *            二进制表示的被减数
	 * @param operand2
	 *            二进制表示的减数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @param gLength
	 *            保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相减结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatSubtraction(String operand1, String operand2, int eLength, int sLength, int gLength) {

		if (operand2.substring(0, 1).equals("0")) {
			operand2 = "1" + operand2.substring(1, operand2.length());
		} else {
			operand2 = "0" + operand2.substring(1, operand2.length());
		}
		String res = floatAddition(operand1, operand2, eLength, sLength, gLength);
		return res;
	}

	/**
	 * 浮点数乘法，可调用{@link #integerMultiplication(String, String, int)
	 * integerMultiplication}等方法实现。<br/>
	 * 例：floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * 
	 * @param operand1
	 *            二进制表示的被乘数
	 * @param operand2
	 *            二进制表示的乘数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatMultiplication(String operand1, String operand2, int eLength, int sLength) {

		if (!operand1.contains("1") || !operand2.contains("1")) { // 乘数有正0
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 2 + sLength + eLength; i++) {
				sb.insert(0, 0);
			}
			return sb.toString();
		}

		// 小数部分布斯乘法
		String ssString;
		String xString;
		String yString;

		// 隐藏位
		if (operand1.substring(1, 1 + eLength).contains("1")) {
			xString = "1" + operand1.substring(1 + eLength);
		} else {
			xString = "0" + operand1.substring(1 + eLength);
		}
		if (operand2.substring(1, 1 + eLength).contains("1")) {
			yString = "1" + operand2.substring(1 + eLength);
		} else {
			yString = "0" + operand2.substring(1 + eLength);
		}

		int gLength = ((sLength + 1) / 4 + 1) * 4;

		xString = leftShift(addLength(xString, gLength), gLength - 1 - sLength);
		yString = leftShift(addLength(yString, gLength), gLength - 1 - sLength);

		xString = logRightShift(xString, gLength - 1 - sLength);
		yString = logRightShift(yString, gLength - 1 - sLength);

		// 尾数相乘
		ssString = integerMultiplication(xString, yString, gLength * 2);
		ssString = ssString.substring(ssString.length() - 2 * xString.length());

		// 阶码部分
		String eString = "0";
		int index = 0;
		int z = new BigInteger(operand1.substring(1, 1 + eLength), 2).intValue();
		int z2 = new BigInteger(operand2.substring(1, 1 + eLength), 2).intValue();
		if (z + z2 < Math.pow(2, eLength - 1)) {
			index = z + z2 - (int) Math.pow(2, eLength - 1);
			for (int i = 0; i < eLength; i++) {
				eString = eString + "0";
			}
		} else {
			// 阶码相加
			eString = integerSubtraction(operand1.substring(1, 1 + eLength),
					"0" + Integer.toBinaryString((int) Math.pow(2, eLength - 1) - 1), eLength).substring(1);
			eString = integerAddition(eString, operand2.substring(1, 1 + eLength), eLength);
			index = ssString.indexOf("1");
		}
		// 排除阶码溢出的情况
		if (index >= 0) {
			ssString = ssString.substring(index + 1, index + 1 + sLength);
			if (operand1.charAt(0) == operand2.charAt(0)) {
				return eString.charAt(0) + "0" + eString.substring(1) + ssString;
			} else {
				return eString.charAt(0) + "1" + eString.substring(1) + ssString;
			}
		} else {
			for (int i = index; i != 0; i++) {
				ssString = logRightShift(ssString, 1);
			}

			ssString = ssString.substring(0, sLength);
			if (!ssString.contains("1")) {
				return "1" + "0" + eString.substring(1) + ssString;
			}

			if (operand1.charAt(0) == operand2.charAt(0)) {
				return eString.charAt(0) + "0" + eString.substring(1) + ssString;
			} else {
				return eString.charAt(0) + "1" + eString.substring(1) + ssString;
			}
		}
	}

	/**
	 * 浮点数除法，可调用{@link #integerDivision(String, String, int)
	 * integerDivision}等方法实现。<br/>
	 * 例：floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * 
	 * @param operand1
	 *            二进制表示的被除数
	 * @param operand2
	 *            二进制表示的除数
	 * @param eLength
	 *            指数的长度，取值大于等于 4
	 * @param sLength
	 *            尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatDivision(String operand1, String operand2, int eLength, int sLength) {

		int expo1 = Integer.parseInt(integerTrueValue("0" + operand1.substring(1, 1 + eLength)));
		int expo2 = Integer.parseInt(integerTrueValue("0" + operand2.substring(1, 1 + eLength)));
		int bias = (int) (Math.pow(2, eLength - 1) - 1);
		if (expo1 == 0) {
			return addLength("0", 2 + eLength + sLength);
		}
		// 隐藏位
		char hideBit1 = '1';
		char hideBit2 = '1';

		if (expo1 == 0) {
			hideBit1 = '0';
		}
		if (expo2 == 0) {
			hideBit1 = '0';
		}

		String sign1 = hideBit1 + operand1.substring(eLength + 1);
		String sign2 = hideBit2 + operand2.substring(eLength + 1);

		char signBit = '0';
		if (operand1.charAt(0) != operand2.charAt(0)) {
			signBit = '1';
		}

		// 如果被除数为正负0，那么答案即为0
		if (floatTrueValue(operand1, eLength, sLength) == "0.0"
				|| floatTrueValue(operand1, eLength, sLength) == "-0.0") {

			String result = "";
			for (int i = 0; i < 1 + eLength + sLength; i++) {
				result += "0";
			}
			return "0" + signBit + result;

		} else if (floatTrueValue(operand2, eLength, sLength) == "0.0" // 除数为0
				|| floatTrueValue(operand2, eLength, sLength) == "-0.0") {

			String eString = "";
			String sString = "";
			for (int i = 0; i < eLength; i++) {
				eString += "1";
			}
			for (int i = 0; i < sLength; i++) {
				sString += "0";
			}

			return "0" + signBit + eString + sString;
		}

		// 指数相减，加上偏值
		int eValue = expo1 - expo2 + bias;

		// 如果指数上溢，返回无穷
		if (eValue > Math.pow(2, eLength) - 1) {
			String expo = "";
			String sign = "";
			for (int i = 0; i < eLength; i++) {
				expo += "1";
			}
			for (int i = 0; i < sLength; i++) {
				sign += "0";
			}

			return "1" + signBit + expo + sign;

		} else if (eValue < 0) { // 指数下溢返回0

			String symAndSign = "";
			for (int i = 0; i < eLength + sLength; i++) {
				symAndSign += "0";
			}

			return "0" + signBit + symAndSign;
		}

		String sString = sign1;
		int length = ((1 + sLength) / 4 + 1) * 4 - 1;

		// 两数相除
		for (int i = 0; i < length - sign1.length(); i++) {
			sString = "0" + sString;
		}

		for (int i = 0; i < length; i++) {
			sString = sString + "0";
		}

		for (int i = 0; i < length; i++) {
			String reminder = sString.substring(0, length);
			char c = '0';

			// 如果够减，就减
			String temp = integerSubtraction("0" + reminder, "0" + sign2, length + 1).substring(2);
			if (Integer.parseInt(integerTrueValue(temp)) >= 0) {
				reminder = temp;
				c = '1';
			}
			sString = reminder + sString.substring(length, length * 2);
			sString = sString.substring(1) + c;

		}

		sString = sString.substring(length, 2 * length);

		while (sString.charAt(0) != '1') {
			eValue--;
			sString = leftShift(sString, 1);
			if (eValue == 0) {
				String expo = integerRepresentation("0", eLength);
				String sign = sString.substring(1, 1 + sLength);
				return "0" + signBit + expo + sign;
			}
		}

		String eString2 = integerRepresentation(String.valueOf(eValue), eLength + 1).substring(1);
		String sString2 = sString.substring(1, 1 + sLength);

		return "0" + signBit + eString2 + sString2;
	}
}
