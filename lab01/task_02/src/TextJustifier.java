import java.util.Arrays;

public class TextJustifier {

    public static String[] justifyText(String[] words, int maxWidth) {
        int arrLength = words.length;
        String[] finalArr = new String[arrLength];
        int currWordInd = 0, resultInd = 0;

        while (currWordInd < arrLength)
        {
            int[] arrIndexes = new int[arrLength];
            int arrInd = 0;
            StringBuilder newStr = new StringBuilder();
            for (int i = currWordInd; i < arrLength; i++){
                if (newStr.length() + words[i].length() <= maxWidth) {
                    newStr.append(words[i]);
                    newStr.append(" ");
                    arrIndexes[arrInd++] = newStr.lastIndexOf(" ");
                    currWordInd++;
                    if (currWordInd == arrLength)
                    {
                        arrIndexes[0] = arrIndexes[--arrInd];
                        arrInd = 1;
                    }
                    continue;
                }
                newStr.deleteCharAt(arrIndexes[--arrInd]);
                break;
            }
            int spacesLeftToInsert = maxWidth - newStr.length();
            if(spacesLeftToInsert < 0 )
            {
                newStr.deleteCharAt(newStr.length() - 1);
            }
            for (int j = 0; j < spacesLeftToInsert; j++)
            {
                if(arrInd == 0) {
                    newStr.insert(newStr.length(), ' ');
                }
                else {
                    int indToInsertSpace = j % arrInd;
                    newStr.insert(arrIndexes[indToInsertSpace], ' ');

                    //fix indexes of spaces
                    for (int l = indToInsertSpace; l < arrIndexes.length; l++) {
                        arrIndexes[l]++;
                    }
                }
            }
            finalArr[resultInd++] = newStr.toString();
        }
        String[] formattedArr = Arrays.copyOf(finalArr, resultInd);
        return formattedArr;
    }
}
