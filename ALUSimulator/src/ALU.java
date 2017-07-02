import java.math.BigInteger;

/**
 * ģ��ALU���������͸���������������
 * 
 * @author [161250025��������] .
 */

public class ALU {

	/**
	 * ����ʮ���������Ķ����Ʋ����ʾ��<br/>
	 * ����integerRepresentation("9", 8)
	 * 
	 * @param number
	 *            ʮ������������Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length
	 *            �����Ʋ����ʾ�ĳ���
	 * @return number�Ķ����Ʋ����ʾ������Ϊlength
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
	 * ����ʮ���Ƹ������Ķ����Ʊ�ʾ�� ��Ҫ���� 0������񻯡����������+Inf���͡�-Inf������ NaN�����أ������� IEEE 754��
	 * �������Ϊ��0���롣<br/>
	 * ����floatRepresentation("11.375", 8, 11)
	 * 
	 * @param number
	 *            ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param eLength
	 *            ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength
	 *            β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return number�Ķ����Ʊ�ʾ������Ϊ 1+eLength+sLength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
	 */
	public String floatRepresentation(String number, int eLength, int sLength) {
		String eString = "";
		String sString = "";
		String result = "";
		int signBit = 0;

		// ����λ
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
			int index = -1;// .������

			for (int i = 0; i < number.length(); i++) {
				if (number.substring(i, i + 1).equals(".")) {
					index = i;
					break;
				}
			}
			String str = number;
			if (index > 0) {
				str = number.substring(0, index);
			} else if (index == 0) {// ���.�ǵ�һλ
				str = "0";
			}

			int num = Integer.parseInt(str);
			String eStr = ""; // �������ֵĶ�����
			int eNum = 0;
			int eNum2 = 0;
			eString = "";
			if (num > 0) {
				do {
					eStr = num % 2 + eStr;
					num = num / 2;
				} while (num != 0);

				eNum = receivePowerOf2(eLength - 1) + eStr.length() - 2; // ����ֵ
				eNum2 = eNum;
				do {
					eString = eNum % 2 + eString;
					eNum = eNum / 2;
				} while (eNum != 0);
				if (eString.length() < eLength) { // �������
					for (int i = 0; i < eLength - eString.length() + 1; i++) {
						eString = "0" + eString;
					}
				}
			}
			// ��β��
			if (index > 0) {

				String str2 = "0." + number.substring(index + 1, number.length());
				int eNum3 = 0;
				double num2 = Double.parseDouble(str2);
				// ��С��

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
						while (eString.length() < eLength) { // �������
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
						while (eString.length() < eLength) { // �������
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
	 * ����ʮ���Ƹ�������IEEE 754��ʾ��Ҫ�����{@link #floatRepresentation(String, int, int)
	 * floatRepresentation}ʵ�֡�<br/>
	 * ����ieee754("11.375", 32)
	 * 
	 * @param number
	 *            ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length
	 *            �����Ʊ�ʾ�ĳ��ȣ�Ϊ32��64
	 * @return number��IEEE 754��ʾ������Ϊlength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
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
	 * ��������Ʋ����ʾ����������ֵ��<br/>
	 * ����integerTrueValue("00001001")
	 * 
	 * @param operand
	 *            �����Ʋ����ʾ�Ĳ�����
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
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
	 * ��2��n�η�(n>=0)
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
	 * ��2��n�η�(n<0)
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
	 * ��С������ֵ
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
	 * ���������ԭ���ʾ�ĸ���������ֵ��<br/>
	 * ����floatTrueValue("01000001001101100000", 8, 11)
	 * 
	 * @param operand
	 *            �����Ʊ�ʾ�Ĳ�����
	 * @param eLength
	 *            ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength
	 *            β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ����������ֱ��ʾΪ��+Inf���͡�-Inf����
	 *         NaN��ʾΪ��NaN��
	 */

	public String floatTrueValue(String operand, int eLength, int sLength) {

		String eString = operand.substring(1, eLength + 1);
		String sString = operand.substring(eLength + 1, operand.length());
		String result = "";
		if (operand.substring(0, 1).equals("1")) {
			result = "-";
		}

		// �������������0
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
			// ָ��
			int eValue = Integer.parseInt(integerTrueValue("0" + eString)) - receivePowerOf2(eLength - 1) + 1;
			String Znum = "";
			String Xnum = "";// С������
			int sZvalue = 0;// ����ֵ
			double sXvalue = 0; // С��ֵ

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
	 * ��λȡ��������<br/>
	 * ����negation("00001001")
	 * 
	 * @param operand
	 *            �����Ʊ�ʾ�Ĳ�����
	 * @return operand��λȡ���Ľ��
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
	 * ���Ʋ�����<br/>
	 * ����leftShift("00001001", 2)
	 * 
	 * @param operand
	 *            �����Ʊ�ʾ�Ĳ�����
	 * @param n
	 *            ���Ƶ�λ��
	 * @return operand����nλ�Ľ��
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
	 * �߼����Ʋ�����<br/>
	 * ����logRightShift("11110110", 2)
	 * 
	 * @param operand
	 *            �����Ʊ�ʾ�Ĳ�����
	 * @param n
	 *            ���Ƶ�λ��
	 * @return operand�߼�����nλ�Ľ��
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
	 * �������Ʋ�����<br/>
	 * ����logRightShift("11110110", 2)
	 * 
	 * @param operand
	 *            �����Ʊ�ʾ�Ĳ�����
	 * @param n
	 *            ���Ƶ�λ��
	 * @return operand��������nλ�Ľ��
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
	 * ȫ����������λ�Լ���λ���мӷ����㡣<br/>
	 * ����fullAdder('1', '1', '0')
	 * 
	 * @param x
	 *            ��������ĳһλ��ȡ0��1
	 * @param y
	 *            ������ĳһλ��ȡ0��1
	 * @param c
	 *            ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ��ӵĽ�����ó���Ϊ2���ַ�����ʾ����1λ��ʾ��λ����2λ��ʾ��
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
	 * 4λ���н�λ�ӷ�����Ҫ�����{@link #fullAdder(char, char, char) fullAdder}��ʵ��<br/>
	 * ����claAdder("1001", "0001", '1')
	 * 
	 * @param operand1
	 *            4λ�����Ʊ�ʾ�ı�����
	 * @param operand2
	 *            4λ�����Ʊ�ʾ�ļ���
	 * @param c
	 *            ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ����Ϊ5���ַ�����ʾ�ļ����������е�1λ�����λ��λ����4λ����ӽ�������н�λ��������ѭ�����
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
	 * ��һ����ʵ�ֲ�������1�����㡣 ��Ҫ�������š����š�����ŵ�ģ�⣬
	 * ������ֱ�ӵ���{@link #fullAdder(char, char, char) fullAdder}��
	 * {@link #claAdder(String, String, char) claAdder}��
	 * {@link #adder(String, String, char, int) adder}��
	 * {@link #integerAddition(String, String, int) integerAddition}������<br/>
	 * ����oneAdder("00001001")
	 * 
	 * @param operand
	 *            �����Ʋ����ʾ�Ĳ�����
	 * @return operand��1�Ľ��������Ϊoperand�ĳ��ȼ�1�����е�1λָʾ�Ƿ���������Ϊ1������Ϊ0��������λΪ��ӽ��
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
	 * �ӷ�����Ҫ�����{@link #claAdder(String, String, char)}����ʵ�֡�<br/>
	 * ����adder("0100", "0011", ��0��, 8)
	 * 
	 * @param operand1
	 *            �����Ʋ����ʾ�ı�����
	 * @param operand2
	 *            �����Ʋ����ʾ�ļ���
	 * @param c
	 *            ���λ��λ
	 * @param length
	 *            ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
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
	 * ���ַ������㳤��
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
	 * �����ӷ���Ҫ�����{@link #adder(String, String, char, int) adder}����ʵ�֡�<br/>
	 * ����integerAddition("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            �����Ʋ����ʾ�ı�����
	 * @param operand2
	 *            �����Ʋ����ʾ�ļ���
	 * @param length
	 *            ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
	 */

	public String integerAddition(String operand1, String operand2, int length) {
		String st = adder(operand1, operand2, '0', length);
		return st;
	}

	/**
	 * �����������ɵ���{@link #adder(String, String, char, int) adder}����ʵ�֡�<br/>
	 * ����integerSubtraction("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            �����Ʋ����ʾ�ı�����
	 * @param operand2
	 *            �����Ʋ����ʾ�ļ���
	 * @param length
	 *            ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ��������
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
	 * �����˷���ʹ��Booth�㷨ʵ�֣��ɵ���{@link #adder(String, String, char, int)
	 * adder}�ȷ�����<br/>
	 * ����integerMultiplication("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            �����Ʋ����ʾ�ı�����
	 * @param operand2
	 *            �����Ʋ����ʾ�ĳ���
	 * @param length
	 *            ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ����˽�������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����˽��
	 */ 
	public String integerMultiplication(String operand1, String operand2, int length) {

		String operand11 = addLength(operand1, length);
		String operand22 = addLength(operand2, length);

		String signBit = "0";
		String product = operand22 + "0"; // ��
		for (int i = 0; i < length; i++)
			product = "0" + product;

		for (int i = 0; i < length; i++) {
			String part = product.substring(0, length); // ���ֻ�
			// �Ƚ�P0��P1��ֵ

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
	 * �����Ĳ��ָ������������ɵ���{@link #adder(String, String, char, int) adder}�ȷ���ʵ�֡�<br/>
	 * ����integerDivision("0100", "0011", 8)
	 * 
	 * @param operand1
	 *            �����Ʋ����ʾ�ı�����
	 * @param operand2
	 *            �����Ʋ����ʾ�ĳ���
	 * @param length
	 *            ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊ2*length+1���ַ�����ʾ�������������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0�������lengthλΪ�̣����lengthλΪ����
	 */
	public String integerDivision(String operand1, String operand2, int length) {
		// ����Ϊ0�����
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

		// ������������չ
		String str1 = operand1;
		for (int i = 0; i < length * 2 - operand1.length(); i++) {
			str1 = operand1.charAt(0) + str1;
		}

		// ���λ
		char overBit = '0';

		for (int i = 0; i < length; i++) {
			// ��������Ĵ����е��������������ͬ��������
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

		// �������ͱ��������Ų�ͬ�������������������ͬ������������ӷ�
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

		// �̵�����������������ͳ��������෴���̼�1
		if (operand1.charAt(0) != operand2.charAt(0)) {
			eString = oneAdder(eString).substring(1);
		}

		// ���λ+��+����
		return overBit + eString + reminder;

	}

	/**
	 * �����������ӷ������Ե���{@link #adder(String, String, char, int) adder}�ȷ�����
	 * ������ֱ�ӽ�������ת��Ϊ�����ʹ��{@link #integerAddition(String, String, int)
	 * integerAddition}�� {@link #integerSubtraction(String, String, int)
	 * integerSubtraction}��ʵ�֡�<br/>
	 * ����signedAddition("1100", "1011", 8)
	 * 
	 * @param operand1
	 *            ������ԭ���ʾ�ı����������е�1λΪ����λ
	 * @param operand2
	 *            ������ԭ���ʾ�ļ��������е�1λΪ����λ
	 * @param length
	 *            ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ����������ţ�����ĳ���������ĳ���С��lengthʱ����Ҫ���䳤����չ��length
	 * @return ����Ϊlength+2���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������2λΪ����λ����lengthλ����ӽ��
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
	 * �������ӷ����ɵ���{@link #signedAddition(String, String, int)
	 * signedAddition}�ȷ���ʵ�֡�<br/>
	 * ����floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * 
	 * @param operand1
	 *            �����Ʊ�ʾ�ı�����
	 * @param operand2
	 *            �����Ʊ�ʾ�ļ���
	 * @param eLength
	 *            ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength
	 *            β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength
	 *            ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����ӽ�������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0����
	 *         ����λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */

	public String floatAddition(String operand1, String operand2, int eLength, int sLength, int gLength) {

		if (operand1.equals("NaN") || operand2.equals("NaN")) { // ����
			return "NaN";
		} else if ((floatTrueValue(operand1, eLength, sLength).contains("Inf")) // ��������
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
			String firstBit = "0";// ��һλ
			String signBit = "0";// ����λ
			String eString = "";// ����
			String sString = "";// β��

			String sString1 = operand1.substring(eLength + 1, operand1.length());
			String sString2 = operand2.substring(eLength + 1, operand2.length());

			int eValue1 = Integer.parseInt(integerTrueValue("0" + operand1.substring(1, eLength + 1)));
			int eValue2 = Integer.parseInt(integerTrueValue("0" + operand2.substring(1, eLength + 1)));
			eString = operand1.substring(1, eLength + 1);
			int eValue = 0;// �������ֵ

			// �Խ�
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
				sString1 = "1" + sString1;// β�����ص�һλ1
				sString2 = "1" + sString2;
			}

			sString = signedAddition(operand1.substring(0, 1) + sString1, operand2.substring(0, 1) + sString2,
					sLength + 1);

			// ����λ
			double a = Double.parseDouble(floatTrueValue(operand1, eLength, sLength));
			double b = Double.parseDouble(floatTrueValue(operand2, eLength, sLength));
			if (Math.abs(a) >= Math.abs(b)) {
				signBit = operand1.substring(0, 1);
			} else {
				signBit = operand2.substring(0, 1);
			}

			if (sString.charAt(0) == '1') { // �ж�β���Ƿ��н�λ
				eString = oneAdder(eString);
				if (eString.charAt(0) == '1') {// �ж��Ƿ����
					firstBit = "1";
				}
				eString = eString.substring(1, eString.length());
				sString = sString.substring(0, 1) + sString.substring(2, sString.length() - 1);
			} else {
				sString = sString.substring(2, sString.length() - 1);// β��
			}

			// ��񻯲���(���)
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
	 * �������������ɵ���{@link #floatAddition(String, String, int, int, int)
	 * floatAddition}����ʵ�֡�<br/>
	 * ����floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * 
	 * @param operand1
	 *            �����Ʊ�ʾ�ı�����
	 * @param operand2
	 *            �����Ʊ�ʾ�ļ���
	 * @param eLength
	 *            ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength
	 *            β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength
	 *            ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ�������������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
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
	 * �������˷����ɵ���{@link #integerMultiplication(String, String, int)
	 * integerMultiplication}�ȷ���ʵ�֡�<br/>
	 * ����floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * 
	 * @param operand1
	 *            �����Ʊ�ʾ�ı�����
	 * @param operand2
	 *            �����Ʊ�ʾ�ĳ���
	 * @param eLength
	 *            ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength
	 *            β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatMultiplication(String operand1, String operand2, int eLength, int sLength) {

		if (!operand1.contains("1") || !operand2.contains("1")) { // ��������0
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 2 + sLength + eLength; i++) {
				sb.insert(0, 0);
			}
			return sb.toString();
		}

		// С�����ֲ�˹�˷�
		String ssString;
		String xString;
		String yString;

		// ����λ
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

		// β�����
		ssString = integerMultiplication(xString, yString, gLength * 2);
		ssString = ssString.substring(ssString.length() - 2 * xString.length());

		// ���벿��
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
			// �������
			eString = integerSubtraction(operand1.substring(1, 1 + eLength),
					"0" + Integer.toBinaryString((int) Math.pow(2, eLength - 1) - 1), eLength).substring(1);
			eString = integerAddition(eString, operand2.substring(1, 1 + eLength), eLength);
			index = ssString.indexOf("1");
		}
		// �ų�������������
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
	 * �������������ɵ���{@link #integerDivision(String, String, int)
	 * integerDivision}�ȷ���ʵ�֡�<br/>
	 * ����floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * 
	 * @param operand1
	 *            �����Ʊ�ʾ�ı�����
	 * @param operand2
	 *            �����Ʊ�ʾ�ĳ���
	 * @param eLength
	 *            ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength
	 *            β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatDivision(String operand1, String operand2, int eLength, int sLength) {

		int expo1 = Integer.parseInt(integerTrueValue("0" + operand1.substring(1, 1 + eLength)));
		int expo2 = Integer.parseInt(integerTrueValue("0" + operand2.substring(1, 1 + eLength)));
		int bias = (int) (Math.pow(2, eLength - 1) - 1);
		if (expo1 == 0) {
			return addLength("0", 2 + eLength + sLength);
		}
		// ����λ
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

		// ���������Ϊ����0����ô�𰸼�Ϊ0
		if (floatTrueValue(operand1, eLength, sLength) == "0.0"
				|| floatTrueValue(operand1, eLength, sLength) == "-0.0") {

			String result = "";
			for (int i = 0; i < 1 + eLength + sLength; i++) {
				result += "0";
			}
			return "0" + signBit + result;

		} else if (floatTrueValue(operand2, eLength, sLength) == "0.0" // ����Ϊ0
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

		// ָ�����������ƫֵ
		int eValue = expo1 - expo2 + bias;

		// ���ָ�����磬��������
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

		} else if (eValue < 0) { // ָ�����緵��0

			String symAndSign = "";
			for (int i = 0; i < eLength + sLength; i++) {
				symAndSign += "0";
			}

			return "0" + signBit + symAndSign;
		}

		String sString = sign1;
		int length = ((1 + sLength) / 4 + 1) * 4 - 1;

		// �������
		for (int i = 0; i < length - sign1.length(); i++) {
			sString = "0" + sString;
		}

		for (int i = 0; i < length; i++) {
			sString = sString + "0";
		}

		for (int i = 0; i < length; i++) {
			String reminder = sString.substring(0, length);
			char c = '0';

			// ����������ͼ�
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
