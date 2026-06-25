class Solution {
    public int[] sortedSquares(int[] nums) {
        int n = nums.length;
        int newarr[] = new int[n];
        for(int i=0;i<n;i++){
            newarr[i] = nums[i] * nums[i];
        }
        Arrays.sort(newarr);
        return newarr;
    }
}

// using two pointer approach

class Solution {
    public int[] sortedSquares(int[] nums) {
        int[] res = new int[nums.length];
        int left = 0;
        int right = nums.length - 1;

        for (int i = nums.length - 1; i >= 0; i--) {
            if (Math.abs(nums[left]) > Math.abs(nums[right])) {
                res[i] = nums[left] * nums[left];
                left++;
            } else {
                res[i] = nums[right] * nums[right];
                right--;
            }
        }
        return res;        
    }
}