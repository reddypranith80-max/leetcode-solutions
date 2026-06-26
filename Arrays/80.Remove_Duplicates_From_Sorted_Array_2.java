class Solution {
    public int removeDuplicates(int[] nums) {
        int indexCount = 2;
    for (int i = 2; i < nums.length; i++) {
        if (nums[i] != nums[indexCount-2]) {
            nums[indexCount] = nums[i];
            indexCount++;
        }
    }
    return indexCount;
    }
}