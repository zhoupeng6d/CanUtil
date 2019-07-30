
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CanUtil {

	static Map<Integer,Integer> motorolaMaping = new ConcurrentHashMap<>();

	static {
		 int [] motorolaArrays = new int []{
	    		    7, 6, 5, 4, 3, 2, 1, 0,
	    		    15, 14, 13, 12, 11, 10, 9, 8,
	    		    23, 22, 21, 20, 19, 18, 17, 16,
	    		    31, 30, 29, 28, 27, 26, 25, 24,
	    		    39, 38, 37, 36, 35, 34, 33, 32,
	    		    47, 46, 45, 44, 43, 42, 41, 40,
	    		    55, 54, 53, 52, 51, 50, 49, 48,
	    		    63, 62, 61, 60, 59, 58, 57, 56
				  };

	    	  for (int i = 0; i < motorolaArrays.length; i++) {
	    		  motorolaMaping.put(motorolaArrays[i], i);
	    	  }
	}

	public synchronized static double getCanStateValue(String candata, int startBit, boolean isLsb, int signalLength, double resolution, double offset) {
		String binaryString = hexString2binaryString(candata);
		return transformationDecimal(binaryString, startBit, isLsb, signalLength,resolution,offset);
	}

    public static String hexString2binaryString(String hexString){
        if (hexString == null || hexString.length() % 2 != 0) {
        	return null;
        }
        String binaryString = "", tmp;
        for (int i = 0; i < hexString.length(); i++){
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            binaryString += tmp.substring(tmp.length() - 4);
        }
        return binaryString;
    }

	public static double transformationDecimal(String binaryString, int startBit, boolean isLsb, int signalLength, double resolution, double offset) {
		String[] binaryArray = binaryString.split("");
		Integer index = motorolaMaping.get(startBit);
		List<String> list = new ArrayList<String>();

		if (isLsb)
		{
			for (int j = index; j > (index - signalLength); j--) {
				list.add(binaryArray[j]);
			}
			Collections.reverse(list);
		}
		else
		{
			for (int j = index; j-index < signalLength; j++) {
				list.add(binaryArray[j]);
			}
		}

		StringBuffer sb = new StringBuffer();
		for (String s : list) {
			sb.append(s);
		}
		return Integer.parseInt(sb.toString(), 2) * resolution + offset ;

	}
	public static void main(String[] args) {
        double canStateValue = getCanStateValue("6e10273619440c2b", 7, false, 13, 0.1, 0);
		System.out.println(canStateValue);
		canStateValue = getCanStateValue("6e10273619440c2b", 11, true, 13, 0.1, 0);
        System.out.println(canStateValue);
    }
}
