class Solution {
    public int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;

        int off = 0;

        for (int cm = 1; cm < nums.length; cm++) {
            if (nums[cm] != nums[off]) {
                off++;
                nums[off] = nums[cm];
            }
        }

        return off + 1;
    }
}