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