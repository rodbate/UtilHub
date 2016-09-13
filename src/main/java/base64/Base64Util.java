package base64;



/**
 * Base64   六位二进制为一组    四组二十四为为一个单位     不足补=
 * 0--63
 * A-Z  0--25
 * a-z  26--51
 * 0-9  52--61
 * +    62
 * /    63
 *
 */


public class Base64Util {


    public static String encode(byte[] src){

        StringBuilder sb = new StringBuilder();

        // 3 * 8  == 4 * 6
        for (int i = 0; i < src.length; i++) {

            int pad = 0;

            int s = (src[i++] & 0xff) << 16;

            if (i < src.length) {
                s |= (src[i++] & 0xff) << 8;
            } else {
                pad++;
            }

            if (i < src.length) {
                s |= (src[i] & 0xff);
            } else {
                pad++;
            }

            sb.append(encode6(s >>> 18));
            sb.append(encode6(s >>> 12));

            switch (pad) {

                case 0 : sb.append(encode6(s >>> 6)).append(encode6(s));
                    break;
                case 1 : sb.append(encode6(s >>> 6)).append("=");
                    break;
                case 2 : sb.append("=").append("=");
                    break;
                default:
                    break;
            }

        }

        return sb.toString();
    }


    public static String decode(String str){

        StringBuilder sb = new StringBuilder();

        //先去除=
        if (str.contains("=")) {
            str = str.substring(0, str.indexOf("="));
        }

        //byte[] bytes = str.getBytes();
        for (int i = 0; i < str.length(); i++) {

            char c = str.charAt(i);

            if ((str.length() - i) / 4 < 1) {

                if (str.length() % 4 == 0) {

                    int s = decode6(str.charAt(i++)) << 18;

                    s |= decode6(str.charAt(i++)) << 12;

                    s |= decode6(str.charAt(i++)) << 6;

                    s |= decode6(str.charAt(i));

                    byte[] b = new byte[3];
                    b[0] = (byte) (s >>> 16);
                    b[1] = (byte) ((s >>> 8) & 0xff);
                    b[2] = (byte) (s & 0xff);
                    sb.append(new String(b));
                }

                if (str.length() % 4 == 2) {

                    int s = decode6(str.charAt(i++)) << 6;

                    s |= decode6(str.charAt(i++));

                    byte[] b = new byte[1];
                    b[0] = (byte) (s >>> 4);

                    sb.append(new String(b));
                    i++;
                }

                if (str.length() % 4 == 3) {

                    int s = decode6(str.charAt(i++)) << 12;

                    s |= decode6(str.charAt(i++)) << 6;

                    s |= decode6(str.charAt(i++));

                    byte[] b = new byte[2];
                    b[0] = (byte) (s >>> 10);
                    b[1] = (byte) ((s >>> 2) & 0xff);

                    sb.append(new String(b));
                }
            } else {
                int s = decode6(str.charAt(i++)) << 18;

                s |= decode6(str.charAt(i++)) << 12;

                s |= decode6(str.charAt(i++)) << 6;

                s |= decode6(str.charAt(i));

                byte[] b = new byte[3];
                b[0] = (byte) (s >>> 16);
                b[1] = (byte) ((s >>> 8) & 0xff);
                b[2] = (byte) (s & 0xff);
                sb.append(new String(b));
            }

        }


        return sb.toString();
    }

    private static char encode6(int a) {

        a = a & 0x3f;

        if (a <= 25) return (char)('A' + a);

        if (a <= 51) return (char)('a' + a - 26);

        if (a <= 61) return (char)('0' + a - 52);

        return a == 62 ? '+' : '/';
    }

    private static int decode6(char c){

        int a = 0;

        if (c >= 'A' && c <= 'Z'){
            a = c - 'A';
        }

        if (c >= 'a' && c <= 'z'){
            a = c - 'a' + 26;
        }

        if (c >= '0' && c <= '9'){
            a = c - '0' + 52;
        }

        if (c == '+') {
            a = 62;
        }

        if (c == '/') {
            a = 63;
        }

        return a;
    }



}
