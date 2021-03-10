package banking;

import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.Vector;

public class RandomCardNumberGenerator {
    public static final String[] MY_BANKING_SYSTEM_IIN = new String[]{"400000"};

    static String strrev(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    static String completedNumber(String prefix, int length) {
        StringBuilder ccnumber = prefix == null ? null : new StringBuilder(prefix);
        while (Objects.requireNonNull(ccnumber).length() < (length - 1)) {
            ccnumber.append(Double.valueOf(Math.floor(Math.random() * 10)).intValue());

        }
        String reversedCCnumberString = strrev(ccnumber.toString());
        List<Integer> reversedCCnumberList = new Vector<>();
        for (int i = 0; i < reversedCCnumberString.length(); i++) {
            reversedCCnumberList.add(Integer.valueOf(String
                    .valueOf(reversedCCnumberString.charAt(i))));
        }
        int sum = 0;
        int pos = 0;
        Integer[] reversedCCnumber;
        reversedCCnumber = reversedCCnumberList
                .toArray(new Integer[reversedCCnumberList.size()]);
        while (pos < length - 1) {
            int odd = reversedCCnumber[pos] * 2;
            if (odd > 9) {
                odd -= 9;
            }
            sum += odd;
            if (pos != (length - 2)) {
                sum += reversedCCnumber[pos + 1];
            }
            pos += 2;
        }
        int checkdigit = Double.valueOf(
                ((Math.floor(sum / 10) + 1) * 10 - sum) % 10).intValue();
        ccnumber.append(checkdigit);
        return ccnumber.toString();
    }

    public static String[] creditCardNumber(String[] prefixList, int length, int howMany) {
        Stack<String> result = new Stack<>();
        for (int i = 0; i < howMany; i++) {
            int randomArrayIndex = (int) Math.floor(Math.random()
                    * prefixList.length);
            String ccnumber = prefixList[randomArrayIndex];
            result.push(completedNumber(ccnumber, length));
        }
        return result.toArray(new String[result.size()]);
    }

    public static String generateMyBankingSystemCardNumber() {
        return creditCardNumber(MY_BANKING_SYSTEM_IIN, 16, 1)[0];
    }

    public static boolean isValidCreditCardNumber(String creditCardNumber) {
        boolean isValid = false;
        try {
            String reversedNumber = new StringBuffer(creditCardNumber)
                    .reverse().toString();
            int mod10Count = 0;
            for (int i = 0; i < reversedNumber.length(); i++) {
                int augend = Integer.parseInt(String.valueOf(reversedNumber
                        .charAt(i)));
                if (((i + 1) % 2) == 0) {
                    String productString = String.valueOf(augend * 2);
                    augend = 0;
                    for (int j = 0; j < productString.length(); j++) {
                        augend += Integer.parseInt(String.valueOf(productString
                                .charAt(j)));
                    }
                }
                mod10Count += augend;
            }
            if ((mod10Count % 10) == 0) {
                isValid = true;
            }
        } catch (NumberFormatException e) {
        }
        return isValid;
    }

}