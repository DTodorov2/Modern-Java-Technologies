import java.util.Arrays;

public class CourseScheduler {
    public static void merge(int[][] courses, int left, int mid, int right)
    {
        int len1 = mid - left + 1;
        int len2 = right - mid;

        int[][] firstArr = new int[len1][2];
        int[][] secArr = new int[len2][2];

        for (int i = 0; i < len1; i++)
        {
            firstArr[i] = Arrays.copyOf(courses[left + i], 2);
        }
        for (int i = 0; i < len2; i++)
        {
            secArr[i] = Arrays.copyOf(courses[i + mid + 1], 2);
        }

        int ind1 = 0, ind2 = 0, indOriginal = left;

        while (ind1 < len1 && ind2 < len2) {
            if (firstArr[ind1][0] <= secArr[ind2][0]) {
                courses[indOriginal++] = Arrays.copyOf(firstArr[ind1++], 2);
            }
            else {
                courses[indOriginal++] = Arrays.copyOf(secArr[ind2++], 2);
            }
        }

        while (ind1 < len1) {
            courses[indOriginal++] = Arrays.copyOf(firstArr[ind1++], 2);
        }

        while (ind2 < len2) {
            courses[indOriginal++] = Arrays.copyOf(secArr[ind2++], 2);
        }
    }

    public static void mergeSort(int[][] courses, int left, int right)
    {
        if (left >= right)
        {
            return;
        }

        int mid = left + (right - left) / 2;
        mergeSort(courses, left, mid);
        mergeSort(courses, mid + 1, right);
        merge(courses, left, mid, right);
    }

    public static int maxNonOverlappingCourses(int[][] courses) {
        int lenArr = courses.length;
        mergeSort(courses, 0, lenArr - 1);

        int maxNotOverlapping = 0;

        for (int i = 0; i < lenArr; i++)
        {
            int counterNotOverlapping = 1;
            int lastEndTime = courses[i][1];
            for (int j = i + 1; j < lenArr; j++)
            {
                if (lastEndTime <= courses[j][0])
                {
                    lastEndTime = courses[j][1];
                    counterNotOverlapping++;
                }
            }
            maxNotOverlapping = Math.max(counterNotOverlapping, maxNotOverlapping);
        }

        return maxNotOverlapping;
    }
}
